package com.jrm.skype.dialog;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrm.skype.dialog.ContactDialog;
import com.jrm.skype.ui.R;
/**
 * @author andy.liu
 * the viewholder of ContactItemDialog
 */
public class ContactViewHolder {
    public ContactDialog mVHContactItemDialog;
    /*
     * the main pop view
     */
    public ImageView mVHHeadIv;

    public ImageView mVHStateIv;

    public TextView mVHContactNameTv;
    
    public RelativeLayout mVHPopMainMidRel;

    public Button mVHVideoCallBtn;

    public Button mVHVoiceCallBtn;

    public TextView mVHViewPofileTv;

    public TextView mVHSendMSGTv;

    public TextView mVHSendVMTv;

    public TextView mVHBlockTv;

    public TextView mVHRemoveTv;

    public ImageView mVHVolUpIv;

    public ImageView mVHVolDownIv;

    public TextView mVHVolTv;

    public TextView mVHBackTv;

    /*
     * the child function of the main pop view
     */

    /* vvcall connecting */
    public RelativeLayout mVHPopMidVvcallRel;

    public ImageView mVHVvcallConnectIv;
    
    public TextView mVHVvcallConnectTv;

    public Button mVHVvcallEndBtn;

    /* mVHPopMidProfileRel */
    public RelativeLayout mVHPopMidProfileRel;
    
    public RelativeLayout mVHContactProfileRel;
    
    public RelativeLayout mVHOtherTypeProfileRel;
    
    public TextView mVHProfileOtherTypeTv;
    
    public TextView mVHProfileOtherTypeTipTv;

    public TextView mVHProfileSkypeNameTv;

    public TextView mVHProfileBirthDayTv;

    public TextView mVHProfileGenderTv;

    public TextView mVHProfileLanguageTv;

    public TextView mVHProfileLocationTv;

    public TextView mVHProfilePhoneTv;

    public TextView mVHProfileEmailTv;

    /* mVHPopMidBlockOrRemoveRel */
    public RelativeLayout mVHPopMidBlockOrRemoveRel; // the block and the remove view use the same layout

    public TextView mVHBlockOrRemoveWarnTv;

    public Button mVHBlockOrRemoveYesBtn;

    public Button mVHBlockOrRemoveCancelBtn;

    /* mVHPopMidSendVMRel */
    public RelativeLayout mVHPopMidSendVMRel;

    public ImageView mVHSendVMRecordIv;

    public TextView mVHSendVMTipTv;

    public TextView mVHSendVMTimeTv;

    public Button mVHSendVMCancelBtn;

    public Button mVHSendVMEndcallBtn;

    public Button mVHSendVMOKBtn;

    public ContactViewHolder(ContactDialog mVHContactItemDialog) {
        super();
        this.mVHContactItemDialog = mVHContactItemDialog;
    }
    
