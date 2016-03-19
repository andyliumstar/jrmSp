
package com.jrm.skype.ui;

import java.util.Timer;
import java.util.TimerTask;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.exception.ActivityManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author andy.liu 
 * the pupnotice of voice mail or contact invitation
 */
public class NotifyActivity extends Activity {
    private int mNotifyType;
    
    private final static int NOTIFY_SHOW_TIME = 4000;
    
    private ViewHolder mViewHolder;

    private Timer mTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        ActivityManager.getInstance().addActivity(this);
        
        this.getWindow().setGravity(Gravity.RIGHT | Gravity.BOTTOM);

        mViewHolder = new ViewHolder();
        mViewHolder.findView();
        mViewHolder.setViewListenner();
    }

    @Override
    protected void onResume() {
        mNotifyType =  getIntent().getIntExtra(SKYPECONSTANT.SKYPESTRING.NOTIFY,SKYPECONSTANT.USRACCOUNTNOTIFY.NONE);
        
        if (mNotifyType == SKYPECONSTANT.USRACCOUNTNOTIFY.INVITATION)
            mViewHolder.setText(getResources().getString(R.string.noti_contact_invitation));
        else if (mNotifyType == SKYPECONSTANT.USRACCOUNTNOTIFY.VOICEMAIL)
            mViewHolder.setText(getResources().getString(R.string.noti_vm));
        
        if (mTimer != null ){
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            
            @Override
            public void run() {
                NotifyActivity.this.finish(); 
            }
        }, NOTIFY_SHOW_TIME);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        mViewHolder = null;
        mTimer = null;
        ActivityManager.getInstance().pupActivity(this);
    }

    public class ViewHolder implements View.OnClickListener {
        private TextView vhTextView;

        private Button vhButton;

        public void findView() {
            vhTextView = (TextView) findViewById(R.id.tv_noti_tip);
            vhButton = (Button) findViewById(R.id.btn_noti_ok);
        }

        public void setViewListenner() {
            vhButton.setOnClickListener(this);
        }

        public void setText(String text) {
            vhTextView.setText(text);
        }

        @Override
        public void onClick(View v) {
            if (null != mTimer)
                mTimer.cancel();
            NotifyActivity.this.finish();
        }
    }

}
