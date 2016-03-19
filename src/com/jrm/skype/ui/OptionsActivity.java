
package com.jrm.skype.ui;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.dialog.BlockListDialog;
import com.jrm.skype.dialog.ChangePictureDialog;
import com.jrm.skype.dialog.CountrycodeDialog;
import com.jrm.skype.dialog.MainDialog;
import com.jrm.skype.exception.ActivityManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;

/**
 * @author andy.liu 
 * A Activity content 8 fragments for settings
 */
public class OptionsActivity extends Activity {
    private OptionsActivityViewHolder mOptionsViewHolder;

    private OptionsActivityViewListenner mOptionsViewListenner;

    private FragmentTransaction mFragmentT;

    private MainDialog mDialog;
    
    private String mFilePath;

    public int mPhoneFocus;   //the phone number set will use it to record the focus 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        ActivityManager.getInstance().addActivity(this);
  
        mOptionsViewHolder = new OptionsActivityViewHolder(this);
        mOptionsViewListenner = new OptionsActivityViewListenner(mOptionsViewHolder, this);

        mOptionsViewHolder.findView();
        mOptionsViewListenner.setViewListenner();
      
        mFragmentT = getFragmentManager().beginTransaction();
        mFragmentT.replace(R.id.rel_options_right_fragment, mOptionsViewHolder.mVHUserprofileFragment);
        mFragmentT.commit();
    }

    @Override
    protected void onResume() {
        if (null != mOptionsViewHolder.mVHUserprofileFragment){
            mOptionsViewHolder.mVHUserprofileFragment.getViewListenner().updateAvater();
        }
        super.onResume();
    }


    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        switch (id) {
            case SKYPECONSTANT.OPTIONSDIALOG.USERPROFILE:
                mDialog = new CountrycodeDialog(this, R.style.dialog_light);
                mDialog.setDialogContentView(R.layout.listview_dialog);
                mDialog.findView();
                mDialog.setViewListenner();
                mDialog.setDialogSizeF(0.375, 0.273);
                mDialog.setDialogPositionF(0.277, 0.278);
                return mDialog;
            case SKYPECONSTANT.OPTIONSDIALOG.CALLSETTINGS:
                mDialog = new CountrycodeDialog(this, R.style.dialog_light);
                mDialog.setDialogContentView(R.layout.listview_dialog);
                mDialog.findView();
                mDialog.setViewListenner();
                mDialog.setDialogSizeF(0.375, 0.273);
                mDialog.setDialogPositionF(0.090, -0.055);
                return mDialog;
            case SKYPECONSTANT.OPTIONSDIALOG.CHANGE_PICTURE:
                mDialog = new ChangePictureDialog(this, R.style.dialog_dim);
                mDialog.setDialogContentView(R.layout.change_picture_dialog);
                mDialog.findView();
                mDialog.setViewListenner();
                mDialog.setDialogSizeF(0.400, 0.273);
                return mDialog;
            case SKYPECONSTANT.OPTIONSDIALOG.UNBLOCK_CONTACTS:
                mDialog = new BlockListDialog(this, R.style.dialog_dim);
                mDialog.setDialogContentView(R.layout.listview_dialog);
                mDialog.findView();
                mDialog.setViewListenner();
                mDialog.setDialogSizeF(0.555, 0.354);
                mDialog.setDialogPositionF(0.020, 0.085);
                return mDialog;
                
        }
        return super.onCreateDialog(id);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
        super.onPrepareDialog(id, dialog, args);
        switch (id) {
            case SKYPECONSTANT.OPTIONSDIALOG.USERPROFILE:
            case SKYPECONSTANT.OPTIONSDIALOG.CALLSETTINGS:
                ((CountrycodeDialog) dialog).initVar(args);
                break;
            case SKYPECONSTANT.OPTIONSDIALOG.CHANGE_PICTURE:
                break;
            case SKYPECONSTANT.OPTIONSDIALOG.UNBLOCK_CONTACTS:
                ((BlockListDialog) dialog).initVar(args);
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus){
            return;
        }
        //set country code when selected 
        if ( null != CountrycodeDialog.mCountryCodeSelected ) {
            switch (mPhoneFocus) {
                case SKYPECONSTANT.OPTIONSPHONEFOCUS.HOMEPHONE:
                    mOptionsViewHolder.mVHUserprofileFragment.getViewHolder().mVHHomePHCodeTv
                            .setText(CountrycodeDialog.mCountryCodeSelected);
                    mOptionsViewHolder.mVHUserprofileFragment.getViewListenner().homephEditDone();
                    break;
                case SKYPECONSTANT.OPTIONSPHONEFOCUS.OFFICEPHONE:
                    mOptionsViewHolder.mVHUserprofileFragment.getViewHolder().mVHOfficePHCodeTv
                            .setText(CountrycodeDialog.mCountryCodeSelected);
                    mOptionsViewHolder.mVHUserprofileFragment.getViewListenner().officephEditDone();
                    break;
                case SKYPECONSTANT.OPTIONSPHONEFOCUS.MOBILEPHONE:
                    mOptionsViewHolder.mVHUserprofileFragment.getViewHolder().mVHMobilePHCodeTv
                            .setText(CountrycodeDialog.mCountryCodeSelected);
                    mOptionsViewHolder.mVHUserprofileFragment.getViewListenner().mobilephEditDone();
                    break;
                case SKYPECONSTANT.OPTIONSPHONEFOCUS.FARWARDONE:
                    mOptionsViewHolder.mVHCallSetFragment.getViewHolder().mVHFirstCallCodeTv
                            .setText(CountrycodeDialog.mCountryCodeSelected);
                    break;
                case SKYPECONSTANT.OPTIONSPHONEFOCUS.FARWARDTWO:
                    mOptionsViewHolder.mVHCallSetFragment.getViewHolder().mVHSecondCallCodeTv
                            .setText(CountrycodeDialog.mCountryCodeSelected);
                    break;
                case SKYPECONSTANT.OPTIONSPHONEFOCUS.FARWARDTHREE:
                    mOptionsViewHolder.mVHCallSetFragment.getViewHolder().mVHThirdCallCodeTv
                            .setText(CountrycodeDialog.mCountryCodeSelected);
                    break;
                default:
                    break;

            }
            CountrycodeDialog.mCountryCodeSelected = null;
            CountrycodeDialog.mCountryNameSelected = null;
         }
        //set contact name when block contact
        if ( null != BlockListDialog.mContactSelected ){
            mOptionsViewHolder.mVHBlockedContactsFragment.getViewListenner().selectBlockContact(BlockListDialog.mContactSelected);
            BlockListDialog.mContactSelected = null;
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
      
        switch (keyCode){
            case KeyEvent.KEYCODE_PAGE_UP:
                if (mOptionsViewListenner.getFragmentFocus() == SKYPECONSTANT.OPTIONSACTIVITYFOCUS.USERPROFILE)
                    mOptionsViewHolder.mVHUserprofileFragment.pageUp();
                break;
            case KeyEvent.KEYCODE_PAGE_DOWN:
                if (mOptionsViewListenner.getFragmentFocus() == SKYPECONSTANT.OPTIONSACTIVITYFOCUS.USERPROFILE)
                    mOptionsViewHolder.mVHUserprofileFragment.pageDown();
            case KeyEvent.KEYCODE_PROG_BLUE:
                if (mOptionsViewListenner.getFragmentFocus() == SKYPECONSTANT.OPTIONSACTIVITYFOCUS.CALLSETTINGS)
                    mOptionsViewHolder.mVHCallSetFragment.getViewListenner().saveSettings();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK )
        {
            Intent intent = new Intent();
            if (requestCode == SKYPECONSTANT.OPTIONSPICTURE.FROM_ALBUM){
                Uri uri = data.getData();
                if (null == uri)
                    return;
                intent.putExtra("ImageUriString",uri.toString());
            } else if (requestCode == SKYPECONSTANT.OPTIONSPICTURE.FROM_CAMERA){
                if  (null == mFilePath)
                    return;
                intent.putExtra("ImagePathString",mFilePath);
            }
           
            intent.setClass(this, PictureActivity.class);
            startActivity(intent);
        }
    }

    public OptionsActivityViewHolder getViewHolder() {
        return mOptionsViewHolder;
    }
    
    public void setFilePath(String path) {
        this.mFilePath = path;
    }

	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    
		mOptionsViewHolder = null;
		mOptionsViewListenner = null;
		mFragmentT = null;
		mDialog = null;
		mFilePath = null;
		 
		System.gc();
		ActivityManager.getInstance().pupActivity(this);
	}
    
}
