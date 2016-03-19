
package com.jrm.skype.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import com.jrm.skype.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.Toast;
import com.jrm.skype.ui.R;
import com.jrm.skype.util.SktApiActor;
import com.jrm.skype.util.SkypePrefrences;
import com.skype.api.Skype.VALIDATERESULT;

/**
 * @author andy.liu the ViewListenner of ChangePasswordFragment
 */
public class ChangePasswordViewListenner implements OnClickListener, OnFocusChangeListener,
        OnKeyListener {
    private ChangePasswordFragment mVLChangePasswordFragment;

    private ChangePasswordViewHolder mVLChangePasswordViewHolder;

    private String mVLOldPassword;

    private String mVLNewPassword;

    private boolean mVLPasswordValid;

    private boolean mVLRePasswordValid;

    public ChangePasswordViewListenner(ChangePasswordFragment mVLChangePasswordFragment,
            ChangePasswordViewHolder mVLChangePasswordViewHolder) {
        this.mVLChangePasswordFragment = mVLChangePasswordFragment;
        this.mVLChangePasswordViewHolder = mVLChangePasswordViewHolder;
        mVLPasswordValid = false;
        mVLRePasswordValid = false;
    }

    public void setViewListenner() {
        mVLChangePasswordViewHolder.mVHNewPasswordEt.addTextChangedListener(newPasswordTextWatcher);
        mVLChangePasswordViewHolder.mVHRePasswordEt.addTextChangedListener(rePasswordTextWatcher);

        mVLChangePasswordViewHolder.mVHCurrentPasswordEt.setOnFocusChangeListener(this);
        mVLChangePasswordViewHolder.mVHNewPasswordEt.setOnFocusChangeListener(this);
        mVLChangePasswordViewHolder.mVHRePasswordEt.setOnFocusChangeListener(this);

        mVLChangePasswordViewHolder.mVHNewPasswordEt.setOnKeyListener(this);
        mVLChangePasswordViewHolder.mVHRePasswordEt.setOnKeyListener(this);

        mVLChangePasswordViewHolder.mVHRePasswordEt.setFocusable(false);
        mVLChangePasswordViewHolder.mVHRePasswordEt.setEnabled(false);

        mVLChangePasswordViewHolder.mVHApplyBtn.setOnClickListener(this);
        mVLChangePasswordViewHolder.mVHApplyBtn.setFocusable(false);
        mVLChangePasswordViewHolder.mVHApplyBtn.setEnabled(false);
    }

    public void initVar() {
        disapperTipText();
        mVLChangePasswordViewHolder.mVHNewPasswordWarnIv.setVisibility(View.GONE);
        mVLChangePasswordViewHolder.mVHRePasswordWarnIm.setVisibility(View.GONE);
    }

    private TextWatcher newPasswordTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkNewPassword();
            if (mVLRePasswordValid){
                checkRePassword();
            }
            updateRePasswordEt(mVLPasswordValid);
        }

        @Override
        public void afterTextChanged(Editable s) {
            updateApplyBtn();
        }
    };

    private TextWatcher rePasswordTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkRePassword();
        }

        @Override
        public void afterTextChanged(Editable s) {
            updateApplyBtn();
        }
    };

    @Override
    public void onClick(View arg0) {
        mVLOldPassword = mVLChangePasswordViewHolder.mVHCurrentPasswordEt.getText().toString();
        mVLNewPassword = mVLChangePasswordViewHolder.mVHNewPasswordEt.getText().toString();
        SktApiActor.changePassword(mVLOldPassword, mVLNewPassword);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        disapperTipText();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.et_changepassword_fragment_enternewpw:
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                            && !mVLChangePasswordViewHolder.mVHRePasswordEt.isEnabled())
                        return true;
                    break;
                case R.id.et_changepassword_fragment_re_enternewpw:
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                            && !mVLChangePasswordViewHolder.mVHApplyBtn.isEnabled())
                        return true;
                    break;
                default:
                    break;

            }
        }
        return false;
    }

    public void checkNewPassword() {
        mVLNewPassword = mVLChangePasswordViewHolder.mVHNewPasswordEt.getText().toString();
        VALIDATERESULT valResult;
        valResult = SktApiActor.checkPassword("", mVLNewPassword);
        Log.v("checkNewPassword", "-------------->"+valResult);

        if (valResult != VALIDATERESULT.VALIDATED_OK) {
            mVLChangePasswordViewHolder.mVHNewPasswordWarnIv.setVisibility(View.VISIBLE);
            mVLChangePasswordViewHolder.mVHNewPasswordWarnTv.setVisibility(View.VISIBLE);
            mVLChangePasswordViewHolder.mVHNewPasswordWarnTv
                    .setText(getPasswordInvalidStr(valResult));
            mVLPasswordValid = false;
        } else {
            mVLChangePasswordViewHolder.mVHNewPasswordWarnIv.setVisibility(View.GONE);
            mVLChangePasswordViewHolder.mVHNewPasswordWarnTv.setVisibility(View.GONE);
            mVLPasswordValid = true;
        }
    }

    public void checkRePassword() {
        if (mVLChangePasswordViewHolder.mVHRePasswordEt.getText().toString().equals(mVLNewPassword)) {
            mVLChangePasswordViewHolder.mVHRePasswordWarnIm.setVisibility(View.GONE);
            mVLChangePasswordViewHolder.mVHRePasswordWarnTv.setVisibility(View.GONE);
            mVLRePasswordValid = true;
        } else {
            mVLChangePasswordViewHolder.mVHRePasswordWarnIm.setVisibility(View.VISIBLE);
            mVLChangePasswordViewHolder.mVHRePasswordWarnTv.setVisibility(View.VISIBLE);
            mVLChangePasswordViewHolder.mVHRePasswordWarnTv.setText(mVLChangePasswordFragment
                    .getActivity().getResources().getString(R.string.password_not_same));
            mVLRePasswordValid = false;
        }
    }

    public void updateRePasswordEt(boolean enable) {
        if (enable) {
            mVLChangePasswordViewHolder.mVHRePasswordEt.setEnabled(true);
            mVLChangePasswordViewHolder.mVHRePasswordEt.setFocusable(true);
            mVLChangePasswordViewHolder.mVHRePasswordEt.setFocusableInTouchMode(true);
        } else {
            mVLChangePasswordViewHolder.mVHRePasswordEt.setEnabled(false);
            mVLChangePasswordViewHolder.mVHRePasswordEt.setFocusable(false);
        }
    }

    public void updateApplyBtn() {
        if (mVLPasswordValid && mVLRePasswordValid) {
            mVLChangePasswordViewHolder.mVHApplyBtn.setEnabled(true);
            mVLChangePasswordViewHolder.mVHApplyBtn.setFocusable(true);
        } else {
            mVLChangePasswordViewHolder.mVHApplyBtn.setEnabled(false);
            mVLChangePasswordViewHolder.mVHApplyBtn.setFocusable(false);
        }

    }

    public String getPasswordInvalidStr(VALIDATERESULT result) {
        String str;
        switch (result) {
            case TOO_SHORT:
            case TOO_LONG:
                str = mVLChangePasswordFragment.getActivity().getResources()
                        .getString(R.string.password_length);
                break;
            case STARTS_WITH_INVALID_CHAR:
                str = mVLChangePasswordFragment.getActivity().getResources()
                        .getString(R.string.password_start_invalid);
                break;
            case CONTAINS_SPACE:
                str = mVLChangePasswordFragment.getActivity().getResources()
                        .getString(R.string.password_contain_space);
                break;
            case CONTAINS_INVALID_WORD:
            case CONTAINS_INVALID_CHAR:
            case TOO_SIMPLE:
                str = mVLChangePasswordFragment.getActivity().getResources()
                        .getString(R.string.password_too_simple);
                break;
            case SAME_AS_USERNAME:
                str = mVLChangePasswordFragment.getActivity().getResources()
                        .getString(R.string.password_same_with);
                break;
            default:
                str = mVLChangePasswordFragment.getActivity().getResources()
                        .getString(R.string.password_invalid);
                break;
        }
        return str;
    }

    public void onPasswordChange(String passTip) {
        Toast.makeText(mVLChangePasswordFragment.getActivity(), passTip, Toast.LENGTH_SHORT).show();
        if (passTip.endsWith(mVLChangePasswordFragment.getActivity().getResources().getString(R.string.psw_ok))){
            SkypePrefrences.changePwd(mVLChangePasswordFragment.getActivity(), mVLNewPassword);
        }
    }

    public void disapperTipText() {
        mVLChangePasswordViewHolder.mVHNewPasswordWarnTv.setVisibility(View.GONE);
        mVLChangePasswordViewHolder.mVHRePasswordWarnTv.setVisibility(View.GONE);
    }

}
