package com.jrm.skype.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.jrm.skype.util.Log;

public class SktDataCfg {
	
	private static final String TAG = "SktDataCfg";
	private static String AVATAR_PATH = "";
	//private Properties mAvatarProper;
	
	public SktDataCfg()
	{
		//mAvatarProper = new Properties();
	}
	
	public static void setAvatarPath(File file) {
		if (null != file) {
			AVATAR_PATH = file.getAbsolutePath();
			if (!AVATAR_PATH.endsWith("/")) {
				AVATAR_PATH += "/";
			}
		}
		
	}
	
	public static byte[] readAvatar(String actName)
	{
		String path = AVATAR_PATH + actName + ".jpg";
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		
		byte data[] = null;
		FileInputStream input = null;
		
		try {
			int length = (int)file.length();
			boolean available = true;
			if (length > 1024 * 512) {
				available = false;
				Log.d(TAG, path + " is too large!!!");
			}
			if (length == 0) {
				available = false;
			}
			if (available) {
				data = new byte[length];
				input = new FileInputStream(file);
				input.read(data);
				SktUtils.closeStream(input);
				input = null;
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			SktUtils.closeStream(input);	
			input = null;
		}
		return data;
	}
	
	public static boolean writeAvatar(String actName, byte data[])
	{
		String path = AVATAR_PATH + actName + ".jpg";
		File file = new File(AVATAR_PATH);
		if (!file.exists()) {
			file.mkdirs();
			file = null;
		}
		file = new File(path);
		if (file.exists()) {
			file.delete();
		}

		boolean available = false;
		FileOutputStream output = null;
		try {
			/*
			if (!file.exists()) {
				file.createNewFile();
			}*/
			if (null != data && data.length > 0) {
				output = new FileOutputStream(file);
				output.write(data);
				SktUtils.closeStream(output);
				output = null;
				if (data.length != file.length()) {
					file.delete();
				} else {
					available = true;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			if (file.exists()) {
				file.delete();
			}
		} finally {
			SktUtils.closeStream(output);
			output= null;
		}
		
		return available;
	}
	
	public static boolean existsAvatar(String actName)
	{
		String path = AVATAR_PATH + actName + ".jpg";
		File file = new File(path);
		boolean bExists = file.exists();
		file = null;
		return bExists;
	}
	
	/*
	public void loadAvatarCfg() 
	{
		try {
			String path = CFG_PATH + AVATAR_CFG;
			FileInputStream input = new FileInputStream(path);
			mAvatarProper.clear();
			mAvatarProper.load(input);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void saveAvatarCfg()
	{
        try {    
            String path = CFG_PATH + AVATAR_CFG;
        	FileOutputStream output = new FileOutputStream(path);
        	mAvatarProper.store(output, null);     
            output.close();        
        } catch (IOException e) {     
            e.printStackTrace();     
        }     
	}
	
	
	public void updateAvatarTimestamp(String actName, Integer timestamp)
	{
		mAvatarProper.setProperty(actName, timestamp.toString());
	}
	
	public Integer getAvatarTimeStamp(String actName)
	{
		Integer val = 0;
		try {
			val = Integer.parseInt(mAvatarProper.getProperty(actName, "0"));
		}catch(NumberFormatException e) {
			e.printStackTrace();
		}
		return val;
	}
	*/

}
