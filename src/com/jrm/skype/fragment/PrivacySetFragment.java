
package com.jrm.skype.fragment;

import com.jrm.skype.ui.R;
import com.jrm.skype.util.SktApiActor;
import com.skype.api.Account.SKYPECALLPOLICY;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

/**
 * @author andy.liu 
 * a fragment of Options , handle the call and video setting
 */
public class PrivacySetFragment extends Fragment implements OnItemSelectedListener{
    private View mLayout;
    
    private Spinner mCallPolicySp;
    
    private Spinner mVideoPolicySp;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        mCallPolicySp = (Spinner)mLayout.findViewById(R.id.sp_privacyset_fragment_allowcalls);
        mVideoPolicySp = (Spinner)mLayout.findViewById(R.id.sp_privacyset_fragment_autoreceivevideo);

        if (SKYPECALLPOLICY.EVERYONE_CAN_CALL == SktApiActor.getSkypeCallPolicy()) {
            mCallPolicySp.setSelection(0);// anyone
        } else {
            mCallPolicySp.setSelection(1);// contactlist
        }
            
        mVideoPolicySp.setSelection(SktApiActor.getVideoRecvPolicy());
        
        mCallPolicySp.setOnItemSelectedListener(this);
        mVideoPolicySp.setOnItemSelectedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.options_privacyset_fragment, container, false);
        return mLayout;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.sp_privacyset_fragment_allowcalls:
                if (0 == position)
                    SktApiActor.setSkypeCallPolicy(SKYPECALLPOLICY.EVERYONE_CAN_CALL);
                else
                    SktApiActor.setSkypeCallPolicy(SKYPECALLPOLICY.BUDDIES_OR_AUTHORIZED_CAN_CALL);
                break;
            case R.id.sp_privacyset_fragment_autoreceivevideo:
                //0:anyone,1:contactlist,2:none
                SktApiActor.setVideoRecvPolicy(position); 
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        
    }
    

    @Override
    public void onDestroy() {
        System.gc();
        super.onDestroy();
    }

}
