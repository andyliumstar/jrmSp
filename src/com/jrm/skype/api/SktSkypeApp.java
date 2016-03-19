package com.jrm.skype.api;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Vector;
import android.content.Context;
import android.content.res.AssetManager;

import com.jrm.skype.util.DefaultPort;
import com.jrm.skype.util.Log;
import android.view.Surface;

import com.skype.api.Account;
import com.skype.api.Contact;
import com.skype.api.ContactSearch;
import com.skype.api.Conversation;
import com.skype.api.Message;
import com.skype.api.Participant;
import com.skype.api.Skype;
import com.skype.api.Skype.NORMALIZERESULT;
import com.skype.api.Skype.NormalizePSTNWithCountryResult;
import com.skype.api.Voicemail;
import com.skype.api.Skype.VALIDATERESULT;
import com.skype.api.Video;
import com.skype.api.Conversation.LIST_TYPE;
import com.skype.api.Conversation.LOCAL_LIVESTATUS;
import com.skype.api.Participant.VIDEO_STATUS;
import com.skype.api.Participant.VOICE_STATUS;
import com.skype.api.Skype.ValidateAvatarResult;
import com.skype.api.Video.MEDIATYPE;
import com.skype.api.Video.STATUS;
import com.skype.ipc.TCPSocketTransport;
import com.skype.ipc.TLSServerTransport;
import com.skype.ipc.Transport;
import com.skype.util.PemReader;

public class SktSkypeApp {
	private static final String TAG = "SktSkypeApp";
	private static final String SKYPEKIT_TCP_ADDRESS = "127.0.0.1";
//	private static final int SKYPEKIT_TCP_PORT = 1663;

	private static SktSkypeApp mSkypeApp = null;
	private static Object mLock = new Object();

	private Context mContext = null;
	private String mRootPath = "";
	private Skype mSkype = null;
	private boolean mLoggedIn = false; // account logged in

	private Transport mTransport = null;
	private boolean mConnected = false;
	private boolean mConnecting = false;

	private SkypeKitRunner mKitRunner = null;
	private NativeCodeCaller mNativeCaller = null;

	private updateObserver mObserver = null;
	private SktListeners mListeners = null;
	private SktAccount mAccount = null;
	private SktContact mContact = null;
	private SktConversation mConversation = null;
	private SktOption mOption = null;
	
	private com.skype.api.Video mVideoSend = null;
	private com.skype.api.Video mVideoRecv = null;
	private final String SEND_VIDEO = "local";
	private final String RECV_VIDEO = "remote";
	private int mVideoSendOid = 0;
	private int mVideoRecvOid = 0;
    
	private Surface mPlayerSurface = null;
	private Surface mPreviewSurface = null;

	private boolean mIsOutingCall = false;
	private ArrayList<SktContact.SktBuddy> mSearchBuddy = null;

	/* these events may be called in thread, so don't operate ui in these enents */
	public interface updateObserver {
		/* this event gets called when the end of the skypekit connection */
		public abstract void onSkypeKitConnectChange(boolean connected);
		
		public abstract void OnSkypeKitConnectionClosed();

		public abstract void onDefaultAccountChange(String defAccount);

		/* this event gets called when the user logged in or logged out */
		public abstract void onOnlineStatusChange(LoginStatus status, Account.LOGOUTREASON logoutReason);

		/*
		 * this event gets called when the user'availability have be changed(for
		 * example away, don't distrob, online...)
		 */
		public abstract void onAvailiblityChange(Contact.AVAILABILITY availablity);

		public abstract void onMoodTextChange();
		
		public abstract void onFullNameChange();
		
		public abstract void onBlanceChange();
		
		public abstract void onSyncStatusChange(int val);
		
		/* this event gets called when the user'avatar have be changed */
		public abstract void onAvatarChange();

		/* this event gets called when get the buddy list */
		public abstract void onGetBuddyList();

		/* this event gets called when some buddy's property have be changed */
		public abstract void onBuddyPropertyChange();

		public abstract void onAlertMessage(String message);

		/* this event gets called when the search target */
		public abstract void onSearchStatusChange(ContactSearch.STATUS status);

		/* this event gets called when new message is received */
		public abstract void onNewMessage(ConvType convType, MsgType msgType, String convName,
				String fromName, long timestamp, String body, int duration);

		/* this event gets called when the contact send a request over */
		public abstract void onReceivedAuthrequest(String actName, String text);

		/* this event gets called when the contact call me */
		public abstract void onIncomingCall(String convName, String customers[], ConvType type);

		/* this event gets called when the user join ov leave a livesession */
		public abstract void onLocalLiveStatusChange(String convName, LiveStatus liveStatus);

		/*
		 * this event gets called when the participants is changed(such as add
		 * or remove participant )
		 */
		public abstract void onParticipantsListChange(String convName, String customers[]);

		/* this event gets called when the participant's voice status is changed */
		public abstract void onVoiceStatusChange(String actName, VoiceStatus status);
		
		/* this event gets called when the participant's video status is changed */
		public abstract void onVideoStatusChange(String actName, VideoStatus status);

		/* this event gets called when the user's password is changed */
		public abstract void onPasswordChange(Account.PWDCHANGESTATUS status);

		/* this event gets called when recording or sending voicemail */
		public abstract void onVoicemailStatusChange(Voicemail.TYPE type, VoicemailStatus status, int value);
		
		/* this event gets called when the call is stopped (only set for dialogs)*/
		public abstract void onReasonForEndedLivession(EndedLivessionReason reason);
	}

	public enum LoginStatus {
		LOGGED_IN, LOGGED_OUT
	}

	public enum SwitchStatus {
		ON, OFF
	}

	public class SwitchResult {
		public boolean result;
		public SwitchStatus status;

		public SwitchResult() {
			result = false;
			status = SwitchStatus.OFF;
		}
	}

	public enum MsgType {
		CHAT, VOICEMAIL
	}

	public enum LiveStatus {
		IM_LIVE, /* voice call has been connected */
		ON_HOLD_REMOTELY,
		ON_HOLD_LOCALLY,
		RECENTLY_LIVE, /* voice call has been disconnected */
		OTHERS_ARE_LIVE,
	}

	public enum VoiceStatus {
		CONNECTING, /* connecting */
		RINGING,    /* remote is ringing */
		SPEAKING,
		HOLDED,
		STOPPED
	}
	
	public enum VideoStatus {
		AVAILABLE, /* */
		STARTING,
		RUNNING,
		PAUSED,
		STOPPED
	}

