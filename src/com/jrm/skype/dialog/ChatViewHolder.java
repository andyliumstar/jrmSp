
package com.jrm.skype.dialog;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jrm.skype.dialog.ChatDialog;
import com.jrm.skype.ui.R;

/**
 * @author andy.liu 
 * the ViewHolder of ChatDialog
 */
public class ChatViewHolder {
    public ChatDialog mVHChatDialog;
    /*
     * chat view
     */
    public ImageView mVHHeadIv;

    public ImageView mVHStatusIv;
    
    public ImageView mVHWaitIv;

    public TextView mVHDisplayNameTv;

    public ListView mVHMessageLv;

    public EditText mVHMessageEt;

    public Button mVHEnterBtn;

    public Button mVHCloseBtn;

    public Button mVHHistoryBtn;

    /*
     * chat history view
     */
    public LinearLayout mVHChatHistoryLil;

    public ListView mVHChatHistoryLv;

    public Button mVHLastPageBtn;

    public Button mVHNextPageBtn;
    
    public Button mVHDeleteAllBtn;

    public TextView mVHPageIndexTv;

    public ChatViewHolder(ChatDialog mVHChatDialog) {
        super();
        this.mVHChatDialog = mVHChatDialog;
    }

    public void findView() {
        mVHHeadIv = (ImageView) mVHChatDialog.getLayout().findViewById(R.id.iv_chat_head);
        mVHStatusIv = (ImageView) mVHChatDialog.getLayout().findViewById(R.id.iv_chat_status);
        mVHWaitIv = (ImageView) mVHChatDialog.getLayout().findViewById(R.id.iv_chat_wait);
        
        mVHDisplayNameTv = (TextView) mVHChatDialog.getLayout().findViewById(R.id.tv_chat_name);

        mVHMessageLv = (ListView) mVHChatDialog.getLayout().findViewById(R.id.lv_chat_message);
        mVHMessageEt = (EditText) mVHChatDialog.getLayout().findViewById(R.id.et_chat_message);

        mVHEnterBtn = (Button) mVHChatDialog.getLayout().findViewById(R.id.btn_chat_enter);
        mVHCloseBtn = (Button) mVHChatDialog.getLayout().findViewById(R.id.btn_chat_close);
        mVHHistoryBtn = (Button) mVHChatDialog.getLayout().findViewById(R.id.btn_chat_history);

        mVHChatHistoryLil = (LinearLayout) mVHChatDialog.getLayout().findViewById(
                R.id.lil_chat_history);
        mVHChatHistoryLv = (ListView) mVHChatDialog.getLayout().findViewById(R.id.lv_chat_history);
        mVHLastPageBtn = (Button) mVHChatDialog.getLayout().findViewById(R.id.btn_chat_history_last);
        mVHNextPageBtn = (Button) mVHChatDialog.getLayout().findViewById(R.id.btn_chat_history_next);
        mVHPageIndexTv = (TextView) mVHChatDialog.getLayout().findViewById(
                R.id.tv_chat_history_pageindex);
        mVHDeleteAllBtn = (Button) mVHChatDialog.getLayout().findViewById(R.id.btn_chat_history_delete);
    }

}
