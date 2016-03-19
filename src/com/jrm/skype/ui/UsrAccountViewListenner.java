
package com.jrm.skype.ui;

import java.util.ArrayList;
import com.jrm.skype.api.SktContact.SktAuthItem;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.ui.OptionsActivity;
import com.jrm.skype.ui.UsrAccountActivity;
import com.jrm.skype.util.SktApiActor;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;

/**
 * @author andy.liu 
 * the ViewListenner of UsrAccountActivity
 */
public class UsrAccountViewListenner implements OnClickListener {
    private UsrAccountViewHolder mVLUsrAccountViewHolder;

    private UsrAccountActivity mVLUsrAccountActivity;
    
    private AnimationDrawable mVLConnectingAnimation;
    
    private String mVLFullName;

    // to show different fragments
    private int mVLFocus;

    private FragmentTransaction mVLFragmentT;
    
    private ArrayList<SktAuthItem> mVLAuthList;
    
    private Handler mVLHandler;
    
    private boolean mVLLoadingOption;

    public UsrAccountViewListenner(UsrAccountViewHolder mVLUsrAccountViewHolder,
            UsrAccountActivity mVLUsrAccountActivity) {
        super();
        this.mVLUsrAccountViewHolder = mVLUsrAccountViewHolder;
        this.mVLUsrAccountActivity = mVLUsrAccountActivity;
        mVLLoadingOption = false;

        mVLFocus = SKYPECONSTANT.USRACCOUNTFOCUS.CONTACTLIST;
    }

