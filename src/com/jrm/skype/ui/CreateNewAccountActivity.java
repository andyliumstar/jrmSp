
package com.jrm.skype.ui;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.exception.ActivityManager;
import com.jrm.skype.util.SktApiActor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * @author andy.liu 
 * A Activity handle the creating a new skype account
 */

public class CreateNewAccountActivity extends Activity {
    private CreateNewAccountViewListenner mCreateNewAccountViewListenner;

    private CreateNewAccountViewHolder mCreateNewAccountViewHolder;
    
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createaccount);
        ActivityManager.getInstance().addActivity(this);
        
        SktApiActor.initApi();

        mCreateNewAccountViewHolder = new CreateNewAccountViewHolder(this);
        mCreateNewAccountViewListenner = new CreateNewAccountViewListenner(this,
                mCreateNewAccountViewHolder);

        mCreateNewAccountViewHolder.findView();
        mCreateNewAccountViewListenner.setViewListenner();
        mCreateNewAccountViewListenner.initVar();
        
        mIntent = new Intent();
        
        showCreateTip();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                back();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    public void back(){
        mIntent.setClass(CreateNewAccountActivity.this, SigninActivity.class);
        startActivity(mIntent);
        finish();
    }

    public void showCreateTip(){
        if (null == getIntent().getStringExtra(SKYPECONSTANT.SKYPESTRING.CREATETIP))
            return;
        
        Toast.makeText(this, getIntent().getStringExtra(SKYPECONSTANT.SKYPESTRING.CREATETIP), Toast.LENGTH_SHORT).show();
    }

	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    
		mCreateNewAccountViewListenner = null;
		mCreateNewAccountViewHolder = null;
		mIntent = null;
		ActivityManager.getInstance().pupActivity(this);
	}
}
