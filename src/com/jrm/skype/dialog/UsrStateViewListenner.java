
package com.jrm.skype.dialog;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.dialog.UsrStateDialog;
import com.jrm.skype.ui.UsrAccountActivity;
import android.view.View;
import android.view.View.OnClickListener;
import com.jrm.skype.ui.R;
import com.jrm.skype.util.SktApiActor;
import com.skype.api.Contact.AVAILABILITY;
/**
 * @author andy.liu 
 * the ViewListenner of UsrStateDialog
 */
public class UsrStateViewListenner implements OnClickListener {
    private UsrStateDialog mVLUsrStateDialog;

    private UsrStateViewHolder mVLUsrStateViewHolder;

    public UsrStateViewListenner(UsrStateDialog mVLUsrStateDialog,
            UsrStateViewHolder mVLUsrStateViewHolder) {
        super();
        this.mVLUsrStateDialog = mVLUsrStateDialog;
        this.mVLUsrStateViewHolder = mVLUsrStateViewHolder;
    }

    public void setViewListenner() {
        mVLUsrStateViewHolder.mVHonlineTv.setOnClickListener(this);
        mVLUsrStateViewHolder.mVHnodisturbTv.setOnClickListener(this);
        mVLUsrStateViewHolder.mVHawayTv.setOnClickListener(this);
        mVLUsrStateViewHolder.mVHinvisibleTv.setOnClickListener(this);
        mVLUsrStateViewHolder.mVHsignoutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_usrState_online:
                onlineTvClick();
                break;
            case R.id.tv_usrState_nodisturb:
                nodisturbTvClick();
                break;
            case R.id.tv_usrState_away:
                awayTvClick();
                break;
            case R.id.tv_usrState_invisiable:
                invisibleTvClick();
                break;
            case R.id.btn_usrState_signout:
                signoutBtnClick();
                break;
            default:
                break;
        } // switch
    }

    public void onlineTvClick() {
        if (((UsrAccountActivity) mVLUsrStateDialog.getOwnerActivity()).getViewListenner() != null
                && SktApiActor.getAccountAvailability() != AVAILABILITY.CONNECTING) {
            ((UsrAccountActivity) mVLUsrStateDialog.getOwnerActivity()).getViewListenner()
                    .setUsrStateIm(R.drawable.presenceonline_32x32);
            SktApiActor.setAccountAvailability(AVAILABILITY.ONLINE);
        }

        mVLUsrStateDialog.dismiss();
    }

    public void nodisturbTvClick() {
        if (((UsrAccountActivity) mVLUsrStateDialog.getOwnerActivity()).getViewListenner() != null
                && SktApiActor.getAccountAvailability() != AVAILABILITY.CONNECTING) {
            ((UsrAccountActivity) mVLUsrStateDialog.getOwnerActivity()).getViewListenner()
                    .setUsrStateIm(R.drawable.presencedonotdisturb_32x32);
            SktApiActor.setAccountAvailability(AVAILABILITY.DO_NOT_DISTURB);
        }

        mVLUsrStateDialog.dismiss();
    }

    public void awayTvClick() {
        if (((UsrAccountActivity) mVLUsrStateDialog.getOwnerActivity()).getViewListenner() != null
                && SktApiActor.getAccountAvailability() != AVAILABILITY.CONNECTING) {
            ((UsrAccountActivity) mVLUsrStateDialog.getOwnerActivity()).getViewListenner()
                    .setUsrStateIm(R.drawable.presenceaway_32x32);
            SktApiActor.setAccountAvailability(AVAILABILITY.AWAY);
        }

        mVLUsrStateDialog.dismiss();
    }

    public void invisibleTvClick() {
        if (((UsrAccountActivity) mVLUsrStateDialog.getOwnerActivity()).getViewListenner() != null
                && SktApiActor.getAccountAvailability() != AVAILABILITY.CONNECTING) {
            ((UsrAccountActivity) mVLUsrStateDialog.getOwnerActivity()).getViewListenner()
                    .setUsrStateIm(R.drawable.presenceinvisible_32x32);
            SktApiActor.setAccountAvailability(AVAILABILITY.INVISIBLE);
        }
        mVLUsrStateDialog.dismiss();
    }

    public void signoutBtnClick() {
        mVLUsrStateDialog.dismiss();

        mVLUsrStateDialog.getOwnerActivity().showDialog(SKYPECONSTANT.USRACCOUNTDIALOG.SIGN_OUT);
    }

}
