
package com.jrm.skype.dialog;

import com.jrm.skype.api.SktContact.SktBuddy;
import com.jrm.skype.api.SktUtils;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.dialog.SearchResultDialog;
import com.jrm.skype.listview.adapter.ListViewAdapter;
import com.jrm.skype.listview.itemholder.SearchResultListItemHolder;
import com.jrm.skype.listview.resourceholder.SearchResultResourceHolder;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.jrm.skype.ui.R;
import com.jrm.skype.util.CountryInfoHelper;
import com.jrm.skype.util.SktApiActor;
/**
 * @author andy.liu
 *  the ViewListenner of SearchResultDialog
 */
public class SearchResultViewListenner implements OnClickListener {
    private SearchResultDialog mVLSearchResultDialog;

    private SearchResultViewHolder mVLSearchResultViewHolder;
    
    private String mVLSearchStr;
    /*
     * SearchResultResourceHolder : list item data resource SearchResultListItemHolder : list item view ListViewAdapter
     * : list adapter
     */
    private SearchResultResourceHolder mVLSearchResultResourceHolder;

    private SearchResultListItemHolder mVLSearchResultListItemHolder;

    private ListViewAdapter mVLListViewAdapter;

    public SearchResultViewListenner(SearchResultDialog mVLSearchResultDialog,
            SearchResultViewHolder mVLSearchResultViewHolder) {
        super();
        this.mVLSearchResultDialog = mVLSearchResultDialog;
        this.mVLSearchResultViewHolder = mVLSearchResultViewHolder;

        mVLSearchResultResourceHolder = new SearchResultResourceHolder();
        mVLSearchResultListItemHolder = new SearchResultListItemHolder(
                R.layout.search_result_list_items);
        mVLListViewAdapter = new ListViewAdapter(mVLSearchResultDialog.getOwnerActivity(),
                mVLSearchResultListItemHolder);
    }

