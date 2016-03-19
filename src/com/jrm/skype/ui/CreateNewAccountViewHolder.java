
package com.jrm.skype.ui;

import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.jrm.skype.ui.CreateNewAccountActivity;

/**
 * @author andy.liu 
 * the ViewHolder of CreateNewAccountActivity
 */
public class CreateNewAccountViewHolder {
    public CreateNewAccountActivity mVHCreateNewAccountActivity;

    public EditText mVHFullNameEt;

    public EditText mVHSkypeNameEt;

    public EditText mVHPasswordEt;

    public EditText mVHRePasswordEt;

    public EditText mVHEmailEt;

    public ImageView mVHSkypeNameWarnIv;

    public ImageView mVHPasswordWarnIv;

    public ImageView mVHRePasswordWarnIv;

    public ImageView mVHEmailWarnIv;

    public TextView mVHSkypeNameWarnTv;

    public TextView mVHPasswordWarnTv;

    public TextView mVHRePasswordWarnTv;

    public TextView mVHEmailWarnTv;

    public ImageView mVHSendSkNewsCK;

    public Button mVHAgrCreBtn;

    public Button mVHCancelBtn;

    public TextView mVHBackTv;

    public CreateNewAccountViewHolder(CreateNewAccountActivity mVHCreateNewAccountActivity) {
        super();
        this.mVHCreateNewAccountActivity = mVHCreateNewAccountActivity;
    }

    public void findView() {
        mVHFullNameEt = (EditText) mVHCreateNewAccountActivity
                .findViewById(R.id.et_createAc_fullname);
        mVHSkypeNameEt = (EditText) mVHCreateNewAccountActivity
                .findViewById(R.id.et_createAc_skypename);
        mVHPasswordEt = (EditText) mVHCreateNewAccountActivity
                .findViewById(R.id.et_createAc_password);
        mVHPasswordEt.setTypeface(Typeface.DEFAULT);
        mVHRePasswordEt = (EditText) mVHCreateNewAccountActivity
                .findViewById(R.id.et_createAc_repassword);
        mVHRePasswordEt.setTypeface(Typeface.DEFAULT);
        mVHEmailEt = (EditText) mVHCreateNewAccountActivity.findViewById(R.id.et_createAc_email);

        mVHSkypeNameWarnIv = (ImageView) mVHCreateNewAccountActivity
                .findViewById(R.id.iv_createAc_skypename_wrong);
        mVHPasswordWarnIv = (ImageView) mVHCreateNewAccountActivity
                .findViewById(R.id.iv_createAc_password_wrong);
        mVHRePasswordWarnIv = (ImageView) mVHCreateNewAccountActivity
                .findViewById(R.id.iv_createAc_repassword_wrong);
        mVHEmailWarnIv = (ImageView) mVHCreateNewAccountActivity
                .findViewById(R.id.iv_createAc_email_wrong);

        mVHSkypeNameWarnTv = (TextView) mVHCreateNewAccountActivity
                .findViewById(R.id.tv_createAc_skypename_wrong);
        mVHPasswordWarnTv = (TextView) mVHCreateNewAccountActivity
                .findViewById(R.id.tv_createAc_password_wrong);
        mVHRePasswordWarnTv = (TextView) mVHCreateNewAccountActivity
                .findViewById(R.id.tv_createAc_repassword_wrong);
        mVHEmailWarnTv = (TextView) mVHCreateNewAccountActivity
                .findViewById(R.id.tv_createAc_email_wrong);

        mVHSendSkNewsCK = (ImageView) mVHCreateNewAccountActivity
                .findViewById(R.id.iv_createAc_sendSkNews);
        mVHAgrCreBtn = (Button) mVHCreateNewAccountActivity.findViewById(R.id.btn_createAc_agree);
        mVHCancelBtn = (Button) mVHCreateNewAccountActivity.findViewById(R.id.btn_createAc_cancel);

        mVHBackTv = (TextView) mVHCreateNewAccountActivity.findViewById(R.id.tv_createAc_back);
    }

}
