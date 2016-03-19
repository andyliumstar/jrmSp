
package com.jrm.skype.ui;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.ui.OptionsActivity;
import android.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;

/**
 * @author andy.liu 
 * the ViewListenner of OptionsActivity
 */
public class OptionsActivityViewListenner implements OnClickListener {
    private OptionsActivityViewHolder mVLOptionsActivityViewHolder;

    private OptionsActivity mVLOptionsActivity;

    private int mVLFragmentFocus;

    private FragmentTransaction mVLFragmentT;

    public OptionsActivityViewListenner(OptionsActivityViewHolder mVLOptionsActivityViewHolder,
            OptionsActivity mVLOptionsActivity) {
        this.mVLOptionsActivityViewHolder = mVLOptionsActivityViewHolder;
        this.mVLOptionsActivity = mVLOptionsActivity;
        mVLFragmentFocus = SKYPECONSTANT.OPTIONSACTIVITYFOCUS.USERPROFILE;
    }

    public void setViewListenner() {
        mVLOptionsActivityViewHolder.mVHAboutSkypeTv.setOnClickListener(this);
        mVLOptionsActivityViewHolder.mVHAudioSetTv.setOnClickListener(this);
        mVLOptionsActivityViewHolder.mVHBlockedCtTv.setOnClickListener(this);
        mVLOptionsActivityViewHolder.mVHCallSetTv.setOnClickListener(this);
        mVLOptionsActivityViewHolder.mVHChangePwTv.setOnClickListener(this);
        mVLOptionsActivityViewHolder.mVHGeneralSetTv.setOnClickListener(this);
        mVLOptionsActivityViewHolder.mVHPrivacyTv.setOnClickListener(this);
        mVLOptionsActivityViewHolder.mVHUsrprofileTv.setOnClickListener(this);

        mVLOptionsActivityViewHolder.mVHBackTv.setOnClickListener(this);
        mVLOptionsActivityViewHolder.mVHSaveTv.setOnClickListener(this);
        
        mVLOptionsActivityViewHolder.mVHUsrprofileTv.setOnKeyListener(new OnKeyListener() {
            
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                    if(null != mVLOptionsActivityViewHolder.mVHUserprofileFragment)
                        mVLOptionsActivityViewHolder.mVHUserprofileFragment.setFocus();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_options_usrprofile:
                usrprofileClick();
                break;
            case R.id.tv_options_changepassword:
                changepasswordClick();
                break;
            case R.id.tv_options_generalSet:
                generalSetClick();
                break;
            case R.id.tv_options_audioSet:
                audioSetClick();
                break;
            case R.id.tv_options_privacySet:
                privacySetClick();
                break;
            case R.id.tv_options_blockedcontacts:
                blockedcontactsClick();
                break;
            case R.id.tv_options_callSet:
                callSetClick();
                break;
            case R.id.tv_options_aboutSkype:
                aboutSkypeClick();
                break;
            case R.id.tv_item_pop_save:
                if (null != mVLOptionsActivityViewHolder.mVHCallSetFragment)
                    mVLOptionsActivityViewHolder.mVHCallSetFragment.getViewListenner()
                            .saveSettings();
                break;
            case R.id.tv_item_pop_back:
                mVLOptionsActivity.finish();
                break;
            default:
                break;

        }
    }

    public void usrprofileClick() {
        if (mVLFragmentFocus != SKYPECONSTANT.OPTIONSACTIVITYFOCUS.USERPROFILE) {
            mVLFragmentT = mVLOptionsActivity.getFragmentManager().beginTransaction();
            mVLFragmentT.replace(R.id.rel_options_right_fragment,
                    mVLOptionsActivityViewHolder.mVHUserprofileFragment);
            mVLFragmentT.commit();

            mVLFragmentFocus = SKYPECONSTANT.OPTIONSACTIVITYFOCUS.USERPROFILE;
        }
        mVLOptionsActivityViewHolder.mVHSaveTv.setVisibility(View.GONE);
    }

    public void changepasswordClick() {
        if (mVLFragmentFocus != SKYPECONSTANT.OPTIONSACTIVITYFOCUS.CHANGEPASSWORD) {
            mVLFragmentT = mVLOptionsActivity.getFragmentManager().beginTransaction();
            mVLFragmentT.replace(R.id.rel_options_right_fragment,
                    mVLOptionsActivityViewHolder.mVHChangePasswordFragment);
            mVLFragmentT.commit();

            mVLFragmentFocus = SKYPECONSTANT.OPTIONSACTIVITYFOCUS.CHANGEPASSWORD;
        }
        mVLOptionsActivityViewHolder.mVHSaveTv.setVisibility(View.GONE);
    }

    public void generalSetClick() {
        if (mVLFragmentFocus != SKYPECONSTANT.OPTIONSACTIVITYFOCUS.GENERALSETTINGS) {
            mVLFragmentT = mVLOptionsActivity.getFragmentManager().beginTransaction();
            mVLFragmentT.replace(R.id.rel_options_right_fragment,
                    mVLOptionsActivityViewHolder.mVHGeneralSetFragment);
            mVLFragmentT.commit();

            mVLFragmentFocus = SKYPECONSTANT.OPTIONSACTIVITYFOCUS.GENERALSETTINGS;
        }
        mVLOptionsActivityViewHolder.mVHSaveTv.setVisibility(View.GONE);
    }

    public void audioSetClick() {
        if (mVLFragmentFocus != SKYPECONSTANT.OPTIONSACTIVITYFOCUS.AUDIOSETTINGS) {
            mVLFragmentT = mVLOptionsActivity.getFragmentManager().beginTransaction();
            mVLFragmentT.replace(R.id.rel_options_right_fragment,
                    mVLOptionsActivityViewHolder.mVHAudioSetFragment);
            mVLFragmentT.commit();

            mVLFragmentFocus = SKYPECONSTANT.OPTIONSACTIVITYFOCUS.AUDIOSETTINGS;
        }
        mVLOptionsActivityViewHolder.mVHSaveTv.setVisibility(View.GONE);
    }

    public void privacySetClick() {
        if (mVLFragmentFocus != SKYPECONSTANT.OPTIONSACTIVITYFOCUS.PRIVACYSETTINGS) {
            mVLFragmentT = mVLOptionsActivity.getFragmentManager().beginTransaction();
            mVLFragmentT.replace(R.id.rel_options_right_fragment,
                    mVLOptionsActivityViewHolder.mVHPrivacySetFragment);
            mVLFragmentT.commit();

            mVLFragmentFocus = SKYPECONSTANT.OPTIONSACTIVITYFOCUS.PRIVACYSETTINGS;
        }
        mVLOptionsActivityViewHolder.mVHSaveTv.setVisibility(View.GONE);
    }

    public void blockedcontactsClick() {
        if (mVLFragmentFocus != SKYPECONSTANT.OPTIONSACTIVITYFOCUS.BLOCKEDCONTACTS) {
            mVLFragmentT = mVLOptionsActivity.getFragmentManager().beginTransaction();
            mVLFragmentT.replace(R.id.rel_options_right_fragment,
                    mVLOptionsActivityViewHolder.mVHBlockedContactsFragment);
            mVLFragmentT.commit();

            mVLFragmentFocus = SKYPECONSTANT.OPTIONSACTIVITYFOCUS.BLOCKEDCONTACTS;
        }
        mVLOptionsActivityViewHolder.mVHSaveTv.setVisibility(View.GONE);
    }

    public void callSetClick() {
        if (mVLFragmentFocus != SKYPECONSTANT.OPTIONSACTIVITYFOCUS.CALLSETTINGS) {
            mVLFragmentT = mVLOptionsActivity.getFragmentManager().beginTransaction();
            mVLFragmentT.replace(R.id.rel_options_right_fragment,
                    mVLOptionsActivityViewHolder.mVHCallSetFragment);
            mVLFragmentT.commit();

            mVLFragmentFocus = SKYPECONSTANT.OPTIONSACTIVITYFOCUS.CALLSETTINGS;
        }
        mVLOptionsActivityViewHolder.mVHSaveTv.setVisibility(View.VISIBLE);
    }

    public void aboutSkypeClick() {
        if (mVLFragmentFocus != SKYPECONSTANT.OPTIONSACTIVITYFOCUS.ABOUTSKYPE) {
            mVLFragmentT = mVLOptionsActivity.getFragmentManager().beginTransaction();
            mVLFragmentT.replace(R.id.rel_options_right_fragment,
                    mVLOptionsActivityViewHolder.mVHAboutSkypeFragment);
            mVLFragmentT.commit();

            mVLFragmentFocus = SKYPECONSTANT.OPTIONSACTIVITYFOCUS.ABOUTSKYPE;
        }

        mVLOptionsActivityViewHolder.mVHSaveTv.setVisibility(View.GONE);
    }
    
    public int getFragmentFocus()
    {
        return mVLFragmentFocus;
    }

}
