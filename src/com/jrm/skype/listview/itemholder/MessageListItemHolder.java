
package com.jrm.skype.listview.itemholder;

import com.jrm.skype.datebase.ChatTableResourceHolder;
import com.jrm.skype.datebase.ChatTableActor.ChatTableInfoHolder;
import android.database.Cursor;
import com.jrm.skype.util.Log;
import android.view.View;
import android.widget.TextView;
import com.jrm.skype.ui.R;
/**
 * @author andy.liu 
 * the ItemHolder of MessageList
 */
public class MessageListItemHolder extends BaseListItemHolder {
    private int mLayout;

    public TextView mDisNameTv;

    public TextView mTimeTv;

    public TextView mStringTv;

    public TextView mDateTv;

    public MessageListItemHolder(int layout) {
        this.mLayout = layout;
    }

    @Override
    public BaseListItemHolder createNewViewHolder() {
        return new MessageListItemHolder(mLayout);
    }

    @Override
    public int getLayoutId() {
        return mLayout;
    }

    @Override
    public void findView(View v) {
        mDisNameTv = (TextView) v.findViewById(R.id.tv_message_name);
        mTimeTv = (TextView) v.findViewById(R.id.tv_message_time);
        mStringTv = (TextView) v.findViewById(R.id.tv_message_content);
        mDateTv = (TextView) v.findViewById(R.id.tv_message_date);
    }

    @Override
    public void setViewResource(Object obj) {
        if (obj instanceof ChatTableResourceHolder) {
            // new message need not date
            mDisNameTv.setText(((ChatTableResourceHolder) obj).getChatDisplayName());
            mTimeTv.setText(((ChatTableResourceHolder) obj).getChatTime());
            mStringTv.setText(((ChatTableResourceHolder) obj).getChatString());
        } else if (obj instanceof Cursor) {
            // message history need date
            mDisNameTv.setText(((Cursor) obj).getString(((Cursor) obj)
                    .getColumnIndex(ChatTableInfoHolder.CHATDISPLAYNAME)));
            mTimeTv.setText(((Cursor) obj).getString(((Cursor) obj)
                    .getColumnIndex(ChatTableInfoHolder.CHATTIME)));
            mStringTv.setText(((Cursor) obj).getString(((Cursor) obj)
                    .getColumnIndex(ChatTableInfoHolder.CHATSTRING)));
            mDateTv.setText(((Cursor) obj).getString(((Cursor) obj)
                    .getColumnIndex(ChatTableInfoHolder.CHATDATE)));
        } else
            Log.w("Message", "set view resource failed!");
    }

}