    public void setViewListenner() {
        mVLUsrAccountViewHolder.mVHUsrstatusIv.setOnClickListener(this);
        mVLUsrAccountViewHolder.mVHContactIv.setOnClickListener(this);
        mVLUsrAccountViewHolder.mVHHistoryIv.setOnClickListener(this);
        mVLUsrAccountViewHolder.mVHSkypeOutIv.setOnClickListener(this);
        mVLUsrAccountViewHolder.mVHOptionIv.setOnClickListener(this);

        mVLUsrAccountViewHolder.mVHExitTv.setOnClickListener(this);
        mVLUsrAccountViewHolder.mVHAddContactTv.setOnClickListener(this);
        mVLUsrAccountViewHolder.mVHStatusTv.setOnClickListener(this);
        mVLUsrAccountViewHolder.mVHRequestTv.setOnClickListener(this);
        
        //Control the focus
        mVLUsrAccountViewHolder.mVHHistoryIv.setOnKeyListener(new OnKeyListener() {
            
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
                            && mVLFocus == SKYPECONSTANT.USRACCOUNTFOCUS.HISTORYLIST
                            && null != mVLUsrAccountViewHolder.mVHHistoryListFragment) {
                        mVLUsrAccountViewHolder.mVHHistoryListFragment.setListFocus();
                    }
                }
                return false;
            }
        });
    }

    public void initVar() {
        mVLAuthList = SktApiActor.getAuthRequestList();
        if (null == mVLAuthList || mVLAuthList.size() == 0)
            mVLUsrAccountViewHolder.mVHRequestTv.setVisibility(View.GONE);
        else
            mVLUsrAccountViewHolder.mVHRequestTv.setVisibility(View.VISIBLE);
        updateAvatar();
        
        updateStatus();

        updateFullName();
       
        updateMoodText();
        
        updateBlance();
        
        mVLUsrAccountViewHolder.mVHContactIv.requestFocus();
    }
    
    public void updateStatus(){
        switch (SktApiActor.getAccountAvailability()){
            case ONLINE:
                mVLUsrAccountViewHolder.mVHUsrstatusIv.setImageResource(R.drawable.presenceonline_32x32);
                break;
            case CONNECTING:
                mVLUsrAccountViewHolder.mVHUsrstatusIv.setImageResource(R.drawable.presenceconnecting_32x32);
                mVLConnectingAnimation = (AnimationDrawable) mVLUsrAccountViewHolder.mVHUsrstatusIv.getDrawable();
                
                if (null != mVLConnectingAnimation)
                    mVLConnectingAnimation.start();
                break;
            case DO_NOT_DISTURB:
                mVLUsrAccountViewHolder.mVHUsrstatusIv.setImageResource(R.drawable.presencedonotdisturb_32x32);
                break;
            case AWAY:
                mVLUsrAccountViewHolder.mVHUsrstatusIv.setImageResource(R.drawable.presenceaway_32x32);
                break;
            case INVISIBLE:
                mVLUsrAccountViewHolder.mVHUsrstatusIv.setImageResource(R.drawable.presenceinvisible_32x32);
                break;
            default:
                mVLUsrAccountViewHolder.mVHUsrstatusIv.setImageResource(R.drawable.presenceconnecting_32x32);
                mVLConnectingAnimation = (AnimationDrawable) mVLUsrAccountViewHolder.mVHUsrstatusIv.getDrawable();
                
                if (null != mVLConnectingAnimation)
                    mVLConnectingAnimation.start();
                break;
        }
        
    }

    public void updateAvatar() {
        byte[] bmyt = SktApiActor.getAccountAvatar();

        if (null != bmyt && bmyt.length > 0) {
            mVLUsrAccountViewHolder.mVHUsrheadIv.setImageBitmap(BitmapFactory.decodeByteArray(bmyt,
                    0, bmyt.length));
        }else{
            mVLUsrAccountViewHolder.mVHUsrheadIv.setImageResource(R.drawable.skype_img_login_user_head_default);
        }
    }
    
    public void updateFullName() {
        mVLFullName = SktApiActor.getFullName();
        if (null != mVLFullName && mVLFullName.length() > 0) {
            mVLUsrAccountViewHolder.mVHUsrNameTv.setText(mVLFullName);
        } else {
            mVLUsrAccountViewHolder.mVHUsrNameTv.setText(SktApiActor.getAccountName());
        }
    }
    
    public void updateMoodText(){
        mVLUsrAccountViewHolder.mVHUsrSigTv.setText(SktApiActor.getMoodText());
    }
    
    public void updateBlance()
    {
        float balance = SktApiActor.getBalance();
        
        if (balance > 0.001f) {
            mVLUsrAccountViewHolder.mVHCreditTv.setText(SktApiActor.getCurrency() + "  " + balance);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_usrAc_contactlist:
                contactListClick();
                break;
            case R.id.iv_usrAc_historylist:
                historyListClick();
                break;
            case R.id.iv_usrAc_skypeout:
                skypeOutClick();
                break;
            case R.id.iv_usrAc_options:
                optionsClick();
                break;
            case R.id.tv_usrAc_addcontact: 
                mVLUsrAccountActivity.showDialog(SKYPECONSTANT.USRACCOUNTDIALOG.ADD_CONTACT_TYPE);
                break;
            case R.id.iv_usrAc_usrstatus:
            case R.id.tv_usrAc_status:
                mVLUsrAccountActivity.showDialog(SKYPECONSTANT.USRACCOUNTDIALOG.USR_STATUS);
                break;
            case R.id.tv_usrAc_request:
                mVLUsrAccountActivity.showDialog(SKYPECONSTANT.USRACCOUNTDIALOG.INVITATION);
                break;
            case R.id.tv_usrAc_exit:
                mVLUsrAccountActivity.finish();
            default:
                break;
        } // switch
    }

    public void contactListClick() {
        if (mVLFocus != SKYPECONSTANT.USRACCOUNTFOCUS.CONTACTLIST) {
            mVLFragmentT = mVLUsrAccountActivity.getFragmentManager().beginTransaction();
             
            mVLFragmentT.replace(R.id.rel_usrAc_right_fragment,
                    mVLUsrAccountViewHolder.mVHContactListFragment);
            // mVLFragmentT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            // mVLFragmentT.addToBackStack(null);

            mVLFragmentT.commit();

            mVLFocus = SKYPECONSTANT.USRACCOUNTFOCUS.CONTACTLIST;
        }
     }

    public void historyListClick() {
        if (mVLFocus != SKYPECONSTANT.USRACCOUNTFOCUS.HISTORYLIST) {
            mVLFragmentT = mVLUsrAccountActivity.getFragmentManager().beginTransaction();
            mVLFragmentT.replace(R.id.rel_usrAc_right_fragment,
                    mVLUsrAccountViewHolder.mVHHistoryListFragment);
            // mVLFragmentT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
             mVLFragmentT.addToBackStack(null);
            mVLFragmentT.commit();

            mVLFocus = SKYPECONSTANT.USRACCOUNTFOCUS.HISTORYLIST;
        }
    }

    public void skypeOutClick() {
        if (mVLFocus != SKYPECONSTANT.USRACCOUNTFOCUS.SKYPEOUT) {
            mVLFragmentT = mVLUsrAccountActivity.getFragmentManager().beginTransaction();
            mVLFragmentT.replace(R.id.rel_usrAc_right_fragment,
                    mVLUsrAccountViewHolder.mVHSkypeOutFragment);
            // mVLFragmentT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            // mVLFragmentT.addToBackStack(null);
            mVLFragmentT.commit();

            mVLFocus = SKYPECONSTANT.USRACCOUNTFOCUS.SKYPEOUT;
        }
    }

    public void optionsClick() {
        if (mVLFocus != SKYPECONSTANT.USRACCOUNTFOCUS.OPTIONS) {
            mVLFragmentT = mVLUsrAccountActivity.getFragmentManager().beginTransaction();
            mVLFragmentT.replace(R.id.rel_usrAc_right_fragment,
                    mVLUsrAccountViewHolder.mVHOptionsFragment);
        
            // mVLFragmentT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            // mVLFragmentT.addToBackStack(null);
            mVLFragmentT.commit();

            mVLFocus = SKYPECONSTANT.USRACCOUNTFOCUS.OPTIONS;
        } else {
            mVLUsrAccountViewHolder.mVHProgressBar.setVisibility(View.VISIBLE);
            if(null == mVLHandler){
                mVLHandler = new Handler();
            }
            mVLLoadingOption = true;
            mVLHandler.postDelayed(loadOption,1000);
        }
    }

    Runnable loadOption = new Runnable() {
        
        @Override
        public void run() {
            mVLLoadingOption = false;
            Intent intent = new Intent();
            intent.setClass(mVLUsrAccountActivity, OptionsActivity.class);
            mVLUsrAccountActivity.startActivity(intent); 
        }
    };
    
    public boolean getLoadOptionBool(){
        return mVLLoadingOption;
    }
    
    public void cancelLoadOption(){
        if(null == mVLHandler){
            mVLHandler = new Handler();
        }
        
        mVLHandler.removeCallbacks(loadOption);
        mVLUsrAccountViewHolder.mVHProgressBar.setVisibility(View.GONE);
    }
    
    public int getFocus() {
        return mVLFocus;
    }

    public void setUsrStateIm(int resId) {
        mVLUsrAccountViewHolder. mVHUsrstatusIv.setImageResource(resId);
    }
    
}
