
package com.jrm.skype.util;

import java.util.ArrayList;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.ui.ConferenceActivity;
import com.jrm.skype.ui.PstnCallActivity;
import com.jrm.skype.ui.SkypeCallActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import com.jrm.skype.util.Log;

/**
 * @author andy.liu 
 * A auxiliary class that handle the call and start the Call Activity 
 * and it is a singleton.
 */
public class CallHelper {
    
    /**
     * 
     * @param mActivity
     * @param skypename
     * @param localVideo  start local video
     * @param isPSTN  is calling a pstn number,only in call out.
     */
    public static void callOut(Activity mActivity, String skypename, boolean localVideo, boolean isPSTN) {
        TvSourceHelper.setInputSourceStorage();
        
        Intent mIntent = new Intent();
        if(isPSTN){
            mIntent.setClass(mActivity, PstnCallActivity.class);
        }else{
            mIntent.setClass(mActivity, SkypeCallActivity.class);
        }
        
        mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.CONVTYPE, SKYPECONSTANT.CONVTYPE.CALLOUT);
        mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.CONVNAME, skypename);
        mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.LOCALVIDEO, localVideo);
        mActivity.startActivity(mIntent);
    }

    /**
     * 
     * @param mActivity the context
     * @param convName  the name of conversation
     * @param skypenames the customers in the conversation
     */
    public static void callIn(Activity mActivity, String convName, int callType, String[] skypenames, boolean localVideo) {
        TvSourceHelper.setInputSourceStorage();
        
         ArrayList<String> customers = new ArrayList<String>();
         for (String customer :skypenames )
             customers.add(customer);
         Intent mIntent = new Intent();
         mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.CONVNAME, convName);
         
         if ( SKYPECONSTANT.CALLTYPE.DIALOG == callType ){
             mIntent.setClass(mActivity, SkypeCallActivity.class);
             mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.CONVTYPE, SKYPECONSTANT.CONVTYPE.CALLIN);
             mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.LOCALVIDEO, localVideo);
         }else{
             mIntent.setClass(mActivity, ConferenceActivity.class);
             mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.CONVTYPE, SKYPECONSTANT.CONVTYPE.CONFERENCEIN);
             mIntent.putExtra(SKYPECONSTANT.SKYPESTRING.CUSTOMERS, customers);
         }
        mActivity.startActivity(mIntent);
    }
    
    public static boolean checkCamera(Context context) {
        if (null == context)
            return false;
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            Log.v("checkCamera------>", "" + Camera.getNumberOfCameras());
            Camera ca = null;
            try {
                ca = Camera.open();
                ca.startPreview();
                if (null != ca)
                    ca.release();
                return true;
            } catch (Exception e) {
                if (null != ca)
                    ca.release();
                return false;
            }

        } else {
            // no camera on this device
            return false;
        }

    }

}
