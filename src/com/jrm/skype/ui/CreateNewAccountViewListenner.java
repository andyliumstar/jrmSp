
package com.jrm.skype.ui;

import com.jrm.skype.api.SktUtils;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.ui.CreateNewAccountActivity;
import com.jrm.skype.util.SktApiActor;
import com.jrm.skype.util.SkypePrefrences;
import com.skype.api.Skype.VALIDATERESULT;
import com.skype.api.Skype.ValidateProfileStringResult;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Toast;

/**
 * @author andy.liu 
 * the ViewListenner of CreateNewAccountActivity
 */
public class CreateNewAccountViewListenner implements OnClickListener, OnFocusChangeListener{
    private CreateNewAccountActivity mVLCreateNewAccountActivity;

    private CreateNewAccountViewHolder mVLCreateNewAccountViewHolder;

    private boolean mVLSendSkNews;
    /*
     * record the input
     */
    private boolean mVLSkypeNameInput;

    private boolean mVLPasswordInput;

    private boolean mVLRePasswordInput;

    private boolean mVLEmailInput;
    
    private String mVLFullNameStr;

    private String mVLSkypeNameStr;

    private String mVLPasswordStr;

    private String mVLRePasswordStr;

    private String mVLEmailStr;

    public CreateNewAccountViewListenner(CreateNewAccountActivity mVLCreateNewAccountActivity,
            CreateNewAccountViewHolder mVLCreateNewAccountViewHolder) {
        super();
        this.mVLCreateNewAccountActivity = mVLCreateNewAccountActivity;
        this.mVLCreateNewAccountViewHolder = mVLCreateNewAccountViewHolder;

        mVLSendSkNews = false;
        mVLSkypeNameInput = false;
        mVLPasswordInput = false;
        mVLRePasswordInput = false;
        mVLEmailInput = false;
    }

    public void setViewListenner() {
        mVLCreateNewAccountViewHolder.mVHSendSkNewsCK.setOnClickListener(this);
        mVLCreateNewAccountViewHolder.mVHSendSkNewsCK.setOnFocusChangeListener(this);

        mVLCreateNewAccountViewHolder.mVHAgrCreBtn.setOnClickListener(this);
        mVLCreateNewAccountViewHolder.mVHAgrCreBtn.setEnabled(false);
        mVLCreateNewAccountViewHolder.mVHAgrCreBtn.setFocusable(false);

        mVLCreateNewAccountViewHolder.mVHCancelBtn.setOnClickListener(this);
        mVLCreateNewAccountViewHolder.mVHBackTv.setOnClickListener(this);

        mVLCreateNewAccountViewHolder.mVHFullNameEt.setOnFocusChangeListener(this);
        mVLCreateNewAccountViewHolder.mVHSkypeNameEt.setOnFocusChangeListener(this);
        mVLCreateNewAccountViewHolder.mVHPasswordEt.setOnFocusChangeListener(this);
        mVLCreateNewAccountViewHolder.mVHEmailEt.setOnFocusChangeListener(this);
        mVLCreateNewAccountViewHolder.mVHRePasswordEt.setOnFocusChangeListener(this);

        mVLCreateNewAccountViewHolder.mVHRePasswordEt.setFocusable(false);
        mVLCreateNewAccountViewHolder.mVHRePasswordEt.setEnabled(false);
        
        mVLCreateNewAccountViewHolder.mVHFullNameEt.addTextChangedListener(fullNameTextWatcher);
        mVLCreateNewAccountViewHolder.mVHSkypeNameEt.addTextChangedListener(skypeNameTextWatcher);
        mVLCreateNewAccountViewHolder.mVHPasswordEt.addTextChangedListener(passwordTextWatcher);
        mVLCreateNewAccountViewHolder.mVHRePasswordEt.addTextChangedListener(rePasswordTextWatcher);
        mVLCreateNewAccountViewHolder.mVHEmailEt.addTextChangedListener(emailTextWatcher);
    }
    
