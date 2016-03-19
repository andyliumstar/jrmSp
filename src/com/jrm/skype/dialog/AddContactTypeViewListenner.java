
package com.jrm.skype.dialog;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.dialog.AddContactTypeDialog;
import android.view.View;
import android.view.View.OnClickListener;
import com.jrm.skype.ui.R;
/**
 * @author andy.liu 
 * the ViewListenner of AddContactTypeDialog
 */
public class AddContactTypeViewListenner implements OnClickListener {
    private AddContactTypeDialog mVLAddContactTypeDialog;

    private AddContactTypeViewHolder mVLAddContactTypeViewHolder;

    public AddContactTypeViewListenner(AddContactTypeDialog mVLAddContactTypeDialog,
            AddContactTypeViewHolder mVLAddContactTypeViewHolder) {
        super();
        this.mVLAddContactTypeDialog = mVLAddContactTypeDialog;
        this.mVLAddContactTypeViewHolder = mVLAddContactTypeViewHolder;
    }

    public void setViewListenner() {
        mVLAddContactTypeViewHolder.mVHSkypeContactBtn.setOnClickListener(this);
        mVLAddContactTypeViewHolder.mVHPhoneNumberBtn.setOnClickListener(this);
        mVLAddContactTypeViewHolder.mVHBackTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_contact_skype:
                mVLAddContactTypeDialog.getOwnerActivity().showDialog(
                        SKYPECONSTANT.USRACCOUNTDIALOG.ADD_SKYPE_CONTACT);
                mVLAddContactTypeDialog.dismiss();
                break;
            case R.id.btn_add_contact_phone:
                mVLAddContactTypeDialog.getOwnerActivity().showDialog(
                        SKYPECONSTANT.USRACCOUNTDIALOG.ADD_PHONE_NUMBER);
                mVLAddContactTypeDialog.dismiss();
                break;
            case R.id.tv_add_contact_back:
                mVLAddContactTypeDialog.dismiss();
                break;
            default:
                return;
        }
    }

}
