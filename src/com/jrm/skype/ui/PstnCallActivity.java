package com.jrm.skype.ui;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.dialog.DialPadDialog;
import com.jrm.skype.dialog.MainDialog;
import com.jrm.skype.exception.ActivityManager;
import com.jrm.skype.service.SkypeService;
import com.jrm.skype.service.SkypeService.MyBinder;
import com.jrm.skype.util.SktApiActor;

import android.app.Activity;
import android.app.Dialog;
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

public class PstnCallActivity extends Activity {
    private final String TAG = "PstnCallActivity";
    
    private PstnCallViewHolder mPstnCallViewHolder;

    private PstnCallViewListenner mPstnCallViewListenner;
    
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pstn_call);
        ActivityManager.getInstance().addActivity(this);
        
        mIntent = getIntent();
        mPstnCallViewHolder = new PstnCallViewHolder(this);
        mPstnCallViewListenner = new PstnCallViewListenner(this, mPstnCallViewHolder);
        mPstnCallViewHolder.findView();
        mPstnCallViewListenner.setViewListenner();
        mPstnCallViewListenner.initVar(mIntent.getIntExtra(SKYPECONSTANT.SKYPESTRING.CONVTYPE, 0),
                mIntent.getStringExtra(SKYPECONSTANT.SKYPESTRING.CONVNAME));
        
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(SKYPECONSTANT.SKYPEACTION.INCALL_PSTN_CONVSTATUS_CHANGE);
        mIntentFilter.addAction(SKYPECONSTANT.SKYPEACTION.MIC_IN);
        mIntentFilter.addAction(SKYPECONSTANT.SKYPEACTION.MIC_OUT);
        
        registerReceiver(mReceiver, mIntentFilter);
        bindService(new Intent(SKYPECONSTANT.SKYPEACTION.SKYPE_SERVICE), mConn,
                Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(TAG, "onServiceConnected");
            SkypeService skypeService = ((MyBinder) service).getService();
            if (null != skypeService){
                skypeService.setConvStatusAction(SKYPECONSTANT.SKYPEACTION.INCALL_PSTN_CONVSTATUS_CHANGE);
                if(skypeService.getConvStatus() == SKYPECONSTANT.CONVERSATIONSTATUS.RECENTLY_LIVE){
                    mPstnCallViewListenner.callEnded();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        } 
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            SktApiActor.leaveConversation();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    
    @Override
    protected Dialog onCreateDialog(int id) {
        MainDialog dialog_dim;
        switch(id){
            case SKYPECONSTANT.USRACCOUNTDIALOG.DIALPAD:
                dialog_dim = new DialPadDialog(this, R.style.dialog_dim);
                dialog_dim.setDialogContentView(R.layout.dialpad);
                dialog_dim.setDialogSizeF(0.3850, 0.1800);
                dialog_dim.setDialogPositionF(-0.3515, 0.1720);
                dialog_dim.findView();
                dialog_dim.setViewListenner();
                return dialog_dim;
            default:
                return null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        unbindService(mConn);
        ActivityManager.getInstance().pupActivity(this);
    }
    
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent)
                return;
            String action = intent.getAction();
            
            if(SKYPECONSTANT.SKYPEACTION.INCALL_PSTN_CONVSTATUS_CHANGE.equals(action)){
                switch (intent.getIntExtra(SKYPECONSTANT.SKYPEACTION.INCALL_PSTN_CONVSTATUS_CHANGE, -1)) {
                    case SKYPECONSTANT.CONVERSATIONSTATUS.RECENTLY_LIVE:
                        mPstnCallViewListenner.callEnded();
                        break;
                    case SKYPECONSTANT.CONVERSATIONSTATUS.ON_HOLD_REMOTELY:// remote video will stop,local will hold
                        break;
                    case SKYPECONSTANT.CONVERSATIONSTATUS.IM_LIVE:
                        break;
                    default:
                        break;
                }
                
            }else if(SKYPECONSTANT.SKYPEACTION.MIC_IN.equals(action)){
                Log.v("SkypeCalViewListenner", "-------------------SKYPECONSTANT.SKYPEACTION.MIC_IN");
                SktApiActor.startAudioIn();
            }else if(SKYPECONSTANT.SKYPEACTION.MIC_OUT.equals(action)){
                Log.v("SkypeCalViewListenner", "-------------------SKYPECONSTANT.SKYPEACTION.MIC_OUT");
                SktApiActor.stopAudioIn();
            }

        }
    };
                
}
