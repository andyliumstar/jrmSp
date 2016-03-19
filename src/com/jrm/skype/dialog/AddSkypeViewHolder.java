
package com.jrm.skype.dialog;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrm.skype.dialog.AddSkypeDialog;
import com.jrm.skype.ui.R;

/**
 * @author andy.liu 
 * the ViewHolder of AddSkypeDialog
 */
public class AddSkypeViewHolder {
    public AddSkypeDialog mVHAddSkypeDialog;

    // search view
    public RelativeLayout mVHSearchRel;

    public EditText mVHSearchEt;

    public Button mVHSearchBtn;

    public TextView mVHBackTv;

    // searching view
    public RelativeLayout mVHSearchingRel;

    public Button mVHCancelBtn;

    public ImageView mVHSearchingIv;

    // no match found
    public RelativeLayout mVHNomatchRel;

    public Button mVHNomatchOkBtn;

    public AddSkypeViewHolder(AddSkypeDialog mVHAddSkypeDialog) {
        super();
        this.mVHAddSkypeDialog = mVHAddSkypeDialog;
    }

    public void findView() {
        mVHSearchRel = (RelativeLayout) mVHAddSkypeDialog.getLayout().findViewById(
                R.id.rel_add_skype_search);
        mVHSearchingRel = (RelativeLayout) mVHAddSkypeDialog.getLayout().findViewById(
                R.id.rel_add_skype_searching);
        mVHSearchBtn = (Button) mVHAddSkypeDialog.getLayout().findViewById(R.id.btn_add_skype_search);
        mVHCancelBtn = (Button) mVHAddSkypeDialog.getLayout().findViewById(R.id.btn_add_skype_cancel);
        mVHBackTv = (TextView) mVHAddSkypeDialog.getLayout().findViewById(R.id.tv_add_skype_back);

        mVHSearchEt = (EditText) mVHAddSkypeDialog.getLayout().findViewById(R.id.et_add_skype_enter);

        mVHSearchingIv = (ImageView) mVHAddSkypeDialog.getLayout().findViewById(
                R.id.iv_add_skype_searching);

        mVHNomatchRel = (RelativeLayout) mVHAddSkypeDialog.getLayout().findViewById(
                R.id.rel_add_skype_nomatch);
        mVHNomatchOkBtn = (Button) mVHAddSkypeDialog.getLayout().findViewById(
                R.id.btn_add_skype_nomatch_ok);
    }

}
