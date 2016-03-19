
package com.jrm.skype.dialog;

import java.util.ArrayList;
import com.jrm.skype.api.SktUtils;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.datebase.ConvTableActor;
import com.jrm.skype.datebase.ConvTableResourceHolder;
import com.jrm.skype.dialog.ContactDialog;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.jrm.skype.ui.R;
import com.jrm.skype.ui.UsrAccountActivity;
import com.jrm.skype.util.CallHelper;
import com.jrm.skype.util.DateFormat;
import com.jrm.skype.util.ProfileHelper;
import com.jrm.skype.util.SktApiActor;
import com.skype.api.Contact;
import com.skype.api.Contact.AVAILABILITY;

/**
 * @author andy.liu the viewlistenner of ContactItemDialog
 */

public class ContactViewListenner implements OnClickListener {
    private ContactViewHolder mVLContactItemViewHolder;

    private ContactDialog mVLContactItemDialog;

    private ArrayList<Integer> mVLPopTopList; // to record the pop view for back

    private String mVLSkypeName;

    private String mVLDisplayName;

    private byte[] mVLAvater;

    private Contact.AVAILABILITY mVLAbility;
    
    private DateFormat mVLDateFormat;

    /*
     * vvcall variables
     */
    private ConvTableResourceHolder mVLConvTableResourceHolder;
    
    private AnimationDrawable mVLVvCallAnimation;
    
    private boolean mVLIsCalling;
    
    private boolean mVLEnableVideo;

    /*
     * send voice mail will use the following variables
     */
    private AnimationDrawable mVLVMAnimation;

    private int mVLVMStatus;
    
    private Handler mVLVMHandler;
    
    private final int VM_WAIT_FOR_SUCCESS = 15000;

    /*
     * block or remove share the same layout ,need a mark
     */
    private boolean mVLBlock;

    private boolean mVLCanBlock;

    public ContactViewListenner(ContactViewHolder mVLContactItemViewHolder,
            ContactDialog mVLContactItemDialog) {
        super();
        this.mVLContactItemViewHolder = mVLContactItemViewHolder;
        this.mVLContactItemDialog = mVLContactItemDialog;
        mVLDateFormat = new DateFormat();
        mVLVMHandler = new Handler();
    }

    public void initVar(Bundle bl) {
        if (bl == null)
            return;

        if (null == mVLPopTopList){
            mVLPopTopList = new ArrayList<Integer>();
        }
        mVLPopTopList.clear();
        mVLIsCalling = false;

        mVLSkypeName = bl.getString(SKYPECONSTANT.SKYPESTRING.SKYPENAME, "");
        mVLDisplayName = bl.getString(SKYPECONSTANT.SKYPESTRING.DISPLAYNAME, "");

        mVLContactItemViewHolder.mVHContactNameTv.setText(mVLDisplayName);
        mVLAbility = SktApiActor.getContactAvailability(mVLSkypeName);
        
        updateAvater();
        updateStatus();
        checkContactAbility();
    }

    public void updateAvater() {
        if (AVAILABILITY.SKYPEOUT == mVLAbility || AVAILABILITY.BLOCKED_SKYPEOUT == mVLAbility) {
            mVLContactItemViewHolder.mVHHeadIv
                    .setImageResource(R.drawable.skype_img_contact_list_buddy_phone);
        } else {
            mVLAvater = SktApiActor.getContactAvatar(mVLSkypeName);
            if (null != mVLAvater && mVLAvater.length > 0) {
                mVLContactItemViewHolder.mVHHeadIv.setImageBitmap(BitmapFactory.decodeByteArray(
                        mVLAvater, 0, mVLAvater.length));
            } else {
                mVLContactItemViewHolder.mVHHeadIv
                        .setImageResource(R.drawable.skype_img_contact_list_buddy_unknown);
            }
        }
    }

