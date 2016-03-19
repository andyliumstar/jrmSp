
package com.jrm.skype.dialog;

import com.jrm.skype.dialog.SearchResultDialog;
import com.jrm.skype.ui.R;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author andy.liu 
 * the ViewHolder of SearchResultDialog
 */
public class SearchResultViewHolder {
    public SearchResultDialog mVHSearchResultDialog;

    // search result
    public TextView mVHSearchNameTv;

    public ListView mVHResultLv;

    public TextView mVHBackTv;

    // result select
    public RelativeLayout mVHPopMainRel;

    public RelativeLayout mVHPopMidAddRel;

    public RelativeLayout mVHPopMidSendRel;
    
    public ImageView mVHPopMainHeadIv;
    
    public TextView mVHPopMainNameTv;
    
    public TextView mVHPopMidAddLanTv;
    
    public TextView mVHPopMidAddNameTv;
    
    public TextView mVHPopMidAddLocTv;
    
    public TextView mVHPopMidAlreadyInTv;

    public Button mVHAddContactBtn;
    
    public EditText mVHRequestEt;

    public Button mVHSendBtn;

    public Button mVHCancelBtn;

    public TextView mVHPopBackTv;

    public SearchResultViewHolder(SearchResultDialog mVHSearchResultDialog) {
        super();
        this.mVHSearchResultDialog = mVHSearchResultDialog;
    }

    public void findView() {
        mVHSearchNameTv = (TextView) mVHSearchResultDialog.getLayout().findViewById(
                R.id.tv_search_result_name);
        mVHResultLv = (ListView) mVHSearchResultDialog.getLayout().findViewById(
                R.id.lv_search_result_contact);
        mVHBackTv = (TextView) mVHSearchResultDialog.getLayout().findViewById(
                R.id.tv_search_result_back);

        mVHPopMainRel = (RelativeLayout) mVHSearchResultDialog.getLayout().findViewById(
                R.id.rel_resultlist_item_pop_main);
        mVHPopMidAddRel = (RelativeLayout) mVHSearchResultDialog.getLayout().findViewById(
                R.id.rel_resultlist_item_pop_midAdd);
        mVHPopMidSendRel = (RelativeLayout) mVHSearchResultDialog.getLayout().findViewById(
                R.id.rel_resultlist_item_pop_midSend);
        
        mVHPopMainHeadIv = (ImageView) mVHSearchResultDialog.getLayout().findViewById(
                R.id.iv_resultlist_item_pop_head);
        mVHPopMainNameTv = (TextView) mVHSearchResultDialog.getLayout().findViewById(
                R.id.tv_resultlist_item_pop_Mainname);
        mVHPopMidAddNameTv = (TextView) mVHSearchResultDialog.getLayout().findViewById(
                R.id.tv_resultlist_item_pop_skypename);
        mVHPopMidAddLanTv = (TextView) mVHSearchResultDialog.getLayout().findViewById(
                R.id.tv_resultlist_item_pop_language);
        mVHPopMidAddLocTv = (TextView) mVHSearchResultDialog.getLayout().findViewById(
                R.id.tv_resultlist_item_pop_location);

        mVHPopMidAlreadyInTv = (TextView) mVHSearchResultDialog.getLayout().findViewById(
                R.id.tv_resultlist_item_pop_alreadyIn_Tip);
        mVHAddContactBtn = (Button) mVHSearchResultDialog.getLayout().findViewById(
                R.id.btn_resultlist_item_pop_add);
        mVHRequestEt = (EditText) mVHSearchResultDialog.getLayout().findViewById(
                R.id.et_resultlist_item_pop_request);
        mVHSendBtn = (Button) mVHSearchResultDialog.getLayout().findViewById(
                R.id.btn_resultlist_item_pop_send);
        mVHCancelBtn = (Button) mVHSearchResultDialog.getLayout().findViewById(
                R.id.btn_resultlist_item_pop_cancel);
        mVHPopBackTv = (TextView) mVHSearchResultDialog.getLayout().findViewById(
                R.id.tv_resultlist_item_pop_back);
    }

}
