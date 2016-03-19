
package com.jrm.skype.dialog;

import com.jrm.skype.constant.SKYPECONSTANT;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

/**
 * @author andy.liu 
 *  a Dialog to show the search results of skype users and add to  contactlist
 */
public class SearchResultDialog extends MainDialog {
    private SearchResultViewHolder mSearchResultViewHolder;

    private SearchResultViewListenner mSearchResultViewListenner;

    public SearchResultDialog(Context context, int theme) {
        super(context, theme);

        mSearchResultViewHolder = new SearchResultViewHolder(this);
        mSearchResultViewListenner = new SearchResultViewListenner(this, mSearchResultViewHolder);

    }

    @Override
    public void findView() {
        mSearchResultViewHolder.findView();
    }

    @Override
    public void setViewListenner() {
        mSearchResultViewListenner.setViewListenner();
    }

    @Override
    public void initVar(Bundle args) {
        mSearchResultViewListenner.initVar(args);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mSearchResultViewHolder.mVHPopMainRel.getVisibility() == View.VISIBLE) {
                mSearchResultViewHolder.mVHPopMidSendRel.setVisibility(View.GONE);
                mSearchResultViewHolder.mVHPopMainRel.setVisibility(View.GONE);
                mSearchResultViewHolder.mVHResultLv.setFocusable(true);
                mSearchResultViewHolder.mVHResultLv.setEnabled(true);
            } else {
                getOwnerActivity().showDialog(SKYPECONSTANT.USRACCOUNTDIALOG.ADD_SKYPE_CONTACT);
                this.dismiss();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
