package com.jrm.skype.util;

import android.content.Context;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

public class LayoutParamsHelper{
    private RelativeLayout.LayoutParams mFSRemoteVideoLp;//full screen LayoutParams for remote video

    private RelativeLayout.LayoutParams mNSRemoteVideoLp;

    private RelativeLayout.LayoutParams mFSLocalVideoLp;

    private RelativeLayout.LayoutParams mNSLocalVideoLp;
    
    private RelativeLayout.LayoutParams mMinistLp;
    
    private static final double FULL_SCREEN_LOCAL_VIDEO_LENGTH_OF_SCREEN = 0.2;//1920*1080--->384*216
    
    private static final double LOCAL_VIDEO_NORMAL_OF_FULL = 1.8;//the size of local video by normal size /full size
    
    private static final double REMOTE_VIDEO_OF_LOCAL_VIDEO_FULL = 2.8;
    
    private static final double FULL_SCREEN_MARGIN_OF_LOCAL_VIDEO_W = 0.1;
    
    private static final double NORMAL_SCREEN_MARGIN_LEFT_RIGHT_OF_LOCAL_VIDEO_W = 0.14;
    
    private static final double NORMAL_SCREEN_MARGIN_BOTTOM_OF_LOCAL_VIDEO_W = 0.52;

    private  int HEIGHT_OF_SCREEN;
    
    private  int WIDTH_OF_SCREEN;
    
    private  int HEIGHT_OF_LOCAL_VIDEO_FULL;
    
    private  int WIDTH_OF_LOCAL_VIDEO_FULL;
    
    private  int HEIGHT_OF_LOCAL_VIDEO_NORMAL;
    
    private  int WIDTH_OF_LOCAL_VIDEO_NORMAL;
    
    private  int HEIGHT_OF_REMOTE_VIDEO_NORMAL;
    
    private  int WIDTH_OF_REMOTE_VIDEO_NORMAL;
    
    private  int FULL_SCREEN_MARGIN;
    
    private  int NORMAL_SCREEN_MARGIN_LEFT_RIGHT;
    
    private  int NORMAL_SCREEN_MARGIN_BOTTOM;
    
    public LayoutParamsHelper(Context context){
        HEIGHT_OF_SCREEN = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        WIDTH_OF_SCREEN = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();

        HEIGHT_OF_LOCAL_VIDEO_FULL = (int) (HEIGHT_OF_SCREEN * FULL_SCREEN_LOCAL_VIDEO_LENGTH_OF_SCREEN);
        WIDTH_OF_LOCAL_VIDEO_FULL = (int) (WIDTH_OF_SCREEN * FULL_SCREEN_LOCAL_VIDEO_LENGTH_OF_SCREEN);

        HEIGHT_OF_LOCAL_VIDEO_NORMAL = (int) (HEIGHT_OF_LOCAL_VIDEO_FULL * LOCAL_VIDEO_NORMAL_OF_FULL);
        WIDTH_OF_LOCAL_VIDEO_NORMAL = (int) (WIDTH_OF_LOCAL_VIDEO_FULL * LOCAL_VIDEO_NORMAL_OF_FULL);

        HEIGHT_OF_REMOTE_VIDEO_NORMAL = (int) (HEIGHT_OF_LOCAL_VIDEO_FULL * REMOTE_VIDEO_OF_LOCAL_VIDEO_FULL);
        WIDTH_OF_REMOTE_VIDEO_NORMAL = (int) (WIDTH_OF_LOCAL_VIDEO_FULL * REMOTE_VIDEO_OF_LOCAL_VIDEO_FULL);
        
        FULL_SCREEN_MARGIN = (int) (WIDTH_OF_LOCAL_VIDEO_FULL * FULL_SCREEN_MARGIN_OF_LOCAL_VIDEO_W);
        NORMAL_SCREEN_MARGIN_LEFT_RIGHT = (int) (WIDTH_OF_LOCAL_VIDEO_FULL * NORMAL_SCREEN_MARGIN_LEFT_RIGHT_OF_LOCAL_VIDEO_W);
        NORMAL_SCREEN_MARGIN_BOTTOM = (int) (WIDTH_OF_LOCAL_VIDEO_FULL * NORMAL_SCREEN_MARGIN_BOTTOM_OF_LOCAL_VIDEO_W);

        mFSLocalVideoLp = new RelativeLayout.LayoutParams(WIDTH_OF_LOCAL_VIDEO_FULL,
                HEIGHT_OF_LOCAL_VIDEO_FULL);
        mFSLocalVideoLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mFSLocalVideoLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mFSLocalVideoLp.setMargins(0, 0, FULL_SCREEN_MARGIN, FULL_SCREEN_MARGIN);

        mNSLocalVideoLp = new RelativeLayout.LayoutParams(WIDTH_OF_LOCAL_VIDEO_NORMAL,
                HEIGHT_OF_LOCAL_VIDEO_NORMAL);
        mNSLocalVideoLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mNSLocalVideoLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mNSLocalVideoLp.setMargins(NORMAL_SCREEN_MARGIN_LEFT_RIGHT, 0, 0, NORMAL_SCREEN_MARGIN_BOTTOM);
        
        mFSRemoteVideoLp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);

        mNSRemoteVideoLp = new RelativeLayout.LayoutParams(WIDTH_OF_REMOTE_VIDEO_NORMAL,
                HEIGHT_OF_REMOTE_VIDEO_NORMAL);
        mNSRemoteVideoLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mNSRemoteVideoLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mNSRemoteVideoLp.setMargins(0, 0, NORMAL_SCREEN_MARGIN_LEFT_RIGHT, NORMAL_SCREEN_MARGIN_BOTTOM);
        
        mMinistLp = new RelativeLayout.LayoutParams(1,1);
        mMinistLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mMinistLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mMinistLp.setMargins(NORMAL_SCREEN_MARGIN_LEFT_RIGHT, 0, 0, NORMAL_SCREEN_MARGIN_BOTTOM);
    }
    
    public RelativeLayout.LayoutParams getMinistLp() {
        return mMinistLp;
    }

    public RelativeLayout.LayoutParams getFSRemoteVideoLp(){
        return mFSRemoteVideoLp;
    }
    
    public RelativeLayout.LayoutParams getNSRemoteVideoLp(){
        return mNSRemoteVideoLp;
    }
    
    public RelativeLayout.LayoutParams getFSLocalVideoLp(){
        return mFSLocalVideoLp;
    }
    
    public RelativeLayout.LayoutParams getNSLocalVideoLp(){
        return mNSLocalVideoLp;
    }
    
}