package com.jrm.skype.service;

import com.jrm.skype.api.SktSkypeApp;
import com.jrm.skype.api.SktSkypeApp.ConvType;
import com.jrm.skype.api.SktSkypeApp.EndedLivessionReason;
import com.jrm.skype.api.SktSkypeApp.LiveStatus;
import com.jrm.skype.api.SktSkypeApp.LoginStatus;
import com.jrm.skype.api.SktSkypeApp.MsgType;
import com.jrm.skype.api.SktSkypeApp.VideoStatus;
import com.jrm.skype.api.SktSkypeApp.VoiceStatus;
import com.jrm.skype.api.SktSkypeApp.VoicemailStatus;
import com.jrm.skype.api.SktUtils;
import com.jrm.skype.binder.SkypeExceptionBinder;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.datebase.ConvTableActor;
import com.jrm.skype.datebase.ConvTableResourceHolder;
import com.jrm.skype.datebase.ChatTableActor;
import com.jrm.skype.datebase.ChatTableResourceHolder;
import com.jrm.skype.datebase.DateBaseHelper;
import com.jrm.skype.datebase.ConvTableActor.ConvTableInfoHolder;
import com.jrm.skype.exception.ActivityManager;
import com.jrm.skype.ui.R;
import com.jrm.skype.util.DateFormat;
import com.jrm.skype.util.SktApiActor;
import com.jrm.skype.util.SkypePrefrences;
import com.jrm.skype.util.SoundPlayer;
import com.jrm.skype.util.UpdateManager;
import com.skype.api.Account;
import com.skype.api.Contact;
import com.skype.api.Account.LOGOUTREASON;
import com.skype.api.Account.PWDCHANGESTATUS;
import com.skype.api.Contact.AVAILABILITY;
import com.skype.api.ContactSearch.STATUS;
import com.skype.api.Voicemail.TYPE;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import com.jrm.skype.util.Log;
import android.widget.Toast;

public class SkypeService extends Service implements SktSkypeApp.updateObserver{
    private final static String TAG = "SkypeService";
    
    private final int CONNECT_FAILED_WAIT_MS = 5000;
    
    private int mConvStatus;
    
    private ConvTableResourceHolder mConvTableResourceHolder;
    
    private ChatTableResourceHolder mChatTableResourceHolder;
    
    private DateFormat mDateFormat;
    
    private UpdateManager mUpdateManager;
    
    private String mNewApkPath;
    
    private boolean mIsAutoSign;//auto sign in when skype start
    
    private boolean mIsRestarKit;// whether it is the restart of skype,should reset after using it.
    
    private boolean mHasSignin;// whether the user has sign in 
    
    private String mConvStatusAction = null;
    
    private String mOnlineStatusChangeAction = null; 
    
    private String mVoiceMailStatusChangeAction = null;
    
    private boolean mIsCallOut = false;
    
    private Handler mHandler;
    
    private int mReasonEndConvId;
    
    private String mLogInFailedTip;
    
    private SkypeExceptionBinder mSkypeExceptionbinder;
    
    @Override
    public void onCreate() {
        Log.d(TAG,"--------->onCreate");
        SkypePrefrences.initSkypePrefrences(getApplicationContext());
        mHandler = new Handler();
        mDateFormat = new DateFormat();
        mReasonEndConvId = -1;
        mConvStatus = -1;
        
        mIsAutoSign = SkypePrefrences.isSignWhenSKTStart(getApplicationContext());
        mIsRestarKit = SkypePrefrences.isReStartKit(getApplicationContext());
        mHasSignin = SkypePrefrences.hasSignin(getApplicationContext());
        
        SkypePrefrences.setReStartKit(getApplicationContext(), false);
        SkypePrefrences.setSignin(getApplicationContext(), false);
        SktUtils.exec("sync");
        
        if(mIsRestarKit){
            if(SkypePrefrences.isInConv(getApplicationContext())){
                Toast.makeText(getApplicationContext(),
                        R.string.skypekit_close,
                        Toast.LENGTH_SHORT).show();
            }
            
            Intent mIntent = new Intent();
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(mHasSignin){
                mIntent.setAction(SKYPECONSTANT.SKYPEACTION.USRACCOUNTACTIVITY);
            }else{
                mIntent.setAction(SKYPECONSTANT.SKYPEACTION.SIGNINACTIVITY);
            }
            startActivity(mIntent);
        }
        super.onCreate();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"--------->onStartCommand");
        
