package com.jrm.skype.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.jrm.skype.util.Log;

import com.skype.api.Account;
import com.skype.api.Contact;
import com.skype.api.ContactGroup;
import com.skype.api.ContactSearch;


public class SktContact {
	
	private static final String TAG = "SktContact";
	private SktSkypeApp mSkypeApp;
	private boolean mBuddyRequest;
	private ArrayList<SktBuddy> mSearchBuddy;
	private ContactSearch mContactSearch;
	private HashMap<String, SktBuddy>mBuddyMap;
	
	public class SktBuddy {
		private String mActName;
		private String mDisplayName;
		private String mFullName;
		private String mMoodText;
		private Contact.AVAILABILITY mAvalability;
		private Contact mContact;
		private byte[] mAvatar;

		public SktBuddy() {
			mActName = "";
			mDisplayName = "";
			mFullName = "";
			mMoodText = "";
			mAvalability = Contact.AVAILABILITY.OFFLINE;
			mContact = null;
			mAvatar = null;
		}

		public void setActName(String actName) {
			mActName = actName;
		}

		public void setDisplayName(String dName) {
			mDisplayName = dName;
		}

		public void setFullName(String fName) {
			mFullName = fName;
		}

		public void setMoodText(String mood) {
			mMoodText = mood;
		}

		public void setAvalabiltity(Contact.AVAILABILITY avalability) {
			mAvalability = avalability;
		}

		protected void setContact(Contact contact) {
			mContact = contact;
		}

		protected Contact getContact() {
			return mContact;
		}

		public void setAvatar(byte[] avatar) {
			mAvatar = avatar;
		}

		public String getActName() {
			return mActName;
		}

		public String getDisplayName() {
			return mDisplayName;
		}

		public String getFullName() {
			return mFullName;
		}

		public String getMoodText() {
			return mMoodText;
		}

		public Contact.AVAILABILITY getAvalability() {
			return mAvalability;
		}

		public String getCountry() {
			if (null != mContact) {
				return mContact.GetStrProperty(Contact.PROPERTY.country);
			} else {
				return "";
			}
		}

		public String getProvince() {
			if (null != mContact) {
				return mContact.GetStrProperty(Contact.PROPERTY.province);
			} else {
				return "";
			}
		}

		public String getCity() {
			if (null != mContact) {
				return mContact.GetStrProperty(Contact.PROPERTY.city);
			} else {
				return "";
			}
		}

		public String getHomePage() {
			if (null != mContact) {
				return mContact.GetStrProperty(Contact.PROPERTY.homepage);
			} else {
				return "";
			}
		}

		public String getLanguage() {
			if (null != mContact) {
				return mContact.GetStrProperty(Contact.PROPERTY.languages);
			} else {
				return "";
			}
		}

		public String getEmail() {
			if (null != mContact) {
				return mContact.GetStrProperty(Contact.PROPERTY.emails);
			} else {
				return "";
			}
		}

		public String getPhoneMobile() {
			if (null != mContact) {
				return mContact.GetStrProperty(Contact.PROPERTY.phone_mobile);
			} else {
				return "";
			}
		}

		public String getPhoneHome() {
			if (null != mContact) {
				return mContact.GetStrProperty(Contact.PROPERTY.phone_home);
			} else {
				return "";
			}
		}

		public String getPhoneOffice() {
			if (null != mContact) {
				return mContact.GetStrProperty(Contact.PROPERTY.phone_office);
			} else {
				return "";
			}
		}

		public String getRichMoodText() {
			if (null != mContact) {
				return mContact.GetStrProperty(Contact.PROPERTY.rich_mood_text);
			} else {
				return "";
			}
		}

		public int getTimezone() {
			if (null != mContact) {
				return mContact.GetIntProperty(Contact.PROPERTY.timezone);
			} else {
				return -1;
			}
		}

		public int getBirthDate() {
			if (null != mContact) {
				return mContact.GetIntProperty(Contact.PROPERTY.birthday);
			} else {
				return -1;
			}
		}

		public int getGender() {
			if (null != mContact) {
				return mContact.GetIntProperty(Contact.PROPERTY.gender);
			} else {
				return -1;
			}
		}

		public byte[] getAvatar() {
			return mAvatar;
		}
	}

	public class CompBuddy implements Comparator<SktBuddy> {
		
		@Override
		public int compare(SktBuddy lBuddy, SktBuddy rBuddy) {
			 
			 String lName = lBuddy.getDisplayName();
			 String rName = rBuddy.getDisplayName();
			 
			 // displayname > fullname > skypename
			 if (lName.isEmpty()) {
				 lName = lBuddy.getFullName();
				 if (lName.isEmpty()) {
					 lName = lBuddy.getActName();
				 }
			 }
			 if (rName.isEmpty()) {
				 rName = rBuddy.getFullName();
				 if (rName.isEmpty()) {
					 rName = rBuddy.getActName();
				 }
			 }
			 return lName.compareTo(rName);		
		 }
	}
	
