package com.jrm.skype.dialog;

import com.jrm.skype.dialog.SkypeOutCallOutDialog;
import com.jrm.skype.ui.R;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * @author andy.liu
 * the viewholder of SkypeOutCallOutDialog
 */
public class SkypeOutCallOutViewHolder {
    public SkypeOutCallOutDialog mVHSkypeOutCallOutDialog;
    
    public RelativeLayout mVHVoiceCallRel;

    public ImageView mVHHeadIv;

    public ImageView mVHStateIv;

    public TextView mVHPhoneNumberTv;

    public ImageView mVHConnectingIv;

    public TextView mVHConnectingTv;
    
    public Button mVHEndCallBtn;

    public ImageView mVHVolUpIv;

    public ImageView mVHVolDownIv;

    public TextView mVHVolTv;

    public TextView mVHBackTv;

    public SkypeOutCallOutViewHolder(SkypeOutCallOutDialog mVHSkypeOutCallOutDialog) {
        super();
        this.mVHSkypeOutCallOutDialog = mVHSkypeOutCallOutDialog;
    }
    
    public void findView() {
        mVHVoiceCallRel = (RelativeLayout) mVHSkypeOutCallOutDialog.getLayout().findViewById(
                R.id.skypeout_fragment_voice_call_mainRel);
        mVHHeadIv = (ImageView) mVHSkypeOutCallOutDialog.getLayout().findViewById(R.id.iv_item_pop_head);
        mVHStateIv = (ImageView) mVHSkypeOutCallOutDialog.getLayout().findViewById(R.id.iv_item_pop_state);
        mVHPhoneNumberTv = (TextView) mVHSkypeOutCallOutDialog.getLayout().findViewById(
                R.id.tv_item_pop_name);
        mVHConnectingIv = (ImageView) mVHSkypeOutCallOutDialog.getLayout().findViewById(
                R.id.iv_item_pop_vvcall_connect);
        mVHConnectingTv = (TextView) mVHSkypeOutCallOutDialog.getLayout().findViewById(
                R.id.tv_item_pop_vvcall_tip);
        mVHEndCallBtn = (Button) mVHSkypeOutCallOutDialog.getLayout().findViewById(
                R.id.btn_item_pop_vvcall_endcall);

        mVHVolUpIv = (ImageView) mVHSkypeOutCallOutDialog.getLayout().findViewById(R.id.iv_item_pop_vol_up);
        mVHVolDownIv = (ImageView) mVHSkypeOutCallOutDialog.getLayout().findViewById(
                R.id.iv_item_pop_vol_down);
        mVHVolTv = (TextView) mVHSkypeOutCallOutDialog.getLayout().findViewById(R.id.tv_item_pop_vol);
        mVHBackTv = (TextView) mVHSkypeOutCallOutDialog.getLayout().findViewById(R.id.tv_item_pop_back);
    }
    
}
