package com.jrm.skype.api;


import java.util.Calendar;
import java.util.regex.PatternSyntaxException;
import com.jrm.skype.util.Log;
import com.jrm.skype.api.SktSkypeApp.ConvType;
import com.jrm.skype.api.SktSkypeApp.MsgType;
import com.skype.api.Account;
import com.skype.api.Contact;
import com.skype.api.ContactGroup;
import com.skype.api.ContactSearch;
import com.skype.api.Conversation;
import com.skype.api.Message;
import com.skype.api.Participant;
import com.skype.api.Skype;
import com.skype.api.SkypeObject;
import com.skype.api.Sms;
import com.skype.api.Transfer;
import com.skype.api.Video;
import com.skype.api.Voicemail;
import com.skype.api.Account.AccountListener;
import com.skype.api.Contact.ContactListener;
import com.skype.api.ContactGroup.ContactGroupListener;
import com.skype.api.ContactSearch.ContactSearchListener;
import com.skype.api.Conversation.ConversationListener;
import com.skype.api.Conversation.LIST_TYPE;
import com.skype.api.Conversation.LOCAL_LIVESTATUS;
import com.skype.api.Message.MessageListener;
import com.skype.api.Participant.DTMF;
import com.skype.api.Participant.ParticipantListener;
//import com.skype.api.Skype.APP2APP_STREAMS;
import com.skype.api.Skype.PROXYTYPE;
import com.skype.api.Skype.SkypeListener;
import com.skype.api.Sms.SmsListener;
import com.skype.api.Transfer.TransferListener;
import com.skype.api.Video.VideoListener;
import com.skype.api.Voicemail.VoicemailListener;
import com.skype.ipc.RootObject.ErrorListener;

