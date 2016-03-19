
package com.jrm.skype.ui;

import com.jrm.skype.exception.ActivityManager;
import com.jrm.skype.util.SkypePrefrences;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author andy.liu WelcomeActivity
 */
public class WelcomeActivity extends Activity {
    private Button mStartBtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        ActivityManager.getInstance().addActivity(this);

        mStartBtn = (Button) findViewById(R.id.btn_welcome_start);

        mStartBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, SignOrCreateActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        });
        /*
         * record this sign in ,and change the ui flow next time
         */
        SkypePrefrences.setFirstStart(this);
    }

    @Override
    protected void onDestroy() {
        mStartBtn = null;
        super.onDestroy();
        ActivityManager.getInstance().pupActivity(this);
    }

}
