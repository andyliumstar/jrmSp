package com.jrm.skype.util;

public class DefaultPort {
    private static int defaultPort = 1663;
    
    public static int getDefaultPort(){
        if(!SkypePrefrences.isKitConnect(null) && 1663 == defaultPort){
            defaultPort = (int) (1663 + Math.random()*1000);
        }
        return defaultPort;
    }
}
