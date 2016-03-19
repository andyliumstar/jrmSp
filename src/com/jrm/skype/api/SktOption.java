package com.jrm.skype.api;

import java.util.ArrayList;
import java.util.Calendar;
import com.skype.api.Account;
import com.skype.api.Conversation;
import com.skype.api.Message;
import com.skype.api.Skype;
import com.skype.api.Video;
import com.jrm.skype.util.Log;

public class SktOption {
	private static final String TAG = "SktOption";
	
	private SktSkypeApp mSkypeApp;
	private Skype.GetISOCountryInfoResult mIsoCountry;
	private Skype.GetISOLanguageInfoResult mIsoLanguage;
	
	public enum HISTORY_SAVE_POLICY {
		NO_HISTORY,
		TWO_WEEKS,
		ONE_MONTH,
		THREE_MONTHS,
		FOREVER	
	}
	
		
	public SktOption(SktSkypeApp skypeApp)
	{
		mSkypeApp = skypeApp;
		mIsoCountry = null;
		mIsoLanguage = null;
	}
	
	private boolean isValid()
	{
		if (null == mSkypeApp)
		{
			Log.d(TAG, "isValid: null == mSkypeApp");
			return false;
		}
		
		return mSkypeApp.isLoggedIn();
	}
	
	public Account.SKYPECALLPOLICY getSkypeCallPolicy()
	{
		if (!isValid()) {
			return null;
		}
		SktAccount sAccount = mSkypeApp.getAccount();
		if (null == sAccount) {
			return null;
		}
		Account account = sAccount.getAccount();
		if (null == account) {
			return null;
		}
		
		int code = account.GetIntProperty(Account.PROPERTY.skype_call_policy);
		return Account.SKYPECALLPOLICY.get(code);
	}
	
	public boolean setSkypeCallPolicy(Account.SKYPECALLPOLICY policy)
	{
		if (!isValid()) {
			return false;
		}
		SktAccount sAccount = mSkypeApp.getAccount();
		if (null == sAccount) {
			return false;
		}
		Account account = sAccount.getAccount();
		if (null == account) {
			return false;
		}
		
		Log.d(TAG, "setSkypeCallPolicy: " + policy);
		account.SetServersideIntProperty(Account.PROPERTY.skype_call_policy.getId(), policy.getId());
		return true;
	}
	
	/**
	 * Get chat policy
	 * @return chat policy
	 */
	public Account.CHATPOLICY getSkypeChatPolicy()
	{
		if (!isValid()) {
			return null;
		}
		SktAccount sAccount = mSkypeApp.getAccount();
		if (null == sAccount) {
			return null;
		}
		Account account = sAccount.getAccount();
		if (null == account) {
			return null;
		}
		
		int code = account.GetIntProperty(Account.PROPERTY.chat_policy);
		return Account.CHATPOLICY.get(code);
	}
	
	/**
	 * Set chat policy
	 * @param policy <br> chat policy
	 */
	public boolean setSkypeChatPolicy(Account.CHATPOLICY policy)
	{
		if (!isValid()) {
			return false;
		}
		SktAccount sAccount = mSkypeApp.getAccount();
		if (null == sAccount) {
			return false;
		}
		Account account = sAccount.getAccount();
		if (null == account) {
			return false;
		}
		
		account.SetServersideIntProperty(Account.PROPERTY.chat_policy.getId(), policy.getId());
		return true;
	}
	
	/**
	 * Get video recv policy
	 * @return 0:NONE; 1:INCONTACTLIST; 2:ANYONE; -1: failed
	 */
	public int getVideoRecvPolicy()
	{
		if (!isValid()) {
			return -1;
		}
		return mSkypeApp.getSkype().GetInt(Video.VIDEO_RECVPOLICY);
	}
	
	
	/**
	 * Set video recv policy
	 * @param policy <br> 0:NONE; 1:INCONTACTLIST; 2:ANYONE
	 */
	public boolean setVideoRecvPolicy(int policy)
	{
		if (!isValid()) {
			return false;
		}

		Log.d(TAG, "setVideoRecvPolicy: " + policy);
		mSkypeApp.getSkype().SetInt(Video.VIDEO_RECVPOLICY, policy);
		return true;
	}
	
	/**
	 * Get enables/disables call forwarding policy
	 * @return 0: disable; 1: enable; -1: failed
	 */
	public int getCallForwarding()
	{
		if (!isValid())
		{
			return -1;
		}

		return mSkypeApp.getSkype().GetInt(Conversation.CALL_APPLY_CF);
	}
	
