
package com.jrm.skype.util;

import com.jrm.appstore.agent.AppStoreAgent;
import com.jrm.appstore.domain.Component;
import com.jrm.authorize.agent.AuthorizeAgent;
import com.jrm.core.container.cmps.upgrade.IDownloadProgressListener;
import com.jrm.core.container.cmps.upgrade.IUpgradeService;
import com.jrm.core.container.cmps.upgrade.IUpgradeStateListener;
import com.jrm.core.container.cmps.upgrade.IUpgradeTask;
import com.jrm.service.IJRMConnectListener;
import com.jrm.service.JRMServiceAgent;
import com.jrm.service.JRMServiceManager2;
import com.jrm.skype.service.SkypeService;
import android.app.Service;
import android.os.RemoteException;
import com.jrm.skype.util.Log;

public class UpdateManager {
    private final String TAG = "UpdateManager";

    private Service mServiceContext;
    
    private IUpgradeTask mUpgradeTask;
    
    public UpdateManager(Service context){
        mServiceContext = context;
    }
    
    public void checkNewApkAndDownload(){
        try {
            if (null == mServiceContext)
                return ;

            JRMServiceManager2.connectToJRM(mServiceContext, iJRMConnectListener, null);
        } catch (Exception e) {
            Log.v(TAG, "JRMServiceManager2.connectToJRM--Exception");
            e.printStackTrace();
        } catch (Throwable e) {
            Log.v(TAG, "JRMServiceManager2.connectToJRM--Throwable");
            e.printStackTrace();
        }
       
    }
    
    IJRMConnectListener iJRMConnectListener = new IJRMConnectListener() {

        @Override
        public void onConnectedSuccess(final JRMServiceAgent serviceAgent) {
            if (null == serviceAgent) {
                Log.v(TAG, "null == serviceAgent");
                return;
            }
            
            new Thread() {

                @Override
                public void run() {
                    AuthorizeAgent authorizeManager = serviceAgent.getAuthorizeManager();
                    Component appByPackage = null;

                    if (null == authorizeManager){
                        Log.v(TAG, "null == authorizeManager");
                        return;
                    }
                        
                    
                    try {
                        AppStoreAgent appStoreService = serviceAgent.getAppStoreService();
                        if (null == appStoreService){
                            Log.v(TAG, "null == appStoreService");
                            return;
                        }
                        
                        appByPackage = appStoreService.getAppByPackage(
                                authorizeManager.getBBNumer(), authorizeManager.getSpecialNumber(),
                                mServiceContext.getPackageName());
                        
                        if (null == appByPackage) {
                            Log.v(TAG, "null == appByPackage");
                            return;
                        }
                        
                        Log.v(TAG,
                                "getVersionCode----->"
                                        + appByPackage.getVersionCode()
                                        + "+"
                                        + mServiceContext.getPackageManager().getPackageInfo(
                                                mServiceContext.getPackageName(), 0).versionCode);

                        if (appByPackage.getVersionCode() <= mServiceContext.getPackageManager()
                                .getPackageInfo(mServiceContext.getPackageName(), 0).versionCode) {
                            return;
                        }

                        IUpgradeService upgradeService = serviceAgent.getUpgradeService(mServiceContext
                                .getPackageName());
                        
                         if (null == upgradeService)
                            return;

                        long taskid = upgradeService.createUpgradeTask(
                                appByPackage.getCompPackage(), appByPackage.getVersionName(),
                                appByPackage.getCompDownloadURL(), 1, appByPackage.getCompName());

                        mUpgradeTask = upgradeService.startUpgradeTask(taskid, true);
                        mUpgradeTask.registUpgradeStateListener(iUpgradeStateListener);
                        mUpgradeTask.setDownloadSizeChangeListener(iDownloadProgressListener, 0);

                    } catch (Exception e) {
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                    super.run();
                }

            }.start();

        }

        @Override
        public void onConnectedFailed(int arg0) {
        }
    };

    IDownloadProgressListener iDownloadProgressListener = new IDownloadProgressListener.Stub() {

        @Override
        public void onDownloadSizeChange(int arg0, int arg1) throws RemoteException {

        }
    };

    IUpgradeStateListener iUpgradeStateListener = new IUpgradeStateListener.Stub() {

        @Override
        public void onResume(int arg0, String arg1) throws RemoteException {
            Log.v(TAG, "onResume-------" + arg1);
        }

        @Override
        public void onPreparedSuccess(int arg0, String arg1) throws RemoteException {
            Log.v(TAG, "onPreparedSuccess-------" + arg1);
        }

        @Override
        public void onPreparedFailed(int arg0, String arg1) throws RemoteException {
            Log.v(TAG, "onPreparedFailed-------" + arg1);
        }

        @Override
        public void onPause(int arg0, String arg1) throws RemoteException {
            Log.v(TAG, "onPause-------" + arg1);
        }

        @Override
        public void onInstalledSuccess(int arg0, String arg1) throws RemoteException {
            Log.v(TAG, "onInstalledSuccess-------" + arg1);
        }

        @Override
        public void onInstalledFailed(int arg0, String arg1) throws RemoteException {
            Log.v(TAG, "onInstalledFailed-------" + arg1);

        }

        @Override
        public void onDownloadedSuccess(int arg0, String arg1) throws RemoteException {
            Log.v(TAG, "onDownloadedSuccess");
            
            if(null == mServiceContext)
                return;
            
            ((SkypeService) mServiceContext).downloadedSuccess(mUpgradeTask.getModel().getLocalUrl());
 
        }

        @Override
        public void onDownloadFailed(int arg0, String arg1) throws RemoteException {
            Log.v(TAG, "onDownloadFailed");

        }

        @Override
        public void onCleanSuccess(int arg0, String arg1) throws RemoteException {
            Log.v(TAG, "onCleanSuccess");
        }

        @Override
        public void onCleanFailed(int arg0, String arg1) throws RemoteException {
            Log.v(TAG, "onCleanFailed");

        }
    };

}
