package com.jrm.skype.ui;

import com.jrm.skype.view.SKTimerTextView;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class ConferenceViewHolder {
    public ConferenceActivity mVHConferenceActivity;
    
    public ScrollView mVHCumtomersSlv;
    
    public SKTimerTextView mVHDurationTv;
    
    public TextView mVHHoldTv;

    public TextView mVHMuteTv;
    
    public Button mVHHoldBtn;

    public Button mVHMuteBtn;
    
    public Button mVHEndCallBtn;
    
    public ImageView mVHPupPauseIv;

    public ConferenceViewHolder(ConferenceActivity mVHConferenceActivity) {
        super();
        this.mVHConferenceActivity = mVHConferenceActivity;
    }
    
    public void findView(){
        mVHCumtomersSlv = (ScrollView) mVHConferenceActivity.findViewById(R.id.conference_scrollView);
        mVHDurationTv = (SKTimerTextView) mVHConferenceActivity.findViewById(R.id.tv_conference_duration);
        
        mVHHoldTv = (TextView) mVHConferenceActivity.findViewById(R.id.tv_conference_hold_resume);
        mVHMuteTv = (TextView) mVHConferenceActivity.findViewById(R.id.tv_conference_mute);
        
        mVHHoldBtn = (Button) mVHConferenceActivity.findViewById(R.id.btn_conference_hold_resume);
        mVHMuteBtn = (Button) mVHConferenceActivity.findViewById(R.id.btn_conference_mute);
        mVHEndCallBtn = (Button) mVHConferenceActivity.findViewById(R.id.btn_conference_end_call);
        
        mVHPupPauseIv = (ImageView) mVHConferenceActivity.findViewById(R.id.iv_conference_pup_pause); 
    }

}
