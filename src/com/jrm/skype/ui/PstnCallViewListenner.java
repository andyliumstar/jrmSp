package com.jrm.skype.ui;

import android.view.View;
import android.view.View.OnClickListener;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.datebase.ConvTableActor;
import com.jrm.skype.datebase.ConvTableResourceHolder;
import com.jrm.skype.util.DateFormat;
import com.jrm.skype.util.LayoutParamsHelper;
import com.jrm.skype.util.SktApiActor;

public class PstnCallViewListenner implements OnClickListener {
    private PstnCallActivity mVLPstnCallActivity;

    private PstnCallViewHolder mVLPstnCallViewHolder;

    private boolean mVLCallHolded;

    private boolean mVLMuted;
    
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

    public PstnCallViewListenner(PstnCallActivity inCallActivity,
            PstnCallViewHolder inCallViewHolder ) {
        this.mVLPstnCallActivity = inCallActivity;
        this.mVLPstnCallViewHolder = inCallViewHolder;
        
        mVLDateFormat = new DateFormat();
        mVLLayoutParamsHelper = new LayoutParamsHelper(mVLPstnCallActivity);
        mVLCallHolded = false;
        mVLMuted = false;
    }

    public void initVar(int callType,String callContact) {
        this.mVLConvType = callType;
        this.mVLConvName = callContact;
        
        mVLConvTime = mVLDateFormat.getFormatString(System.currentTimeMillis(), "HH:mm:ss");
        mVLConvDate = mVLDateFormat.getFormatString(System.currentTimeMillis(), "yyyy-MM-dd");
        mVLPstnCallViewHolder.mVHCallDurationTv.startRecordTime();
        
        mVLPstnCallViewHolder.mVHHoldTv.setText(R.string.hold);
        mVLPstnCallViewHolder.mVHHoldBtn.setBackgroundResource(R.drawable.button_hold_call);
        
        mVLPstnCallViewHolder.mVHMuteTv.setText(R.string.mute);
        mVLPstnCallViewHolder.mVHMuteBtn.setBackgroundResource(R.drawable.button_mute_call);

        //set the LayoutParams for surface view
        mVLPstnCallViewHolder.mVHRemoteVideoIv.setLayoutParams(mVLLayoutParamsHelper.getNSRemoteVideoLp());
        mVLPstnCallViewHolder.mVHLocalVideoIv.setLayoutParams(mVLLayoutParamsHelper.getNSLocalVideoLp());
        
    }

    public boolean getCallHold(){
        return mVLCallHolded;
    }
    
    public void setViewListenner() {
        mVLPstnCallViewHolder.mVHDialPadBtn.setOnClickListener(this);
        mVLPstnCallViewHolder.mVHHoldBtn.setOnClickListener(this);
        mVLPstnCallViewHolder.mVHMuteBtn.setOnClickListener(this);
        mVLPstnCallViewHolder.mVHEndCallBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pstncall_dial_pad:
                mVLPstnCallActivity.showDialog(SKYPECONSTANT.USRACCOUNTDIALOG.DIALPAD);
                break;

            case R.id.btn_pstncall_hold_resume:
                holdResume();
                break;

            case R.id.btn_pstncall_mute:
                muteUnmute();
                break;

            case R.id.btn_pstncall_end_call:
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
     *  hold or resume btnClick
     */
    public void holdResume() {
        mVLCallHolded = !mVLCallHolded;
        
        if (mVLCallHolded){
            mVLPstnCallViewHolder.mVHHoldBtn.setBackgroundResource(R.drawable.button_resume_call);
            mVLPstnCallViewHolder.mVHHoldTv.setText(R.string.resume);
            
            mVLPstnCallViewHolder.mVHDialPadBtn.setEnabled(false);
            mVLPstnCallViewHolder.mVHDialPadBtn.setFocusable(false);
            
            mVLPstnCallViewHolder.mVHPupPauseIv.setVisibility(View.VISIBLE);
        }else{
            mVLPstnCallViewHolder.mVHHoldBtn.setBackgroundResource(R.drawable.button_hold_call);
            mVLPstnCallViewHolder.mVHHoldTv.setText(R.string.hold);
            
            mVLPstnCallViewHolder.mVHDialPadBtn.setEnabled(true);
            mVLPstnCallViewHolder.mVHDialPadBtn.setFocusable(true);
            mVLPstnCallViewHolder.mVHPupPauseIv.setVisibility(View.GONE);
        }
        
        SktApiActor.holdResumeConversation(mVLCallHolded);
    }

    /**
     * remote hold
     */
    public void otherHold(){
        mVLPstnCallViewHolder.mVHHoldBtn.setEnabled(false);
        mVLPstnCallViewHolder.mVHHoldBtn.setFocusable(false);
        
        mVLPstnCallViewHolder.mVHDialPadBtn.setEnabled(false);
        mVLPstnCallViewHolder.mVHDialPadBtn.setFocusable(false);
        
        mVLPstnCallViewHolder.mVHPupPauseIv.setVisibility(View.VISIBLE);
    }
    
    /**
     * remote resume
     */
    public void otherResume(){
        mVLPstnCallViewHolder.mVHHoldBtn.setEnabled(true);
        mVLPstnCallViewHolder.mVHHoldBtn.setFocusable(true);
        
        mVLPstnCallViewHolder.mVHDialPadBtn.setEnabled(true);
        mVLPstnCallViewHolder.mVHDialPadBtn.setFocusable(true);
        
        mVLPstnCallViewHolder.mVHPupPauseIv.setVisibility(View.GONE);
    }
    /**
     *  mute unmute btnClick
     */
    private void muteUnmute() {
        mVLMuted = !mVLMuted;
        
        if (mVLMuted){
            mVLPstnCallViewHolder.mVHMuteBtn.setBackgroundResource(R.drawable.button_unmute_call);
            mVLPstnCallViewHolder.mVHMuteTv.setText(R.string.unmute);
        }else{
            mVLPstnCallViewHolder.mVHMuteBtn.setBackgroundResource(R.drawable.button_mute_call);
            mVLPstnCallViewHolder.mVHMuteTv.setText(R.string.mute);
        }
        
       SktApiActor.muteConversation(mVLMuted);
    }

   
    
    /*
     * do after call ended :update the database and update the UI
     * ------------------------------------------------------------------------->
     */
    public void callEnded() {
        mVLConvTableResourceHolder = new ConvTableResourceHolder();
        mVLConvTableResourceHolder.setConvName(mVLConvName);
        mVLConvTableResourceHolder.setConvDate(mVLConvDate);
        mVLConvTableResourceHolder.setConvDuration(mVLPstnCallViewHolder.mVHCallDurationTv
                .getDurationStr());
        mVLConvTableResourceHolder.setConvType(mVLConvType + "");
        mVLConvTableResourceHolder.setConvTime(mVLConvTime);

        ConvTableActor.getInstance().insert(mVLConvTableResourceHolder);
        mVLPstnCallViewHolder.mVHCallDurationTv.stopRecordTime();
        
        mVLPstnCallActivity.finish();
    }

}
