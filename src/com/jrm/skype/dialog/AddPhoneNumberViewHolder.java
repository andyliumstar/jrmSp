
package com.jrm.skype.dialog;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrm.skype.dialog.AddPhoneNumberDialog;
import com.jrm.skype.ui.R;

/**
 * @author andy.liu 
 * the ViewHolder of AddPhoneNumberDialog
 */
public class AddPhoneNumberViewHolder {
    public AddPhoneNumberDialog mVHAddPhoneNumberDialog;

    // the normal view to show
    public Button mVHAddBtn;

    public TextView mVHCountryCodeSelectTv;
    
    public EditText mVHPhonetNumberEt;

    public TextView mVHBackTv;

    public RelativeLayout mVHAddRel;

    // when input a invalid number
    public RelativeLayout mVHInvalidRel;

    public Button mVHAOkBtn;

    public AddPhoneNumberViewHolder(AddPhoneNumberDialog mVHAddPhoneNumberDialog) {
        super();
        this.mVHAddPhoneNumberDialog = mVHAddPhoneNumberDialog;
    }

    public void findView() {
        mVHCountryCodeSelectTv = (TextView) mVHAddPhoneNumberDialog.getLayout().findViewById(
                R.id.tv_add_phonenumber_countrycode);
        mVHPhonetNumberEt = (EditText) mVHAddPhoneNumberDialog.getLayout().findViewById(
                R.id.et_add_phonenumber_enter);
        mVHAddBtn = (Button) mVHAddPhoneNumberDialog.getLayout().findViewById(
                R.id.btn_add_phonenumber_add);
        mVHBackTv = (TextView) mVHAddPhoneNumberDialog.getLayout().findViewById(
                R.id.tv_add_phonenumber_back);

        mVHAddRel = (RelativeLayout) mVHAddPhoneNumberDialog.getLayout().findViewById(
                R.id.rel_add_phonenumber);
        mVHInvalidRel = (RelativeLayout) mVHAddPhoneNumberDialog.getLayout().findViewById(
                R.id.rel_add_phonenumber_invalid);
        mVHAOkBtn = (Button) mVHAddPhoneNumberDialog.getLayout().findViewById(
                R.id.btn_add_phonenumber_invalid_ok);
    }

}
