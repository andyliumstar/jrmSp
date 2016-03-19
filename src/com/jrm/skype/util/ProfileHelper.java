package com.jrm.skype.util;

import android.content.Context;
import com.jrm.skype.api.SktContact;
import com.jrm.skype.ui.R;

public class ProfileHelper {
    private SktContact.SktBuddy mBuddy;
    
    private Context mContext;

    public ProfileHelper(String skypeName, Context context) {
        mBuddy = SktApiActor.getBuddy(skypeName);
        mContext = context;
    }
    /**
     * 
     * @return the birthday format like xxxx/xx/xx or ""
     */
    public String getBirthDay(){
        if (null != mBuddy){
            int birthInt = mBuddy.getBirthDate();
            if (birthInt>0){
                String birthStr = birthInt+"";
                if(8 == birthStr.length())
                    return birthStr.substring(0, 4)+"/"+birthStr.substring(4, 6)+"/"+birthStr.substring(6, 8);
            }
        }
        return "";
    }
    /**
     * 
     * @return male female or nuknown
     */
    public String getGender(){
        if (null != mBuddy && null != mContext){
            switch (mBuddy.getGender()){
                case 1:
                    return mContext.getString(R.string.male);
                case 2:
                    return mContext.getString(R.string.female);
                default :
                    return mContext.getString(R.string.unknown); 
            }
        }
        return "";
    }
    
    public String getMobile(){
        if (null != mBuddy)
            return mBuddy.getPhoneMobile();
        return "";
    }
    
    public String getEmail(){
        if (null != mBuddy)
            return mBuddy.getEmail();
        return "";
    }
    
   
    public String getLanguage() {
        if (null != mBuddy && null != mContext)  {
            return CountryInfoHelper.getLanguageNameStrByShortName(mContext, mBuddy.getLanguage());
		} 
		return "";
    }

    /**
     * 
     * @return city+province+country
     */
    public String getLocation() {
        if (null != mBuddy && null != mContext) {
            String city = mBuddy.getCity();
            String province = mBuddy.getProvince();
            String country = mBuddy.getCountry();

            String location = city;
            if (!location.isEmpty()) {
                if (!province.isEmpty()) {
                    location = location + ", " + province;
                }
            } else {
                location = province;
            }

            if (!location.isEmpty()) {
                if (!country.isEmpty()) {
                    location = location + ", "
                            + CountryInfoHelper.getCountryNameStrByShortName(mContext, country);
                }
            } else {
                location = CountryInfoHelper.getCountryNameStrByShortName(mContext, country);
            }
            return location;
        }
        return "";
    }
}