	public class SktAuthItem {
		private String mActName;
		private String mText;

		public SktAuthItem() {
			mActName = "";
			mText = "";
		}

		public void setActName(String actName) {
			mActName = actName;
		}

		public void setText(String text) {
			mText = text;
		}

		public String getActName() {
			return mActName;
		}

		public String getText() {
			return mText;
		}
	}
	
	public SktContact(SktSkypeApp skypeApp)
	{
		assert(null != skypeApp);
		mSkypeApp = skypeApp;
		mBuddyMap = new HashMap<String, SktBuddy>();
		mSearchBuddy = new ArrayList<SktBuddy>(); 
		mContactSearch = null;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		
		clear();
		mBuddyMap = null;
		mContactSearch = null;
		mSearchBuddy = null;
		mSkypeApp = null;
	}
	
	protected void clear() {
		mBuddyMap.clear();
		mSearchBuddy.clear();
	}

	private boolean isValid()
	{
		if (null != mSkypeApp && mSkypeApp.isLoggedIn())
		{
			return true;
		}
		Log.d(TAG, "isValid: false");
		return false;
	}
	
	private ArrayList<SktBuddy> sortBuddyList()
	{
		SktBuddy buddy = null;
		ArrayList<SktBuddy> onlines = new ArrayList<SktBuddy>();
		ArrayList<SktBuddy> others = new ArrayList<SktBuddy>();
		if (null == onlines || null == others) {
			return null;
		}
		
		if (mBuddyMap.isEmpty()) {
			return null;
		}
		
		Iterator<Entry<String, SktBuddy>> iter = mBuddyMap.entrySet().iterator();
		while(iter.hasNext()) {
			buddy = iter.next().getValue();
			assert(null == buddy);
			if (null == buddy) {
				break;
			}
			
			Contact.AVAILABILITY avl = buddy.getAvalability();
			/*
			contact = buddy.getContact();
			if (null == contact) {
				offlines.add(buddy);
			} else {
				//int code = contact.GetIntProperty(Contact.PROPERTY.availability);
				//Contact.AVAILABILITY avl = Contact.AVAILABILITY.get(code);
				 */
				switch(avl) {
				case ONLINE:
				case AWAY:
				case DO_NOT_DISTURB:
					onlines.add(buddy);
					break;
				default:
				    others.add(buddy);
					break;
				//}
			}
		}
				
		Collections.sort(onlines, new CompBuddy());
		Collections.sort(others, new CompBuddy());

		for (SktBuddy sb : others) {
			onlines.add(sb);
		}
		others.clear();
		others = null;
		
		return onlines;
	}
	
	private Contact[] fetchBuddies()
	{
		ContactGroup cg = null;
		cg = mSkypeApp.getSkype().GetHardwiredContactGroup(ContactGroup.TYPE.ALL_BUDDIES);
		if (null == cg) {
			Log.d(TAG, "fetchBuddies: " + "null == cg");
			return null;
		}
		
		Contact[] contacts = cg.GetContacts();
		if (null == contacts) {
			Log.d(TAG, "fetchBuddies: " + "null == buddies");
			return null;
		}

		for (Contact buddy : contacts) {
			if (!mSkypeApp.isLoggedIn()) {
				Log.d(TAG, "has logout");
				break;
			}
			
			SktUtils.sleep(100);
			
			String actName = buddy.GetIdentity();
			if (null == actName) {
				break;
			}
			
			SktBuddy sb = new SktBuddy();
			if (null != sb) {
				sb.setContact(buddy);
				synchronized (this) {
					if (!mBuddyMap.containsKey(actName)) {
						mBuddyMap.put(actName, sb);
					}
				}

				int code = buddy.GetIntProperty(Contact.PROPERTY.availability);
				sb.setAvalabiltity(Contact.AVAILABILITY.get(code));
				sb.setActName(actName);
				sb.setDisplayName(buddy.GetStrProperty(Contact.PROPERTY.displayname));
				sb.setFullName(buddy.GetStrProperty(Contact.PROPERTY.fullname));
				sb.setMoodText(buddy.GetStrProperty(Contact.PROPERTY.mood_text));
				
				//Log.d(TAG, "actName = " + actName + ", img = " + buddy.GetIntProperty(Contact.PROPERTY.avatar_timestamp));
			}
		}
		
		mSkypeApp.onGetBuddyList();
		
		return contacts;
	}
	
