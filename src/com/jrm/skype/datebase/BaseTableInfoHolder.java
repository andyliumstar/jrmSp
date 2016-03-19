
package com.jrm.skype.datebase;

/**
 * @author andy.liu 
 * the base class of TableInfoHolder which will use for the
 *         database
 */
public abstract class BaseTableInfoHolder {
    /**
     * @return the name of the table
     */
    public abstract String getTableName();

    /**
     * @return the str to create the table
     */
    public abstract String getCreateTableStr();

    /**
     * @return the String[] for query Columns
     */
    public abstract String[] getSelectColumns();

    /**
     * @param selectColumns the String[] for query Columns result
     */
    public abstract void setSelectColumns(String[] selectColumns);

    /**
     * @return the Column for query condition
     */
    public abstract String getSelection();

    /**
     * @return the value of Selection
     */
    public abstract String[] getSelectionArgs();

    /**
     * @return the OrderBy str for query
     */
    public abstract String getOrderBy();
}
