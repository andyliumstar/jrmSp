
package com.jrm.skype.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jrm.skype.ui.R;
/**
 * @author andy.liu 
 * a fragment of Options , handle the block contacts
 */
public class BlockedContactsFragment extends Fragment {
    private View mLayout;

    private BlockedContactsViewHolder mBlockedContactsViewHolder;

    private BlockedContactsViewListenner mBlockedContactsViewListenner;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBlockedContactsViewHolder = new BlockedContactsViewHolder(this);
        mBlockedContactsViewListenner = new BlockedContactsViewListenner(this,
                mBlockedContactsViewHolder);

        mBlockedContactsViewHolder.findView();
        mBlockedContactsViewListenner.initVar();
        mBlockedContactsViewListenner.setViewListenner();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.options_blockedcontact_fragment, container, false);
        return mLayout;
    }

    @Override
    public void onDestroy() {
        System.gc();
        super.onDestroy();
    }

    
    public View getLayout() {
        return mLayout;
    }
    
    public BlockedContactsViewListenner getViewListenner(){
        return mBlockedContactsViewListenner;
    }

}
