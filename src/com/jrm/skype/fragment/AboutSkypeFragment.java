
package com.jrm.skype.fragment;

import com.jrm.skype.ui.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author andy.liu a fragment of Options , show the info of skype
 */
public class AboutSkypeFragment extends Fragment {
    private View mLayout;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.options_aboutskype_fragment, container, false);
        return mLayout;
    }

}