        SktSkypeApp.InitSktSkypeApp(getApplicationContext(),SkypeService.this);
        bindService(new Intent(SKYPECONSTANT.SKYPEACTION.SKYPE_EXCEPTION_SERVICE), exceptionSC, Context.BIND_AUTO_CREATE); 
        return START_STICKY;//the UI will auto start when met some uncaught exception, so had better auto start the service
    }
    
    private ServiceConnection exceptionSC = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(TAG, "bindService---SKYPE_EXCEPTION_SERVICE");
            mSkypeExceptionbinder = SkypeExceptionBinder.Stub.asInterface(service);        
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mSkypeExceptionbinder = null ;
        } 
    };  
    
    public void checkAndDownload(){
        mUpdateManager = new UpdateManager(this);
        mUpdateManager.checkNewApkAndDownload();
    }
    
    public String getNewApkFilePath(){
        return mNewApkPath;
    }
    
    public void downloadedSuccess(String filePath){
        mNewApkPath = filePath;
        
        Intent mIntent = new Intent();
        mIntent.setAction(SKYPECONSTANT.SKYPEACTION.MAINACTION);
        mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.MAINACTION, SKYPECONSTANT.MAINACTION.UPDATE);
        sendBroadcast(mIntent);
    }
    
    public void setOnlineStatusAction(String action){
        mOnlineStatusChangeAction = action;
    }
    
    public void setVoiceMailStatusChangeAction(String action){
        mVoiceMailStatusChangeAction = action;
    }
    
    public void setConvStatusAction(String action){
        mConvStatusAction = action;
    }
    
    public int getConvStatus(){
        return mConvStatus;
    }

    public void setCallOutBool(boolean isCallOut){
        this.mIsCallOut = isCallOut;
    }
  
    @Override
    public void OnSkypeKitConnectionClosed() {
        Log.d(TAG, "OnSkypeKitConnectionClosed------------------>");
        
        if(mConvStatus == SKYPECONSTANT.CONVERSATIONSTATUS.IM_LIVE){
            SkypePrefrences.setInConv(getApplicationContext(), true); 
        }else{
            SkypePrefrences.setInConv(getApplicationContext(), false); 
        }
        SkypePrefrences.setReStartKit(getApplicationContext(), true);
        SkypePrefrences.setRequestContactListBool(getApplicationContext(),true);
        
        SktUtils.exec("sync");
        
        ActivityManager.getInstance().finishAll();
        try {
            if (null != mSkypeExceptionbinder) {
                mSkypeExceptionbinder.reStartSkypeKit(android.os.Process.myPid());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSkypeKitConnectChange(boolean connected) {
        Log.d(TAG,"onSkypeKitConnectChange--------->"+connected);
        SkypePrefrences.setKitConnect(getApplicationContext(), connected);
        
        if (!connected){ 
            SkypePrefrences.setSignin(getApplicationContext(), mHasSignin);
            OnSkypeKitConnectionClosed();
        }else{
            SktApiActor.initApi();
            SoundPlayer.loadSound(getApplicationContext());
            // restartkit or set auto login need auto login
            if ((mIsRestarKit && mHasSignin)
                    || (!mIsRestarKit && !SktApiActor.isLoggedIn() && mIsAutoSign)) {
                String actName = SkypePrefrences.getSignSkypeName(this);
                String password = SkypePrefrences.getSignPassword(this);
                if (!SktApiActor.login(actName, password, true)) {
                    Intent mIntent = new Intent();
                    mIntent.setAction(mOnlineStatusChangeAction);
                    mLogInFailedTip = getResources().getString(R.string.password_incorrect);
                    mIntent.putExtra(mOnlineStatusChangeAction, false);
                    mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.LOGINTIP, mLogInFailedTip);
                    sendBroadcast(mIntent);
                }
            } else {// if it is the tv power on,no one will receive the Broadcast
                Intent mIntent = new Intent();
                mIntent.setAction(SKYPECONSTANT.SKYPEACTION.SKYPEKITCONNECT_CHANGE);
                mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.SKYPEKITCONNECT_CHANGE, connected);
                sendBroadcast(mIntent);
            }
        }
    }

    @Override
    public void onDefaultAccountChange(String defAccount) {
    }
    

    @Override
    public void onOnlineStatusChange(LoginStatus status, LOGOUTREASON logoutReason) {
        Log.d(TAG,"onOnlineStatusChange--------->"+status+"---"+logoutReason);
        mIsRestarKit = false; //reset the value
        
        ConvTableActor.initConvTableActor(SktApiActor.getAccountName(), this);
        ChatTableActor.initChatTableActor(SktApiActor.getAccountName(), this);
        
        Intent mIntent = new Intent();
        mIntent.setAction(mOnlineStatusChangeAction);
        
        if (status.compareTo(LoginStatus.LOGGED_IN) == 0) {
            //only need request the contact list when login
            SkypePrefrences.setRequestContactListBool(getApplicationContext(),true);
            SkypePrefrences.setSignin(getApplicationContext(), true);
            SktUtils.exec("sync");
            if (null == mOnlineStatusChangeAction) {
                return;
            }
            mIntent.putExtra(mOnlineStatusChangeAction, true);

        } else {
            SkypePrefrences.setSignin(getApplicationContext(), false);
            SktUtils.exec("sync");
            SktApiActor.release();

            switch (logoutReason) {
                case LOGOUT_CALLED:
                    if (mOnlineStatusChangeAction
                            .equals(SKYPECONSTANT.SKYPEACTION.SIGNING_ONLINESTATUS_CHANGE))
                        mLogInFailedTip = getResources().getString(R.string.login_cancel);
                    else
                        mLogInFailedTip = getResources().getString(R.string.create_cancel);
                    break;
                case INCORRECT_PASSWORD:
                case PASSWORD_HAS_CHANGED:
                    mLogInFailedTip = getResources().getString(R.string.password_incorrect);
                    break;
                case SERVER_CONNECT_FAILED:
                    if (SktUtils.isNetworkAvailable(getApplicationContext())) {
                        if (null != mHandler) {
                            mHandler.removeCallbacks(reSignRunnable);
                            mHandler.postDelayed(reSignRunnable, CONNECT_FAILED_WAIT_MS);
                        }
                        return;
                    } else {
                        mLogInFailedTip = getResources().getString(R.string.server_connect_failed);
                    }
                    break;
                case P2P_CONNECT_FAILED:
                    mLogInFailedTip = getResources().getString(R.string.network_wrong);
                    mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.PASSWORD, true);
                    break;
                case SKYPENAME_TAKEN:
                    mLogInFailedTip = getResources().getString(R.string.skypename_exist);
                    break;
                case UNACCEPTABLE_PASSWORD:
                    mLogInFailedTip = getResources().getString(R.string.password_unacceptable);
                    break;
                case INVALID_EMAIL:
                    mLogInFailedTip = getResources().getString(R.string.email_invalid);
                    break;
                case INVALID_SKYPENAME:
                    mLogInFailedTip = getResources().getString(R.string.skypename_invalid);
                    break;
                default:
                    mLogInFailedTip = getResources().getString(R.string.unknown_error);
                    break;
            }
            
            ConvTableActor.releaseConvTableActor();
            ChatTableActor.releaseChatTableActor();
            
            if (null == mOnlineStatusChangeAction) {// no one will receive the broadcast
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), mLogInFailedTip, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
                return;
            }
            mIntent.putExtra(mOnlineStatusChangeAction, false);
            mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.LOGINTIP, mLogInFailedTip);
        }
        
        sendBroadcast(mIntent);
    }

   Runnable reSignRunnable = new Runnable() {
        
        @Override
        public void run() {
            SktSkypeApp.InitSktSkypeApp(getApplicationContext(),SkypeService.this);
        }
    };
    
    @Override
    public void onAvatarChange()
    {
        Intent mIntent = new Intent();
        mIntent.setAction(SKYPECONSTANT.SKYPEACTION.MAINACTION);
        mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.MAINACTION, SKYPECONSTANT.MAINACTION.AVATAR);
        sendBroadcast(mIntent);
    }
    
    @Override
    public void onSyncStatusChange(int val) {
        Account.CBLSYNCSTATUS status = Account.CBLSYNCSTATUS.get(val);
        if (Account.CBLSYNCSTATUS.CBL_SYNC_FAILED == status) {
            Intent mIntent = new Intent();
            mIntent.setAction(SKYPECONSTANT.SKYPEACTION.MAINACTION);
            mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.MAINACTION, SKYPECONSTANT.MAINACTION.SYNCFAILED);
            sendBroadcast(mIntent);
        }
    }
    
    @Override
    public void onAvailiblityChange(AVAILABILITY availablity) {
        Intent mIntent = new Intent();
        mIntent.setAction(SKYPECONSTANT.SKYPEACTION.MAINACTION);
        mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.MAINACTION, SKYPECONSTANT.MAINACTION.AVAILABILITY);
        sendBroadcast(mIntent);
    }
    
    @Override
    public void onMoodTextChange() {
        Intent mIntent = new Intent();
        mIntent.setAction(SKYPECONSTANT.SKYPEACTION.MAINACTION);
        mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.MAINACTION, SKYPECONSTANT.MAINACTION.MOODTEXT);
        sendBroadcast(mIntent);
    }

    @Override
    public void onFullNameChange() {
        Intent mIntent = new Intent();
        mIntent.setAction(SKYPECONSTANT.SKYPEACTION.MAINACTION);
        mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.MAINACTION, SKYPECONSTANT.MAINACTION.FULLNAME);
        sendBroadcast(mIntent);
    } 
    
    @Override
    public void onBlanceChange() {
        Intent mIntent = new Intent();
        mIntent.setAction(SKYPECONSTANT.SKYPEACTION.MAINACTION);
        mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.MAINACTION, SKYPECONSTANT.MAINACTION.BLANCE);
        sendBroadcast(mIntent);
    }


    @Override
    public void onGetBuddyList() {
        Intent mIntent = new Intent();
        mIntent.setAction(SKYPECONSTANT.SKYPEACTION.MAINACTION);
        mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.MAINACTION, SKYPECONSTANT.MAINACTION.GETBUDDYLIST);
        sendBroadcast(mIntent);
    }

    @Override
    public void onBuddyPropertyChange() {
        Intent mIntent = new Intent();
        mIntent.setAction(SKYPECONSTANT.SKYPEACTION.MAINACTION);
        mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.MAINACTION, SKYPECONSTANT.MAINACTION.BUDDYPROPERTYCHANGE);
        sendBroadcast(mIntent); 
    }

    @Override
    public void onAlertMessage(String message) {
        
    }

    @Override
    public void onSearchStatusChange(STATUS status) {
        Intent mIntent = new Intent();
        mIntent.setAction(SKYPECONSTANT.SKYPEACTION.SEARCHSTATUS_CHANGE);
        switch (status) {
            case PENDING:
                mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.SEARCHSTATUS_CHANGE,
                        SKYPECONSTANT.SEARCHSTATUS.PENDING);
                break;
            case FINISHED:
            case EXTENDABLE:
                mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.SEARCHSTATUS_CHANGE,
                        SKYPECONSTANT.SEARCHSTATUS.FINISHED);
                break;
            case FAILED:
                mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.SEARCHSTATUS_CHANGE,
                        SKYPECONSTANT.SEARCHSTATUS.FAILED);
                break;
            default :
                break;
        }
        sendBroadcast(mIntent);
    }

    @Override
    public synchronized void onNewMessage(ConvType convType, MsgType msgType, String convName, 
    		String fromName, long timestamp, String body, int duration) {
        
        SoundPlayer.playMsgSound(getApplicationContext());
        
        Intent mIntent = new Intent();
        mIntent.setAction(SKYPECONSTANT.SKYPEACTION.MAINACTION);
        
        switch (msgType){
            case VOICEMAIL:
              //update the history database
                mConvTableResourceHolder = new ConvTableResourceHolder();
                mConvTableResourceHolder.setConvName(convName);
                mConvTableResourceHolder.setConvDate(mDateFormat.getFormatString(timestamp, "yyyy-MM-dd"));
                mConvTableResourceHolder.setConvDuration(mDateFormat.getFormatString(duration, "HH:mm:ss"));
                mConvTableResourceHolder.setConvType(SKYPECONSTANT.CONVTYPE.VOICEMAILNEW+"");
                mConvTableResourceHolder.setConvTime(mDateFormat.getFormatString(timestamp, "HH:mm:ss"));
                mConvTableResourceHolder.setVMId(body);

                ConvTableActor.getInstance().insert(mConvTableResourceHolder); 
                
                mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.MAINACTION, SKYPECONSTANT.MAINACTION.VOICEMAIL);
                break;
            case CHAT:
                //update the history database
                String  colums = ConvTableInfoHolder.CONVNAME+"=? and ( "+ConvTableInfoHolder.CONVTYPE+"=? or "+ConvTableInfoHolder.CONVTYPE+"=? )";
                String [] whereValue = {convName,SKYPECONSTANT.CONVTYPE.NEWMESSAGE+"",SKYPECONSTANT.CONVTYPE.OLDMESSAGE+""}; 
                Cursor csr= ConvTableActor.getInstance().select(colums, whereValue);
                
                String msgNum;
                int num = 1;
                if ( null != csr && csr.getCount()>0){
                    csr.moveToFirst();
                    msgNum = csr.getString(csr.getColumnIndex(ConvTableInfoHolder.CONVDURATION));
                    num = Integer.parseInt(msgNum)+1;
                    csr.close();
                    ConvTableActor.getInstance().delete(colums, whereValue);
                }
                
                mConvTableResourceHolder = new ConvTableResourceHolder();
                mConvTableResourceHolder.setConvName(convName);
                mConvTableResourceHolder.setConvDate(mDateFormat.getFormatString(timestamp, "yyyy-MM-dd"));
                mConvTableResourceHolder.setConvDuration(num+"");
                mConvTableResourceHolder.setConvType(SKYPECONSTANT.CONVTYPE.NEWMESSAGE+"");
                mConvTableResourceHolder.setConvTime(mDateFormat.getFormatString(timestamp, "HH:mm:ss"));

                ConvTableActor.getInstance().insert(mConvTableResourceHolder); 
                //insert the new msg into chat database
                mChatTableResourceHolder = new ChatTableResourceHolder();
                mChatTableResourceHolder.setChatConvName(convName);
                mChatTableResourceHolder.setChatTime(mDateFormat.getFormatString(timestamp, "HH:mm:ss"));
                mChatTableResourceHolder.setChatDate(mDateFormat.getFormatString(timestamp, "yyyy-MM-dd"));
                mChatTableResourceHolder.setChatString(body);
                mChatTableResourceHolder.setChatDisplayName(SktApiActor.getContactDisplayName(fromName));
                mChatTableResourceHolder.setChatRead("no");
                ChatTableActor.getInstance().insert(mChatTableResourceHolder);
                mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.MAINACTION, SKYPECONSTANT.MAINACTION.MESSAGE);
                mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.CONVNAME, convName);
                mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.CONVPARTNER, fromName);
                mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.CONVTIME, timestamp);
                mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.CONVBODY, body);
            default :
                break;
        }
        sendBroadcast(mIntent);
    }

    @Override
    public void onReceivedAuthrequest(String actName, String text) {
        SoundPlayer.playRequestSound(getApplicationContext());
        Intent mIntent = new Intent();
        mIntent.setAction(SKYPECONSTANT.SKYPEACTION.MAINACTION);
        mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.MAINACTION, SKYPECONSTANT.MAINACTION.AUTHREQUEST);
        sendBroadcast(mIntent);
    }

    @Override
    public void onIncomingCall(String convName, String[] customers, ConvType type) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mConvStatus = -1;
        