	/**
	 * Set enables/disables call forwarding policy
	 * @param policy <br> 0: disable; 1: enable;
	 */
	public boolean setCallForwarding(int policy)
	{
		if (!isValid())
		{
			return false;
		}
		if (0 == policy || 1 == policy)
		{
			mSkypeApp.getSkype().SetInt(Conversation.CALL_APPLY_CF, policy);
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	/**
	 * Get callForwarding list
	 * @return list
	 */
	public ArrayList<String> getCallForwardList()
	{		
		ArrayList<String> al = new ArrayList<String>();
		if (!isValid()) {
			return al;
		}
		SktAccount sAccount = mSkypeApp.getAccount();
		if (null == sAccount) {
			return al;
		}
		Account account = sAccount.getAccount();
		if (null == account) {
			return al;
		}
		Log.d(TAG, "getCallForwardList: " + account.GetStrProperty(Account.PROPERTY.offline_callforward));
		
		/* example: 0,60,+861234567890 0,60,+862345678901 0,60,+863456789012 */
		String cf = account.GetStrProperty(Account.PROPERTY.offline_callforward);
		if (!cf.isEmpty())
		{
			String str[] = cf.split(" ");
			for(int i=0; null != str && i < str.length; i++)
			{
				String tmp[] = str[i].split(",");
				if (null != tmp && 3 == tmp.length)
				{
					al.add(tmp[2]);
				}
			}
		}
		return al;
	}
	
	/**
	 * Set callForwarding list
	 * @param list <br>
	 */
	public boolean setCallForwardList(ArrayList<String> list)
	{
		if (!isValid() || null == list) {
			return false;
		}
		SktAccount sAccount = mSkypeApp.getAccount();
		if (null == sAccount) {
			return false;
		}
		Account account = sAccount.getAccount();
		if (null == account) {
			return false;
		}
		
		String cf = "";
		for (int i=0; i<list.size(); i++)
		{
			cf += " 0,60," + list.get(i);
		}
		cf.trim();
		Log.d(TAG, "setCallForwardList: " + cf);
//		if (!cf.isEmpty())
//		{
//			account.SetServersideStrProperty(Account.PROPERTY.offline_callforward.getId(), cf);
//		}
		account.SetServersideStrProperty(Account.PROPERTY.offline_callforward.getId(), cf);
		return true;
	}
	
	/**
	 * Set beamformer mic spacing
	 * @param bEnable <br> 1: Space-separated array of 1 (in case of 2 microphones) integers; 2: Space-separated array of 2 (in case of 4 microphones) integers
	 */
	public boolean setBeamformerMICSpacing(int policy)
	{
		if (!isValid()) {
			return false;
		}
		
		if (1 == policy)
		{
			mSkypeApp.getSkype().SetStr(Skype.BEAMFORMER_MIC_SPACING, "0 0");    // for Maxim 2-MIC
			return true;
		}
		else if (2 == policy)
		{
			mSkypeApp.getSkype().SetStr(Skype.BEAMFORMER_MIC_SPACING, "34 75"); // for Maxim 4-MIC
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	/**
	 * Set timeout of calling
	 * @param seconds <br> timeout
	 * @return true: succes; false: failed
	 */
	public boolean setTimeoutOfCalling(int seconds)
	{
		if (!isValid()) {
			return false;
		}
		mSkypeApp.getSkype().SetInt(Conversation.CALL_NOANSWER_TIMEOUT, seconds);
		return true;
	}
	
	/**
	 * Get timeout of calling. (seconds), default value:"15" 
	 * @return -1: failed: other: timeout
	 */
	public int getTimeoutOfCalling()
	{
		if (!isValid()) {
			return -1;
		}
		int timeout = mSkypeApp.getSkype().GetInt(Conversation.CALL_NOANSWER_TIMEOUT);
		return (0 == timeout ? 15 : timeout);
	}
	
	/**
	 * Set autoforwarding of incoming calls to voicemail
	 * @param flag <br> 0: off; 1: on
	 */
	public boolean setAutoforwardingToVoicemail(int policy)
	{
		if (!isValid()) {
			return false;
		}
		
		if (0 == policy || 1 == policy)
		{
			Log.d(TAG, "setAutoforwardingToVoicemail : " + policy);
			mSkypeApp.getSkype().SetInt(Conversation.CALL_SEND_TO_VM, policy);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Set autoforwarding of incoming calls to voicemail policy
	 * @return <br> 0: off; 1: on; -1:failed
	 */
	public int getAutoforwardingToVoicemail()
	{
		if (!isValid()) {
			return -1;
		}
		
		return mSkypeApp.getSkype().GetInt(Conversation.CALL_SEND_TO_VM);
	}
	
	/**
	 * Set idle time of away. Number of seconds since the last keyboard or mouse activity, after which the online status of currently logged in account should be set to AWAY
	 * @param seconds
	 */
	public boolean setIdleTimeOfAway(int seconds)
	{
		if (!isValid()) {
			return false;
		}
	    mSkypeApp.getSkype().SetInt(Skype.IDLE_TIME_FOR_AWAY, seconds);
	    return true;
	}
	
	/**
	 * Get idle time of away
	 * @return -1: failed: other: idle time
	 */
	public int getIdleTimeOfAway()
	{
		if (!isValid()) {
			return -1;
		}
	    return mSkypeApp.getSkype().GetInt(Skype.IDLE_TIME_FOR_AWAY);
	}
	
	/**
	 * Set timeout of recently live
	 * @param seconds
	 */
	public boolean setTimeoutOfRecentlyLive(int seconds)
	{
		if (!isValid()) 
		{
			return false;
		}
		mSkypeApp.getSkype().SetInt(Conversation.RECENTLY_LIVE_TIMEOUT, seconds);
		return true;
	}
	
	/**
	 * Get timeout of recently live, default value:"20"
	 * @return <br> -1: failed; other: timeout
	 */
	public int getTimeoutOfRecentlyLive()
	{
		if (!isValid()) 
		{
			return -1;
		}
		int timeout = 0;
		timeout = mSkypeApp.getSkype().GetInt(Conversation.RECENTLY_LIVE_TIMEOUT);	
		return (0 == timeout ? 20 : timeout);
	}
	
	// 璇ラ�椤瑰鎵�湁鏈湴鐢ㄦ埛閮芥湁鏁�
	public void setDisableVideoFunctionality(boolean bEnable)
	{
		if (!isValid()) {
			return;
		}
		if (bEnable) {
			mSkypeApp.getSkype().SetInt(Video.VIDEO_DISABLE, 1);
		}
		else {
			mSkypeApp.getSkype().SetInt(Video.VIDEO_DISABLE, 0);
		}
	}
	
	public int getDisableVideoFunctionality()
	{
		if (!isValid()) {
			return 0;
		}
		return mSkypeApp.getSkype().GetInt(Video.VIDEO_DISABLE);
	}
	
	public Skype.GetISOLanguageInfoResult getLanguageInfo()
	{
		if (!isValid()) {
			return null;
		}
		if (null == mIsoLanguage) 
		{
			mIsoLanguage = mSkypeApp.getSkype().GetISOLanguageInfo();
		}
		return mIsoLanguage;
	}
	
	public Skype.GetISOCountryInfoResult getISOCountryInfo()
	{
		if (!isValid()) {
			return null;
		}
		if (null == mIsoCountry) {
			mIsoCountry = mSkypeApp.getSkype().GetISOCountryInfo();
		}
		return mIsoCountry;
	}
	
	public int getSpeakerVolume()
	{
		if (!isValid()) {
			return -1;
		}
		return mSkypeApp.getSkype().GetSpeakerVolume();
	}
	
	public void setSpeakerVolume(int val)
	{
		if (!isValid()) {
			return;
		}
		if (val >= 0 && val <= 100)
		{
			mSkypeApp.getSkype().SetSpeakerVolume(val);
		}
	}
	
	public int getMicVolume()
	{
		if (!isValid()) {
			return -1;
		}
		return mSkypeApp.getSkype().GetMicVolume();
	}
	
	public void setMicVolume(int val)
	{
		if (!isValid()) {
			return;
		}
		if (val >= 0 && val <= 100)
		{
			mSkypeApp.getSkype().SetMicVolume(val);
		}
	}
	
	public void setHistoryDays(HISTORY_SAVE_POLICY policy)
	{
		int val = (HISTORY_SAVE_POLICY.NO_HISTORY == policy ? 1 : 0);
		mSkypeApp.getSkype().SetInt(Conversation.DISABLE_CHAT_HISTORY, val);
		
		switch(policy)
		{
		case NO_HISTORY:
			break;
		case TWO_WEEKS:
			mSkypeApp.getSkype().SetInt(Conversation.CHAT_HISTORY_DAYS, 14);
			break;
		case ONE_MONTH:
			mSkypeApp.getSkype().SetInt(Conversation.CHAT_HISTORY_DAYS, 30);
			break;
		case THREE_MONTHS:
			mSkypeApp.getSkype().SetInt(Conversation.CHAT_HISTORY_DAYS, 90);
			break;
		default:
			mSkypeApp.getSkype().SetInt(Conversation.CHAT_HISTORY_DAYS, 0);
			break;
		}
	}
	
	public HISTORY_SAVE_POLICY getHistoryDays() {
		HISTORY_SAVE_POLICY policy = HISTORY_SAVE_POLICY.FOREVER;
		int val = mSkypeApp.getSkype().GetInt(Conversation.DISABLE_CHAT_HISTORY);
		if (1 == val) {
			policy = HISTORY_SAVE_POLICY.NO_HISTORY;
		} else {
			val = mSkypeApp.getSkype().GetInt(Conversation.CHAT_HISTORY_DAYS);
			switch (val) {
			case 14:
				policy = HISTORY_SAVE_POLICY.TWO_WEEKS;
				break;
			case 30:
				policy = HISTORY_SAVE_POLICY.ONE_MONTH;
				break;
			case 90:
				policy = HISTORY_SAVE_POLICY.THREE_MONTHS;
				break;
			default:
				policy = HISTORY_SAVE_POLICY.FOREVER;
				break;
			}
		}
		return policy;
	}
	
	public void clearHistory() {
		Calendar calendar = Calendar.getInstance();
		long toTime = calendar.getTimeInMillis() / 1000L;
		Message.TYPE[] types = new Message.TYPE[4];
		types[0] = Message.TYPE.STARTED_LIVESESSION;
		types[1] = Message.TYPE.ENDED_LIVESESSION;
		types[2] = Message.TYPE.POSTED_TEXT;
		types[3] = Message.TYPE.POSTED_VOICE_MESSAGE;
		for (int i = 0; i < types.length; i++) {
			SktUtils.sleep(300);
			Message[] msgs = mSkypeApp.getSkype().GetMessageListByType(types[i], false, 0, (int) toTime);
			if (null == msgs || msgs.length <= 0) {
				continue;
			}

			for (int j = 0; j < msgs.length; j++) {
				msgs[j].DeleteLocally();
				msgs[j] = null;
			}
			msgs = null;
		}
	}
	
	public void testAllGet()
	{		
		Log.d(TAG, "getAutoforwardingToVoicemail = " + getAutoforwardingToVoicemail());
		Log.d(TAG, "getCallForwarding = " + getCallForwarding());
		Log.d(TAG, "getCallForwardList = " + getCallForwardList());
		Log.d(TAG, "getDisableVideoFunctionality = " + getDisableVideoFunctionality());
		Log.d(TAG, "getIdleTimeOfAway = " + getIdleTimeOfAway());
		Log.d(TAG, "getMicVolume = " + getMicVolume());
		Log.d(TAG, "getSkypeCallPolicy = " + getSkypeCallPolicy());
		Log.d(TAG, "getSkypeChatPolicy = " + getSkypeChatPolicy());
		Log.d(TAG, "getSpeakerVolume = " + getSpeakerVolume());
		Log.d(TAG, "getTimeoutOfCalling = " + getTimeoutOfCalling());
		Log.d(TAG, "getTimeoutOfRecentlyLive = " + getTimeoutOfRecentlyLive());
		Log.d(TAG, "getVideoRecvPolicy = " + getVideoRecvPolicy());
		
		Log.d(TAG, "CALL_APPLY_CF = " + mSkypeApp.getSkype().GetInt(Conversation.CALL_APPLY_CF));
		Log.d(TAG, "CALL_NOANSWER_TIMEOUT = " + mSkypeApp.getSkype().GetInt(Conversation.CALL_NOANSWER_TIMEOUT));
		Log.d(TAG, "CALL_SEND_TO_VM = " + mSkypeApp.getSkype().GetInt(Conversation.CALL_SEND_TO_VM));
		Log.d(TAG, "CHAT_HISTORY_DAYS = " + mSkypeApp.getSkype().GetInt(Conversation.CHAT_HISTORY_DAYS));
		Log.d(TAG, "RECENTLY_LIVE_TIMEOUT = " + mSkypeApp.getSkype().GetInt(Conversation.RECENTLY_LIVE_TIMEOUT));
		
		Log.d(TAG, "BEAMFORMER_MIC_SPACING = " + mSkypeApp.getSkype().GetInt(Skype.BEAMFORMER_MIC_SPACING));
		Log.d(TAG, "IDLE_TIME_FOR_AWAY = " + mSkypeApp.getSkype().GetInt(Skype.IDLE_TIME_FOR_AWAY));		
	}
} 	
