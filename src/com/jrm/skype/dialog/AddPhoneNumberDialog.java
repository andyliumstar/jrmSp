
package com.jrm.skype.dialog;

import com.jrm.skype.constant.SKYPECONSTANT;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

/**
 * @author andy.liu 
 * a Dialog to handle add phone number : input number then add to the contactlist
 */

public class AddPhoneNumberDialog extends MainDialog {
    private AddPhoneNumberViewHolder mAddPhoneNumberViewHolder;

    private AddPhoneNumberViewListenner mAddPhoneNumberListenner;

    public AddPhoneNumberDialog(Context context, int theme) {
        super(context, theme);

        mAddPhoneNumberViewHolder = new AddPhoneNumberViewHolder(this);
        mAddPhoneNumberListenner = new AddPhoneNumberViewListenner(this, mAddPhoneNumberViewHolder);
    }

    @Override
    public void findView() {
        mAddPhoneNumberViewHolder.findView();
    }

    @Override
    public void setViewListenner() {
        mAddPhoneNumberListenner.setViewListenner();
    }

    @Override
    public void initVar(Bundle args) {
        mAddPhoneNumberListenner.initVar();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // when select a coutry ,CountryResourceHolder have remembered it
        if (CountrycodeDialog.mCountryCodeSelected != null)
            mAddPhoneNumberViewHolder.mVHCountryCodeSelectTv
                    .setText(CountrycodeDialog.mCountryCodeSelected);
        // we should reset the select after used
        CountrycodeDialog.mCountryCodeSelected = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.dismiss();
            getOwnerActivity().showDialog(SKYPECONSTANT.USRACCOUNTDIALOG.ADD_CONTACT_TYPE);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
