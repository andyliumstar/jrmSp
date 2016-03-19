package com.jrm.skype.dialog;

import com.jrm.skype.listview.adapter.ListViewAdapter;
import com.jrm.skype.listview.itemholder.BlockContactItemHolder;
import com.jrm.skype.listview.resourceholder.BlockContactResourceHolder;
import com.jrm.skype.ui.R;
import com.jrm.skype.util.SktApiActor;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class BlockListDialog extends MainDialog {
    public static String mContactSelected = null;

    private ListView mContactsLs;

    private ListViewAdapter mContactsAdapter;

    private BlockContactResourceHolder mContactsResourceHolder;

    private BlockContactItemHolder mContactsItemHolder;

    public BlockListDialog(Context context, int theme) {
        super(context, theme);
        mContactsResourceHolder = new BlockContactResourceHolder();
        mContactsItemHolder = new BlockContactItemHolder(R.layout.block_contact_item);
    }

    @Override
    public void findView() {
        mContactsLs = (ListView) getLayout().findViewById(R.id.listview);
    }

    @Override
    public void setViewListenner() {
        mContactsLs.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mContactSelected = (String) mContactsAdapter.getItem(position);
                BlockListDialog.this.dismiss();
            }
        });
    }

    @Override
    public void initVar(Bundle args) {
        mContactsResourceHolder.setResourceList(SktApiActor.getUnBlockedContactList());
        if (null == mContactsAdapter) {
            mContactsAdapter = new ListViewAdapter(getOwnerActivity(), mContactsItemHolder);
            mContactsAdapter.setListItem(mContactsResourceHolder.getResourceList());
            mContactsLs.setAdapter(mContactsAdapter);
        } else {
            mContactsAdapter.setListItem(mContactsResourceHolder.getResourceList());
            mContactsAdapter.notifyDataSetChanged();
        }

    }

}
