
package com.jrm.skype.ui;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jrm.skype.view.SKTimerTextView;

public class PstnCallViewHolder {
    public PstnCallActivity mVHPstnCallActivity;

    /*
     * normal screen
     */
    public TextView mVHUsrNameTv;

    public TextView mVHContactNameTv;

    public TextView mVHCallDurationTip;

    public SKTimerTextView mVHCallDurationTv;

    public ImageView mVHLine;

    public TextView mVHDialPadTv;

    public TextView mVHHoldTv;

    public TextView mVHMuteTv;


    public ImageView mVHLocalVideoIv;

    public ImageView mVHRemoteVideoIv;

    public RelativeLayout mVHNormalScreenBtnsRel;

    public Button mVHDialPadBtn;

    public Button mVHHoldBtn;

    public Button mVHMuteBtn;

    public Button mVHEndCallBtn;

    public ImageView mVHPupPauseIv;


    public PstnCallViewHolder(PstnCallActivity pstnCallActivity) {
        this.mVHPstnCallActivity = pstnCallActivity;
    }

    public void findView() {
        /*
         * normal screen
         */
        mVHLine = (ImageView) mVHPstnCallActivity.findViewById(R.id.iv_pstncall_line);
        mVHUsrNameTv = (TextView) mVHPstnCallActivity.findViewById(R.id.tv_pstncall_local_video_name);
        mVHContactNameTv = (TextView) mVHPstnCallActivity
                .findViewById(R.id.tv_pstncall_remote_video_name);
        mVHCallDurationTip = (TextView) mVHPstnCallActivity
                .findViewById(R.id.tv_pstncall_calldurationTip);
        mVHCallDurationTv = (SKTimerTextView) mVHPstnCallActivity
                .findViewById(R.id.tv_pstncall_duration);
        mVHDialPadTv = (TextView) mVHPstnCallActivity
                .findViewById(R.id.tv_pstncall_dial_pad);
        mVHHoldTv = (TextView) mVHPstnCallActivity.findViewById(R.id.tv_pstncall_hold_resume);
        mVHMuteTv = (TextView) mVHPstnCallActivity.findViewById(R.id.tv_pstncall_mute);

        mVHDialPadBtn = (Button) mVHPstnCallActivity
                .findViewById(R.id.btn_pstncall_dial_pad);
        mVHHoldBtn = (Button) mVHPstnCallActivity.findViewById(R.id.btn_pstncall_hold_resume);
        mVHMuteBtn = (Button) mVHPstnCallActivity.findViewById(R.id.btn_pstncall_mute);
        mVHEndCallBtn = (Button) mVHPstnCallActivity.findViewById(R.id.btn_pstncall_end_call);


        mVHLocalVideoIv = (ImageView) mVHPstnCallActivity.findViewById(R.id.iv_pstncall_local_video);
        mVHRemoteVideoIv = (ImageView) mVHPstnCallActivity.findViewById(R.id.iv_pstncall_remote_video);

        mVHPupPauseIv = (ImageView) mVHPstnCallActivity.findViewById(R.id.iv_pstncall_pup_pause);
        mVHNormalScreenBtnsRel = (RelativeLayout) mVHPstnCallActivity
                .findViewById(R.id.rel_pstncall_normal_button);
    }

}
