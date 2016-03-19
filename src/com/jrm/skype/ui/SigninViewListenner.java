
package com.jrm.skype.ui;

import com.jrm.skype.api.SktUtils;
import com.jrm.skype.ui.SigninActivity;
import com.jrm.skype.ui.SigningActivity;
import com.jrm.skype.util.SktApiActor;
import com.jrm.skype.util.SkypePrefrences;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Toast;

/**
 * @author andy.liu 
 * the ViewListenner of SigninActivity
 */
public class SigninViewListenner implements OnClickListener, OnFocusChangeListener {
    private SigninActivity mVLSigninActivity;

    private SigninViewHolder mVLSigninViewHolder;

    private Intent mVLIntent;

    private boolean mVHSignSkStart;

    private boolean mVHStartTVStart;
    
    private String mVLSkypeNameStr;
    
    private String mVLPasswordStr;

    public SigninViewListenner(SigninActivity mVLSigninActivity, SigninViewHolder mVLSigninViewHolder) {
        super();
        this.mVLSigninActivity = mVLSigninActivity;
        this.mVLSigninViewHolder = mVLSigninViewHolder;

        mVLIntent = new Intent();
    }

    public void initVar() {
        if (SkypePrefrences.isSignWhenSKTStart(mVLSigninActivity))
            mVHSignSkStart = true;
        else
            mVHSignSkStart = false;

        if (SkypePrefrences.isStartSKTWhenTVStart(mVLSigninActivity))
            mVHStartTVStart = true;
        else
            mVHStartTVStart = false;

        updateSignSkStartCK(false, mVHSignSkStart);
        updateStartTVStartCK(false, mVHStartTVStart);
        
        mVLSigninViewHolder.mVHSkypeNameEt.setText(SkypePrefrences.getSignSkypeName(mVLSigninActivity));
    }
    
    public void setViewListener() {
        mVLSigninViewHolder.mVHSignSkStartCK.setOnClickListener(this);
        mVLSigninViewHolder.mVHStartTVStartCK.setOnClickListener(this);
        mVLSigninViewHolder.mVHSigninBtn.setOnClickListener(this);
        mVLSigninViewHolder.mVHCreateAccountBtn.setOnClickListener(this);
        mVLSigninViewHolder.mVHExitTv.setOnClickListener(this);

        mVLSigninViewHolder.mVHSignSkStartCK.setOnFocusChangeListener(this);
        mVLSigninViewHolder.mVHStartTVStartCK.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.iv_signin_signwhenskstart:
                updateSignSkStartCK(hasFocus, mVHSignSkStart);
                break;
            case R.id.iv_signin_startwhentvstart:
                updateStartTVStartCK(hasFocus, mVHStartTVStart);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_signin_signwhenskstart:
                mVHSignSkStart = !mVHSignSkStart;
                updateSignSkStartCK(mVLSigninViewHolder.mVHSignSkStartCK.hasFocus(), mVHSignSkStart);
                SkypePrefrences.setSignWhenSKTStart(mVLSigninActivity, mVHSignSkStart);
                break;
            case R.id.iv_signin_startwhentvstart:
                mVHStartTVStart = !mVHStartTVStart;
                updateStartTVStartCK(mVLSigninViewHolder.mVHStartTVStartCK.hasFocus(), mVHStartTVStart);
                SkypePrefrences.setStartSKTWhenTVStart(mVLSigninActivity, mVHStartTVStart);
                break;
            case R.id.btn_signin_signin:
                clickSignin();
                break;
            case R.id.btn_signin_create:
                clickCreate();
                break;
            case R.id.tv_signin_exit:
                mVLSigninActivity.exitApk();
            default:
                break;
        }
    }

    public void updateSignSkStartCK(boolean hasFocus, boolean signSkStart) {
        if (hasFocus)
            if (signSkStart)
                mVLSigninViewHolder.mVHSignSkStartCK
                        .setImageResource(R.drawable.skype_img_login_checkbox_sel);
            else
                mVLSigninViewHolder.mVHSignSkStartCK
                        .setImageResource(R.drawable.skype_img_login_checkbox_sel_no);
        else if (signSkStart)
            mVLSigninViewHolder.mVHSignSkStartCK
                    .setImageResource(R.drawable.skype_img_login_checkbox_on);
        else
            mVLSigninViewHolder.mVHSignSkStartCK
                    .setImageResource(R.drawable.skype_img_login_checkbox);
    }

    public void updateStartTVStartCK(boolean hasFocus, boolean StartTVStart) {
        if (hasFocus)
            if (StartTVStart)
                mVLSigninViewHolder.mVHStartTVStartCK
                        .setImageResource(R.drawable.skype_img_login_checkbox_sel);
            else
                mVLSigninViewHolder.mVHStartTVStartCK
                        .setImageResource(R.drawable.skype_img_login_checkbox_sel_no);
        else if (StartTVStart)
            mVLSigninViewHolder.mVHStartTVStartCK
                    .setImageResource(R.drawable.skype_img_login_checkbox_on);
        else
            mVLSigninViewHolder.mVHStartTVStartCK
                    .setImageResource(R.drawable.skype_img_login_checkbox);
    }

    public void clickSignin() {
        mVLSkypeNameStr = mVLSigninViewHolder.mVHSkypeNameEt.getText().toString();
        mVLPasswordStr = mVLSigninViewHolder.mVHPasswordEt.getText().toString();

        SkypePrefrences.recordSignInfo(mVLSigninActivity, mVLSkypeNameStr, mVLPasswordStr);
        
        if (!SktUtils.isNetworkAvailable(mVLSigninActivity)) {
        	Toast.makeText(mVLSigninActivity, R.string.internet_connect_not_available, Toast.LENGTH_SHORT).show();
        	return;
        }
        
        if( mVLSkypeNameStr.length()<6 ||mVLSkypeNameStr.length()>32 ){
            Toast.makeText(mVLSigninActivity, R.string.skypename_length, Toast.LENGTH_SHORT).show();
            return;
        }
        
        boolean ret = SktApiActor.login(mVLSkypeNameStr, mVLPasswordStr, (mVHSignSkStart || mVHStartTVStart));       

        if (ret) {
            // if success
            mVLIntent.setClass(mVLSigninActivity, SigningActivity.class);
            mVLSigninActivity.startActivity(mVLIntent);
            mVLSigninActivity.finish();
        } else {
            Toast.makeText(mVLSigninActivity, R.string.invalid_input, Toast.LENGTH_SHORT).show();
        }
    }

    public void clickCreate() {
        mVLIntent.setClass(mVLSigninActivity, CreateNewAccountActivity.class);
        mVLSigninActivity.startActivity(mVLIntent);
        mVLSigninActivity.finish();
    }

}
