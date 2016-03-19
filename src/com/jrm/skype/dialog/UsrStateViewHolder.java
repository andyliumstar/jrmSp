
package com.jrm.skype.dialog;

import android.widget.Button;
import android.widget.TextView;

import com.jrm.skype.dialog.UsrStateDialog;
import com.jrm.skype.ui.R;

/**
 * @author andy.liu 
 * the ViewHolder of UsrStateDialog
 */
public class UsrStateViewHolder {
    public UsrStateDialog mVHUsrStateDialog;

    public TextView mVHonlineTv;

    public TextView mVHnodisturbTv;

    public TextView mVHawayTv;

    public TextView mVHinvisibleTv;

    public Button mVHsignoutBtn;

    public UsrStateViewHolder(UsrStateDialog mVHUsrStateDialog) {
        super();
        this.mVHUsrStateDialog = mVHUsrStateDialog;
    }

    public void findView() {
        mVHonlineTv = (TextView) mVHUsrStateDialog.getLayout().findViewById(R.id.tv_usrState_online);
        mVHnodisturbTv = (TextView) mVHUsrStateDialog.getLayout().findViewById(
                R.id.tv_usrState_nodisturb);
        mVHawayTv = (TextView) mVHUsrStateDialog.getLayout().findViewById(R.id.tv_usrState_away);
        mVHinvisibleTv = (TextView) mVHUsrStateDialog.getLayout().findViewById(
                R.id.tv_usrState_invisiable);
        mVHsignoutBtn = (Button) mVHUsrStateDialog.getLayout().findViewById(R.id.btn_usrState_signout);
    }

}
