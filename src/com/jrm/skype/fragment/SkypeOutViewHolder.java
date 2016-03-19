
package com.jrm.skype.fragment;

import android.widget.Button;
import android.widget.TextView;

import com.jrm.skype.fragment.SkypeOutFragment;
import com.jrm.skype.ui.R;
import com.jrm.skype.view.SKEditText;

/**
 * @author andy.liu 
 * the ViewHolder of SkypeOutFragment
 */
public class SkypeOutViewHolder {
    public SkypeOutFragment mVHSkypeOutFragment;

    public TextView mVHCountryCodeTv;

    public TextView mVHCountryCodeSelectTv;

    public SKEditText mVHPhoneNumberEt;

    public Button mVHVoiceCallBtn;


    public SkypeOutViewHolder(SkypeOutFragment mVHSkypeOutFragment) {
        super();
        this.mVHSkypeOutFragment = mVHSkypeOutFragment;
    }

    public void findView() {
        mVHCountryCodeTv = (TextView) mVHSkypeOutFragment.getLayout().findViewById(
                R.id.tv_skypeout_fragment_inputcountrycode);
        mVHCountryCodeSelectTv = (TextView) mVHSkypeOutFragment.getLayout().findViewById(
                R.id.tv_skypeout_fragment_countrycode_selected);
        mVHPhoneNumberEt = (SKEditText) mVHSkypeOutFragment.getLayout().findViewById(
                R.id.et_skypeout_fragment_input_phonenumber);
        mVHVoiceCallBtn = (Button) mVHSkypeOutFragment.getLayout().findViewById(
                R.id.btn_skypeout_fragment_voicecall);
    }

}