    public void initVar(Bundle args) {
        if (null != args)
            mVLSearchStr = args.getString(SKYPECONSTANT.SKYPESTRING.SEARCHSTRING, "");
        
        mVLSearchResultViewHolder.mVHPopMainRel.setVisibility(View.GONE);
        mVLSearchResultViewHolder.mVHPopMidSendRel.setVisibility(View.GONE);
        mVLSearchResultViewHolder.mVHSearchNameTv.setText("(" + mVLSearchStr + ")");
        mVLSearchResultResourceHolder.setResourceListItem(SktApiActor.getSearchList());
        mVLListViewAdapter.setListItem(mVLSearchResultResourceHolder.getResourceList());
        mVLSearchResultViewHolder.mVHResultLv.setAdapter(mVLListViewAdapter);
        
        //no contact found
        if (null == mVLSearchResultResourceHolder.getResourceList()
                || mVLSearchResultResourceHolder.getResourceList().isEmpty()) {
            Toast.makeText(mVLSearchResultDialog.getOwnerActivity(), R.string.no_match_found,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void setViewListenner() {
        mVLSearchResultViewHolder.mVHAddContactBtn.setOnClickListener(this);
        mVLSearchResultViewHolder.mVHSendBtn.setOnClickListener(this);
        mVLSearchResultViewHolder.mVHCancelBtn.setOnClickListener(this);
        mVLSearchResultViewHolder.mVHPopBackTv.setOnClickListener(this);

        mVLSearchResultViewHolder.mVHBackTv.setOnClickListener(this);

        mVLSearchResultViewHolder.mVHResultLv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                mVLSearchResultViewHolder.mVHPopMainRel.setVisibility(View.VISIBLE);
                mVLSearchResultViewHolder.mVHPopMidAddRel.setVisibility(View.VISIBLE);
                mVLSearchResultViewHolder.mVHPopMidSendRel.setVisibility(View.GONE);
                
                String actName = ((SktBuddy) mVLListViewAdapter.getItem(arg2)).getActName();
                String languabge = CountryInfoHelper.getLanguageNameStrByShortName(
                        mVLSearchResultDialog.getOwnerActivity(),
                        ((SktBuddy) mVLListViewAdapter.getItem(arg2)).getLanguage());
                
                String city = ((SktBuddy) mVLListViewAdapter.getItem(arg2)).getCity();
                String province = ((SktBuddy) mVLListViewAdapter.getItem(arg2)).getProvince();
                String country = ((SktBuddy) mVLListViewAdapter.getItem(arg2)).getCountry();
                
                String location = city;
                if (!location.isEmpty()) {
                    if (!province.isEmpty()) {
                        location = location + ", " + province;
                    }
                } else {
                    location = province;
                }

                if (!location.isEmpty()) {
                    if (!country.isEmpty()) {
                        location = location
                                + ", "
                                + CountryInfoHelper.getCountryNameStrByShortName(
                                        mVLSearchResultDialog.getOwnerActivity(), country);
                    }
                } else {
                    location = CountryInfoHelper.getCountryNameStrByShortName(
                            mVLSearchResultDialog.getOwnerActivity(), country);
                }

                mVLSearchResultViewHolder.mVHPopMainNameTv.setText(actName);
                mVLSearchResultViewHolder.mVHPopMidAddNameTv.setText(actName);
                mVLSearchResultViewHolder.mVHPopMidAddLanTv.setText(languabge);
                mVLSearchResultViewHolder.mVHPopMidAddLocTv.setText(location);
                
                byte[] avatar = ((SktBuddy) mVLListViewAdapter.getItem(arg2)).getAvatar();
                
                if (null != avatar) {
                    mVLSearchResultViewHolder.mVHPopMainHeadIv.setImageBitmap(BitmapFactory
                            .decodeByteArray(avatar, 0, avatar.length));
                }
                
                // 0: can add to; 1: my buddy; 2: my self
                int ret = SktUtils.canAddToContactList(
                        mVLSearchResultViewHolder.mVHPopMidAddNameTv.getText().toString());
                switch(ret){
                    case 0:
                        mVLSearchResultViewHolder.mVHAddContactBtn.setFocusable(true);
                        mVLSearchResultViewHolder.mVHAddContactBtn.setEnabled(true);
                        mVLSearchResultViewHolder.mVHAddContactBtn.requestFocus();
                        mVLSearchResultViewHolder.mVHResultLv.setFocusable(false);
                        mVLSearchResultViewHolder.mVHResultLv.setEnabled(false);
                        mVLSearchResultViewHolder.mVHPopMidAlreadyInTv.setVisibility(View.GONE);
                        break;
                    case 1:
                        mVLSearchResultViewHolder.mVHAddContactBtn.setFocusable(false);
                        mVLSearchResultViewHolder.mVHAddContactBtn.setEnabled(false);
                        mVLSearchResultViewHolder.mVHPopMidAlreadyInTv.setVisibility(View.VISIBLE);
                        mVLSearchResultViewHolder.mVHPopMidAlreadyInTv.setText(R.string.already_in_contact);
                        break;
                    case 2:
                        mVLSearchResultViewHolder.mVHAddContactBtn.setFocusable(false);
                        mVLSearchResultViewHolder.mVHAddContactBtn.setEnabled(false);
                        mVLSearchResultViewHolder.mVHPopMidAlreadyInTv.setVisibility(View.VISIBLE);
                        mVLSearchResultViewHolder.mVHPopMidAlreadyInTv.setText(R.string.myself);
                        break;
                    default:
                        break;
                }
                
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            /* click add will show send and cancle view */
            case R.id.btn_resultlist_item_pop_add:
                mVLSearchResultViewHolder.mVHPopMidAddRel.setVisibility(View.GONE);
                mVLSearchResultViewHolder.mVHPopMidSendRel.setVisibility(View.VISIBLE);
                mVLSearchResultViewHolder.mVHSendBtn.requestFocus();
                mVLSearchResultViewHolder.mVHAddContactBtn.setFocusable(false);
                break;
            case R.id.btn_resultlist_item_pop_send:
                SktApiActor.addContact(mVLSearchResultViewHolder.mVHPopMainNameTv.getText().toString(),
                                mVLSearchResultViewHolder.mVHRequestEt.getText().toString());
                mVLSearchResultViewHolder.mVHResultLv.setFocusable(true);
                mVLSearchResultViewHolder.mVHResultLv.setEnabled(true);
                mVLSearchResultDialog.dismiss();
                break;
            case R.id.tv_search_result_back:
                mVLSearchResultDialog.getOwnerActivity().showDialog(
                        SKYPECONSTANT.USRACCOUNTDIALOG.ADD_SKYPE_CONTACT);
                mVLSearchResultViewHolder.mVHResultLv.setFocusable(true);
                mVLSearchResultViewHolder.mVHResultLv.setEnabled(true);
                mVLSearchResultDialog.dismiss();
                break;
            case R.id.btn_resultlist_item_pop_cancel:
            case R.id.tv_resultlist_item_pop_back:
                mVLSearchResultViewHolder.mVHPopMidSendRel.setVisibility(View.GONE);
                mVLSearchResultViewHolder.mVHPopMainRel.setVisibility(View.GONE);
                mVLSearchResultViewHolder.mVHResultLv.setFocusable(true);
                mVLSearchResultViewHolder.mVHResultLv.setEnabled(true);
                break;
        }

    }

}
