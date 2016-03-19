
package com.jrm.skype.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jrm.skype.ui.R;
/**
 * @author andy.liu 
 * a fragment of UserAccount , handle the skype call
 */
public class SkypeOutFragment extends Fragment {

    private View mLayout;

    private SkypeOutViewHolder mSkypeOutViewHolder;

    private SkypeOutViewListenner mSkypeOutViewListenner;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSkypeOutViewHolder = new SkypeOutViewHolder(this);
        mSkypeOutViewListenner = new SkypeOutViewListenner(mSkypeOutViewHolder, this);
        mSkypeOutViewHolder.findView();
        mSkypeOutViewListenner.setViewListenner();
        mSkypeOutViewListenner.initVar();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.usraccount_skypeout_fragment, container, false);
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

    public SkypeOutViewHolder getViewHolder() {
        return mSkypeOutViewHolder;
    }

    public SkypeOutViewListenner getViewListenner() {
        return mSkypeOutViewListenner;
    }
    
}
