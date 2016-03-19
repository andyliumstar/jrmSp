
package com.jrm.skype.dialog;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.util.SktApiActor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

/**
 * @author andy.liu 
 * a Dialog to handle add a skype contact
 */
public class AddSkypeDialog extends MainDialog {
    private AddSkypeViewHolder mAddSkypeViewHolder;

    private AddSkypeViewListenner mAddSkypeViewListenner;

    public AddSkypeDialog(Context context, int theme) {
        super(context, theme);

        mAddSkypeViewHolder = new AddSkypeViewHolder(this);
        mAddSkypeViewListenner = new AddSkypeViewListenner(mAddSkypeViewHolder, this);
    }

    @Override
    public void findView() {
        mAddSkypeViewHolder.findView();
    }

    @Override
    public void setViewListenner() {
        mAddSkypeViewListenner.setViewListenner();
    }

    @Override
    public void initVar(Bundle args) {
        mAddSkypeViewListenner.initVar();
        registerBoradcastReceiver();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mAddSkypeViewHolder.mVHSearchingRel.getVisibility() == View.VISIBLE) {// search
                                                                                     // is
                                                                                     // running
                mAddSkypeViewListenner.cancelBtnClick();
            } else {
                this.dismiss(); 
                getOwnerActivity().showDialog(SKYPECONSTANT.USRACCOUNTDIALOG.ADD_CONTACT_TYPE);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    
    public AddSkypeViewListenner getViewListenner(){
        return mAddSkypeViewListenner;
    }
    
    
    @Override
    public void dismiss() {
        this.getOwnerActivity().unregisterReceiver(mSearchStatusChangeReceiver);
        super.dismiss();
    }

    private BroadcastReceiver mSearchStatusChangeReceiver = new BroadcastReceiver (){

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent)
                return;
            switch(intent.getIntExtra(SKYPECONSTANT.SKYPEACTION.SEARCHSTATUS_CHANGE,-1)){
                case SKYPECONSTANT.SEARCHSTATUS.PENDING:
                    getViewListenner().searchRunning();
                    break;
                case SKYPECONSTANT.SEARCHSTATUS.FINISHED:
                    if (null == SktApiActor.getCurrentContactSearch())
                        return;
                    if (null == SktApiActor.getSearchList() && SktApiActor.getSearchList().size() == 0)
                        getViewListenner().noMatchFound();
                    else
                        getViewListenner().searchFinished();
                    break;
                case SKYPECONSTANT.SEARCHSTATUS.FAILED:
                    getViewListenner().searchFailed();
                    break;
            }
            
        }
    };
    
    public void  registerBoradcastReceiver(){  
        IntentFilter myIntentFilter = new IntentFilter();  
        myIntentFilter.addAction(SKYPECONSTANT.SKYPEACTION.SEARCHSTATUS_CHANGE);  
        this.getOwnerActivity().registerReceiver(mSearchStatusChangeReceiver, myIntentFilter);  
    }  

}
