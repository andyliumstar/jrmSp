
package com.jrm.skype.fragment;

import java.util.ArrayList;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.fragment.CallSetFragment;
import com.jrm.skype.ui.OptionsActivity;
import com.jrm.skype.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.TextView;
import com.jrm.skype.ui.R;
import com.jrm.skype.util.SktApiActor;
/**
 * @author andy.liu 
 * the ViewListenner of CallSetFragment
 */
public class CallSetViewListenner implements OnClickListener,
        OnItemSelectedListener{
    private CallSetFragment mVLCallSetFragment;

    private CallSetViewHolder mVLCallSetViewHolder;
    
    private ArrayList<String> mCallForwardList;
    
    private String mFirstCallStr;
    
    private String mSecondCallStr;
    
    private String mThirdCallStr;
    
    private int mSelect;
    
    public CallSetViewListenner(CallSetFragment mVLCallSetFragment,
            CallSetViewHolder mVLCallSetViewHolder) {
        super();
        this.mVLCallSetFragment = mVLCallSetFragment;
        this.mVLCallSetViewHolder = mVLCallSetViewHolder;
    }

    public void setViewListenner() {
        mVLCallSetViewHolder.mVHForwardCallSp.setOnItemSelectedListener(this);
        mVLCallSetViewHolder.mVHVoiceMailSp.setOnItemSelectedListener(this);

        mVLCallSetViewHolder.mVHFirstCallCodeTv.setOnClickListener(this);
        mVLCallSetViewHolder.mVHSecondCallCodeTv.setOnClickListener(this);
        mVLCallSetViewHolder.mVHThirdCallCodeTv.setOnClickListener(this);
        
        mVLCallSetViewHolder.mVHFirstCallSh.setOnClickListener(this);
        mVLCallSetViewHolder.mVHSecondCallSh.setOnClickListener(this);
        mVLCallSetViewHolder.mVHThirdCallSh.setOnClickListener(this);
    }
    
    public void initVar() {
        mSelect = SktApiActor.getCallForwarding();// 0----off,1-----on
        Log.v("CallSetViewListenner", "SktApiActor.getCallForwarding()----->"+mSelect);
        mVLCallSetViewHolder.mVHForwardCallSp.setSelection(mSelect);
        mVLCallSetViewHolder.mVHVoiceMailSp
                .setSelection(SktApiActor.getAutoforwardingToVoicemail());

        if (!SktApiActor.haveVoicemailCapabilityOwn()) {
            mVLCallSetViewHolder.mVHVoiceMailSp.setEnabled(false);
            mVLCallSetViewHolder.mVHVoiceMailSp.setFocusable(false);
        }
        
//        if(SktApiActor.getBalance()<0.1f){
//            mVLCallSetViewHolder.mVHForwardCallSp.setEnabled(false);
//            mVLCallSetViewHolder.mVHForwardCallSp.setFocusable(false);
//            
//            mVLCallSetViewHolder.mVHFirstCallSh.setEnabled(false);
//            mVLCallSetViewHolder.mVHFirstCallCodeTv.setEnabled(false);
//            mVLCallSetViewHolder.mVHFirstCallEt.setEnabled(false);
//            
//            mVLCallSetViewHolder.mVHFirstCallSh.setFocusable(false);
//            mVLCallSetViewHolder.mVHFirstCallCodeTv.setFocusable(false);
//            mVLCallSetViewHolder.mVHFirstCallEt.setFocusable(false);
//            
//            mVLCallSetViewHolder.mVHSecondCallSh.setEnabled(false);
//            mVLCallSetViewHolder.mVHSecondCallCodeTv.setEnabled(false);
//            mVLCallSetViewHolder.mVHSecondCallEt.setEnabled(false);
//            
//            mVLCallSetViewHolder.mVHSecondCallSh.setFocusable(false);
//            mVLCallSetViewHolder.mVHSecondCallCodeTv.setFocusable(false);
//            mVLCallSetViewHolder.mVHSecondCallEt.setFocusable(false);
//            
//            mVLCallSetViewHolder.mVHThirdCallSh.setEnabled(false);
//            mVLCallSetViewHolder.mVHThirdCallCodeTv.setEnabled(false);
//            mVLCallSetViewHolder.mVHThirdCallEt.setEnabled(false);
//            
//            mVLCallSetViewHolder.mVHThirdCallSh.setFocusable(false);
//            mVLCallSetViewHolder.mVHThirdCallCodeTv.setFocusable(false);
//            mVLCallSetViewHolder.mVHThirdCallEt.setFocusable(false);
//        }
    }
  

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        switch (arg0.getId()) {
            case R.id.sp_callset_fragment_forward:
                // 0----off,1-----on
                if (0 == arg2) {
                    updateCallFarwardViews(false);
                } else {
                    updateCallFarwardViews(true);
                }
                SktApiActor.setCallForwarding(arg2);
                mVLCallSetViewHolder.mVHForwardSecondsEt.setText(SktApiActor.getTimeoutOfCallStr());
                break;
            case R.id.sp_callset_fragment_send_unanswer_vm:
                // 0----off,1-----on
                SktApiActor.setAutoforwardingToVoicemail(arg2);
                break;
            default:
                break;
        }
       
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_callset_fragment_firstcall_code:
                ((OptionsActivity) mVLCallSetFragment.getActivity()).mPhoneFocus = SKYPECONSTANT.OPTIONSPHONEFOCUS.FARWARDONE;
                mVLCallSetFragment.getActivity().showDialog(
                        SKYPECONSTANT.OPTIONSDIALOG.CALLSETTINGS);
                break;
            case R.id.tv_callset_fragment_secondcall_code:
                ((OptionsActivity) mVLCallSetFragment.getActivity()).mPhoneFocus = SKYPECONSTANT.OPTIONSPHONEFOCUS.FARWARDTWO;
                mVLCallSetFragment.getActivity().showDialog(
                        SKYPECONSTANT.OPTIONSDIALOG.CALLSETTINGS);
                break;
            case R.id.tv_callset_fragment_thirdcall_code:
                ((OptionsActivity) mVLCallSetFragment.getActivity()).mPhoneFocus = SKYPECONSTANT.OPTIONSPHONEFOCUS.FARWARDTHREE;
                mVLCallSetFragment.getActivity().showDialog(
                        SKYPECONSTANT.OPTIONSDIALOG.CALLSETTINGS);
                break;
            case R.id.tv_callset_fragment_firstcall:
                mVLCallSetViewHolder.mVHFirstCallSh.setVisibility(View.GONE);
                mVLCallSetViewHolder.mVHFirstCallCodeTv.setVisibility(View.VISIBLE);
                mVLCallSetViewHolder.mVHFirstCallEt.setVisibility(View.VISIBLE);
                mVLCallSetViewHolder.mVHFirstCallCodeTv.requestFocus();
                mVLCallSetViewHolder.mVHFirstCallCodeTv.requestFocusFromTouch();
                break;
            case R.id.tv_callset_fragment_secondcall:
                mVLCallSetViewHolder.mVHSecondCallSh.setVisibility(View.GONE);
                mVLCallSetViewHolder.mVHSecondCallCodeTv.setVisibility(View.VISIBLE);
                mVLCallSetViewHolder.mVHSecondCallEt.setVisibility(View.VISIBLE);
                mVLCallSetViewHolder.mVHSecondCallCodeTv.requestFocus();
                mVLCallSetViewHolder.mVHSecondCallCodeTv.requestFocusFromTouch();
                break;
            case R.id.tv_callset_fragment_thirdcall:
                mVLCallSetViewHolder.mVHThirdCallSh.setVisibility(View.GONE);
                mVLCallSetViewHolder.mVHThirdCallCodeTv.setVisibility(View.VISIBLE);
                mVLCallSetViewHolder.mVHThirdCallEt.setVisibility(View.VISIBLE);
                mVLCallSetViewHolder.mVHThirdCallCodeTv.requestFocus();
                mVLCallSetViewHolder.mVHThirdCallCodeTv.requestFocusFromTouch();
                break;
            default:
                break;
        }
    }
    
    protected void updateCallFarwardViews(boolean enable){
        mVLCallSetViewHolder.mVHFirstCallSh.setVisibility(View.GONE);
        mVLCallSetViewHolder.mVHSecondCallSh.setVisibility(View.GONE);
        mVLCallSetViewHolder.mVHThirdCallSh.setVisibility(View.GONE);
        mVLCallSetViewHolder.mVHFirstCallCodeTv.setText("");
        mVLCallSetViewHolder.mVHSecondCallCodeTv.setText("");
        mVLCallSetViewHolder.mVHThirdCallCodeTv.setText("");
        mVLCallSetViewHolder.mVHFirstCallEt.setText("");
        mVLCallSetViewHolder.mVHSecondCallEt.setText("");
        mVLCallSetViewHolder.mVHThirdCallEt.setText("");
        
        if (enable)// on
        {
            mVLCallSetViewHolder.mVHFirstCallCodeTv.setEnabled(true);
            mVLCallSetViewHolder.mVHFirstCallCodeTv.setFocusable(true);

            mVLCallSetViewHolder.mVHSecondCallCodeTv.setEnabled(true);
            mVLCallSetViewHolder.mVHSecondCallCodeTv.setFocusable(true);

            mVLCallSetViewHolder.mVHThirdCallCodeTv.setEnabled(true);
            mVLCallSetViewHolder.mVHThirdCallCodeTv.setFocusable(true);

            mVLCallSetViewHolder.mVHFirstCallEt.setEnabled(true);
            mVLCallSetViewHolder.mVHFirstCallEt.setFocusable(true);
            mVLCallSetViewHolder.mVHFirstCallEt.setFocusableInTouchMode(true);

            mVLCallSetViewHolder.mVHSecondCallEt.setEnabled(true);
            mVLCallSetViewHolder.mVHSecondCallEt.setFocusable(true);
            mVLCallSetViewHolder.mVHSecondCallEt.setFocusableInTouchMode(true);

            mVLCallSetViewHolder.mVHThirdCallEt.setEnabled(true);
            mVLCallSetViewHolder.mVHThirdCallEt.setFocusable(true);
            mVLCallSetViewHolder.mVHThirdCallEt.setFocusableInTouchMode(true);
            
            mVLCallSetViewHolder.mVHForwardSecondsEt.setEnabled(true);
            mVLCallSetViewHolder.mVHForwardSecondsEt.setFocusable(true);
            mVLCallSetViewHolder.mVHForwardSecondsEt.setFocusableInTouchMode(true);
            
            updateCallForwarding();
            
        } else// off
        {
            mVLCallSetViewHolder.mVHFirstCallCodeTv.setVisibility(View.VISIBLE);
            mVLCallSetViewHolder.mVHFirstCallCodeTv.setEnabled(false);
            mVLCallSetViewHolder.mVHFirstCallCodeTv.setFocusable(false);
            
            mVLCallSetViewHolder.mVHSecondCallCodeTv.setVisibility(View.VISIBLE);
            mVLCallSetViewHolder.mVHSecondCallCodeTv.setEnabled(false);
            mVLCallSetViewHolder.mVHSecondCallCodeTv.setFocusable(false);

            mVLCallSetViewHolder.mVHThirdCallCodeTv.setVisibility(View.VISIBLE);
            mVLCallSetViewHolder.mVHThirdCallCodeTv.setEnabled(false);
            mVLCallSetViewHolder.mVHThirdCallCodeTv.setFocusable(false);

            mVLCallSetViewHolder.mVHFirstCallEt.setVisibility(View.VISIBLE);
            mVLCallSetViewHolder.mVHFirstCallEt.setEnabled(false);
            mVLCallSetViewHolder.mVHFirstCallEt.setFocusable(false);

            mVLCallSetViewHolder.mVHSecondCallEt.setVisibility(View.VISIBLE);
            mVLCallSetViewHolder.mVHSecondCallEt.setEnabled(false);
            mVLCallSetViewHolder.mVHSecondCallEt.setFocusable(false);

            mVLCallSetViewHolder.mVHThirdCallEt.setVisibility(View.VISIBLE);
            mVLCallSetViewHolder.mVHThirdCallEt.setEnabled(false);
            mVLCallSetViewHolder.mVHThirdCallEt.setFocusable(false);
            
            mVLCallSetViewHolder.mVHForwardSecondsEt.setEnabled(false);
            mVLCallSetViewHolder.mVHForwardSecondsEt.setFocusable(false);
            
            if (null == mCallForwardList){
                mCallForwardList = new ArrayList<String>();
            }
            mCallForwardList.clear();
            SktApiActor.setCallForwardList(mCallForwardList);
        }
    }

    public void updateCallForwarding(){
        mCallForwardList = SktApiActor.getCallForwardList();
        mFirstCallStr = null;
        mSecondCallStr = null;
        mThirdCallStr = null;
        
        if (null != mCallForwardList){
            int i = 0;
            while (i<mCallForwardList.size()){
                switch(i){
                    case 0:
                        mFirstCallStr = mCallForwardList.get(i);
                        break;
                    case 1:
                        mSecondCallStr = mCallForwardList.get(i);
                        break;
                    case 2:
                        mThirdCallStr = mCallForwardList.get(i);
                        break;
                }
                ++i;
            }
        }
        
        if (null == mFirstCallStr ){
            mVLCallSetViewHolder.mVHFirstCallSh.setVisibility(View.GONE);
            mVLCallSetViewHolder.mVHFirstCallCodeTv.setVisibility(View.VISIBLE);
            mVLCallSetViewHolder.mVHFirstCallEt.setVisibility(View.VISIBLE);
        }
        else{
            mVLCallSetViewHolder.mVHFirstCallSh.setText(mFirstCallStr);
            mVLCallSetViewHolder.mVHFirstCallSh.setVisibility(View.VISIBLE);
            mVLCallSetViewHolder.mVHFirstCallCodeTv.setVisibility(View.GONE);
            mVLCallSetViewHolder.mVHFirstCallEt.setVisibility(View.GONE);
        }
        
        if (null == mSecondCallStr ){
            mVLCallSetViewHolder.mVHSecondCallSh.setVisibility(View.GONE);
            mVLCallSetViewHolder.mVHSecondCallCodeTv.setVisibility(View.VISIBLE);
            mVLCallSetViewHolder.mVHSecondCallEt.setVisibility(View.VISIBLE);
        }
        else{
            mVLCallSetViewHolder.mVHSecondCallSh.setText(mSecondCallStr);
            mVLCallSetViewHolder.mVHSecondCallSh.setVisibility(View.VISIBLE);
            mVLCallSetViewHolder.mVHSecondCallCodeTv.setVisibility(View.GONE);
            mVLCallSetViewHolder.mVHSecondCallEt.setVisibility(View.GONE);
        }
        
        if (null == mThirdCallStr) {
            mVLCallSetViewHolder.mVHThirdCallSh.setVisibility(View.GONE);
            mVLCallSetViewHolder.mVHThirdCallCodeTv.setVisibility(View.VISIBLE);
            mVLCallSetViewHolder.mVHThirdCallEt.setVisibility(View.VISIBLE);
        } else {
            mVLCallSetViewHolder.mVHThirdCallSh.setText(mThirdCallStr);
            mVLCallSetViewHolder.mVHThirdCallSh.setVisibility(View.VISIBLE);
            mVLCallSetViewHolder.mVHThirdCallCodeTv.setVisibility(View.GONE);
            mVLCallSetViewHolder.mVHThirdCallEt.setVisibility(View.GONE);
        }
    }
    
    public void saveSettings() {
        if (null == mCallForwardList){
            mCallForwardList = new ArrayList<String>();
        }
        mCallForwardList.clear();
        saveCallForwarding(mVLCallSetViewHolder.mVHFirstCallSh,mVLCallSetViewHolder.mVHFirstCallCodeTv,mVLCallSetViewHolder.mVHFirstCallEt);
        
        saveCallForwarding(mVLCallSetViewHolder.mVHSecondCallSh,mVLCallSetViewHolder.mVHSecondCallCodeTv,mVLCallSetViewHolder.mVHSecondCallEt);
        
        saveCallForwarding(mVLCallSetViewHolder.mVHThirdCallSh,mVLCallSetViewHolder.mVHThirdCallCodeTv,mVLCallSetViewHolder.mVHThirdCallEt);
        
        SktApiActor.setCallForwardList(mCallForwardList);
        
        int seconds = 15;
        try{
            seconds = Integer.parseInt(mVLCallSetViewHolder.mVHForwardSecondsEt.getText().toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        SktApiActor.setTimeoutOfCalling(seconds);
    }

    protected void saveCallForwarding(TextView oldCallForwarding, TextView codeTv, EditText numberEt){
        if (View.VISIBLE == oldCallForwarding.getVisibility())
            mCallForwardList.add(oldCallForwarding.getText().toString());
        else{
            String code =  codeTv.getText().toString();
            String number = numberEt.getText().toString().replaceAll(" ", "");
            
            if(code.isEmpty() || number.isEmpty()){
                return;
            }
            if (SktApiActor.checkPSTNNumberBool(number,code)){
                if (hasNumberAlready(code + number)) {
                    return;
                }
                
                mCallForwardList.add(code+number);
                codeTv.setVisibility(View.GONE);
                numberEt.setVisibility(View.GONE);
                oldCallForwarding.setVisibility(View.VISIBLE);
                oldCallForwarding.setText(code+number);
                oldCallForwarding.requestFocus();
                oldCallForwarding.requestFocusFromTouch();
            }
        }
    }
    
    protected boolean hasNumberAlready(String phnumber) {
        for (int i = 0; i < mCallForwardList.size(); ++i) {
            if (phnumber.equals(mCallForwardList.get(i))) {
                return true;
            }
        }
        return false;
    }
}
