package com.jrm.skype.dialog;

import android.content.Context;
import android.os.Bundle;

public class DialPadDialog extends MainDialog {
    private DialPadViewHolder mDialPadViewHolder;
    
    private DialPadViewListenner mDialPadViewListenner;

    public DialPadDialog(Context context, int theme) {
        super(context, theme);
        mDialPadViewHolder = new DialPadViewHolder(this);
        mDialPadViewListenner = new DialPadViewListenner(mDialPadViewHolder, this);
    }

    @Override
    public void findView() {
        mDialPadViewHolder.findView();
    }

    @Override
    public void setViewListenner() {
        mDialPadViewListenner.setViewListenner();
    }

    @Override
    public void initVar(Bundle args) {
       return;
    }

}
