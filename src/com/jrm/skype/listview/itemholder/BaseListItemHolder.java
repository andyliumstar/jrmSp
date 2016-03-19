
package com.jrm.skype.listview.itemholder;

import android.view.View;

/**
 * @author andy.liu 
 * the base item holder for all the listview
 */
public abstract class BaseListItemHolder {
    /**
     * create a new class to return , for list adapter
     * every item should has its item holder
     * @return the new class
     */
    public abstract BaseListItemHolder createNewViewHolder();
    /**
     * get the list item's layout
     * @return the id of the layout
     */
    public abstract int getLayoutId();
    /**
     * for listadapter's getView()
     * @param v the layout inflated
     */
    public abstract void findView(View v);
    /**
     * set the resource for the item
     * @param obj the item resource holder
     */
    public abstract void setViewResource(Object obj);

}
