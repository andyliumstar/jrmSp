
package com.jrm.skype.dialog;

import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrm.skype.dialog.InvitationDialog;
import com.jrm.skype.ui.R;

/**
 * @author andy.liu 
 * the ViewHolder of InvitationDialog
 */

public class InvitationViewHolder {
    public InvitationDialog mVHInvitationDialog;

    public TextView mVHHeaderTv;

    // request from
    public RelativeLayout mVHRequestRel;

    public ListView mVHRequestLv;

    public TextView mVHBackTv;

    // accept ,block,decline
    public RelativeLayout mVHAcceptRel;
    
    public TextView mVHRequestTv;

    public Button mVHAcceptBtn;

    public Button mVHBlockBtn;

    public Button mVHDeclineBtn;

    public InvitationViewHolder(InvitationDialog mVHInvitationDialog) {
        super();
        this.mVHInvitationDialog = mVHInvitationDialog;
    }

    public void findView() {
        mVHHeaderTv = (TextView) mVHInvitationDialog.getLayout().findViewById(
                R.id.tv_invitation_header);
        mVHRequestLv = (ListView) mVHInvitationDialog.getLayout().findViewById(
                R.id.lv_invitation_request);
        mVHBackTv = (TextView) mVHInvitationDialog.getLayout().findViewById(R.id.tv_invitation_back);
        mVHRequestRel = (RelativeLayout) mVHInvitationDialog.getLayout().findViewById(
                R.id.rel_invitation_request);

        mVHAcceptRel = (RelativeLayout) mVHInvitationDialog.getLayout().findViewById(
                R.id.rel_invitation_accept);
        mVHRequestTv = (TextView) mVHInvitationDialog.getLayout().findViewById(
                R.id.tv_invitation_request);
        mVHAcceptBtn = (Button) mVHInvitationDialog.getLayout().findViewById(
                R.id.btn_invitation_accept);
        mVHBlockBtn = (Button) mVHInvitationDialog.getLayout().findViewById(R.id.btn_invitation_block);
        mVHDeclineBtn = (Button) mVHInvitationDialog.getLayout().findViewById(
                R.id.btn_invitation_decline);
    }

}
