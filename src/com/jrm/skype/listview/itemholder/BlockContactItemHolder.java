package com.jrm.skype.listview.itemholder;

import com.jrm.skype.ui.R;

import android.view.View;
import android.widget.TextView;

public class BlockContactItemHolder extends BaseListItemHolder {
    private int mLayout;

    public TextView mSkypeNameTv;
    
    public BlockContactItemHolder(int mLayout) {
        super();
        this.mLayout = mLayout;
    }

    @Override
    public BaseListItemHolder createNewViewHolder() {
        return new BlockContactItemHolder(mLayout);
    }

    @Override
    public int getLayoutId() {
        return mLayout;
    }

    @Override
    public void findView(View v) {
        mSkypeNameTv = (TextView) v.findViewById(R.id.tv_block_contact_name);
    }

    @Override
    public void setViewResource(Object obj) {
        mSkypeNameTv.setText((String)obj);
    }

}