	private void fetchBuddyAvatars(Contact[] contacts)
	{		
		if (null == contacts) {
			return;
		}

		String actName = "";
		for(int i = 0; i < contacts.length; i++) {
		
			SktUtils.sleep(100);
			if (!mSkypeApp.isLoggedIn()) {
				break;
			}
			
			actName = contacts[i].GetIdentity();
			if (null == actName) {
				Log.d(TAG, "actName == null");
				break;
			}
			
			Contact.TYPE type = contacts[i].GetType();
			if (Contact.TYPE.SKYPE != type) {
				continue;
			}
			
			int timestamp = contacts[i].GetIntProperty(Contact.PROPERTY.avatar_timestamp);
			if (timestamp > 0) {
				if (!SktDataCfg.existsAvatar(actName)) {
					Log.d(TAG, "RefreshProfile: " + actName);
					contacts[i].RefreshProfile();
					SktUtils.sleep(900);
					
					Log.d(TAG, "GetAvatar: " + actName);
					Contact.GetAvatarResult avatar = contacts[i].GetAvatar();
					Log.d(TAG, "GetAvatar: Ok, " + actName);
					if ((avatar != null) && avatar.present) {
						SktDataCfg.writeAvatar(actName, avatar.avatar);
					} 
				}
			}
		}
		
		synchronized (this) {
			SktBuddy myBuddy = null;
			Iterator<Entry<String, SktBuddy>> iter = mBuddyMap.entrySet().iterator();
			while (iter.hasNext()) {
				myBuddy = iter.next().getValue();
				if (null == myBuddy) {
					break;
				}
				actName = myBuddy.getActName();
				if (SktDataCfg.existsAvatar(actName)) {
					byte[] jpg = SktDataCfg.readAvatar(actName);
					if (null != jpg) {
						myBuddy.setAvatar(jpg);
					}
				}
			}
		}
		
		mSkypeApp.onBuddyPropertyChange();
	}

	
	/**
	 * Request buddies list
	 * @return
	 */
	public void requestBuddies()
	{		 
		if (!isValid())
		{
			return;
		}
		
		synchronized(this) {
			if (mBuddyRequest) 
			{
				return;
			}
			mBuddyRequest = true;
			Log.d(TAG, "requestBuddies: true");
		}
		
		new Thread() {
			@Override
			public void run() {
				super.run();
				Log.d(TAG, "In fecth buddy thread");
				
				/* timeout after 30S (1500MS * 20) */
				int i = 0;
				final int tryNums = 20;
				
				for (i = 0; i <= tryNums; i++) {
					if (!isValid()) {
						i = tryNums + 1;
						break;
					}
					SktUtils.sleep(1500);
					
					Account.CBLSYNCSTATUS status = SktUtils.getSyncStatus();
					Log.d(TAG, "cbl_sync_status = " + status);
					if (Account.CBLSYNCSTATUS.CBL_IN_SYNC == status) {
						break;
					}
					if (Account.CBLSYNCSTATUS.CBL_SYNC_FAILED == status) {
						break;
					}
					
				}
				
				SktUtils.sleep(2500);//delay 2.5s for the ui to receive the callback
				if (Account.CBLSYNCSTATUS.CBL_IN_SYNC == SktUtils.getSyncStatus()) {
					SktUtils.initConvs();
					Contact[] contacts = fetchBuddies();
					fetchBuddyAvatars(contacts);
					SktUtils.getMyAvatar();
				} else {
					mSkypeApp.onSyncStatusChange(Account.CBLSYNCSTATUS.CBL_SYNC_FAILED.getId());
				}
				synchronized(this) {
					mBuddyRequest = false;
				}
			}
		}.start();
	}
	
	/**
	 * Get buddies list
	 * @return buddies list
	 */
	public synchronized ArrayList<SktBuddy> getBuddyList() {
		return sortBuddyList();
	}
	
	/**
	 * Get buddy Nums
	 * @return buddy Nums
	 */
	public synchronized int getBuddyNums()
	{
		return mBuddyMap.size();
	}
	
	/**
	 * find contact
	 * @param type <br> contact group type
	 * @param actName <br> account name
	 * @return contact
	 */
	private Contact findContact(ContactGroup.TYPE type, String actName)
	{
		if (!isValid())
		{
			return null;
		}
		
		ContactGroup cg = mSkypeApp.getSkype().GetHardwiredContactGroup(type);
		if (null == cg)
		{
			return null;
		}
		
		Contact[] buddies = cg.GetContacts();
		if (null == buddies)
		{
			return null;
		}
		
		String skypeName = "";
		for (int i = 0; i < buddies.length; ++i)
		{
			Contact contact = buddies[i];
			if (null == contact) continue;
			
			skypeName = contact.GetIdentity();
			if (null == skypeName)
			{
				Log.d(TAG, "actName == null");
				break;
			}
			if (skypeName.compareTo(actName) == 0)
			{
				return contact;
			}
		}
		return null;
	}
	
