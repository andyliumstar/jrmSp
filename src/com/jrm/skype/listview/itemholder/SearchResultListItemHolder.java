
package com.jrm.skype.listview.itemholder;

import com.jrm.skype.util.Log;
import android.view.View;
import android.widget.TextView;
import com.jrm.skype.api.SktContact.SktBuddy;
import com.jrm.skype.ui.R;
import com.jrm.skype.util.CountryInfoHelper;
/**
 * @author andy.liu 
 * the ItemHolder of SearchResultList
 */
public class SearchResultListItemHolder extends BaseListItemHolder {
    private int mLayout;

    public TextView mFullNameTv;

    public TextView mSkypeAccountTv;

    public TextView mCountryTv;

    public SearchResultListItemHolder(int layout) {
        this.mLayout = layout;
    }

    @Override
    public BaseListItemHolder createNewViewHolder() {
        return new SearchResultListItemHolder(mLayout);
    }

    @Override
    public int getLayoutId() {
        return mLayout;
    }

    @Override
    public void findView(View v) {
        mFullNameTv = (TextView) v.findViewById(R.id.tv_search_result_list_fullname);
        mSkypeAccountTv = (TextView) v.findViewById(R.id.tv_search_result_list_skypeaccount);
        mCountryTv = (TextView) v.findViewById(R.id.tv_search_result_list_country);
    }

    @Override
    public void setViewResource(Object obj) {
        if (obj instanceof SktBuddy) {
            mFullNameTv.setText(((SktBuddy) obj).getFullName());
            mSkypeAccountTv.setText(((SktBuddy) obj).getActName());
            mCountryTv.setText(CountryInfoHelper.getCountryNameStrByShortName(
                    mCountryTv.getContext(), ((SktBuddy) obj).getCountry()));
        } else {
            Log.w("SearchResult", "setViewResource failed!");
        }
    }

}
