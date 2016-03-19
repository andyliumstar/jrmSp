package com.jrm.skype.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jrm.skype.exception.ActivityManager;
import com.jrm.skype.ui.R;

public class PictureActivity extends Activity {
    private PictureActivityViewHolder mPictureActivityViewHolder;
    
    private PictureActivityViewListenner mPictureActivityViewListenner;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture);
        ActivityManager.getInstance().addActivity(this);
        
        Intent intent = getIntent();
        mPictureActivityViewHolder = new PictureActivityViewHolder(this);
        mPictureActivityViewListenner = new PictureActivityViewListenner(this, mPictureActivityViewHolder);
        
        mPictureActivityViewHolder.findView();
        mPictureActivityViewListenner.setViewListenner();
        mPictureActivityViewListenner.initVar(intent);
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        
        if (hasFocus)
        {
            int rw = mPictureActivityViewHolder.mVHPictureRel.getWidth();
            int rh= mPictureActivityViewHolder.mVHPictureRel.getHeight();
            
            int iw = mPictureActivityViewHolder.mVHPictureIv.getWidth();
            int ih = mPictureActivityViewHolder.mVHPictureIv.getHeight();
            
            mPictureActivityViewListenner.initViewFrame(rw, rh, iw, ih);
        }
    }

	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    
		mPictureActivityViewHolder = null; 
		mPictureActivityViewListenner = null;
		ActivityManager.getInstance().pupActivity(this);
	}

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch ( keyCode ) {
//        case  KeyEvent.KEYCODE_DPAD_DOWN:
//            mPictureActivityViewListenner.down();
//            break;
//        case  KeyEvent.KEYCODE_DPAD_LEFT:
//            mPictureActivityViewListenner.left();
//            break;
//        case  KeyEvent.KEYCODE_DPAD_UP:
//            mPictureActivityViewListenner.up();
//            break;
//        case  KeyEvent.KEYCODE_DPAD_RIGHT:
//            mPictureActivityViewListenner.right();
//            break;
//        case  KeyEvent.KEYCODE_DPAD_CENTER:
//            mPictureActivityViewListenner.save();
//            break;
//    }
//            
//        return true;
//    }
    
    
    
}
