package com.jrm.skype.util;
 
public class Log {
    private static boolean enableLog = true;
    
    public static void v(String tag, String msg){
        if(enableLog){
            android.util.Log.v(tag,msg);
        }
    }
    
    public static void e(String tag, String msg){
        if(enableLog){
            android.util.Log.e(tag,msg);
        }
    }
    
    public static void w(String tag, String msg){
        if(enableLog){
            android.util.Log.w(tag,msg);
        }
    }
    
    public static void d(String tag, String msg){
        if(enableLog){
            android.util.Log.d(tag,msg);
        }
    }

}
