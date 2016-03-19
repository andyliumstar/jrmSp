
package com.jrm.skype.dialog;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.ui.UsrAccountActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

/**
 * @author andy.liu A dialog that contains all the contact list's function: View Profile , SendMessage, Send VocieMail
 *         Block, Remove
 */
public class ContactDialog extends MainDialog {
    private ContactViewHolder mContactItemViewHolder;

    private ContactViewListenner mContactItemViewListenner;

    private boolean mIsRegisterVMReceiver;

    private boolean mIsRegisterConvReceiver;

    public ContactDialog(Context context, int theme) {
        super(context, theme);
        mContactItemViewHolder = new ContactViewHolder(this);
        mContactItemViewListenner = new ContactViewListenner(mContactItemViewHolder, this);
    }

    @Override
    public void findView() {
        mContactItemViewHolder.findView();
    }

    @Override
    public void setViewListenner() {
        mContactItemViewListenner.setViewListenner();
    }

    @Override
    public void initVar(Bundle args) {
        mIsRegisterVMReceiver = false;
        mIsRegisterConvReceiver = false;
        mContactItemViewListenner.initVar(args);
        // different with the ConversationReceiver(will change), the VoiceMailReceiver can register when the dialog
        // shows
        registerVoiceMailReceiver();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mContactItemViewListenner.removeView();
            return true;
        } else if (mContactItemViewHolder.mVHPopMidProfileRel.getVisibility() == View.VISIBLE)
            // disable the key when the contact profile is show
            return true;
        else
            return false;
    }

    public ContactViewListenner getViewListenner() {
        return mContactItemViewListenner;
    }

    @Override
    public void dismiss() {
        unregisterVoiceMailReceiver();
        super.dismiss();
    }

    private void registerVoiceMailReceiver() {
        getOwnerActivity().registerReceiver(mVoiceMailStatusChangeReceiver,
                new IntentFilter(SKYPECONSTANT.SKYPEACTION.CONTACT_VOICEMAILSTATUS_CHANGE));
        if (null != ((UsrAccountActivity) getOwnerActivity()).getService()){
            ((UsrAccountActivity) getOwnerActivity()).getService().setVoiceMailStatusChangeAction(
                    SKYPECONSTANT.SKYPEACTION.CONTACT_VOICEMAILSTATUS_CHANGE);
        }
 
        mIsRegisterVMReceiver = true;
    }

    private void unregisterVoiceMailReceiver() {
        if (mIsRegisterVMReceiver) {
            getOwnerActivity().unregisterReceiver(mVoiceMailStatusChangeReceiver);
            mIsRegisterVMReceiver = false;
        }
    }

    public void registerConversationReceiver() {
        getOwnerActivity().registerReceiver(mConvStatusChangeReceiver,
                new IntentFilter(SKYPECONSTANT.SKYPEACTION.CONTACT_CONVSTATUS_CHANGE));
        if (null != ((UsrAccountActivity) getOwnerActivity()).getService()) {
            ((UsrAccountActivity) getOwnerActivity()).getService().setConvStatusAction(
                    SKYPECONSTANT.SKYPEACTION.CONTACT_CONVSTATUS_CHANGE);
        }

        mIsRegisterConvReceiver = true;
    }

    public void unregisterConversationReceiver() {
        if (mIsRegisterConvReceiver) {
            getOwnerActivity().unregisterReceiver(mConvStatusChangeReceiver);
            mIsRegisterConvReceiver = false;
        }
    }

    private BroadcastReceiver mVoiceMailStatusChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent)
                return;
            switch (intent
                    .getIntExtra(SKYPECONSTANT.SKYPEACTION.CONTACT_VOICEMAILSTATUS_CHANGE, -1)) {
                case SKYPECONSTANT.VOICEMAILSTATUS.INITIALIZE:
                    if (getViewListenner().isCalling()) {
                        getViewListenner().callStop();
                        getViewListenner().initVM();
                    }
                    break;
                case SKYPECONSTANT.VOICEMAILSTATUS.RECORDING:
                    getViewListenner().removeVMFailCallback();
                    getViewListenner().startRecord();
                    break;
                case SKYPECONSTANT.VOICEMAILSTATUS.RECORDING_PROGRESS:
                    getViewListenner().updateRecordDuration(
                            intent.getIntExtra(SKYPECONSTANT.SKYPESTRING.VOICEMAILSTATUS, -1));
                    break;
                case SKYPECONSTANT.VOICEMAILSTATUS.CANCELLED:
                    getViewListenner().vmCancelled();
                    break;
                case SKYPECONSTANT.VOICEMAILSTATUS.SENDED:
                    getViewListenner().vmSended();
                    break;
                case SKYPECONSTANT.VOICEMAILSTATUS.CANCELLED_RECORD_WHEN_INCOMING_CALL:
                    getViewListenner().vmStop();
                    break;
                case SKYPECONSTANT.VOICEMAILSTATUS.SENDFAILED:
                    getViewListenner().vmFailed();
                    getViewListenner().removeVMFailCallback();
                    if (getViewListenner().isCalling()) {
                        getViewListenner().callStop();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private BroadcastReceiver mConvStatusChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent)
                return;
            switch (intent.getIntExtra(SKYPECONSTANT.SKYPEACTION.CONTACT_CONVSTATUS_CHANGE, -1)) {
                case SKYPECONSTANT.CONVERSATIONSTATUS.IM_LIVE:
                    getViewListenner().callStart();
                    break;
                case SKYPECONSTANT.CONVERSATIONSTATUS.RECENTLY_LIVE:
                    getViewListenner().callStop();
                    break;
                default:
                    break;
            }

        }
    };

}
