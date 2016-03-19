
package com.jrm.skype.datebase;

import android.content.ContentValues;

/**
 * @author andy.liu 
 * the BaseTableResourceHolder
 */
public abstract class BaseTableResourceHolder {
    /**
     * @return the ContentValues that to insert into the database
     */
    public abstract ContentValues createContentValues();
}
