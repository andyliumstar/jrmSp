
package com.jrm.skype.ui;

import java.io.File;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.dialog.AddContactTypeDialog;
import com.jrm.skype.dialog.AddPhoneNumberDialog;
import com.jrm.skype.dialog.AddSkypeDialog;
import com.jrm.skype.dialog.ChatDialog;
import com.jrm.skype.dialog.ContactDialog;
import com.jrm.skype.dialog.CountrycodeDialog;
import com.jrm.skype.dialog.HistoryDialog;
import com.jrm.skype.dialog.InvitationDialog;
import com.jrm.skype.dialog.MainDialog;
import com.jrm.skype.dialog.SearchResultDialog;
import com.jrm.skype.dialog.SignoutDialog;
import com.jrm.skype.dialog.SkypeOutCallOutDialog;
import com.jrm.skype.dialog.UsrStateDialog;
import com.jrm.skype.exception.ActivityManager;
import com.jrm.skype.service.SkypeService;
import com.jrm.skype.service.SkypeService.MyBinder;
import com.jrm.skype.util.RunningTasksInfo;
import com.jrm.skype.util.SktApiActor;
import com.skype.api.Contact.AVAILABILITY;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import com.jrm.skype.util.Log;
import android.view.KeyEvent;
import android.view.View;

/**
 * @author andy.liu 
 * the main Activity that contents 4 fragments
 */

public class UsrAccountActivity extends Activity {
    private final static String TAG = "UsrAccountActivity";
    
    private UsrAccountViewHolder mUsrAccountViewHolder;

    private UsrAccountViewListenner mUsrAccountViewListenner;
    
    private SkypeService mSkypeService;
    
    private FragmentTransaction mFragmentT;

    private HistoryDialog mHistoryDialog;
    
    private ContactDialog mContactDialog;
    
    private SkypeOutCallOutDialog mCallOutDialog;
    
    private AddSkypeDialog mAddSkypeDialog;
    
    private ChatDialog mChatDialog;

    private ProgressDialog mProgressDialog;
    
    private boolean mNeedUpdate;
    
