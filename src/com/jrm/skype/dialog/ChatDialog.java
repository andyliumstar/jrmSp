
package com.jrm.skype.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

/**
 * @author andy.liu 
 * a Dialog to handle the send message part
 */
public class ChatDialog extends MainDialog {
    private ChatViewHolder mChatViewHolder;

    private ChatViewListenner mChatViewListenner;

    public ChatDialog(Context context, int theme) {
        super(context, theme);

        mChatViewHolder = new ChatViewHolder(this);
        mChatViewListenner = new ChatViewListenner(this, mChatViewHolder, 30);
    }

    @Override
    public void findView() {
        mChatViewHolder.findView();
    }

    @Override
    public void setViewListenner() {
        mChatViewListenner.setViewListenner();
    }

    @Override
    public void initVar(Bundle args) {
        mChatViewListenner.initVar(args);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mChatViewListenner.closeCursor();
    }
    
    public ChatViewListenner getViewListenner(){
        return mChatViewListenner;
    }
}
