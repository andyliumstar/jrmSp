
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
import android.widget.ImageView;

/**
 * @author andy.liu the loading activity ,depend on the setting ,it will start different Activity
 */
public class LoadingSkypeActivity extends Activity {
    private final String TAG = "LoadingSkypeActivity";

    private final int LOG_DELAY_MS = 2000;

    private AnimationDrawable mLoadingAnimation;

    private ImageView mLoadingIv;

    private Intent mIntent;

    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_loading);
        ActivityManager.getInstance().addActivity(this);

        mLoadingIv = (ImageView) findViewById(R.id.iv_loading);
        mLoadingAnimation = (AnimationDrawable) mLoadingIv.getBackground();
        mIntent = new Intent();
        mHandler = new Handler();

        registerReceiver(mSkypeKitConnectChangeReceiver, new IntentFilter(
                SKYPECONSTANT.SKYPEACTION.SKYPEKITCONNECT_CHANGE));
        registerReceiver(mOnlineStatusChangeReceiver, new IntentFilter(
                SKYPECONSTANT.SKYPEACTION.LOADING_ONLINESTATUS_CHANGE));

        startService(new Intent(SKYPECONSTANT.SKYPEACTION.SKYPE_SERVICE));
        startService(new Intent(SKYPECONSTANT.SKYPEACTION.SKYPE_EXCEPTION_SERVICE));
        bindService(new Intent(SKYPECONSTANT.SKYPEACTION.SKYPE_SERVICE), mConn,
                Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(TAG, "onServiceConnected");
            SkypeService skypeService = ((MyBinder) service).getService();
            if (null != skypeService)
                skypeService.setOnlineStatusAction(SKYPECONSTANT.SKYPEACTION.LOADING_ONLINESTATUS_CHANGE);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            startLoadingAnimation();
        } else {
            stopLoadingAnimation();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                exitApk();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exitApk() {
        stopService(new Intent(SKYPECONSTANT.SKYPEACTION.SKYPE_SERVICE));
        stopService(new Intent(SKYPECONSTANT.SKYPEACTION.SKYPE_EXCEPTION_SERVICE));
        finish();
        System.exit(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mSkypeKitConnectChangeReceiver);
        unregisterReceiver(mOnlineStatusChangeReceiver);
        unbindService(mConn);
        mLoadingAnimation = null;
        mLoadingIv = null;
        mConn = null;
        ActivityManager.getInstance().pupActivity(this);
    }

    public void startLoadingAnimation() {
        if (null != mLoadingAnimation) {
            mLoadingAnimation.start();
        }
    }

    public void stopLoadingAnimation() {
        if (null != mLoadingAnimation)
            mLoadingAnimation.stop();
    }

    private BroadcastReceiver mSkypeKitConnectChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent)
                return;
            if (intent.getBooleanExtra(SKYPECONSTANT.SKYPEACTION.SKYPEKITCONNECT_CHANGE, false)) {
                Intent mIntent = new Intent();

                if (SkypePrefrences.isFirstStart(context)) {// first start
                    mIntent.setClass(LoadingSkypeActivity.this, WelcomeActivity.class);
                } else {// not first start
                    if (SktApiActor.isLoggedIn())// have already sign in
                    {
                        mIntent.setClass(LoadingSkypeActivity.this, UsrAccountActivity.class);
                    } else {// not sign in
                        mIntent.setClass(LoadingSkypeActivity.this, SigninActivity.class);
                    }// ---->have already sign in
                }// ---->first start

                stopLoadingAnimation();
                LoadingSkypeActivity.this.finish();
                startActivity(mIntent);
            }// -------->SKYPEKITCONNECT_CHANGE
        }
    };

    private BroadcastReceiver mOnlineStatusChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent)
                return;
            if (intent.getBooleanExtra(SKYPECONSTANT.SKYPEACTION.LOADING_ONLINESTATUS_CHANGE, false)) {
                mIntent.setClass(LoadingSkypeActivity.this, UsrAccountActivity.class);
            } else {
                mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.PASSWORD,
                        intent.getBooleanExtra(SKYPECONSTANT.SKYPESTRING.PASSWORD, false));
                mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.LOGINTIP,
                        intent.getStringExtra(SKYPECONSTANT.SKYPESTRING.LOGINTIP));
                mIntent.setClass(LoadingSkypeActivity.this, SigninActivity.class);
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
