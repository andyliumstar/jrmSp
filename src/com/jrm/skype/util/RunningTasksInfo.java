package com.jrm.skype.util;

import java.util.List;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class RunningTasksInfo {
    
    public static boolean IsActivityTopOfRunningTask(Activity act){
        if (null == act)
            return false;
        
        ActivityManager aManager = (ActivityManager)act.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = aManager.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;

        if (act.getComponentName().equals(componentInfo))
            return true;
        return false;
    }
    
    private static boolean IsActivityTopOfRunningTask(Context ctx ,String packageName){
        ActivityManager aManager = (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = aManager.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;

        if (packageName.equals(componentInfo.getPackageName()))
            return true;
        return false;
    }
    
    public static boolean IsVidepApkRunning(Context ctx){
        if (null == ctx){
            return false;
        }
     
       //localMM is special one ,
        if (IsActivityTopOfRunningTask(ctx,"com.jrm.localmm")){
            return true;
        }
        
        Intent intent = new Intent(Intent.ACTION_VIEW,null);
        intent.setType("video/*");    
        PackageManager pManager = ctx.getPackageManager();  
        List<ResolveInfo> mApps = pManager.queryIntentActivities(intent,PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);  
        
        if (null != mApps){
            for(int index = 0; index<mApps.size(); ++index){
                if (IsActivityTopOfRunningTask(ctx,mApps.get(index).activityInfo.packageName)){
                    return true;
                }
            }
        }
        
        return false;
    }

}
