
package com.jrm.skype.fragment;

import com.jrm.skype.api.SktUtils;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.fragment.SkypeOutFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Toast;
import com.jrm.skype.ui.R;
import com.jrm.skype.util.SktApiActor;
/**
 * @author andy.liu 
 * the ViewListenner of SkypeOutFragment
 */
public class SkypeOutViewListenner implements OnClickListener{

    private SkypeOutViewHolder mVLSkypeOutViewHolder;

    private SkypeOutFragment mVLSkypeOutFragment;

    private boolean mVHCodeSelected;


    public SkypeOutViewListenner(SkypeOutViewHolder mVLSkypeOutViewHolder,
            SkypeOutFragment mVLSkypeOutFragment) {
        this.mVLSkypeOutViewHolder = mVLSkypeOutViewHolder;
        this.mVLSkypeOutFragment = mVLSkypeOutFragment;
    }

    public void initVar() {
        mVHCodeSelected = false;
        mVLSkypeOutViewHolder.mVHVoiceCallBtn.setEnabled(false);
        mVLSkypeOutViewHolder.mVHVoiceCallBtn.setFocusable(false);
    }

    public void setViewListenner() {
        mVLSkypeOutViewHolder.mVHVoiceCallBtn.setOnClickListener(this);
        mVLSkypeOutViewHolder.mVHCountryCodeTv.setOnClickListener(this);

        mVLSkypeOutViewHolder.mVHPhoneNumberEt.setOnKeyListener(new OnKeyListener() {
            
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                            && !mVLSkypeOutViewHolder.mVHVoiceCallBtn.isEnabled())
                        return true;
                }
                return false;
            }
        });
        
        mVLSkypeOutViewHolder.mVHPhoneNumberEt.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (SktApiActor.checkPSTNNumberBool(mVLSkypeOutViewHolder.mVHPhoneNumberEt
                        .getText().toString(), mVLSkypeOutViewHolder.mVHCountryCodeSelectTv
                        .getText().toString())) {
                    updateVoiceCall(mVHCodeSelected);
                } else {
                    updateVoiceCall(false);
                }
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_skypeout_fragment_voicecall:
                skypeOutCall();
                break;
            case R.id.tv_skypeout_fragment_inputcountrycode:
                mVLSkypeOutFragment.getActivity().showDialog(SKYPECONSTANT.USRACCOUNTDIALOG.COUNTRYCODE_CALL,null);
                break;
            default:
                break;
        }
    }

    /**
     *  fill the TextView to show what selected; keep focus on CountryCodeTv.
     * @param countryName the selected country name
     * @param countryCode the selected country code
     */
    public void setCallCountryInfo(String countryName ,String countryCode)
    {
        String code = countryName + "          " + countryCode;

        mVLSkypeOutViewHolder.mVHCountryCodeSelectTv.setText(countryCode);
        mVLSkypeOutViewHolder.mVHCountryCodeTv.setText(code);
        mVLSkypeOutViewHolder.mVHCountryCodeTv.requestFocus();

        mVHCodeSelected = true;
        updateVoiceCall(SktApiActor.checkPSTNNumberBool(
                mVLSkypeOutViewHolder.mVHPhoneNumberEt.getText().toString(),
                mVLSkypeOutViewHolder.mVHCountryCodeSelectTv.getText().toString()));
    }
  

    public void updateVoiceCall(boolean enable) {
        if (enable) {
            mVLSkypeOutViewHolder.mVHVoiceCallBtn.setEnabled(true);
            mVLSkypeOutViewHolder.mVHVoiceCallBtn.setFocusable(true);
        } else {
            mVLSkypeOutViewHolder.mVHVoiceCallBtn.setEnabled(false);
            mVLSkypeOutViewHolder.mVHVoiceCallBtn.setFocusable(false);
        }
    }
    
    private void skypeOutCall(){
        if (!SktUtils.isNetworkAvailable(mVLSkypeOutFragment.getActivity())) {
            Toast.makeText(mVLSkypeOutFragment.getActivity(), R.string.internet_connect_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        
        Bundle bl = new Bundle();
        bl.putString(SKYPECONSTANT.SKYPESTRING.PHONENUMBER, mVLSkypeOutViewHolder.mVHCountryCodeSelectTv.getText().toString()
                + mVLSkypeOutViewHolder.mVHPhoneNumberEt.getText().toString());
        mVLSkypeOutFragment.getActivity().showDialog(SKYPECONSTANT.USRACCOUNTDIALOG.SKYPEOUTCALLOUT, bl);
    }

}
