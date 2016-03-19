
package com.jrm.skype.dialog;

import java.util.ArrayList;
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

import com.jrm.skype.api.SktConversation;
import com.jrm.skype.api.SktUtils;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.datebase.ConvTableActor;
import com.jrm.skype.datebase.ConvTableResourceHolder;
import com.jrm.skype.dialog.HistoryDialog;
import com.jrm.skype.ui.UsrAccountActivity;
import com.jrm.skype.ui.R;
import com.jrm.skype.util.CallHelper;
import com.jrm.skype.util.DateFormat;
import com.jrm.skype.util.ProfileHelper;
import com.jrm.skype.util.SktApiActor;
import com.skype.api.Contact;
import com.skype.api.Contact.AVAILABILITY;

/**
 * @author andy.liu the viewlistenner of HistoryItemDialog
 */

public class HistoryViewListenner implements OnClickListener {
    private HistoryViewHolder mVLHistoryItemViewHolder;

    private HistoryDialog mVLHistoryItemDialog;

    private DateFormat mVLDateFormat;

    private ArrayList<Integer> mVLPopTopList; // to record the pop view

    private String mVLConvName;

    private String mVLDisplayName;

    private int mVLConvType;

    private byte[] mVLAvater;

    private Contact.AVAILABILITY mVLAbility;

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
    private AnimationDrawable mVLSendVMAnimation;

    private int mVLVMStatus;
    
    private Handler mVLVMHandler;
    
    private final int VM_WAIT_FOR_SUCCESS = 15000;


    /*
     * listen to voice mail will use the following variables
     */
    private AnimationDrawable mVLListenVMAnimation;

    private int mVLVMId;

    private int mVLVMLength;

    /*
     * add contact or delete itm share the same layout ,need a mark
     */
    private boolean mVLAddContact;

    public HistoryViewListenner(HistoryViewHolder mVLHistoryItemViewHolder,
            HistoryDialog mVLHistoryItemDialog) {
        super();
        this.mVLHistoryItemViewHolder = mVLHistoryItemViewHolder;
        this.mVLHistoryItemDialog = mVLHistoryItemDialog;

        mVLDateFormat = new DateFormat();
        mVLVMHandler = new Handler();
    }

    public void initVar(Bundle bl) {
        if (null == bl) {
            return;
        }

        if (null == mVLPopTopList) {
            mVLPopTopList = new ArrayList<Integer>();
        }
        mVLPopTopList.clear();
        mVLIsCalling = false;

        mVLConvName = bl.getString(SKYPECONSTANT.SKYPESTRING.CONVNAME, "");
        mVLDisplayName = bl.getString(SKYPECONSTANT.SKYPESTRING.DISPLAYNAME, "");
        mVLVMId = bl.getInt(SKYPECONSTANT.SKYPESTRING.VOICEMAILID, -1);
        mVLConvType = bl.getInt(SKYPECONSTANT.SKYPESTRING.CONVTYPE, -1);

        mVLHistoryItemViewHolder.mVHConatctDisNameTv.setText(mVLDisplayName);

        updateConvType(mVLConvType);
        checkConvAbility();
        updateAvater();
    }

    public void updateAvater() {
        mVLAbility = SktApiActor.getContactAvailability(mVLConvName);

        if (AVAILABILITY.SKYPEOUT == mVLAbility || AVAILABILITY.BLOCKED_SKYPEOUT == mVLAbility) {
            mVLHistoryItemViewHolder.mVHHeadIv
                    .setImageResource(R.drawable.skype_img_contact_list_buddy_phone);
        } else {
            mVLAvater = SktApiActor.getConvAvatar(mVLConvName);
            if (null != mVLAvater && mVLAvater.length > 0)
                mVLHistoryItemViewHolder.mVHHeadIv.setImageBitmap(BitmapFactory.decodeByteArray(
                        mVLAvater, 0, mVLAvater.length));
            else {

                mVLHistoryItemViewHolder.mVHHeadIv
                        .setImageResource(R.drawable.skype_img_contact_list_buddy_unknown);
            }
        }
    }

