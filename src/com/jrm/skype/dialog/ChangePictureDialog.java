package com.jrm.skype.dialog;

import android.content.Context;
import android.os.Bundle;

public class ChangePictureDialog extends MainDialog {
    private ChangePictureViewHolder mChangePictureViewHolder;
   
    private ChangePictureViewListenner mChangePictureViewListenner;
    
    public ChangePictureDialog(Context context, int theme) {
        super(context, theme);
        mChangePictureViewHolder = new ChangePictureViewHolder(this);
        mChangePictureViewListenner = new ChangePictureViewListenner(this, mChangePictureViewHolder);
        
    }

    @Override
    public void findView() {
        mChangePictureViewHolder.findView();
    }

    @Override
    public void setViewListenner() {
        mChangePictureViewListenner.setViewListenner();
    }

    @Override
    public void initVar(Bundle args) {

    }

}
