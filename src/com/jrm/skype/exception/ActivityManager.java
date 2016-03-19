package com.jrm.skype.exception;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;

public class ActivityManager {
    private ArrayList<Activity> activityList = new ArrayList<Activity>();
    private static ActivityManager instance;

    private ActivityManager() {
    }

    public static ActivityManager getInstance() {
        if (null == instance) {
            instance = new ActivityManager();
        }
        return instance;
    }

    public synchronized void addActivity(Activity activity) {
        if(null != activity){
            activityList.add(activity);  
        }
    }
    
    public synchronized void pupActivity(Activity activity){
        if(null == activity){
            return;
        }
        for(int i = activityList.size()-1; i>=0; --i){
            if(activityList.get(i).getComponentName().equals(activity.getComponentName())){
                activityList.remove(i);
                break;
            }
        }
    }

    public void finishAll() {
        Activity activity;
        for(Iterator<Activity> iter = activityList.iterator(); iter.hasNext();){
            activity=iter.next();
            activity.finish();
            iter.remove();
        }
    }
}

