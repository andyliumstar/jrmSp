
package com.jrm.skype.fragment;

import android.widget.Spinner;
import android.widget.TextView;
import com.jrm.skype.fragment.CallSetFragment;
import com.jrm.skype.ui.R;
import com.jrm.skype.view.SKEditText;

/**
 * @author andy.liu 
 * the ViewHolder of CallSetFragment
 */
public class CallSetViewHolder {
    public CallSetFragment mVHCallSetFragment;

    public Spinner mVHForwardCallSp;

    public TextView mVHFirstCallCodeTv;

    public TextView mVHSecondCallCodeTv;

    public TextView mVHThirdCallCodeTv;
    
    public TextView mVHFirstCallSh;

    public TextView mVHSecondCallSh;

    public TextView mVHThirdCallSh;

    public Spinner mVHVoiceMailSp;

    public SKEditText mVHFirstCallEt;

    public SKEditText mVHSecondCallEt;

    public SKEditText mVHThirdCallEt;

    public SKEditText mVHForwardSecondsEt;

    public CallSetViewHolder(CallSetFragment mVHCallSetFragment) {
        super();
        this.mVHCallSetFragment = mVHCallSetFragment;
    }

    public void findView() {
        mVHForwardCallSp = (Spinner) mVHCallSetFragment.getLayout().findViewById(
                R.id.sp_callset_fragment_forward);
        mVHFirstCallCodeTv = (TextView) mVHCallSetFragment.getLayout().findViewById(
                R.id.tv_callset_fragment_firstcall_code);
        mVHSecondCallCodeTv = (TextView) mVHCallSetFragment.getLayout().findViewById(
                R.id.tv_callset_fragment_secondcall_code);
        mVHThirdCallCodeTv = (TextView) mVHCallSetFragment.getLayout().findViewById(
                R.id.tv_callset_fragment_thirdcall_code);
        mVHVoiceMailSp = (Spinner) mVHCallSetFragment.getLayout().findViewById(
                R.id.sp_callset_fragment_send_unanswer_vm);
        
        mVHFirstCallSh = (TextView) mVHCallSetFragment.getLayout().findViewById(
                R.id.tv_callset_fragment_firstcall);
        mVHSecondCallSh = (TextView) mVHCallSetFragment.getLayout().findViewById(
                R.id.tv_callset_fragment_secondcall);
        mVHThirdCallSh = (TextView) mVHCallSetFragment.getLayout().findViewById(
                R.id.tv_callset_fragment_thirdcall);

        mVHFirstCallEt = (SKEditText) mVHCallSetFragment.getLayout().findViewById(
                R.id.et_callset_fragment_firstcall);
        mVHSecondCallEt = (SKEditText) mVHCallSetFragment.getLayout().findViewById(
                R.id.et_callset_fragment_secondcall);
        mVHThirdCallEt = (SKEditText) mVHCallSetFragment.getLayout().findViewById(
                R.id.et_callset_fragment_thirdcall);
        mVHForwardSecondsEt = (SKEditText) mVHCallSetFragment.getLayout().findViewById(
                R.id.et_callset_fragment_withinsecond);
    }

}
