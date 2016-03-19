package com.jrm.skype.api;

import com.jrm.skype.util.Log;
import com.skype.api.Contact;
import com.skype.api.Account;
import com.skype.api.Skype;
import com.skype.api.Skype.VALIDATERESULT;

public class SktAccount {
	
	private static final String TAG = "SktAccout";
	
	private Account mAccount;
	private byte[] mAvatar;
	private SktSkypeApp mSkypeApp;
	private Account.CBLSYNCSTATUS mSyncStatus;
	
	public SktAccount(SktSkypeApp skypeApp)
	{
		mAccount = null;
		mAvatar = null;
		mSkypeApp = skypeApp;
		mSyncStatus = Account.CBLSYNCSTATUS.CBL_INITIALIZING;
	}
	
	protected synchronized int getOid()
	{
		if (null != mAccount)
		{
			return mAccount.getOid();
		}
		
		return 0;
	}
	
	protected synchronized Account getAccount()
	{
		return mAccount;
	}
	
	public synchronized String getAccountName()
	{
		if (null == mAccount) {
			return "";
		} else {
			return mAccount.GetStrProperty(Account.PROPERTY.skypename);
		}
	}
	
	/**
	 * Loggin with default account
	 * @return login status. true: Logging In; false: Login failed
	 */
	public synchronized boolean loginWithDefaultAccount()
	{	
		if (mSkypeApp.isLoggedIn()) {
			Log.d(TAG, "alread loged in");
			return true;
		}
		
		if (null != mAccount) {
			int v = mAccount.GetIntProperty(Account.PROPERTY.status);
			if (v > Account.STATUS.LOGGED_OUT_AND_PWD_SAVED.getId()) {
				Log.d(TAG, "You have to log out first");
				return false;
			}
		}
		
		String actName = mSkypeApp.getSkype().GetDefaultAccountName();
		if (null == actName || actName.isEmpty()) {
			return false;
		}
		mAccount = mSkypeApp.getSkype().GetAccount(actName);
		if (null == mAccount) {
			Log.d(TAG, "Unable to get account");
			return false;
		}
		int v = mAccount.GetIntProperty(Account.PROPERTY.status);
		if (v != Account.STATUS.LOGGED_OUT_AND_PWD_SAVED.getId()) {
			Log.d(TAG, "Password was not saved for given account: unable to login, use aL");
			return false;
		} else {
			mAccount.Login(Contact.AVAILABILITY.ONLINE);
			return true;
		}
	}
	
