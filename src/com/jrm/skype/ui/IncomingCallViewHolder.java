
package com.jrm.skype.ui;

import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jrm.skype.ui.IncomingCallActivity;

/**
 * @author andy.liu 
 * the ViewHolder of IncomingCallActivity
 */
public class IncomingCallViewHolder {
    public IncomingCallActivity mVHIncomingCallActivity;

    public TextView mVHContactNameTv;
    
    public TextView mVHNotifyTv;
    
    public RelativeLayout mVHBtnsRel;

    public Button mVHVideoBtn;

    public Button mVHVoiceBtn;

    public Button mVHDeclineBtn;

    public IncomingCallViewHolder(IncomingCallActivity mVHIncomingCallActivity) {
        super();
        this.mVHIncomingCallActivity = mVHIncomingCallActivity;
    }

    public void findView() {
        mVHContactNameTv = (TextView) mVHIncomingCallActivity.findViewById(R.id.tv_incomingcall_name);
        mVHNotifyTv = (TextView) mVHIncomingCallActivity.findViewById(R.id.tv_incomingcall_notify);
        
        mVHBtnsRel = (RelativeLayout) mVHIncomingCallActivity.findViewById(R.id.rel_incomingcall_btns);
        mVHVideoBtn = (Button) mVHIncomingCallActivity.findViewById(R.id.btn_incomingcall_video);
        mVHVoiceBtn = (Button) mVHIncomingCallActivity.findViewById(R.id.btn_incomingcall_voice);
        mVHDeclineBtn = (Button) mVHIncomingCallActivity.findViewById(R.id.btn_incomingcall_decline);
    }

}
