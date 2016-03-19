package com.jrm.skype.ui;

import java.util.ArrayList;
import com.jrm.skype.datebase.ConvTableActor;
import com.jrm.skype.datebase.ConvTableResourceHolder;
import com.jrm.skype.util.DateFormat;
import com.jrm.skype.util.SktApiActor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import com.jrm.skype.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ConferenceViewListenner implements OnClickListener {
    private ConferenceActivity mVLConferenceActivity;
    
    private ConferenceViewHolder mVLConferenceViewHolder;
    
    private ArrayList<String> mVLCustomers;
    
    private String mVLConvName;
    
    private boolean mVLCallHolded;

    private boolean mVLMuted;
    /*
     * database part
     */
    private ConvTableResourceHolder mVLConvTableResourceHolder;
    
    private DateFormat mDateFormat;
    
    private int mVLCallType;
    
    private String mVLCallTime;

    private String mVLCallDate;
    
    /*
     * view part
     */
    
    private final int NUMBERINROW = 6;
    
    private int mVLNumberOfContact; //the total number of contact in this conference(the user not include)
    
    private int mVLNumberOfRow;    // the number of row , 6 contacts in one row
    
    private int mVLNumberOfLastRow;    // the number contact in the last row
    
    private ArrayList<ImageView>  mVLContactHeadImList;
    
    private ArrayList<TextView> mVLContactNameList;
    
    private HeadImGetThread mHeadImGetThread;
    
    private boolean mStopHeadThread;

    private Handler mHandler;
    public ConferenceViewListenner(ConferenceActivity mVLConferenceActivity,
            ConferenceViewHolder mVLConferenceViewHolder,String convName,ArrayList<String> customers,int callType) {
        super();
        this.mVLConferenceActivity = mVLConferenceActivity;
        this.mVLConferenceViewHolder = mVLConferenceViewHolder;
        
        this.mVLConvName = convName;
        this.mVLCustomers = customers;
        this.mVLCallType = callType;
        
        mVLCallHolded = false;
        mVLMuted = false;
        mStopHeadThread = false;
        
        mDateFormat = new DateFormat();
        mVLContactNameList = new ArrayList<TextView>();
        mVLContactHeadImList = new ArrayList<ImageView>();
        mHandler = new Handler();
    }

    public void setViewListenner(){
        mVLConferenceViewHolder.mVHEndCallBtn.setOnClickListener(this);
        mVLConferenceViewHolder.mVHHoldBtn.setOnClickListener(this);
        mVLConferenceViewHolder.mVHMuteBtn.setOnClickListener(this);
    }
    
    public void changeConvCustomers(ArrayList<String> customers){
        this.mVLCustomers = customers;
        initVar();
    }
    
    public void initVar(){
        mVLCallTime = mDateFormat.getFormatString(System.currentTimeMillis(), "HH:mm:ss");
        mVLCallDate = mDateFormat.getFormatString(System.currentTimeMillis(), "yyyy-MM-dd");
        
        mVLConferenceViewHolder.mVHHoldTv.setText(R.string.hold);
        mVLConferenceViewHolder.mVHHoldBtn.setBackgroundResource(R.drawable.button_hold_call);
        
        mVLConferenceViewHolder.mVHMuteTv.setText(R.string.mute);
        mVLConferenceViewHolder.mVHMuteBtn.setBackgroundResource(R.drawable.button_mute_call);
        
        mVLConferenceViewHolder.mVHDurationTv.startRecordTime();
        
        mVLNumberOfContact = mVLCustomers.size();
        
        mVLNumberOfLastRow = mVLNumberOfContact%NUMBERINROW;
        
        if ( 0 == mVLNumberOfLastRow )
            mVLNumberOfRow = mVLNumberOfContact/NUMBERINROW;
        else
            mVLNumberOfRow = mVLNumberOfContact/NUMBERINROW + 1;
        
        initConference();
    }
    
    protected void initConference(){
        RelativeLayout conferenceItemRel = null;
        LinearLayout conferenceViewLil = new LinearLayout(mVLConferenceActivity);
        conferenceViewLil.setOrientation(Configuration.ORIENTATION_PORTRAIT);
        
        if (mVLNumberOfContact < NUMBERINROW)
            mVLConferenceViewHolder.mVHCumtomersSlv.setPadding(0, 180, 0, 0);
        mVLContactNameList.clear();
        mVLContactHeadImList.clear();
        while ( mVLNumberOfRow > 1){
            conferenceItemRel = (RelativeLayout) mVLConferenceActivity.getLayoutInflater().inflate(R.layout.conference_item_6, null).findViewById(R.id.conference_item6);
            
            mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item6_1) );
            mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item6_2) );
            mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item6_3) );
            mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item6_4) );
            mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item6_5) );
            mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item6_6) );
            
            mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item6_1) );
            mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item6_2) );
            mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item6_3) );
            mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item6_4) );
            mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item6_5) );
            mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item6_6) );
            
            conferenceViewLil.addView(conferenceItemRel);
            -- mVLNumberOfRow;
        }
        
        switch (mVLNumberOfLastRow)
        {
            case 0:
                conferenceItemRel= (RelativeLayout) mVLConferenceActivity.getLayoutInflater().inflate(R.layout.conference_item_6, null).findViewById(R.id.conference_item6);
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item6_1) );
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item6_2) );
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item6_3) );
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item6_4) );
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item6_5) );
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item6_6) );
                
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item6_1) );
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item6_2) );
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item6_3) );
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item6_4) );
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item6_5) );
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item6_6) );
                break;
            case 1:
                conferenceItemRel= (RelativeLayout) mVLConferenceActivity.getLayoutInflater().inflate(R.layout.conference_item_1, null).findViewById(R.id.conference_item1);
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item1_1) );
                
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item1_1) );
                break;
            case 2:
                conferenceItemRel= (RelativeLayout) mVLConferenceActivity.getLayoutInflater().inflate(R.layout.conference_item_2, null).findViewById(R.id.conference_item2);
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item2_1) );
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item2_2) );
                
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item2_1) );
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item2_2) );
                break;
            case 3:
                conferenceItemRel= (RelativeLayout) mVLConferenceActivity.getLayoutInflater().inflate(R.layout.conference_item_3, null).findViewById(R.id.conference_item3);
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item3_1) );
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item3_2) );
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item3_3) );
                
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item3_1) );
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item3_2) );
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item3_3) );
                break;
            case 4:
                conferenceItemRel= (RelativeLayout) mVLConferenceActivity.getLayoutInflater().inflate(R.layout.conference_item_4, null).findViewById(R.id.conference_item4);
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item4_1) );
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item4_2) );
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item4_3) );
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item4_4) );
                
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item4_1) );
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item4_2) );
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item4_3) );
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item4_4) );
                break;
            case 5:
                conferenceItemRel= (RelativeLayout) mVLConferenceActivity.getLayoutInflater().inflate(R.layout.conference_item_5, null).findViewById(R.id.conference_item5);
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item5_1) );
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item5_2) );
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item5_3) );
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item5_4) );
                mVLContactNameList.add( (TextView)conferenceItemRel.findViewById(R.id.tv_conference_item5_5) );
                
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item5_1) );
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item5_2) );
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item5_3) );
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item5_4) );
                mVLContactHeadImList.add( (ImageView)conferenceItemRel.findViewById(R.id.iv_conference_item5_5) );
                break;
            default:
                break;
        }

        conferenceViewLil.addView(conferenceItemRel);
       
        mVLConferenceViewHolder.mVHCumtomersSlv.removeAllViews();
        mVLConferenceViewHolder.mVHCumtomersSlv.addView(conferenceViewLil);
        
        for (int index = 0; index <mVLContactNameList.size();  ++ index ) {
            mVLContactNameList.get(index).setText(mVLCustomers.get(index));
        }
        
        if (null == mHeadImGetThread){
            mHeadImGetThread = new HeadImGetThread();
            mHeadImGetThread.start();
        }else{
            mStopHeadThread = true;
            mHeadImGetThread = new HeadImGetThread();
            mHeadImGetThread.start();
        }
        
        
    }
    
    class HeadImGetThread extends Thread{

        @Override
        public void run() {
            mStopHeadThread = false;
            Bitmap bt = null;
            byte[] avatar = null;
            
            for (int index = 0; index <mVLCustomers.size();  ++ index ) {
                if (mStopHeadThread)
                    break;
                
                avatar = SktApiActor.getContactAvatar(mVLCustomers.get(index));
                if (null != avatar){
                    bt = BitmapFactory.decodeByteArray(avatar, 0,avatar.length);
                    if (bt != null){
                        mHandler.post(new HeadImSetRunnable(mVLContactHeadImList.get(index),bt)); 
                    }
                    
                }
                Log.v("HeadImGetThread", "----------------"+mVLCustomers.get(index));
            }
        }
        
    };
    
    class HeadImSetRunnable implements Runnable{
        ImageView iv;
        Bitmap bt;
        HeadImSetRunnable(ImageView iv,Bitmap bt)
        {
            this.iv=iv;
            this.bt=bt;
        }
        public void run() {
            iv.setImageBitmap(bt);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_conference_end_call:
                SktApiActor.leaveConversation();
                callEnded();
                break;
            case R.id.btn_conference_hold_resume:
                holdResume();
                break;
            case R.id.btn_conference_mute:
                muteUnmute();
                break;
            default:
                break;
        }

    }
    
    /**
     *  hold or resume btnClick
     */
    private void holdResume() {
        mVLCallHolded = !mVLCallHolded;
        
        if (mVLCallHolded){
            mVLConferenceViewHolder.mVHHoldBtn.setBackgroundResource(R.drawable.button_resume_call);
            mVLConferenceViewHolder.mVHHoldTv.setText(R.string.resume);
            
            mVLConferenceViewHolder.mVHPupPauseIv.setVisibility(View.VISIBLE);
        }else{
            mVLConferenceViewHolder.mVHHoldBtn.setBackgroundResource(R.drawable.button_hold_call);
            mVLConferenceViewHolder.mVHHoldTv.setText(R.string.hold);
            
            mVLConferenceViewHolder.mVHPupPauseIv.setVisibility(View.GONE);
        }

        SktApiActor.holdResumeConversation(mVLCallHolded);
    }

    /**
     *  mute unmute btnClick
     */
    private void muteUnmute() {
        mVLMuted = !mVLMuted;
        
        if (mVLMuted){
            mVLConferenceViewHolder.mVHMuteBtn.setBackgroundResource(R.drawable.button_unmute_call);
            mVLConferenceViewHolder.mVHMuteTv.setText(R.string.unmute);
        }else{
            mVLConferenceViewHolder.mVHMuteBtn.setBackgroundResource(R.drawable.button_mute_call);
            mVLConferenceViewHolder.mVHMuteTv.setText(R.string.mute);
        }
        
        SktApiActor.muteConversation(mVLMuted);
    }
    
    public void callEnded(){
        mVLConvTableResourceHolder = new ConvTableResourceHolder();
        mVLConvTableResourceHolder.setConvName(mVLConvName);
        mVLConvTableResourceHolder.setConvDate(mVLCallDate);
        mVLConvTableResourceHolder.setConvDuration(mVLConferenceViewHolder.mVHDurationTv.getDurationStr());
        mVLConvTableResourceHolder.setConvType(mVLCallType + "");
        mVLConvTableResourceHolder.setConvTime(mVLCallTime);

        ConvTableActor.getInstance().insert(mVLConvTableResourceHolder);
        mVLConferenceViewHolder.mVHDurationTv.stopRecordTime();
        mVLConferenceActivity.finish();
    }

}