    public void updateConvType(int type) {
        switch (type) {
            case SKYPECONSTANT.CONVTYPE.CALLIN:
                mVLHistoryItemViewHolder.mVHStateIv.setBackgroundResource(R.drawable.callin_32x32);
                break;
            case SKYPECONSTANT.CONVTYPE.CALLOUT:
                mVLHistoryItemViewHolder.mVHStateIv.setBackgroundResource(R.drawable.callout_32x32);
                break;
            case SKYPECONSTANT.CONVTYPE.CONFERENCEIN:
                mVLHistoryItemViewHolder.mVHStateIv
                        .setBackgroundResource(R.drawable.callconferencein_32x32);
                break;
            case SKYPECONSTANT.CONVTYPE.NEWMESSAGE:
                mVLHistoryItemViewHolder.mVHStateIv.setBackgroundResource(R.drawable.new_msg);
                break;
            case SKYPECONSTANT.CONVTYPE.OLDMESSAGE:
                mVLHistoryItemViewHolder.mVHStateIv.setBackgroundResource(R.drawable.old_msg);
                break;
            case SKYPECONSTANT.CONVTYPE.VOICEMAILNEW:
                mVLHistoryItemViewHolder.mVHStateIv
                        .setBackgroundResource(R.drawable.callvoicemailnew_32x32);
                break;
            case SKYPECONSTANT.CONVTYPE.VOICEMAIL:
                mVLHistoryItemViewHolder.mVHStateIv
                        .setBackgroundResource(R.drawable.callvoicemail_32x32);
                break;
            default:
                mVLHistoryItemViewHolder.mVHStateIv.setBackgroundResource(R.drawable.callin_32x32);
        }
    }

    public void checkConvAbility() {
        // init send msg
        if (SKYPECONSTANT.CONVTYPE.NEWMESSAGE == mVLConvType) {
            mVLHistoryItemViewHolder.mVHSendMSGTv.setText(R.string.checkMS);
        } else {
            mVLHistoryItemViewHolder.mVHSendMSGTv.setText(R.string.sendMS);
        }

        updateTextView(mVLHistoryItemViewHolder.mVHSendVMTv, SktApiActor.haveVoicemailCapabilityConv(mVLConvName));
        updateTextView(mVLHistoryItemViewHolder.mVHSendMSGTv, SktApiActor.haveChatCapability(mVLConvName));

        updateTextView(mVLHistoryItemViewHolder.mVHAddToContactTv, SktApiActor.canAddtoBuddyList(mVLConvName));

        if (SKYPECONSTANT.CONVTYPE.VOICEMAIL == mVLConvType
                || SKYPECONSTANT.CONVTYPE.VOICEMAILNEW == mVLConvType) {
            updateTextView(mVLHistoryItemViewHolder.mVHLisToVMTv, true);
        } else {
            updateTextView(mVLHistoryItemViewHolder.mVHLisToVMTv, false);
        }

        updateCallAbility();
    }