    public void updateStatus() {
        int id;
        mVLAbility = SktApiActor.getContactAvailability(mVLSkypeName);
        switch (mVLAbility) {
            case ONLINE:
                id = R.drawable.presenceonline_32x32;
                break;
            case AWAY:
                id = R.drawable.presenceaway_32x32;
                break;
            case DO_NOT_DISTURB:
                id = R.drawable.presencedonotdisturb_32x32;
                break;
            case BLOCKED:
            case BLOCKED_SKYPEOUT:
                id = R.drawable.presenceblocked_32x32;
                break;
            case SKYPEOUT:
                id = R.drawable.presencephone_32x32;
                break;
            case OFFLINE_BUT_CF_ABLE:
                id = R.drawable.presencecallforward_32x32;
                break;
            case OFFLINE_BUT_VM_ABLE:
            case OFFLINE:
                id = R.drawable.presenceoffline_32x32;
                break;
            default:
                id = R.drawable.presenceunknown_32x32;
                break;
        }
        mVLContactItemViewHolder.mVHStateIv.setImageResource(id);
    }

    public void checkContactAbility() {
        updateTextView(mVLContactItemViewHolder.mVHSendVMTv, SktApiActor.haveVoicemailCapability(mVLSkypeName));
        updateTextView(mVLContactItemViewHolder.mVHSendMSGTv, SktApiActor.haveChatCapability(mVLSkypeName));

        mVLCanBlock = SktApiActor.canBlocked(mVLSkypeName);
        if (mVLCanBlock) {
            mVLContactItemViewHolder.mVHBlockTv.setText(R.string.block);
        } else {
            mVLContactItemViewHolder.mVHBlockTv.setText(R.string.unblock);
        }

        updateCallAbility();
    }

    public void updateCallAbility() {
        boolean bVoice = SktApiActor.havaVoiceCallCapabilityContact(mVLSkypeName);
        boolean bVideo = SktApiActor.havaVideoCallCapabilityContact(mVLSkypeName);

        mVLContactItemViewHolder.mVHVideoCallBtn.setEnabled(bVideo);
        mVLContactItemViewHolder.mVHVideoCallBtn.setFocusable(bVideo);

        mVLContactItemViewHolder.mVHVoiceCallBtn.setEnabled(bVoice);
        mVLContactItemViewHolder.mVHVoiceCallBtn.setFocusable(bVoice);
    }

    public void updateTextView(TextView view, boolean enable) {
        view.setEnabled(enable);
        view.setFocusable(enable);

        if (enable) {
            view.setTextColor(Color.BLACK);
        } else {
            view.setTextColor(Color.GRAY);
        }
    }

