package com.jrm.skype.listview.resourceholder;

import java.util.ArrayList;

public class BlockContactResourceHolder {
    private ArrayList<Object> mlistItem;

    public void setResourceList(ArrayList<String> list) {
        if (null == mlistItem) {
            mlistItem = new ArrayList<Object>();
        } else {
            mlistItem.clear();
        }

        if (null != list) {
            for (String name : list) {
                mlistItem.add(name);
            }
        }
    }
    
    public ArrayList<Object> getResourceList(){
        return mlistItem;
    }

}
