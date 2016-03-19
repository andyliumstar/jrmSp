
package com.jrm.skype.listview.itemholder;

import com.jrm.skype.util.Log;
import android.view.View;
import android.widget.TextView;
import com.jrm.skype.api.SktContact.SktAuthItem;
import com.jrm.skype.ui.R;
/**
 * @author andy.liu 
 * the ItemHolder of InvitationList
 */
public class InvitationListItemHolder extends BaseListItemHolder {
    private int mLayout;

    public TextView mTextView;
    
    public String mText;

    public InvitationListItemHolder(int layout) {
        this.mLayout = layout;
    }

    @Override
    public BaseListItemHolder createNewViewHolder() {
        return new InvitationListItemHolder(mLayout);
    }

    @Override
    public int getLayoutId() {
        return mLayout;
    }

    @Override
    public void setViewResource(Object obj) {
        if (obj instanceof SktAuthItem){
            mTextView.setText(((SktAuthItem) obj).getActName());
            mText = ((SktAuthItem) obj).getText();
        }
        else
            Log.w("Invitation", "set view resource failed!");
    }

    @Override
    public void findView(View v) {
        mTextView = (TextView) v.findViewById(R.id.tv_search_result_list_fullname);
    }

}
