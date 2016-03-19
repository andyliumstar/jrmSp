package com.jrm.skype.dialog;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.ui.UsrAccountActivity;
import com.jrm.skype.util.SktApiActor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
/**
 * @author andy.liu
 * A dialog that for the skype call out.
 */
public class SkypeOutCallOutDialog extends MainDialog {
    private SkypeOutCallOutViewHolder mSkypeOutCallOutViewHolder;
    
    private SkypeOutCallOutViewListenner mSkypeOutCallOutViewListenner;

    public SkypeOutCallOutDialog(Context context, int theme) {
        super(context, theme);
        mSkypeOutCallOutViewHolder = new SkypeOutCallOutViewHolder(this);
        mSkypeOutCallOutViewListenner = new SkypeOutCallOutViewListenner(mSkypeOutCallOutViewHolder, this);
    }

    @Override
    public void findView() {
        mSkypeOutCallOutViewHolder.findView();
    }

    @Override
    public void setViewListenner() {
        mSkypeOutCallOutViewListenner.setViewListenner();
    }

    @Override
    public void initVar(Bundle args) {
        mSkypeOutCallOutViewListenner
                .initVar(args.getString(SKYPECONSTANT.SKYPESTRING.PHONENUMBER));
        
        getOwnerActivity().registerReceiver(mConvStatusChangeReceiver,
                new IntentFilter(SKYPECONSTANT.SKYPEACTION.SKYPEOUT_CONVSTATUS_CHANGE));
        if (null != ((UsrAccountActivity) getOwnerActivity()).getService()) {
            ((UsrAccountActivity) getOwnerActivity()).getService().setConvStatusAction(
                    SKYPECONSTANT.SKYPEACTION.SKYPEOUT_CONVSTATUS_CHANGE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SktApiActor.leaveConversation();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    
    @Override
    public void dismiss() {
        getOwnerActivity().unregisterReceiver(mConvStatusChangeReceiver);
        super.dismiss();
    }

    public SkypeOutCallOutViewListenner getViewListenner(){
        return mSkypeOutCallOutViewListenner;
    }

    private BroadcastReceiver mConvStatusChangeReceiver = new BroadcastReceiver (){

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent)
                return;
            switch (intent.getIntExtra(SKYPECONSTANT.SKYPEACTION.SKYPEOUT_CONVSTATUS_CHANGE, -1)) {
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
