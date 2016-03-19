package com.jrm.skype.ui;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.exception.ActivityManager;
import com.jrm.skype.service.SkypeService;
import com.jrm.skype.service.SkypeService.MyBinder;
import com.jrm.skype.util.SktApiActor;
import com.jrm.skype.util.SkypePrefrences;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import com.jrm.skype.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CreatingActivity extends Activity {
    private final String TAG = "CreatingActivity";
    
    private final int LOGOUT_DELAY_MS = 2000;
    
    private AnimationDrawable mCreatingAnimation;

    private ImageView mCreatingIv;

    private TextView mBackTv;

    private Button mCancelBtn;

    private String mFullNameStr;

    private Intent mIntent ;
    
    private Handler mHandler;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creating);
        ActivityManager.getInstance().addActivity(this);
        
        mIntent = new Intent();
        mHandler = new Handler();
        
        mFullNameStr = getIntent().getStringExtra(SKYPECONSTANT.SKYPESTRING.CREATEFULLNAME);

        mCreatingIv = (ImageView) findViewById(R.id.iv_creating_creating);
        mCancelBtn = (Button) findViewById(R.id.btn_creating_cancel);
        mBackTv = (TextView) findViewById(R.id.tv_creating_back);

        mCreatingIv.setBackgroundResource(R.drawable.signinganimation);
        mCreatingAnimation = (AnimationDrawable) mCreatingIv.getBackground();

        mCancelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelCreate();
            }
        });

        mBackTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelCreate();
            }
        });
        
        registerReceiver(mOnlineStatusChangeReceiver, new IntentFilter(SKYPECONSTANT.SKYPEACTION.CREATING_ONLINESTATUS_CHANGE));
        
        bindService(new Intent(SKYPECONSTANT.SKYPEACTION.SKYPE_SERVICE), mConn, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(TAG, "onServiceConnected");
            SkypeService skypeService = ((MyBinder) service).getService();
            if (null != skypeService)
                skypeService.setOnlineStatusAction(SKYPECONSTANT.SKYPEACTION.CREATING_ONLINESTATUS_CHANGE);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        } 
    };
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mOnlineStatusChangeReceiver);
        unbindService(mConn);
        
        mCreatingAnimation = null;
        mCreatingIv = null;
        mBackTv = null;
        mCancelBtn = null;
        mFullNameStr = null;
        mIntent = null;
        mConn = null;
        ActivityManager.getInstance().pupActivity(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        startLoadingAnimation();
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                cancelCreate();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void stopLoadingAnimation() {
        if ( null != mCreatingAnimation)
            mCreatingAnimation.stop();
    }
    
    public void startLoadingAnimation() {
        if ( null != mCreatingAnimation)
            mCreatingAnimation.start();
    }
    
    public void cancelCreate(){
        // set the create info readable
        SkypePrefrences.setCreateOut(CreatingActivity.this, true);
        SktApiActor.cancelCreateAccount();
    }
 
    private BroadcastReceiver mOnlineStatusChangeReceiver = new BroadcastReceiver (){

        @Override
        public void onReceive(Context context, Intent intent) {
            
            if (null == intent)
                return;
           
            if (intent.getBooleanExtra(SKYPECONSTANT.SKYPEACTION.CREATING_ONLINESTATUS_CHANGE,
                    false)) {
                if (null != mFullNameStr)
                    SktApiActor.setFullName(mFullNameStr);
                mIntent.setClass(CreatingActivity.this, UsrAccountActivity.class);
                SkypePrefrences.recordSignInfo(CreatingActivity.this,
                        SkypePrefrences.getCreateSkypeName(CreatingActivity.this),
                        SkypePrefrences.getCreatePassword(CreatingActivity.this));
            } else {
                mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.CREATETIP,
                        intent.getStringExtra(SKYPECONSTANT.SKYPESTRING.LOGINTIP));
                mIntent.setClass(CreatingActivity.this, CreateNewAccountActivity.class);
                SkypePrefrences.setCreateOut(CreatingActivity.this, true);
            }
            
            if (null != mHandler){
                mHandler.removeCallbacks(logoutRunnable);
                mHandler.postDelayed(logoutRunnable, LOGOUT_DELAY_MS);
            }
             
        }
    };
    
    Runnable logoutRunnable = new Runnable() {

        @Override
        public void run() {
            stopLoadingAnimation();
            startActivity(mIntent);
            finish();
        }
    };

}

