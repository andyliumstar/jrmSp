package com.jrm.skype.service;

import com.jrm.skype.binder.SkypeExceptionBinder;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.jrm.skype.util.Log;

public class SkypeExceptionService extends Service{
    
    private final SkypeExceptionBinder.Stub binder = new SkypeExceptionBinder.Stub() {

        @Override
        public boolean reStartSkypeKit(final int pid) throws RemoteException {

            new Thread() {

                @Override
                public void run() {
                    android.os.Process.killProcess(pid);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startService(new Intent("skype_service"));
                    Log.d("SkypeExceptionService", "startService------skype_service");
                }

            }.start();

            return false;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        Log.d("SkypeExceptionService", "onDestroy------>");
        super.onDestroy();
        System.exit(0);
    }

}
