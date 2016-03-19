
package com.jrm.skype.dialog;

import com.jrm.skype.api.SktUtils;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.dialog.AddSkypeDialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.jrm.skype.ui.R;
import com.jrm.skype.util.SktApiActor;
/**
 * @author andy.liu 
 * the ViewListenner of AddSkypeDialog
 */
public class AddSkypeViewListenner implements OnClickListener {
    private AddSkypeViewHolder mVLAddSkypeViewHolder;

    private AddSkypeDialog mVLAddSkypeDialog;

    private String mVLSearchStr;

    /* for search running */
    private AnimationDrawable mVLSearchingAnimation;

    public AddSkypeViewListenner(AddSkypeViewHolder mVLAddSkypeViewHolder,
            AddSkypeDialog mVLAddSkypeDialog) {
        super();
        this.mVLAddSkypeViewHolder = mVLAddSkypeViewHolder;
        this.mVLAddSkypeDialog = mVLAddSkypeDialog;
    }

    public void initVar() {
        mVLAddSkypeViewHolder.mVHSearchRel.setVisibility(View.VISIBLE);
        mVLAddSkypeViewHolder.mVHSearchingRel.setVisibility(View.GONE);
        mVLAddSkypeViewHolder.mVHNomatchRel.setVisibility(View.GONE);
        mVLAddSkypeViewHolder.mVHSearchEt.setText("");

        mVLSearchingAnimation = (AnimationDrawable) mVLAddSkypeViewHolder.mVHSearchingIv.getBackground();
    }

    public void setViewListenner() {
        mVLAddSkypeViewHolder.mVHSearchBtn.setOnClickListener(this);
        mVLAddSkypeViewHolder.mVHCancelBtn.setOnClickListener(this);
        mVLAddSkypeViewHolder.mVHBackTv.setOnClickListener(this);
        mVLAddSkypeViewHolder.mVHNomatchOkBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_add_skype_search:
                searchBtnClick();
                break;
            case R.id.btn_add_skype_cancel:
                cancelBtnClick();
                break;
            case R.id.tv_add_skype_back:
                mVLAddSkypeDialog.dismiss();
                mVLAddSkypeDialog.getOwnerActivity().showDialog(
                        SKYPECONSTANT.USRACCOUNTDIALOG.ADD_CONTACT_TYPE);
                break;
            case R.id.btn_add_skype_nomatch_ok:
                mVLAddSkypeDialog.dismiss();
                mVLAddSkypeDialog.getOwnerActivity().showDialog(
                        SKYPECONSTANT.USRACCOUNTDIALOG.ADD_CONTACT_TYPE);
                break;
            default:
                return;
        }

    }

    public void searchBtnClick() {
        mVLSearchStr = mVLAddSkypeViewHolder.mVHSearchEt.getEditableText().toString().trim();

        if (mVLSearchStr.equals(""))
            Toast.makeText(mVLAddSkypeDialog.getOwnerActivity(), R.string.input_is_null, Toast.LENGTH_SHORT)
                    .show();
        else {
        	SktUtils.search(mVLSearchStr);
        }
    }

    public void cancelBtnClick() {
        SktApiActor.cancelContactSearch();
        mVLSearchingAnimation.stop();
        mVLAddSkypeViewHolder.mVHSearchRel.setVisibility(View.VISIBLE);
        mVLAddSkypeViewHolder.mVHSearchingRel.setVisibility(View.GONE);
    }

    /*
     * search running
     */
    public void searchRunning(){
        mVLAddSkypeViewHolder.mVHSearchRel.setVisibility(View.GONE);
        mVLAddSkypeViewHolder.mVHSearchingRel.setVisibility(View.VISIBLE);
        mVLSearchingAnimation.start();
        mVLAddSkypeViewHolder.mVHCancelBtn.requestFocus();
    }
    
    public void searchFinished(){
        mVLSearchingAnimation.stop();
        Bundle bl = new Bundle();
        bl.putString(SKYPECONSTANT.SKYPESTRING.SEARCHSTRING, mVLSearchStr);
        mVLAddSkypeDialog.getOwnerActivity().showDialog(
                SKYPECONSTANT.USRACCOUNTDIALOG.SEARCH_RESULT,bl);
        mVLAddSkypeDialog.dismiss();
    }
 
    public void searchFailed(){
        mVLSearchingAnimation.stop();
        mVLAddSkypeViewHolder.mVHSearchRel.setVisibility(View.VISIBLE);
        mVLAddSkypeViewHolder.mVHSearchingRel.setVisibility(View.GONE);
        Toast.makeText(mVLAddSkypeDialog.getOwnerActivity(), R.string.search_failed, Toast.LENGTH_SHORT).show();
    }
    public void noMatchFound() {
        mVLAddSkypeViewHolder.mVHNomatchRel.setVisibility(View.VISIBLE);
        mVLAddSkypeViewHolder.mVHSearchingRel.setVisibility(View.GONE);
        mVLAddSkypeViewHolder.mVHNomatchOkBtn.requestFocus();
        mVLSearchingAnimation.stop();
    }
}
