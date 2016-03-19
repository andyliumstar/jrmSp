package com.jrm.skype.ui;

import java.util.ArrayList;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.exception.ActivityManager;
import com.jrm.skype.service.SkypeService;
import com.jrm.skype.service.SkypeService.MyBinder;
import com.jrm.skype.util.SktApiActor;
import com.jrm.skype.util.TvSourceHelper;

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

public class ConferenceActivity extends Activity {
	private static final String TAG = "ConferenceActivity";
    
    private ConferenceViewHolder mConferenceViewHolder;
    
    private ConferenceViewListenner mConferenceViewListenner;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conference_call);
        ActivityManager.getInstance().addActivity(this);
        
        mConferenceViewHolder = new ConferenceViewHolder(this);
        mConferenceViewListenner = new ConferenceViewListenner(this, mConferenceViewHolder,
                getIntent().getStringExtra(SKYPECONSTANT.SKYPESTRING.CONVNAME),
                getIntent().getStringArrayListExtra(SKYPECONSTANT.SKYPESTRING.CUSTOMERS),
                getIntent().getIntExtra(SKYPECONSTANT.SKYPESTRING.CONVTYPE, 0));
        
        mConferenceViewHolder.findView();
        mConferenceViewListenner.setViewListenner();
        mConferenceViewListenner.initVar();
        
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(SKYPECONSTANT.SKYPEACTION.INCALL_CONFERENCE_CONVSTATUS_CHANGE);
        mIntentFilter.addAction(SKYPECONSTANT.SKYPEACTION.MIC_IN);
        mIntentFilter.addAction(SKYPECONSTANT.SKYPEACTION.MIC_OUT);
        
        registerReceiver(mConvStatusChangeReceiver,mIntentFilter);
        bindService(new Intent(SKYPECONSTANT.SKYPEACTION.SKYPE_SERVICE), mConn, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(TAG, "onServiceConnected");
            SkypeService skypeService = ((MyBinder) service).getService();
            if (null != skypeService) {
                skypeService
                        .setConvStatusAction(SKYPECONSTANT.SKYPEACTION.INCALL_CONFERENCE_CONVSTATUS_CHANGE);
                if (skypeService.getConvStatus() == SKYPECONSTANT.CONVERSATIONSTATUS.RECENTLY_LIVE) {
                    OnCallEnd();
                    finish();
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
            OnCallEnd();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        unregisterReceiver(mConvStatusChangeReceiver);
        unbindService(mConn);
        
        mConferenceViewHolder = null;
        mConferenceViewListenner = null;
        TvSourceHelper.resetInputSource();
        ActivityManager.getInstance().pupActivity(this);
    }

    public void OnCallEnd(){
        if (!this.isFinishing()){
            mConferenceViewListenner.callEnded();
        }
    }
    
    private BroadcastReceiver mConvStatusChangeReceiver = new BroadcastReceiver (){

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent)
                return;
            String action = intent.getAction();
            
            if(SKYPECONSTANT.SKYPEACTION.INCALL_CONFERENCE_CONVSTATUS_CHANGE.equals(action)){
                switch (intent.getIntExtra(SKYPECONSTANT.SKYPEACTION.INCALL_CONFERENCE_CONVSTATUS_CHANGE, -1)) {
                    case SKYPECONSTANT.CONVERSATIONSTATUS.RECENTLY_LIVE:
                    case SKYPECONSTANT.CONVERSATIONSTATUS.OTHERS_ARE_LIVE:
                        OnCallEnd();
                        break;
                    case SKYPECONSTANT.CONVERSATIONSTATUS.PARTICIPANTSCHANGE:
                        ArrayList<String> cts = new ArrayList<String>();
                        String [] customers = intent.getStringArrayExtra(SKYPECONSTANT.SKYPESTRING.CUSTOMERS);
                        for (String customer :customers )
                            cts.add(customer);
                        mConferenceViewListenner.changeConvCustomers(cts);
                        break;
                    default:
                        break;
                }
                
            }else if(SKYPECONSTANT.SKYPEACTION.MIC_IN.equals(action)){
                Log.v("ConferenceActivity", "-------------------SKYPECONSTANT.SKYPEACTION.MIC_IN");
                SktApiActor.stopAudioIn();
                SktApiActor.startAudioIn();
            }else if(SKYPECONSTANT.SKYPEACTION.MIC_OUT.equals(action)){
                Log.v("ConferenceActivity", "-------------------SKYPECONSTANT.SKYPEACTION.MIC_OUT");
                SktApiActor.stopAudioIn();
            }
        }
    };
}