    public void findView() {
        mVHHeadIv = (ImageView) mVHContactItemDialog.getLayout().findViewById(R.id.iv_item_pop_head);
        mVHStateIv = (ImageView) mVHContactItemDialog.getLayout().findViewById(
                R.id.iv_item_pop_state);
        mVHContactNameTv = (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_name);

        mVHPopMainMidRel = (RelativeLayout)mVHContactItemDialog.getLayout().findViewById(
                R.id.rel_contactlist_item_pop_midMain);
        mVHVideoCallBtn = (Button) mVHContactItemDialog.getLayout().findViewById(
                R.id.btn_contactlist_item_pop_videocall);
        mVHVoiceCallBtn = (Button) mVHContactItemDialog.getLayout().findViewById(
                R.id.btn_contactlist_item_pop_voicecall);

        mVHPopMidVvcallRel = (RelativeLayout) mVHContactItemDialog.getLayout().findViewById(
                R.id.rel_contactlist_item_pop_midVvcall);
        mVHVvcallConnectTv= (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_vvcall_tip);
        mVHVvcallConnectIv = (ImageView) mVHContactItemDialog.getLayout().findViewById(
                R.id.iv_item_pop_vvcall_connect);
        mVHVvcallEndBtn = (Button) mVHContactItemDialog.getLayout().findViewById(
                R.id.btn_item_pop_vvcall_endcall);

        mVHViewPofileTv = (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_contactlist_item_pop_viewprofile);
        mVHPopMidProfileRel = (RelativeLayout) mVHContactItemDialog.getLayout().findViewById(
                R.id.rel_contactlist_item_pop_midProfile);
        
        mVHContactProfileRel = (RelativeLayout)mVHContactItemDialog.getLayout().findViewById(
                R.id.contactlist_item_pop_midRel_contact_profile);
        mVHOtherTypeProfileRel = (RelativeLayout)mVHContactItemDialog.getLayout().findViewById(
                R.id.contactlist_item_pop_midRel_other_profile);
        mVHProfileOtherTypeTipTv = (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_profile_other_typeTip);
        mVHProfileOtherTypeTv = (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_profile_other_type);

        mVHProfileSkypeNameTv = (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_profile_skypename);
        mVHProfileBirthDayTv = (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_profile_birthday);
        mVHProfileGenderTv = (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_profile_gender);
        mVHProfileLanguageTv = (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_profile_language);
        mVHProfileLocationTv = (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_profile_location);
        mVHProfilePhoneTv = (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_profile_mobilephone);
        mVHProfileEmailTv = (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_profile_email);

        mVHSendMSGTv = (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_contactlist_item_pop_sendMSG);

        mVHSendVMTv = (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_contactlist_item_pop_sendVM);
        mVHSendVMTipTv = (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_sendVM_tip);
        mVHSendVMTimeTv = (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_sendVM_time);
        mVHSendVMCancelBtn = (Button) mVHContactItemDialog.getLayout().findViewById(
                R.id.btn_item_pop_sendVM_cancel);
        mVHSendVMEndcallBtn = (Button) mVHContactItemDialog.getLayout().findViewById(
                R.id.btn_item_pop_sendVM_endcall);
        mVHSendVMOKBtn = (Button) mVHContactItemDialog.getLayout().findViewById(
                R.id.btn_item_pop_sendVM_ok);
        mVHSendVMRecordIv = (ImageView) mVHContactItemDialog.getLayout().findViewById(
                R.id.iv_item_pop_sendVM_record);

        mVHPopMidSendVMRel = (RelativeLayout) mVHContactItemDialog.getLayout().findViewById(
                R.id.rel_contactlist_item_pop_midSendVM);

        mVHRemoveTv = (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_contactlist_item_pop_remove);
        mVHBlockTv = (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_contactlist_item_pop_block);
        mVHBlockOrRemoveWarnTv = (TextView) mVHContactItemDialog.getLayout().findViewById(
                R.id.tv_contactlist_item_pop_blockOrremove_warn);
        mVHBlockOrRemoveYesBtn = (Button) mVHContactItemDialog.getLayout().findViewById(
                R.id.btn_contactlist_item_pop_blockOrremove_yes);
        mVHBlockOrRemoveCancelBtn = (Button) mVHContactItemDialog.getLayout().findViewById(
                R.id.btn_contactlist_item_pop_blockOrremove_cancel);
        mVHPopMidBlockOrRemoveRel = (RelativeLayout) mVHContactItemDialog.getLayout().findViewById(
                R.id.rel_contactlist_item_pop_midBlockOrremove);

        mVHVolUpIv = (ImageView) mVHContactItemDialog.getLayout().findViewById(
                R.id.iv_item_pop_vol_up);
        mVHVolDownIv = (ImageView) mVHContactItemDialog.getLayout().findViewById(
                R.id.iv_item_pop_vol_down);
        mVHVolTv = (TextView) mVHContactItemDialog.getLayout().findViewById(R.id.tv_item_pop_vol);
        mVHBackTv = (TextView) mVHContactItemDialog.getLayout().findViewById(R.id.tv_item_pop_back);
    }


}
