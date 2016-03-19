package com.jrm.skype.util;
import java.security.MessageDigest;

public class DigestUtil {
    public final static String MD5(String s) {
        if (null == s || s.equals("")) {
            return "";
        }
         
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       

        
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public final static String strToCip(String s) {
        if (null == s || s.equals("")) {
            return "";
        }
        
        int l = s.length();
        int n = 0;
        char str[]= new char[l];
        
        for(int i = 0; i < l; ++i){
            n = s.charAt(i)-i;
            if(n<0){
               n+=127; 
            }
            str[i] = (char) (n);
        }
        
        return new String(str);
    }

    public final static String cipToStr(String cip) {
        if (null == cip || cip.equals("")) {
            return "";
        }
        
        int l = cip.length();
        int n = 0;
        char str[]= new char[l];
        
        for(int i = 0; i < l; ++i){
            n = cip.charAt(i)+i;
            if(n>127){
               n-=127; 
            }
            str[i] = (char) (n);
        }
        
        return new String(str);
    }
}