	/**
	 * Login in an account by specifying its password
	 * @param actName <br> skypename
	 * @param password 
	 * @param savePwd <br> true: Saves the password, ensuring that auto-login is enabled. <br> - false (default): Does not save the password.
	 * @return login status. true: Logging In will fire SktSkypeApp.onOnlineStatusChange; false: Login failed.
	 */
	public synchronized boolean login(String actName, String password, boolean savePwd)
	{		
		if (mSkypeApp.isLoggedIn()) {
			Log.d(TAG, "alread loged in");
			return true;
		}

		if (null == actName || actName.isEmpty()) {
			return false;
		}
		
		mAccount = mSkypeApp.getSkype().GetAccount(actName);
		Log.d(TAG, "login: mAccount =" + mAccount);
		if (null != mAccount) {
			mAccount.LoginWithPassword(password, savePwd, true);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Logout
	 */
	public synchronized void logout(boolean clearSavedPwd)
	{
		if (null != mAccount) 
		{
			mAccount.Logout(clearSavedPwd);
		}
	}
	
	/**
	 * Get login status
	 * @return status
	 */
	public synchronized Account.STATUS getStatus()
	{		
		if (null != mAccount)
		{
			int v = mAccount.GetIntProperty(Account.PROPERTY.status);
			return Account.STATUS.get(v);
		}
		return Account.STATUS.LOGGED_OUT;
	}
	
	/**
	 * Set online status (online/offline/away/do not trouble/invisibale)
	 * @param availability 
	 */
	public synchronized void setAvailability(Contact.AVAILABILITY availability)
	{
		if (null != mAccount)
		{
			mAccount.SetAvailability(availability);
		}
	}
	
	/**
	 * get online status
	 * @param availability 
	 */
	public synchronized Contact.AVAILABILITY getAvailability()
	{
		if (null != mAccount)
		{
			return Contact.AVAILABILITY.get(mAccount.GetIntProperty(Account.PROPERTY.availability));
		}
		return Contact.AVAILABILITY.OFFLINE;
	}
	
	/**
	 * 鍒涘缓鏂板笎鎴� UI搴旇鍦ㄨ皟鐢ㄨ鍑芥暟鍓嶈皟鐢⊿ktSkypeApp鐨刢heckProfileString銆乧heckPassword棰勫厛妫�煡鍙傛暟鏈夋晥鎬�
	 * @param actName <br> 鍚嶇О
	 * @param password <br> 瀵嗙爜
	 * @param savePwd <br> 鏄惁淇濆瓨瀵嗙爜. true: 淇濆瓨; false: 涓嶄繚瀛�
	 * @param email <br> email
	 * @param allowSpam <br> 鏄惁鎺ユ敹Skype鏂伴椈. true: 鎺ユ敹; fasle:涓嶆帴鏀�
	 * @return 鍒涘缓鐘舵�. true锛氭鍦ㄥ垱寤轰腑; false: 鍒涘缓澶辫触
	 */
	public boolean createNewAccount(String actName, String password, boolean savePwd, String email, boolean allowSpam)
	{
		Account.STATUS status = getStatus();
		if (status != Account.STATUS.LOGGED_OUT && status != Account.STATUS.LOGGED_OUT_AND_PWD_SAVED)
		{
			Log.d(TAG, "createNewAccount: already logged in, please logged out first and the try it again");
			return false;
		}
			
		synchronized (this) {
			mAccount = mSkypeApp.getSkype().GetAccount(actName);
			Log.d(TAG, "createNewAccount: skypename = " + actName);
			if (null == mAccount) {
				Log.d(TAG, "createNewAccount: Unable to get account");
				return false;
			} else {

				mAccount.Register(password, savePwd, true, email, allowSpam);
				return true;
			}
		}
	}
	
	public void cancelCreateNewAccount()
	{
		logout(false);
	}
	
	/**
	 * 鑾峰彇甯愭埛浣欓
	 * @return 甯愭埛浣欓
	 */
	public synchronized float getBalance()
	{
		if (null == mAccount) {
			return 0.0f;
		}
		
		int balance = mAccount.GetIntProperty(Account.PROPERTY.skypeout_balance);
		int precision = mAccount.GetIntProperty(Account.PROPERTY.skypeout_precision);
		if (precision <= 0) {
			return 0.0f;
		}
		else {
			return (float)(balance/Math.pow(10, precision));
		}
	}
	
	/**
	 * 鑾峰彇璐у竵鍚嶇О
	 * @return 璐у竵鍚嶇О
	 */
	public synchronized String getCurrency()
	{
		if (null == mAccount) {
			return "";
		}
		return mAccount.GetStrProperty(Account.PROPERTY.skypeout_balance_currency);
	}
	
	/**
	 * 淇敼甯愭埛瀵嗙爜
	 * @param oldPwd <br> 鏃у瘑鐮�
	 * @param newPwd <br> 鏂板瘑鐮�
	 * @return 杩斿洖淇敼鐘舵�. true: 姝ｅ湪淇敼涓� false: 淇敼澶辫触;
	 */
	public boolean changePassword(String oldPwd, String newPwd)
	{				
		if (getStatus() != Account.STATUS.LOGGED_IN) {
			Log.d(TAG, "createNewAccount: please logged in, then try it again");
			return false;
		}
		
		String actName = getAccountName();
		if (null == actName || actName.isEmpty()) {
			return false;
		}
		
		synchronized (this) {
			VALIDATERESULT vResult = mSkypeApp.checkPassword(actName, newPwd);
			if (vResult != VALIDATERESULT.VALIDATED_OK) {
				return false;
			}

			mAccount.ChangePassword(oldPwd, newPwd, true);
			return true;
		}
	}

	/**
	 * 璁剧疆Standby
	 * @param bEnable <br>
	 */
	public synchronized void changeStandby(boolean bEnable)
	{
		if (null == mAccount) {
			return;
		}
		mAccount.SetStandby(bEnable);
	}
	
	/**
	 * Set FullName
	 * @param fname <br> fullname
	 */
	public synchronized boolean setFullName(String fname)
	{
		if (null == mAccount) {
			return false;
		}
		
		mAccount.SetStrProperty(Account.PROPERTY.fullname.getId(), fname.trim());
		return true;
	}
	
	/**
	 * Get FullName
	 * @return FullName
	 */
	public synchronized String getFullName() 
	{
		if (null == mAccount) {
			return "";
		}
		return mAccount.GetStrProperty(Account.PROPERTY.fullname);
	}
	
	/**
	 * Set MoodText
	 * @param mood <br> MoodText
	 */
	public synchronized boolean setMoodText(String mood)
	{
		if (null == mAccount) {
			return false;
		}
		
		mAccount.SetStrProperty(Account.PROPERTY.mood_text.getId(), mood);
		return true;
	}
	
	/**
	 * Get MoodText
	 * @return MoodText
	 */
	public synchronized String getMoodText() 
	{
		if (null == mAccount) {
			return "";
		}
		return mAccount.GetStrProperty(Account.PROPERTY.mood_text);
	}
	
	/**
	 * Set Email
	 * @param email 
	 */
	public synchronized boolean setEmail(String email)
	{
		if (null == mAccount) {
			return false;
		}
				
		Skype.ValidateProfileStringResult vpsResult;
		vpsResult = mSkypeApp.checkProfileString(Contact.PROPERTY.emails.getId(), email, false);
		if (null == vpsResult || vpsResult.result != VALIDATERESULT.VALIDATED_OK)
		{
			return false;
		}
		mAccount.SetStrProperty(Account.PROPERTY.emails.getId(), email);
		return true;
	}
	
	/**
	 * Get Emial
	 * @return Email
	 */
	public synchronized String getEmail() 
	{
		if (null == mAccount) {
			return "";
		}
		return mAccount.GetStrProperty(Account.PROPERTY.emails);
	}
	
	/**
	 * Set HomePage
	 * @param HomePage
	 */
	public synchronized void setHomePage(String homepage)
	{
		if (null == mAccount) {
			return;
		}
		mAccount.SetStrProperty(Account.PROPERTY.homepage.getId(), homepage);
	}
	
	/**
	 * Get HomePage
	 * @return HomePage
	 */
	public synchronized String getHomePage() 
	{
		if (null == mAccount) {
			return "";
		}
		return mAccount.GetStrProperty(Account.PROPERTY.homepage);
	}
	
	/**
	 * 璁剧疆鍔炲叕鐢佃瘽
	 * @param phone <br> 鍔炲叕鐢佃瘽
	 */
	public synchronized void setOfficePhone(String phone)
	{
		if (null == mAccount) {
			return;
		}
		mAccount.SetStrProperty(Account.PROPERTY.phone_office.getId(), phone);
	}
	
	/**
	 * 鑾峰彇鍔炲叕鐢佃瘽
	 * @return 鍔炲叕鐢佃瘽
	 */
	public synchronized String getOfficePhone() 
	{
		if (null == mAccount) {
			return "";
		}
		return mAccount.GetStrProperty(Account.PROPERTY.phone_office);
	}
	
	/**
	 * 璁剧疆瀹跺涵鐢佃瘽
	 * @param phone <br> 瀹跺涵鐢佃瘽
	 */
	public synchronized void setHomePhone(String phone)
	{
		if (null == mAccount) {
			return;
		}
		mAccount.SetStrProperty(Account.PROPERTY.phone_home.getId(), phone);
	}
	
	/**
	 * 鑾峰彇瀹跺涵鐢佃瘽
	 * @return 瀹跺涵鐢佃瘽
	 */
	public synchronized String getHomePhone() 
	{
		if (null == mAccount) {
			return "";
		}
		return mAccount.GetStrProperty(Account.PROPERTY.phone_home);
	}
	
	/**
	 * Set mobile
	 * @param phone <br> mobile number
	 */
	public synchronized void setMobilePhone(String phone)
	{
		if (null == mAccount) {
			return;
		}
		mAccount.SetStrProperty(Account.PROPERTY.phone_mobile.getId(), phone);
	}
	
	/**
	 * Get mobile
	 * @return mobile number
	 */
	public synchronized String getMobilePhone() 
	{
		if (null == mAccount) {
			return "";
		}
		return mAccount.GetStrProperty(Account.PROPERTY.phone_mobile);
	}
	
	/**
	 * Set birthday.
	 * @param birthday <br> birthday, format is YYYYMMDD
	 */
	public synchronized void setBirthday(int birthday)
	{
		if (null == mAccount) {
			return;
		}
		mAccount.SetIntProperty(Account.PROPERTY.birthday.getId(), birthday);
	}
	
	/**
	 * Get Birthday
	 * @return birthday <br> birthday, format is YYYYMMDD
	 */
	public synchronized int getBirthday() 
	{
		if (null == mAccount) {
			return -1;
		}
		return mAccount.GetIntProperty(Account.PROPERTY.birthday);
	}
	
	/**
	 * Set gender
	 * @param gander <br> 1-male, 2-female , 0-not set gender
	 */
	public synchronized void setGender(int gender)
	{
		if (null == mAccount) {
			return;
		}
		assert(gender == 0 || gender == 1 || gender == 2);
		mAccount.SetIntProperty(Account.PROPERTY.gender.getId(), gender);
	}
	
	
	/**
	 * Get gander
	 * @return 1-male, 2-female
	 */
	public synchronized int getGender() 
	{
		if (null == mAccount) {
			return 0;
		}
		return mAccount.GetIntProperty(Account.PROPERTY.gender);
	}
	
	/**
	 * set ISO language codes
	 * @param language <br> ISO language codes
	 */
	public synchronized void setLanguage(String language)
	{
		if (null == mAccount) {
			return;
		}
		mAccount.SetStrProperty(Account.PROPERTY.languages.getId(), language);
	}
	
	/**
	 * get ISO language codes
	 * @return if true retrun ISO language codes else return ""
	 */
	public synchronized String getLanguage()
	{
		if (null == mAccount) {
			return "";
		}
		return mAccount.GetStrProperty(Account.PROPERTY.languages);
	}
	
	/**
	 * Set country
	 * @param country
	 */
	public synchronized void setCountry(String country)
	{
		if (null == mAccount) {
			return;
		}
		mAccount.SetStrProperty(Account.PROPERTY.country.getId(), country);
	}
	
	/**
	 * Get country
	 * @return country
	 */
	public synchronized String getCountry()
	{
		if (null == mAccount) {
			return "";
		}
		return mAccount.GetStrProperty(Account.PROPERTY.country);
	}
	
	/**
	 * Set region
	 * @param region
	 */
	public synchronized void setProvice(String region)
	{
		if (null == mAccount) {
			return;
		}
		mAccount.SetStrProperty(Account.PROPERTY.province.getId(), region);
	}
	
	/**
	 * Get region
	 * @return region
	 */
	public synchronized String getProvice()
	{
		if (null == mAccount) {
			return "";
		}
		return mAccount.GetStrProperty(Account.PROPERTY.province);
	}
	

	/**
	 * Set city
	 * @param city
	 */
	public synchronized void setCity(String city)
	{
		if (null == mAccount) {
			return;
		}
		mAccount.SetStrProperty(Account.PROPERTY.city.getId(), city);
	}
	
	/**
	 * Get city
	 * @return city
	 */
	public synchronized String getCity()
	{
		if (null == mAccount) {
			return "";
		}
		return mAccount.GetStrProperty(Account.PROPERTY.city);
	}
	
	/**
	 * Set about string
	 * @param about
	 */
	public synchronized void setAbout(String about)
	{
		if (null == mAccount) {
			return;
		}
		mAccount.SetStrProperty(Account.PROPERTY.about.getId(), about);
	}
	
	/**
	 * Get about string
	 * @return string
	 */
	public synchronized String getAbout()
	{
		if (null == mAccount) {
			return "";
		}
		return mAccount.GetStrProperty(Account.PROPERTY.about);
	}
	
	/**
	 * Set timezone
	 * @param tz
	 */
	public synchronized void setTimeZone(int tz)
	{
		if (null == mAccount) {
			return;
		}
		mAccount.SetIntProperty(Account.PROPERTY.timezone.getId(), tz);
	}
	
	/**
	 * Get timezone
	 * @return
	 */
	public synchronized int getTimeZone()
	{
		if (null == mAccount) {
			return 0;
		}
		return mAccount.GetIntProperty(Account.PROPERTY.timezone);
	}
	
	/**
	 * Get guggested skypeName
	 * @return
	 */
	public synchronized String getSuggestedSkypeName()
	{
		if (null == mAccount) {
			return "";
		}
		return mAccount.GetStrProperty(Account.PROPERTY.suggested_skypename);
	}
	
	/**
	 * Set avatar
	 * @param avatar
	 */
	public synchronized void setAvatar(byte[] avatar)
	{
		if (null == mAccount || null == avatar) {
			return;
		}
		//mAccount.SetIntProperty(Account.PROPERTY.avatar_timestamp.getId(), 0);
		//Log.d(TAG, "avatar_timestamp = " + mAccount.GetIntProperty(Account.PROPERTY.avatar_timestamp));
		
		
		mAvatar = avatar;
		String actName = mAccount.GetStrProperty(Account.PROPERTY.skypename);
		Contact contact = mSkypeApp.getSkype().GetContact(actName);
		mAccount.SetBinProperty(Account.PROPERTY.avatar_image.getId(), avatar);
		if (null != contact) {
			contact.RefreshProfile();
		}
	}
	
	/**
	 * Get avatar
	 * @return
	 */
	public synchronized byte[] getAvatar()
	{
		return mAvatar;
	}
	
	/**
	 * Update local avatar
	 * @param avatar
	 */
	public synchronized void updateLocalAvatar(byte[] avatar)
	{
		if (null != avatar) {
			mAvatar = avatar;
		}
		mSkypeApp.updateAccountAvatar(mAccount.getOid());
	}
	
	/**
	 * Request avatar
	 * @return
	 */
	protected void requestAvatar()
	{
		Contact contact = null;
		String actName = getAccountName();
		byte[] jpg = SktDataCfg.readAvatar(actName);
		
		contact = mSkypeApp.getSkype().GetContact(actName);
		if (null != contact) {
			contact.RefreshProfile();
			SktUtils.sleep(300);
			Contact.GetAvatarResult avatar = contact.GetAvatar();
			Log.d(TAG, "requestAvatar:");
		    if ((avatar != null) && avatar.present) 
		    {
		    	jpg = avatar.avatar;
		    	SktDataCfg.writeAvatar(actName, jpg);
		    	Log.d(TAG, "requestAvatar: avatar = " + (avatar != null ? "Ok" : "null"));
		    }
		}
		updateLocalAvatar(jpg);
	}
	
	public boolean haveVoicemailCapability(){
	    boolean bCapability = false;
        if (null != mAccount) {
            
            Account.GetCapabilityStatusResult status = mAccount.GetCapabilityStatus(Contact.CAPABILITY.CAPABILITY_VOICEMAIL);
            bCapability = (status.status == Account.CAPABILITYSTATUS.CAPABILITY_EXISTS);
        }
        
        return bCapability;
	}
	public boolean isMySlef(String actName) {
		String myName = getAccountName();
		return (0 == myName.compareTo(actName));
	}
	
	protected synchronized void setSyncStatus(int val)
	{
		mSyncStatus = Account.CBLSYNCSTATUS.get(val);
	}
	
	protected synchronized Account.CBLSYNCSTATUS getSyncStatus() {
		if (null != mAccount) {
			if (Account.CBLSYNCSTATUS.CBL_INITIALIZING == mSyncStatus ||
				  Account.CBLSYNCSTATUS.CBL_SYNC_FAILED == mSyncStatus ) {
				int code = mAccount.GetIntProperty(Account.PROPERTY.cblsyncstatus);
				mSyncStatus = Account.CBLSYNCSTATUS.get(code);
			}
			return mSyncStatus;
			
		} else {
			return Account.CBLSYNCSTATUS.CBL_SYNC_FAILED;
		}
		
	}
}
