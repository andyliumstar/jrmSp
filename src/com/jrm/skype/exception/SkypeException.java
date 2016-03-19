
package com.jrm.skype.exception;

import java.lang.Thread.UncaughtExceptionHandler;
import android.content.Context;
import com.jrm.skype.util.Log;

public class SkypeException implements UncaughtExceptionHandler {
//   private Context mContext;

    private Thread.UncaughtExceptionHandler defaultExceptionHandler;

    private static SkypeException customException;
    
    private SkypeException() {
    }

    public static SkypeException getInstance() {
        if (customException == null) {
            customException = new SkypeException();
        }
        return customException;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        if (defaultExceptionHandler != null) {
            Log.e("SkypeException", "----exception >>>>>>>");
            exception.printStackTrace();
           // defaultExceptionHandler.uncaughtException(thread, exception);
            ActivityManager.getInstance().finishAll();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public void init(Context context) {
   //     mContext = context;
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
}
