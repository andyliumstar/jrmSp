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
 * @author andy.liu
 * A dialog that contains all the history list's function:
 * View Profile , SendMessage, Send VocieMail , Listen To VoiceMail , Add to Contacts, Delete
 */
public class HistoryDialog extends MainDialog {
    private HistoryViewHolder mHistoryItemViewHolder;
    
    private HistoryViewListenner mHistoryItemViewListenner;
    
    private boolean mIsRegisterVMReceiver;
    
    private boolean mIsRegisterConvReceiver;

    public HistoryDialog(Context context, int theme) {
        super(context, theme);
        mHistoryItemViewHolder = new HistoryViewHolder(this);
        mHistoryItemViewListenner = new HistoryViewListenner(mHistoryItemViewHolder, this);
    }

    @Override
    public void findView() {
        mHistoryItemViewHolder.findView();
    }

    @Override
    public void setViewListenner() {
        mHistoryItemViewListenner.setViewListenner();
    }

    @Override
    public void initVar(Bundle args) {
        mIsRegisterVMReceiver = false;
        mIsRegisterConvReceiver = false;
        mHistoryItemViewListenner.initVar(args);
        // different with the ConversationReceiver(will change), the VoiceMailReceiver can register when the dialog shows
        registerVoiceMailReceiver();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_BACK ){
            mHistoryItemViewListenner.removeView();
            return true;
        }
        else if ( mHistoryItemViewHolder.mVHPopMidProfileRel.getVisibility() == View.VISIBLE )
         // disable the key when the contact profile is show
            return true;
        else
            return false;
    }
    
    @Override
    public void dismiss() {
        unregisterVoiceMailReceiver();
        super.dismiss();
    }

    public HistoryViewListenner getViewListenner(){
        return mHistoryItemViewListenner;
    }
    
    private void registerVoiceMailReceiver(){
        getOwnerActivity().registerReceiver(mVoiceMailStatusChangeReceiver,
                new IntentFilter(SKYPECONSTANT.SKYPEACTION.HISTORY_VOICEMAILSTATUS_CHANGE));
        if (null != ((UsrAccountActivity) getOwnerActivity()).getService()){
            ((UsrAccountActivity) getOwnerActivity()).getService().setVoiceMailStatusChangeAction(
                    SKYPECONSTANT.SKYPEACTION.HISTORY_VOICEMAILSTATUS_CHANGE);
        }
        mIsRegisterVMReceiver = true;
    }
    
    private void unregisterVoiceMailReceiver(){
        if (mIsRegisterVMReceiver){
            getOwnerActivity().unregisterReceiver(mVoiceMailStatusChangeReceiver);
            mIsRegisterVMReceiver = false;
        }
    }

    
    public void registerConversationReceiver() {
        getOwnerActivity().registerReceiver(mConvStatusChangeReceiver,
                new IntentFilter(SKYPECONSTANT.SKYPEACTION.HISTORY_CONVSTATUS_CHANGE));
        if (null != ((UsrAccountActivity) getOwnerActivity()).getService()) {
            ((UsrAccountActivity) getOwnerActivity()).getService().setConvStatusAction(
                    SKYPECONSTANT.SKYPEACTION.HISTORY_CONVSTATUS_CHANGE);
        }
        mIsRegisterConvReceiver = true;
    }
    
    public void unregisterConversationReceiver(){
        if (mIsRegisterConvReceiver){
            getOwnerActivity().unregisterReceiver(mConvStatusChangeReceiver);
            mIsRegisterConvReceiver = false;
        }
        
    }

    
    private BroadcastReceiver mVoiceMailStatusChangeReceiver = new BroadcastReceiver (){

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent)
                return;
            switch (intent.getIntExtra(SKYPECONSTANT.SKYPEACTION.HISTORY_VOICEMAILSTATUS_CHANGE, -1)) {
                case SKYPECONSTANT.VOICEMAILSTATUS.INITIALIZE:
                    if (getViewListenner().isCalling()) {
                        getViewListenner().callStop();
                        getViewListenner().initVM();
                    }
                    break;
                case SKYPECONSTANT.VOICEMAILSTATUS.RECORDING:
                    getViewListenner().startRecord();
                    getViewListenner().removeVMFailCallback();
                    break;
                case SKYPECONSTANT.VOICEMAILSTATUS.RECORDING_PROGRESS:
                    getViewListenner().updateRecordDuration(intent.getIntExtra(
                            SKYPECONSTANT.SKYPESTRING.VOICEMAILSTATUS, -1));
                    break;
                case SKYPECONSTANT.VOICEMAILSTATUS.CANCELLED:
                    getViewListenner().vmCancelled();
                    break;
                case SKYPECONSTANT.VOICEMAILSTATUS.SENDED:
                    getViewListenner().vmSended();
                    break;
                case SKYPECONSTANT.VOICEMAILSTATUS.SENDFAILED:
                    getViewListenner().removeVMFailCallback();
                    getViewListenner().vmFailed();
                    if (getViewListenner().isCalling()) {
                        getViewListenner().callStop();
                    }
                    break;
                case SKYPECONSTANT.VOICEMAILSTATUS.PLAYING:
                    getViewListenner().beginPaly(intent.getIntExtra(
                            SKYPECONSTANT.SKYPESTRING.VOICEMAILSTATUS, -1));
                    break;
                case SKYPECONSTANT.VOICEMAILSTATUS.PLAYBACK_PROGRESS:
                    getViewListenner().playProgress(intent.getIntExtra(
                            SKYPECONSTANT.SKYPESTRING.VOICEMAILSTATUS, -1));
                    break;
                case SKYPECONSTANT.VOICEMAILSTATUS.PLAYED:
                    getViewListenner().played();
                    break;
                case SKYPECONSTANT.VOICEMAILSTATUS.RECFAILED:
                    getViewListenner().recFailed();
                    break;
                case SKYPECONSTANT.VOICEMAILSTATUS.CANCELLED_RECORD_WHEN_INCOMING_CALL:
                    getViewListenner().vmStop();
                    break;
                case SKYPECONSTANT.VOICEMAILSTATUS.CANCELLED_PLAYBACK_WHEN_INCOMING_CALL:
                    getViewListenner().listenVMStop();
                    break;
                default:
                    break;
            }
            
        }
    };
    
    private BroadcastReceiver mConvStatusChangeReceiver = new BroadcastReceiver (){

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent)
                return;
            switch (intent.getIntExtra(SKYPECONSTANT.SKYPEACTION.HISTORY_CONVSTATUS_CHANGE, -1)) {
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