	/**
	 * Add contact to contact list
	 * @param actName <br> account name
	 * @param msg <br> request info
	 * @return true: successed; false: failed
	 */
	public boolean addContact(String actName, String msg)
	{		
		if (!isValid())
		{
			Log.d(TAG, "addContact: null == mSkype");
			return false;
		}
		
		Contact contact = mSkypeApp.getSkype().GetContact(actName);
		if (null == contact)
		{
			Log.d(TAG, "addContact: null == contact");
			return false;
		}
		
		if (contact.IsMemberOfHardwiredGroup(ContactGroup.TYPE.ALL_BUDDIES))
		{
		    Log.d(TAG, "addContact: IsMemberOfHardwiredGroup");
			return true;
		}
		else
		{
			return addContact(contact, msg);
		}	
	}
	
	/**
	 * Add contact to buddy list
	 * @param contact <br> 
	 * @param msg <br> Text that typically introduces the requesting user and details the reason for the authorization request
	 * @return true: success; false: faild
	 */
	public boolean addContact(Contact contact, String msg)
	{
		if (!isValid())
		{
			Log.d(TAG, "addContact: null == mSkype");
			return false;
		}
		
		contact.SetBuddyStatus(true, true);
		if (null != msg && !msg.isEmpty())
		{
			contact.SendAuthRequest(msg, 0);
		}
		
		synchronized (this) {
			addContactToMyBuddies(contact);
			mSkypeApp.onBuddyPropertyChange();
		}
		return true;
	}
	
	/**
	 * Adds this Contact from the ALL_BUDDIES hardwired group
	 * @param actName <br> contact's skype name
	 */
	public void addIncomingContactRequest(String actName) 
	{
		Contact contact = findContact(ContactGroup.TYPE.CONTACTS_WAITING_MY_AUTHORIZATION, actName);
		if (null != contact) 
		{
			contact.SetBuddyStatus(true, true);
			synchronized (this) {
				addContactToMyBuddies(contact);
				mSkypeApp.onBuddyPropertyChange();
			}
		}
	}
	
	/**
	 * Blocks any further incoming communication attempts from this contact
	 * @param actName <br>
	 */
	public void blockIncomingContactRequest(String actName)
	{
		Contact contact = findContact(ContactGroup.TYPE.CONTACTS_WAITING_MY_AUTHORIZATION, actName);
		if (null != contact) 
		{
			contact.SetBlocked(true, false);
		}
	}
	
	/**
	 * Rejects and removes a pending authorization request from this Contact
	 * @param actName <br>
	 */
	public void ignoreIncomingContactRequest(String actName)
	{
		Contact contact = findContact(ContactGroup.TYPE.CONTACTS_WAITING_MY_AUTHORIZATION, actName);
		if (null != contact) 
		{
			contact.IgnoreAuthRequest();
		}
	}
	
	/**
	 * Remove Contact from contact list
	 * @param actName <br> 
	 */
	public void removeContact(String actName)
	{	
		SktBuddy buddy = getBuddy(actName);
		if (null != buddy) {
			Contact contact = buddy.getContact();
			if (null != contact) {
				contact.SetBuddyStatus(false, true);
				synchronized (this) {
					removeContactFromMyBuddies(contact);
					mSkypeApp.onBuddyPropertyChange();
				}
			}else {
				Log.d(TAG, "removeContact: contact == null");
			}
		} else {
			Log.d(TAG, "removeContact: buddy == null");
		}
	}
	
	/**
	 * Block contact
	 * @param actName <br>
	 */
	public void blockContact(String actName)
	{
		Contact contact = findContact(ContactGroup.TYPE.ALL_KNOWN_CONTACTS, actName);
		if (null == contact)
		{
			Log.d(TAG, "blockContact: null == contact");
			return;
		}
		contact.SetBlocked(true, false);
	}
	
	/**
	 * UnBlock contact
	 * @param actName <br>
	 */
	public void unBlockContact(String actName)
	{
		Contact contact = findContact(ContactGroup.TYPE.ALL_KNOWN_CONTACTS, actName);
		if (null == contact)
		{
			Log.d(TAG, "unBlockContact: null == contact");
			return;
		}
		contact.SetBlocked(false, false);
	}
	
	/**
	 * Search contast
	 * @param key <br> key value(skypenames/aliases/fullnames/emails)
	 * @return true: searching; false: search failed
	 */
	public boolean search(String key)
	{		
		if (!isValid())
		{
			return false;
		}
		ContactSearch cs = mSkypeApp.getSkype().CreateBasicContactSearch(key);
		if (null == cs)
		{
			Log.d(TAG, "baseContactSearch: null == cs");
			return false;	
		}
		if (!cs.IsValid())
		{
			Log.d(TAG, "baseContactSearch: cs.IsValid() == false");
			return false;	
		}
		synchronized(this) {
			mSearchBuddy.clear();
			mContactSearch = cs;
		}
		cs.Submit();
		return true;
	}
	