    public void setViewListenner() {
        mVLContactItemViewHolder.mVHVideoCallBtn.setOnClickListener(this);
        mVLContactItemViewHolder.mVHVoiceCallBtn.setOnClickListener(this);

        mVLContactItemViewHolder.mVHViewPofileTv.setOnClickListener(this);
        mVLContactItemViewHolder.mVHSendMSGTv.setOnClickListener(this);
        mVLContactItemViewHolder.mVHRemoveTv.setOnClickListener(this);
        mVLContactItemViewHolder.mVHBlockTv.setOnClickListener(this);
        mVLContactItemViewHolder.mVHBackTv.setOnClickListener(this);
        mVLContactItemViewHolder.mVHSendVMTv.setOnClickListener(this);

        mVLContactItemViewHolder.mVHVvcallEndBtn.setOnClickListener(this);

        mVLContactItemViewHolder.mVHSendVMCancelBtn.setOnClickListener(this);
        mVLContactItemViewHolder.mVHSendVMEndcallBtn.setOnClickListener(this);
        mVLContactItemViewHolder.mVHSendVMOKBtn.setOnClickListener(this);

        mVLContactItemViewHolder.mVHVolDownIv.setOnClickListener(this);
        mVLContactItemViewHolder.mVHVolUpIv.setOnClickListener(this);

        mVLContactItemViewHolder.mVHBlockOrRemoveCancelBtn.setOnClickListener(this);
        mVLContactItemViewHolder.mVHBlockOrRemoveYesBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            /*
             * the main funtions:vvcall,viewprofile,sendMs,sendVM,block and remove
             */
            case R.id.btn_contactlist_item_pop_videocall:
                startVVCall(true);
                break;
            case R.id.btn_contactlist_item_pop_voicecall:
                startVVCall(false);
                break;
            case R.id.tv_contactlist_item_pop_viewprofile:
                showViewProfile();
                break;
            case R.id.tv_contactlist_item_pop_sendMSG:
                // show chat dialog and close the pop view
                sendMsg();
                break;
            case R.id.tv_contactlist_item_pop_sendVM:
                startVoiceMail();
                break;
            case R.id.tv_contactlist_item_pop_block:
                blockContact();
                break;
            case R.id.tv_contactlist_item_pop_remove:
                removeContact();
                break;
            /* vvcall */
            case R.id.btn_item_pop_vvcall_endcall:
                SktApiActor.leaveConversation();
                break;
            /* the sendVM funtions:cancel ,endcall and ok */
            case R.id.btn_item_pop_sendVM_cancel:
                sendVMCancel();
                break;
            case R.id.btn_item_pop_sendVM_endcall:
                sendVMEnd();
                break;
            case R.id.btn_item_pop_sendVM_ok:
                sendVMOK();
                break;
            /*
             * the block or remove's yes cancel btn
             */
            case R.id.btn_contactlist_item_pop_blockOrremove_yes:
                blockOrRemoveYes();
                break;
            case R.id.btn_contactlist_item_pop_blockOrremove_cancel:
                blockOrRemoveCancel();
                break;
            /*
             * the back funtion
             */
            case R.id.tv_item_pop_back:
                removeView();
                break;
            default:
                break;
        }
    }

    /*
     * voice video call
     */
    // ----------------------------------------------------------------------------------------------------->
    private void startVVCall(boolean enableVideo) {
        mVLEnableVideo = enableVideo;
        
        if (mVLEnableVideo) {
            if (!SktUtils.cameraExists()) {
                Toast.makeText(mVLContactItemDialog.getOwnerActivity(), R.string.no_camera,
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }
        
        Context parent = mVLContactItemDialog.getOwnerActivity();
        if (!SktUtils.isNetworkAvailable(parent)) {
            Toast.makeText(parent, R.string.internet_connect_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        
        // register the callback ,cause the callback will move to other places so must register when click the call btn
        mVLContactItemDialog.registerConversationReceiver();

        mVLIsCalling = true;
        // show the callout view
        addView(SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDVVCALLREL);
        if (mVLEnableVideo) {
            mVLContactItemViewHolder.mVHVvcallConnectTv.setText(R.string.video_connecting);
        } else {
            mVLContactItemViewHolder.mVHVvcallConnectTv.setText(R.string.voice_connecting);
        }

        if (false == SktApiActor.startConversationCall(mVLSkypeName, mVLEnableVideo)) {
            callStop();
            Toast.makeText(mVLContactItemDialog.getOwnerActivity(), R.string.call_failed,
                    Toast.LENGTH_SHORT).show();
        }else{
            if (null != ((UsrAccountActivity)mVLContactItemDialog.getOwnerActivity()).getService()) {
                ((UsrAccountActivity) mVLContactItemDialog.getOwnerActivity()).getService().setCallOutBool(true);
            }
        }
    }

    public boolean isCalling() {
        return mVLIsCalling;
    }

    public void callStart() {
        // call out
        CallHelper.callOut(
                        mVLContactItemDialog.getOwnerActivity(),
                        mVLSkypeName,
                        mVLEnableVideo,
                        SktApiActor.isPstnUser(mVLSkypeName));
        // stop the animation and init the fragment to the normal style
        mVLIsCalling = false;
        mVLVvCallAnimation.stop();
        mVLContactItemViewHolder.mVHPopMidVvcallRel.setVisibility(View.GONE);
        mVLContactItemViewHolder.mVHVideoCallBtn.requestFocus();
        updateVol(false);
        if (mVLPopTopList.size() > 0){
            mVLPopTopList.remove(mVLPopTopList.size() - 1);
        }
        setMainFunctionVisible(true);
        // unregister the callback
        mVLContactItemDialog.unregisterConversationReceiver();
    }

    public void callStop() {
        mVLIsCalling = false;
        mVLEnableVideo = false;
        mVLVvCallAnimation.stop();
        mVLContactItemViewHolder.mVHPopMidVvcallRel.setVisibility(View.GONE);
        mVLContactItemViewHolder.mVHVideoCallBtn.requestFocus();
        updateVol(false);
        if (mVLPopTopList.size() > 0){
            mVLPopTopList.remove(mVLPopTopList.size() - 1);
        }
        setMainFunctionVisible(true);
        // unregister the callback
        mVLContactItemDialog.unregisterConversationReceiver();
        //add the database
        mVLConvTableResourceHolder = new ConvTableResourceHolder();
        mVLConvTableResourceHolder.setConvName(mVLSkypeName);
        mVLConvTableResourceHolder.setConvDate(mVLDateFormat.getFormatString(System.currentTimeMillis(), "yyyy-MM-dd"));
        mVLConvTableResourceHolder.setConvDuration("00:00:00");
        mVLConvTableResourceHolder.setConvType(SKYPECONSTANT.CONVTYPE.CALLOUT + "");
        mVLConvTableResourceHolder.setConvTime(mVLDateFormat.getFormatString(System.currentTimeMillis(), "HH:mm:ss"));

        ConvTableActor.getInstance().insert(mVLConvTableResourceHolder);
    }

    /*
     * view profile
     */
    // ----------------------------------------------------------------------------------------------------->
    private void showViewProfile() {
        // show Profile view
        addView(SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDPROFILEREL);

        // set viewprofile
        if (AVAILABILITY.SKYPEOUT == mVLAbility || AVAILABILITY.BLOCKED_SKYPEOUT == mVLAbility) {
            mVLContactItemViewHolder.mVHOtherTypeProfileRel.setVisibility(View.VISIBLE);
            mVLContactItemViewHolder.mVHContactProfileRel.setVisibility(View.GONE);
            mVLContactItemViewHolder.mVHProfileOtherTypeTipTv.setText(R.string.mobileph);
            mVLContactItemViewHolder.mVHProfileOtherTypeTv.setText(mVLSkypeName);
        } else {
            mVLContactItemViewHolder.mVHOtherTypeProfileRel.setVisibility(View.GONE);
            mVLContactItemViewHolder.mVHContactProfileRel.setVisibility(View.VISIBLE);

            ProfileHelper profileHelper = new ProfileHelper(mVLSkypeName,
                    mVLContactItemDialog.getContext());

            mVLContactItemViewHolder.mVHProfileSkypeNameTv.setText(mVLSkypeName);
            mVLContactItemViewHolder.mVHProfileBirthDayTv.setText(profileHelper.getBirthDay());
            mVLContactItemViewHolder.mVHProfileGenderTv.setText(profileHelper.getGender());

            mVLContactItemViewHolder.mVHProfileLanguageTv.setText(profileHelper.getLanguage());

            mVLContactItemViewHolder.mVHProfileLocationTv.setText(profileHelper.getLocation());
            mVLContactItemViewHolder.mVHProfilePhoneTv.setText(profileHelper.getMobile());
            mVLContactItemViewHolder.mVHProfileEmailTv.setText(profileHelper.getEmail());
        }

    }

    /*
     * send message
     */
    // ---------------------------------------------------------------------------------------------------->
    private void sendMsg() {
        Bundle bl = new Bundle();
        bl.putString(SKYPECONSTANT.SKYPESTRING.SKYPENAME, mVLSkypeName);
        bl.putString(SKYPECONSTANT.SKYPESTRING.DISPLAYNAME, mVLDisplayName);
        mVLContactItemDialog.getOwnerActivity().showDialog(SKYPECONSTANT.USRACCOUNTDIALOG.CHAT, bl);

        mVLContactItemDialog.dismiss();
    }

    /*
     * send Voice mail will deal by the below functions
     */
    // ----------------------------------------------------------------------------------------------------->

    // send vm click
    private void startVoiceMail() {
    	
        if (!SktUtils.isNetworkAvailable(mVLContactItemDialog.getOwnerActivity())) {
            Toast.makeText(mVLContactItemDialog.getOwnerActivity(), R.string.internet_connect_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        
        SktApiActor.startRecordingVoicemail(mVLSkypeName);
        initVM();
    }

    // callback function when play the greeting
    public void initVM() {
        addView(SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDSENDVMREL);
        mVLVMStatus = SKYPECONSTANT.VOICEMAILSTATUS.INITIALIZE;
        
        //check whether the vm success
        if (null != mVLVMHandler) {
            mVLVMHandler.removeCallbacks(vmFailRunnable);
            mVLVMHandler.postDelayed(vmFailRunnable, VM_WAIT_FOR_SUCCESS);
        }
    }
    
    //remove the vm failed callback
    public void removeVMFailCallback() {
        if (null != mVLVMHandler) {
            mVLVMHandler.removeCallbacks(vmFailRunnable);
        }
    }
    
    Runnable vmFailRunnable = new Runnable() {
        
        @Override
        public void run() {
            vmFailed();
        }
    };
    
    // the vm will lose its status when net wrong and the status is INITIALIZE(not change)
    public void cancelVMWhenNetError(){
        if(mVLVMStatus == SKYPECONSTANT.VOICEMAILSTATUS.INITIALIZE){
            vmFailed();
        }
    }
    // end call click
    private void sendVMEnd() {
        if (SKYPECONSTANT.VOICEMAILSTATUS.INITIALIZE != mVLVMStatus) {// have begin to record the time
            sendVoiceMail();
        } else {
            sendVMCancel();
        }
    }

    // send voicemail
    private void sendVoiceMail() {
        mVLContactItemViewHolder.mVHSendVMTipTv.setText(R.string.sendingVM);
        mVLContactItemViewHolder.mVHSendVMCancelBtn.setVisibility(View.GONE);
        mVLContactItemViewHolder.mVHSendVMEndcallBtn.setVisibility(View.GONE);
        SktApiActor.sendRecordVoicemail();
        mVLVMStatus = SKYPECONSTANT.VOICEMAILSTATUS.SENDED;
        updateOkBtn(false);
    }

    // cancel voicemail
    private void sendVMCancel() {
        SktApiActor.cancelRecordingVoicemail();
        removeVMFailCallback();
        vmCancelled();
    }

    // ok click
    private void sendVMOK() {
        removeView();
    }

    // callback function when start recording
    public void startRecord() {
        mVLContactItemViewHolder.mVHSendVMTipTv.setText(R.string.recordVM);
        updateCancelBtn(true);
        mVLVMStatus = SKYPECONSTANT.VOICEMAILSTATUS.RECORDING;
    }
    
    //execute the cancel callback when the mVLVMStatus is not FAILED
    public void vmCancelled(){
       if(mVLVMStatus == SKYPECONSTANT.VOICEMAILSTATUS.SENDFAILED)
           return;
       
        mVLContactItemViewHolder.mVHSendVMTipTv.setText(R.string.VMcancel);
        mVLContactItemViewHolder.mVHSendVMCancelBtn.setVisibility(View.GONE);
        mVLContactItemViewHolder.mVHSendVMEndcallBtn.setVisibility(View.GONE);
        updateOkBtn(true);
        mVLVMStatus = SKYPECONSTANT.VOICEMAILSTATUS.CANCELLED;
    }

    // callback function when vm sended
    public void vmSended() {
        updateOkBtn(true);
        mVLContactItemViewHolder.mVHSendVMTipTv.setText(R.string.sentVM);
        mVLContactItemViewHolder.mVHSendVMCancelBtn.setVisibility(View.GONE);
        mVLContactItemViewHolder.mVHSendVMEndcallBtn.setVisibility(View.GONE);
        mVLVMStatus = SKYPECONSTANT.VOICEMAILSTATUS.SENDED;
    }
    
    public void vmFailed() {
        if(mVLVMStatus == SKYPECONSTANT.VOICEMAILSTATUS.CANCELLED)
            return;
        updateOkBtn(true);
        mVLContactItemViewHolder.mVHSendVMTipTv.setText(R.string.sentVM_failed);
        mVLContactItemViewHolder.mVHSendVMCancelBtn.setVisibility(View.GONE);
        mVLContactItemViewHolder.mVHSendVMEndcallBtn.setVisibility(View.GONE);
        mVLVMStatus = SKYPECONSTANT.VOICEMAILSTATUS.SENDFAILED;
        SktApiActor.cancelRecordingVoicemail();
    }
    
    public void vmStop(){
        mVLVMStatus = SKYPECONSTANT.VOICEMAILSTATUS.CANCELLED_RECORD_WHEN_INCOMING_CALL;
        removeView();
    }

    // callback function when recording
    public void updateRecordDuration(int duration) {
        mVLContactItemViewHolder.mVHSendVMTimeTv.setText(mVLDateFormat.getFormatString(duration,
                "HH:mm:ss"));
    }

    // update the cancel btn
    private void updateCancelBtn(boolean enable) {
        mVLContactItemViewHolder.mVHSendVMCancelBtn.setVisibility(View.VISIBLE);
        if (enable) {
            mVLContactItemViewHolder.mVHSendVMCancelBtn.setEnabled(true);
            mVLContactItemViewHolder.mVHSendVMCancelBtn.setFocusable(true);
        } else {
            mVLContactItemViewHolder.mVHSendVMCancelBtn.setEnabled(false);
            mVLContactItemViewHolder.mVHSendVMCancelBtn.setFocusable(false);
        }
    }

    // update the ok btn
    private void updateOkBtn(boolean enable) {
        mVLContactItemViewHolder.mVHSendVMOKBtn.setVisibility(View.VISIBLE);
        if (enable) {
            mVLContactItemViewHolder.mVHSendVMOKBtn.setEnabled(true);
            mVLContactItemViewHolder.mVHSendVMOKBtn.setFocusable(true);
            mVLContactItemViewHolder.mVHSendVMOKBtn.requestFocus();
        } else {
            mVLContactItemViewHolder.mVHSendVMOKBtn.setEnabled(false);
            mVLContactItemViewHolder.mVHSendVMOKBtn.setFocusable(false);
        }
    }

    /*
     * block or remove contact
     * functions----------------------------------------------------------------------------------------------------->
     */
    // block
    private void blockContact() {
        mVLBlock = true;
        addView(SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDBLOCK);
    }

    // remove
    private void removeContact() {
        mVLBlock = false;
        addView(SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDREMOVE);
    }

    public void blockOrRemoveCancel() {
        removeView();
    }

    public void blockOrRemoveYes() {
        if (mVLBlock) {
            if (mVLCanBlock) {
                SktApiActor.blockContact(mVLSkypeName);
            } else {
                SktApiActor.unBlockContact(mVLSkypeName);
            }
        } else {
            SktApiActor.removeContact(mVLSkypeName);
        }

        removeView();
        mVLContactItemDialog.dismiss();
    }

    /*
     * other
     * function----------------------------------------------------------------------------------------------------->
     */
    private void setMainFunctionVisible(boolean visible) {
        if (!visible) {
            mVLContactItemViewHolder.mVHPopMainMidRel.setVisibility(View.GONE);
        } else {
            mVLContactItemViewHolder.mVHPopMainMidRel.setVisibility(View.VISIBLE);
        }
    }

    private void updateVol(boolean visible) {
        if (visible) {
            mVLContactItemViewHolder.mVHVolDownIv.setVisibility(View.VISIBLE);
            mVLContactItemViewHolder.mVHVolUpIv.setVisibility(View.VISIBLE);
            mVLContactItemViewHolder.mVHVolTv.setVisibility(View.VISIBLE);
        } else {
            mVLContactItemViewHolder.mVHVolDownIv.setVisibility(View.GONE);
            mVLContactItemViewHolder.mVHVolUpIv.setVisibility(View.GONE);
            mVLContactItemViewHolder.mVHVolTv.setVisibility(View.GONE);
        }
    }

    private void addView(int viewId) {
        switch (viewId) {
            case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDVVCALLREL:
                mVLContactItemViewHolder.mVHPopMidVvcallRel.setVisibility(View.VISIBLE);
                // set the focus and update the vol view
                mVLContactItemViewHolder.mVHVvcallEndBtn.requestFocus();
                updateVol(true);

                // start the Animation
                mVLVvCallAnimation = (AnimationDrawable) mVLContactItemViewHolder.mVHVvcallConnectIv
                        .getBackground();
                mVLVvCallAnimation.start();

                break;
            case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDPROFILEREL:
                mVLContactItemViewHolder.mVHPopMidProfileRel.setVisibility(View.VISIBLE);
                break;
            case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDSENDVMREL:
                mVLContactItemViewHolder.mVHPopMidSendVMRel.setVisibility(View.VISIBLE);

                updateVol(true);
                updateCancelBtn(false);

                mVLContactItemViewHolder.mVHSendVMEndcallBtn.setVisibility(View.VISIBLE);
                mVLContactItemViewHolder.mVHSendVMEndcallBtn.requestFocus();
                mVLContactItemViewHolder.mVHSendVMOKBtn.setVisibility(View.GONE);

                mVLContactItemViewHolder.mVHSendVMTipTv.setText(R.string.play_greet);
                mVLContactItemViewHolder.mVHSendVMTimeTv.setText("");

                // start the recording animation
                mVLVMAnimation = (AnimationDrawable) mVLContactItemViewHolder.mVHSendVMRecordIv
                        .getBackground();
                mVLVMAnimation.start();
                break;
            case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDBLOCK:
                mVLContactItemViewHolder.mVHPopMidBlockOrRemoveRel.setVisibility(View.VISIBLE);
                mVLContactItemViewHolder.mVHBlockOrRemoveYesBtn.requestFocus();
                if (mVLCanBlock)
                    mVLContactItemViewHolder.mVHBlockOrRemoveWarnTv.setText(R.string.block_contact);
                else
                    mVLContactItemViewHolder.mVHBlockOrRemoveWarnTv
                            .setText(R.string.unblock_contact);
                break;
            case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDREMOVE:
                mVLContactItemViewHolder.mVHPopMidBlockOrRemoveRel.setVisibility(View.VISIBLE);
                mVLContactItemViewHolder.mVHBlockOrRemoveYesBtn.requestFocus();
                mVLContactItemViewHolder.mVHBlockOrRemoveWarnTv.setText(R.string.remove_contact);
                break;
            default:
                break;
        }
        mVLPopTopList.add(viewId);
        setMainFunctionVisible(false);
    }

    public void removeView() {
        if (mVLPopTopList.size() > 0) {
            switch (mVLPopTopList.get(mVLPopTopList.size() - 1)) {
                case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDVVCALLREL:
                    SktApiActor.leaveConversation();
                    break;
                case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDPROFILEREL:
                    mVLContactItemViewHolder.mVHPopMidProfileRel.setVisibility(View.GONE);
                    mVLPopTopList.remove(mVLPopTopList.size() - 1);
                    mVLContactItemViewHolder.mVHViewPofileTv.requestFocus();
                    break;
                case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDBLOCK:
                case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDREMOVE:
                    if (mVLBlock) {
                        mVLContactItemViewHolder.mVHBlockTv.requestFocus();
                    } else {
                        mVLContactItemViewHolder.mVHRemoveTv.requestFocus();
                    }

                    mVLContactItemViewHolder.mVHPopMidBlockOrRemoveRel.setVisibility(View.GONE);
                    mVLPopTopList.remove(mVLPopTopList.size() - 1);
                    break;
                case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDSENDVMREL:
                    switch (mVLVMStatus) {
                        case SKYPECONSTANT.VOICEMAILSTATUS.INITIALIZE:
                        case SKYPECONSTANT.VOICEMAILSTATUS.RECORDING:
                            sendVMCancel();
                            break;
                        case SKYPECONSTANT.VOICEMAILSTATUS.CANCELLED:
                        case SKYPECONSTANT.VOICEMAILSTATUS.SENDED:
                        case SKYPECONSTANT.VOICEMAILSTATUS.CANCELLED_RECORD_WHEN_INCOMING_CALL:
                        case SKYPECONSTANT.VOICEMAILSTATUS.SENDFAILED:
                            mVLVMAnimation.stop();
                            updateVol(false);
                            mVLContactItemViewHolder.mVHPopMidSendVMRel.setVisibility(View.GONE);
                            mVLPopTopList.remove(mVLPopTopList.size() - 1);
                            mVLContactItemViewHolder.mVHSendVMTv.requestFocus();
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        } else {
            mVLContactItemDialog.dismiss();
        }
        setMainFunctionVisible(true);
    }
}
