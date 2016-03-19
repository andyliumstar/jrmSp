
package com.jrm.skype.datebase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import com.jrm.skype.util.Log;

/**
 * @author andy.liu 
 * A class to handle the database and it is a Singleton
 */
public class DateBaseHelper extends SQLiteOpenHelper {
    private static DateBaseHelper myDateBaseHelper;

    private BaseTableInfoHolder mBaseTableInfoHolder; // different tables share
                                                      // the same DateBaseHelper
                                                      // ,must have its own
                                                      // TableInfoHolder

    public final static String DATEBASENAME = "SKYPE_DATEBASE";

    private DateBaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * @param context
     * @param name the name of database
     * @return
     */
    public static synchronized DateBaseHelper getInstance(Context context, String name) {
        if (myDateBaseHelper == null)
            myDateBaseHelper = new DateBaseHelper(context, name, null, 1);

        return myDateBaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * let the DateBaseHelper associate with a table,must execute before use the
     * db function like insert or select
     * 
     * @param baseTableInfoHolder the info of table
     * @return null
     */
    public synchronized void setTableInfoHolderAndCreate(BaseTableInfoHolder baseTableInfoHolder) {
        if (null != baseTableInfoHolder && this.mBaseTableInfoHolder != baseTableInfoHolder) {
            this.mBaseTableInfoHolder = baseTableInfoHolder;
            try {
                // "CREATE TABLE IF NOT EXISTS "+"..............."
                getWritableDatabase().execSQL(mBaseTableInfoHolder.getCreateTableStr());
            } catch (SQLException e) {
                Log.e("SKYPE_CREATE_TABLE", "fail to create table.");
            }
        }

    }

    /**
     * insert one row into the database
     * 
     * @param contentValues the data to insert
     */
    public synchronized void insert(ContentValues contentValues) {
        SQLiteDatabase db = getWritableDatabase();
        
        if (null != mBaseTableInfoHolder && null != contentValues) {
            try {

                db.insert(mBaseTableInfoHolder.getTableName(), null, contentValues);

            } catch (Exception e) {
                Log.e("SKYPE_INSERT_TABLE", "fail to intert table.");
            }
        }
    }

    /**
     * @param column the column of the table
     * @param whereValue the value of column
     * @return
     */
    public synchronized void delete(String column, String[] whereValue) {
        SQLiteDatabase db = getWritableDatabase();
        
        if (null != mBaseTableInfoHolder) {
            try {

                db.delete(mBaseTableInfoHolder.getTableName(), column, whereValue);

            } catch (Exception e) {
                Log.e("SKYPE_DELETE_TABLE", "fail to delete item!");
            }
        }
    }
    
    /**
     * 
     * @param contentValues the data to update
     * @param column column the column of the table
     * @param whereValue whereValue the value of column
     */
    public synchronized void update(ContentValues contentValues,String column, String[] whereValue) {
        SQLiteDatabase db = getWritableDatabase();

        if (null != mBaseTableInfoHolder && column != null && whereValue != null && contentValues != null) {
            try {

                db.update(mBaseTableInfoHolder.getTableName(), contentValues, column, whereValue);

            } catch (Exception e) {
                Log.e("SKYPE_UPDATE_TABLE", "fail to update!");
            }
        }
    }

    /**
     * the select function using query() to complete; and most of the var that
     * the query() need is offered by TableInfoHolder
     */
    public synchronized Cursor selectAll() {
        SQLiteDatabase db = getReadableDatabase();
        if (null != mBaseTableInfoHolder) {
            // return db.query(
            // mBaseTableInfoHolder.getTableName(),
            // new
            // String[]{"_id","chat_name","chat_content","chat_time","chat_date"},
            // "chat_contact=?",
            // new String[]{"MJ"}, null, null, "chat_date,chat_time asc");
            try {
                return db.query(
                        mBaseTableInfoHolder.getTableName(),
                        mBaseTableInfoHolder.getSelectColumns(),
                        mBaseTableInfoHolder.getSelection(), mBaseTableInfoHolder.getSelectionArgs(),
                        null, null, mBaseTableInfoHolder.getOrderBy());
            } catch (Exception e) {
                Log.e("SKYPE_SELECT_TABLE", "selectAll failed!");
                return null;
            }
        }
        return null;
    }
    
    public synchronized Cursor select(String selection,String [] selectionArgs) {
        SQLiteDatabase db = getReadableDatabase();
        if (null != mBaseTableInfoHolder) {
            try {
                return db.query(
                        mBaseTableInfoHolder.getTableName(),null,
                        selection, selectionArgs,
                        null, null, null);
            } catch (Exception e) {
                Log.e("SKYPE_SELECT_TABLE", "selectAll failed!");
                return null;
            }
        }
        return null;
    }

    /**
     * for page select
     * 
     * @param sql the select sql
     * @param startLine the first line from where to start query
     * @param numberOfLine the total number of lines to query
     * @return the cursor which is positioned before the first entry
     */
    public synchronized Cursor selectPage(String sql, int startLine, int numberOfLine) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            return db.rawQuery(sql, new String[] {
                    String.valueOf(startLine), String.valueOf(numberOfLine)
            });
        } catch (Exception e) {
            Log.e("SKYPE_SELECT_TABLE", "selectPage failed!");
            return null;
        }
    }

    /**
     * get the total number of select
     * 
     * @param sql the select sql
     * @return the total number of select
     */
    public synchronized int getNumberOfSelect(String sql,String[] args) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            return db.rawQuery(sql, args).getCount();
        } catch (Exception e) {
            Log.e("SKYPE_SELECT_TABLE", "getNumberOfSelect failed!");
            return 0;
        }
    }

}
