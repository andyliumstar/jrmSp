
package com.jrm.skype.ui;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.exception.ActivityManager;
import com.jrm.skype.service.SkypeService;
import com.jrm.skype.service.SkypeService.MyBinder;
import com.jrm.skype.util.SktApiActor;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author andy.liu Signing Activity
 */

public class SigningActivity extends Activity {
    private final int LOG_DELAY_MS = 2000;

    private AnimationDrawable mSigningAnimation;

    private ImageView mSigningIv;

    private TextView mBackTv;

    private Button mCancelBtn;

    private Intent mIntent;

    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signing);
        ActivityManager.getInstance().addActivity(this);

        mIntent = new Intent();
        mHandler = new Handler();

        mSigningIv = (ImageView) findViewById(R.id.iv_signing_signing);
        mCancelBtn = (Button) findViewById(R.id.btn_signing_cancel);
        mBackTv = (TextView) findViewById(R.id.tv_signing_back);
        mSigningAnimation = (AnimationDrawable) mSigningIv.getBackground();

        mCancelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelLogin();
            }
        });

        mBackTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelLogin();
            }
        });

        registerReceiver(mOnlineStatusChangeReceiver, new IntentFilter(
                SKYPECONSTANT.SKYPEACTION.SIGNING_ONLINESTATUS_CHANGE));

        bindService(new Intent(SKYPECONSTANT.SKYPEACTION.SKYPE_SERVICE), mConn,
                Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SkypeService skypeService = ((MyBinder) service).getService();
            if (null != skypeService)
                skypeService.setOnlineStatusAction(SKYPECONSTANT.SKYPEACTION.SIGNING_ONLINESTATUS_CHANGE);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(mOnlineStatusChangeReceiver);
        unbindService(mConn);

        mHandler.removeCallbacks(logRunnable);
        mSigningAnimation = null;
        mSigningIv = null;
        mBackTv = null;
        mCancelBtn = null;
        mIntent = null;
        mConn = null;
        super.onDestroy();
        ActivityManager.getInstance().pupActivity(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            startLoadingAnimation();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                cancelLogin();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void stopLoadingAnimation() {
        if (null != mSigningAnimation)
            mSigningAnimation.stop();
    }

    public void startLoadingAnimation() {
        if (null != mSigningAnimation)
            mSigningAnimation.start();
    }

    public void cancelLogin() {
        SktApiActor.logout(false);
    }

    private BroadcastReceiver mOnlineStatusChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent)
                return;
            if (intent.getBooleanExtra(SKYPECONSTANT.SKYPEACTION.SIGNING_ONLINESTATUS_CHANGE, false)) {
                mIntent.setClass(SigningActivity.this, UsrAccountActivity.class);
            } else {
                mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.PASSWORD,
                        intent.getBooleanExtra(SKYPECONSTANT.SKYPESTRING.PASSWORD, false));
                mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.LOGINTIP,
                        intent.getStringExtra(SKYPECONSTANT.SKYPESTRING.LOGINTIP));
                mIntent.setClass(SigningActivity.this, SigninActivity.class);
            }

            // if fail to login, the logout callback will be called twice 
            if (null != mHandler) {
                mHandler.removeCallbacks(logRunnable);
                mHandler.postDelayed(logRunnable, LOG_DELAY_MS);
            }
        }
    };

    Runnable logRunnable = new Runnable() {

        @Override
        public void run() {
            stopLoadingAnimation();
            startActivity(mIntent);
            finish();
        }
    };

}
