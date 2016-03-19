
package com.jrm.skype.listview.resourceholder;

import java.util.ArrayList;
import com.jrm.skype.api.SktContact.SktBuddy;
/**
 * @author andy.liu 
 * the ResourceHolder of ContactList
 */
public class ContactResourceHolder {
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

    public ArrayList<Object> getResourceListItem() { 
        return mlistItem;
    }

}