public class SktListeners implements AccountListener, SkypeListener,
		VideoListener, ParticipantListener, ConversationListener,
		ContactListener, ContactGroupListener, ContactSearchListener,
		MessageListener, SmsListener, TransferListener, VoicemailListener, 
		ErrorListener {

	private final static String TAG = "SktListeners";
	private SktSkypeApp mSkypeApp;
	private boolean mRegisterListener;
	


	public SktListeners(SktSkypeApp skypeApp) {
		mSkypeApp = skypeApp;
		registerAllListeners();
		skypeApp.getSkype().SetErrorListener(this); //xujr add, 20130428
	}

	@Override
	protected void finalize() throws Throwable {
		unRegisterAllListeners();
	}

	public void registerListener(int modid) {
		// Register the listener with Skype service
		mSkypeApp.getSkype().RegisterListener(modid, this);
	}

	public void registerAllListeners() {
		if (!mRegisterListener) {
			mRegisterListener = true;
			registerListener(Skype.getmoduleid());
			registerListener(Account.moduleID());
			registerListener(Contact.moduleID());
			registerListener(ContactGroup.moduleID());
			registerListener(ContactSearch.moduleID());
			registerListener(Conversation.moduleID());
			registerListener(Message.moduleID());
			registerListener(Participant.moduleID());
			registerListener(Sms.moduleID());
			registerListener(Transfer.moduleID());
			registerListener(Video.moduleID());
			registerListener(Voicemail.moduleID());
		}
	}

	public void unRegisterListener(int modid) {
		// Register the listener with Skype service
		mSkypeApp.getSkype().UnRegisterListener(modid, this);
	}

	public void unRegisterAllListeners() {
		if (mRegisterListener) {
			mRegisterListener = false;
			unRegisterListener(Account.moduleID());
			unRegisterListener(Contact.moduleID());
			unRegisterListener(ContactGroup.moduleID());
			unRegisterListener(ContactSearch.moduleID());
			unRegisterListener(Conversation.moduleID());
			unRegisterListener(Message.moduleID());
			unRegisterListener(Participant.moduleID());
			unRegisterListener(Sms.moduleID());
			unRegisterListener(Transfer.moduleID());
			unRegisterListener(Video.moduleID());
			unRegisterListener(Voicemail.moduleID());
		}
	}

	public void OnApp2AppDatagram(String appname, String stream, byte[] data) {
		Log.d(TAG, "OnApp2AppDatagram: "+ appname + ", "+ "stream");
	}

//	public void OnApp2AppStreamListChange(String appname,
//			APP2APP_STREAMS listType, String[] streams) {
//		 
//
//	}

	@Override
	public void OnAvailableDeviceListChange() {
		Log.d(TAG, "OnAvailableDeviceListChange: ");
	}

	@Override
	public void OnAvailableVideoDeviceListChange() {
		Log.d(TAG, "OnAvailableVideoDeviceListChange: ");
	}

	@Override
	public void OnContactGoneOffline(Contact contact) {
		Log.d(TAG, "OnContactGoneOffline " + contact);

	}

	@Override
	public void OnContactOnlineAppearance(Contact contact) {
		Log.d(TAG, "OnContactOnlineAppearance " + contact);

	}

	@Override
	public void OnConversationListChange(Conversation conversation, LIST_TYPE type, boolean added) {
		mSkypeApp.onConversationListChange(conversation, type, added);
	}

	@Override
	public void OnMessage(Message message, boolean changesInboxTimestamp, Message supersedesHistoryMessage, Conversation conversation) {
		Log.d(TAG, "OnMessage " + message + ", " + changesInboxTimestamp + ", " + 
				supersedesHistoryMessage + ", " + conversation);	
	}

	@Override
	public void OnNewCustomContactGroup(ContactGroup group) {
		Log.d(TAG, "OnNewCustomContactGroup " + group);
	}

	@Override
	public void OnNrgLevelsChange() {
		Log.d(TAG, "OnNrgLevelsChange ");
	}

	@Override
	public void OnProxyAuthFailure(PROXYTYPE type) {
		Log.d(TAG, "OnProxyAuthFailure " + type);
	}

	@Override
	public void OnCaptureRequestCompleted(SkypeObject o, int requestId, boolean isSuccessful,
			byte[] image, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnLastFrameCapture(SkypeObject o, byte[] image, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnPropertyChange(SkypeObject obj, com.skype.api.Video.PROPERTY prop, Object value) {
		Log.d(TAG, "Video OnPropertyChange:" + prop + ", value:" + value);
		Video v = (Video) obj;
		switch (prop) {
		case dimensions:
			mSkypeApp.onVideoDimensionsChange(v, (String) value);
			break;
		case error:
			Log.d(TAG, "error: " + v.GetStrProperty(Video.PROPERTY.error));
			mSkypeApp.onVideoErrorChange(v, (String) value);
			break;
		case status:
			mSkypeApp.onVideoStatusChange(v, Video.STATUS.get((Integer) value));
			break;
		case media_type:
			mSkypeApp.onVideoMediaTypeChange(v, Video.MEDIATYPE.get((Integer) value));
		}

	}

	@Override
	public void OnIncomingDTMF(SkypeObject o, DTMF dtmf) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnPropertyChange(SkypeObject obj,
			com.skype.api.Participant.PROPERTY prop, Object value) {
		
		Participant p = (Participant) obj;
		if (prop != Participant.PROPERTY.debuginfo && 
				prop != Participant.PROPERTY.sound_level) {
			Log.d(TAG, "Participant identity  = " + p.GetStrProperty(Participant.PROPERTY.identity));
			Log.d(TAG, "Participant OnPropertyChange, prop = " + prop + ", value = " + value );
		}
		switch (prop) {
		case video_status:
			Log.d(TAG, "participant video_status change: prop = " + prop + ", value = " + value + 
					", identity  = " + p.GetStrProperty(Participant.PROPERTY.identity));
			mSkypeApp.onVideoStatusChange(p, Participant.VIDEO_STATUS.get((Integer) value));
			break;
		case voice_status:
			Log.d(TAG, "participant voice_status change");
			mSkypeApp.onVoiceStatusChange(p, Participant.VOICE_STATUS.get((Integer) value));
			break;
		default:
			break;
		}
	}

	@Override
	public void OnMessage(SkypeObject obj, Message message) {
		Log.d(TAG, "OnMessage2: " + message.GetStrProperty(Message.PROPERTY.author) +  "," +
				Message.TYPE.get(message.GetIntProperty(Message.PROPERTY.type)) + ","  +
				message.GetStrProperty(Message.PROPERTY.body_xml));
		
		Conversation conv = (Conversation)obj;
		Message.TYPE msgType = Message.TYPE.get(message.GetIntProperty(Message.PROPERTY.type));
		
		Calendar calendar = Calendar.getInstance();
		Long timeStamp = calendar.getTimeInMillis() / 1000L;
		conv.SetConsumedHorizon(timeStamp.intValue(), false);
		
		if(null == msgType){
		    return;
		}
		
		switch(msgType)
		{
		case ADDED_CONSUMERS:
		case RETIRED_OTHERS:
			mSkypeApp.updateParticipants(conv);
			break;
		
		case POSTED_TEXT:
			processTextMessage(conv, message, msgType);
			break;
			
		case POSTED_VOICE_MESSAGE:
			processVoiceMessage(conv, message, msgType);
			break;
			
		case REQUESTED_AUTH:
			break;
		case GRANTED_AUTH:
			break;
		case STARTED_LIVESESSION:
			mSkypeApp.onStartedLiveSession(message);
			break;
		case ENDED_LIVESESSION:
			mSkypeApp.onEndedLiveSession(message);
			break;
		default:
			break;
		}		
	}

	@Override
	public void OnParticipantListChange(SkypeObject obj) {
		// TODO Auto-generated method stub
		Log.d(TAG, "OnParticipantListChange ");
	}

	@Override
	public void OnPropertyChange(SkypeObject obj,
			com.skype.api.Conversation.PROPERTY prop, Object value) {
		Log.d(TAG, "Conversation OnPropertyChange, prop = " + prop + ", value = " + value);
		Conversation conv = (Conversation)obj;
		switch (prop) {
		case local_livestatus:
			Integer enVal = (Integer)value;
			mSkypeApp.onLocalLiveStatusChange(conv, LOCAL_LIVESTATUS.get(enVal));
			break;
		case active_vm_id:
	        mSkypeApp.onRecodingVmStatusChange(conv);
			break;
		default:
			break;
		}
	}

	@Override
	public void OnSpawnConference(SkypeObject o, Conversation spawned) {
		 //when the conv change from dialog to conference, just leave
		SktConversation conv = SktUtils.getConversation();
		if (null != conv) {
			conv.spawnConference(spawned);
			conv.leaveConversationCall();
		} 
		Log.d(TAG, "OnSpawnConference: convid = " + spawned.getOid());
	}

	@Override
	public void OnPropertyChange(SkypeObject obj,
			com.skype.api.Contact.PROPERTY prop, Object value) {
		Contact conctact = (Contact)obj;
		String name = conctact.GetIdentity();
		Log.d(TAG, "Contact OnPropertyChange, prop = " + prop + ", value = " + value + ", skypename = " + name);
		
		switch(prop) {
		case authrequest_count:
			if (value instanceof Integer)
			{
				Integer nums = (Integer)value;
				if (nums > 0)
				{
					
					Log.d(TAG, "Contact skypename " + conctact.GetIdentity());
					Log.d(TAG, "Contact emails " + ((Contact)obj).GetStrProperty(Contact.PROPERTY.emails));
					Log.d(TAG, "Contact received_authrequest " + ((Contact)obj).GetStrProperty(Contact.PROPERTY.received_authrequest));	
					conctact.RefreshProfile();
					SktUtils.sleep(100);
					name = conctact.GetIdentity();
					String text = conctact.GetStrProperty(Contact.PROPERTY.received_authrequest);
					mSkypeApp.receivedAuthrequest(name, text);
				}
			}
			break;
		case availability:
			int code = conctact.GetIntProperty(Contact.PROPERTY.availability);
			Contact.AVAILABILITY availability = Contact.AVAILABILITY.get(code);
			mSkypeApp.updateContactAvailability(name, availability);
			Log.d(TAG, "availability: " + name);
			break;
		case avatar_image:
			conctact.RefreshProfile();
			SktUtils.sleep(200);
			byte [] avatar = conctact.GetBinProperty(Contact.PROPERTY.avatar_image);
			mSkypeApp.updateContactAvatar(name, avatar);
			Log.d(TAG, "avatar_image: " + name);
			break;
		case fullname:
			mSkypeApp.updateContactFullname(name, conctact.GetStrProperty(Contact.PROPERTY.fullname));
			break;
		case mood_text:
			mSkypeApp.updateContactMoodtext(name, conctact.GetStrProperty(Contact.PROPERTY.mood_text));
			break;
		case displayname:
			mSkypeApp.updateContactDisplayname(name, conctact.GetStrProperty(Contact.PROPERTY.displayname));
			break;
		default:
			break;
		}
	}

	@Override
	public void OnChange(SkypeObject o, Contact contact) {
		Log.d(TAG, "OnChange, SkypeObject = " + o + ", contact = " + contact);
	}

	@Override
	public void OnChangeConversation(SkypeObject o, Conversation conversation) {
		Log.d(TAG, "OnChangeConversation, SkypeObject = " + o + ", conversation = " + conversation);
	}

	@Override
	public void OnPropertyChange(SkypeObject obj,
			com.skype.api.ContactGroup.PROPERTY prop, Object value) {
		Log.d(TAG, "ContactGroup OnPropertyChange, prop = " + prop + ", value = " + value);
	}

	@Override
	public void OnNewResult(SkypeObject obj, Contact contact, int rankValue) {
		Log.d(TAG, "OnNewResult, contact = " + contact.GetIdentity());
		ContactSearch cs = (ContactSearch)obj;
		mSkypeApp.onNewSearchResult(cs,contact);
	}

	@Override
	public void OnPropertyChange(SkypeObject obj,
			com.skype.api.ContactSearch.PROPERTY prop, Object value) {
		Log.d(TAG, "ContactSearch OnPropertyChange, prop = " + prop + ", value = " + value);
		mSkypeApp.onSearchStatusChange((ContactSearch)obj, prop);
	}

	@Override
	public void OnPropertyChange(SkypeObject obj,
			com.skype.api.Message.PROPERTY prop, Object value) {
		Log.d(TAG, "Message OnPropertyChange, prop = " + prop + ", value = " + value);
	}

	@Override
	public void OnPropertyChange(SkypeObject obj,
			com.skype.api.Sms.PROPERTY prop, Object value) {
		Log.d(TAG, "Sms OnPropertyChange, prop = " + prop + ", value = " + value);
	}

	@Override
	public void OnPropertyChange(SkypeObject obj,
			com.skype.api.Transfer.PROPERTY prop, Object value) {
		Log.d(TAG, "Transfer OnPropertyChange, prop = " + prop + ", value = " + value);
	}

	@Override
	public void OnPropertyChange(SkypeObject obj,
			com.skype.api.Voicemail.PROPERTY prop, Object value) {
		
		Voicemail vm = (Voicemail)obj;
		Log.d(TAG, "Voicemail.getOid() = " + (vm != null ? vm.getOid() : ""));
		Log.d(TAG, "Voicemail OnPropertyChange, prop = " + prop + ", value = " + value);
		Integer tmpValue = 0;
		if (value instanceof Integer)
		{
			tmpValue = (Integer)value;
		}
		mSkypeApp.onVmStatusChange(vm, prop, tmpValue);
	}

    @Override
	public void OnPropertyChange(SkypeObject obj,
            com.skype.api.Account.PROPERTY prop, Object value)
    {
    	Log.d(TAG, "Account OnPropertyChange, prop = " + prop + ", value = " + value);
    	mSkypeApp.onAccountStatusChange((Account)obj, prop, value);
    }
    
    private void processTextMessage(Conversation conv, Message message, Message.TYPE msgType) {
		int code = conv.GetIntProperty(Conversation.PROPERTY.type);
		Conversation.TYPE type = Conversation.TYPE.get(code);
		ConvType convType = ConvType.DIALOG;
		if (type != Conversation.TYPE.DIALOG) {
			convType = ConvType.CONFERENCE;
		}
			
		String myName = mSkypeApp.getAccount().getAccountName();
		String fromName = message.GetStrProperty(Message.PROPERTY.author);
		Log.d(TAG,
				"type: " + msgType + ", from: " + fromName + ", "
						+ message.GetStrProperty(Message.PROPERTY.body_xml));
		if (fromName.compareTo(myName) != 0) {
			String body = message.GetStrProperty(Message.PROPERTY.body_xml);
			long timeStamp = message.GetIntProperty(Message.PROPERTY.timestamp);
			timeStamp = timeStamp * 1000; /* second convert to millisecond */
			String convName = conv.GetStrProperty(Conversation.PROPERTY.identity);

			try {
				if (null != body) {
					body = body.replaceAll("&lt;", "<");
				}
				if (null != body) {
					body = body.replaceAll("&gt;", ">");
				}
				if (null != body) {
					body = body.replaceAll("&amp;", "&");
				}
				if (null != body) {
					body = body.replaceAll("&apos;", "\'");
				}
				if (null != body) {
					body = body.replaceAll("&quot;", "\"");
				}
			} catch (PatternSyntaxException e) {
			}

			mSkypeApp.onNewMessage(MsgType.CHAT, convName, fromName, timeStamp,
					body, 0, convType, conv);
			conv.SetConsumedHorizon((int) (timeStamp / 1000), false);
		}
	}
    
    private void processVoiceMessage(Conversation conv, Message message, Message.TYPE msgType) {
		int code = conv.GetIntProperty(Conversation.PROPERTY.type);
		Conversation.TYPE type = Conversation.TYPE.get(code);
		ConvType convType = ConvType.DIALOG;
		if (type != Conversation.TYPE.DIALOG) {
			convType = ConvType.CONFERENCE;
		}
			
		String myName = mSkypeApp.getAccount().getAccountName();
		String fromName = message.GetStrProperty(Message.PROPERTY.author);
		if (fromName.compareTo(myName) != 0) {
			long timeStamp = message.GetIntProperty(Message.PROPERTY.timestamp);
			timeStamp = timeStamp * 1000; /* second convert to millisecond */
			String convName = conv.GetStrProperty(Conversation.PROPERTY.identity);

			int duration = 0;
			Voicemail vm = message.GetVoiceMessage();
			if (null != vm) {
				duration = vm.GetIntProperty(Voicemail.PROPERTY.duration);
			} else {
				Log.e(TAG, "vm == null");
			}

			Log.d(TAG, "POSTED_VOICE_MESSAGE, message id = " + message.getOid());
			Log.d(TAG, "POSTED_VOICE_MESSAGE, message id2 = "+ message.GetVoiceMessage().getOid());
			String vmid = "" + message.GetVoiceMessage().getOid();
			mSkypeApp.onNewMessage(MsgType.VOICEMAIL, convName, fromName,
					timeStamp, vmid, duration, convType, conv);
			conv.SetConsumedHorizon((int) (timeStamp / 1000), false);
		}
	}

	@Override
	public void OnSkypeKitFatalError() {
		Log.d(TAG, "SkypeKit fatal error reported.  Continue at your own risk.");
		Log.d(TAG, "Real applications should shut down at this point.");
	}

	@Override
	public void OnSkypeKitConnectionClosed() {
		Log.d(TAG, "The connection to the SkypeKit runtime has closed.");
		Log.d(TAG, "Recovery is possible is the runtime resumes and the user reconnects to it.");
		mSkypeApp.OnSkypeKitConnectionClosed();
	}
}

