
package com.jrm.skype.ui;

import com.jrm.skype.api.SktUtils;
import com.jrm.skype.datebase.ConvTableActor;
import com.jrm.skype.datebase.ConvTableResourceHolder;
import com.jrm.skype.ui.SkypeCallActivity;
import com.jrm.skype.util.DateFormat;
import com.jrm.skype.util.LayoutParamsHelper;
import com.jrm.skype.util.SktApiActor;
import com.skype.api.Video;
import android.R.color;
import com.jrm.skype.util.Log;

import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * @author andy.liu 
 * the ViewListenner of SkypeCallActivity
 */
public class SkypeCallViewListenner implements OnClickListener {
    private final static String TAG = "SkypeCallViewListenner";
    
    private SkypeCallActivity mVLSkypeCallActivity;

    private SkypeCallViewHolder mVLSkypeCallViewHolder;

    private boolean mVLLocalVideoStarted;//whether sending video now
    
    private boolean mVLRemoteVideoStarted;//whether receiving video now
    
    private boolean mVLSurfaceAvailable;

    private boolean mVLCallHolded;

    private boolean mVLMuted;

    private boolean mVLFullScreened;
    
    private String mVLConvName;

    private int mVLConvType;

    /*
     * database part: 
     * mVLConvTableActor : the executer of call table , mainly execute insert function here
     * mVLConvTableResourceHolder :the resource to insert
     */
    private ConvTableResourceHolder mVLConvTableResourceHolder;
    
    private DateFormat mVLDateFormat;

    private String mVLConvTime;

    private String mVLConvDate;
 
    private LayoutParamsHelper mVLLayoutParamsHelper;

    public SkypeCallViewListenner(SkypeCallActivity inCallActivity,
            SkypeCallViewHolder inCallViewHolder ) {
        this.mVLSkypeCallActivity = inCallActivity;
        this.mVLSkypeCallViewHolder = inCallViewHolder;
        
        mVLDateFormat = new DateFormat();
        mVLLayoutParamsHelper = new LayoutParamsHelper(mVLSkypeCallActivity);
        
        mVLLocalVideoStarted = false;
        mVLRemoteVideoStarted = false;
        mVLCallHolded = false;
        mVLMuted = false;
        mVLFullScreened = false;
    }

    public void initVar(int callType,String callContact,boolean localVideo) {
        this.mVLConvType = callType;
        this.mVLConvName = callContact;
        mVLLocalVideoStarted = localVideo;
        
        mVLConvTime = mVLDateFormat.getFormatString(System.currentTimeMillis(), "HH:mm:ss");
        mVLConvDate = mVLDateFormat.getFormatString(System.currentTimeMillis(), "yyyy-MM-dd");
        mVLSkypeCallViewHolder.mVHCallDurationTv.startRecordTime();
        
        //make the video loading progress bar gone
        mVLSkypeCallViewHolder.mVHLocalNormalPb.setVisibility(View.GONE);
        mVLSkypeCallViewHolder.mVHLocalFullPb.setVisibility(View.GONE);
        
        mVLSkypeCallViewHolder.mVHRemoteNormalPb.setVisibility(View.GONE);
        mVLSkypeCallViewHolder.mVHRemoteFullPb.setVisibility(View.GONE);
        
        //init the local video view
        if (!mVLLocalVideoStarted){
            mVLSkypeCallViewHolder.mVHLocalVideoTv.setText(R.string.start_video);
            mVLSkypeCallViewHolder.mVHLocalVideoBtn.setBackgroundResource(R.drawable.button_start_video);
            mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setBackgroundResource(R.drawable.button_start_video);
            mVLSkypeCallViewHolder.mVHLocalVideoSv.setBackgroundResource(R.drawable.skype_img_call_control_local_default_16_9);
        }else{
            mVLSkypeCallViewHolder.mVHLocalVideoTv.setText(R.string.stop_video);
            mVLSkypeCallViewHolder.mVHLocalVideoBtn.setBackgroundResource(R.drawable.button_stop_video);
            mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setBackgroundResource(R.drawable.button_stop_video);
            mVLSkypeCallViewHolder.mVHLocalVideoSv.setBackgroundResource(color.background_dark);
            mVLSkypeCallViewHolder.mVHLocalNormalPb.setVisibility(View.VISIBLE);  
            
            mVLSkypeCallViewHolder.mVHLocalVideoBtn.setEnabled(false);
            mVLSkypeCallViewHolder.mVHLocalVideoBtn.setFocusable(false);
            
            mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setEnabled(false);
            mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setFocusable(false);
        }
        
        mVLSkypeCallViewHolder.mVHHoldTv.setText(R.string.hold);
        mVLSkypeCallViewHolder.mVHHoldBtn.setBackgroundResource(R.drawable.button_hold_call);
        mVLSkypeCallViewHolder.mVHFullScreenHoldBtn.setBackgroundResource(R.drawable.button_hold_call);
        
        mVLSkypeCallViewHolder.mVHMuteTv.setText(R.string.mute);
        mVLSkypeCallViewHolder.mVHMuteBtn.setBackgroundResource(R.drawable.button_mute_call);
        mVLSkypeCallViewHolder.mVHFullScreenMuteBtn.setBackgroundResource(R.drawable.button_mute_call);

        //set the LayoutParams for surface view
        mVLSkypeCallViewHolder.mVHRemoteVideoSv.setLayoutParams(mVLLayoutParamsHelper.getNSRemoteVideoLp());
        mVLSkypeCallViewHolder.mVHLocalVideoSv.setLayoutParams(mVLLayoutParamsHelper.getNSLocalVideoLp());
        
        mVLSkypeCallViewHolder.mVHRemoteVideoSv
                .setBackgroundResource(R.drawable.skype_img_call_control_local_default_16_9);
        
        Log.v(TAG,"--------SktApiActor---------->");
    }

