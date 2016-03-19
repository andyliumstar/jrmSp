
package com.jrm.skype.ui;

import com.jrm.skype.api.SktUtils;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.ui.IncomingCallActivity;
import com.jrm.skype.util.SktApiActor;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * @author andy.liu 
 * the ViewListenner of IncomingCallActivity
 */
public class IncomingCallViewListenner implements OnClickListener {
    private IncomingCallActivity mVLIncomingCallActivity;

    private IncomingCallViewHolder mVLIncomingCallViewHolder;
    
    private int mVLCallType;
    
    public IncomingCallViewListenner(IncomingCallActivity mVLIncomingCallActivity,
            IncomingCallViewHolder mVLIncomingCallViewHolder) {
        this.mVLIncomingCallActivity = mVLIncomingCallActivity;
        this.mVLIncomingCallViewHolder = mVLIncomingCallViewHolder;
    }
    
    public void initVar(boolean isVideoPlaying, String convName, int callType) {
        if (isVideoPlaying) {
            mVLIncomingCallViewHolder.mVHBtnsRel.setVisibility(View.GONE);
            mVLIncomingCallViewHolder.mVHNotifyTv.setText(R.string.incomingcall_notify);
        } else {
            mVLIncomingCallViewHolder.mVHContactNameTv.setText(SktApiActor.getConvDisplayName(convName));
            mVLCallType = callType;

            if (mVLCallType == SKYPECONSTANT.CALLTYPE.DIALOG) {
                mVLIncomingCallViewHolder.mVHVideoBtn.setEnabled(true);
                mVLIncomingCallViewHolder.mVHVideoBtn.setFocusable(true);
            } else {
                mVLIncomingCallViewHolder.mVHVideoBtn.setEnabled(false);
                mVLIncomingCallViewHolder.mVHVideoBtn.setFocusable(false);
            }
        }

    }

    public void setViewListenner() {
        mVLIncomingCallViewHolder.mVHDeclineBtn.setOnClickListener(this);
        mVLIncomingCallViewHolder.mVHVideoBtn.setOnClickListener(this);
        mVLIncomingCallViewHolder.mVHVoiceBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_incomingcall_video:
                if (!SktUtils.cameraExists()){
                    Toast.makeText(mVLIncomingCallActivity, R.string.no_camera,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                mVLIncomingCallActivity.setEnableVideo(true);
                SktApiActor.anwserIncomingCall(true);
                break;
            case R.id.btn_incomingcall_voice:
                SktApiActor.anwserIncomingCall(false);
                break;
            case R.id.btn_incomingcall_decline:
                SktApiActor.leaveConversation();
                mVLIncomingCallActivity.setCallMissed(false);
                break;
            default:
                break;
        }

    }

}
