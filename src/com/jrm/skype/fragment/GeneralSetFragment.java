
package com.jrm.skype.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jrm.skype.ui.R;
/**
 * @author andy.liu 
 * a fragment of Options , handle the history save and other  settings
 */

public class GeneralSetFragment extends Fragment {
    private View mLayout;

    private GeneralSetViewHolder mGeneralSetViewHolder;

    private GeneralSetViewListenner mGeneralSetViewListenner;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mGeneralSetViewHolder = new GeneralSetViewHolder(this);
        mGeneralSetViewListenner = new GeneralSetViewListenner(mGeneralSetViewHolder, this);

        mGeneralSetViewHolder.findView();
        mGeneralSetViewListenner.setViewListenner();
        mGeneralSetViewListenner.initVar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.options_generalset_fragment, container, false);
        return mLayout;
    }


    @Override
    public void onDestroy() {
        System.gc();
        super.onDestroy();
    }
    
    public View GetLayout() {
        return mLayout;
    }

}
