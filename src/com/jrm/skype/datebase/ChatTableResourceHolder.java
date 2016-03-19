
package com.jrm.skype.datebase;

import com.jrm.skype.datebase.ChatTableActor.ChatTableInfoHolder;

import android.content.ContentValues;

/**
 * @author andy.liu 
 * The class  contains all of the data of a column of a chat
 *         table
 */
public class ChatTableResourceHolder extends BaseTableResourceHolder {
    private String mChatConvName; // for different contacts

    private String mChatDisName; //Me for the user,the displayname for the contact

    private String mChatString;

    private String mChatTime;

    private String mChatDate;
    
    private String mChatRead;
    
//    public String getChatConvName() {
//        return mChatConvName;
//    }

    public void setChatConvName(String mChatConvName) {
        this.mChatConvName = mChatConvName;
    }

    public String getChatDisplayName() {
        return mChatDisName;
    }

    public void setChatDisplayName(String mChatDisName) {
        this.mChatDisName = mChatDisName;
    }

   
    public String getChatString() {
        return mChatString;
    }

    public void setChatString(String mChatString) {
        this.mChatString = mChatString;
    }

    public String getChatTime() {
        return mChatTime;
    }

    public void setChatTime(String mChatTime) {
        this.mChatTime = mChatTime;
    }

    public String getChatDate() {
        return mChatDate;
    }

    public void setChatDate(String mChatDate) {
        this.mChatDate = mChatDate;
    }

    public String getChatRead() {
        return mChatRead;
    }

    public void setChatRead(String mChatRead) {
        this.mChatRead = mChatRead;
    }

    @Override
    public ContentValues createContentValues() {
        ContentValues values = new ContentValues();
        values.put(ChatTableInfoHolder.CHATCONVNAME, mChatConvName != null ? mChatConvName : "null");
        values.put(ChatTableInfoHolder.CHATDISPLAYNAME, mChatDisName != null ? mChatDisName : "null");
        values.put(ChatTableInfoHolder.CHATSTRING, mChatString != null ? mChatString : "null");
        values.put(ChatTableInfoHolder.CHATTIME, mChatTime != null ? mChatTime : "null");
        values.put(ChatTableInfoHolder.CHATDATE, mChatDate != null ? mChatDate : "null");
        values.put(ChatTableInfoHolder.CHATREAD, mChatRead != null ? mChatRead : "yes");
        return values;
    }
}
