package com.jrm.skype.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.jrm.skype.util.Log;
import com.jrm.skype.api.SktContact.SktAuthItem;
import com.jrm.skype.api.SktContact.SktBuddy;
import com.skype.api.Account;

public class SktUtils {
	
	private static final String TAG = "SktUtils";

	
	//----------------------------Utils----------------------
	
	public static void sleep(int milliseconds)
	{
		try {
			Thread.sleep(milliseconds);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeStream(InputStream input)
	{
		if (null != input)
		{
			try {
				input.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closeStream(OutputStream output)
	{
		if (null != output)
		{
			try {
				output.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void exec(String cmd) {
		try {
//			String env[] = new String[1];
//			env[0] = new String("HOME=/data/data/com.jrm.skype.ui/files");
			if (!cmd.startsWith("busybox")) {
				cmd = "busybox " + cmd;
			}
			Log.d(TAG, "cmd = " + cmd);
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean setFileExecutable(String fileName, boolean executable) {
		boolean ret = false;
		File file = new File(fileName);
		if (null != file && file.exists()) {
			file.setExecutable(true);
			ret = true;
		}
		file = null;
		return ret;
	}
	
	public static void mkdirs(String path)
	{
		File dir = new File(path);
		if (null != dir) {
			if (!dir.exists()) {
				dir.mkdirs();
			}
			dir = null;
		}
	}
	
	public static boolean fileExists(String name) {
		boolean ret = false;
		File file = new File(name);
		if (null != file) {
			ret = file.exists();
		}
		return ret;
	}
	
	public static boolean micExists() {
		File file = new File("/dev/snd/dsp1");
		return file.exists();
	}
	
	public static boolean cameraExists() {
		File file = new File("/dev/video0");
		if(!file.exists()){
		    file = new File("/dev/video1");
		    if(!file.exists()){
		        file = new File("/dev/video2"); 
		        return file.exists();
		    }
		} 
		    
		return true;
	}
	
	public static boolean isNetworkAvailable(Context context)
	{
		ConnectivityManager cm = null;
		cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null == cm)
		{
			return false;
		}
		else
		{
			NetworkInfo[] network = cm.getAllNetworkInfo();
			if (null != network) {
				for (int i = 0; i < network.length; i++) {
					if (network[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	//---------------------------- SktSkypeApp Interface ----------------------
	
	
	public static SktSkypeApp getSkypeApp() {
		return SktSkypeApp.getInstance();
	}
	
	
	//---------------------------- SktAccount Interface ----------------------
	
	/**
	 * Get SktAccount
	 * @return 
	 */
	public static SktAccount getAccount() {
		SktSkypeApp sktSkype = SktSkypeApp.getInstance();
		if (null != sktSkype) {
			return sktSkype.getAccount();
		} else {
			return null;
		}
	}
	
	/**
	 * Send request for get account's avatar
	 */
	public static void getMyAvatar()
	{
		SktAccount act = getAccount();
		if (null != act) {
			act.requestAvatar();
		}
	}
	
	/**
	 * Get Account's SYNC status
	 * @return SYNC status
	 */
	public static Account.CBLSYNCSTATUS getSyncStatus() {
		SktAccount act = getAccount();
		if (null != act) {
			return act.getSyncStatus();
		} else {
			return Account.CBLSYNCSTATUS.CBL_SYNC_FAILED;
		}
	}
	
	
	//---------------------------- SktContact Interface ----------------------
	public static SktContact getContact() {
		SktSkypeApp sktSkype = SktSkypeApp.getInstance();
		if (null != sktSkype) {
			return sktSkype.getContact();
		} else {
			return null;
		}
	}
	
	public static SktBuddy getBuddy(String actName) {
		SktContact contact = getContact();
		if (null != contact) {
			return contact.getBuddy(actName);
		} else {
			return null;
		}
	}
	
	public static ArrayList<SktBuddy> getBuddyList() {
		SktContact contact = getContact();
		if (null != contact) {
			return contact.getBuddyList();
		} else {
			return null;
		}
	}
	
	public static ArrayList<SktAuthItem> getAuthReqList() {
		SktContact contact = getContact();
		if (null != contact) {
			return contact.getAuthRequestList();
		} else {
			return null;
		}
	}
	
	public static int getBuddyNums() {
		SktContact contact = getContact();
		if (null != contact) {
			return contact.getBuddyNums();
		} else {
			return 0;
		}
	}
	
	public static boolean search(String key) {
		SktContact contact = getContact();
		if (null != contact) {
			return contact.search(key);
		} else {
			return false;
		}
	}
	
	public static boolean searchByName(String key) {
		SktContact contact = getContact();
		if (null != contact) {
			return contact.searchByName(key);
		} else {
			return false;
		}
	}
	
	/**
	 * Check whether the user can be added to contact list
	 * @param actName
	 * @return 0: can add; 1: my buddy; 2: my self
	 */
	public static int canAddToContactList(String actName) {
		SktContact contact = getContact();
		if (null != contact) {
			if (contact.isMyBuddy(actName)) {
				return 1;
			}
		}

		SktAccount account = getAccount();
		if (null != account) {
			if (account.isMySlef(actName)) {
				return 2;
			}
		}
		
		return 0;
	}
	
	
	//---------------------------- SktConversation Interface ----------------------
	
	/**
	 * Get SktConversation
	 * @return 
	 */
	public static SktConversation getConversation()
	{
		SktSkypeApp sktSkype = SktSkypeApp.getInstance();
		if (null != sktSkype) {
			return sktSkype.getConversation();
		} else {
			return null;
		}
	}
	
	/**
	 * Init all the conversations
	 */
	public static void initConvs()
	{
		SktConversation conv = getConversation();
		if (null != conv) {
			conv.initAllConvs();
		}
	}
	
	//---------------------------- SktConversation Interface ----------------------
	public static SktOption getOption()
	{
		SktSkypeApp sktSkype = SktSkypeApp.getInstance();
		if (null != sktSkype) {
			return sktSkype.getOption();
		} else {
			return null;
		}
	}
}