    public void setSurface() {
        SktApiActor.setPlayerSurface(mVLSkypeCallViewHolder.mVHRemoteVideoSv.getHolder().getSurface());
        SktApiActor.setPreviewSurface(mVLSkypeCallViewHolder.mVHLocalVideoSv.getHolder().getSurface());
        SktApiActor.updateSurface();
        mVLSkypeCallViewHolder.mVHUsrNameTv.setText(SktApiActor.getFullName());
        mVLSkypeCallViewHolder.mVHContactNameTv.setText(SktApiActor.getContactDisplayname(mVLConvName));
        
        mVLSurfaceAvailable = true;

        if (mVLLocalVideoStarted) {
            SktApiActor.startSendVideo();
            mVLSkypeCallActivity.postStopLocalVideo();
        }
        
        if (Video.STATUS.AVAILABLE == SktApiActor.getVideoStatus(2)){
            mVLSkypeCallActivity.dealRecVideo();
        }
        
        mVLSkypeCallViewHolder.mVHRemoteVideoSv.getHolder().addCallback(new Callback() {
            
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                SktApiActor.leaveConversation();
            }
            
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }
            
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });
    }
    
    public void setRemoteVideo(boolean isRecRemoteVideo){
        mVLRemoteVideoStarted = isRecRemoteVideo;
    }
    
    public void setLocalVideo(boolean isSendVideo){
        mVLLocalVideoStarted = isSendVideo;
    }

    public boolean getSurfaceAvailable(){
        return mVLSurfaceAvailable;
    }

    public boolean getCallHold(){
        return mVLCallHolded;
    }
    
    public boolean isFullScreen(){
        return mVLFullScreened;
    }
    
    public void setViewListenner() {
        mVLSkypeCallViewHolder.mVHLocalVideoBtn.setOnClickListener(this);
        mVLSkypeCallViewHolder.mVHHoldBtn.setOnClickListener(this);
        mVLSkypeCallViewHolder.mVHMuteBtn.setOnClickListener(this);
        mVLSkypeCallViewHolder.mVHFullScreenBtn.setOnClickListener(this);
        mVLSkypeCallViewHolder.mVHEndCallBtn.setOnClickListener(this);

        mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setOnClickListener(this);
        mVLSkypeCallViewHolder.mVHFullScreenHoldBtn.setOnClickListener(this);
        mVLSkypeCallViewHolder.mVHFullScreenMuteBtn.setOnClickListener(this);
        mVLSkypeCallViewHolder.mVHNormalScreenBtn.setOnClickListener(this);
        mVLSkypeCallViewHolder.mVHFullScreenEndCallBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        
        if(mVLFullScreened){
            mVLSkypeCallActivity.removeHideMenuCallback();
            mVLSkypeCallViewHolder.mVHFullScreenBtnsLil.setVisibility(View.VISIBLE);
            mVLSkypeCallActivity.postHideMenu();
        }
        
        switch (v.getId()) {
            case R.id.btn_incall_stop_start_video:
            case R.id.btn_incall_fullSrceen_stop_start_video:
                startStopVideo();
                break;

            case R.id.btn_incall_hold_resume:
            case R.id.btn_incall_fullSrceen_hold_resume:
                holdResume();
                break;

            case R.id.btn_incall_mute:
            case R.id.btn_incall_fullSrceen_mute:
                muteUnmute();
                break;

            case R.id.btn_incall_full_screen:
            case R.id.btn_incall_fullSrceen_normal_screen:
                normalFullScreen();
                break;

            case R.id.btn_incall_end_call:
            case R.id.btn_incall_fullSrceen_end_call:
                SktApiActor.leaveConversation();
                break;
            default:
                break;
        }
    }
    /*
     * button click
     * function-------------------------------------------------------------------------------------------->
     */
    /**
     *  stop or start VideoBtnClick
     */
    private synchronized void startStopVideo() {
        if (!SktUtils.cameraExists()) {
            Toast.makeText(mVLSkypeCallActivity, R.string.no_camera,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mVLLocalVideoStarted = !mVLLocalVideoStarted;
        if (mVLLocalVideoStarted) {
            SktApiActor.startSendVideo();
            mVLSkypeCallActivity.postStopLocalVideo();
            localVideoWaiting();
        } else {
            SktApiActor.stopSendVideo();
        }
    }

    /**
     *  hold or resume btnClick
     */
    public void holdResume() {
        mVLCallHolded = !mVLCallHolded;
        SktApiActor.holdResumeConversation(mVLCallHolded);
        
        if (mVLCallHolded){
            mVLSkypeCallViewHolder.mVHFullScreenHoldBtn.setBackgroundResource(R.drawable.button_resume_call);
            mVLSkypeCallViewHolder.mVHHoldBtn.setBackgroundResource(R.drawable.button_resume_call);
            mVLSkypeCallViewHolder.mVHHoldTv.setText(R.string.resume);
            
            mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setEnabled(false);
            mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setFocusable(false);
            mVLSkypeCallViewHolder.mVHLocalVideoBtn.setEnabled(false);
            mVLSkypeCallViewHolder.mVHLocalVideoBtn.setFocusable(false);
            
            mVLSkypeCallViewHolder.mVHPupPauseIv.setVisibility(View.VISIBLE);
            localVideoStop();//the call back also need do this,here for sometimes that the callback is miss because the loal video is not running and hold again
        }else{
            mVLSkypeCallViewHolder.mVHFullScreenHoldBtn.setBackgroundResource(R.drawable.button_hold_call);
            mVLSkypeCallViewHolder.mVHHoldBtn.setBackgroundResource(R.drawable.button_hold_call);
            mVLSkypeCallViewHolder.mVHHoldTv.setText(R.string.hold);
            
            mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setEnabled(true);
            mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setFocusable(true);
            mVLSkypeCallViewHolder.mVHLocalVideoBtn.setEnabled(true);
            mVLSkypeCallViewHolder.mVHLocalVideoBtn.setFocusable(true);
            mVLSkypeCallViewHolder.mVHPupPauseIv.setVisibility(View.GONE);
            if(mVLLocalVideoStarted){
             //   SktApiActor.sendLocalVideo();//sometimes we should start send again if the resume do not
                localVideoWaiting();
            }
        }
        
        mVLSkypeCallViewHolder.mVHFullScreenHoldBtn.setEnabled(false); 
        mVLSkypeCallViewHolder.mVHFullScreenHoldBtn.setFocusable(false);
        mVLSkypeCallViewHolder.mVHHoldBtn.setEnabled(false);
        mVLSkypeCallViewHolder.mVHHoldBtn.setFocusable(false);
    }

    /**
     * remote hold
     */
    public void otherHold(){
        mVLSkypeCallViewHolder.mVHFullScreenHoldBtn.setEnabled(false); 
        mVLSkypeCallViewHolder.mVHFullScreenHoldBtn.setFocusable(false);
        mVLSkypeCallViewHolder.mVHHoldBtn.setEnabled(false);
        mVLSkypeCallViewHolder.mVHHoldBtn.setFocusable(false);
        
        mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setEnabled(false);
        mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setFocusable(false);
        mVLSkypeCallViewHolder.mVHLocalVideoBtn.setEnabled(false);
        mVLSkypeCallViewHolder.mVHLocalVideoBtn.setFocusable(false);
        
        mVLSkypeCallViewHolder.mVHPupPauseIv.setVisibility(View.VISIBLE);
        
    }
    
    /**
     * remote resume
     */
    public void otherResume(){
        mVLSkypeCallViewHolder.mVHFullScreenHoldBtn.setEnabled(true); 
        mVLSkypeCallViewHolder.mVHFullScreenHoldBtn.setFocusable(true);
        mVLSkypeCallViewHolder.mVHHoldBtn.setEnabled(true);
        mVLSkypeCallViewHolder.mVHHoldBtn.setFocusable(true);
        
        mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setEnabled(true);
        mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setFocusable(true);
        mVLSkypeCallViewHolder.mVHLocalVideoBtn.setEnabled(true);
        mVLSkypeCallViewHolder.mVHLocalVideoBtn.setFocusable(true);
        
        mVLSkypeCallViewHolder.mVHPupPauseIv.setVisibility(View.GONE);
        
        if(mVLCallHolded || !mVLLocalVideoStarted){
            localVideoStop();
        }else{
            if(mVLLocalVideoStarted){
                if(SktApiActor.getVideoStatus(1) == Video.STATUS.RUNNING){
                    localVideoRunning();
                }else{
                    localVideoWaiting();
                }
            }
        }
    }
    /**
     *  mute unmute btnClick
     */
    private void muteUnmute() {
        mVLMuted = !mVLMuted;
        
        if (mVLMuted){
            mVLSkypeCallViewHolder.mVHFullScreenMuteBtn.setBackgroundResource(R.drawable.button_unmute_call);
            mVLSkypeCallViewHolder.mVHMuteBtn.setBackgroundResource(R.drawable.button_unmute_call);
            mVLSkypeCallViewHolder.mVHMuteTv.setText(R.string.unmute);
        }else{
            mVLSkypeCallViewHolder.mVHFullScreenMuteBtn.setBackgroundResource(R.drawable.button_mute_call);
            mVLSkypeCallViewHolder.mVHMuteBtn.setBackgroundResource(R.drawable.button_mute_call);
            mVLSkypeCallViewHolder.mVHMuteTv.setText(R.string.mute);
        }
        
       SktApiActor.muteConversation(mVLMuted);
    }

    /**
     * normal or full screen click
     */
    private void normalFullScreen() {
        mVLFullScreened = !mVLFullScreened;
        
        if (mVLFullScreened) {
            mVLSkypeCallViewHolder.mVHLocalVideoSv.setLayoutParams(mVLLayoutParamsHelper.getFSLocalVideoLp());
            mVLSkypeCallViewHolder.mVHRemoteVideoSv.setLayoutParams(mVLLayoutParamsHelper.getFSRemoteVideoLp());
            
            mVLSkypeCallViewHolder.mVHNormalScreenBtnsRel.setVisibility(View.INVISIBLE);
            mVLSkypeCallViewHolder.mVHFullScreenBtnsLil.setVisibility(View.VISIBLE);
            mVLSkypeCallViewHolder.mVHContactNameTv.setVisibility(View.GONE);
            mVLSkypeCallViewHolder.mVHUsrNameTv.setVisibility(View.GONE);
            mVLSkypeCallViewHolder.mVHCallDurationTip.setVisibility(View.GONE);
            mVLSkypeCallViewHolder.mVHCallDurationTv.setVisibility(View.GONE);
            mVLSkypeCallViewHolder.mVHLine.setVisibility(View.GONE);
            mVLSkypeCallActivity.postHideMenu();
        } else {
            mVLSkypeCallViewHolder.mVHLocalVideoSv.setLayoutParams(mVLLayoutParamsHelper.getNSLocalVideoLp());
            mVLSkypeCallViewHolder.mVHRemoteVideoSv.setLayoutParams(mVLLayoutParamsHelper.getNSRemoteVideoLp());
            
            mVLSkypeCallViewHolder.mVHNormalScreenBtnsRel.setVisibility(View.VISIBLE);
            mVLSkypeCallViewHolder.mVHFullScreenBtnsLil.setVisibility(View.INVISIBLE);
            mVLSkypeCallViewHolder.mVHContactNameTv.setVisibility(View.VISIBLE);
            mVLSkypeCallViewHolder.mVHUsrNameTv.setVisibility(View.VISIBLE);
            mVLSkypeCallViewHolder.mVHCallDurationTip.setVisibility(View.VISIBLE);
            mVLSkypeCallViewHolder.mVHCallDurationTv.setVisibility(View.VISIBLE);
            mVLSkypeCallViewHolder.mVHLine.setVisibility(View.VISIBLE);
            mVLSkypeCallActivity.removeHideMenuCallback();
        }
        changeFocusable(mVLFullScreened);
        updateLocalVideo();
        updateRemoteVideo();
    }

    /*
     * update the video -------------------------------------------------------------------------------------------->
     */
    
    /*
     * local video
     */
    public void localVideoRunning(){
        mVLSkypeCallViewHolder.mVHLocalVideoBtn.setEnabled(true);
        mVLSkypeCallViewHolder.mVHLocalVideoBtn.setFocusable(true);
        
        mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setEnabled(true);
        mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setFocusable(true);
        
        mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setBackgroundResource(R.drawable.button_stop_video);
        mVLSkypeCallViewHolder.mVHLocalVideoBtn.setBackgroundResource(R.drawable.button_stop_video);
        mVLSkypeCallViewHolder.mVHLocalVideoTv.setText(R.string.stop_video);
        
        mVLSkypeCallViewHolder.mVHLocalNormalPb.setVisibility(View.GONE); 
        mVLSkypeCallViewHolder.mVHLocalFullPb.setVisibility(View.GONE);
        
        if (mVLFullScreened) {
            mVLSkypeCallViewHolder.mVHLocalVideoSv.setLayoutParams(mVLLayoutParamsHelper
                    .getFSLocalVideoLp());
        } else {
            mVLSkypeCallViewHolder.mVHLocalVideoSv.setLayoutParams(mVLLayoutParamsHelper
                    .getNSLocalVideoLp());
        }
        mVLSkypeCallViewHolder.mVHLocalVideoSv.setBackgroundResource(0);
    }
    
    public void localVideoWaiting(){
        mVLSkypeCallViewHolder.mVHLocalVideoBtn.setEnabled(false);
        mVLSkypeCallViewHolder.mVHLocalVideoBtn.setFocusable(false);
        
        mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setEnabled(false);
        mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setFocusable(false);
        
        mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setBackgroundResource(R.drawable.button_stop_video);
        mVLSkypeCallViewHolder.mVHLocalVideoBtn.setBackgroundResource(R.drawable.button_stop_video);
        mVLSkypeCallViewHolder.mVHLocalVideoTv.setText(R.string.stop_video);
        
        if(mVLFullScreened){
            mVLSkypeCallViewHolder.mVHLocalFullPb.setVisibility(View.VISIBLE);
            mVLSkypeCallViewHolder.mVHLocalNormalPb.setVisibility(View.GONE);
        }else{
            mVLSkypeCallViewHolder.mVHLocalNormalPb.setVisibility(View.VISIBLE);  
            mVLSkypeCallViewHolder.mVHLocalFullPb.setVisibility(View.GONE);
        }
        
        if (mVLFullScreened) {
            mVLSkypeCallViewHolder.mVHLocalVideoSv.setLayoutParams(mVLLayoutParamsHelper
                    .getFSLocalVideoLp());
        } else {
            mVLSkypeCallViewHolder.mVHLocalVideoSv.setLayoutParams(mVLLayoutParamsHelper
                    .getNSLocalVideoLp());
        }
        mVLSkypeCallViewHolder.mVHLocalVideoSv.setBackgroundResource(color.background_dark);
    }
    
    public void localVideoStop(){
        mVLSkypeCallViewHolder.mVHLocalVideoBtn.setEnabled(!mVLCallHolded);
        mVLSkypeCallViewHolder.mVHLocalVideoBtn.setFocusable(!mVLCallHolded);
        
        mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setEnabled(!mVLCallHolded);
        mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setFocusable(!mVLCallHolded);
        
        mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setBackgroundResource(R.drawable.button_start_video);
        mVLSkypeCallViewHolder.mVHLocalVideoBtn.setBackgroundResource(R.drawable.button_start_video);
        mVLSkypeCallViewHolder.mVHLocalVideoTv.setText(R.string.start_video);
        
        mVLSkypeCallViewHolder.mVHLocalNormalPb.setVisibility(View.GONE); 
        mVLSkypeCallViewHolder.mVHLocalFullPb.setVisibility(View.GONE);
        
        if (mVLFullScreened) {
            mVLSkypeCallViewHolder.mVHLocalVideoSv.setLayoutParams(mVLLayoutParamsHelper
                    .getMinistLp());
        } else {
            mVLSkypeCallViewHolder.mVHLocalVideoSv.setLayoutParams(mVLLayoutParamsHelper
                    .getNSLocalVideoLp());
            mVLSkypeCallViewHolder.mVHLocalVideoSv
                    .setBackgroundResource(R.drawable.skype_img_call_control_local_default_16_9);
        }
    }
    
    public void updateLocalVideo() {
        if(mVLCallHolded || !mVLLocalVideoStarted){
            localVideoStop();
        }else{
            if(mVLLocalVideoStarted){
                if(SktApiActor.getVideoStatus(1) == Video.STATUS.RUNNING){
                    localVideoRunning();
                }else{
                    localVideoWaiting();
                }
            }
        }
    }
    
    public void otherRejectVideo(){
        if(mVLLocalVideoStarted){
            SktApiActor.stopSendVideo();
            localVideoStop();
            mVLLocalVideoStarted = false;
        }
    }
    
    public void cameraIn(){
//        Log.v("SkypeCallViewListenner", "mVLSendVideoCameraIn----------------"+mVLSendVideoCameraIn);
//        
//        if(SktApiActor.getVideoStatus(1) != Video.STATUS.AVAILABLE){
//            mIsCameraIn = true;
//        }else{
//            mIsCameraIn = false;
//            if(mVLSendVideoCameraIn){
//                SktApiActor.sendLocalVideo(); 
//                localVideoWaiting();
//                mVLLocalVideoStarted = true;
//            }
//        }
    }
    
    public void cameraOut(){
        Log.v("SkypeCallViewListenner", "mVLLocalVideoStarted----------------"+mVLLocalVideoStarted);
        if(mVLLocalVideoStarted){
            SktApiActor.stopSendVideo(); 
            mVLLocalVideoStarted = false;
        }
    }

    /*
     * remote video
     */
    
    public void remoteVideoRunning(){
        mVLSkypeCallViewHolder.mVHRemoteNormalPb.setVisibility(View.GONE); 
        mVLSkypeCallViewHolder.mVHRemoteFullPb.setVisibility(View.GONE);
        
        if (mVLFullScreened) {
            mVLSkypeCallViewHolder.mVHRemoteVideoSv.setLayoutParams(mVLLayoutParamsHelper
                    .getFSRemoteVideoLp());
        } else {
            mVLSkypeCallViewHolder.mVHRemoteVideoSv.setLayoutParams(mVLLayoutParamsHelper
                    .getNSRemoteVideoLp());
        }
        mVLSkypeCallViewHolder.mVHRemoteVideoSv.setBackgroundResource(0);
    }
    
    public void remoteVideoWaiting(){
        if(mVLFullScreened){
            mVLSkypeCallViewHolder.mVHRemoteFullPb.setVisibility(View.VISIBLE);
            mVLSkypeCallViewHolder.mVHRemoteNormalPb.setVisibility(View.GONE);
        }else{
            mVLSkypeCallViewHolder.mVHRemoteNormalPb.setVisibility(View.VISIBLE);  
            mVLSkypeCallViewHolder.mVHRemoteFullPb.setVisibility(View.GONE);
        }
        
        if (mVLFullScreened) {
            mVLSkypeCallViewHolder.mVHRemoteVideoSv.setLayoutParams(mVLLayoutParamsHelper
                    .getFSRemoteVideoLp());
        } else {
            mVLSkypeCallViewHolder.mVHRemoteVideoSv.setLayoutParams(mVLLayoutParamsHelper
                    .getNSRemoteVideoLp());
        }
        mVLSkypeCallViewHolder.mVHRemoteVideoSv.setBackgroundResource(color.background_dark);
    }
    
    public void remoteVideoStop() {
        mVLSkypeCallViewHolder.mVHRemoteNormalPb.setVisibility(View.GONE);
        mVLSkypeCallViewHolder.mVHRemoteFullPb.setVisibility(View.GONE);
        if (mVLFullScreened) {
            mVLSkypeCallViewHolder.mVHRemoteVideoSv.setLayoutParams(mVLLayoutParamsHelper
                    .getFSRemoteVideoLp());
            mVLSkypeCallViewHolder.mVHRemoteVideoSv.setBackgroundResource(color.background_dark);
        } else {
            mVLSkypeCallViewHolder.mVHRemoteVideoSv.setLayoutParams(mVLLayoutParamsHelper
                    .getNSRemoteVideoLp());
            mVLSkypeCallViewHolder.mVHRemoteVideoSv
                    .setBackgroundResource(R.drawable.skype_img_call_control_local_default_16_9);
        }

    }
    
    public void updateRemoteVideo() {
        if(mVLCallHolded){
            remoteVideoStop();
        }else{ 
            if(mVLRemoteVideoStarted){
                if(SktApiActor.getVideoStatus(2) == Video.STATUS.RUNNING){
                    remoteVideoRunning();
                }else{
                    remoteVideoWaiting();
                }
            }
        }
    }

    /*
     * change the buttons' focusable
     * --------------------------------------------------------------------------------------------->
     */
    private void changeFocusable(boolean fullScreen) {
        if (fullScreen) {
            if (!mVLCallHolded){
                mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setFocusable(true); 
            }
            
            mVLSkypeCallViewHolder.mVHFullScreenHoldBtn.setFocusable(true);
            mVLSkypeCallViewHolder.mVHFullScreenMuteBtn.setFocusable(true);
            mVLSkypeCallViewHolder.mVHNormalScreenBtn.setFocusable(true);
            mVLSkypeCallViewHolder.mVHFullScreenEndCallBtn.setFocusable(true);
            mVLSkypeCallViewHolder.mVHNormalScreenBtn.requestFocus();

            mVLSkypeCallViewHolder.mVHLocalVideoBtn.setFocusable(false);
            mVLSkypeCallViewHolder.mVHHoldBtn.setFocusable(false);
            mVLSkypeCallViewHolder.mVHMuteBtn.setFocusable(false);
            mVLSkypeCallViewHolder.mVHFullScreenBtn.setFocusable(false);
            mVLSkypeCallViewHolder.mVHEndCallBtn.setFocusable(false);
        } else {
            if (!mVLCallHolded){
                mVLSkypeCallViewHolder.mVHLocalVideoBtn.setFocusable(true);
            }
            
            mVLSkypeCallViewHolder.mVHHoldBtn.setFocusable(true);
            mVLSkypeCallViewHolder.mVHMuteBtn.setFocusable(true);
            mVLSkypeCallViewHolder.mVHFullScreenBtn.setFocusable(true);
            mVLSkypeCallViewHolder.mVHEndCallBtn.setFocusable(true);
            mVLSkypeCallViewHolder.mVHFullScreenBtn.requestFocus();

            mVLSkypeCallViewHolder.mVHFullScreenLocalVideoBtn.setFocusable(false);
            mVLSkypeCallViewHolder.mVHFullScreenHoldBtn.setFocusable(false);
            mVLSkypeCallViewHolder.mVHFullScreenMuteBtn.setFocusable(false);
            mVLSkypeCallViewHolder.mVHNormalScreenBtn.setFocusable(false);
            mVLSkypeCallViewHolder.mVHFullScreenEndCallBtn.setFocusable(false);
        }
    }

    /*
     * do after call ended :update the database and update the UI
     * ------------------------------------------------------------------------->
     */
    public void callEnded() {
        mVLConvTableResourceHolder = new ConvTableResourceHolder();
        mVLConvTableResourceHolder.setConvName(mVLConvName);
        mVLConvTableResourceHolder.setConvDate(mVLConvDate);
        mVLConvTableResourceHolder.setConvDuration(mVLSkypeCallViewHolder.mVHCallDurationTv
                .getDurationStr());
        mVLConvTableResourceHolder.setConvType(mVLConvType + "");
        mVLConvTableResourceHolder.setConvTime(mVLConvTime);

        ConvTableActor.getInstance().insert(mVLConvTableResourceHolder);
        mVLSkypeCallViewHolder.mVHCallDurationTv.stopRecordTime();

        mVLSkypeCallActivity.finish();
    }
    
}
