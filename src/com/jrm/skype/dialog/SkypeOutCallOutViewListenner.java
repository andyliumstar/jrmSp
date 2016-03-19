package com.jrm.skype.dialog;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.datebase.ConvTableActor;
import com.jrm.skype.datebase.ConvTableResourceHolder;
import com.jrm.skype.dialog.SkypeOutCallOutDialog;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.jrm.skype.ui.R;
import com.jrm.skype.ui.UsrAccountActivity;
import com.jrm.skype.util.CallHelper;
import com.jrm.skype.util.DateFormat;
import com.jrm.skype.util.SktApiActor;
/**
 * @author andy.liu
 * the viewlistenner of SkypeOutCallOutDialog
 */
public class SkypeOutCallOutViewListenner implements OnClickListener{
    private SkypeOutCallOutViewHolder mVLSkypeOutCallOutViewHolder;
    
    private SkypeOutCallOutDialog mVLSkypeOutCallOutDialog;
    
    private String mVLCallNumber;
    
    private AnimationDrawable mVLCallAnimation;

    public SkypeOutCallOutViewListenner(SkypeOutCallOutViewHolder skypeOutCallOutViewHolder,
            SkypeOutCallOutDialog skypeOutCallOutDialog) {
        this.mVLSkypeOutCallOutViewHolder = skypeOutCallOutViewHolder;
        this.mVLSkypeOutCallOutDialog = skypeOutCallOutDialog;
    }

    public void setViewListenner()
    {
        mVLSkypeOutCallOutViewHolder.mVHEndCallBtn.setOnClickListener(this);
        mVLSkypeOutCallOutViewHolder.mVHBackTv.setOnClickListener(this);
    }

    public void initVar(String phoneNumber) {
        mVLCallNumber = phoneNumber;
        if(null == phoneNumber){
            mVLSkypeOutCallOutDialog.dismiss();
        }
        
        mVLCallAnimation = (AnimationDrawable) mVLSkypeOutCallOutViewHolder.mVHConnectingIv.getBackground();

        mVLSkypeOutCallOutViewHolder.mVHHeadIv
                .setBackgroundResource(R.drawable.skype_img_contact_list_buddy_phone);
        mVLSkypeOutCallOutViewHolder.mVHStateIv.setBackgroundResource(R.drawable.presencephone_32x32);
        /*
         * init the view
         */
        mVLSkypeOutCallOutViewHolder.mVHVolDownIv.setVisibility(View.VISIBLE);
        mVLSkypeOutCallOutViewHolder.mVHVolUpIv.setVisibility(View.VISIBLE);
        mVLSkypeOutCallOutViewHolder.mVHVolTv.setVisibility(View.VISIBLE);

        mVLSkypeOutCallOutViewHolder.mVHEndCallBtn.requestFocus();

        mVLSkypeOutCallOutViewHolder.mVHPhoneNumberTv.setText(mVLCallNumber);
        mVLSkypeOutCallOutViewHolder.mVHConnectingTv.setText(R.string.voice_connecting);
        mVLCallAnimation.start();
        
        if (false == SktApiActor.startPSTBCall(mVLCallNumber)){
            Toast.makeText(mVLSkypeOutCallOutDialog.getOwnerActivity(), R.string.call_failed,
                    Toast.LENGTH_SHORT).show();
            callStop();
        }else{
            if (null != ((UsrAccountActivity)mVLSkypeOutCallOutDialog.getOwnerActivity()).getService()) {
                ((UsrAccountActivity) mVLSkypeOutCallOutDialog.getOwnerActivity()).getService().setCallOutBool(true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        SktApiActor.leaveConversation();
    }
    
    public void callStop()  {
        mVLCallAnimation.stop();
        ConvTableResourceHolder mVLConvTableResourceHolder;
        DateFormat mVLDateFormat;
        mVLConvTableResourceHolder = new ConvTableResourceHolder();
        mVLDateFormat = new DateFormat();
        
        mVLConvTableResourceHolder.setConvName(mVLCallNumber);
        mVLConvTableResourceHolder.setConvDate(mVLDateFormat.getFormatString(System.currentTimeMillis(), "yyyy-MM-dd"));
        mVLConvTableResourceHolder.setConvDuration("00:00:00");
        mVLConvTableResourceHolder.setConvType(SKYPECONSTANT.CONVTYPE.CALLOUT + "");
        mVLConvTableResourceHolder.setConvTime(mVLDateFormat.getFormatString(System.currentTimeMillis(), "HH:mm:ss"));

        ConvTableActor.getInstance().insert(mVLConvTableResourceHolder);
        mVLSkypeOutCallOutDialog.dismiss();
    }
    
    public void callStart(){
        CallHelper.callOut(mVLSkypeOutCallOutDialog.getOwnerActivity(),
                mVLSkypeOutCallOutViewHolder.mVHPhoneNumberTv.getText().toString(), false,true);
        mVLCallAnimation.stop();
        mVLSkypeOutCallOutDialog.dismiss();
    }

}