    private boolean mIsSigningOut;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usraccount);
        ActivityManager.getInstance().addActivity(this);
        
        mNeedUpdate = false;
        mIsSigningOut = false;

        // init the view
        mUsrAccountViewHolder = new UsrAccountViewHolder(this);
        mUsrAccountViewListenner = new UsrAccountViewListenner(mUsrAccountViewHolder, this);
        
        mUsrAccountViewHolder.findView();
        mUsrAccountViewListenner.setViewListenner();
        mUsrAccountViewListenner.initVar();

        registerReceiver(mMainReceiver, new IntentFilter(SKYPECONSTANT.SKYPEACTION.MAINACTION));
        registerReceiver(mOnlineStatusChangeReceiver, new IntentFilter(SKYPECONSTANT.SKYPEACTION.SIGNOUT_ONLINESTATUS_CHANGE));
        bindService(new Intent(SKYPECONSTANT.SKYPEACTION.SKYPE_SERVICE), mServiceConn, Context.BIND_AUTO_CREATE);
        
        // at first ContactList should show out
        mFragmentT = getFragmentManager().beginTransaction();
        mFragmentT.replace(R.id.rel_usrAc_right_fragment,
                mUsrAccountViewHolder.mVHContactListFragment);
        mFragmentT.commit();
    }

    private ServiceConnection mServiceConn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(TAG, "onServiceConnected");
            mSkypeService = ((MyBinder) service).getService();
            if (null != mSkypeService){
                mSkypeService.setOnlineStatusAction(SKYPECONSTANT.SKYPEACTION.SIGNOUT_ONLINESTATUS_CHANGE);
                mSkypeService.checkAndDownload();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mSkypeService = null;
        } 
    };
    
    public SkypeService getService()
    {
        return mSkypeService;
    }
    
    @Override
    protected void onResume() {
        if ( mNeedUpdate ){
            updateApk();
            mNeedUpdate = false;//only notify one time
        }
        
        //reset the online status action
        if (null != mSkypeService){
            mSkypeService.setOnlineStatusAction(SKYPECONSTANT.SKYPEACTION.SIGNOUT_ONLINESTATUS_CHANGE);
        }
        
        //invisible the waiting ProgressBar
        mUsrAccountViewHolder.mVHProgressBar.setVisibility(View.GONE);
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_PROG_BLUE:
                if (mUsrAccountViewHolder.mVHRequestTv.getVisibility() == View.VISIBLE){
                    showDialog(SKYPECONSTANT.USRACCOUNTDIALOG.INVITATION);
                }
                break;
            case KeyEvent.KEYCODE_PROG_GREEN:
                showDialog(SKYPECONSTANT.USRACCOUNTDIALOG.USR_STATUS);
                break;
            case KeyEvent.KEYCODE_PROG_YELLOW:
                showDialog(SKYPECONSTANT.USRACCOUNTDIALOG.ADD_CONTACT_TYPE);
                break;
            case KeyEvent.KEYCODE_BACK:
                if(mUsrAccountViewListenner.getLoadOptionBool()){
                    mUsrAccountViewListenner.cancelLoadOption();
                    return true;
                }
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (CountrycodeDialog.mCountryCodeSelected != null) {
            mUsrAccountViewHolder.mVHSkypeOutFragment.getViewListenner().setCallCountryInfo(
                    CountrycodeDialog.mCountryNameSelected, CountrycodeDialog.mCountryCodeSelected);
        }
        
        CountrycodeDialog.mCountryCodeSelected = null;
        CountrycodeDialog.mCountryNameSelected = null;
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConn);
        unregisterReceiver(mMainReceiver);
        unregisterReceiver(mOnlineStatusChangeReceiver);
        
		mUsrAccountViewHolder = null;
		mUsrAccountViewListenner = null;
		mFragmentT = null;
		mHistoryDialog = null;
		mContactDialog = null;
		mCallOutDialog = null;
		mAddSkypeDialog = null;
		mChatDialog = null;
		mProgressDialog = null;
		mServiceConn = null;
		mMainReceiver = null;
		mOnlineStatusChangeReceiver = null;
		System.gc();
        
        super.onDestroy();
        ActivityManager.getInstance().pupActivity(this);
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        MainDialog dialog_dim;
        switch (id) {
            case SKYPECONSTANT.USRACCOUNTDIALOG.USR_STATUS:
                dialog_dim = new UsrStateDialog(this, R.style.dialog_dim);
                dialog_dim.setDialogContentView(R.layout.usrstate_dialog);
                dialog_dim.findView();
                dialog_dim.setViewListenner();
                dialog_dim.setDialogSizeF(0.3150, 0.1450);
                dialog_dim.setDialogPositionF(-0.3915, -0.1500);
                return dialog_dim;
            case SKYPECONSTANT.USRACCOUNTDIALOG.SIGN_OUT:
                dialog_dim = new SignoutDialog(this, R.style.dialog_dim);
                dialog_dim.setDialogContentView(R.layout.signout_dialog);
                dialog_dim.findView();
                dialog_dim.setViewListenner();
                dialog_dim.setDialogSizeF(0.2365, 0.3100);
                return dialog_dim;
            case SKYPECONSTANT.USRACCOUNTDIALOG.ADD_CONTACT_TYPE:
                dialog_dim = new AddContactTypeDialog(this, R.style.dialog_dim);
                dialog_dim.setDialogContentView(R.layout.add_contact_type_dialog);
                dialog_dim.findView();
                dialog_dim.setViewListenner();
                dialog_dim.setDialogSizeF(0.4450, 0.3750);
                return dialog_dim;
            case SKYPECONSTANT.USRACCOUNTDIALOG.ADD_SKYPE_CONTACT:
                mAddSkypeDialog = new AddSkypeDialog(this, R.style.dialog_dim);
                mAddSkypeDialog.setDialogContentView(R.layout.add_skype_dialog);
                mAddSkypeDialog.findView();
                mAddSkypeDialog.setViewListenner();
                mAddSkypeDialog.setDialogSizeF(0.4450, 0.3750);
                return mAddSkypeDialog;
            case SKYPECONSTANT.USRACCOUNTDIALOG.ADD_PHONE_NUMBER:
                dialog_dim = new AddPhoneNumberDialog(this, R.style.dialog_dim);
                dialog_dim.setDialogContentView(R.layout.add_phonenumber_dialog);
                dialog_dim.findView();
                dialog_dim.setViewListenner();
                dialog_dim.setDialogSizeF(0.4450, 0.3750);
                return dialog_dim;
            case SKYPECONSTANT.USRACCOUNTDIALOG.COUNTRYCODE_ADD:
                dialog_dim = new CountrycodeDialog(this, R.style.dialog_light);
                dialog_dim.setDialogContentView(R.layout.listview_dialog);
                dialog_dim.findView();
                dialog_dim.setViewListenner();
                // dialog_dim.setDialogSize(230, 350) ;
                dialog_dim.setDialogSizeF(0.319, 0.273);
                // dialog_dim.setDialogPosition(-25, 115);
                dialog_dim.setDialogPositionF(-0.019, 0.160);
                return dialog_dim;
            case SKYPECONSTANT.USRACCOUNTDIALOG.COUNTRYCODE_CALL:
                dialog_dim = new CountrycodeDialog(this, R.style.dialog_light);
                dialog_dim.setDialogContentView(R.layout.listview_dialog);
                dialog_dim.findView();
                dialog_dim.setViewListenner();
                // dialog_dim.setDialogSize(250, 380) ;
                dialog_dim.setDialogSizeF(0.347, 0.299);
                //dialog_dim.setDialogPosition(-210, 90);
                dialog_dim.setDialogPositionF(-0.175, 0.125);
                return dialog_dim;
            case SKYPECONSTANT.USRACCOUNTDIALOG.SEARCH_RESULT:
                dialog_dim = new SearchResultDialog(this, R.style.dialog_dim);
                dialog_dim.setDialogContentView(R.layout.search_result_dialog);
                dialog_dim.findView();
                dialog_dim.setViewListenner();
                // dialog_dim.setDialogSize(470, 650) ;
                dialog_dim.setDialogSizeF(0.653, 0.508);
                return dialog_dim;
            case SKYPECONSTANT.USRACCOUNTDIALOG.INVITATION:
                dialog_dim = new InvitationDialog(this, R.style.dialog_dim);
                dialog_dim.setDialogContentView(R.layout.invitation_dialog);
                dialog_dim.findView();
                dialog_dim.setViewListenner();
                // dialog_dim.setDialogSize(280, 400) ;
                dialog_dim.setDialogSizeF(0.389, 0.313);
                return dialog_dim;
            case SKYPECONSTANT.USRACCOUNTDIALOG.CHAT:
                mChatDialog = new ChatDialog(this, R.style.dialog_dim);
                mChatDialog.setDialogContentView(R.layout.chat_dialog);
                mChatDialog.findView();
                mChatDialog.setViewListenner();
                // dialog_dim.setDialogSize(560, 415) ;
                mChatDialog.setDialogSizeF(0.780, 0.324);
                return mChatDialog;
            case SKYPECONSTANT.USRACCOUNTDIALOG.CONTACTITEM:
                mContactDialog = new ContactDialog(this, R.style.dialog_dim);
                mContactDialog.setDialogContentView(R.layout.contactlist_item_pop_dialog);
                mContactDialog.findView();
                mContactDialog.setViewListenner();
                //dialog_dim.setDialogSize(505, 380) ;
                mContactDialog.setDialogSizeF(0.701, 0.299);
               // dialog_dim.setDialogPosition(390, 40);
                mContactDialog.setDialogPositionF(0.300, 0.065);
                return mContactDialog;
            case SKYPECONSTANT.USRACCOUNTDIALOG.HISTORYITEM:
                mHistoryDialog = new HistoryDialog(this, R.style.dialog_dim);
                mHistoryDialog.setDialogContentView(R.layout.historylist_item_pop_dialog);
                mHistoryDialog.findView();
                mHistoryDialog.setViewListenner();
              //dialog_dim.setDialogSize(505, 380) ;
                mHistoryDialog.setDialogSizeF(0.701, 0.299);
               // dialog_dim.setDialogPosition(390, 40);
                mHistoryDialog.setDialogPositionF(0.300, 0.065);
                return mHistoryDialog;   
            case SKYPECONSTANT.USRACCOUNTDIALOG.SKYPEOUTCALLOUT:
                mCallOutDialog = new SkypeOutCallOutDialog(this, R.style.dialog_light);
                mCallOutDialog.setDialogContentView(R.layout.skypeout_callout_dialog);
                mCallOutDialog.findView();
                mCallOutDialog.setViewListenner();
              //dialog_dim.setDialogSize(505, 380) ;
                mCallOutDialog.setDialogSizeF(0.701, 0.299);
               // dialog_dim.setDialogPosition(390, 40);
                mCallOutDialog.setDialogPositionF(0.300, 0.065);
                return mCallOutDialog; 
            default:
                return null;

        }
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
        super.onPrepareDialog(id, dialog, args);

        switch (id) {
            case SKYPECONSTANT.USRACCOUNTDIALOG.USR_STATUS:
                ((UsrStateDialog) dialog).initVar(args);
                break;
            case SKYPECONSTANT.USRACCOUNTDIALOG.SIGN_OUT:
                //((SignoutDialog) dialog).initVar(args);
                break;
            case SKYPECONSTANT.USRACCOUNTDIALOG.ADD_CONTACT_TYPE:
                ((AddContactTypeDialog) dialog).initVar(args);
                break;
            case SKYPECONSTANT.USRACCOUNTDIALOG.ADD_SKYPE_CONTACT:
                ((AddSkypeDialog) dialog).initVar(args);
                break;
            case SKYPECONSTANT.USRACCOUNTDIALOG.ADD_PHONE_NUMBER:
                ((AddPhoneNumberDialog) dialog).initVar(args);
                break;
            case SKYPECONSTANT.USRACCOUNTDIALOG.SEARCH_RESULT:
                ((SearchResultDialog) dialog).initVar(args);
                break;
            case SKYPECONSTANT.USRACCOUNTDIALOG.INVITATION:
                ((InvitationDialog) dialog).initVar(args);
                break;
            case SKYPECONSTANT.USRACCOUNTDIALOG.CHAT:
                if (args != null)
                    ((ChatDialog) dialog).initVar(args);
                break;
            case SKYPECONSTANT.USRACCOUNTDIALOG.COUNTRYCODE_ADD:
                ((CountrycodeDialog) dialog).initVar(args);
                break;
            case SKYPECONSTANT.USRACCOUNTDIALOG.COUNTRYCODE_CALL:
                ((CountrycodeDialog) dialog).initVar(args);
                break;
            case SKYPECONSTANT.USRACCOUNTDIALOG.CONTACTITEM:
                ((ContactDialog) dialog).initVar(args);
                break;
            case SKYPECONSTANT.USRACCOUNTDIALOG.HISTORYITEM:
                ((HistoryDialog)dialog).initVar(args);
                break;
            case SKYPECONSTANT.USRACCOUNTDIALOG.SKYPEOUTCALLOUT:
                ((SkypeOutCallOutDialog)dialog).initVar(args);
                break;
            default:
                break;
        }
    }
    //for sign out
    public void signingOut(){
        mIsSigningOut = true;
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(R.string.signingout);
        mProgressDialog.setMessage(getResources().getText(R.string.waiting));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
    
    //
    public void signedOut(){
        if (null != mProgressDialog){
            mProgressDialog.cancel();
        }
    }
    
    public UsrAccountViewHolder getViewHolder() {
        return mUsrAccountViewHolder;
    }

    public UsrAccountViewListenner getViewListenner() {
        return mUsrAccountViewListenner;
    }
 
    //skypeapp callback  ------------------------------------------------------------------------->
    private void onGetBuddyList() {
        if (mUsrAccountViewListenner.getFocus() == SKYPECONSTANT.USRACCOUNTFOCUS.CONTACTLIST) {
            mUsrAccountViewHolder.mVHContactListFragment.initList(SktApiActor.getBuddyList());
            mUsrAccountViewHolder.mVHContactListFragment.stopLoadingDialog();
            
            mUsrAccountViewListenner.updateBlance();
        }
    }

    private void onSyncFailed(){
        if (mUsrAccountViewListenner.getFocus() == SKYPECONSTANT.USRACCOUNTFOCUS.CONTACTLIST) {
            mUsrAccountViewHolder.mVHContactListFragment.stopLoadingDialog();
        }
        
        AlertDialog.Builder builder = new Builder(this);
        builder.setMessage(R.string.re_sign);
        builder.setTitle(R.string.sync_failed);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.Yes,new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SktApiActor.logout(false);
                signingOut();
                dialog.dismiss();
            }
        }); 

        builder.create().show();
    }
    private void onBuddyPropertyChange() {
        if (mUsrAccountViewListenner.getFocus() == SKYPECONSTANT.USRACCOUNTFOCUS.CONTACTLIST)
            mUsrAccountViewHolder.mVHContactListFragment.refreshList(SktApiActor.getBuddyList());

        if (null != mContactDialog && mContactDialog.isShowing()){
            mContactDialog.getViewListenner().updateStatus();
            mContactDialog.getViewListenner().updateAvater();
            mContactDialog.getViewListenner().updateCallAbility();
        }
        if (null != mChatDialog && mChatDialog.isShowing()){
            mChatDialog.getViewListenner().updateStatus();
            mChatDialog.getViewListenner().updateAvater();
        }
            
        if(null != mHistoryDialog && mHistoryDialog.isShowing()){
            mHistoryDialog.getViewListenner().updateAvater();
            mHistoryDialog.getViewListenner().updateCallAbility();
        }
    }

    private void onReceivedAuthrequest() {
        mUsrAccountViewHolder.mVHRequestTv.setVisibility(View.VISIBLE);
        
      // pup the notify,must do after the other function done or will cause some problem by startActivity
        Intent intent = new Intent();
        intent.setClass(this, NotifyActivity.class);
        intent.putExtra(SKYPECONSTANT.SKYPESTRING.NOTIFY, SKYPECONSTANT.USRACCOUNTNOTIFY.INVITATION);
        startActivity(intent);
    }
    
    private void onRecivedVoiceMail() {
        // update the ui
        if (mUsrAccountViewListenner.getFocus() == SKYPECONSTANT.USRACCOUNTFOCUS.HISTORYLIST) 
            mUsrAccountViewHolder.mVHHistoryListFragment.updateHistory();
        
      // pup the notify,must do after the other function done or will cause some problem by startActivity
        Intent intent = new Intent();
        intent.setClass(this, NotifyActivity.class);
        intent.putExtra(SKYPECONSTANT.SKYPESTRING.NOTIFY, SKYPECONSTANT.USRACCOUNTNOTIFY.VOICEMAIL);
        startActivity(intent);
    }
    
 
    private void onRecivedChatMessage(String convName, String fromName, long timestamp,String body){
        if ( null != mChatDialog && mChatDialog.isShowing() && mChatDialog.getViewListenner().isSameConv(convName))
            mChatDialog.getViewListenner().receiveNewMsg(convName,fromName,timestamp,body);
        else{
           if (mUsrAccountViewListenner.getFocus() == SKYPECONSTANT.USRACCOUNTFOCUS.HISTORYLIST)
               mUsrAccountViewHolder.mVHHistoryListFragment.updateHistory();
        }
    }
    
    private void onAvatarChange(){
        if (null != mUsrAccountViewListenner){
            mUsrAccountViewListenner.updateAvatar();
        }
    }
    
    private void onAvalibilityChange(){
        if (null != mUsrAccountViewListenner){
            mUsrAccountViewListenner.updateStatus();
        }
        
        //net failed
        if(AVAILABILITY.CONNECTING == SktApiActor.getAccountAvailability()){
            if (null != mContactDialog && mContactDialog.isShowing()){
                mContactDialog.getViewListenner().cancelVMWhenNetError();
            }
                
            if(null != mHistoryDialog && mHistoryDialog.isShowing()){
                mHistoryDialog.getViewListenner().cancelVMWhenNetError();
            }
        }
    }
    
    private void onFullNameChange(){
        if (null != mUsrAccountViewListenner){
            mUsrAccountViewListenner.updateFullName();
        }
    }
    
    private void onMoodTextChange(){
        if (null != mUsrAccountViewListenner){
            mUsrAccountViewListenner.updateMoodText();
        }
    }
    
    private void onBlanceChange(){
        if (null != mUsrAccountViewListenner){
            mUsrAccountViewListenner.updateBlance();
        }
    }
    
    private void updateApk(){
        if (null != mSkypeService){
            final String path = mSkypeService.getNewApkFilePath();
            
            if (null != path){
                AlertDialog.Builder builder = new Builder(this);
                builder.setMessage(R.string.new_version);
                builder.setTitle(R.string.install);
                builder.setPositiveButton(R.string.Yes,new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        installApk(path);
                        dialog.dismiss();
                    }
                }); 
                builder.setNegativeButton(R.string.No,  new DialogInterface.OnClickListener() {
                   
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();              
                   }
                }); 

                builder.create().show();
            }
        }
    }
    
    private void installApk(String path){
        File apkfile;
        try {
            apkfile = new File(path);
            if (!apkfile.exists()) {
                return;
            }
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(apkfile.getAbsolutePath())),
                    "application/vnd.android.package-archive");  
            startActivity(intent);
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    private BroadcastReceiver mMainReceiver = new BroadcastReceiver (){

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent)
                return;
            switch (intent.getIntExtra(SKYPECONSTANT.SKYPEACTION.MAINACTION, -1)){
                case SKYPECONSTANT.MAINACTION.GETBUDDYLIST:
                    onGetBuddyList();
                    break;
                case SKYPECONSTANT.MAINACTION.BUDDYPROPERTYCHANGE:
                    onBuddyPropertyChange();
                    break;
                case SKYPECONSTANT.MAINACTION.AUTHREQUEST:
                    onReceivedAuthrequest();
                    break;
                case SKYPECONSTANT.MAINACTION.VOICEMAIL:
                    onRecivedVoiceMail();
                    break;
                case SKYPECONSTANT.MAINACTION.MESSAGE:
                    onRecivedChatMessage(intent.getStringExtra(SKYPECONSTANT.SKYPESTRING.CONVNAME), 
                            intent.getStringExtra(SKYPECONSTANT.SKYPESTRING.CONVPARTNER),
                            intent.getLongExtra(SKYPECONSTANT.SKYPESTRING.CONVTIME, 0),
                            intent.getStringExtra(SKYPECONSTANT.SKYPESTRING.CONVBODY));
                    break;
                case SKYPECONSTANT.MAINACTION.AVATAR:
                    onAvatarChange();
                    break;
                case SKYPECONSTANT.MAINACTION.AVAILABILITY:
                    onAvalibilityChange();
                    break;
                case SKYPECONSTANT.MAINACTION.FULLNAME:
                    onFullNameChange();
                    break;
                case SKYPECONSTANT.MAINACTION.MOODTEXT:
                    onMoodTextChange();
                    break;
                case SKYPECONSTANT.MAINACTION.BLANCE:
                    onBlanceChange();
                    break;
                case SKYPECONSTANT.MAINACTION.SYNCFAILED:
                    onSyncFailed();
                    break;
                case SKYPECONSTANT.MAINACTION.UPDATE:
                    if (!RunningTasksInfo.IsActivityTopOfRunningTask(UsrAccountActivity.this)||mIsSigningOut ) {
                        mNeedUpdate = true;
                        return;
                    }
                    updateApk();
                    break;
                default:
                    break;
            }
        }
    };
    
    private BroadcastReceiver mOnlineStatusChangeReceiver = new BroadcastReceiver (){

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent)
                return;
            if (!intent.getBooleanExtra(SKYPECONSTANT.SKYPEACTION.SIGNOUT_ONLINESTATUS_CHANGE, false)){
                Intent mIntent = new Intent();
                mIntent.setClass(UsrAccountActivity.this, SigninActivity.class);
                startActivity(mIntent);
                signedOut(); 
                finish();
            }else{
                signedOut();  
            }
        }
    };
}
