
package com.jrm.skype.fragment;

import com.jrm.skype.ui.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author andy.liu 
 * a fragment of UserAccount , handle the settings of user
 */
public class OptionsFragment extends Fragment {
    private View mLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.usraccount_options_fragment, container, false);
        return mLayout;
    }

}
