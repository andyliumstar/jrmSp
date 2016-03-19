
package com.jrm.skype.datebase;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.datebase.ConvTableActor.ConvTableInfoHolder;
import com.jrm.skype.util.SktApiActor;

import android.content.ContentValues;

/**
 * @author andy.liu 
 * The class  contains all of the data of a column of a call table
 */
public class ConvTableResourceHolder extends BaseTableResourceHolder {
    private String mConvName;
    
    private String mConvType;

    private String mConvDuration;

    private String mConvTime;

    private String mConvDate;
   
    private String mVMId;
    
    private String mSaveDays;

    public void setVMId(String mVMId) {
        this.mVMId = mVMId;
    }

    public void setConvName(String mConvName) {
        this.mConvName = mConvName;
    }

    public void setConvType(String mConvType) {
        this.mConvType = mConvType;
    }

    public void setConvDuration(String mConvDuration) {
        this.mConvDuration = mConvDuration;
    }

    public void setConvTime(String mConvTime) {
        this.mConvTime = mConvTime;
    }

    public void setConvDate(String mConvDate) {
        this.mConvDate = mConvDate;
    }

    @Override
    public ContentValues createContentValues() {
        switch(SktApiActor.getHistoryDays()){
            case NO_HISTORY://just for call
                if (mConvType.equals(SKYPECONSTANT.CONVTYPE.NEWMESSAGE + "")
                        || mConvType.equals(SKYPECONSTANT.CONVTYPE.OLDMESSAGE + "")
                        || mConvType.equals(SKYPECONSTANT.CONVTYPE.VOICEMAIL + "")
                        || mConvType.equals(SKYPECONSTANT.CONVTYPE.VOICEMAILNEW + "")){
                    this.mSaveDays = SKYPECONSTANT.HISTORYSAVEDAYS.FOREVER+"";  
                    break;
                }else{
                    return null;  
                }
            case TWO_WEEKS:
                this.mSaveDays = SKYPECONSTANT.HISTORYSAVEDAYS.TWO_WEEKS+"";
                break;
            case ONE_MONTH:
                this.mSaveDays = SKYPECONSTANT.HISTORYSAVEDAYS.ONE_MONTH+"";
                break;
            case THREE_MONTHS:
                this.mSaveDays = SKYPECONSTANT.HISTORYSAVEDAYS.THREE_MONTHS+"";
                break;
            case FOREVER:
                this.mSaveDays = SKYPECONSTANT.HISTORYSAVEDAYS.FOREVER+"";
                break;
            default:
                break;
        }
        ContentValues values = new ContentValues();
        values.put(ConvTableInfoHolder.CONVNAME, mConvName != null ? mConvName : "null");
        values.put(ConvTableInfoHolder.CONVTYPE, mConvType != null ? mConvType : "null");
        values.put(ConvTableInfoHolder.CONVDURATION, mConvDuration != null ? mConvDuration : "null");
        values.put(ConvTableInfoHolder.CONVTIME, mConvTime != null ? mConvTime : "null");
        values.put(ConvTableInfoHolder.CONVDATE, mConvDate != null ? mConvDate : "null");
        values.put(ConvTableInfoHolder.VOICEMAILID, mVMId != null ? mVMId : "null");
        values.put(ConvTableInfoHolder.SAVEDAYS, mSaveDays != null ? mSaveDays : "0");
        return values;
    }

}
