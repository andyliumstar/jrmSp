
package com.jrm.skype.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

/**
 * @author andy.liu 
 * a Dialog to handle add contact type: skype or phone number
 */
public class AddContactTypeDialog extends MainDialog {
    private AddContactTypeViewHolder mAddContactTypeViewHolder;

    private AddContactTypeViewListenner mAddContactTypeViewListenner;

    public AddContactTypeDialog(Context context, int theme) {
        super(context, theme);

        mAddContactTypeViewHolder = new AddContactTypeViewHolder(this);
        mAddContactTypeViewListenner = new AddContactTypeViewListenner(this,
                mAddContactTypeViewHolder);
    }

    @Override
    public void findView() {
        mAddContactTypeViewHolder.findView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void setViewListenner() {
        mAddContactTypeViewListenner.setViewListenner();
    }

    @Override
    public void initVar(Bundle args) {
        return;
    }

}
