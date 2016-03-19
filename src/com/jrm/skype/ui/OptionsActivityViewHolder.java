
package com.jrm.skype.ui;

import com.jrm.skype.fragment.AboutSkypeFragment;
import com.jrm.skype.fragment.AudioSetFragment;
import com.jrm.skype.fragment.BlockedContactsFragment;
import com.jrm.skype.fragment.CallSetFragment;
import com.jrm.skype.fragment.ChangePasswordFragment;
import com.jrm.skype.fragment.GeneralSetFragment;
import com.jrm.skype.fragment.PrivacySetFragment;
import com.jrm.skype.fragment.UserprofileFragment;
import com.jrm.skype.ui.OptionsActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author andy.liu 
 * the ViewHolder of OptionsActivity
 */
public class OptionsActivityViewHolder {
    public OptionsActivity mVHOptionsActivity;

    /* the options' functions,8 parts, use textview as a button */
    public TextView mVHUsrprofileTv;

    public TextView mVHChangePwTv;

    public TextView mVHGeneralSetTv;

    public TextView mVHAudioSetTv;

    public TextView mVHPrivacyTv;

    public TextView mVHBlockedCtTv;

    public TextView mVHCallSetTv;

    public TextView mVHAboutSkypeTv;

    /* the options' functions' views, to match the 8 textview buttons */
    public UserprofileFragment mVHUserprofileFragment;

    public ChangePasswordFragment mVHChangePasswordFragment;

    public GeneralSetFragment mVHGeneralSetFragment;

    public AudioSetFragment mVHAudioSetFragment;

    public PrivacySetFragment mVHPrivacySetFragment;

    public BlockedContactsFragment mVHBlockedContactsFragment;

    public CallSetFragment mVHCallSetFragment;

    public AboutSkypeFragment mVHAboutSkypeFragment;

    /* bottom view */
    public ImageView mVHVolUpIv;

    public ImageView mVHVolDownIv;

    public TextView mVHVolTv;

    public TextView mVHBackTv;

    public TextView mVHSaveTv;

    public OptionsActivityViewHolder(OptionsActivity mVHOptionsActivity) {
        this.mVHOptionsActivity = mVHOptionsActivity;
        mVHUserprofileFragment = new UserprofileFragment();
        mVHChangePasswordFragment = new ChangePasswordFragment();
        mVHGeneralSetFragment = new GeneralSetFragment();
        mVHAudioSetFragment = new AudioSetFragment();
        mVHPrivacySetFragment = new PrivacySetFragment();
        mVHBlockedContactsFragment = new BlockedContactsFragment();
        mVHCallSetFragment = new CallSetFragment();
        mVHAboutSkypeFragment = new AboutSkypeFragment();
    }

    public void findView() {
        mVHUsrprofileTv = (TextView) mVHOptionsActivity.findViewById(R.id.tv_options_usrprofile);
        mVHChangePwTv = (TextView) mVHOptionsActivity.findViewById(R.id.tv_options_changepassword);
        mVHGeneralSetTv = (TextView) mVHOptionsActivity.findViewById(R.id.tv_options_generalSet);
        mVHAudioSetTv = (TextView) mVHOptionsActivity.findViewById(R.id.tv_options_audioSet);
        mVHPrivacyTv = (TextView) mVHOptionsActivity.findViewById(R.id.tv_options_privacySet);
        mVHBlockedCtTv = (TextView) mVHOptionsActivity.findViewById(R.id.tv_options_blockedcontacts);
        mVHCallSetTv = (TextView) mVHOptionsActivity.findViewById(R.id.tv_options_callSet);
        mVHAboutSkypeTv = (TextView) mVHOptionsActivity.findViewById(R.id.tv_options_aboutSkype);

        mVHVolUpIv = (ImageView) mVHOptionsActivity.findViewById(R.id.iv_item_pop_vol_up);
        mVHVolDownIv = (ImageView) mVHOptionsActivity.findViewById(R.id.iv_item_pop_vol_down);
        mVHVolTv = (TextView) mVHOptionsActivity.findViewById(R.id.tv_item_pop_vol);
        mVHBackTv = (TextView) mVHOptionsActivity.findViewById(R.id.tv_item_pop_back);
        mVHSaveTv = (TextView) mVHOptionsActivity.findViewById(R.id.tv_item_pop_save);
    }

}
