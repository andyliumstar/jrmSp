
package com.jrm.skype.ui;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.jrm.skype.fragment.ContactListFragment;
import com.jrm.skype.fragment.HistoryListFragment;
import com.jrm.skype.fragment.OptionsFragment;
import com.jrm.skype.fragment.SkypeOutFragment;
import com.jrm.skype.ui.UsrAccountActivity;

/**
 * @author andy.liu 
 * the ViewHolder of UsrAccountActivity
 */
public class UsrAccountViewHolder {
    public UsrAccountActivity mVHUsrAccountActivity;

    /* user infos */
    public ImageView mVHUsrheadIv;

    public ImageView mVHUsrstatusIv;
    
    public TextView mVHUsrNameTv;
    
    public TextView mVHUsrSigTv;
    
    public TextView mVHCreditTv;

    /* main four functions */
    public ImageView mVHContactIv;

    public ImageView mVHHistoryIv;

    public ImageView mVHSkypeOutIv;

    public ImageView mVHOptionIv;

    /* four fragments */
    public ContactListFragment mVHContactListFragment;

    public HistoryListFragment mVHHistoryListFragment;

    public SkypeOutFragment mVHSkypeOutFragment;

    public OptionsFragment mVHOptionsFragment;
    
    public ProgressBar mVHProgressBar;

    /* the bottom functions */
    public TextView mVHExitTv;

    public TextView mVHAddContactTv;

    public TextView mVHStatusTv;

    public TextView mVHRequestTv;

    public UsrAccountViewHolder(UsrAccountActivity mVHUsrAccountActivity) {
        this.mVHUsrAccountActivity = mVHUsrAccountActivity;

        mVHContactListFragment = new ContactListFragment();
        mVHHistoryListFragment = new HistoryListFragment();
        mVHSkypeOutFragment = new SkypeOutFragment();
        mVHOptionsFragment = new OptionsFragment();
    }

    public void findView() {
        mVHUsrheadIv = (ImageView) mVHUsrAccountActivity.findViewById(R.id.iv_usrAc_usrhead);
        mVHUsrstatusIv = (ImageView) mVHUsrAccountActivity.findViewById(R.id.iv_usrAc_usrstatus);
        mVHUsrNameTv = (TextView) mVHUsrAccountActivity.findViewById(R.id.tv_usrAc_usrname);
        mVHCreditTv = (TextView) mVHUsrAccountActivity.findViewById(R.id.tv_usrAc_usrcredit);
        mVHUsrSigTv = (TextView) mVHUsrAccountActivity.findViewById(R.id.tv_usrAc_usrsig);

        mVHContactIv = (ImageView) mVHUsrAccountActivity.findViewById(R.id.iv_usrAc_contactlist);
        mVHHistoryIv = (ImageView) mVHUsrAccountActivity.findViewById(R.id.iv_usrAc_historylist);
        mVHSkypeOutIv = (ImageView) mVHUsrAccountActivity.findViewById(R.id.iv_usrAc_skypeout);
        mVHOptionIv = (ImageView) mVHUsrAccountActivity.findViewById(R.id.iv_usrAc_options);
        
        mVHProgressBar = (ProgressBar) mVHUsrAccountActivity.findViewById(R.id.pb_usrAc);

        mVHExitTv = (TextView) mVHUsrAccountActivity.findViewById(R.id.tv_usrAc_exit);
        mVHAddContactTv = (TextView) mVHUsrAccountActivity.findViewById(R.id.tv_usrAc_addcontact);
        mVHStatusTv = (TextView) mVHUsrAccountActivity.findViewById(R.id.tv_usrAc_status);
        mVHRequestTv = (TextView) mVHUsrAccountActivity.findViewById(R.id.tv_usrAc_request);
    }
}
