package com.jrm.skype.api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import com.jrm.skype.api.SktContact.SktBuddy;
import com.jrm.skype.api.SktSkypeApp.VoicemailStatus;
import com.skype.api.Conversation;
import com.skype.api.Message;
import com.skype.api.Participant;
import com.skype.api.Skype;
import com.skype.api.Video;
import com.skype.api.Voicemail;
import com.jrm.skype.util.Log;

public class SktConversation {
	private static final String TAG = "SktConversation";
	private SktSkypeApp mSkypeApp = null;
	private Conversation mConvCall = null;   /* convseration for a/v call */
	private Conversation mConvVM = null;     /* convseration for voice mail */
	private boolean mEnableVideo;
	private Voicemail mOutgoingVm = null;
	private Voicemail mIncomingVm = null;
	private HashMap<String, Conversation> mConvsMap = null;
	
	public class SktCallInfo {
		private int mConvId;
		private int mTimeStamp;
		private int mDuration;
		private int mPartNums;
		private String mDirect;
		private String mDisplayName;
		
		public void setConvId(int convId) {
			mConvId = convId;
		}
		
		public void setTimeStamp(int timestamp) {
			mTimeStamp = timestamp;
		}
		
		public void setDuration(int duration) {
			mDuration = duration;
		}
		
		public void setPartNums(int partnums) {
			mPartNums = partnums;
		}
		
		public void setDirect(String direct) {
			mDirect = direct;
		}
		
		public void setDisplayName(String displayName) {
			mDisplayName = displayName;
		}
		
		public int getConvId() {
			return mConvId;
		}
		
		public int getTimeStamp() {
			return mTimeStamp;
		}
		
		public int getDuration() {
			return mDuration;
		}
		
		public int getPartNums() {
			return mPartNums;
		}
		
		public String getDirect() {
			return mDirect;
		}
		
		public String getDisplayName() {
			return mDisplayName;
		}
		
	}
	
	
	public enum SktTextStatus {
		Unknow,
		Writing,
		Reading, 
	}
	
