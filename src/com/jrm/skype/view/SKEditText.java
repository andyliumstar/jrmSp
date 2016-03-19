
package com.jrm.skype.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

/**
 * @author andy.liu 
 * A edittext that can handle the back key message when edit
 */
public class SKEditText extends EditText {
    private OnEditBackListenner mEditBackListenner;

    public SKEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnEditBackListenner(OnEditBackListenner mEditBackListenner) {
        this.mEditBackListenner = mEditBackListenner;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        // when the press back key without open the ime,this function could also execute,this is a little bug.
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction()== KeyEvent.ACTION_DOWN )
            if (mEditBackListenner != null)
                mEditBackListenner.onEditBack(this);
        
        return super.onKeyPreIme(keyCode, event);
    }
    
    /**
     * handle the back key message when edit
     * @param v a view
     */
    public interface OnEditBackListenner {
        public void onEditBack(View v);
    }
 
}
