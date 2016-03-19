
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
 * A Activity handle the sign in
 */
public class SigninActivity extends Activity {
    public static final String TAG = "SigninActivity";
    
    private SigninViewHolder mSigninViewHolder;

    private SigninViewListenner mSigninViewListenner;
    
    private Intent mIntent;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        ActivityManager.getInstance().addActivity(this);
        
        SktApiActor.initApi();
        mIntent = getIntent();
        mSigninViewHolder = new SigninViewHolder(this);
        mSigninViewListenner = new SigninViewListenner(this, mSigninViewHolder);

        mSigninViewHolder.findView();
        mSigninViewListenner.setViewListener();
        mSigninViewListenner.initVar();
        
        ShowLoginTip();
    }
    
    public void ShowLoginTip(){
        if (null == mIntent.getStringExtra(SKYPECONSTANT.SKYPESTRING.LOGINTIP))
            return;
//        Toast toast = new Toast(this);
//    
//        LayoutInflater inflater = getLayoutInflater();
//        View layout = inflater.inflate(R.layout.mytoast, null);
//        TextView view  = (TextView) layout.findViewById(R.id.toastTv);
//        view.setText("qwertyu");
//        
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.setView(layout);
//        toast.show();
//        
       Toast.makeText(this, mIntent.getStringExtra(SKYPECONSTANT.SKYPESTRING.LOGINTIP), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                exitApk();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    
	    mSigninViewHolder = null;
	    mSigninViewListenner = null;
	    mIntent = null;
	    ActivityManager.getInstance().pupActivity(this);
	}
    
    public void exitApk(){
        stopService(new Intent(SKYPECONSTANT.SKYPEACTION.SKYPE_SERVICE));
        stopService(new Intent(SKYPECONSTANT.SKYPEACTION.SKYPE_EXCEPTION_SERVICE));
        finish();
        System.exit(0);
    }

}