	public SktConversation(SktSkypeApp skypeApp)
	{
		mSkypeApp = skypeApp;
		mConvCall = null;
		mConvVM = null;
		mEnableVideo = false;
		
		mOutgoingVm = null;
		mIncomingVm = null;
		mConvsMap = new HashMap<String, Conversation>();
	}
	
	
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		
		mConvsMap.clear();
		mConvsMap = null;
		mSkypeApp = null;
		mOutgoingVm = null;
		mConvCall = null;
		mConvVM = null;
	}

	protected synchronized void initAllConvs()
	{
		if (!isValid())
		{
		    Log.d(TAG, "initAllConvs: isValid() = false");
			return;
		}
		Skype skype = mSkypeApp.getSkype();
		Conversation[] convs = null;
		for (int j = 0; j < 10; j++) {
			convs = skype.GetConversationList(Conversation.LIST_TYPE.ALL_CONVERSATIONS);
			if (convs == null || convs.length <= 0) {
				SktUtils.sleep(200);
				continue;
			}
		}
		for (int i = 0; i < convs.length; i++) {
			convs[i].getOid();
			mConvsMap.put(convs[i].GetStrProperty(Conversation.PROPERTY.identity),convs[i]);
		}
	}
	
	/*
	public synchronized boolean isLiveSession()
	{
		return mIsLiveSession;
	}*/
	
	private boolean isValid()
	{
		if (null == mSkypeApp)
		{
			Log.d(TAG, "isValid: null == mSkypeApp");
			return false;
		}
			
		if (!mSkypeApp.isLoggedIn())
		{
			Log.d(TAG, "isValid: false == mSkypeApp.isLoggedIn()");
			return false;
		}
		
		return true;
	}
	
	
	private Conversation getConvFromActName(String actName)
	{
		if ( null == actName) {
		    return null;
		}
	    if (mConvsMap.containsKey(actName)) {
	    	return mConvsMap.get(actName);
	    } else {
	    	String names[] = { actName };
	    	Conversation conv = mSkypeApp.getSkype().GetConversationByParticipants(names, true, false);
	    	mConvsMap.put(actName, conv);
	    	return conv;
	    }
	}
	
	private Conversation getConvFromConvName(String convName)
	{
	    if (null == convName)
	        return null;
	    if (mConvsMap.containsKey(convName)) {
	    	return mConvsMap.get(convName);
	    } else {
	    	Conversation conv = mSkypeApp.getSkype().GetConversationByIdentity(convName);
	    	mConvsMap.put(convName, conv);
	    	return conv;
	    }
	}
	
	public synchronized boolean startPSTBCall(String pstnNumber)
	{
		if (!isValid())
		{
			Log.d(TAG, "setConversation: isValid() = false");
			return false;
		}
		
		if (null != mConvCall) 
		{
			Log.d(TAG, "startConversationCall: mIsLiveSession = true");
			return false;
		}
		
		mConvCall = getConvFromConvName(pstnNumber);
		if (null == mConvCall) {
			return false;
		}
		
		// create participant objects to receive propchanges
		Participant[] parts = mConvCall.GetParticipants(Conversation.PARTICIPANTFILTER.ALL);
		for (int k = 0; k < parts.length; k++) {
			parts[k].getOid();
		}
		
		parts = mConvCall.GetParticipants(Conversation.PARTICIPANTFILTER.OTHER_CONSUMERS);
		if (null == parts) {
			Log.d(TAG, "Unable to get participant list");
			return false;
		}
		if (parts.length == 0) {
			Log.d(TAG, "No participants");
			return false;
		}

		mEnableVideo = false;
		
		//ring participants
		for (int i = 0; i < parts.length; i++) {
			parts[i].Ring("", false, 0, 0, false, "");
		}
		
		return true;
	}
	
	/*
	public synchronized void setConversationTextStatus(SktTextStatus status)
	{
		if (!isValid())
		{
			Log.d(TAG, "setConversationTextStatus: isValid() = false");
			return;
		}
		
		if (null == mConvChat)
		{
			Log.d(TAG, "setConversationTextStatus: mConversation = null");
			return;
		}
		
		if (status == SktTextStatus.Reading)
		{
			mConvChat.SetMyTextStatusTo(Participant.TEXT_STATUS.READING);
			Log.d(TAG, "setConversationTextStatus: READING");
		}
		else if (status == SktTextStatus.Writing)
		{
			mConvChat.SetMyTextStatusTo(Participant.TEXT_STATUS.WRITING);
			Log.d(TAG, "setConversationTextStatus: WRITING");
		}
		else
		{
			mConvChat.SetMyTextStatusTo(Participant.TEXT_STATUS.TEXT_UNKNOWN);
			Log.d(TAG, "setConversationTextStatus: TEXT_UNKNOWN");
		}
	}*/
	
	public void sendChatMessage(String convName, String text)
	{
		if (null == convName || convName.isEmpty())
		{
			Log.d(TAG, "sendChatMessage: invalid convName");
			return;
		}
		if (!isValid())
		{
			Log.d(TAG, "sendChatMessage: isValid() = false");
			return;
		}
		
		Conversation conv = getConvFromConvName(convName);
		if (null != conv)
		{	
			conv.SetMyTextStatusTo(Participant.TEXT_STATUS.WRITING);
			Message msg = conv.PostText(text, false);
			if (null == msg)
			{
				Log.d(TAG, "sendChatMessage: PostText failed");
			}
		} 
	}
	
	public void setTopic(String convName, String topic)
	{
		if (!isValid()) {
		    Log.d(TAG, "setTopic: isValid() = false");
			return;
		}
		
		Conversation conv = getConvFromConvName(convName);
		if (null != conv) {
			conv.SetTopic(topic, false);
		}
	}
	
	public synchronized boolean startConversationCall(String actName, boolean bEnableVideo)
	{
		if (!isValid()) {
		    Log.d(TAG, "startConversationCall: isValid() = false");
			return false;
		}
		
		if (null != mConvCall)
		{
			Log.d(TAG, "startConversationCall: mIsLiveSession = true");
			return false;
		}
		
		mConvCall = getConvFromActName(actName);
		Log.d(TAG, "startConversationCall: oid=" + mConvCall.getOid() + ", actName=" + actName + ", video=" + bEnableVideo);
		if (null == mConvCall) {
			return false;
		}

		// create participant objects to receive propchanges
		Participant[] participants = mConvCall.GetParticipants(Conversation.PARTICIPANTFILTER.ALL);
		if (null == participants) {
			return false;
		}
		for (int k = 0; k < participants.length; k++) {
			participants[k].getOid();
		}
		
		participants = mConvCall.GetParticipants(Conversation.PARTICIPANTFILTER.OTHER_CONSUMERS);
		if (participants == null) 
		{
			Log.d(TAG, "Unable to get participant list");
			return false;
		}
		if (participants.length == 0)
		{
			Log.d(TAG, "No participants");
			return false;
		}

		mEnableVideo = bEnableVideo;
		
		//ring participants
		for (int i = 0; i < participants.length; i++) 
		{
			participants[i].Ring("", bEnableVideo, 0, 0, false, "");
		}

		return true;
	}
	
	protected synchronized void setIncomingCall(Conversation conv)
	{
		mConvCall = conv;
	}
	
	/*
	protected synchronized void setLiveSession(boolean isLive)
	{
		mIsLiveSession = isLive;
	}
	*/
	
	protected synchronized void cacheConvs(String convName, Conversation conv)
	{
		if (null != convName) {
			if (!mConvsMap.containsKey(convName)) {
				mConvsMap.put(convName, conv);
			}
		}
	}
	
	protected synchronized boolean isEnableVideo()
	{
		return mEnableVideo;
	}
	
	public boolean anwserIncomingCall(boolean bEnableVideo)
	{
		boolean bNotify = false;
		/* cancel recording voice mail */
		bNotify = cancelRecordingVoicemail();
		if (bNotify) {
			mSkypeApp.cancelVoicemailWhenIncomingcall(VoicemailStatus.CANCELLED_RECORD_WHEN_INCOMING_CALL);
		}
		
		synchronized(this) {
		
		if (null == mConvCall) {
			Log.d(TAG, "anwserIncomingCall: mConversation = null");
			return false;
		}
		
		mEnableVideo = bEnableVideo;
		mConvCall.JoinLiveSession("");
		
		
        // create participant objects to receive propchanges
        Participant[] participants = mConvCall.GetParticipants(Conversation.PARTICIPANTFILTER.ALL);
        for (int k = 0; k < participants.length; k++) {
            participants[k].getOid();
        }    
		}
		return true;
	}
	
	public synchronized void leaveConversationCall()
	{
		if (!isValid()) {
		    Log.d(TAG, "leaveConversationCall: isValid() = false");
			return;
		}
		
		mEnableVideo = false;
		
		if (null != mConvCall)
		{
			Log.d(TAG, "leaveIncomingCall: oid=" + mConvCall.getOid());
			mConvCall.LeaveLiveSession(false);
			Log.d(TAG, "leaveIncomingCall end-------");
		}
	}
	
	public synchronized String getConvCallName()
	{
		String convName = "";
		if (null != mConvCall)
		{
			convName = mConvCall.GetStrProperty(Conversation.PROPERTY.identity);
		}
		return convName;
	}
	
	public synchronized ArrayList<String> getParticipants(String convName)
	{
		ArrayList<String> partList = new ArrayList<String>();
		Conversation conv = getConvFromConvName(convName);
		if (null != conv && null != partList)
		{
			Participant parts[] = conv.GetParticipants(Conversation.PARTICIPANTFILTER.ALL);
			if (null != parts)
			{
				int nums = parts.length;
				for (int i=0; i<nums; i++)
				{
					partList.add(parts[i].GetStrProperty(Participant.PROPERTY.identity));
				}
			}
		}
		return partList;
	}
	
	public synchronized void holdConversationCall(boolean bOn)
	{
		if (!isValid())
		{
			Log.d(TAG, "holdConversationCall: isValid() = false");
			return;
		}
		
		if (null == mConvCall)
		{
			Log.d(TAG, "holdConversationCall: mConversation = null");
			return;
		}
		
		if (bOn) 
		{
		    Log.d(TAG, "HoldMyLiveSession---------------------->");
			mConvCall.HoldMyLiveSession();
		}
		else
		{
		    Log.d(TAG, "ResumeMyLiveSession---------------------->");
			mConvCall.ResumeMyLiveSession();
		}
		Log.d(TAG, "Hold-Resume------------called--------->");
	}
	
	public synchronized boolean getCallHoldStatus()
	{
		if (!isValid()) {
		    Log.d(TAG, "getCallHoldStatus: isValid() = false");
			return false;
		}

		if (null == mConvCall) {
			return false;
		}
		
		int code = mConvCall.GetIntProperty(Conversation.PROPERTY.local_livestatus);
		Conversation.LOCAL_LIVESTATUS liveStatus = Conversation.LOCAL_LIVESTATUS.get(code);
		return (liveStatus == Conversation.LOCAL_LIVESTATUS.ON_HOLD_LOCALLY);
	}
	
	public synchronized void muteMicroPhone(boolean bOn)
	{
		if (!isValid()) {
		    Log.d(TAG, "muteMicroPhone: isValid() = false");
			return;
		}

		if (null == mConvCall) {
			return;
		}

		if (bOn) {
			mConvCall.MuteMyMicrophone();
		} else {
			mConvCall.UnmuteMyMicrophone();
		}
	}
	
	public synchronized boolean getCallMuteStatus() {
		if (!isValid()) {
		    Log.d(TAG, "getCallMuteStatus: isValid() = false");
			return false;
		}

		if (null == mConvCall) {
			return false;
		}

		return mConvCall.GetBooleanProperty(Conversation.PROPERTY.live_is_muted);
	}
	
	public synchronized boolean startRecordingVoicemail(String actName)
	{
		if (!isValid() || null == actName)
		{
			return false;
		}
		
		mConvVM = getConvFromActName(actName);
		if (null == mConvVM) {
			return false;
		}

		mConvVM.StartVoiceMessage();
		return true;
	}
	
	public synchronized boolean cancelRecordingVoicemail()
	{
		boolean ret = (null != mConvVM);
		
		if (!isValid()) {
		    Log.d(TAG, "cancelRecordingVoicemail: isValid() = false");
			return false;
		}
		/*
		if (null != mOutgoingVm) {
			Log.d(TAG, "cancelRecordingVoicemail : oid = " + mOutgoingVm.getOid());
			int code = mOutgoingVm.GetIntProperty(Voicemail.PROPERTY.type);
			Voicemail.TYPE type = Voicemail.TYPE.get(code);
			if (Voicemail.TYPE.OUTGOING == type) {
			    Log.v(TAG, "mOutgoingVm.Cancel();");
				mOutgoingVm.Cancel();
			} else {
				mOutgoingVm.StopPlayback();
			}
			mOutgoingVm = null;
		}*/
		
		if (null != mConvVM) {
		    Log.v(TAG, "LeaveLiveSession(false)");
			mConvVM.LeaveLiveSession(false);
			mConvVM = null;
		}
		mOutgoingVm = null;
		
		return ret;
	}
	
	public synchronized void sendRecordVoicemail()
	{
		if (!isValid()) {
		    Log.d(TAG, "sendRecordVoicemail: isValid() = false");
			return;
		}
		
		if (null == mOutgoingVm) {
			return;
		}

		if (null != mConvVM) {
			mConvVM.PostVoiceMessage(mOutgoingVm, "");
		} else {
			mOutgoingVm.Cancel();
		}
		mConvVM = null;
		mOutgoingVm = null;
	}
	
	public synchronized void playbackVoicemail(int vmId)
	{
		if (vmId <= 0) {
			return;
		}
		if (!isValid()) {
		    Log.d(TAG, "playbackVoicemail: isValid() = false");
			return;
		}

		mIncomingVm = mSkypeApp.getSkype().GetVoiceMailFromId(vmId, mSkypeApp.getSkype());
		if (null != mIncomingVm) {
			mIncomingVm.StartPlayback();
		}
	}
	
	public synchronized boolean stopPlaybackVoicemail()
	{
		boolean ret = (null != mIncomingVm);
		if (null != mIncomingVm) {
			mIncomingVm.StopPlayback();
			mIncomingVm = null;
		}
		return ret;
	}
	
	
    /**
      * send dtmf code
      * @param ch <br> (1,2,3,4,5,6,7,8,9,0,*,#)
      */
	public synchronized boolean sendDTMF(char ch)
	{
		if (!isValid()) {
		    Log.d(TAG, "sendDTMF: isValid() = false");
			return false;
		}

		if (null == mConvCall) {
			return false;
		}

		Participant.DTMF dtmf = null;
		
		switch(ch)
		{
		case '0':
			dtmf = Participant.DTMF.DTMF_0;
			break;
		case '1':
			dtmf = Participant.DTMF.DTMF_1;
			break;
		case '2':
			dtmf = Participant.DTMF.DTMF_2;
			break;
		case '3':
			dtmf = Participant.DTMF.DTMF_3;
			break;
		case '4':
			dtmf = Participant.DTMF.DTMF_4;
			break;
		case '5':
			dtmf = Participant.DTMF.DTMF_5;
			break;
		case '6':
			dtmf = Participant.DTMF.DTMF_6;
			break;
		case '7':
			dtmf = Participant.DTMF.DTMF_7;
			break;
		case '8':
			dtmf = Participant.DTMF.DTMF_8;
			break;
		case '9':
			dtmf = Participant.DTMF.DTMF_9;
			break;
		case '*':
			dtmf = Participant.DTMF.DTMF_STAR;
			break;
		case '#':
			dtmf = Participant.DTMF.DTMF_POUND;
			break;
		default:
			break;
		}
		
		if (null != dtmf) {
			mConvCall.SendDTMF(dtmf, 260);
			Log.v(TAG, "SendDTMF---------------------------------->"+dtmf);
			return true;
		} else {
			return false;
		}
	}
	
	protected Conversation getConvCall()
	{
		return mConvCall;
	}
	
	protected synchronized int getConvCallOid() {
		if (null != mConvCall) {
			return mConvCall.getOid();
		} else {
			return -1;
		}
	}
	
	/*
	public Message[] getUnConsumedMessage(String actName, SktSkypeApp.MsgType type) 
	{
		String names[] = { actName };
		Conversation conversation = mSkypeApp.getSkype().GetConversationByParticipants(names, true, false);
		if (null == conversation) {
			return null;
		}
		Conversation.GetLastMessagesResult msgRet = conversation.GetLastMessages(0);
		if (null == msgRet) {
			return null;
		}
		
		Message.TYPE wantMsgType = Message.TYPE.POSTED_TEXT;
		switch(type){
		case VOICEMAIL:
			wantMsgType = Message.TYPE.POSTED_VOICE_MESSAGE;
			break;
		default:
			wantMsgType = Message.TYPE.POSTED_TEXT;
			break;
		}
		
		String myActName = mSkypeApp.getAccount().getAccountName();
		Vector<Message> unconsumedMessages = new Vector<Message>();
		if (null == unconsumedMessages) {
			return null;
		}
		for (int i = 0; i < msgRet.unconsumedMessages.length; i++)
	    {
			Message msg = msgRet.unconsumedMessages[i];
			Message.TYPE msgType = Message.TYPE.get(msg.GetIntProperty(Message.PROPERTY.type));
			String fromName = msg.GetStrProperty(Message.PROPERTY.author);
			if (fromName.compareTo(myActName) == 0) {
				continue;
			}
			if (msgType == wantMsgType) {
				Log.d(TAG, "id " + msg.getOid() + "  , from: " + msg.GetStrProperty(Message.PROPERTY.author) +
						", : " + msg.GetStrProperty(Message.PROPERTY.body_xml));
			}
			unconsumedMessages.add(msg);
			msg.DeleteLocally();
	    }
		
		return unconsumedMessages.toArray(new Message[unconsumedMessages.size()]);
	}
	*/
	
	public synchronized String getDisplayName(String convName)
	{
		if (mConvsMap.containsKey(convName)) {
			return mConvsMap.get(convName).GetStrProperty(Conversation.PROPERTY.displayname);
		} else {
			return convName;
		}
	}
	
	/**
	 * checi the conversation is a 1:1 conversation or a conference conversation.
	 * @param convName <br> conversation name
	 * @return true: conference; false: 1:1 conversation
	 */
	public synchronized boolean isConferenceConv(String convName)
	{
		boolean bConference = false;
		Conversation conv = getConvFromConvName(convName);
		if (null != conv)
		{
			int code = conv.GetIntProperty(Conversation.PROPERTY.type);
			Conversation.TYPE type = Conversation.TYPE.get(code);
			switch(type) {
			case DIALOG:
				bConference = false;
				break;
			case CONFERENCE:
			case TERMINATED_CONFERENCE:
			case LEGACY_VOICE_CONFERENCE:
				bConference = true;
				break;
			case LEGACY_SHAREDGROUP:
				Log.d(TAG, "LEGACY_SHAREDGROUP");
				break;
			}
		}
		//Log.d(TAG, "isConferenceConv: " + convName + " = " + bConference);
		
		return bConference;
	}
	
	protected synchronized void setOutgoingVm(Conversation conv, Voicemail vm)
	{
		if (null != mConvCall && null != conv) {
			if (mConvCall.getOid() == conv.getOid()) {
				mConvCall = null;
			}
		}
		mConvVM = conv;
		mOutgoingVm = vm;
		
		/*
		if (mNoVoicmail && null != mOutgoingVm) {
			
			int code = mOutgoingVm.GetIntProperty(Voicemail.PROPERTY.type);
			Voicemail.TYPE type = Voicemail.TYPE.get(code);
			if (Voicemail.TYPE.OUTGOING == type) {
				mOutgoingVm.Cancel();
				Log.d(TAG, "setOutgoingVm: mOutgoingVm.Cancel()");
			} else {
				mOutgoingVm.StopPlayback();
				Log.d(TAG, "setOutgoingVm: mOutgoingVm.StopPlayback()");
			}
			mOutgoingVm = null;
			mNoVoicmail = false;
		}*/
		
		Log.d(TAG, "setOutgoingVm: " +  (conv !=null ? conv.getOid() : "null"));
	}
	
	/**
	 * Check conversation's voice calling capability
	 * @param convName
	 * @return true: have; false: don't have
	 */
	public boolean havaVoiceCallCapability(String convName)
	{
		boolean bCapability = false;
		if (!isConferenceConv(convName))
		{
			SktContact contact  = mSkypeApp.getContact();
			if (null != contact)
			{
				bCapability = contact.havaVoiceCallCapability(convName);
			}
		}
		return bCapability;
	}
	
	/**
	 * Check conversation's video capability
	 * @param convName
	 * @return true: have; false: don't have
	 */
	public boolean havaVideoCallCapability(String convName)
	{
		boolean bCapability = false;
		if (!isConferenceConv(convName))
		{
			SktContact contact  = mSkypeApp.getContact();
			if (null != contact)
			{
				bCapability = contact.havaVideoCallCapability(convName);
			}
		}
		return bCapability;
	}
	
	/**
	 * check conversation's voicemail capability
	 * @param convName
	 * @return true: have; false: don't have
	 */
	public boolean haveVoicemailCapability(String convName)
	{
		boolean bCapability = false;
		if (!isConferenceConv(convName))
		{
			SktContact contact  = mSkypeApp.getContact();
			if (null != contact)
			{
				bCapability = contact.haveVoicemailCapability(convName);
			}
		}
		return bCapability;
	}
	
	/**
	 * check conversation's skypeout capability
	 * @param convName
	 * @return true: have; false: don't have
	 */
	public boolean haveSkypeoutCapability(String convName)
	{
		boolean bCapability = false;
		if (!isConferenceConv(convName))
		{
			SktContact contact  = mSkypeApp.getContact();
			if (null != contact)
			{
				bCapability = contact.haveSkypeoutCapability(convName);
			}
		}
		return bCapability;
	}
	
	
	/**
	 * check conversation's chat capability
	 * @param convName
	 * @return true: have; false: don't have
	 */
	public boolean haveChatCapability(String convName)
	{
		boolean bCapability = false;
		if (!isConferenceConv(convName))
		{
			SktContact contact  = mSkypeApp.getContact();
			if (null != contact)
			{
				bCapability = contact.haveChatCapability(convName);
			}
		}
		else 
		{
			bCapability = true;
		}
		return bCapability;
	}
	
	/**
	 * Check whether the user in the buddy list, only skype user can add to buddy list
	 * @param convName
	 * @return true: in buddy list; false not in
	 */
	public boolean canAddtoBuddyList(String convName)
	{
		boolean bCapability = false;
		if (!isConferenceConv(convName))
		{
			bCapability = (0 == SktUtils.canAddToContactList(convName));
		}
		return bCapability;
	}
	
	public boolean startSendVideo()
	{
		return mSkypeApp.startSendVideo();
	}
	
	public void startSendVideoForce()
    {
        mSkypeApp.startSendVideoForce();
    }
	
	public void stopSendVideo()
	{
		mSkypeApp.stopSendVideo();
	}
	
	public boolean startRecvVideo()
	{
		return mSkypeApp.startRecvVideo();
	}
	
	public void stopRecvVideo()
	{
		mSkypeApp.stopRecvVideo();
	}
	
	/**
	 * get video status
	 * @param whichVideo <br> 1: send video; 2: recv video
	 * @return video status
	 */
	public Video.STATUS getVideoStatus(int whichVideo) {
		return mSkypeApp.getVideoStatus(whichVideo);
	}
	
	public byte[] getAvatar(String convName) {
		boolean convfer = isConferenceConv(convName);
		if (!convfer) {
			SktBuddy buddy = SktUtils.getBuddy(convName);
			if (null != buddy) {
				return buddy.getAvatar();
			} else {
				return null;
			}
		} else {
			Conversation conv = null;
			synchronized (this) {
				conv = getConvFromConvName(convName);
			}
			if (null != conv) {
				return conv.GetBinProperty(Conversation.PROPERTY.meta_picture);
			} else {
				return null;
			}
		}
	}
	
	public synchronized String getTopic(String convName) {
		Conversation conv = getConvFromConvName(convName);
		if (null != conv) {
			return conv.GetStrProperty(Conversation.PROPERTY.meta_topic);
		} else {
			return "";
		}
	}
	
	protected synchronized boolean checkIncomingCall(Conversation conv) {
		
		/**
		 * check for playback voicemail. 
		 * Playback will automatically be stopped by skypekit when an incoming call 
		 */
		if (null != mIncomingVm) {
			mSkypeApp.cancelVoicemailWhenIncomingcall(VoicemailStatus.CANCELLED_PLAYBACK_WHEN_INCOMING_CALL);
		}
		
		/**
		 *  check for recording voicemail. 
		 *  if we are recording voicemail for B and now B is being called to me, we must cancel the recording.
		 */
		if (null != mConvVM) {
			if (mConvVM.getOid() == conv.getOid()) {
				if (null != mOutgoingVm) {
					Log.d(TAG, "checkIncomingCall: the same conversation (A voicemail to B and B calling A)");
					/*
					int code = mOutgoingVm.GetIntProperty(Voicemail.PROPERTY.type);
					Voicemail.TYPE type = Voicemail.TYPE.get(code);
					if (Voicemail.TYPE.OUTGOING == type) {
						mOutgoingVm.Cancel();
					} else {
						mOutgoingVm.StopPlayback();
					}*/
					conv.LeaveLiveSession(false);
					SktUtils.sleep(100);
					mConvVM.LeaveLiveSession(false);
					SktUtils.sleep(100);
					mOutgoingVm = null;
					mConvVM = null;
					mConvCall = null;
					mSkypeApp.cancelVoicemailWhenIncomingcall(VoicemailStatus.CANCELLED_RECORD_WHEN_INCOMING_CALL);
				}
				
				return false;
			}
		}
		
		/**
		 *  check for calling.
		 *  if we are in a call, reject the incoming call. 
		 *  if we are being called to B and now B is being called to me, do nothing
		 */
		if (null != mConvCall) {
			if (mConvCall.getOid() != conv.getOid()) {
				conv.LeaveLiveSession(false);
				Log.d(TAG, "checkIncomingCall: we already have a live session, we will reject it");
			} else {
				Log.d(TAG, "checkIncomingCall: the same conversation (A calling B and B calling A)");
			}
			return false;
		}

		return true;
	}
	
	protected synchronized void spawnConference(Conversation conv) {
		mConvCall = conv;
	}
	
	public ArrayList<SktCallInfo> getCallHistory() {
		ArrayList<SktCallInfo> array = new ArrayList<SktCallInfo>();
		Calendar calendar = Calendar.getInstance();
		long toTime = calendar.getTimeInMillis() / 1000L;
		Message[] msgs = mSkypeApp.getSkype().GetMessageListByType(Message.TYPE.STARTED_LIVESESSION, false, 0, (int)toTime);
		if (null == msgs || msgs.length <= 0) {
			Log.d(TAG, "getCallHistory: lenght=0");
			return array;
		}
		SktAccount act = SktUtils.getAccount();
		if (null == act) {
			return array;
		}
		
		String myActName = act.getAccountName();
		
		for(int i = 0; i < msgs.length; i++) {
			
			SktCallInfo callInfo = new SktCallInfo();
			callInfo.setConvId(msgs[i].GetIntProperty(Message.PROPERTY.convo_id));
			callInfo.setTimeStamp(msgs[i].GetIntProperty(Message.PROPERTY.timestamp));
			callInfo.setDisplayName(msgs[i].GetStrProperty(Message.PROPERTY.author_displayname));
			
			int partnums = 0;
			String body = msgs[i].GetStrProperty(Message.PROPERTY.body_xml);
			String msgAuthor = msgs[i].GetStrProperty(Message.PROPERTY.author);

			Log.d(TAG, "body = " + body);
			
			int pos = 0;
			while(true) {
				pos = body.indexOf("</part>", pos);
				if (pos >= 0) {
					pos++;
					partnums++;
				} else {
					break;
				}
			}
			
			String partname = "<part identity=\"" + myActName +"\">";
			pos = body.indexOf(partname);
			if (pos > 0) {
				body = body.substring(pos);
				pos = body.indexOf("</part>");
				if (pos > 0) {
					body = body.substring(0, pos);
				}
				
				pos = body.indexOf("<duration>");
				if (pos > 0) {
					body = body.substring(pos + "<duration>".length());
					pos = body.indexOf("</duration>");
					assert(pos > 0);
					try {
						callInfo.setDuration(Integer.parseInt(body.substring(0, pos)));
					} catch(NumberFormatException e) {}
				}
			}
			
			if (msgAuthor.compareTo(myActName) == 0) {
				callInfo.setDirect("outgoing");
			} else {
				if (callInfo.getDuration() > 0) {
					callInfo.setDirect("incoming");
				} else {
					callInfo.setDirect("missed");
				}
			}
			
			array.add(callInfo);
			
			Log.d(TAG, "convId = " + callInfo.getConvId());
			Log.d(TAG, "dname  = " + callInfo.getDisplayName());
			Log.d(TAG, "duration = " + callInfo.getDuration());
			Log.d(TAG, "partnums = " + callInfo.getPartNums());
			Log.d(TAG, "direct = " + callInfo.getDirect());
			Log.d(TAG, "\n\n\n");
			
		}
		
		return array;
	}
}
