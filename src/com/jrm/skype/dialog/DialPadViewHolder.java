package com.jrm.skype.dialog;

import com.jrm.skype.ui.R;

import android.widget.ImageButton;
import android.widget.TextView;

public class DialPadViewHolder {
    public DialPadDialog mVHDialPadDialog;
    
    public TextView mVHDtmfTv;
    
    public ImageButton mVHNOOneIbtn;

    public ImageButton mVHNOTwoIbtn;
    
    public ImageButton mVHNOThreeIbtn;
    
    public ImageButton mVHNOFourIbtn;
    
    public ImageButton mVHNOFiveIbtn;
    
    public ImageButton mVHNOSixIbtn;
    
    public ImageButton mVHNOSevenIbtn;
    
    public ImageButton mVHNOEightIbtn;
    
    public ImageButton mVHNONineIbtn;
    
    public ImageButton mVHNOStarIbtn;
    
    public ImageButton mVHNOZeroIbtn;
    
    public ImageButton mVHNOPoundIbtn;
    
    public DialPadViewHolder(DialPadDialog dialPadDialog){
        this.mVHDialPadDialog = dialPadDialog;
    }
    
    public void findView(){
        mVHDtmfTv = (TextView)mVHDialPadDialog.getLayout().findViewById(R.id.tv_dialpad_dtmf);
        mVHNOOneIbtn = (ImageButton)mVHDialPadDialog.getLayout().findViewById(R.id.iv_dialpad_one);
        mVHNOTwoIbtn = (ImageButton)mVHDialPadDialog.getLayout().findViewById(R.id.iv_dialpad_two);
        mVHNOThreeIbtn = (ImageButton)mVHDialPadDialog.getLayout().findViewById(R.id.iv_dialpad_three);
        mVHNOFourIbtn = (ImageButton)mVHDialPadDialog.getLayout().findViewById(R.id.iv_dialpad_four);
        mVHNOFiveIbtn = (ImageButton)mVHDialPadDialog.getLayout().findViewById(R.id.iv_dialpad_five);
        mVHNOSixIbtn = (ImageButton)mVHDialPadDialog.getLayout().findViewById(R.id.iv_dialpad_six);
        mVHNOSevenIbtn = (ImageButton)mVHDialPadDialog.getLayout().findViewById(R.id.iv_dialpad_seven);
        mVHNOEightIbtn = (ImageButton)mVHDialPadDialog.getLayout().findViewById(R.id.iv_dialpad_eight);
        mVHNONineIbtn = (ImageButton)mVHDialPadDialog.getLayout().findViewById(R.id.iv_dialpad_nine);
        mVHNOStarIbtn = (ImageButton)mVHDialPadDialog.getLayout().findViewById(R.id.iv_dialpad_star);
        mVHNOZeroIbtn = (ImageButton)mVHDialPadDialog.getLayout().findViewById(R.id.iv_dialpad_zero);
        mVHNOPoundIbtn = (ImageButton)mVHDialPadDialog.getLayout().findViewById(R.id.iv_dialpad_pound);
    }
}
