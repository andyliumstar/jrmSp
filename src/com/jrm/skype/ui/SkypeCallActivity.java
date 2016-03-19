
package com.jrm.skype.ui;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.exception.ActivityManager;
import com.jrm.skype.service.SkypeService;
import com.jrm.skype.service.SkypeService.MyBinder;
import com.jrm.skype.util.SktApiActor;
import com.jrm.skype.util.TvSourceHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import com.jrm.skype.util.Log;
import com.skype.api.Video;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author andy.liu 
 * A Activity handle the call function
 */
public class SkypeCallActivity extends Activity  {
    private String TAG = "SkypeCalActivity";
    
    private SkypeCallViewHolder mSkypeCallViewHolder;

    private SkypeCallViewListenner mSkypeCalViewListenner;
    
    private SkypeService mSkypeService;
    
    private IntentFilter mIntentFilter;
    
    private Intent mIntent;
    
    private String mConvName;
    
    private boolean mHasDealRecVideo;
    
    private boolean mHasAgreeRecVideo;
    
    private Handler mHandler;
    
    private final int REMOTE_VIDEO_WAIT = 3000;
    
    private final int LOCAL_VIDEO_WAIT = 2000;

    private final static int TIME_TO_STOP_SEND_VIDEO = 10000;//wait time to stop send video, if other do not deal with it.
   
