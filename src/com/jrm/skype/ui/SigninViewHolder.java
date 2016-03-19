
package com.jrm.skype.ui;

import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.jrm.skype.ui.SigninActivity;

/**
 * @author andy.liu 
 * the ViewHolder of SigninActivity
 */
public class SigninViewHolder {
    public SigninActivity mVHSigninActivity;

    public ImageView mVHUsrHeadIm;

    public EditText mVHSkypeNameEt;

    public EditText mVHPasswordEt;

    // using two imageviews for two checkboxes
    public ImageView mVHSignSkStartCK;

    public ImageView mVHStartTVStartCK;

    public Button mVHSigninBtn;

    public Button mVHCreateAccountBtn;

    public TextView mVHExitTv;

    public SigninViewHolder(SigninActivity mVHSigninActivity) {
        this.mVHSigninActivity = mVHSigninActivity;
    }

    public void findView() {
        mVHUsrHeadIm = (ImageView) mVHSigninActivity.findViewById(R.id.iv_signin_head);
        mVHSkypeNameEt = (EditText) mVHSigninActivity.findViewById(R.id.et_signin_skypename);
        mVHPasswordEt = (EditText) mVHSigninActivity.findViewById(R.id.et_signin_password);
        mVHPasswordEt.setTypeface(Typeface.DEFAULT);
        mVHSignSkStartCK = (ImageView) mVHSigninActivity.findViewById(R.id.iv_signin_signwhenskstart);
        mVHStartTVStartCK = (ImageView) mVHSigninActivity
                .findViewById(R.id.iv_signin_startwhentvstart);
        mVHSigninBtn = (Button) mVHSigninActivity.findViewById(R.id.btn_signin_signin);
        mVHCreateAccountBtn = (Button) mVHSigninActivity.findViewById(R.id.btn_signin_create);
        mVHExitTv = (TextView) mVHSigninActivity.findViewById(R.id.tv_signin_exit);
    }

}
