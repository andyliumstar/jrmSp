
package com.jrm.skype.ui;

import android.view.SurfaceView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jrm.skype.ui.SkypeCallActivity;
import com.jrm.skype.view.SKTimerTextView;

/**
 * @author andy.liu the ViewHolder of SkypeCallActivity
 */
public class SkypeCallViewHolder {
    public SkypeCallActivity mVHSkypeCallActivity;

    /*
     * normal screen
     */
    public TextView mVHUsrNameTv;

    public TextView mVHContactNameTv;

    public TextView mVHCallDurationTip;

    public SKTimerTextView mVHCallDurationTv;
    
    public ImageView mVHLine;

    public TextView mVHLocalVideoTv;

    public TextView mVHHoldTv;

    public TextView mVHMuteTv;

    public ProgressBar mVHLocalNormalPb;

    public ProgressBar mVHLocalFullPb;

    public ProgressBar mVHRemoteNormalPb;

    public ProgressBar mVHRemoteFullPb;
    
    public SurfaceView mVHLocalVideoSv;

    public SurfaceView mVHRemoteVideoSv;

    public RelativeLayout mVHNormalScreenBtnsRel;

    public Button mVHLocalVideoBtn;

    public Button mVHHoldBtn;

    public Button mVHMuteBtn;

    public Button mVHFullScreenBtn;

    public Button mVHEndCallBtn;

    public ImageView mVHPupPauseIv;

    /*
     * full screen
     */
    public LinearLayout mVHFullScreenBtnsLil;

    public Button mVHFullScreenLocalVideoBtn;

    public Button mVHFullScreenHoldBtn;

    public Button mVHFullScreenMuteBtn;

    public Button mVHNormalScreenBtn;

    public Button mVHFullScreenEndCallBtn;

    public SkypeCallViewHolder(SkypeCallActivity mVHSkypeCallActivity) {
        super();
        this.mVHSkypeCallActivity = mVHSkypeCallActivity;
    }

    public void findView() {
        /*
         * normal screen
         */
        mVHLine = (ImageView) mVHSkypeCallActivity.findViewById(R.id.iv_incall_line);
        mVHUsrNameTv = (TextView) mVHSkypeCallActivity.findViewById(R.id.tv_incall_local_video_name);
        mVHContactNameTv = (TextView) mVHSkypeCallActivity
                .findViewById(R.id.tv_incall_remote_video_name);
        mVHCallDurationTip = (TextView) mVHSkypeCallActivity
                .findViewById(R.id.tv_incall_calldurationTip);
        mVHCallDurationTv = (SKTimerTextView) mVHSkypeCallActivity
                .findViewById(R.id.tv_incall_duration);
        mVHLocalVideoTv = (TextView) mVHSkypeCallActivity
                .findViewById(R.id.tv_incall_stop_start_video);
        mVHHoldTv = (TextView) mVHSkypeCallActivity.findViewById(R.id.tv_incall_hold_resume);
        mVHMuteTv = (TextView) mVHSkypeCallActivity.findViewById(R.id.tv_incall_mute);

        mVHLocalVideoBtn = (Button) mVHSkypeCallActivity
                .findViewById(R.id.btn_incall_stop_start_video);
        mVHHoldBtn = (Button) mVHSkypeCallActivity.findViewById(R.id.btn_incall_hold_resume);
        mVHMuteBtn = (Button) mVHSkypeCallActivity.findViewById(R.id.btn_incall_mute);
        mVHFullScreenBtn = (Button) mVHSkypeCallActivity.findViewById(R.id.btn_incall_full_screen);
        mVHEndCallBtn = (Button) mVHSkypeCallActivity.findViewById(R.id.btn_incall_end_call);

        mVHLocalNormalPb  = (ProgressBar) mVHSkypeCallActivity.findViewById(R.id.pb_incall_local_normal);
        mVHLocalFullPb  = (ProgressBar) mVHSkypeCallActivity.findViewById(R.id.pb_incall_local_full);
        
        mVHRemoteNormalPb  = (ProgressBar) mVHSkypeCallActivity.findViewById(R.id.pb_incall_remote_normal);
        mVHRemoteFullPb  = (ProgressBar) mVHSkypeCallActivity.findViewById(R.id.pb_incall_remote_full);
        
        mVHLocalVideoSv = (SurfaceView) mVHSkypeCallActivity.findViewById(R.id.sv_incall_local_video);
        mVHRemoteVideoSv = (SurfaceView) mVHSkypeCallActivity.findViewById(R.id.sv_incall_remote_video);

        mVHPupPauseIv = (ImageView) mVHSkypeCallActivity.findViewById(R.id.iv_incall_pup_pause);
        mVHNormalScreenBtnsRel = (RelativeLayout) mVHSkypeCallActivity
                .findViewById(R.id.rel_incall_normal_button);
        /*
         * full screen
         */
        mVHFullScreenBtnsLil = (LinearLayout) mVHSkypeCallActivity
                .findViewById(R.id.lil_incall_fullSrceen_buttom_funtion);
        mVHFullScreenLocalVideoBtn = (Button) mVHSkypeCallActivity
                .findViewById(R.id.btn_incall_fullSrceen_stop_start_video);
        mVHFullScreenHoldBtn = (Button) mVHSkypeCallActivity
                .findViewById(R.id.btn_incall_fullSrceen_hold_resume);
        mVHFullScreenMuteBtn = (Button) mVHSkypeCallActivity
                .findViewById(R.id.btn_incall_fullSrceen_mute);
        mVHNormalScreenBtn = (Button) mVHSkypeCallActivity
                .findViewById(R.id.btn_incall_fullSrceen_normal_screen);
        mVHFullScreenEndCallBtn = (Button) mVHSkypeCallActivity
                .findViewById(R.id.btn_incall_fullSrceen_end_call);
    }

}