    private final static int TIME_TO_HIDE_MENU = 10000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skype_call);
        ActivityManager.getInstance().addActivity(this);
        
        mHandler = new Handler();
        mHasDealRecVideo = false;
        mHasAgreeRecVideo = false;
        mIntentFilter = new IntentFilter();

        mIntentFilter.addAction(SKYPECONSTANT.SKYPEACTION.INCALL_DIALOG_CONVSTATUS_CHANGE);
        mIntentFilter.addAction(SKYPECONSTANT.SKYPEACTION.VIDEO_STATUS_CHANGE);
        mIntentFilter.addAction(SKYPECONSTANT.SKYPEACTION.CAMERA_IN);
        mIntentFilter.addAction(SKYPECONSTANT.SKYPEACTION.CAMERA_OUT);
        mIntentFilter.addAction(SKYPECONSTANT.SKYPEACTION.MIC_IN);
        mIntentFilter.addAction(SKYPECONSTANT.SKYPEACTION.MIC_OUT);


        mIntent = getIntent();
        mSkypeCallViewHolder = new SkypeCallViewHolder(this);
        mSkypeCalViewListenner = new SkypeCallViewListenner(this, mSkypeCallViewHolder);

        mConvName = mIntent.getStringExtra(SKYPECONSTANT.SKYPESTRING.CONVNAME);
        mSkypeCallViewHolder.findView();
        mSkypeCalViewListenner.setViewListenner();
        mSkypeCalViewListenner.initVar(mIntent.getIntExtra(SKYPECONSTANT.SKYPESTRING.CONVTYPE, 0),
                mConvName, mIntent.getBooleanExtra(SKYPECONSTANT.SKYPESTRING.LOCALVIDEO, false));

        registerReceiver(mReceiver, mIntentFilter);
        bindService(new Intent(SKYPECONSTANT.SKYPEACTION.SKYPE_SERVICE), mConn,
                Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(TAG, "onServiceConnected");
            mSkypeService = ((MyBinder) service).getService();
            if (null != mSkypeService){
                mSkypeService.setConvStatusAction(SKYPECONSTANT.SKYPEACTION.INCALL_DIALOG_CONVSTATUS_CHANGE);
                if(mSkypeService.getConvStatus() == SKYPECONSTANT.CONVERSATIONSTATUS.RECENTLY_LIVE){
                    mSkypeCalViewListenner.callEnded();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        } 
    };

    /**
     * stop the local video in 10s if there is no response
     */
    public void postStopLocalVideo(){
        mHandler.postDelayed(stopLocalVideoRunnable, TIME_TO_STOP_SEND_VIDEO);
    }
    
    public void removeStopLocalVideoCallback(){
        mHandler.removeCallbacks(stopLocalVideoRunnable);
    }
    
    /**
     * hide fullscreen menu in 10s if there is no response
     */
    public void postHideMenu(){
        mHandler.postDelayed(hideMenuRunnable, TIME_TO_HIDE_MENU);
    }
    
    public void removeHideMenuCallback(){
        mHandler.removeCallbacks(hideMenuRunnable);
    }
   
   
    public void dealRecVideo(){
        if(mHasDealRecVideo){
            if(mHasAgreeRecVideo){
                SktApiActor.startRecvVideo();
            }else{
                SktApiActor.stopRecvVideo();
            }
        }else{
            mHasDealRecVideo = true;
            
            int videoRecvPolicy = SktApiActor.getVideoRecvPolicy();
            /*
             * videoRecvPolicy 0, recv everyone , 1 only my friends, 2 none, -1 failed
             */
            if ((0 == videoRecvPolicy) || (1 == videoRecvPolicy && SktApiActor.isMyBuddy(mConvName))) {
                SktApiActor.startRecvVideo();
                mHasAgreeRecVideo = true;
            }else{
                AlertDialog.Builder builder = new Builder(SkypeCallActivity.this);
                builder.setMessage(R.string.is_rec_video);
                builder.setTitle(R.string.rec_video);
                builder.setPositiveButton(R.string.Yes,new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SktApiActor.startRecvVideo();
                        mHasAgreeRecVideo = true;
                        dialog.dismiss();
                    }
                }); 
                builder.setNegativeButton(R.string.No,  new DialogInterface.OnClickListener() {
                   
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       SktApiActor.stopRecvVideo();
                       mHasAgreeRecVideo = false;
                       dialog.dismiss();              
                   }
                }); 
                builder.create().show();   
             }
        }
    }
    
    
    @Override
    protected void onPause() {
//        if(!TvSourceHelper.isSourceStorage()){
//            SktApiActor.leaveConversation();
//        }
        super.onPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null != mSkypeCalViewListenner && mSkypeCalViewListenner.isFullScreen()){
            removeHideMenuCallback();
            mSkypeCallViewHolder.mVHFullScreenBtnsLil.setVisibility(View.VISIBLE);
            postHideMenu();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            SktApiActor.leaveConversation();
            return true;
        }else{
            if(null != mSkypeCalViewListenner && mSkypeCalViewListenner.isFullScreen()){
                removeHideMenuCallback();
                mSkypeCallViewHolder.mVHFullScreenBtnsLil.setVisibility(View.VISIBLE);
                postHideMenu();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            mSkypeCalViewListenner.setSurface();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        unbindService(mConn);
        mHandler.removeCallbacks(remoteVideoRunningRunnable);
        mHandler.removeCallbacks(localVideoRunningRunnable);
        removeStopLocalVideoCallback();
        mSkypeCallViewHolder = null;
        mSkypeCalViewListenner = null;
        TvSourceHelper.resetInputSource();
        ActivityManager.getInstance().pupActivity(this);
        
        System.gc();
    }

    /**
     * four action: videostautus convstatus camera in/out
     */
    
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            
            if(SKYPECONSTANT.SKYPEACTION.INCALL_DIALOG_CONVSTATUS_CHANGE.equals(action)){
                switch (intent.getIntExtra(SKYPECONSTANT.SKYPEACTION.INCALL_DIALOG_CONVSTATUS_CHANGE, -1)) {
                    case SKYPECONSTANT.CONVERSATIONSTATUS.OTHERS_ARE_LIVE:
                    case SKYPECONSTANT.CONVERSATIONSTATUS.RECENTLY_LIVE:
                        //call the jni because the video may not stop when conv is end
                        SktApiActor.stopRemoteVideoJni();
                        SktApiActor.stopLocalVideoJni();
                        mSkypeCalViewListenner.callEnded();
                        break;
                    case SKYPECONSTANT.CONVERSATIONSTATUS.ON_HOLD_REMOTELY://remote video will stop,local will hold
                        mSkypeCalViewListenner.otherHold(); 
                        break;
                    case SKYPECONSTANT.CONVERSATIONSTATUS.IM_LIVE:
                        mSkypeCalViewListenner.otherResume();
                        break;
                    default:
                        break;
                }
                
            }else if(SKYPECONSTANT.SKYPEACTION.VIDEO_STATUS_CHANGE.equals(action)){
                String actName = intent.getStringExtra(SKYPECONSTANT.SKYPESTRING.VIDEO_ACTNAME);
                if("remote".equals(actName)){
                    switch (intent.getIntExtra(SKYPECONSTANT.SKYPEACTION.VIDEO_STATUS_CHANGE, -1)) {
                        case SKYPECONSTANT.VIDEOSTATUS.AVAILABLE:
                            if (mSkypeCalViewListenner.getSurfaceAvailable()){// && !mSkypeCalViewListenner.getCallHold()){
                                dealRecVideo();  
                            }
                            break;
                        case SKYPECONSTANT.VIDEOSTATUS.RUNNING:
                            mSkypeCalViewListenner.setRemoteVideo(true);
                            mSkypeCalViewListenner.remoteVideoWaiting();
                            mHandler.postDelayed(remoteVideoRunningRunnable, REMOTE_VIDEO_WAIT);
                            break;
                        case SKYPECONSTANT.VIDEOSTATUS.PAUSED:
                            mHandler.removeCallbacks(remoteVideoRunningRunnable);
                            mSkypeCalViewListenner.remoteVideoStop();
                            break;
                        case SKYPECONSTANT.VIDEOSTATUS.STOPPED:
                            SktApiActor.stopRemoteVideoJni();//we prevent the api calling the stop, so we must call jni to stop
                            mSkypeCalViewListenner.setRemoteVideo(false);
                            mHandler.removeCallbacks(remoteVideoRunningRunnable);
                            mSkypeCalViewListenner.remoteVideoStop();
                            break;
                        default:
                            break;
                    }
                    
                }else if("local".equals(actName)){
                    switch (intent.getIntExtra(SKYPECONSTANT.SKYPEACTION.VIDEO_STATUS_CHANGE, -1)) {
                        case SKYPECONSTANT.VIDEOSTATUS.AVAILABLE:
                            //when other reject the video, we also get this callback
                            mSkypeCalViewListenner.otherRejectVideo();                         
                            break;
                        case SKYPECONSTANT.VIDEOSTATUS.RUNNING: 
                            mSkypeCalViewListenner.setLocalVideo(true);
                            removeStopLocalVideoCallback();
                            mHandler.postDelayed(localVideoRunningRunnable, LOCAL_VIDEO_WAIT);
                            break;
                            // for local video ,we stop change its status when hold. and will update when we hold it, 
                            // and if remote hold, ON_HOLD_REMOTELY will update the local video
                        case SKYPECONSTANT.VIDEOSTATUS.PAUSED:
                            mHandler.removeCallbacks(localVideoRunningRunnable);
                            mSkypeCalViewListenner.localVideoStop();
                            break;
                        case SKYPECONSTANT.VIDEOSTATUS.STOPPED:
                            mSkypeCalViewListenner.setLocalVideo(false);
                           // mSkypeCalViewListenner.updateLocalVideo(false);
                            mHandler.removeCallbacks(localVideoRunningRunnable);
                            mSkypeCalViewListenner.localVideoStop();
                            break;
                        default:
                            break;
                    }
                }
                
            }else if(SKYPECONSTANT.SKYPEACTION.CAMERA_IN.equals(action)){
                Log.v("SkypeCallActivity", "-------------------SKYPECONSTANT.SKYPEACTION.CAMERA_IN");
                mSkypeCalViewListenner.cameraIn();
            }else if(SKYPECONSTANT.SKYPEACTION.CAMERA_OUT.equals(action)){
                Log.v("SkypeCallActivity", "--------------------SKYPECONSTANT.SKYPEACTION.CAMERA_OUT");
                mSkypeCalViewListenner.cameraOut();
            }else if(SKYPECONSTANT.SKYPEACTION.MIC_IN.equals(action)){
                Log.v("SkypeCallActivity", "-------------------SKYPECONSTANT.SKYPEACTION.MIC_IN");
                SktApiActor.stopAudioIn();
                SktApiActor.startAudioIn();
            }else if(SKYPECONSTANT.SKYPEACTION.MIC_OUT.equals(action)){
                Log.v("SkypeCallActivity", "-------------------SKYPECONSTANT.SKYPEACTION.MIC_OUT");
                SktApiActor.stopAudioIn();
            }
        }
    };
    
    Runnable remoteVideoRunningRunnable = new Runnable() {

        @Override
        public void run() {
            if (null != mSkypeCalViewListenner){
                mSkypeCalViewListenner.remoteVideoRunning();
            }
        }
    };

    Runnable localVideoRunningRunnable = new Runnable() {

        @Override
        public void run() {
            if (null != mSkypeCalViewListenner){
                mSkypeCalViewListenner.localVideoRunning();
            }
        }
    };
    
    Runnable stopLocalVideoRunnable = new Runnable() {

        @Override
        public void run() {
            if (null != mSkypeCalViewListenner){
                if(Video.STATUS.STARTING == SktApiActor.getVideoStatus(1)){
                    SktApiActor.stopSendVideo();  
                }
            }
        }
    };
    
    Runnable hideMenuRunnable = new Runnable() {

        @Override
        public void run() {
            if (null != mSkypeCallViewHolder){
                mSkypeCallViewHolder.mVHFullScreenBtnsLil.setVisibility(View.GONE);
            }
        }
    };
                    
}
