package com.jrm.skype.dialog;

import com.jrm.skype.ui.R;
import com.jrm.skype.util.SktApiActor;

import android.view.View;
import android.view.View.OnClickListener;

public class DialPadViewListenner implements OnClickListener{
    @SuppressWarnings("unused")
    private DialPadDialog mVLDialPadDialog;
    
    private DialPadViewHolder mVLDialPadViewHolder;
    
    public DialPadViewListenner(DialPadViewHolder dialPadViewHolder,DialPadDialog dialPadDialog){
        this.mVLDialPadDialog = dialPadDialog;
        this.mVLDialPadViewHolder = dialPadViewHolder;
    }
    
    public void setViewListenner(){
        mVLDialPadViewHolder.mVHNOOneIbtn.setOnClickListener(this);
        mVLDialPadViewHolder.mVHNOTwoIbtn.setOnClickListener(this);
        mVLDialPadViewHolder.mVHNOThreeIbtn.setOnClickListener(this);
        mVLDialPadViewHolder.mVHNOFourIbtn.setOnClickListener(this);
        mVLDialPadViewHolder.mVHNOFiveIbtn.setOnClickListener(this);
        mVLDialPadViewHolder.mVHNOSixIbtn.setOnClickListener(this);
        mVLDialPadViewHolder.mVHNOSevenIbtn.setOnClickListener(this);
        mVLDialPadViewHolder.mVHNOEightIbtn.setOnClickListener(this);
        mVLDialPadViewHolder.mVHNONineIbtn.setOnClickListener(this);
        mVLDialPadViewHolder.mVHNOStarIbtn.setOnClickListener(this);
        mVLDialPadViewHolder.mVHNOZeroIbtn.setOnClickListener(this);
        mVLDialPadViewHolder.mVHNOPoundIbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       switch(v.getId()){
           case R.id.iv_dialpad_one:
               SktApiActor.sendDTMF('1');
               mVLDialPadViewHolder.mVHDtmfTv.setText(mVLDialPadViewHolder.mVHDtmfTv.getText()+"1");
               break;
           case R.id.iv_dialpad_two:
               SktApiActor.sendDTMF('2');
               mVLDialPadViewHolder.mVHDtmfTv.setText(mVLDialPadViewHolder.mVHDtmfTv.getText()+"2");
               break;
           case R.id.iv_dialpad_three:
               SktApiActor.sendDTMF('3');
               mVLDialPadViewHolder.mVHDtmfTv.setText(mVLDialPadViewHolder.mVHDtmfTv.getText()+"3");
               break;
           case R.id.iv_dialpad_four:
               SktApiActor.sendDTMF('4');
               mVLDialPadViewHolder.mVHDtmfTv.setText(mVLDialPadViewHolder.mVHDtmfTv.getText()+"4");
               break;
           case R.id.iv_dialpad_five:
               SktApiActor.sendDTMF('5');
               mVLDialPadViewHolder.mVHDtmfTv.setText(mVLDialPadViewHolder.mVHDtmfTv.getText()+"5");
               break;
           case R.id.iv_dialpad_six: 
               SktApiActor.sendDTMF('6');
               mVLDialPadViewHolder.mVHDtmfTv.setText(mVLDialPadViewHolder.mVHDtmfTv.getText()+"6");
               break;
           case R.id.iv_dialpad_seven:
               SktApiActor.sendDTMF('7');
               mVLDialPadViewHolder.mVHDtmfTv.setText(mVLDialPadViewHolder.mVHDtmfTv.getText()+"7");
               break;
           case R.id.iv_dialpad_eight:
               SktApiActor.sendDTMF('8');
               mVLDialPadViewHolder.mVHDtmfTv.setText(mVLDialPadViewHolder.mVHDtmfTv.getText()+"8");
               break;
           case R.id.iv_dialpad_nine: 
               SktApiActor.sendDTMF('9');
               mVLDialPadViewHolder.mVHDtmfTv.setText(mVLDialPadViewHolder.mVHDtmfTv.getText()+"9");
               break;
           case R.id.iv_dialpad_star:
               SktApiActor.sendDTMF('*');
               mVLDialPadViewHolder.mVHDtmfTv.setText(mVLDialPadViewHolder.mVHDtmfTv.getText()+"*");
               break;
           case R.id.iv_dialpad_zero:
               SktApiActor.sendDTMF('0');
               mVLDialPadViewHolder.mVHDtmfTv.setText(mVLDialPadViewHolder.mVHDtmfTv.getText()+"0");
               break;
           case R.id.iv_dialpad_pound: 
               SktApiActor.sendDTMF('#');
               mVLDialPadViewHolder.mVHDtmfTv.setText(mVLDialPadViewHolder.mVHDtmfTv.getText()+"#");
               break;
           default:
               break;
               
       }
    }

}
