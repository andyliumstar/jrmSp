package com.jrm.skype.dialog;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrm.skype.dialog.HistoryDialog;
import com.jrm.skype.ui.R;
/**
 * @author andy.liu
 * the viewholder of HistoryItemDialog
 */
public class HistoryViewHolder {
    public HistoryDialog mVHHistoryItemDialog;
    /*
     * the main functions of the history list fragment
     */
    public ImageView mVHHeadIv;

    public ImageView mVHStateIv;

    public TextView mVHConatctDisNameTv;
    
    public RelativeLayout mVHPopMainMidRel;

    public Button mVHVideoCallBtn;

    public Button mVHVoiceCallBtn;

    public TextView mVHViewPofileTv;

    public TextView mVHSendMSGTv;

    public TextView mVHSendVMTv;

    public TextView mVHLisToVMTv;

    public TextView mVHAddToContactTv;

    public TextView mVHDeleteItemTv;

    public TextView mVHVolTv;

    public TextView mVHBackTv;

    public ImageView mVHVolUpIv;

    public ImageView mVHVolDownIv;
    /*
     * the child funtion of the main pop view
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

    /* mVHPopMidsendVMRel */
    public RelativeLayout mVHPopMidSendVMRel;

    public ImageView mVHSendVMRecordIv;

    public TextView mVHSendVMTipTv;

    public TextView mVHSendVMTimeTv;

    public Button mVHSendVMCancelBtn;

    public Button mVHSendVMEndcallBtn;

    public Button mVHSendVMOKBtn;

    /* mVHPopMidAddContactOrDeleteRel */
    public RelativeLayout mVHPopMidAddContactOrDeleteRel;

    public Button mVHAddContactOrDeleteOKBtn;

    public Button mVHAddContactOrDeleteCancelBtn;

    public TextView mVHAddContactOrDeleteWarnTv;

    /* mVHPopMidListenVMRel */
    public RelativeLayout mVHPopMidLisToVMRel;

    public ImageView mVHLisToVMPlayIm;

    public ProgressBar mVHLisToVMPb;

    public Button mVHLisToVMStopBtn;

    public TextView mVHLisToVMCTipTv;
    
    public TextView mVHLisToVMCurrentTv;

    public TextView mVHLisToVMentireTv;


    public HistoryViewHolder(HistoryDialog mVHHistoryItemDialog) {
        super();
        this.mVHHistoryItemDialog = mVHHistoryItemDialog;
    }

    public void findView() {

        mVHHeadIv = (ImageView) mVHHistoryItemDialog.getLayout().findViewById(R.id.iv_item_pop_head);
        mVHStateIv = (ImageView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.iv_item_pop_state);
        mVHConatctDisNameTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(R.id.tv_item_pop_name);
        mVHPopMainMidRel = (RelativeLayout)mVHHistoryItemDialog.getLayout().findViewById(
                R.id.rel_historylist_item_pop_midMain);

        mVHVideoCallBtn = (Button) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.btn_historylist_item_pop_videocall);
        mVHVoiceCallBtn = (Button) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.btn_historylist_item_pop_voicecall);

        mVHPopMidVvcallRel = (RelativeLayout) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.rel_historylist_item_pop_midVvcall);
        mVHVvcallConnectTv= (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_vvcall_tip);
        mVHVvcallConnectIv = (ImageView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.iv_item_pop_vvcall_connect);
        mVHVvcallEndBtn = (Button) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.btn_item_pop_vvcall_endcall);

        mVHViewPofileTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_historylist_item_pop_viewprofile);
        mVHPopMidProfileRel = (RelativeLayout) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.rel_historylist_item_pop_midProfile);

        mVHContactProfileRel = (RelativeLayout)mVHHistoryItemDialog.getLayout().findViewById(
                R.id.historylist_item_pop_midRel_contact_profile);
        mVHOtherTypeProfileRel = (RelativeLayout)mVHHistoryItemDialog.getLayout().findViewById(
                R.id.historylist_item_pop_midRel_other_profile);
        mVHProfileOtherTypeTipTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_profile_other_typeTip);
        mVHProfileOtherTypeTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_profile_other_type);
        mVHProfileSkypeNameTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_profile_skypename);
        mVHProfileBirthDayTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_profile_birthday);
        mVHProfileGenderTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_profile_gender);
        mVHProfileLanguageTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_profile_language);
        mVHProfileLocationTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_profile_location);
        mVHProfilePhoneTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_profile_mobilephone);
        mVHProfileEmailTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_profile_email);

        mVHSendMSGTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_historylist_item_pop_sendMSG);

        mVHPopMidSendVMRel = (RelativeLayout) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.rel_historylist_item_pop_midSendVM);
        mVHSendVMTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_historylist_item_pop_sendVM);
        mVHSendVMTipTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_sendVM_tip);
        mVHSendVMTimeTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_item_pop_sendVM_time);
        mVHSendVMCancelBtn = (Button) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.btn_item_pop_sendVM_cancel);
        mVHSendVMEndcallBtn = (Button) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.btn_item_pop_sendVM_endcall);
        mVHSendVMOKBtn = (Button) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.btn_item_pop_sendVM_ok);
        mVHSendVMRecordIv = (ImageView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.iv_item_pop_sendVM_record);
        mVHSendVMRecordIv.setBackgroundResource(R.drawable.voicemailanimation);

        mVHAddToContactTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_historylist_item_pop_addtocontact);
        mVHPopMidAddContactOrDeleteRel = (RelativeLayout) mVHHistoryItemDialog.getLayout()
                .findViewById(R.id.rel_historylist_item_pop_midAddcontactOrdelete);
        mVHAddContactOrDeleteOKBtn = (Button) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.btn_historylist_item_pop_addcontactOrdelete_ok);
        mVHAddContactOrDeleteCancelBtn = (Button) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.btn_historylist_item_pop_addcontactOrdelete_cancel);
        mVHAddContactOrDeleteWarnTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_historylist_item_pop_addcontactOrdelete_warn);

        mVHLisToVMTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_historylist_item_pop_LisToVM);
        mVHLisToVMPlayIm = (ImageView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.historylist_item_pop_lisToVM_playIm);
        mVHLisToVMPlayIm.setBackgroundResource(R.drawable.voicemailanimation);
        mVHLisToVMPb = (ProgressBar) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.historylist_item_pop_lisToVM_pb);
        mVHLisToVMStopBtn = (Button) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.btn_historylist_item_pop_lisToVM_stop);
        mVHLisToVMCTipTv  = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_historylist_item_pop_lisToVM_tip);
        mVHLisToVMCurrentTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_historylist_item_pop_lisToVM_currenttime);
        mVHLisToVMentireTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_historylist_item_pop_lisToVM_entiretime);
        mVHPopMidLisToVMRel = (RelativeLayout) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.rel_historylist_item_pop_midLisToVM);

        mVHDeleteItemTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.tv_historylist_item_pop_deleteitem);

        mVHVolUpIv = (ImageView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.iv_item_pop_vol_up);
        mVHVolDownIv = (ImageView) mVHHistoryItemDialog.getLayout().findViewById(
                R.id.iv_item_pop_vol_down);
        mVHVolTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(R.id.tv_item_pop_vol);
        mVHBackTv = (TextView) mVHHistoryItemDialog.getLayout().findViewById(R.id.tv_item_pop_back);
    }

}