    public void updateCallAbility() {
        boolean bVoice = SktApiActor.havaVoiceCallCapabilityConv(mVLConvName);
        boolean bVideo = SktApiActor.havaVideoCallCapabilityConv(mVLConvName);

        mVLHistoryItemViewHolder.mVHVideoCallBtn.setEnabled(bVideo);
        mVLHistoryItemViewHolder.mVHVideoCallBtn.setFocusable(bVideo);

        mVLHistoryItemViewHolder.mVHVoiceCallBtn.setEnabled(bVoice);
        mVLHistoryItemViewHolder.mVHVoiceCallBtn.setFocusable(bVoice);
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
        mVLHistoryItemViewHolder.mVHVideoCallBtn.setOnClickListener(this);
        mVLHistoryItemViewHolder.mVHVoiceCallBtn.setOnClickListener(this);

        mVLHistoryItemViewHolder.mVHViewPofileTv.setOnClickListener(this);
        mVLHistoryItemViewHolder.mVHSendMSGTv.setOnClickListener(this);
        mVLHistoryItemViewHolder.mVHDeleteItemTv.setOnClickListener(this);
        mVLHistoryItemViewHolder.mVHLisToVMTv.setOnClickListener(this);
        mVLHistoryItemViewHolder.mVHBackTv.setOnClickListener(this);
        mVLHistoryItemViewHolder.mVHSendVMTv.setOnClickListener(this);
        mVLHistoryItemViewHolder.mVHAddToContactTv.setOnClickListener(this);
        mVLHistoryItemViewHolder.mVHDeleteItemTv.setOnClickListener(this);

        mVLHistoryItemViewHolder.mVHVvcallEndBtn.setOnClickListener(this);
        mVLHistoryItemViewHolder.mVHSendVMCancelBtn.setOnClickListener(this);
        mVLHistoryItemViewHolder.mVHSendVMEndcallBtn.setOnClickListener(this);
        mVLHistoryItemViewHolder.mVHSendVMOKBtn.setOnClickListener(this);

        mVLHistoryItemViewHolder.mVHAddContactOrDeleteCancelBtn.setOnClickListener(this);
        mVLHistoryItemViewHolder.mVHAddContactOrDeleteOKBtn.setOnClickListener(this);

        mVLHistoryItemViewHolder.mVHLisToVMStopBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            /*
             * the main functions:vvcall,viewprofile,sendMS,sendVM,Listen VM,add and delete
             */
            case R.id.btn_historylist_item_pop_videocall:
                startVVCall(true);
                break;
            case R.id.btn_historylist_item_pop_voicecall:
                startVVCall(false);
                break;
            case R.id.tv_historylist_item_pop_viewprofile:
                showViewProfile();
                break;
            case R.id.tv_historylist_item_pop_sendMSG:
                // show chat dialog and close the pop view
                sendMsg();
                break;
            case R.id.tv_historylist_item_pop_sendVM:
                startVoiceMail();
                break;
            case R.id.tv_historylist_item_pop_LisToVM:
                listenVoiceMail();
                break;
            case R.id.tv_historylist_item_pop_addtocontact:
                addContact();
                break;
            case R.id.tv_historylist_item_pop_deleteitem:
                deleteItem();
                break;

            /* vvcall */
            case R.id.btn_item_pop_vvcall_endcall:
             	SktConversation conv = SktUtils.getConversation();
            	if (null != conv) {
            		conv.leaveConversationCall();
            	}
                break;
            /* the startVoiceMail functions:cancel ,endcall and ok */
            case R.id.btn_item_pop_sendVM_cancel:
                sendVMCancel();
                break;
            case R.id.btn_item_pop_sendVM_endcall:
                sendVMEnd();
                break;
            case R.id.btn_item_pop_sendVM_ok:
                sendVMOK();
                break;
            /* the add to contact functions: ok , cancel */
            case R.id.btn_historylist_item_pop_addcontactOrdelete_ok:
                addContactOrDeleteOK();
                break;
            case R.id.btn_historylist_item_pop_addcontactOrdelete_cancel:
                addContactOrDeleteCancel();
                break;
            /* the listen to vm function */
            case R.id.btn_historylist_item_pop_lisToVM_stop:
                listenVMStop();
                break;
            /* the back function */
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
            if (!SktUtils.cameraExists()){
                Toast.makeText(mVLHistoryItemDialog.getOwnerActivity(), R.string.no_camera,
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }
        
        Context parent = mVLHistoryItemDialog.getOwnerActivity();
        if (!SktUtils.isNetworkAvailable(parent)) {
            Toast.makeText(parent, R.string.internet_connect_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        
        // register the callback ,cause the callback will move to other places so must register when click the call btn
        mVLHistoryItemDialog.registerConversationReceiver();

        mVLIsCalling = true;
        addView(SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDVVCALLREL);

        if (mVLEnableVideo) {
            mVLHistoryItemViewHolder.mVHVvcallConnectTv.setText(R.string.video_connecting);
        } else {
            mVLHistoryItemViewHolder.mVHVvcallConnectTv.setText(R.string.voice_connecting);
        }

        if (false == SktApiActor.startConversationCall(mVLConvName, mVLEnableVideo)) {
            callStop();
            Toast.makeText(mVLHistoryItemDialog.getOwnerActivity(), R.string.call_failed,
                    Toast.LENGTH_SHORT).show();
        }else{
            if (null != ((UsrAccountActivity)mVLHistoryItemDialog.getOwnerActivity()).getService()) {
                ((UsrAccountActivity) mVLHistoryItemDialog.getOwnerActivity()).getService().setCallOutBool(true);
            }
        }
    }

    public void callStart() {
        // call out
        CallHelper.callOut(
                        mVLHistoryItemDialog.getOwnerActivity(),
                        mVLConvName,
                        mVLEnableVideo,
                        SktApiActor.isPstnUser(mVLConvName));
        // stop the animation and init the fragment to the normal style
        mVLVvCallAnimation.stop();
        mVLHistoryItemViewHolder.mVHPopMidVvcallRel.setVisibility(View.GONE);
        mVLHistoryItemViewHolder.mVHVideoCallBtn.requestFocus();
        updateVol(false);
        if (mVLPopTopList.size() > 0) {
            mVLPopTopList.remove(mVLPopTopList.size() - 1);
        }
        setMainFunctionVisible(true);
        // unregister the callback
        mVLHistoryItemDialog.unregisterConversationReceiver();
    }

    public boolean isCalling() {
        return mVLIsCalling;
    }

    public void callStop() {
        mVLIsCalling = false;
        mVLEnableVideo = false;
        mVLVvCallAnimation.stop();
        mVLHistoryItemViewHolder.mVHPopMidVvcallRel.setVisibility(View.GONE);
        mVLHistoryItemViewHolder.mVHVideoCallBtn.requestFocus();
        updateVol(false);
        if (mVLPopTopList.size() > 0) {
            mVLPopTopList.remove(mVLPopTopList.size() - 1);
        }
        setMainFunctionVisible(true);
        // unregister the callback
        mVLHistoryItemDialog.unregisterConversationReceiver();
        
        //add the database
        mVLConvTableResourceHolder = new ConvTableResourceHolder();
        mVLConvTableResourceHolder.setConvName(mVLConvName);
        mVLConvTableResourceHolder.setConvDate(mVLDateFormat.getFormatString(System.currentTimeMillis(), "yyyy-MM-dd"));
        mVLConvTableResourceHolder.setConvDuration("00:00:00");
        mVLConvTableResourceHolder.setConvType(SKYPECONSTANT.CONVTYPE.CALLOUT + "");
        mVLConvTableResourceHolder.setConvTime(mVLDateFormat.getFormatString(System.currentTimeMillis(), "HH:mm:ss"));

        ConvTableActor.getInstance().insert(mVLConvTableResourceHolder);
        // update the history
        ((UsrAccountActivity) mVLHistoryItemDialog.getOwnerActivity()).getViewHolder().mVHHistoryListFragment
                .updateHistory();
    }

    /*
     * view profile
     */
    // ----------------------------------------------------------------------------------------------------->
    private void showViewProfile() {
        // show Profile view
        addView(SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDPROFILEREL);

        // set viewprofile
        if (SktApiActor.isConferenceConv(mVLConvName)) {
            mVLHistoryItemViewHolder.mVHOtherTypeProfileRel.setVisibility(View.VISIBLE);
            mVLHistoryItemViewHolder.mVHContactProfileRel.setVisibility(View.GONE);
            mVLHistoryItemViewHolder.mVHProfileOtherTypeTipTv.setText(R.string.participant_count);
            mVLHistoryItemViewHolder.mVHProfileOtherTypeTv.setText(""
                    + SktApiActor.getParticipantsSize(mVLConvName));
        } else if (AVAILABILITY.SKYPEOUT == mVLAbility
                || AVAILABILITY.BLOCKED_SKYPEOUT == mVLAbility) {
            mVLHistoryItemViewHolder.mVHOtherTypeProfileRel.setVisibility(View.VISIBLE);
            mVLHistoryItemViewHolder.mVHContactProfileRel.setVisibility(View.GONE);
            mVLHistoryItemViewHolder.mVHProfileOtherTypeTipTv.setText(R.string.mobileph);
            mVLHistoryItemViewHolder.mVHProfileOtherTypeTv.setText(mVLConvName);
        } else {
            mVLHistoryItemViewHolder.mVHOtherTypeProfileRel.setVisibility(View.GONE);
            mVLHistoryItemViewHolder.mVHContactProfileRel.setVisibility(View.VISIBLE);

            ProfileHelper profileHelper = new ProfileHelper(mVLConvName,
                    mVLHistoryItemDialog.getContext());

            mVLHistoryItemViewHolder.mVHProfileSkypeNameTv.setText(mVLConvName);
            mVLHistoryItemViewHolder.mVHProfileBirthDayTv.setText(profileHelper.getBirthDay());
            mVLHistoryItemViewHolder.mVHProfileGenderTv.setText(profileHelper.getGender());

            mVLHistoryItemViewHolder.mVHProfileLanguageTv.setText(profileHelper.getLanguage());

            mVLHistoryItemViewHolder.mVHProfileLocationTv.setText(profileHelper.getLocation());
            mVLHistoryItemViewHolder.mVHProfilePhoneTv.setText(profileHelper.getMobile());
            mVLHistoryItemViewHolder.mVHProfileEmailTv.setText(profileHelper.getEmail());
        }
    }

    /*
     * send message
     */
    // ---------------------------------------------------------------------------------------------------->
    private void sendMsg() {
        Bundle bl = new Bundle();
        bl.putString(SKYPECONSTANT.SKYPESTRING.SKYPENAME, mVLConvName);
        bl.putString(SKYPECONSTANT.SKYPESTRING.DISPLAYNAME, mVLDisplayName);
        mVLHistoryItemDialog.getOwnerActivity().showDialog(SKYPECONSTANT.USRACCOUNTDIALOG.CHAT, bl);

        mVLHistoryItemDialog.dismiss();
    }

    /*
     * send Voice mail will deal by the below functions
     */
    // ----------------------------------------------------------------------------------------------------->

    // send vm click
    private void startVoiceMail() {
    	
        Context parent = mVLHistoryItemDialog.getOwnerActivity();
        if (!SktUtils.isNetworkAvailable(parent)) {
            Toast.makeText(parent, R.string.internet_connect_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        
        SktApiActor.startRecordingVoicemail(mVLConvName);
        initVM();
    }

    // callback function when play the greeting
    public void initVM() {
        addView(SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDSENDVMREL);
        mVLVMStatus = SKYPECONSTANT.VOICEMAILSTATUS.INITIALIZE;
        
        if (null != mVLVMHandler) {
            mVLVMHandler.removeCallbacks(vmFailRunnable);
            mVLVMHandler.postDelayed(vmFailRunnable, VM_WAIT_FOR_SUCCESS);
        }
    }

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
    // end call of voicemail
    private void sendVMEnd() {
        if (SKYPECONSTANT.VOICEMAILSTATUS.INITIALIZE != mVLVMStatus) {// have begin to record the time
            sendVoiceMail();
        } else {
            sendVMCancel();
        }
    }

    // send voicemail
    private void sendVoiceMail() {
        mVLHistoryItemViewHolder.mVHSendVMTipTv.setText(R.string.sendingVM);
        mVLHistoryItemViewHolder.mVHSendVMCancelBtn.setVisibility(View.GONE);
        mVLHistoryItemViewHolder.mVHSendVMEndcallBtn.setVisibility(View.GONE);
        updateOkBtn(false);
        SktApiActor.sendRecordVoicemail();
        mVLVMStatus = SKYPECONSTANT.VOICEMAILSTATUS.SENDED;
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
        mVLHistoryItemViewHolder.mVHSendVMTipTv.setText(R.string.recordVM);
        updateCancelBtn(true);
        mVLVMStatus = SKYPECONSTANT.VOICEMAILSTATUS.RECORDING;
    }
    //execute the cancel callback when the mVLVMStatus is not FAILED
    public void vmCancelled() {
        if (mVLVMStatus == SKYPECONSTANT.VOICEMAILSTATUS.SENDFAILED)
            return;

        mVLHistoryItemViewHolder.mVHSendVMTipTv.setText(R.string.VMcancel);
        mVLHistoryItemViewHolder.mVHSendVMCancelBtn.setVisibility(View.GONE);
        mVLHistoryItemViewHolder.mVHSendVMEndcallBtn.setVisibility(View.GONE);
        updateOkBtn(true);
        mVLVMStatus = SKYPECONSTANT.VOICEMAILSTATUS.CANCELLED;
    }

    // callback function when vm sended
    public void vmSended() {
        updateOkBtn(true);
        mVLVMStatus = SKYPECONSTANT.VOICEMAILSTATUS.SENDED;
        mVLHistoryItemViewHolder.mVHSendVMTipTv.setText(R.string.sentVM);
        mVLHistoryItemViewHolder.mVHSendVMCancelBtn.setVisibility(View.GONE);
        mVLHistoryItemViewHolder.mVHSendVMEndcallBtn.setVisibility(View.GONE);
    }

    public void vmFailed() {
        if(mVLVMStatus == SKYPECONSTANT.VOICEMAILSTATUS.CANCELLED)
            return;
        updateOkBtn(true);
        mVLHistoryItemViewHolder.mVHSendVMTipTv.setText(R.string.sentVM_failed);
        mVLHistoryItemViewHolder.mVHSendVMCancelBtn.setVisibility(View.GONE);
        mVLHistoryItemViewHolder.mVHSendVMEndcallBtn.setVisibility(View.GONE);
        mVLVMStatus = SKYPECONSTANT.VOICEMAILSTATUS.SENDFAILED;
        SktApiActor.cancelRecordingVoicemail();
    }
    // callback function when recording
    public void updateRecordDuration(int duration) {
        mVLHistoryItemViewHolder.mVHSendVMTimeTv.setText(mVLDateFormat.getFormatString(duration,
                "HH:mm:ss"));
    }

    // update the cancel btn
    public void updateCancelBtn(boolean enable) {
        mVLHistoryItemViewHolder.mVHSendVMCancelBtn.setVisibility(View.VISIBLE);
        if (enable) {
            mVLHistoryItemViewHolder.mVHSendVMCancelBtn.setEnabled(true);
            mVLHistoryItemViewHolder.mVHSendVMCancelBtn.setFocusable(true);
        } else {
            mVLHistoryItemViewHolder.mVHSendVMCancelBtn.setEnabled(false);
            mVLHistoryItemViewHolder.mVHSendVMCancelBtn.setFocusable(false);
        }
    }

    // update the ok btn
    public void updateOkBtn(boolean enable) {
        mVLHistoryItemViewHolder.mVHSendVMOKBtn.setVisibility(View.VISIBLE);
        if (enable) {
            mVLHistoryItemViewHolder.mVHSendVMOKBtn.setEnabled(true);
            mVLHistoryItemViewHolder.mVHSendVMOKBtn.setFocusable(true);
            mVLHistoryItemViewHolder.mVHSendVMOKBtn.requestFocus();
        } else {
            mVLHistoryItemViewHolder.mVHSendVMOKBtn.setEnabled(false);
            mVLHistoryItemViewHolder.mVHSendVMOKBtn.setFocusable(false);
        }
    }
    
    public void vmStop(){
        mVLVMStatus = SKYPECONSTANT.VOICEMAILSTATUS.CANCELLED_RECORD_WHEN_INCOMING_CALL;
        removeView();
    }

    /*
     * listen to voice mail functions
     */
    // -------------------------------------------------------------------------------------------------->
    private void listenVoiceMail() {
        Context parent = mVLHistoryItemDialog.getOwnerActivity();
        if (!SktUtils.isNetworkAvailable(parent)) {
            Toast.makeText(parent, R.string.internet_connect_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        // show the view and add to top list
        addView(SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDLISTENVMREL);

        SktApiActor.playbackVoicemail(mVLVMId);
        // get the vm length and set the text of textview
        mVLHistoryItemViewHolder.mVHLisToVMCTipTv.setText(R.string.VM);
        mVLHistoryItemViewHolder.mVHLisToVMCurrentTv.setText("00:00");
        mVLHistoryItemViewHolder.mVHLisToVMentireTv.setText("00:00");
        mVLHistoryItemViewHolder.mVHLisToVMPb.setProgress(0);

        // update the voicemail from new to old
        ((UsrAccountActivity) mVLHistoryItemDialog.getOwnerActivity()).getViewHolder().mVHHistoryListFragment
                .updateVMType();
        mVLHistoryItemViewHolder.mVHStateIv.setBackgroundResource(R.drawable.callvoicemail_32x32);
    }

    public void listenVMStop() {
        removeView();
    }

    public void beginPaly(int vmlength) {
        if (0 >= vmlength)
            return;
        mVLVMLength = vmlength;
        mVLHistoryItemViewHolder.mVHLisToVMentireTv.setText(mVLDateFormat.getFormatString(
                mVLVMLength, "mm:ss"));
    }

    public void playProgress(int playTime) {
        int progress = 0;
        mVLHistoryItemViewHolder.mVHLisToVMCurrentTv.setText(mVLDateFormat.getFormatString(playTime,
                "mm:ss"));
        if (0 != mVLVMLength)
            progress = (int) playTime * mVLHistoryItemViewHolder.mVHLisToVMPb.getMax()
                    / mVLVMLength;
        mVLHistoryItemViewHolder.mVHLisToVMPb.setProgress(progress);
    }

    public void played() {
        mVLHistoryItemViewHolder.mVHLisToVMCurrentTv.setText(mVLDateFormat.getFormatString(
                mVLVMLength, "mm:ss"));
        mVLHistoryItemViewHolder.mVHLisToVMPb.setProgress(mVLHistoryItemViewHolder.mVHLisToVMPb
                .getMax());
    }
    
    public void recFailed(){
        mVLHistoryItemViewHolder.mVHLisToVMCTipTv.setText(R.string.recVM_failed);
    }
    
    /*
     * add to contact functions
     */
    // --------------------------------------------------------------->
    private void addContact() {
        mVLAddContact = true;
        addView(SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDADDTOCONTACTREL);
    }

    private void deleteItem() {
        mVLAddContact = false;
        addView(SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDDELETEITEMREL);
    }

    public void addContactOrDeleteCancel() {
        removeView();
    }

    public void addContactOrDeleteOK() {
        if (mVLAddContact) {
            SktApiActor.addContact(mVLConvName,
                    mVLHistoryItemDialog.getOwnerActivity().getString(R.string.request_to_add));
        } else {
            ((UsrAccountActivity) mVLHistoryItemDialog.getOwnerActivity()).getViewHolder().mVHHistoryListFragment
                    .deleteItem();
        }

        removeView();
        mVLHistoryItemDialog.dismiss();
    }

    /*
     * other function
     */
    // ----------------------------------------------------------------------------------------------------->
    public void setMainFunctionVisible(boolean visible) {
        if (!visible) {
            mVLHistoryItemViewHolder.mVHPopMainMidRel.setVisibility(View.GONE);
        } else {
            mVLHistoryItemViewHolder.mVHPopMainMidRel.setVisibility(View.VISIBLE);
        }
    }

    public void updateVol(boolean visible) {
        if (visible) {
            mVLHistoryItemViewHolder.mVHVolDownIv.setVisibility(View.VISIBLE);
            mVLHistoryItemViewHolder.mVHVolUpIv.setVisibility(View.VISIBLE);
            mVLHistoryItemViewHolder.mVHVolTv.setVisibility(View.VISIBLE);
        } else {
            mVLHistoryItemViewHolder.mVHVolDownIv.setVisibility(View.GONE);
            mVLHistoryItemViewHolder.mVHVolUpIv.setVisibility(View.GONE);
            mVLHistoryItemViewHolder.mVHVolTv.setVisibility(View.GONE);
        }
    }

    private void addView(int viewId) {
        switch (viewId) {
            case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDVVCALLREL:
                mVLHistoryItemViewHolder.mVHPopMidVvcallRel.setVisibility(View.VISIBLE);

                mVLHistoryItemViewHolder.mVHVvcallEndBtn.requestFocus();
                updateVol(true);

                mVLVvCallAnimation = (AnimationDrawable) mVLHistoryItemViewHolder.mVHVvcallConnectIv
                        .getBackground();
                mVLVvCallAnimation.start();
                break;
            case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDPROFILEREL:
                mVLHistoryItemViewHolder.mVHPopMidProfileRel.setVisibility(View.VISIBLE);
                break;
            case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDSENDVMREL:
                mVLHistoryItemViewHolder.mVHPopMidSendVMRel.setVisibility(View.VISIBLE);
                updateVol(true);
                updateCancelBtn(false);

                mVLHistoryItemViewHolder.mVHSendVMEndcallBtn.setVisibility(View.VISIBLE);
                mVLHistoryItemViewHolder.mVHSendVMEndcallBtn.requestFocus();
                mVLHistoryItemViewHolder.mVHSendVMOKBtn.setVisibility(View.GONE);

                mVLHistoryItemViewHolder.mVHSendVMTipTv.setText(R.string.play_greet);
                mVLHistoryItemViewHolder.mVHSendVMTimeTv.setText("");

                // start the recording animation
                mVLSendVMAnimation = (AnimationDrawable) mVLHistoryItemViewHolder.mVHSendVMRecordIv
                        .getBackground();
                mVLSendVMAnimation.start();
                break;
            case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDLISTENVMREL:
                mVLHistoryItemViewHolder.mVHPopMidLisToVMRel.setVisibility(View.VISIBLE);
                updateVol(true);

                mVLHistoryItemViewHolder.mVHLisToVMStopBtn.requestFocus();
                // start the animation
                mVLListenVMAnimation = (AnimationDrawable) mVLHistoryItemViewHolder.mVHLisToVMPlayIm
                        .getBackground();
                mVLListenVMAnimation.start();
                break;
            case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDADDTOCONTACTREL:
                mVLHistoryItemViewHolder.mVHPopMidAddContactOrDeleteRel.setVisibility(View.VISIBLE);
                mVLHistoryItemViewHolder.mVHAddContactOrDeleteOKBtn.requestFocus();
                mVLHistoryItemViewHolder.mVHAddContactOrDeleteWarnTv
                        .setText(R.string.addto_contact_);
                break;
            case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDDELETEITEMREL:
                mVLHistoryItemViewHolder.mVHPopMidAddContactOrDeleteRel.setVisibility(View.VISIBLE);
                mVLHistoryItemViewHolder.mVHAddContactOrDeleteOKBtn.requestFocus();
                mVLHistoryItemViewHolder.mVHAddContactOrDeleteWarnTv.setText(R.string.delete_item_);
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
                    mVLHistoryItemViewHolder.mVHPopMidProfileRel.setVisibility(View.GONE);
                    mVLPopTopList.remove(mVLPopTopList.size() - 1);
                    mVLHistoryItemViewHolder.mVHViewPofileTv.requestFocus();
                    break;
                case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDDELETEITEMREL:
                case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDADDTOCONTACTREL:
                    if (mVLAddContact)
                        mVLHistoryItemViewHolder.mVHAddToContactTv.requestFocus();
                    else
                        mVLHistoryItemViewHolder.mVHDeleteItemTv.requestFocus();

                    mVLHistoryItemViewHolder.mVHPopMidAddContactOrDeleteRel
                            .setVisibility(View.GONE);
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
                            mVLSendVMAnimation.stop();
                            updateVol(false);
                            mVLHistoryItemViewHolder.mVHPopMidSendVMRel.setVisibility(View.GONE);
                            mVLPopTopList.remove(mVLPopTopList.size() - 1);
                            mVLHistoryItemViewHolder.mVHSendVMTv.requestFocus();
                            break;
                        default:
                            break;
                    }
                    break;
                case SKYPECONSTANT.USRACCOUNTPOPDIALOG.MIDLISTENVMREL:
                    SktApiActor.stopPlaybackVoicemail();
                    mVLListenVMAnimation.stop();
                    mVLHistoryItemViewHolder.mVHPopMidLisToVMRel.setVisibility(View.GONE);
                    mVLPopTopList.remove(mVLPopTopList.size() - 1);
                    updateVol(false);
                    mVLHistoryItemViewHolder.mVHLisToVMTv.requestFocus();
                    break;
                default:
                    break;
            }
        } else {
            mVLHistoryItemDialog.dismiss();
        }
        setMainFunctionVisible(true);

    }

}
