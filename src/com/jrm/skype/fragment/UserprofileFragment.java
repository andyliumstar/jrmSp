
package com.jrm.skype.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jrm.skype.ui.R;
/**
 * @author andy.liu 
 * a fragment of Options , handle the info of the user
 */
public class UserprofileFragment extends Fragment {
    private View mLayout;

    private UsrProfileViewHolder mUsrProfileViewHolder;

    private UsrProfileViewListenner mUsrProfileViewListenner;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mUsrProfileViewHolder = new UsrProfileViewHolder(this);
        mUsrProfileViewListenner = new UsrProfileViewListenner(this, mUsrProfileViewHolder);

        mUsrProfileViewHolder.findView();
        mUsrProfileViewListenner.setViewListenner();
        mUsrProfileViewListenner.initVar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.options_userprofile_fragment, container, false);
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

    public UsrProfileViewHolder getViewHolder() {
        return mUsrProfileViewHolder;
    }
    
    public UsrProfileViewListenner getViewListenner() {
        return mUsrProfileViewListenner;
    }
    
    public void pageUp()  {
        mUsrProfileViewHolder.mVHPageOneRel.setVisibility(View.VISIBLE);
        mUsrProfileViewHolder.mVHPageTwoRel.setVisibility(View.GONE);
        mUsrProfileViewHolder.mVHPageTv.setText("(1/2)");
        mUsrProfileViewHolder.mVHFullNameEt.requestFocus();
    }
    
    public void pageDown() {
        mUsrProfileViewHolder.mVHPageOneRel.setVisibility(View.GONE);
        mUsrProfileViewHolder.mVHPageTwoRel.setVisibility(View.VISIBLE);
        mUsrProfileViewHolder.mVHPageTv.setText("(2/2)");
        mUsrProfileViewHolder.mVHCountrySp.requestFocus();
    }
    
    public void setFocus(){
        if (View.VISIBLE == mUsrProfileViewHolder.mVHPageOneRel.getVisibility()){
            mUsrProfileViewHolder.mVHFullNameEt.requestFocus();
        }else{
            mUsrProfileViewHolder.mVHCountrySp.requestFocus();
        }
    }
}