	public enum ConvType {
		DIALOG, /* 1:1 */
		CONFERENCE /* conference */
	}

	public enum VoicemailStatus {
		INITIALIZE, /* initialize voicemail recording UI */
		RECORDING, /* beging to recording voicemail */
		RECORDING_PROGRESS, /* recording progress */
		RECORDED, 
		SENDED, /* voicemail send ok */
		FAILED, /* voicemail recording or playig error */
		CANCELLED, /* cancel recording */
		BUFFERING,
		PLAYING, /* play back incoming voicemail */
		PLAYBACK_PROGRESS, /* play back progress */
		PLAYED, /* play back over */
		CANCELLED_RECORD_WHEN_INCOMING_CALL,  /* cancel recording when join a incoming call */
		CANCELLED_PLAYBACK_WHEN_INCOMING_CALL /* cancel playback when join a incoming call */
	}
	
	public enum EndedLivessionReason {
		NO_ANSWER,
		//MANUAL,
		BUSY,
		CONNECTION_DROPPED,
		//no_skypeout_subscription,
		INSUFFICIENT_FUNDS,
		//internet_connection_lost,
		//skypeout_account_blocked,
		//pstn_could_not_connect_to_skype_proxy,
		PSTN_INVALID_NUMBER,
		//pstn_number_forbidden,
		PSTN_CALL_TIMED_OUT,
		PSTN_BUSY,
		//pstn_call_terminated,
		PSTN_NETWORK_ERROR,
		//number_unavailable,
		//pstn_call_rejected,
		//pstn_misc_error,
		//INTERNAL_ERROR,
		UNABLE_TO_CONNECT,
		//recording_failed,
		//playback_error,
		//legacy_error,
		//blocked_by_privacy_settings,
		//error,
		//transfer_failed,
		//transfer_insufficient_funds,
		//blocked_by_us,
		//emergency_call_denied
	}

	public static void InitSktSkypeApp(Context ctx,	SktSkypeApp.updateObserver obs) {
		synchronized (mLock) {
			if (null == mSkypeApp) {
				mSkypeApp = new SktSkypeApp(ctx);
			}
		}
		mSkypeApp.setObserver(obs);
		mSkypeApp.start();
	}

	public static SktSkypeApp getInstance() {
		synchronized (mLock) {
			if (null == mSkypeApp) {
				Log.e(TAG, "you should init SktSkypeApp first");
			}
		}
		return mSkypeApp;
	}

	protected SktSkypeApp(Context ctx) {
		mContext = ctx;
		mSkype = new Skype();
		mLoggedIn = false;

		mTransport = null;
		mConnected = false;
		mConnecting = false;

		mKitRunner = new SkypeKitRunner(ctx);
		//mIsRunning = false;
		mNativeCaller = new NativeCodeCaller();

		mObserver = null;
		mListeners = null;
		mAccount = null;
		mContact = null;
		mConversation = null;
		mOption = null;
		
		mVideoSend = null;
		mVideoRecv = null;

		mPlayerSurface = null;
		mPreviewSurface = null;

		mIsOutingCall = false;
		mSearchBuddy = new ArrayList<SktContact.SktBuddy>();
		
		mRootPath = ctx.getFilesDir().getAbsolutePath();
		if (!mRootPath.endsWith("/")) {
			mRootPath += "/";
		}
		SktDataCfg.setAvatarPath(ctx.getDir("avatar", 0));
	}

