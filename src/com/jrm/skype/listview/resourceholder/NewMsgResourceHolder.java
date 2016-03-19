package com.jrm.skype.listview.resourceholder;

import java.util.ArrayList;
import android.database.Cursor;
import com.jrm.skype.datebase.ChatTableActor.ChatTableInfoHolder;
import com.jrm.skype.datebase.ChatTableResourceHolder;
import com.jrm.skype.util.DateFormat;
import com.jrm.skype.util.SktApiActor;

public class NewMsgResourceHolder {
    private ArrayList<Object> mlistItem;
    
    private DateFormat mDateFormat;
    
    public NewMsgResourceHolder(){
        mlistItem = new ArrayList<Object>();
        mDateFormat = new DateFormat();
    }
    
    public void addList(String convName,String fromName,long timestamp,String body){
        ChatTableResourceHolder msgResource = new ChatTableResourceHolder();
        
        msgResource.setChatConvName(convName);
        msgResource.setChatString(body);
        msgResource.setChatTime(mDateFormat.getFormatString(timestamp, "HH:mm:ss"));
        msgResource.setChatDisplayName(SktApiActor.getContactDisplayName(fromName));
        msgResource.setChatDate(mDateFormat.getFormatString(timestamp, "yyyy-MM-dd"));
        mlistItem.add(msgResource);
    }
    
    public void setList(Cursor csr){
        if (null != csr && csr.getCount() > 0){
            csr.moveToFirst();
            ChatTableResourceHolder msgResource = new ChatTableResourceHolder();
            
            msgResource.setChatConvName(csr.getString(csr.getColumnIndex(ChatTableInfoHolder.CHATCONVNAME)));
            msgResource.setChatString(csr.getString(csr.getColumnIndex(ChatTableInfoHolder.CHATSTRING)));
            msgResource.setChatTime(csr.getString(csr.getColumnIndex(ChatTableInfoHolder.CHATTIME)));
            msgResource.setChatDisplayName(csr.getString(csr.getColumnIndex(ChatTableInfoHolder.CHATDISPLAYNAME)));
            msgResource.setChatDate(csr.getString(csr.getColumnIndex(ChatTableInfoHolder.CHATDATE)));
            mlistItem.add(msgResource); 
            
            while (!csr.isLast())
             {
                csr.moveToNext();
                msgResource = new ChatTableResourceHolder();
                
                msgResource.setChatConvName(csr.getString(csr.getColumnIndex(ChatTableInfoHolder.CHATCONVNAME)));
                msgResource.setChatString(csr.getString(csr.getColumnIndex(ChatTableInfoHolder.CHATSTRING)));
                msgResource.setChatTime(csr.getString(csr.getColumnIndex(ChatTableInfoHolder.CHATTIME)));
                msgResource.setChatDisplayName(csr.getString(csr.getColumnIndex(ChatTableInfoHolder.CHATDISPLAYNAME)));
                msgResource.setChatDate(csr.getString(csr.getColumnIndex(ChatTableInfoHolder.CHATDATE)));
                mlistItem.add(msgResource); 
            }
        }
    }
    
    public void clearList(){
        mlistItem.clear();
    }
    
    public ArrayList<Object> getResourceList(){
        return mlistItem;
    }

}
