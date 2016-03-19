
package com.jrm.skype.dialog;

import android.content.Context;
import android.os.Bundle;

/**
 * @author andy.liu 
 * change the user's state or sign out
 */

public class UsrStateDialog extends MainDialog {
    private UsrStateViewHolder mUsrStateViewHolder;

    private UsrStateViewListenner mUsrStateViewListenner;

    public UsrStateDialog(Context context, int theme) {
        super(context, theme);

        mUsrStateViewHolder = new UsrStateViewHolder(this);
        mUsrStateViewListenner = new UsrStateViewListenner(this, mUsrStateViewHolder);
    }

    @Override
    public void findView() {
        mUsrStateViewHolder.findView();
    }

    @Override
    public void setViewListenner() {
        mUsrStateViewListenner.setViewListenner();
    }

    @Override
    public void initVar(Bundle args) {
        mUsrStateViewHolder.mVHsignoutBtn.requestFocus();
    }

}