    public void initVar(){
        if (SkypePrefrences.isCreateOut(mVLCreateNewAccountActivity)){
            mVLFullNameStr  = SkypePrefrences.getCreateFullName(mVLCreateNewAccountActivity);
            mVLSkypeNameStr = SkypePrefrences.getCreateSkypeName(mVLCreateNewAccountActivity);
            mVLPasswordStr = SkypePrefrences.getCreatePassword(mVLCreateNewAccountActivity);
            mVLRePasswordStr = mVLPasswordStr;
            mVLEmailStr = SkypePrefrences.getCreateEmail(mVLCreateNewAccountActivity);
            mVLSendSkNews =  SkypePrefrences.getCreateSendSKNEWS(mVLCreateNewAccountActivity);
            
            mVLCreateNewAccountViewHolder.mVHFullNameEt.setText(mVLFullNameStr);
            mVLCreateNewAccountViewHolder.mVHSkypeNameEt.setText(mVLSkypeNameStr);
            mVLCreateNewAccountViewHolder.mVHPasswordEt.setText(mVLPasswordStr);
            mVLCreateNewAccountViewHolder.mVHRePasswordEt.setText(mVLRePasswordStr);
            mVLCreateNewAccountViewHolder.mVHEmailEt.setText(mVLEmailStr);
            
            mVLSkypeNameInput = true;
            mVLPasswordInput = true;
            mVLRePasswordInput = true;
            mVLEmailInput = true;
            
            updateRePasswordEt();
            updateAgreeBtn();
        }
        
        SkypePrefrences.setCreateOut(mVLCreateNewAccountActivity, false);
    }

