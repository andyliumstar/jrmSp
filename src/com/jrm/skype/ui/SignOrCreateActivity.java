
package com.jrm.skype.ui;

import com.jrm.skype.exception.ActivityManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author andy.liu 
 * A Activity just for select sign in or create a new account
 */
public class SignOrCreateActivity extends Activity {
    private Button mSigninBtn;

    private Button mCreateBtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signorcreate);
        ActivityManager.getInstance().addActivity(this);

        mSigninBtn = (Button) findViewById(R.id.btn_signOrCr_signin);
        mSigninBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SignOrCreateActivity.this, SigninActivity.class);
                startActivity(intent);
                SignOrCreateActivity.this.finish();
            }
        });

        mCreateBtn = (Button) findViewById(R.id.btn_signOrCr_create);
        mCreateBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SignOrCreateActivity.this, CreateNewAccountActivity.class);
                startActivity(intent);
                SignOrCreateActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSigninBtn = null;
        mCreateBtn = null;
        ActivityManager.getInstance().pupActivity(this);
    }

}
