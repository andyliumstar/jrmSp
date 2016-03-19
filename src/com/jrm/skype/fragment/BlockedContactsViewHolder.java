
package com.jrm.skype.fragment;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jrm.skype.fragment.BlockedContactsFragment;
import com.jrm.skype.ui.R;

/**
 * @author andy.liu 
 * the ViewHolder of BlockedContactsFragment
 */
public class BlockedContactsViewHolder {
    public BlockedContactsFragment mVHBlockedContactsFragment;

    public Button mVHBlockBtn;

    public Button mVHUnBlockBtn;

    public TextView mVHContactsTv;
    
    public TextView mVHBlockContactSelectedTv;

    public ListView mVHBlockedContactsLv;

    public BlockedContactsViewHolder(BlockedContactsFragment mVHBlockedContactsFragment) {
        super();
        this.mVHBlockedContactsFragment = mVHBlockedContactsFragment;
    }

    public void findView() {
        mVHBlockBtn = (Button) mVHBlockedContactsFragment.getLayout().findViewById(
                R.id.btn_blockedcontacts_fragment_block);
        mVHUnBlockBtn = (Button) mVHBlockedContactsFragment.getLayout().findViewById(
                R.id.btn_blockedcontacts_fragment_unblock);
        mVHContactsTv = (TextView) mVHBlockedContactsFragment.getLayout().findViewById(
                R.id.tv_blockedcontacts_fragment_unblock);
        mVHBlockContactSelectedTv = (TextView) mVHBlockedContactsFragment.getLayout().findViewById(
                R.id.tv_blockedcontacts_fragment_blockedpeople_selected);
        mVHBlockedContactsLv = (ListView) mVHBlockedContactsFragment.getLayout().findViewById(
                R.id.lv_blockedcontacts_fragment_blockedpeople);
    }

}