//        // other apk is playing video
//        if (!RunningTasksInfo.IsVidepApkRunning(getApplicationContext())) {
//            Log.v(TAG, "IncomingConv--------start---->" + type);
//            if (null == convName || null == customers) {
//                return;
//            }
//
//            if (Contact.AVAILABILITY.DO_NOT_DISTURB != SktApiActor.getAccountAvailability()) {
//                SoundPlayer.playCallinSound(getApplicationContext());
//            }
//        } else {
//            intent.putExtra(SKYPECONSTANT.SKYPESTRING.ISVIDEOPLAYING, true);
//        }
        
        // other apk is playing video or music
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (null != audioManager && !audioManager.isMusicActive()) {
            Log.v(TAG, "IncomingConv--------start---->" + type);
            if (null == convName || null == customers) {
                return;
            }

            if (Contact.AVAILABILITY.DO_NOT_DISTURB != SktApiActor.getAccountAvailability()) {
                SoundPlayer.playCallinSound(getApplicationContext());
            }
        } else {
            intent.putExtra(SKYPECONSTANT.SKYPESTRING.ISVIDEOPLAYING, true);
        }

        if (customers.length < 1) {
            Log.e(TAG, "IncomingConv----------->customers--error.");
            return;
        } else {
            switch (type) {
                case DIALOG:
                    intent.putExtra(SKYPECONSTANT.SKYPESTRING.CALLTYPE,
                            SKYPECONSTANT.CALLTYPE.DIALOG);
                    break;
                case CONFERENCE:
                    intent.putExtra(SKYPECONSTANT.SKYPESTRING.CALLTYPE,
                            SKYPECONSTANT.CALLTYPE.CONFERENCE);
                    break;
                default:
                    intent.putExtra(SKYPECONSTANT.SKYPESTRING.CALLTYPE,
                            SKYPECONSTANT.CALLTYPE.DIALOG);
                    break;
            }
            intent.putExtra(SKYPECONSTANT.SKYPESTRING.CONVNAME, convName);
            intent.putExtra(SKYPECONSTANT.SKYPESTRING.CUSTOMERS, customers);
        }
        intent.setAction(SKYPECONSTANT.SKYPEACTION.INCOMINGCALLACTIVITY);
        startActivity(intent);
    }

    @Override
    public void onLocalLiveStatusChange(String convName, LiveStatus liveStatus) {
        Log.v(TAG, "onLiveStatusChange----------->"+liveStatus);
        SoundPlayer.stopCallinSound();
        SoundPlayer.stopCalloutSound();
        
        if (null == convName){
            Log.v(TAG, "onLiveStatusChange----------->action is null");
            return;
        }
 
        if (!convName.equals(SktApiActor.getConvCallName())){
            Log.v(TAG, "onLiveStatusChange----------->convname is not match");
            return;
        }
        Intent mIntent = new Intent();
        mIntent.setAction(mConvStatusAction);
        
        switch (liveStatus) {
            case IM_LIVE:
                mIntent.putExtra(mConvStatusAction, SKYPECONSTANT.CONVERSATIONSTATUS.IM_LIVE);
                mConvStatus = SKYPECONSTANT.CONVERSATIONSTATUS.IM_LIVE;
                break;
            case RECENTLY_LIVE:
                mIntent.putExtra(mConvStatusAction, SKYPECONSTANT.CONVERSATIONSTATUS.RECENTLY_LIVE);
                mConvStatus = SKYPECONSTANT.CONVERSATIONSTATUS.RECENTLY_LIVE;
                break;
            case ON_HOLD_REMOTELY:
                mIntent.putExtra(mConvStatusAction, SKYPECONSTANT.CONVERSATIONSTATUS.ON_HOLD_REMOTELY);
                mConvStatus = SKYPECONSTANT.CONVERSATIONSTATUS.ON_HOLD_REMOTELY;
                break;
            case OTHERS_ARE_LIVE://for conference 
                mIntent.putExtra(mConvStatusAction, SKYPECONSTANT.CONVERSATIONSTATUS.OTHERS_ARE_LIVE);
                mConvStatus = SKYPECONSTANT.CONVERSATIONSTATUS.RECENTLY_LIVE;//just for check the conv status, OTHERS_ARE_LIVE = RECENTLY_LIVE, conv end
                break;
            default :
                mConvStatus = -1;
                break;
        }
        
        sendBroadcast(mIntent);
    }

    @Override
    public void onParticipantsListChange(String convName, String[] customers) {
        if (null == mConvStatusAction || null == convName || null == customers){
            return;
        }
        if (!convName.equals(SktApiActor.getConvCallName())){
            return;
        }
        
        Intent mIntent = new Intent();
        mIntent.setAction(mConvStatusAction);
        mIntent.putExtra(mConvStatusAction, SKYPECONSTANT.CONVERSATIONSTATUS.PARTICIPANTSCHANGE);
        mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.CUSTOMERS, customers);
        sendBroadcast(mIntent);
    }

    @Override
    public void onVoiceStatusChange(String actName, VoiceStatus status) {
        Log.v(TAG, "actName--->"+actName+"---VoiceStatus"+status);
        
        if(null == actName){
          return;  
        }
        
        if (status == VoiceStatus.CONNECTING){
            return; 
        }
        
        //call out and other is ringing
        if (mIsCallOut && !SktApiActor.getAccountName().endsWith(actName)) {
            if (status == VoiceStatus.RINGING) {
                SoundPlayer.playCalloutSound(getApplicationContext());
            } else {
                SoundPlayer.stopCalloutSound();
                mIsCallOut = false;
            }
        }
    }
    
    @Override
    public void onVideoStatusChange(String actName, VideoStatus status) {
        Log.v(TAG, "------------VideoStatus----"+actName+"-----------"+status);
        
        if (null == actName || "".equals(actName))
            return;
        
        Intent mIntent = new Intent();
        mIntent.setAction(SKYPECONSTANT.SKYPEACTION.VIDEO_STATUS_CHANGE);
        mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.VIDEO_ACTNAME,actName);
        
        switch (status) {
            case AVAILABLE:
                mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.VIDEO_STATUS_CHANGE, SKYPECONSTANT.VIDEOSTATUS.AVAILABLE);
                break;
            case RUNNING:
                mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.VIDEO_STATUS_CHANGE, SKYPECONSTANT.VIDEOSTATUS.RUNNING);
                break;
            case STOPPED:
                mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.VIDEO_STATUS_CHANGE, SKYPECONSTANT.VIDEOSTATUS.STOPPED);
                break;
            case PAUSED:
                mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.VIDEO_STATUS_CHANGE, SKYPECONSTANT.VIDEOSTATUS.PAUSED);
                break;
            default :
                break;
        }
        sendBroadcast(mIntent);
    }

    @Override
    public void onPasswordChange(PWDCHANGESTATUS status) {
        String mPswChangeTip;
        switch(status){
            case PWD_OK:
            case PWD_OK_BUT_CHANGE_SUGGESTED:
                mPswChangeTip = getResources().getString(R.string.psw_ok);
                break;
            case PWD_INVALID_OLD_PASSWORD:
                mPswChangeTip = getResources().getString(R.string.oldpsw_incorrect);
                break;
            case PWD_MUST_DIFFER_FROM_OLD:
                mPswChangeTip = getResources().getString(R.string.psw_same);
                break;
            case PWD_INVALID_NEW_PWD:
                mPswChangeTip = getResources().getString(R.string.newpsw_invalid);
                break;
            case PWD_CHANGING:
                mPswChangeTip = getResources().getString(R.string.psw_changing);
                break;
            default:
                mPswChangeTip = getResources().getString(R.string.psw_failed);
                break;
        }
        Intent mIntent = new Intent();
        mIntent.setAction(SKYPECONSTANT.SKYPEACTION.PASSWORD_CHANGE);
        mIntent.putExtra(SKYPECONSTANT.SKYPEACTION.PASSWORD_CHANGE, mPswChangeTip);
        sendBroadcast(mIntent);
    }

    @Override
    public synchronized void onVoicemailStatusChange(TYPE type, VoicemailStatus status, int value) {
        Log.v(TAG, "onVoicemailStatusChange-------------"+status);
        
        if (null == mVoiceMailStatusChangeAction )
            return;
        Intent mIntent = new Intent();
        mIntent.setAction(mVoiceMailStatusChangeAction);
        switch (status){
            case INITIALIZE:
                mIntent.putExtra(mVoiceMailStatusChangeAction, SKYPECONSTANT.VOICEMAILSTATUS.INITIALIZE);
                break;
            case RECORDING:
                mIntent.putExtra(mVoiceMailStatusChangeAction, SKYPECONSTANT.VOICEMAILSTATUS.RECORDING);
                break;
            case RECORDING_PROGRESS:
                mIntent.putExtra(mVoiceMailStatusChangeAction, SKYPECONSTANT.VOICEMAILSTATUS.RECORDING_PROGRESS);
                break;
            case CANCELLED:
                mIntent.putExtra(mVoiceMailStatusChangeAction, SKYPECONSTANT.VOICEMAILSTATUS.CANCELLED);
                break;
            case RECORDED:
            case SENDED:
                mIntent.putExtra(mVoiceMailStatusChangeAction, SKYPECONSTANT.VOICEMAILSTATUS.SENDED);
                break;
            case PLAYING:
                mIntent.putExtra(mVoiceMailStatusChangeAction, SKYPECONSTANT.VOICEMAILSTATUS.PLAYING);
                break;
            case PLAYBACK_PROGRESS:
                mIntent.putExtra(mVoiceMailStatusChangeAction, SKYPECONSTANT.VOICEMAILSTATUS.PLAYBACK_PROGRESS);
                break;
            case PLAYED:
                mIntent.putExtra(mVoiceMailStatusChangeAction, SKYPECONSTANT.VOICEMAILSTATUS.PLAYED);
                break;
            case CANCELLED_RECORD_WHEN_INCOMING_CALL:
                mIntent.putExtra(mVoiceMailStatusChangeAction, SKYPECONSTANT.VOICEMAILSTATUS.CANCELLED_RECORD_WHEN_INCOMING_CALL);
                break;
            case CANCELLED_PLAYBACK_WHEN_INCOMING_CALL:
                mIntent.putExtra(mVoiceMailStatusChangeAction, SKYPECONSTANT.VOICEMAILSTATUS.CANCELLED_PLAYBACK_WHEN_INCOMING_CALL);
                break;
            case FAILED:
                if (TYPE.INCOMING == type){
                    mIntent.putExtra(mVoiceMailStatusChangeAction, SKYPECONSTANT.VOICEMAILSTATUS.RECFAILED);
                }else{
                    mIntent.putExtra(mVoiceMailStatusChangeAction, SKYPECONSTANT.VOICEMAILSTATUS.SENDFAILED);
                }
                break;
            default :
                break;
        }
        mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.VOICEMAILSTATUS, value);
        sendBroadcast(mIntent);
    }
    
    @Override
	public void onReasonForEndedLivession(EndedLivessionReason reason) {
	    switch(reason){
            case NO_ANSWER:
                mReasonEndConvId = R.string.no_answer;
                break;
            case BUSY:
                mReasonEndConvId = R.string.busy;
                break;
            case INSUFFICIENT_FUNDS:
                mReasonEndConvId = R.string.insufficient_funds;
                break;
            case PSTN_INVALID_NUMBER:
                mReasonEndConvId = R.string.invalid_phone_numer;
                break;
            case PSTN_CALL_TIMED_OUT:
                mReasonEndConvId = R.string.pstn_call_time_out;
                break;
            case PSTN_BUSY:
                mReasonEndConvId = R.string.pstn_busy;
                break;
            case UNABLE_TO_CONNECT:
                mReasonEndConvId = R.string.unable_to_connect;
                break;
        }
        
        Log.v(TAG, "------------" + reason + "-------------");
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (-1 != mReasonEndConvId) {
                    Toast.makeText(getApplicationContext(), mReasonEndConvId, Toast.LENGTH_SHORT).show();
                    mReasonEndConvId = -1;
                }
            }
        });
	}
    
    @Override
    public void onDestroy() {
        Log.d(TAG,"--------->onDestroy");
        SoundPlayer.release();
        try {
            DateBaseHelper.getInstance(this, DateBaseHelper.DATEBASENAME).close();
        } catch (Exception e) {
            Log.e("SKYPE_DATABASE", "failed to close datebase!");
        }
        
        unbindService(exceptionSC);
        mConvTableResourceHolder = null;
        mChatTableResourceHolder = null;
        mDateFormat = null;
        
        super.onDestroy();
    }

	@Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "=============onBind");
        return binder;
    }

    private final IBinder binder = new MyBinder();  
    
    public class MyBinder extends Binder { 
        public SkypeService getService() { 
            return SkypeService.this; 
        } 
    }

}