	@Override
	protected void finalize() throws Throwable {
		Log.d(TAG, "mSkype.Close(), mSkype = null");
		super.finalize();
		disconnect();
		try {
			mSkype.Close();
			mSkype = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setObserver(SktSkypeApp.updateObserver obs) {
		mObserver = obs;
	}

	public SktSkypeApp.updateObserver getObserver() {
		return mObserver;
	}

	public Skype getSkype() {
		return mSkype;
	}

	private boolean connect() {
		if (null != mTransport) {
			return false;
		}

		if(null == mSkype){
            mSkype = new Skype();
        }
		
		boolean ret = false;
		InputStream keyInput = null;
		InputStream certInput = null;
		mListeners = new SktListeners(this);
		mTransport = new TCPSocketTransport(SKYPEKIT_TCP_ADDRESS, DefaultPort.getDefaultPort());

		try {
			AssetManager am = mContext.getAssets();
			keyInput = am.open("skype/skypekit.der");
			certInput = am.open("skype/skypekit.pem");
			
			PemReader pemreader = new PemReader(certInput, keyInput);
			X509Certificate cert = pemreader.getCertificate();
			PrivateKey pk = pemreader.getKey();
			TLSServerTransport tls = new TLSServerTransport(mTransport, cert, pk);

			mSkype.Init(tls);
			Log.d(TAG, "Connecting to skypekit");
			if (!mTransport.isConnected()) {
				Log.d(TAG, "don't connec to skypekit");
				throw new InvalidKeySpecException("don't connec, this error throw by myself");
			}
			Log.d(TAG, "Connected to skypekit");
			String version = mSkype.GetVersionString();
			Log.d(TAG, "skypekit version: " + version);
			mSkype.Start();
			//mSkype.SetInt(Skype.DISABLE_AEC, 1);
			//mSkype.SetInt(Skype.DISABLE_NOISE_SUPPRESSOR,1);
			//mSkype.SetInt(Skype.DISABLE_AGC, 1);
			//Log.d(TAG, "------------------------------------DISABLE_AEC");

			if (null != mObserver) {
				// mObserver.onDefaultAccountChange(mAccountName);
			}
			
			ret = true;
			
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			disconnect();
		} finally {
			SktUtils.closeStream(keyInput);
			SktUtils.closeStream(certInput);
		}
		
		return ret;
	}

	private void disconnect() {
		try {
			if (mListeners != null) {
				mListeners.unRegisterAllListeners();
				mListeners = null;
			}

			if (mTransport != null) {
				mTransport.disconnect();
				mConnected = false;
				mTransport = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getDefaultAccountName() {
		synchronized (mLock) {
			if (mConnected) {
				return mSkype.GetDefaultAccountName();
			}
		}
		return "";
	}

	public String[] getExistingAccounts() {
		synchronized (mLock) {
			if (mConnected) {
				return mSkype.GetExistingAccounts();
			}
		}
		return null;
	}

	/**
	 * Check whether the password have be saved.
	 * 
	 * @param actName
	 * @return
	 */
	public boolean isPwdSaved(String actName) {
		Account act = mSkypeApp.getSkype().GetAccount(actName);
		if (null == act) {
			return false;
		}

		int v = act.GetIntProperty(Account.PROPERTY.status);
		return (v == Account.STATUS.LOGGED_OUT_AND_PWD_SAVED.getId());
	}

	public synchronized void setPreviewSurface(Surface surface) {
		mPreviewSurface = surface;
	}

	public synchronized void setPlayerSurface(Surface surface) {
		mPlayerSurface = surface;
	}

	public synchronized void updateSurface() {
		if (null != mNativeCaller) {
			mNativeCaller.setVideoDisplay(mPlayerSurface);
			mNativeCaller.setPreviewDisplay(mPreviewSurface);
			mNativeCaller.initSurface();
		} else {
			Log.e(TAG, "Invalid Surface!!!");
			// assert(false);
		}
	}
	
	public void startAudioIn (){
	    if (null != mNativeCaller) {
            mNativeCaller.startAudioIn();
        } else {
            Log.e(TAG, "startAudioIn failed!!!");
        }
	}
	
	public void stopAudioIn (){
        if (null != mNativeCaller) {
            mNativeCaller.stopAudioIn();
        } else {
            Log.e(TAG, "stopAudioIn failed!!!");
        }
    }
	
	public void stopRemoteVideo (){
        if (null != mNativeCaller) {
            mNativeCaller.stopRemoteVideo();
        } else {
            Log.e(TAG, "stopRemoteVideo failed!!!");
        }
    }
	
	public void stopLocalVideo (){
        if (null != mNativeCaller) {
            mNativeCaller.stopLocalVideo();
        } else {
            Log.e(TAG, "stopLocalVideo failed!!!");
        }
    }

	public boolean start() {
		synchronized (mLock) {
			if (mConnected) {
				Log.d(TAG, "start(): has conntected!");
				if (null != mObserver) {
					mObserver.onSkypeKitConnectChange(mConnected);
				}
				return true;
			}
			if (mConnecting) {
				Log.d(TAG, "start(): is conntecting...");
				return false;
			}

			if (null != mKitRunner) {
				mConnecting = true;

				Thread thread = new Thread() {
					@Override
					public void run() {
						super.run();

						if (!checkPemFiles())
						{
							if (null != mObserver) {
								mObserver.onSkypeKitConnectChange(false);
								return;
							}
						}
						mNativeCaller.initAVHostsEx(0);
						mKitRunner.startApp(false);
						mNativeCaller.startAVHosts();

						mConnected = connect();
						if (null != mObserver) {
							mObserver.onSkypeKitConnectChange(mConnected);
						}
						mConnecting = false;
					}
				};
				thread.start();

				// try {
				// thread.join();
				// }catch(InterruptedException e) {
				// e.printStackTrace();
				// }

				// }

			}
		}
		return true;
	}

	public boolean stop() {
		synchronized (mLock) {
			if (mConnecting) {
				Log.d(TAG, "stop(): is conntecting, wait a moment");
				return false;
			}
			if (mConnected) {
				disconnect();
				mKitRunner.stopApp();
				mNativeCaller.stopAVHosts();
				mConnected = false;
			}
		}
		mSkypeApp = null;
		return true;
	}

	public synchronized boolean isLoggedIn() {
		return mLoggedIn;
	}

	public void reStartKit(){
	    if(null != mNativeCaller){
            mNativeCaller.stopAVHosts();
            Log.d(TAG, "mNativeCaller.stopAVHosts()");
        }
	    
	    disconnect();
	    
        try {
            mSkype.Close();
            mSkype = null;
            Log.d(TAG, "mSkype.Close()");
        } catch (IOException e) {
            e.printStackTrace();
        }
	    
	    if(null != mKitRunner){
            mKitRunner.stopApp(); 
            Log.d(TAG, " mKitRunner.stopApp()");
        }
        
	   
	    synchronized (mLock) {
            if (mConnecting) {
                Log.d(TAG, "start(): is conntecting...");
                return ;
            }

            if (null != mKitRunner) {
                mConnecting = true;

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "mKitRunner.startApp(false)...");
                        mNativeCaller.initAVHostsEx(0);
                        mKitRunner.startApp(false);
                        mNativeCaller.startAVHosts();

                        mConnected = connect();
                        if (null != mObserver) {
                            mObserver.onSkypeKitConnectChange(mConnected);
                        }
                        mConnecting = false;
                    }
                };
                thread.start();
            }
        }
	}
	
	// Interface for Account
	public SktAccount getAccount() {
		synchronized (mLock) {
			if (!mConnected) {
				return null;
			}
			if (null == mAccount) {
				mAccount = new SktAccount(this);
			}
			return mAccount;
		}
	}

	/**
	 * get active accout. <br>
	 * 
	 * @return SktAccount
	 */
	public SktAccount getActiveAccount() {
		synchronized (mLock) {
			return mAccount;
		}
	}

	/**
	 * set active accout. <br>
	 * 
	 * @return SktAccount
	 */
	public void setActiveAccount(SktAccount act) {
		synchronized (mLock) {
			mAccount = act;
		}
	}

	public SktContact getContact() {
		synchronized (mLock) {
			if (!mConnected) {
				return null;
			}
			if (null == mContact) {
				mContact = new SktContact(this);
			}
			return mContact;
		}
	}

	public SktConversation getConversation() {
		synchronized (mLock) {
			if (!mConnected) {
				return null;
			}
			if (null == mConversation) {
				mConversation = new SktConversation(this);
			}
			return mConversation;
		}
	}

	public SktOption getOption() {
		synchronized (mLock) {
			if (!mConnected) {
				return null;
			}
			if (null == mOption) {
				mOption = new SktOption(this);
			}
			return mOption;
		}
	}
	
	public Skype.ValidateProfileStringResult checkProfileString(int propkey, String value, boolean forRegistration)
	{
		if (null == mSkype)
		{
			return null;
		}
		return mSkype.ValidateProfileString(propkey, value, forRegistration);
	}

	public Skype.ValidateProfileStringResult checkSkypeName(String actName, boolean forRegistration) {
		return checkProfileString(Contact.PROPERTY.skypename.getId(), actName, forRegistration);
	}

	public Skype.ValidateProfileStringResult checkEmail(String email, boolean forRegistration) {
		return checkProfileString(Contact.PROPERTY.emails.getId(), email, forRegistration);
	}

	public Skype.VALIDATERESULT checkPassword(String actName, String pwd) {
		if (null == mSkype) {
			return null;
		}
		return mSkype.ValidatePassword(actName, pwd);
	}

	public ValidateAvatarResult checkAvatar(byte[] value) {
		if (null == mSkype) {
			return null;
		}
		return mSkype.ValidateAvatar(value);
	}

    public Skype.NormalizePSTNWithCountryResult checkPSTNNumber(String original,
            String countryPrefix) {
        if (null == original || 0 == original.length() || null == countryPrefix
                || 0 == countryPrefix.length()) {
            return null;
        }

        try {
            int countryCode = Integer.parseInt(countryPrefix.replace("+", ""));
            return mSkype.NormalizePSTNWithCountry(original, countryCode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	public boolean checkPSTNNumberBool(String original, String countryPrefix) {
		NormalizePSTNWithCountryResult mResult = checkPSTNNumber(original, countryPrefix);

		if (null != mResult && mResult.result == NORMALIZERESULT.IDENTITY_OK)
			return true;
		else
			return false;
	}

	/**
	 * Get result of check profile
	 * 
	 * @param result
	 * <br>
	 *            check result
	 * @return true, success; other return false;
	 */
	public boolean checkProfileSucces(Skype.ValidateProfileStringResult result) {
		return (result.result == VALIDATERESULT.VALIDATED_OK);
	}

	private synchronized void fetchAvailableVideo(Participant p) {
		if (null == mConversation) {
			return;
		}

		Conversation conv = mConversation.getConvCall();
		if (null == conv) {
			return;
		}

		Conversation.TYPE ctype = Conversation.TYPE.get(conv.GetIntProperty(Conversation.PROPERTY.type));
		if (ctype != Conversation.TYPE.DIALOG) {
			Log.d(TAG, "Selected conversation is not 1-1 dialog call");
			return;
		}

		Participant[] myself = conv.GetParticipants(Conversation.PARTICIPANTFILTER.MYSELF);
		Participant[] participants = conv.GetParticipants(Conversation.PARTICIPANTFILTER.OTHER_CONSUMERS);
		
		assert (myself != null);
		assert (myself.length == 1);
		assert (participants != null);
		assert (participants.length > 0);
		
		
		fetchLocalVideo(p,myself[0]);
		fetchRemoteVideo(p,participants[0],myself[0]);
 
	}

	private void fetchLocalVideo(Participant p,Participant dst){
	    if(null == p || null == dst){
	        return;
	    }
	    
        if (p == dst) {
            Video v = dst.GetVideo();
            
            mVideoSend = v;
            mVideoSendOid = 0;
            
            if (null != v) {
                mVideoSendOid = v.getOid();
            }
            Log.d(TAG,"fetchLocalVideo-------->");
        }
	}
	
	private void fetchRemoteVideo(Participant p,Participant dst,Participant local){
	    if(null == p || null == dst){
            return;
        }
	    
        if (p == dst) {
            Video v = dst.GetVideo();
            
            mVideoRecv = v;
            mVideoRecvOid = 0;
            
            if (null != v){
                mVideoRecvOid = v.getOid();
            }
            Log.d(TAG,"fetchRemoteVideo-------->");
            
           //only remote video need this for losing onVideoStatusChange(Video v, STATUS status)
            if(null != mObserver){
                mObserver.onVideoStatusChange(RECV_VIDEO, VideoStatus.AVAILABLE);
            }
        }
        
        if(null == mVideoSend){
            Log.d(TAG, "-------------------------------1111111111111111111111---------------------------------->fetchLocalVideo(local,local)");
            fetchLocalVideo(local,local);
        }
    }
	
	protected boolean startSendVideo() {
		synchronized (this) {
			if (null != mVideoSend) {
				int code = mVideoSend.GetIntProperty(Video.PROPERTY.status);
				Video.STATUS status = Video.STATUS.get(code);
				if (Video.STATUS.AVAILABLE == status) {
					Log.d(TAG, "startSendVideo");
					mVideoSend.Start();
					return true;
				}
			}else{
			    Log.d(TAG, "startSendVideo------>false"); 
			}
		}
		
		return false;
	}
	
    protected void startSendVideoForce() {
        synchronized (this) {
            if (null != mVideoSend) {
                Log.d(TAG, "startSendVideo----------------->force");
                mVideoSend.Start();
            }
        }
    }
	

	protected void stopSendVideo() {
		synchronized (this) {
			if (null != mVideoSend) {
				Log.d(TAG, "stopSendVideo");
				mVideoSend.Stop();
			}
		}
	}
	
	protected boolean startRecvVideo() {
		synchronized (this) {
			if (null != mVideoRecv) {
				int code = mVideoRecv.GetIntProperty(Video.PROPERTY.status);
				Video.STATUS status = Video.STATUS.get(code);
				if (Video.STATUS.AVAILABLE == status) {
					Log.d(TAG, "startRecvVideo");
					mVideoRecv.Start();
					return true;
				}
			}
		}
		return false;
	}

	protected void stopRecvVideo() {
		synchronized (this) {
			if (null != mVideoRecv) {
				Log.d(TAG, "stopRecvVideo");
				mVideoRecv.Stop();
			}
		}
	}
	
	/**
	 * get video status
	 * @param whichVideo <br> 1: send video; 2: recv video
	 * @return video status
	 */
	protected Video.STATUS getVideoStatus(int whichVideo) {
		int code = 0;
		Video.STATUS status = Video.STATUS.NOT_AVAILABLE;
		synchronized (this) {
			
			if (1 == whichVideo && null != mVideoSend) {
				code = mVideoSend.GetIntProperty(Video.PROPERTY.status);
				status = Video.STATUS.get(code);
			}
			
			if (2 == whichVideo && null != mVideoRecv) {
				code = mVideoRecv.GetIntProperty(Video.PROPERTY.status);
				status = Video.STATUS.get(code);
			}
		}
		return status;
	}

	protected void onGetBuddyList() {
		Log.d(TAG, "onGetBuddyList");
		mObserver.onGetBuddyList();
	}

	protected void onBuddyPropertyChange() {
		mObserver.onBuddyPropertyChange();
	}

	protected void updateContactAvailability(String actName, Contact.AVAILABILITY availability) {
		SktContact contact = getContact();
		if (null != contact) {
			boolean ret = contact.updateContactAvailability(actName, availability);
			Log.d(TAG, "updateContactAvailability: = " + ret);
			if (ret) {
				onBuddyPropertyChange();
			}
		}
	}

	protected void updateContactAvatar(String actName, byte[] avatar) {
		SktContact contact = getContact();
		if (null != contact) {
			boolean ret = contact.updateContactAvatar(actName, avatar);
			if (ret) {
				onBuddyPropertyChange();
			}
		}
	}

	protected void updateContactFullname(String actName, String fullname) {
		SktContact contact = getContact();
		if (null != contact) {
			boolean ret = contact.updateContactFullname(actName, fullname);
			if (ret) {
				onBuddyPropertyChange();
			}
		}
	}

	protected void updateContactDisplayname(String actName, String displayname) {
		SktContact contact = getContact();
		if (null != contact) {
			boolean ret = contact.updateContactDisplayname(actName, displayname);
			if (ret) {
				onBuddyPropertyChange();
			}
		}
	}

	protected void updateContactMoodtext(String actName, String mood) {
		SktContact contact = getContact();
		if (null != contact) {
			boolean ret = contact.updateContactMoodtext(actName, mood);
			if (ret) {
				onBuddyPropertyChange();
			}
		}
	}

	protected void receivedAuthrequest(String actName, String text) {
		Log.d(TAG, "receivedAuthrequest: " + actName + ", " + text);
		mObserver.onReceivedAuthrequest(actName, text);
	}

	protected void updateParticipants(Conversation conv) {
		Vector<String> names = new Vector<String>();
		Participant[] parts = conv.GetParticipants(Conversation.PARTICIPANTFILTER.OTHER_CONSUMERS);
		for (int i = 0; i < parts.length; i++) {
			names.add(parts[i].GetStrProperty(Participant.PROPERTY.identity));
		}
		mObserver.onParticipantsListChange(
				conv.GetStrProperty(Conversation.PROPERTY.identity),
				names.toArray(new String[names.size()]));
		Log.d(TAG, "updateParticipants, conv = " + conv.getOid());
	}

	protected void updateAccountAvatar(int oid) {
		if (null != mAccount && mAccount.getAccount().getOid() == oid) {
			mObserver.onAvatarChange();
		}
	}

	// Interface for Linstener callback
	protected void onAccountStatusChange(Account account,
			com.skype.api.Account.PROPERTY prop, Object value) {

		if (null == mAccount || null == account
				|| mAccount.getOid() != account.getOid()) {
			Log.d(TAG, "Invalid accountStatusChange event..");
			return;
		}
		
		switch (prop) {
		case status: {
			Account.GetStatusWithProgressResult proress = account.GetStatusWithProgress();
			switch (proress.status) {
			case LOGGED_IN:
				synchronized (this) {
					mLoggedIn = true;
				}
				if (null != mObserver) {
					mObserver.onOnlineStatusChange(LoginStatus.LOGGED_IN, null);
					mObserver.onAvailiblityChange(null);
					mObserver.onMoodTextChange();
					mObserver.onBlanceChange();
					mObserver.onFullNameChange();
				}
				
				/* auto get contact list */
				getContact().requestBuddies();
				break;
			case LOGGED_OUT:
			case LOGGED_OUT_AND_PWD_SAVED:
				synchronized (this) {
					mLoggedIn = false;
				}
				if (null != mObserver) {
					int code = account.GetIntProperty(Account.PROPERTY.logoutreason);
					mObserver.onOnlineStatusChange(LoginStatus.LOGGED_OUT, Account.LOGOUTREASON.get(code));
				}
				onLogout();
				break;
			}
		}
		break;
		
		case logoutreason: {
			int i = (Integer) (value);
			Log.d(TAG, "ACCOUNT:LOGOUTREASON = " + Account.LOGOUTREASON.get(i));

			// If the credentials are bad, die immediately
			if (i == Account.LOGOUTREASON.INVALID_APP_ID.getId()) {
				// mObserver.onAlertMessage("Fatal error: application credential failure.");
				Log.d(TAG, "Fatal error: application credential failure.");
				// System.exit(1);
			}
		}
		break;
		
		case availability: {
		    Log.d(TAG, "ACCOUNT:availability = " + prop);
			if (null != mObserver) {
				int code = account.GetIntProperty(prop);
				Contact.AVAILABILITY availablity = Contact.AVAILABILITY.get(code);
				mObserver.onAvailiblityChange(availablity);
			}
		}
		break;
		
		case avatar_timestamp:
			mAccount.updateLocalAvatar(null);
			break;
		
		case fullname:
		    if (null != mObserver) {
                mObserver.onFullNameChange();
            }
		    break;

		case mood_text:
		    if (null != mObserver) {
                mObserver.onMoodTextChange();
            }
            break;
		    
		case skypeout_balance:
		    if (null != mObserver) {
                mObserver.onBlanceChange();
            }
            break;
		    
		case pwdchangestatus: {
			if (null != mObserver) {
				int code = account.GetIntProperty(prop);
				Account.PWDCHANGESTATUS pwdStatus = Account.PWDCHANGESTATUS.get(code);
				mObserver.onPasswordChange(pwdStatus);
			}
		}
		break;
		
		case cblsyncstatus: {
			if (value instanceof Integer) {
				Integer val = (Integer)value;
				mAccount.setSyncStatus(val);
			}
		}
		break;
		
		default:
			break;
		}
	}

	protected void onLocalLiveStatusChange(Conversation conv, LOCAL_LIVESTATUS status) {
		String convName = conv.GetStrProperty(Conversation.PROPERTY.identity);
		Log.d(TAG, "livestatus change: status = " + status);

		switch (status) {
		case STARTING:
			Log.d(TAG, "STARTING");
			break;
		case IM_LIVE:
			Log.d(TAG, "IM_LIVE");
//			 fetchAvailableVideo(null);
			processOnLiveChange(conv, LiveStatus.IM_LIVE);
			break;
		case OTHERS_ARE_LIVE:
			Log.d(TAG, "OTHERS_ARE_LIVE");
			processOnLiveChange(conv, LiveStatus.OTHERS_ARE_LIVE);
			break;
		case ON_HOLD_REMOTELY:
			Log.d(TAG, "ON_HOLD_REMOTELY");
			processOnLiveChange(conv, LiveStatus.ON_HOLD_REMOTELY);
			break;
		case ON_HOLD_LOCALLY:
			Log.d(TAG, "ON_HOLD_LOCALLY");
			processOnLiveChange(conv, LiveStatus.ON_HOLD_LOCALLY);
			break;
		case RINGING_FOR_ME:
			Log.d(TAG, "RINGING_FOR_ME, convid = " + conv.getOid());
			getConversation().cacheConvs(convName, conv);
			
			if (mConversation.checkIncomingCall(conv)) {
				
				Log.d(TAG, "Notify ui that have a incomeing call");
				
				Vector<String> names = new Vector<String>();
				Participant[] parts = conv.GetParticipants(Conversation.PARTICIPANTFILTER.OTHER_CONSUMERS);
				for (int i = 0; i < parts.length; i++) {
					names.add(parts[i].GetStrProperty(Participant.PROPERTY.identity));
				}
				
				ConvType convType = ConvType.DIALOG;
				int code = conv.GetIntProperty(Conversation.PROPERTY.type);
				Conversation.TYPE type = Conversation.TYPE.get(code);
				switch (type) {
				case DIALOG:
					convType = ConvType.DIALOG;
					break;
				case CONFERENCE:
				case TERMINATED_CONFERENCE:
				case LEGACY_VOICE_CONFERENCE:
					convType = ConvType.CONFERENCE;
					break;
				default:
					break;
				}

				mConversation.setIncomingCall(conv);
				mObserver.onIncomingCall(convName, names.toArray(new String[names.size()]), convType);
			}
			break;
			
		case RECENTLY_LIVE:
			Log.d(TAG, "RECENTLY_LIVE");
			processOnLiveChange(conv, LiveStatus.RECENTLY_LIVE);
			break;
		case NONE:
		    Log.d(TAG, "NONE");
		    //when i am blocked, send voicemail will failed and i only get this message.
		  //  mObserver.onVoicemailStatusChange(Voicemail.TYPE.OUTGOING, VoicemailStatus.FAILED, 0);
		    break;
		default:
			break;
		}
	}

	protected void onVideoErrorChange(Video v, String error) {
	}

	protected void onVideoMediaTypeChange(Video v, MEDIATYPE mediatype) {
	}

	protected void onVideoStatusChange(Video v, STATUS status) {
        String whichVideo = "";
        if (v == mVideoSend) {
            whichVideo = SEND_VIDEO;
            Log.d(TAG, "Send video status: " + status);
        } else if (v == mVideoRecv) {
            whichVideo = RECV_VIDEO;
            Log.d(TAG, "Recv video status: " + status);
        }

        if (whichVideo.isEmpty()) {
            int oid = v.getOid();
            if (oid == mVideoSendOid) {
                whichVideo = SEND_VIDEO;
                Log.d(TAG, "Send video status: " + status);
            } else if (oid == mVideoRecvOid) {
                whichVideo = RECV_VIDEO;
                Log.d(TAG, "Recv video status: " + status);
            }
        }
		
		switch (status) {
		case NOT_AVAILABLE:
			Log.d(TAG, "NOT_AVAILABLE");
			break;
		case AVAILABLE:
			Log.d(TAG, "AVAILABLE: " + whichVideo);
			mObserver.onVideoStatusChange(whichVideo, VideoStatus.AVAILABLE);
			break;
		case STARTING:
			Log.d(TAG, "STARTING");
			mObserver.onVideoStatusChange(whichVideo, VideoStatus.STARTING);
			break;
		case REJECTED:
			Log.d(TAG, "REJECTED");
			break;
		case RUNNING:
			Log.d(TAG, "RUNNING: " + whichVideo);
			mObserver.onVideoStatusChange(whichVideo, VideoStatus.RUNNING);
			break;
		case STOPPING:
			Log.d(TAG, "STOPPING: " + whichVideo);
			mObserver.onVideoStatusChange(whichVideo, VideoStatus.STOPPED);
			break;
		case PAUSED:
			Log.d(TAG, "PAUSED: " + whichVideo);
			mObserver.onVideoStatusChange(whichVideo, VideoStatus.PAUSED);
			break;
		case NOT_STARTED:
			Log.d(TAG, "NOT_STARTED");
			break;
		case HINT_IS_VIDEOCALL_RECEIVED:
			Log.d(TAG, "HINT_IS_VIDEOCALL_RECEIVED");
			break;
		case UNKNOWN:
			Log.d(TAG, "UNKNOWN");
			break;
		case RENDERING:
			Log.d(TAG, "RENDERING");
			break;
		case CHECKING_SUBSCRIPTION:
			Log.d(TAG, "CHECKING_SUBSCRIPTION");
			break;
		case SWITCHING_DEVICE:
			Log.d(TAG, "SWITCHING_DEVICE");
			break;
		}
	}

	protected void onVideoStatusChange(Participant obj, VIDEO_STATUS videoStatus) {
		String actName = obj.GetStrProperty(Participant.PROPERTY.identity);
		if (null == actName) {
			return;
		}
		Log.d(TAG, "Participant videoStatus:"+videoStatus);
		switch (videoStatus) {
		case VIDEO_UNKNOWN:
			//mObserver.onVideoStatusChange(actName, VideoStatus.STOPPED);
			break;
		case VIDEO_NA:
			break;
		case VIDEO_AVAILABLE:
			fetchAvailableVideo(obj);
			break;
		case VIDEO_CONNECTING:
			//mObserver.onVideoStatusChange(actName, VideoStatus.CONNECTING);
			break;
		case STREAMING:
			//mObserver.onVideoStatusChange(actName, VideoStatus.SENDING);
			break;
		case VIDEO_ON_HOLD:
			//mObserver.onVideoStatusChange(actName, VideoStatus.HOLDED);
			break;
		}
	}

	protected void onConversationListChange(Conversation conversation,	LIST_TYPE type, boolean added) {
		/*
		 * mIncomingCallConversation = null; String identity =
		 * conversation.GetStrProperty(Conversation.PROPERTY.displayname);
		 * Integer status =
		 * conversation.GetIntProperty(Conversation.PROPERTY.local_livestatus);
		 * if (added) { Log("onconversationListChange added '" + identity +
		 * "' status:" + status.toString()); } else {
		 * Log("onconversationListChange '" + identity + "' status:" +
		 * status.toString()); } if (Conversation.LOCAL_LIVESTATUS.get(status)
		 * == Conversation.LOCAL_LIVESTATUS.RINGING_FOR_ME) {
		 * mIncomingCallConversation = conversation; changeState(new
		 * CallRingingState()); }
		 */
	}

	protected void onVoiceStatusChange(Participant p,VOICE_STATUS voiceSTATUS) {
		String actName = p.GetStrProperty(Participant.PROPERTY.identity);
		Log.d(TAG, "onParticipantVoiceStatusChange: " + actName + ": "
				+ voiceSTATUS);
		switch (voiceSTATUS) {
		case VOICE_UNKNOWN:
			break;
		case VOICE_NA:
			break;
		case VOICE_AVAILABLE:
		case VOICE_CONNECTING:
		case EARLY_MEDIA:
		case LISTENING:
			Log.d(TAG, "VoiceStatus: Connecting:" + actName);
			mObserver.onVoiceStatusChange(actName, VoiceStatus.CONNECTING);
			break;
		case RINGING:
			mObserver.onVoiceStatusChange(actName, VoiceStatus.RINGING);
			break;
		case SPEAKING:
			Log.d(TAG, "VoiceStatus: Speaking:" + actName);
			mObserver.onVoiceStatusChange(actName, VoiceStatus.SPEAKING);
			break;
		case VOICE_STOPPED:
			Log.d(TAG, "VoiceStatus: Stopped:" + actName);
			mObserver.onVoiceStatusChange(actName, VoiceStatus.STOPPED);
			break;
		case VOICE_ON_HOLD:
			mObserver.onVoiceStatusChange(actName, VoiceStatus.HOLDED);
			break;
		default:
			break;
		}
	}

	protected void onVideoDimensionsChange(Video v, String value) {

		if (v == mVideoSend) {
			Log.d(TAG, "Send video dimensions change:" + value);
		}
		if (v == mVideoRecv) {
			Log.d(TAG, "Recv video dimensions change:" + value);
		}
	}

	protected void onNewSearchResult(ContactSearch search, Contact contact) {

		if (null != contact) {
			mContact.addToSearchList(search, contact);
		}
	}

	protected void onSearchStatusChange(ContactSearch search,ContactSearch.PROPERTY prop) {
		if (prop != ContactSearch.PROPERTY.contact_search_status) {
			return;
		}

		int oid = -1;
		if (null != mContact) {
			ContactSearch cs = mContact.getCurrentContactSearch();
			if (null != cs) {
				oid = cs.getOid();
			}
		}
		if (oid != search.getOid()) {
			return;
		}

		int code = search.GetIntProperty(prop);
		ContactSearch.STATUS status = ContactSearch.STATUS.get(code);
		if (null != mObserver) {
			mObserver.onSearchStatusChange(status);
		}
	}

	protected void onNewMessage(MsgType msgType, String convName,
			String fromName, long timeStamp, String body, int duration, 
			ConvType convType, Conversation conv) {
		getConversation().cacheConvs(convName, conv);
		
		if (null != mObserver) {
			mObserver.onNewMessage(convType, msgType, convName, fromName, 
					timeStamp, body, duration);
		}
	}

	private void processOnLiveChange(Conversation conversation,	LiveStatus status) {
		int convOid = -1;
		if (null != mConversation) {
			convOid = mConversation.getConvCallOid();
			Log.d(TAG, "processOnLiveChange: oldconvOid = " + convOid);
			Log.d(TAG, "processOnLiveChange: convOid = " + conversation.getOid());
		}

        if (convOid == conversation.getOid()) {
            String convName = conversation.GetStrProperty(Conversation.PROPERTY.identity);
            mObserver.onLocalLiveStatusChange(convName, status);

            if (LiveStatus.RECENTLY_LIVE == status || LiveStatus.OTHERS_ARE_LIVE == status) {
                mConversation.setIncomingCall(null);
                mVideoSend = null;
                mVideoRecv = null;
            }
        }
	}

	protected void onRecodingVmStatusChange(Conversation conv) {
		int vmId = conv.GetIntProperty(Conversation.PROPERTY.active_vm_id);
		if (vmId <= 0) {
			return;
		}
		Voicemail vm = mSkype.GetVoiceMailFromId(vmId, mSkype);
		if (null == vm) {
			return;
		}
		Voicemail.TYPE type = Voicemail.TYPE.get(vm.GetIntProperty(Voicemail.PROPERTY.type));
		switch (type) {
		case INCOMING:
			break;
		case DEFAULT_GREETING:
		case CUSTOM_GREETING:
			Log.d(TAG, "voicemail greeting..., vid = " + vm.getOid() + " , convid = " + conv.getOid());
			getConversation().setOutgoingVm(conv, vm);
			mObserver.onVoicemailStatusChange(type, VoicemailStatus.INITIALIZE, 0);
			break;
		case OUTGOING:
			Log.d(TAG, "voicemail Redording..., vid = " + vm.getOid() + " , convid = " + conv.getOid());
			getConversation().setOutgoingVm(conv, vm);
			mObserver.onVoicemailStatusChange(type, VoicemailStatus.RECORDING, 0);
			break;
		}
	}

	protected void onVmStatusChange(Voicemail vm, Voicemail.PROPERTY prop, int value) {
		
		int val = 0;
		int typeId = vm.GetIntProperty(Voicemail.PROPERTY.type);
		Voicemail.TYPE type = Voicemail.TYPE.get(typeId);
		
		Log.d(TAG, "onVmStatusChange: " + vm.getOid() + " , prop = " + prop + " , value = "+ value);
		switch (prop) {
		case status:
			Voicemail.STATUS mystatus = Voicemail.STATUS.get(value);
			Log.d(TAG, "voicemail status = " + mystatus);
			switch (mystatus) {
			case PLAYING:
				switch (type) {
				case INCOMING:
				case OUTGOING:
					val = vm.GetIntProperty(Voicemail.PROPERTY.duration);
					mObserver.onVoicemailStatusChange(type, VoicemailStatus.PLAYING, val);
					break;
				case CUSTOM_GREETING:
					break;
				case DEFAULT_GREETING:
					break;
				}

				break;

			case PLAYED:
				if (Voicemail.TYPE.INCOMING == type || Voicemail.TYPE.OUTGOING == type) {
					mObserver.onVoicemailStatusChange(type, VoicemailStatus.PLAYED, 0);
				}
				break;
				
			case RECORDED:
				Log.d(TAG, "voicemail recorded...");
				val = vm.GetIntProperty(Voicemail.PROPERTY.duration);
				mObserver.onVoicemailStatusChange(type, VoicemailStatus.RECORDED, val);
				getConversation().setOutgoingVm(null, null);
				break;

			case UPLOADED:
				Log.d(TAG, "voicemail sended...");
				val = vm.GetIntProperty(Voicemail.PROPERTY.duration);
				mObserver.onVoicemailStatusChange(type, VoicemailStatus.SENDED, val);
				getConversation().setOutgoingVm(null, null);
				break;

			case FAILED:
				Log.d(TAG, "voicemail FAILED...");
				mObserver.onVoicemailStatusChange(type, VoicemailStatus.FAILED, 0);
				getConversation().setOutgoingVm(null, null);
				break;

			case CANCELLED:
				Log.d(TAG, "voicemail cancelled...");
				mObserver.onVoicemailStatusChange(type, VoicemailStatus.CANCELLED, 0);
				getConversation().setOutgoingVm(null, null);
				break;
				
			case BUFFERING:
				Log.d(TAG, "voicemail buffering...");
				mObserver.onVoicemailStatusChange(type, VoicemailStatus.BUFFERING, 0);
				break;

			default:
				break;
			}
			break;

		case playback_progress:
			if (Voicemail.TYPE.INCOMING == type) {
				mObserver.onVoicemailStatusChange(type, VoicemailStatus.PLAYBACK_PROGRESS, value);
			}
			break;

		case failurereason:
			val = vm.GetIntProperty(Voicemail.PROPERTY.failurereason);
			mObserver.onVoicemailStatusChange(type, VoicemailStatus.FAILED, val);
			Log.d(TAG, "voicemail FAILED... " + val);
			break;

		case duration:
			if (Voicemail.TYPE.OUTGOING == type) {
				mObserver.onVoicemailStatusChange(type, VoicemailStatus.RECORDING_PROGRESS, value);
			}
			break;

		default:
			break;
		}
	}

	public synchronized void onLogout() {
		
		if (null != mContact) {
			mContact.clear();
		}
		if (null != mSearchBuddy) {
			mSearchBuddy.clear();
		}
		
		mAccount = null;
		mContact = null;
		mConversation = null;
		mOption = null;
		mLoggedIn = false;
	}

	private boolean checkPemFiles() {
		
		String checkFiles = "linux-armv7-skypekit-voicepcm-videortp";
		if (SktUtils.fileExists(mRootPath + checkFiles)) {
			return true;
		}

		// create path
		SktUtils.mkdirs(mRootPath);

		// copy files
		InputStream input = null;
		OutputStream output = null;
		

		try {
			byte[] buffer = new byte[4096];
			AssetManager am = mContext.getAssets();
			input = am.open("skype/" + checkFiles);
			output = new FileOutputStream(mRootPath + checkFiles);
			if (null != input && null != output) {
				int bytesRead = 0;
				while ((bytesRead = input.read(buffer)) != -1) {
					output.write(buffer, 0, bytesRead);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			SktUtils.closeStream(input);
			SktUtils.closeStream(output);
			input = null;
			output = null;
		}

		// set executable
		SktUtils.setFileExecutable(mRootPath + checkFiles, true);

		// sync;
		SktUtils.sleep(50);
		SktUtils.exec("sync;");
		SktUtils.sleep(50);
		
		return SktUtils.fileExists(mRootPath + checkFiles);
	}
	
	protected void onSkypeKitStoped() {
	    if(null != mObserver){
	        mObserver.onSkypeKitConnectChange(false);
	    }
	}
	
	protected void OnSkypeKitConnectionClosed() {
	    if(null != mObserver){
	        mObserver.OnSkypeKitConnectionClosed();
	    }
    }
	
	protected void onStartedLiveSession(Message message) {
		mIsOutingCall = false;
		String author = message.GetStrProperty(Message.PROPERTY.author);
		if (null != author && null != mAccount) {
			String actName = mAccount.getAccountName();
			if (0 == author.compareTo(actName)) {
				mIsOutingCall = true;
			}
		}
	}
	
	protected void onEndedLiveSession(Message message) {
		
		if (!mIsOutingCall) {
			return;
		}
		
		String strErr = message.GetStrProperty(Message.PROPERTY.reason);
		if (null == strErr || strErr.isEmpty()) {
			return;
		}
		Log.d(TAG, "reason = " + strErr);
		
		EndedLivessionReason reason = EndedLivessionReason.BUSY;
		
		do {
			if (strErr.compareTo("no_answer") == 0) {
				reason = EndedLivessionReason.NO_ANSWER;
				break;
			}

			if (strErr.compareTo("manual") == 0 ||
				strErr.compareTo("busy") == 0) {
				reason = EndedLivessionReason.BUSY;
				break;
			}
			
			if (strErr.compareTo("connection_dropped") == 0 ||
				strErr.compareTo("internet_connection_lost") == 0) {
				reason = EndedLivessionReason.CONNECTION_DROPPED;
				break;
			}

			if (strErr.compareTo("insufficient_funds") == 0 || strErr.compareTo("no_skypeout_subscription") == 0) {
				reason = EndedLivessionReason.INSUFFICIENT_FUNDS;
				break;
			} 
			
			if (strErr.compareTo("pstn_invalid_number") == 0) {
				reason = EndedLivessionReason.PSTN_INVALID_NUMBER;
				break;
			} 
			
			if (strErr.compareTo("pstn_call_timed_out") == 0) {
				reason = EndedLivessionReason.PSTN_CALL_TIMED_OUT;
				break;
			} 
			
			if (strErr.compareTo("pstn_busy") == 0) {
				reason = EndedLivessionReason.PSTN_BUSY;
				break;
			}
			
			if (strErr.compareTo("pstn_could_not_connect_to_skype_proxy") == 0 ||
				strErr.compareTo("pstn_number_forbidden") == 0 ||
				strErr.compareTo("pstn_call_terminated") == 0 ||
				strErr.compareTo("pstn_network_error") == 0 ||
				strErr.compareTo("number_unavailable") == 0 ||
				strErr.compareTo("pstn_call_rejected") == 0 ||
				strErr.compareTo("pstn_misc_error") == 0  ) {
				reason = EndedLivessionReason.PSTN_NETWORK_ERROR;
				break;
			}
			
			if (strErr.compareTo("unable_to_connect") == 0) {
				reason = EndedLivessionReason.UNABLE_TO_CONNECT;
				break;
			}
		} while (false);
		
		mObserver.onReasonForEndedLivession(reason);
	}
	
	protected void cancelVoicemailWhenIncomingcall(VoicemailStatus status) {
		mObserver.onVoicemailStatusChange(Voicemail.TYPE.DEFAULT_GREETING, status, 0);
	}
	
	protected void onSyncStatusChange(int val) {
		mObserver.onSyncStatusChange(val);
	}
}