
package com.jrm.skype.dialog;

import android.content.Context;
import android.os.Bundle;

/**
 * @author andy.liu 
 * a Dialog to handle the invitation from other skype user
 */
public class InvitationDialog extends MainDialog {
    private InvitationViewHolder mInvitationViewHolder;

    private InvitationViewListenner mInvitationViewListenner;

    public InvitationDialog(Context context, int theme) {
        super(context, theme);

        mInvitationViewHolder = new InvitationViewHolder(this);
        mInvitationViewListenner = new InvitationViewListenner(this, mInvitationViewHolder);
    }

    @Override
    public void findView() {
        mInvitationViewHolder.findView();
    }

    @Override
    public void setViewListenner() {
        mInvitationViewListenner.setViewListenner();
    }

    @Override
    public void initVar(Bundle args) {
        mInvitationViewListenner.initVar();
    }


}
