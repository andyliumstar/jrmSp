
package com.jrm.skype.datebase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.jrm.skype.util.DigestUtil;
import com.jrm.skype.util.Log;

/**
 * @author andy.liu 
 * ChatTableActor is in change of the act for ChatTable
 */
public class ChatTableActor {
    private final static String TAG = "ChatTableActor";
    
    private DateBaseHelper mDateBaseHelper;

    private ChatTableInfoHolder mChatTableInfoHolder;
    
    private static ChatTableActor CHAT_INSTANCE = null;
    
    public synchronized static void initChatTableActor(String usrName, Context context){
        if (null == CHAT_INSTANCE) {
            CHAT_INSTANCE = new ChatTableActor(usrName, context);
		}
        if (null != CHAT_INSTANCE) {
        	CHAT_INSTANCE.mChatTableInfoHolder.setUsrName(usrName);
		}
    }
    
    public static ChatTableActor getInstance(){
        if (null == CHAT_INSTANCE){
            Log.e(TAG, "you should init ChatTableActor first");
        }
        return CHAT_INSTANCE;
    }

    public static void releaseChatTableActor(){
        CHAT_INSTANCE = null;
    }
    /**
     * @param usrName the skype name of the user
     * @param context
     */
    private ChatTableActor(String usrName, Context context) {
        mDateBaseHelper = DateBaseHelper.getInstance(context, DateBaseHelper.DATEBASENAME);
        mChatTableInfoHolder = new ChatTableInfoHolder(usrName);
    }

    /**
     * set the TableInfo for DateBaseHelper
     */
    public void prepare() {
        mDateBaseHelper.setTableInfoHolderAndCreate(mChatTableInfoHolder);
    }

    /**
     * @param chatTableResourceHolder the holder of the data to insert
     */
    public  void insert(ChatTableResourceHolder chatTableResourceHolder) {
        prepare();
        mDateBaseHelper.insert(chatTableResourceHolder.createContentValues());
    }

    /**
     * @param column the column of the table
     * @param whereValue the value of column
     */
    public synchronized void delete(String colum, String[] whereValue) {
        prepare();
        mDateBaseHelper.delete(colum, whereValue);
    }
    
    public synchronized void update(ContentValues values,String colum, String[] whereValue) {
        prepare();
        mDateBaseHelper.update(values, colum, whereValue);
    }
    
    public synchronized Cursor select(String selection,String [] selectionArgs){
        prepare();
        return mDateBaseHelper.select(selection,selectionArgs);
    }


    public synchronized Cursor selectAll() {
        prepare();
        return mDateBaseHelper.selectAll();
    }

    /**
     * @return the total number of select
     */
    public synchronized int getNumberOfSelect() {
        prepare();
        return mDateBaseHelper.getNumberOfSelect(mChatTableInfoHolder.getSelectNumStr(),
                new String[]{mChatTableInfoHolder.getChatContact()});
    }

    /**
     * @param startLine the first line from where to start query
     * @param numberOfLine the total number of lines to query
     * @return the cursor which is positioned before the first entry
     */
    public synchronized Cursor selectPage(int startLine, int numberOfLine) {
        prepare();
        if (mChatTableInfoHolder.getSelectPageStr() != null) {
            startLine = startLine > 0 ? startLine : 0;
            numberOfLine = numberOfLine > 0 ? numberOfLine : 0;
            return mDateBaseHelper.selectPage(mChatTableInfoHolder.getSelectPageStr(), startLine,
                    numberOfLine);

        } else {
            Log.v(TAG, "selectPage failed!");
            return null;
        }
    }

    public synchronized void setSelectColumns(String[] selectColumns) {
        mChatTableInfoHolder.setSelectColumns(selectColumns);
    }

    public synchronized void setChatContact(String chatContact) {
        mChatTableInfoHolder.setChatContact(chatContact);
    }

    /**
     * ChatTableInfoHolder in charge of the info of chat table in database
     */
    public class ChatTableInfoHolder extends BaseTableInfoHolder {
        private String mTableName;

        private String mChatContact;

        private String mCreateTableStr;

        private String[] mSelectColumns;

        public static final String CHATCONVNAME= "chat_convname";

        public static final String CHATDISPLAYNAME = "chat_displayname";

        public static final String CHATSTRING = "chat_string";

        public static final String CHATTIME = "chat_time";

        public static final String CHATDATE = "chat_date";
        
        public static final String CHATREAD = "chat_read";

        public ChatTableInfoHolder(String usrName) {
            super();
            if (null == usrName)
                return;
            this.mTableName = "chat_" + DigestUtil.MD5(usrName);
            this.mCreateTableStr = "CREATE TABLE IF NOT EXISTS " + mTableName
                    + " (_id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT,"
                    + "chat_convname TEXT  NOT NULL," + "chat_displayname TEXT  NOT NULL,"
                    + "chat_string TEXT  NOT NULL," + "chat_read  TEXT  NOT NULL,"
                    + "chat_time TEXT  NOT NULL,chat_date DATE  NOT NULL)";

            mSelectColumns = null;
        }

        public void setUsrName(String usrName) {
            if (null == usrName)
                return;
            this.mTableName = "chat_" + DigestUtil.MD5(usrName);
        }
        
        @Override
        public String getTableName() {
            return mTableName;
        }

        @Override
        public String getCreateTableStr() {
            return mCreateTableStr;
        }

        @Override
        public void setSelectColumns(String[] selectColumns) {
            this.mSelectColumns = selectColumns;
        }

        @Override
        public String[] getSelectColumns() {
            return mSelectColumns; // null mean *
        }

        @Override
        public String getSelection() {
            return CHATCONVNAME+"=?";
        }

        @Override
        public String[] getSelectionArgs() {
            return new String[] {
                mChatContact
            };
        }

        @Override
        public String getOrderBy() {
            return CHATDATE + "," + CHATTIME + " asc";
        }

        public String getSelectNumStr() {
            return "select * from " + getTableName() + " where "
                    + getSelection();
        }

        // the newer message are at the tail of the selection results
        public String getSelectPageStr() {
            return "select * from " + getTableName() + " where "
                    + CHATCONVNAME + "='" + getChatContact() + "'" +
                    " ORDER BY " + CHATDATE + "," + CHATTIME + " asc" + " limit ?,? ";
        }

        public void setChatContact(String chatContact) {
            this.mChatContact = chatContact;
        }

        public String getChatContact() {
            return mChatContact;
        }
    }
}