	/**
	 * Search contast
	 * @param key <br> key value(skypenames)
	 * @return true: searching; false: search failed
	 */
	public boolean searchByName(String key)
	{		
		if (!isValid())
		{
			return false;
		}
		
		ContactSearch cs = mSkypeApp.getSkype().CreateIdentitySearch(key);
		if (null == cs)
		{
			Log.d(TAG, "contactSearchByName: null == cs");
			return false;	
		}
		
		if (!cs.IsValid())
		{
			Log.d(TAG, "contactSearchByName: cs.IsValid() == false");
			return false;	
		}
		synchronized(this) {
			mSearchBuddy.clear();
			mContactSearch = cs;
		}
		cs.Submit();
		return true;
	}
	
	/**
	 * get current ContactSearch
	 * @return
	 */
	public ContactSearch getCurrentContactSearch () {
		synchronized(this) {
			return mContactSearch;
		}
	}
	
	public void cancelContactSearch() {
		synchronized(this) {
			mContactSearch = null;
		}
	}
		
	/**
	 * Get search list
	 * @return search list
	 */
	public synchronized ArrayList<SktBuddy> getSearchList()
	{
		return mSearchBuddy;
	}
	
	public synchronized byte[] getAvatar(String actName)
	{
		SktBuddy buddy = null;
		if (mBuddyMap.containsKey(actName)) {
			buddy = mBuddyMap.get(actName);
			return buddy.getAvatar();
		} else {
			return null;
		}
	}
	
	/**
	 * Add contact to search list
	 * @param contact <br> 
	 */
	protected synchronized void addToSearchList(ContactSearch search, Contact contact)
	{
		if (null != search && null != mContactSearch && search.getOid() == mContactSearch.getOid())
		{
			SktBuddy sb = new SktBuddy();
			if (null != sb)
			{
				contact.RefreshProfile();
				SktUtils.sleep(50);
				String actName = contact.GetIdentity();
				if (null == actName) {
					Log.d(TAG, "actName == null");
					return;
				}
				sb.setActName(actName);
				sb.setFullName(contact.GetStrProperty(Contact.PROPERTY.fullname));
				sb.setContact(contact);
				mSearchBuddy.add(sb);
			}
		}
	}
	
