
package com.jrm.skype.listview.itemholder;

import com.jrm.skype.listview.resourceholder.CountryResourceHolder.CountryResource;
import com.jrm.skype.util.Log;
import android.view.View;
import android.widget.TextView;
import com.jrm.skype.ui.R;
/**
 * @author andy.liu 
 * the ItemHolder of CountryInfoList
 */
public class CountryInfoListItemHolder extends BaseListItemHolder {
    private int mLayout;

    public TextView mNameTv;

    public TextView mCodeTv;

    public CountryInfoListItemHolder(int layout) {
        this.mLayout = layout;
    }

    @Override
    public BaseListItemHolder createNewViewHolder() {
        return new CountryInfoListItemHolder(mLayout);
    }

    @Override
    public int getLayoutId() {
        return mLayout;
    }

    @Override
    public void findView(View v) {
        mNameTv = (TextView) v.findViewById(R.id.tv_countryinfo_listitem_name);
        mCodeTv = (TextView) v.findViewById(R.id.tv_countryinfo_lsititem_code);
    }

    @Override
    public void setViewResource(Object obj) {
        if (obj instanceof CountryResource) {
            mNameTv.setText(((CountryResource) obj).mCountryName);
            mCodeTv.setText(((CountryResource) obj).mCountryCode);
        } else
            Log.w("CountryInfo", "set view resource failed!");
    }

}
