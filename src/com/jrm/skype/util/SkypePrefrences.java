
package com.jrm.skype.util;

import com.jrm.skype.constant.SKYPECONSTANT;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SkypePrefrences {
    private static SharedPreferences SharedPrefrences;

    private static Editor Editor;
    
    private static Context mCtx;
    
    public static void initSkypePrefrences(Context ctx){
        mCtx = ctx;
    }

    //whether start the skype first time
    public static boolean isFirstStart(Context ctx) {
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        return SharedPrefrences.getBoolean(SKYPECONSTANT.SKYPESTRING.FIRSTSTART, true);
    }
    
    public static void setFirstStart(Context ctx) {
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        Editor = SharedPrefrences.edit();
        Editor.putBoolean(SKYPECONSTANT.SKYPESTRING.FIRSTSTART, false);
        Editor.commit();
    }
    
    //whether restart the kit
    public static boolean isReStartKit(Context ctx) {
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        return SharedPrefrences.getBoolean(SKYPECONSTANT.SKYPESTRING.RESTARTKIT, false);
    }
    
    public static void setReStartKit(Context ctx,boolean value) {
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        Editor = SharedPrefrences.edit();
        Editor.putBoolean(SKYPECONSTANT.SKYPESTRING.RESTARTKIT, value);
        Editor.commit();
    }
    
    //whether the kit is connected
    public static boolean isKitConnect(Context ctx) {
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        return SharedPrefrences.getBoolean(SKYPECONSTANT.SKYPESTRING.KITCONNECT, true);
    }
    
    public static void setKitConnect(Context ctx,boolean value) {
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        Editor = SharedPrefrences.edit();
        Editor.putBoolean(SKYPECONSTANT.SKYPESTRING.KITCONNECT, value);
        Editor.commit();
    }
    
    //whether is in conv
    public static boolean isInConv(Context ctx) {
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        return SharedPrefrences.getBoolean(SKYPECONSTANT.SKYPESTRING.ISINCONV, false);
    }
    
    public static void setInConv(Context ctx,boolean value) {
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        Editor = SharedPrefrences.edit();
        Editor.putBoolean(SKYPECONSTANT.SKYPESTRING.ISINCONV, value);
        Editor.commit();
    }
    
    //whether has sign in
    public static boolean hasSignin(Context ctx) {
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        return SharedPrefrences.getBoolean(SKYPECONSTANT.SKYPESTRING.HASSIGNIN, false);
    }

    public static void setSignin(Context ctx,boolean value) {
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        Editor = SharedPrefrences.edit();
        Editor.putBoolean(SKYPECONSTANT.SKYPESTRING.HASSIGNIN, value);
        Editor.commit();
    }
    
    //whether have to request the contact list
    public static void setRequestContactListBool(Context ctx,boolean value){
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        Editor = SharedPrefrences.edit();
        Editor.putBoolean(SKYPECONSTANT.SKYPESTRING.REQUESTCONTACTLIST, value);
        Editor.commit();
    }
    
    public static boolean getRequestContactListBool(Context ctx){
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        return SharedPrefrences.getBoolean(SKYPECONSTANT.SKYPESTRING.REQUESTCONTACTLIST, false);
    }
    //auto start skype
    public static boolean isStartSKTWhenTVStart(Context ctx) {
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        return SharedPrefrences.getBoolean(SKYPECONSTANT.SKYPESTRING.STARTSKTTVSTART, false);
    }

    public static void setStartSKTWhenTVStart(Context ctx, boolean value) {
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        Editor = SharedPrefrences.edit();
        Editor.putBoolean(SKYPECONSTANT.SKYPESTRING.STARTSKTTVSTART, value);
        Editor.commit();
    }
    
    //auto sign in
    public static boolean isSignWhenSKTStart(Context ctx) {
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        return SharedPrefrences.getBoolean(SKYPECONSTANT.SKYPESTRING.SIGNINSKTSTART, false);
    }

    public static void setSignWhenSKTStart(Context ctx, boolean value) {
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        Editor = SharedPrefrences.edit();
        Editor.putBoolean(SKYPECONSTANT.SKYPESTRING.SIGNINSKTSTART, value);
        Editor.commit();
    }
    
    //whether sign out because of creating account
    public static boolean isCreateOut(Context ctx){
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        return SharedPrefrences.getBoolean(SKYPECONSTANT.SKYPESTRING.CREATEACCOUNT, false);
    }
    
    public static void setCreateOut(Context ctx, boolean value){
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        Editor = SharedPrefrences.edit();
        Editor.putBoolean(SKYPECONSTANT.SKYPESTRING.CREATEACCOUNT, value);
        Editor.commit();
    }
    
    public static void recordCreateInfo(Context ctx,String skypeNameStr, String passwordStr, String emailStr, String fullNameStr, boolean sendSktNews){
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        Editor = SharedPrefrences.edit();
        
        Editor.putString(SKYPECONSTANT.SKYPESTRING.CREATESKYPENAME, skypeNameStr);
        Editor.putString(SKYPECONSTANT.SKYPESTRING.CREATEPASSWORD, passwordStr);
        Editor.putString(SKYPECONSTANT.SKYPESTRING.CREATEEMAIL, emailStr);
        Editor.putString(SKYPECONSTANT.SKYPESTRING.CREATEFULLNAME, fullNameStr);
        Editor.putBoolean(SKYPECONSTANT.SKYPESTRING.SENDSKNEWS, sendSktNews);
        Editor.commit();
    }
    
    public static String getCreateSkypeName(Context ctx){
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        return SharedPrefrences.getString(SKYPECONSTANT.SKYPESTRING.CREATESKYPENAME, "");
    }
    
    public static String getCreatePassword(Context ctx){
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        return SharedPrefrences.getString(SKYPECONSTANT.SKYPESTRING.CREATEPASSWORD, "");
    }
    
    public static String getCreateEmail(Context ctx){
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        return SharedPrefrences.getString(SKYPECONSTANT.SKYPESTRING.CREATEEMAIL, "");
    }
    
    public static String getCreateFullName(Context ctx){
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        return SharedPrefrences.getString(SKYPECONSTANT.SKYPESTRING.CREATEFULLNAME, "");
    }
    
    public static boolean getCreateSendSKNEWS(Context ctx){
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        return SharedPrefrences.getBoolean(SKYPECONSTANT.SKYPESTRING.SENDSKNEWS, false);
    }
    
   
    
    public static void setRingVolume(Context ctx,int value){
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        Editor = SharedPrefrences.edit();
        Editor.putInt(SKYPECONSTANT.SKYPESTRING.RINGINGVOLUME, value);
        Editor.commit();
    }
    
    public static int getRingVolume(Context ctx){
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        return SharedPrefrences.getInt(SKYPECONSTANT.SKYPESTRING.RINGINGVOLUME, 80);
    }
    
    public static void recordSignInfo(Context ctx,String skypeNameStr, String passwordStr){
        if(null == skypeNameStr || null == passwordStr){
            return;
        }
        
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        Editor = SharedPrefrences.edit();
        
        Editor.putString(SKYPECONSTANT.SKYPESTRING.SKYPENAME, DigestUtil.strToCip(skypeNameStr));
        Editor.putString(SKYPECONSTANT.SKYPESTRING.PASSWORD, DigestUtil.strToCip(passwordStr));
        Editor.commit();
    }
    
    public static void changePwd(Context ctx, String passwordStr){
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        Editor = SharedPrefrences.edit();
        
        Editor.putString(SKYPECONSTANT.SKYPESTRING.PASSWORD, passwordStr);
        Editor.commit();
    }
    
    public static String getSignSkypeName(Context ctx){
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        return DigestUtil.cipToStr(SharedPrefrences.getString(SKYPECONSTANT.SKYPESTRING.SKYPENAME, ""));
    }
    
    public static String getSignPassword(Context ctx){
        if(null == ctx){
            ctx = mCtx;
        }
        
        SharedPrefrences = ctx.getSharedPreferences(SKYPECONSTANT.SKYPESTRING.SKYPEPREFRENCES,
                Context.MODE_WORLD_READABLE);
        return DigestUtil.cipToStr(SharedPrefrences.getString(SKYPECONSTANT.SKYPESTRING.PASSWORD, ""));
    }
}
