
package com.jrm.skype.listview.itemholder;

import com.jrm.skype.api.SktContact.SktBuddy;
import android.graphics.BitmapFactory;
import com.jrm.skype.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jrm.skype.ui.R;
/**
 * @author andy.liu 
 * the ItemHolder of ContactList
 */
public class ContactListItemHolder extends BaseListItemHolder {
    private int mLayout;

    public ImageView mHeadIv;

    public ImageView mStateIv;

    public TextView mDisPlayNameTv;
    
    public String mSkypeNameStr;

    public TextView mSigTv;

    public ContactListItemHolder(int layout) {
        this.mLayout = layout;
    }

    @Override
    public BaseListItemHolder createNewViewHolder() {
        return new ContactListItemHolder(mLayout);
    }

    @Override
    public int getLayoutId() {
        return mLayout;
    }

    @Override
    public void findView(View v) {
        mHeadIv = (ImageView) v.findViewById(R.id.iv_contact_listitem_head);
        mStateIv = (ImageView) v.findViewById(R.id.iv_contact_listitem_state);
        mDisPlayNameTv = (TextView) v.findViewById(R.id.tv_contact_listitem_name);
        mSigTv = (TextView) v.findViewById(R.id.tv_contact_listitem_sig);
    }

    @Override
    public void setViewResource(final Object obj) {
        
         if (obj instanceof SktBuddy) {
            if (null != ((SktBuddy) obj).getAvatar()&& ((SktBuddy) obj).getAvatar().length > 0) {
                mHeadIv.setImageBitmap(BitmapFactory.decodeByteArray(((SktBuddy) obj).getAvatar(),
                        0, ((SktBuddy) obj).getAvatar().length));
            } else {
                mHeadIv.setImageResource(R.drawable.skype_img_contact_list_buddy_unknown);
            }

            int id;
            switch (((SktBuddy) obj).getAvalability()){
                case ONLINE:
                    id = R.drawable.presenceonline_32x32;
                    break;
                case AWAY:
                    id = R.drawable.presenceaway_32x32;
                    break;
                case DO_NOT_DISTURB:
                    id = R.drawable.presencedonotdisturb_32x32;
                    break;
                case BLOCKED:
                    id = R.drawable.presenceblocked_32x32;
                    break;
                case BLOCKED_SKYPEOUT:
                    id = R.drawable.presenceblocked_32x32;
                    mHeadIv.setImageResource(R.drawable.skype_img_contact_list_buddy_phone);
                    break;
                case SKYPEOUT:
                    id = R.drawable.presencephone_32x32;
                    mHeadIv.setImageResource(R.drawable.skype_img_contact_list_buddy_phone);
                    break;
                case OFFLINE_BUT_CF_ABLE:
                    id = R.drawable.presencecallforward_32x32;
                    break;
                case OFFLINE_BUT_VM_ABLE:
                case OFFLINE:
                    id = R.drawable.presenceoffline_32x32;
                    break;
                default:
                    id = R.drawable.presenceunknown_32x32;
                    break;
                    
            }
            mStateIv.setBackgroundResource(id);
            mDisPlayNameTv.setText(((SktBuddy) obj).getDisplayName());
            mSigTv.setText(((SktBuddy) obj).getMoodText());
            mSkypeNameStr =  ((SktBuddy) obj).getActName();
        } else
            Log.w("ContactListItemHolder", "set Contact resource failed!");
    }

}
