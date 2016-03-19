
package com.jrm.skype.dialog;

import com.jrm.skype.api.SktUtils;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.dialog.AddPhoneNumberDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.jrm.skype.ui.R;
import com.jrm.skype.util.SktApiActor;
/**
 * @author andy.liu
 *  the ViewListenner of AddPhoneNumberDialog
 */
public class AddPhoneNumberViewListenner implements OnClickListener {
    private AddPhoneNumberDialog mVLAddPhoneNumberDialog;

    private AddPhoneNumberViewHolder mVLAddPhoneNumberViewHolder;

    public AddPhoneNumberViewListenner(AddPhoneNumberDialog mVLAddPhoneNumberDialog,
            AddPhoneNumberViewHolder mVLAddPhoneNumberViewHolder) {
        super();
        this.mVLAddPhoneNumberDialog = mVLAddPhoneNumberDialog;
        this.mVLAddPhoneNumberViewHolder = mVLAddPhoneNumberViewHolder;
    }

    public void initVar() {
        mVLAddPhoneNumberViewHolder.mVHAddRel.setVisibility(View.VISIBLE);
        mVLAddPhoneNumberViewHolder.mVHInvalidRel.setVisibility(View.GONE);
        mVLAddPhoneNumberViewHolder.mVHCountryCodeSelectTv.requestFocus();
        mVLAddPhoneNumberViewHolder.mVHCountryCodeSelectTv.setText("");
        mVLAddPhoneNumberViewHolder.mVHPhonetNumberEt.setText("");
    }

    public void setViewListenner() {
        mVLAddPhoneNumberViewHolder.mVHCountryCodeSelectTv.setOnClickListener(this);
        mVLAddPhoneNumberViewHolder.mVHAddBtn.setOnClickListener(this);
        mVLAddPhoneNumberViewHolder.mVHBackTv.setOnClickListener(this);
        mVLAddPhoneNumberViewHolder.mVHAOkBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_phonenumber_countrycode:
                mVLAddPhoneNumberDialog.getOwnerActivity().showDialog(
                        SKYPECONSTANT.USRACCOUNTDIALOG.COUNTRYCODE_ADD);
                break;
            case R.id.btn_add_phonenumber_add:
                String number = mVLAddPhoneNumberViewHolder.mVHPhonetNumberEt.getText().toString();
                String code = mVLAddPhoneNumberViewHolder.mVHCountryCodeSelectTv.getText().toString();
                
                if (SktApiActor.checkPSTNNumberBool(number,code)){
                 // 0: can add to; 1: my buddy; 2: my self
                    int ret = SktUtils.canAddToContactList(code + number);
                    if(0 == ret){
                        if (SktApiActor.addContact(code + number, "")){
                            Toast.makeText(mVLAddPhoneNumberDialog.getOwnerActivity(), R.string.add_phonenumber_success, Toast.LENGTH_SHORT).show();
                            mVLAddPhoneNumberDialog.dismiss();
                            mVLAddPhoneNumberDialog.getOwnerActivity().showDialog(
                                    SKYPECONSTANT.USRACCOUNTDIALOG.ADD_CONTACT_TYPE);
                        }
                        else{
                            Toast.makeText(mVLAddPhoneNumberDialog.getOwnerActivity(), R.string.add_phonenumber_failed, Toast.LENGTH_SHORT).show();
                        } 
                    }else{
                        Toast.makeText(mVLAddPhoneNumberDialog.getOwnerActivity(), R.string.phonenumber_alreadyin_contactlist, Toast.LENGTH_SHORT).show();
                    }
                    
                }
                else{
                    mVLAddPhoneNumberViewHolder.mVHAddRel.setVisibility(View.GONE);
                    mVLAddPhoneNumberViewHolder.mVHInvalidRel.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_add_phonenumber_back:
                mVLAddPhoneNumberDialog.dismiss();
                mVLAddPhoneNumberDialog.getOwnerActivity().showDialog(
                        SKYPECONSTANT.USRACCOUNTDIALOG.ADD_CONTACT_TYPE);
                break;
            // when input a invalid number
            case R.id.btn_add_phonenumber_invalid_ok:
                mVLAddPhoneNumberDialog.dismiss();
                mVLAddPhoneNumberDialog.getOwnerActivity().showDialog(
                        SKYPECONSTANT.USRACCOUNTDIALOG.ADD_CONTACT_TYPE);
                break;
            default:
                return;
        }
    }

}
