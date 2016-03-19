
package com.jrm.skype.fragment;

import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.jrm.skype.fragment.ChangePasswordFragment;
import com.jrm.skype.ui.R;

/**
 * @author andy.liu 
 * the ViewHolder of ChangePasswordFragment
 */
public class ChangePasswordViewHolder {
    public ChangePasswordFragment mVHChangePasswordFragment;

    public EditText mVHCurrentPasswordEt;

    public EditText mVHNewPasswordEt;

    public EditText mVHRePasswordEt;
    
    public ImageView mVHNewPasswordWarnIv;

    public ImageView mVHRePasswordWarnIm;
    
    public TextView mVHNewPasswordWarnTv;

    public TextView mVHRePasswordWarnTv;

    public Button mVHApplyBtn;

    public ChangePasswordViewHolder(ChangePasswordFragment mVHChangePasswordFragment) {
        super();
        this.mVHChangePasswordFragment = mVHChangePasswordFragment;
    }

    public void findView() {
        mVHCurrentPasswordEt = (EditText) mVHChangePasswordFragment.getLayout().findViewById(
                R.id.et_changepassword_fragment_entercurpw);
        mVHCurrentPasswordEt.setTypeface(Typeface.DEFAULT);
        mVHNewPasswordEt = (EditText) mVHChangePasswordFragment.getLayout().findViewById(
                R.id.et_changepassword_fragment_enternewpw);
        mVHNewPasswordEt.setTypeface(Typeface.DEFAULT);
        mVHNewPasswordWarnIv = (ImageView)mVHChangePasswordFragment.getLayout().findViewById(
                R.id.iv_changepassword_fragment_password_wrong);
        mVHNewPasswordWarnTv = (TextView)mVHChangePasswordFragment.getLayout().findViewById(
                R.id.tv_changepassword_fragment_password_wrong);
        mVHRePasswordEt = (EditText) mVHChangePasswordFragment.getLayout().findViewById(
                R.id.et_changepassword_fragment_re_enternewpw);
        mVHRePasswordEt.setTypeface(Typeface.DEFAULT);
        mVHRePasswordWarnIm = (ImageView)mVHChangePasswordFragment.getLayout().findViewById(
                R.id.iv_changepassword_fragment_repassword_wrong);
        mVHRePasswordWarnTv = (TextView)mVHChangePasswordFragment.getLayout().findViewById(
                R.id.tv_changepassword_fragment_repassword_wrong);
        mVHApplyBtn = (Button) mVHChangePasswordFragment.getLayout().findViewById(
                R.id.btn_changepassword_fragment_apply);
    }
}
