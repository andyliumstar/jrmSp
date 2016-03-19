
package com.jrm.skype.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jrm.skype.ui.R;
/**
 * @author andy.liu 
 * a fragment of Options , handle the call set
 */
public class CallSetFragment extends Fragment {
    private View mLayout;

    private CallSetViewHolder mCallSetViewHolder;

    private CallSetViewListenner mCallSetViewListenner;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCallSetViewHolder = new CallSetViewHolder(this);
        mCallSetViewListenner = new CallSetViewListenner(this, mCallSetViewHolder);

        mCallSetViewHolder.findView();
        mCallSetViewListenner.setViewListenner();
        mCallSetViewListenner.initVar(); 
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.options_callset_fragment, container, false);
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

    public CallSetViewHolder getViewHolder() {
        return mCallSetViewHolder;
    }
    
    public CallSetViewListenner getViewListenner(){
        return mCallSetViewListenner;
    }

}
