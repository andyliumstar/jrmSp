
package com.jrm.skype.dialog;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.ui.R;
import com.jrm.skype.ui.UsrAccountActivity;
import com.jrm.skype.util.SktApiActor;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author andy.liu 
 * a Dialog to handle the sign out, back to sign in activity
 */
public class SignoutDialog extends MainDialog {
    private Button mOkBtn;

    private Button mCancelBtn;

    public SignoutDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void findView() {
        mOkBtn = (Button) getLayout().findViewById(R.id.btn_signout_ok);
        mCancelBtn = (Button) getLayout().findViewById(R.id.btn_signout_cancel);
    }

    @Override
    public void setViewListenner() {

        mOkBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SktApiActor.logout(false);
                ((UsrAccountActivity) getOwnerActivity()).signingOut();
                SignoutDialog.this.dismiss();
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SignoutDialog.this.dismiss();
                getOwnerActivity().showDialog(SKYPECONSTANT.USRACCOUNTDIALOG.USR_STATUS);
            }
        });

    }

    @Override
    public void initVar(Bundle args) {
        
    }

}
