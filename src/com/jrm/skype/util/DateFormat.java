package com.jrm.skype.util;

import java.text.ParseException;

public class DateFormat {
    private  java.text.SimpleDateFormat mFormat; 

    private  java.util.Date mDate ;
    
    public DateFormat(){
        mFormat = new java.text.SimpleDateFormat();
        mDate = new java.util.Date();
    }

    /**
     * 
     * @param time the timestamp
     * @param pattern "yyyy-MM-dd" or "HH:mm:ss"
     * @return
     */
    
    public String getFormatString(long time,String pattern){
        if (null == pattern )
            return "";
        mDate.setTime(time);
        try{
            mFormat.applyPattern(pattern); 
        }catch (Exception e) {
            return "";
        }
        
        return mFormat.format(mDate.getTime());
    }
    /**
     * 
     * @param duration
     * @param pattern
     * @return
     */
    public String getFormatString(int duration ,String pattern){
        if (null == pattern )
            return "";
        mDate.setTime(duration*1000);
        mDate.setHours(0);
        try{
            mFormat.applyPattern(pattern);
        }catch (Exception e) {
            return "";
        }
        
        return mFormat.format(mDate.getTime());
    }
    
    public  boolean parseStr(String str,boolean lenient,String pattern){
        if (null == pattern )
            return false;
        try {
            mFormat.applyPattern(pattern);
            mFormat.setLenient(lenient);
            mFormat.parse(str);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getSeparateDaysFromNow(String oldDate){
        if (null == oldDate)
            return 0;

        try {
            mFormat.applyPattern("yyyy-MM-dd");
            mFormat.setLenient(true);
            return (int) (System.currentTimeMillis() / (24 * 60 * 60 * 1000) - mFormat.parse(
                    oldDate).getTime()/ (24 * 60 * 60 * 1000));
        } catch (ParseException e) {
            return 0;
        }
    }

}
