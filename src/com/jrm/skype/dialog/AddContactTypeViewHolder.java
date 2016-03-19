
package com.jrm.skype.dialog;

import android.widget.Button;
import android.widget.TextView;
import com.jrm.skype.dialog.AddContactTypeDialog;
import com.jrm.skype.ui.R;

/**
 * @author andy.liu 
 * the ViewHolder of AddContactTypeDialog
 */
public class AddContactTypeViewHolder {
    public AddContactTypeDialog mVHAddContactTypeDialog;

    public Button mVHSkypeContactBtn;

    public Button mVHPhoneNumberBtn;

    public TextView mVHBackTv;

    public AddContactTypeViewHolder(AddContactTypeDialog mVHAddContactTypeDialog) {
        super();
        this.mVHAddContactTypeDialog = mVHAddContactTypeDialog;
    }

    public void findView() {
        mVHSkypeContactBtn = (Button) mVHAddContactTypeDialog.getLayout().findViewById(
                R.id.btn_add_contact_skype);
        mVHPhoneNumberBtn = (Button) mVHAddContactTypeDialog.getLayout().findViewById(
                R.id.btn_add_contact_phone);
        mVHBackTv = (TextView) mVHAddContactTypeDialog.getLayout().findViewById(
                R.id.tv_add_contact_back);
    }

}
