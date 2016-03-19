
package com.jrm.skype.listview.resourceholder;

import java.util.ArrayList;

import com.jrm.skype.api.SktContact.SktBuddy;

/**
 * @author andy.liu 
 * the ResourceHolder of SearchResultList
 */
public class SearchResultResourceHolder {
    private ArrayList<Object> mlistItem;

    public void setResourceListItem(ArrayList<SktBuddy> buddies) {
        if (null == mlistItem) {
            mlistItem = new ArrayList<Object>();
        } else {
            mlistItem.clear();
        }

        if (null != buddies) {
            for (SktBuddy buddy : buddies) {
                mlistItem.add(buddy);
            }
        }
    }

    public ArrayList<Object> getResourceList() {
        return mlistItem;
    }

}
