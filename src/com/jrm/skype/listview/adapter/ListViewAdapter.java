
package com.jrm.skype.listview.adapter;

import java.util.ArrayList;
import com.jrm.skype.listview.itemholder.BaseListItemHolder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author andy.liu 
 * we use one adapter for all the list view,so we should have different list view itemholder
 */
public class ListViewAdapter extends BaseAdapter {
    /*
     * mListItemResource : the resource of listview item mListItemHolder: the item of listview
     */
    private ArrayList<Object> mListItemResource;

    private LayoutInflater mInflater;

    private BaseListItemHolder mListItemHolder;

    private int mSelected;

    public ListViewAdapter(Context context, BaseListItemHolder mListItemHolder) {
        if(null == context || null == mListItemHolder){
            return;
        }
        mInflater = LayoutInflater.from(context);
        this.mListItemHolder = mListItemHolder;
    }

    public void setListItem(ArrayList<Object> listItem) {
        if (listItem != mListItemResource && null != mListItemResource) {
            mListItemResource.clear();
        }
        mListItemResource = listItem;
    }

    public void deleteListItem(int pos) {
        if (mListItemResource != null) {
            if (pos >= 0 && pos < mListItemResource.size()) {
                mListItemResource.remove(pos);
            }
        }

    }

    public void addListItem(Object obj) {
        if (mListItemResource == null) {
            mListItemResource = new ArrayList<Object>();
        }
        mListItemResource.add(obj);
    }

    public void setSelected(int pos) {
        mSelected = pos;
    }

    public int getSelected() {
        return mSelected;
    }

    @Override
    public int getCount() {

        if (mListItemResource != null) {
            return mListItemResource.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {

        if (mListItemResource != null && position < mListItemResource.size()) {
            return mListItemResource.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mInflater){
            return null;
        }

        if (convertView == null) {
            /*
             * the mListItemHolder's first instance just using for createNewViewHolder(); findView are the abstract
             * functions ,so it can adapt for different listview
             */
            mListItemHolder = mListItemHolder.createNewViewHolder();
            convertView = mInflater.inflate(mListItemHolder.getLayoutId(), null);
            mListItemHolder.findView(convertView);
            convertView.setTag(mListItemHolder);
        } else {
            mListItemHolder = (BaseListItemHolder) convertView.getTag();
        }

        if (mListItemResource != null && position < mListItemResource.size()) {
            mListItemHolder.setViewResource(mListItemResource.get(position));
        }

        return convertView;
    }

}