    /*
     * 5 edit text TextWatcher----------------------------------------------------------------------->
     */
    //fullNameTextWatcher ---------->
    private TextWatcher fullNameTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            fullNameEditDone();
        }

        @Override
        public void afterTextChanged(Editable s) {
            
        }  
    };
    
    //skypeNameTextWatcher-------------->
    private TextWatcher skypeNameTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            skypeNameEditDone();
        }

        @Override
        public void afterTextChanged(Editable s) {
            updateAgreeBtn();
        }  
    };
    //passwordTextWatcher------------------->
    private TextWatcher passwordTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            passwordEditDone();
            if (mVLRePasswordInput == true)
                rePasswordEditDone();
            updateRePasswordEt();
        }

        @Override
        public void afterTextChanged(Editable s) {
            updateAgreeBtn();
        }  
    };  
    //rePasswordTextWatcher------------------>
    private TextWatcher rePasswordTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            rePasswordEditDone();
        }

        @Override
        public void afterTextChanged(Editable s) {
            updateAgreeBtn();
        }  
    };
    //emailTextWatcher----------------------->
    private TextWatcher emailTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            emailEditDone();
        }

        @Override
        public void afterTextChanged(Editable s) {
            updateAgreeBtn();
        }  
    };
    
    /*
     * 2 callback functions:onFocusChange; onClick------------------------------------------------------->
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.iv_createAc_sendSkNews:
                if (hasFocus)
                    if (!mVLSendSkNews)
                        mVLCreateNewAccountViewHolder.mVHSendSkNewsCK
                                .setImageResource(R.drawable.skype_img_login_checkbox_sel_no);
                    else
                        mVLCreateNewAccountViewHolder.mVHSendSkNewsCK
                                .setImageResource(R.drawable.skype_img_login_checkbox_sel);
                else if (!mVLSendSkNews)
                    mVLCreateNewAccountViewHolder.mVHSendSkNewsCK
                            .setImageResource(R.drawable.skype_img_login_checkbox);
                else
                    mVLCreateNewAccountViewHolder.mVHSendSkNewsCK
                            .setImageResource(R.drawable.skype_img_login_checkbox_on);
                break;
            case R.id.et_createAc_repassword:
                if (!hasFocus)
                    rePasswordEditDone();
                break;
            
            case R.id.et_createAc_fullname:
                if (!hasFocus)
                    fullNameEditDone();
                break;
            case R.id.et_createAc_skypename:
                if (!hasFocus)
                    skypeNameEditDone();
                break;
            case R.id.et_createAc_password:
                if (!hasFocus){
                    passwordEditDone();
                    if (mVLRePasswordInput == true)
                        rePasswordEditDone();
                    updateRePasswordEt();
                }
                break;
            case R.id.et_createAc_email:
                if (!hasFocus)
                    emailEditDone();
                break;
            default:
                break;
        }
        disapperTipText();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_createAc_sendSkNews:
                mVLSendSkNews = !mVLSendSkNews;
                sendSkNewsImClick();
                break;
            case R.id.btn_createAc_agree:
                agreeBtnClick();
                break;
            case R.id.tv_createAc_back:
            case R.id.btn_createAc_cancel:
                mVLCreateNewAccountActivity.back();
                break;
            default:
                break;
        }
    }

    /*
     * 5 edittexts' Editdone functions----------------------------------------------------------------------->
     */
    public void fullNameEditDone() {
        mVLFullNameStr = mVLCreateNewAccountViewHolder.mVHFullNameEt.getEditableText().toString().trim();
    }

    public void skypeNameEditDone() {
        mVLSkypeNameStr =  mVLCreateNewAccountViewHolder.mVHSkypeNameEt.getEditableText().toString();
        ValidateProfileStringResult vpsResult;
        vpsResult = SktApiActor.checkSkypeName(mVLSkypeNameStr, true);
        
        if (null == vpsResult)
            return;
        
        if (vpsResult.result == VALIDATERESULT.VALIDATED_OK) {
            mVLSkypeNameInput = true;
            mVLCreateNewAccountViewHolder.mVHSkypeNameWarnIv.setVisibility(View.GONE);
            mVLCreateNewAccountViewHolder.mVHSkypeNameWarnTv.setVisibility(View.GONE);
        } else {
            mVLSkypeNameInput = false;
            mVLCreateNewAccountViewHolder.mVHSkypeNameWarnIv.setVisibility(View.VISIBLE);
            mVLCreateNewAccountViewHolder.mVHSkypeNameWarnTv.setVisibility(View.VISIBLE);
            mVLCreateNewAccountViewHolder.mVHSkypeNameWarnTv
                    .setText(getSkypeNameInvalidStr(vpsResult.result));
        }
    }

    public void passwordEditDone() {
        mVLPasswordStr = mVLCreateNewAccountViewHolder.mVHPasswordEt.getEditableText().toString();
        VALIDATERESULT valResult;
        valResult = SktApiActor.checkPassword(mVLSkypeNameStr == null?"":mVLSkypeNameStr, mVLPasswordStr);
        
        if (null == valResult)
            return;
        
        if(valResult == VALIDATERESULT.VALIDATED_OK) {
            mVLPasswordInput = true;
            mVLCreateNewAccountViewHolder.mVHPasswordWarnIv.setVisibility(View.GONE);
            mVLCreateNewAccountViewHolder.mVHPasswordWarnTv.setVisibility(View.GONE);
        } else {
            mVLPasswordInput = false;
            mVLCreateNewAccountViewHolder.mVHPasswordWarnIv.setVisibility(View.VISIBLE);
            mVLCreateNewAccountViewHolder.mVHPasswordWarnTv.setVisibility(View.VISIBLE);
            mVLCreateNewAccountViewHolder.mVHPasswordWarnTv.setText(getPasswordInvalidStr(valResult));
        }
    }

    public void rePasswordEditDone() {
        mVLRePasswordStr = mVLCreateNewAccountViewHolder.mVHRePasswordEt.getText().toString();
        
        if (mVLRePasswordStr.equals(mVLPasswordStr)) {
            mVLRePasswordInput = true;
            mVLCreateNewAccountViewHolder.mVHRePasswordWarnIv.setVisibility(View.GONE);
            mVLCreateNewAccountViewHolder.mVHRePasswordWarnTv.setVisibility(View.GONE);
        } else {
            mVLRePasswordInput = false;
            mVLCreateNewAccountViewHolder.mVHRePasswordWarnIv.setVisibility(View.VISIBLE);
            mVLCreateNewAccountViewHolder.mVHRePasswordWarnTv.setVisibility(View.VISIBLE);
            mVLCreateNewAccountViewHolder.mVHRePasswordWarnTv.setText(R.string.password_not_same);
        }
    }

    public void emailEditDone() {
        mVLEmailStr = mVLCreateNewAccountViewHolder.mVHEmailEt.getEditableText().toString();
        
        ValidateProfileStringResult vpsResult;
        vpsResult = SktApiActor.checkEmail(mVLEmailStr, true);
        
        if (null == vpsResult)
            return;
        
        if(vpsResult.result == VALIDATERESULT.VALIDATED_OK) {
            mVLEmailInput = true;
            mVLCreateNewAccountViewHolder.mVHEmailWarnIv.setVisibility(View.GONE);
            mVLCreateNewAccountViewHolder.mVHEmailWarnTv.setVisibility(View.GONE);
        } else {
            mVLEmailInput = false;
            mVLCreateNewAccountViewHolder.mVHEmailWarnIv.setVisibility(View.VISIBLE);
            mVLCreateNewAccountViewHolder.mVHEmailWarnTv.setVisibility(View.VISIBLE);
            mVLCreateNewAccountViewHolder.mVHEmailWarnTv.setText(getEmailInvalidStr(vpsResult.result));
        }
    }
    
    /*
     *  three invalid string tips----------------------------------------------------------------------->
     */
    public String getSkypeNameInvalidStr(VALIDATERESULT result) {
        String str;
        switch(result){
            case TOO_SHORT:
            case TOO_LONG:
                str = mVLCreateNewAccountActivity.getResources().getString(R.string.skypename_length);
                break;
            case STARTS_WITH_INVALID_CHAR:
                str = mVLCreateNewAccountActivity.getResources().getString(R.string.skypename_start_invalid);
                break;
            case CONTAINS_INVALID_WORD:
            case CONTAINS_SPACE:
            case CONTAINS_INVALID_CHAR:
                str = mVLCreateNewAccountActivity.getResources().getString(R.string.skypename_contain_invalid);
                break;
            default:
                str = mVLCreateNewAccountActivity.getResources().getString(R.string.skypename_invalid);
                break;
        }
        return str;
    }

    public String getPasswordInvalidStr(VALIDATERESULT result) {
        String str;
        switch(result){
            case TOO_SHORT:
            case TOO_LONG:
                str = mVLCreateNewAccountActivity.getResources().getString(R.string.password_length);
                break;
            case STARTS_WITH_INVALID_CHAR:
                str = mVLCreateNewAccountActivity.getResources().getString(R.string.password_start_invalid);
                break;
            case CONTAINS_SPACE:
            	str = mVLCreateNewAccountActivity.getResources().getString(R.string.password_contain_space);
            	break;
            case CONTAINS_INVALID_WORD:
            case CONTAINS_INVALID_CHAR:
            case TOO_SIMPLE:
                str = mVLCreateNewAccountActivity.getResources().getString(R.string.password_too_simple);
                break;
            case SAME_AS_USERNAME:
                str = mVLCreateNewAccountActivity.getResources().getString(R.string.password_same_with);
                break;
            default:
                str = mVLCreateNewAccountActivity.getResources().getString(R.string.password_invalid);
                break;
        }
        return str;
    }
    
    public String getEmailInvalidStr(VALIDATERESULT result) {
        String str = mVLCreateNewAccountActivity.getResources().getString(R.string.email_invalid);
        return str;
    }
    
    /*
     * two views: a eidttext and a button should update its state-------------------------------------------------------->
     */
    public void updateRePasswordEt() {
        if (mVLPasswordInput) {
            mVLCreateNewAccountViewHolder.mVHRePasswordEt.setFocusable(true);
            mVLCreateNewAccountViewHolder.mVHRePasswordEt.setFocusableInTouchMode(true);
            mVLCreateNewAccountViewHolder.mVHRePasswordEt.setEnabled(true);
        } else {
            mVLCreateNewAccountViewHolder.mVHRePasswordEt.setFocusable(false);
            mVLCreateNewAccountViewHolder.mVHRePasswordEt.setEnabled(false);
        }
    }

    public void updateAgreeBtn() {
        if ( mVLSkypeNameInput && mVLPasswordInput && mVLRePasswordInput && mVLEmailInput) {
            mVLCreateNewAccountViewHolder.mVHAgrCreBtn.setEnabled(true);
            mVLCreateNewAccountViewHolder.mVHAgrCreBtn.setFocusable(true);
        } else {
            mVLCreateNewAccountViewHolder.mVHAgrCreBtn.setEnabled(false);
            mVLCreateNewAccountViewHolder.mVHAgrCreBtn.setFocusable(false);

        }
    }

    /*
     * view click function----------------------------------------------------------------------->
     */
    public void sendSkNewsImClick() {
        if (!mVLSendSkNews)
            if (mVLCreateNewAccountViewHolder.mVHSendSkNewsCK.hasFocus())
                mVLCreateNewAccountViewHolder.mVHSendSkNewsCK
                        .setImageResource(R.drawable.skype_img_login_checkbox_sel_no);
            else
                mVLCreateNewAccountViewHolder.mVHSendSkNewsCK
                        .setImageResource(R.drawable.skype_img_login_checkbox);
        else if (mVLCreateNewAccountViewHolder.mVHSendSkNewsCK.hasFocus())
            mVLCreateNewAccountViewHolder.mVHSendSkNewsCK
                    .setImageResource(R.drawable.skype_img_login_checkbox_sel);
        else
            mVLCreateNewAccountViewHolder.mVHSendSkNewsCK
                    .setImageResource(R.drawable.skype_img_login_checkbox_on);
    }

    public void agreeBtnClick() {
        SkypePrefrences.recordCreateInfo(mVLCreateNewAccountActivity, mVLSkypeNameStr,
                mVLPasswordStr, mVLEmailStr, mVLFullNameStr, mVLSkypeNameInput);
        
        if (!SktUtils.isNetworkAvailable(mVLCreateNewAccountActivity)) {
        	Toast.makeText(mVLCreateNewAccountActivity, R.string.internet_connect_not_available, Toast.LENGTH_SHORT).show();
        	return;
        }
        
        boolean ret = SktApiActor.createNewAccount(mVLSkypeNameStr, mVLPasswordStr, true, mVLEmailStr, mVLSendSkNews);
        if (ret){
            Intent intent = new Intent();
            intent.putExtra( SKYPECONSTANT.SKYPESTRING.CREATEFULLNAME, mVLFullNameStr);
            intent.setClass(mVLCreateNewAccountActivity, CreatingActivity.class);
            mVLCreateNewAccountActivity.startActivity(intent);
            mVLCreateNewAccountActivity.finish();
        }else{
            Toast.makeText(mVLCreateNewAccountActivity, R.string.invalid_input, Toast.LENGTH_SHORT).show();
        }
       
    }
    
    //other function
    public void disapperTipText() {
        mVLCreateNewAccountViewHolder.mVHSkypeNameWarnTv.setVisibility(View.GONE);
        mVLCreateNewAccountViewHolder.mVHPasswordWarnTv.setVisibility(View.GONE);
        mVLCreateNewAccountViewHolder.mVHRePasswordWarnTv.setVisibility(View.GONE);
        mVLCreateNewAccountViewHolder.mVHEmailWarnTv.setVisibility(View.GONE);
    }
}
