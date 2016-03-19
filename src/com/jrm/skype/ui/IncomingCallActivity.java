
package com.jrm.skype.ui;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.datebase.ConvTableActor;
import com.jrm.skype.datebase.ConvTableResourceHolder;
import com.jrm.skype.exception.ActivityManager;
import com.jrm.skype.service.SkypeService;
import com.jrm.skype.service.SkypeService.MyBinder;
import com.jrm.skype.util.CallHelper;
import com.jrm.skype.util.DateFormat;
import com.jrm.skype.util.SktApiActor;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import com.jrm.skype.util.Log;
import android.view.KeyEvent;

/**
 * @author andy.liu 
 * A Activity handle the pup notice of a call in
 */

public class IncomingCallActivity extends Activity {
    private IncomingCallViewHolder mIncomingCallViewHolder;

    private IncomingCallViewListenner mIncomingCallViewListenner;
    
    private Intent mIntent;
    
    private String mConvName;
    
    private int mCallType;
    
    private int mConvType;
    
    private boolean mIsVideoPlaying;
    
    private boolean mEnableVideo;
    
    private boolean mIsMissed;
    
    private String[] mCustomers;
    
    private ConvTableResourceHolder mConvTableResourceHolder;
    
    private DateFormat mDateFormat;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incomingcall);
        ActivityManager.getInstance().addActivity(this);
        
        mDateFormat = new DateFormat();
        mIncomingCallViewHolder = new IncomingCallViewHolder(this);
        mIncomingCallViewListenner = new IncomingCallViewListenner(this, mIncomingCallViewHolder);
        mIncomingCallViewHolder.findView();
        mIncomingCallViewListenner.setViewListenner();
        
        mIntent = getIntent();
        if (null == mIntent){
            return;
        }
        mIsVideoPlaying  = mIntent.getBooleanExtra(SKYPECONSTANT.SKYPESTRING.ISVIDEOPLAYING,false);
        mConvName = mIntent.getStringExtra(SKYPECONSTANT.SKYPESTRING.CONVNAME);
        mCallType = mIntent.getIntExtra(SKYPECONSTANT.SKYPESTRING.CALLTYPE,0);
        mCustomers = mIntent.getStringArrayExtra(SKYPECONSTANT.SKYPESTRING.CUSTOMERS);
        mIsMissed = true;
        mEnableVideo = false;
        
        mIncomingCallViewListenner.initVar(mIsVideoPlaying,mConvName,mCallType);
        
        registerReceiver(mConvStatusChangeReceiver,
                new IntentFilter(SKYPECONSTANT.SKYPEACTION.INCOMINGCALL_CONVSTATUS_CHANGE));
        bindService(new Intent(SKYPECONSTANT.SKYPEACTION.SKYPE_SERVICE), mConn, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v("IncomingCallActivity", "onServiceConnected");
            SkypeService skypeService = ((MyBinder) service).getService();
            if (null != skypeService){
                skypeService.setConvStatusAction(SKYPECONSTANT.SKYPEACTION.INCOMINGCALL_CONVSTATUS_CHANGE);
                if(skypeService.getConvStatus() == SKYPECONSTANT.CONVERSATIONSTATUS.RECENTLY_LIVE){
                    endCall();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        } 
    };
    /**
     * 
     * @param isMissed whether the conv stop by the opposite
     */
    public void setCallMissed(boolean isMissed){
        mIsMissed = isMissed;
    }
    
    public void setEnableVideo(boolean isVideoEnable){
        mEnableVideo = isVideoEnable;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                SktApiActor.leaveConversation();
                if(mIsVideoPlaying){
                    setCallMissed(true);
                }else{
                    setCallMissed(false);
                }
                return true;
            case KeyEvent.KEYCODE_ENTER:
                if (mIsVideoPlaying) {
                    SktApiActor.leaveConversation();
                    setCallMissed(true);
                    return true;
                }
            default:
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    	unregisterReceiver(mConvStatusChangeReceiver);
    	unbindService(mConn);
    	
        mIncomingCallViewHolder = null;
        mIncomingCallViewListenner = null;
        mConvName = null;
        mCustomers = null;
        mConvTableResourceHolder = null;
        mConn = null;
        ActivityManager.getInstance().pupActivity(this);
    }
 
    public void endCall(){
        if (mIsMissed){
            if ( SKYPECONSTANT.CALLTYPE.DIALOG == mCallType ){
                mConvType = SKYPECONSTANT.CONVTYPE.CALLMISSED;
            }else{
                mConvType = SKYPECONSTANT.CONVTYPE.CONFERENCEMISSED;
            }
        }else{
            if ( SKYPECONSTANT.CALLTYPE.DIALOG == mCallType ){
                mConvType = SKYPECONSTANT.CONVTYPE.CALLIN;
            }else{
                mConvType = SKYPECONSTANT.CONVTYPE.CONFERENCEIN;
            }
        }
        
        mConvTableResourceHolder = new ConvTableResourceHolder();
        mConvTableResourceHolder.setConvName(mConvName);
        mConvTableResourceHolder.setConvDate(mDateFormat.getFormatString(System.currentTimeMillis(), "yyyy-MM-dd"));
        mConvTableResourceHolder.setConvDuration("00:00:00");
        mConvTableResourceHolder.setConvType(mConvType + "");
        mConvTableResourceHolder.setConvTime(mDateFormat.getFormatString(System.currentTimeMillis(), "HH:mm:ss"));
        ConvTableActor.getInstance().insert(mConvTableResourceHolder);
        finish();
    }
       
    private BroadcastReceiver mConvStatusChangeReceiver = new BroadcastReceiver (){

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent)
                return;
            switch (intent.getIntExtra(SKYPECONSTANT.SKYPEACTION.INCOMINGCALL_CONVSTATUS_CHANGE, -1)) {
                case SKYPECONSTANT.CONVERSATIONSTATUS.IM_LIVE:
                    finish();
                    CallHelper.callIn(IncomingCallActivity.this,mConvName,mCallType,mCustomers,mEnableVideo);
                    break;
                case SKYPECONSTANT.CONVERSATIONSTATUS.RECENTLY_LIVE:
                case SKYPECONSTANT.CONVERSATIONSTATUS.OTHERS_ARE_LIVE:
                    //add the database
                    endCall();
                    break;
                default:
                    break;
            }
            
        }
    };
 
}
