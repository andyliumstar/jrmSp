
package com.jrm.skype.listview.resourceholder;

import java.util.ArrayList;

import com.jrm.skype.api.SktContact.SktAuthItem;

/**
 * @author andy.liu 
 * the ResourceHolder of InvitationList
 */
public class InvitationResourceHolder {
    private ArrayList<Object> mlistItem;

    public void setListItem(ArrayList<SktAuthItem> arrayList) {
        if (null == mlistItem) {
            mlistItem = new ArrayList<Object>();
        } else {
            mlistItem.clear();
        }

        if (null != arrayList) {
            for (SktAuthItem authItem : arrayList) {
                mlistItem.add(authItem);
            }
        }
    }

    public ArrayList<Object> getResourceListItem() {
        return mlistItem;
    }
}
