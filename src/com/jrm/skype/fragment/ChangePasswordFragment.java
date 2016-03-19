
package com.jrm.skype.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.ui.R;
/**
 * @author andy.liu 
 * a fragment of Options , handle the password change
 */
public class ChangePasswordFragment extends Fragment {
    private View mLayout;

    private ChangePasswordViewHolder mChangePasswordViewHolder;

    private ChangePasswordViewListenner mChangePsaawordViewListenner;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mChangePasswordViewHolder = new ChangePasswordViewHolder(this);
        mChangePsaawordViewListenner = new ChangePasswordViewListenner(this,
                mChangePasswordViewHolder);

        mChangePasswordViewHolder.findView();
        mChangePsaawordViewListenner.setViewListenner();
        getActivity().registerReceiver(mPasswordChangeReceiver, new IntentFilter(SKYPECONSTANT.SKYPEACTION.PASSWORD_CHANGE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.options_changepw_fragment, container, false);
        return mLayout;
    }

    
    @Override
    public void onResume() {
        mChangePsaawordViewListenner.initVar();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mPasswordChangeReceiver);
        mLayout = null;
        mChangePasswordViewHolder = null;
        mChangePsaawordViewListenner = null;
        
        System.gc();
        super.onDestroy();
    }

    public View getLayout() {
        return mLayout;
    }

    private BroadcastReceiver mPasswordChangeReceiver = new BroadcastReceiver (){

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent)
                return;
            mChangePsaawordViewListenner.onPasswordChange(intent.getStringExtra(SKYPECONSTANT.SKYPEACTION.PASSWORD_CHANGE));
        }
             
    };
}
