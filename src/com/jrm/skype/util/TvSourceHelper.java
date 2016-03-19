package com.jrm.skype.util;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;


public class TvSourceHelper {
    private static EnumInputSource currentSource;
    
    public static void setInputSourceStorage(){
        TvCommonManager commonService = TvCommonManager.getInstance();
        currentSource = commonService.getCurrentInputSource();
        
        if (currentSource != EnumInputSource.E_INPUT_SOURCE_STORAGE){
            commonService.setInputSource(EnumInputSource.E_INPUT_SOURCE_STORAGE);
        }
    }

    public static void resetInputSource(){
        if (currentSource == null)
            return;
        
        TvCommonManager commonService = TvCommonManager.getInstance();
        commonService.setInputSource(currentSource);
    }
    
    public static boolean isSourceStorage() {
        TvCommonManager commonService = TvCommonManager.getInstance();
        return (commonService.getCurrentInputSource() == EnumInputSource.E_INPUT_SOURCE_STORAGE);
    }
}
