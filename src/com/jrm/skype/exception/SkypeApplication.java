package com.jrm.skype.exception;

import android.app.Application;

public class SkypeApplication  extends Application {
    @Override 
    public void onCreate() { 
        
        super.onCreate(); 
        
        SkypeException customException = SkypeException.getInstance(); 
        
        customException.init(getApplicationContext()); 
    }  
}
