
package com.jrm.skype.datebase;

import java.util.ArrayList;
import com.jrm.skype.util.DateFormat;
import com.jrm.skype.util.DigestUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.jrm.skype.util.Log;

/**
 * @author andy.liu 
 * The class is in change of the act for ConvTable:
 *         delete,select or insert
 */
public class ConvTableActor {
    private final static String TAG = "ConvTableActor";
    
    private DateBaseHelper mDateBaseHelper;

    private ConvTableInfoHolder mConvTableInfoHolder;

    private static ConvTableActor Conv_INSTANCE = null;
    
    public synchronized static void initConvTableActor(String usrName, Context context){
        if ( null == Conv_INSTANCE) {
            Conv_INSTANCE= new ConvTableActor(usrName, context);
		}
        if ( null != Conv_INSTANCE) {
       		Conv_INSTANCE.mConvTableInfoHolder.setUsrName(usrName);
		}
        
    }
    
    public static ConvTableActor getInstance(){
        if ( null == Conv_INSTANCE){
            Log.e(TAG, "you should init ConvTableActor first");
        }
        return Conv_INSTANCE;
    }
    
    public static void releaseConvTableActor(){
        Conv_INSTANCE = null;
    }
    /**
     * @param usrName the skype name of the user
     * @param context
     */
    private ConvTableActor(String usrName, Context context) {
        mDateBaseHelper = DateBaseHelper.getInstance(context, DateBaseHelper.DATEBASENAME);
        mConvTableInfoHolder = new ConvTableInfoHolder(usrName);
        updateData();
    }

    /**
     * set the TableInfo for DateBaseHelper
     */
    public void prepare() {
        mDateBaseHelper.setTableInfoHolderAndCreate(mConvTableInfoHolder);
    }
    /**
     * clear the old history which is out of date
     */
    private void updateData(){
        ArrayList<Integer> indexList = new ArrayList<Integer>();
        int savedays;
        int separateDays;
        DateFormat df = new DateFormat();
        
        Cursor cursor = selectAll();
        
        if (null != cursor){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                savedays = cursor.getInt(cursor.getColumnIndex(ConvTableInfoHolder.SAVEDAYS));
                if (-1 == savedays)//forever{
                {
                    cursor.moveToNext(); 
                    continue;
                }
                separateDays = df.getSeparateDaysFromNow(cursor.getString(cursor.getColumnIndex(ConvTableInfoHolder.CONVDATE)));
                if (savedays < separateDays){
                    indexList.add(cursor.getInt(cursor.getColumnIndex("_id")));
                }
                cursor.moveToNext();
            }
            
            for(int index = 0; index<indexList.size(); ++index){
                delete("_id=?",new String[] {indexList.get(index)+""});
            }
        }
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

    /**
     * @param mConvTableResourceHolder the holder of the data to insert
     */
    public  void insert(ConvTableResourceHolder mConvTableResourceHolder) {
        prepare();
        mDateBaseHelper.insert(mConvTableResourceHolder.createContentValues());
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
     * @author andy.liu A class the content the info of Conv table in the
     *         database
     */
    public class ConvTableInfoHolder extends BaseTableInfoHolder {
        private String mTableName;

        private String mCreateTableStr;

        private String[] mSelectColumns;
        
        public static final String CONVNAME = "conv_convname";
        
        public static final String CONVDURATION = "conv_duration";//time for Conv and number for message

        public static final String CONVTYPE = "conv_type";

        public static final String CONVTIME = "conv_time";

        public static final String CONVDATE = "conv_date";
        
        public static final String VOICEMAILID = "voicemail_id";
        
        public static final String SAVEDAYS = "savedays";

        public ConvTableInfoHolder(String usrName) {
            super();
            if (null == usrName)
                return;
            this.mTableName = "conv_" + DigestUtil.MD5(usrName);
            this.mCreateTableStr = "CREATE TABLE IF NOT EXISTS " + mTableName
                    + " (_id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT,"
                    + "conv_convname TEXT  NOT NULL," 
                    + "voicemail_id TEXT,"
                    + "conv_duration TEXT  NOT NULL," 
                    + "conv_type NUMBER  NOT NULL,"
                    + "conv_time TEXT  NOT NULL,"
                    + "conv_date DATE  NOT NULL," 
                    + "savedays NUMBER  NOT NULL)";
            mSelectColumns = null;
        }
        
        public void setUsrName(String usrName){
            if (null == usrName)
                return;
            this.mTableName = "conv_" + DigestUtil.MD5(usrName);
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
            return mSelectColumns;
        }

        @Override
        public String getSelection() {
            return null;
        }

        @Override
        public String[] getSelectionArgs() {
            return null;
        }

        @Override
        public String getOrderBy() {
            return "conv_date desc,conv_time desc";
        }
    }

}
