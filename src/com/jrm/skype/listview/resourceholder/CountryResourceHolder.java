
package com.jrm.skype.listview.resourceholder;

import java.util.ArrayList;
import android.content.Context;
import com.jrm.skype.ui.R;
/**
 * @author andy.liu 
 * the ResourceHolder of CountryInfoList
 */
public class CountryResourceHolder {
    private ArrayList<Object> mlistItem;

    public String[] mCountryNameItems;
    
    public String[] mCountryShortNameItems;

    public String[] mCountryCodeItems;

    public class CountryResource {
        public String mCountryName;
        
        public String mCountryShortName;

        public String mCountryCode;
    }

    public void readCountryInfoResource(Context context) {
        if (mCountryNameItems == null) {
            mCountryNameItems = context.getResources().getStringArray(R.array.countryName);
            mCountryShortNameItems  = context.getResources().getStringArray(R.array.countryShortName);
            mCountryCodeItems = context.getResources().getStringArray(R.array.countryCode);
        }
    }

    public void setListItem() {
        mlistItem = new ArrayList<Object>();
        for (int i = 1; i < mCountryNameItems.length; i++) // need not the first item -select one-
        {
            CountryResource countryResource = new CountryResource();
            countryResource.mCountryName = mCountryNameItems[i];
            countryResource.mCountryShortName = mCountryShortNameItems[i];
            countryResource.mCountryCode = mCountryCodeItems[i];
            mlistItem.add(countryResource);
        }
    }

    public ArrayList<Object> getResourceListItem() {
        setListItem();
        return mlistItem;
    }

}