	/**
	 * Get buddy with the specified name 
	 * @param actName <br> account name
	 * @return null: failed; other: buddy
	 */
	public SktBuddy getBuddy(String actName)
	{
		synchronized(this) {
			if (mBuddyMap.containsKey(actName)) {
				return mBuddyMap.get(actName);
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Get contact's availability
	 * @param actName <br> contact's skype name
	 * @return availability
	 */
	public Contact.AVAILABILITY getAvailability(String actName) 
	{
		SktBuddy buddy = getBuddy(actName);
		if (null == buddy) {
			return Contact.AVAILABILITY.UNKNOWN;
		} else {
			return buddy.getAvalability();
		}
	}
	
	/**
	 * Get auth list
	 * @return contact list
	 */
	public ArrayList<SktAuthItem> getAuthRequestList()
	{
		ArrayList<SktAuthItem> authList = new ArrayList<SktAuthItem>();
	    ContactGroup cg = mSkypeApp.getSkype().GetHardwiredContactGroup(ContactGroup.TYPE.CONTACTS_WAITING_MY_AUTHORIZATION);
	    if (null == cg) 
	    { 
	    	Log.d(TAG, "Cann't get cg\n");
	    	return authList;
	    }
	    Contact[] authrequests = cg.GetContacts();
	    if (null == authrequests || authrequests.length == 0)
	    {
	    	Log.d(TAG, "Auth requests list is empty\n");
	    	return authList;
	    }

	    String actName = "";
	    for (int i = 0; i < authrequests.length; i++)
	    {
	    	
	    	Contact contact = authrequests[i];
	    	actName = contact.GetIdentity();
	    	if (null == actName)
	    	{
	    		Log.d(TAG, "actName == null");
	    		break;
	    	}
	    	SktAuthItem item = new SktAuthItem();
	    	item.setActName(actName);
	    	item.setText(contact.GetStrProperty(Contact.PROPERTY.received_authrequest));
	    	authList.add(item);
	    }
	    
	    return authList;
	}
	
	protected synchronized boolean updateContactAvailability(String actName, Contact.AVAILABILITY avl) {
		if (mBuddyMap.isEmpty()) {
			return false;
		}
		if (mBuddyMap.containsKey(actName)) {
			mBuddyMap.get(actName).setAvalabiltity(avl);
			return true;
		} else {
			return false;
		}
	}
	
	protected synchronized boolean updateContactAvatar(String actName, byte[] avatar) {
		if (mBuddyMap.isEmpty()) {
			return false;
		}
		if (mBuddyMap.containsKey(actName)) {
			mBuddyMap.get(actName).setAvatar(avatar);
			SktDataCfg.writeAvatar(actName, avatar);
			return true;
		} else {
			return false;
		}
	}
	
	protected synchronized boolean updateContactFullname(String actName, String fullName)
	{
		if (mBuddyMap.isEmpty()) {
			return false;
		}
		if (mBuddyMap.containsKey(actName)) {
			mBuddyMap.get(actName).setFullName(fullName);
			return true;
		} else {
			return false;
		}
	}
	
	protected synchronized boolean updateContactDisplayname(String actName, String displayname)
	{
		if (mBuddyMap.isEmpty()) {
			return false;
		}
		if (mBuddyMap.containsKey(actName)) {
			mBuddyMap.get(actName).setDisplayName(displayname);
			return true;
		} else {
			return false;
		}
	}
	
	protected synchronized boolean updateContactMoodtext(String actName, String mood)
	{
		if (mBuddyMap.isEmpty()) {
			return false;
		}
		if (mBuddyMap.containsKey(actName)) {
			mBuddyMap.get(actName).setMoodText(mood);
			return true;
		} else {
			return false;
		}
	}
	
	public String getDisplayName(String actName)
    {
		synchronized (this) {
			if (mBuddyMap.containsKey(actName)) {
				return mBuddyMap.get(actName).getDisplayName();
			}
		}
		
		String dName = "";
		Contact contact = null;
		contact = mSkypeApp.getSkype().GetContact(actName);
		if (null != contact) {
			dName = contact.GetStrProperty(Contact.PROPERTY.displayname);
			if (dName.isEmpty()) {
				dName = contact.GetStrProperty(Contact.PROPERTY.fullname);
			}
		}
		return dName;
    }
	
	private ArrayList<String> getContactList(ContactGroup.TYPE type)
	{
		ArrayList<String> list = new ArrayList<String>();
		if (!isValid() || null == list) {
			return null;
		}

		ContactGroup cg = mSkypeApp.getSkype().GetHardwiredContactGroup(type);
		if (null == cg) {
			return list;
		}

		Contact[] buddies = cg.GetContacts();
		if (null == buddies) {
			return list;
		}

		String actName = "";
		for (Contact buddy : buddies) {
			actName = buddy.GetIdentity();
			if (null == actName) {
				break;
			} else {
				list.add(actName);
			}
		}

		return list;
	}
	
	/**
	 * Get all known contacts
	 * @return contacts list
	 */
	public ArrayList<String> getAllContactList()
	{
		return getContactList(ContactGroup.TYPE.ALL_KNOWN_CONTACTS);
	}
	
	/**
	 * Get blocked contacts
	 * @return contacts list
	 */
	public ArrayList<String> getBlockedContactList()
	{
		return getContactList(ContactGroup.TYPE.CONTACTS_BLOCKED_BY_ME);
	}
	
	/**
	 * Get unblocked contacts
	 * @return contacts list
	 */
	public ArrayList<String> getUnBlockedContactList() {
		ArrayList<String> contacts = getAllContactList();
		if (null == contacts) {
			return null;
		}
		
		ArrayList<String> blockeds = getBlockedContactList();
		for (String name : blockeds) {
			contacts.remove(name);
		}

		return contacts;
	}
	
	/**
	 * Check contact's voice calling capability
	 * @param actName
	 * @return true: have calling capalibity; false: don't have
	 */
	public boolean havaVoiceCallCapability(String actName)
	{			
		boolean bCapability = false;
		Contact contact = null;
		synchronized (this) {
			if (mBuddyMap.containsKey(actName)) {
				contact = mBuddyMap.get(actName).getContact();
			}
		}
		if (null == contact) {
			contact = findContact(ContactGroup.TYPE.ALL_KNOWN_CONTACTS, actName);
		}
		
		if (null == contact) {
			contact = mSkypeApp.getSkype().GetContact(actName);
		}
				
		if (null != contact) {
			bCapability = true;
			int code = contact.GetIntProperty(Contact.PROPERTY.availability);
			Contact.AVAILABILITY ava = Contact.AVAILABILITY.get(code);
			Log.d(TAG, "havaVoiceCallCapability: ava = " + ava);
			switch (ava) {
			case BLOCKED:
			case BLOCKED_SKYPEOUT:
				bCapability = false;
				break;
			default:
				break;
			}
		}
		Log.d(TAG, "havaVoiceCallCapability :" + actName + ": " + bCapability);
		return bCapability;
	}
	
	/**
	 * Check contact's video capability
	 * @param actName
	 * @return true: have calling capalibity; false: don't have
	 */
	public boolean havaVideoCallCapability(String actName)
	{
		if (null == actName || actName.compareTo("echo123") == 0) {
			return false;
		}
		
		boolean bCapability = false;
		Contact contact = null;
		synchronized (this) {
			if (mBuddyMap.containsKey(actName)) {
				contact = mBuddyMap.get(actName).getContact();
			}
		}
		if (null == contact) {
			contact = findContact(ContactGroup.TYPE.ALL_KNOWN_CONTACTS, actName);
		}
			
		if (null == contact) {
            contact = mSkypeApp.getSkype().GetContact(actName);
        }
		
		if (null != contact) {
			Contact.TYPE type = contact.GetType();
			if (Contact.TYPE.SKYPE == type) {
				int code = contact.GetIntProperty(Contact.PROPERTY.availability);
				Contact.AVAILABILITY ava = Contact.AVAILABILITY.get(code);	
				Log.d(TAG, "havaVideoCallCapability: ava = " + ava);
				if (Contact.AVAILABILITY.ONLINE == ava ||
						Contact.AVAILABILITY.AWAY == ava ||
						Contact.AVAILABILITY.DO_NOT_DISTURB == ava)
				{
					bCapability = true;
				}
			}
		}
		return bCapability;
	}
	
	/**
	 * check contact's voicemail capability
	 * @param actName
	 * @return
	 */
	public boolean haveVoicemailCapability(String actName)
	{
		boolean bCapability = false;
		/*echo123 is a automatic sound test service of skype, filter it*/
		if (null == actName || actName.compareTo("echo123") == 0)
		{
			return false;
		}
		
		Contact contact = null;
		synchronized (this) {
			if (mBuddyMap.containsKey(actName)) {
				contact = mBuddyMap.get(actName).getContact();
			}
		}
		if (null == contact) {
			contact = findContact(ContactGroup.TYPE.ALL_KNOWN_CONTACTS, actName);
		}
		
		if (null == contact) {
            contact = mSkypeApp.getSkype().GetContact(actName);
        }
		
		if (null != contact) {
			Contact.TYPE type = contact.GetType();
			if (Contact.TYPE.SKYPE != type) {
				Log.d(TAG, "haveVoicemailCapability :" + actName + " Not a skype user" + ", " + type);
				return false;
			}
			
			int code = contact.GetIntProperty(Contact.PROPERTY.availability);
			Contact.AVAILABILITY ava = Contact.AVAILABILITY.get(code);
			if (Contact.AVAILABILITY.BLOCKED == ava) {
				return false;
			}
			
			bCapability = contact.HasCapability(Contact.CAPABILITY.CAPABILITY_VOICEMAIL, true);
			if (bCapability) {
				return bCapability;
			}
		}
		else 
		{
			//assert(false);
		}
		
		Account account = null;
		SktAccount sktAccout = mSkypeApp.getAccount();
		if (null != sktAccout) {
			account = sktAccout.getAccount();
		}
		if (null != account) {
			
			Account.GetCapabilityStatusResult status = account.GetCapabilityStatus(Contact.CAPABILITY.CAPABILITY_VOICEMAIL);
			bCapability = (status.status == Account.CAPABILITYSTATUS.CAPABILITY_EXISTS);
		}
		
		return bCapability;
	}
	
	/**
	 * check contact's skypeout capability
	 * @param actName
	 * @return
	 */
	public boolean haveSkypeoutCapability(String actName)
	{
		boolean bCapability = false;
		Contact contact = null;
		synchronized (this) {
			if (mBuddyMap.containsKey(actName)) {
				contact = mBuddyMap.get(actName).getContact();
			}
		}
		if (null == contact) {
			contact = findContact(ContactGroup.TYPE.ALL_KNOWN_CONTACTS, actName);
		}
		
		if (null != contact)
		{
			Contact.TYPE type = contact.GetType();
			if (Contact.TYPE.PSTN == type)
			{
				int code = contact.GetIntProperty(Contact.PROPERTY.availability);
				Contact.AVAILABILITY ava = Contact.AVAILABILITY.get(code);
				if (Contact.AVAILABILITY.BLOCKED == ava) {
					return false;
				}
				bCapability = contact.HasCapability(Contact.CAPABILITY.CAPABILITY_SKYPEOUT, true);
			}
		}
		//Log.d(TAG, "haveVoicemailCapability :" + actName + ": " + bCapability);
		return bCapability;
	}
	
	
	/**
	 * check contact's chat capability
	 * @param actName
	 * @return
	 */
	public boolean haveChatCapability(String actName)
	{
		boolean bCapability = false;
		Contact contact = null;
		synchronized (this) {
			if (mBuddyMap.containsKey(actName)) {
				contact = mBuddyMap.get(actName).getContact();
			}
		}
		if (null == contact) {
			contact = findContact(ContactGroup.TYPE.ALL_KNOWN_CONTACTS, actName);
		}
		
		if (null == contact) {
            contact = mSkypeApp.getSkype().GetContact(actName);
        }
		
		if (null != contact) {
			Contact.TYPE type = contact.GetType();
			if (Contact.TYPE.SKYPE == type) {
				int code = contact.GetIntProperty(Contact.PROPERTY.availability);
				Contact.AVAILABILITY ava = Contact.AVAILABILITY.get(code);
				if (Contact.AVAILABILITY.BLOCKED == ava) {
					return false;
				}
				bCapability = contact.HasCapability(Contact.CAPABILITY.CAPABILITY_TEXT, true);
			}
		}
		return bCapability;
	}
	
	/**
	 * Check whether the user in the buddy list
	 * @param actName
	 * @return true: in buddy list; false not in
	 */
	public synchronized boolean isMyBuddy(String actName)
	{
	    if (null == actName || actName.isEmpty())
	        return false;
		return mBuddyMap.containsKey(actName);
	}
	
	/**
	 * Check whether the user can be blocked
	 * @param actName
	 * @return true: ; false 
	 */
	public synchronized boolean canBlocked(String actName)
	{		
		if (null == actName || actName.isEmpty()) {
			return false;
		}
		
		Contact contact = null;
		synchronized (this) {
			if (mBuddyMap.containsKey(actName)) {
				contact = mBuddyMap.get(actName).getContact();
			}
		}
		if (null == contact) {
			contact = findContact(ContactGroup.TYPE.ALL_KNOWN_CONTACTS, actName);
		}
		
		if (null != contact)
		{
			int code = contact.GetIntProperty(Contact.PROPERTY.given_authlevel);
			Contact.AUTHLEVEL level = Contact.AUTHLEVEL.get(code);
			return (Contact.AUTHLEVEL.BLOCKED_BY_ME != level);
		}
		return false;
	}
	
	public synchronized Contact.TYPE getType(String actName) {
		if (null == actName || actName.isEmpty()) {
			return Contact.TYPE.UNRECOGNIZED;
		}
		Contact contact = null;
		synchronized (this) {
			if (mBuddyMap.containsKey(actName)) {
				contact = mBuddyMap.get(actName).getContact();
			}
		}
		if (null == contact) {
			contact = findContact(ContactGroup.TYPE.ALL_KNOWN_CONTACTS, actName);
		}
		if (null == contact) {
			contact = mSkypeApp.getSkype().GetContact(actName);
		}
		if (null != contact) {
			return contact.GetType();
		} else {
			return Contact.TYPE.UNRECOGNIZED;
		}
	}
	
	public boolean isPstnUser(String actName) {
		boolean ret = false;
		Contact.TYPE type = getType(actName);
		switch(type) {
		case PSTN:
		case EMERGENCY_PSTN:
		case FREE_PSTN:
		case UNDISCLOSED_PSTN:
			ret = true;
			break;
		default:
			ret = false;
		}
		
		return ret;
	}
	
	public boolean isSkypeUser(String actName) {
		boolean ret = false;
		Contact.TYPE type = getType(actName);
		switch(type) {
		case SKYPE:
			ret = true;
			break;
		default:
			ret = false;
		}
		
		return ret;
	}
	
	private void addContactToMyBuddies(Contact contact)
	{
		String actName = contact.GetIdentity();
		if (null == actName) {
			return;
		}
		if (mBuddyMap.containsKey(actName)) {
			return;
		}
		
		SktBuddy sb = new SktBuddy();
		if (null != sb) {
			sb.setActName(actName);
			sb.setContact(contact);
			sb.setDisplayName(contact.GetStrProperty(Contact.PROPERTY.displayname));
			sb.setFullName(contact.GetStrProperty(Contact.PROPERTY.fullname));
			int code = contact.GetIntProperty(Contact.PROPERTY.availability);
			sb.setAvalabiltity(Contact.AVAILABILITY.get(code));
			if (SktDataCfg.existsAvatar(actName)) {
				sb.setAvatar(SktDataCfg.readAvatar(actName));
			}
			mBuddyMap.put(actName, sb);
		}
	}
	
	private void removeContactFromMyBuddies(Contact contact) {
		String actName = contact.GetIdentity();
		if (null == actName) {
			return;
		}
		if (mBuddyMap.containsKey(actName)) {
			mBuddyMap.remove(actName);
		}
	}
}
