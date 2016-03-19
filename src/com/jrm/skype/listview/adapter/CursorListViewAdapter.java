
package com.jrm.skype.listview.adapter;

import com.jrm.skype.listview.itemholder.BaseListItemHolder;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * @author andy.liu 
 * A CursorListViewAdapter of a list view in using of database cause the Adapter should adapt for all
 *         the list view so it has a ListItemHolder for different list view
 */
public class CursorListViewAdapter extends CursorAdapter {
    private BaseListItemHolder mListItemHolder;

    private LayoutInflater mInflater;

    public CursorListViewAdapter(Context context, Cursor c, BaseListItemHolder mListItemHolder) {
        super(context, c);
        mInflater = LayoutInflater.from(context);
        this.mListItemHolder = mListItemHolder;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(mListItemHolder.getLayoutId(), null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        if (view.getTag() == null) {
            mListItemHolder = mListItemHolder.createNewViewHolder();
            mListItemHolder.findView(view);
            view.setTag(mListItemHolder);
        } else{
            mListItemHolder = (BaseListItemHolder) view.getTag();
        }

        // cursor have moved to the current position
        if (cursor != null){
            mListItemHolder.setViewResource(cursor);
        }
    }

}
