package com.jrm.skype.ui;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.jrm.skype.ui.R;

public class PictureActivityViewHolder {
    public PictureActivity mVHPictureActivity;
    
    public RelativeLayout mVHPictureRel;
    
    public ImageView mVHPictureIv;
    
    public Button mVHSaveBtn;
    
    public Button mVHCancelBtn;
    
    public Button mVHLeftBtn;
    
    public Button mVHRightBtn;
    
    public Button mVHUpBtn;
    
    public Button mVHDownBtn;

    public PictureActivityViewHolder(PictureActivity mVHPictureActivity) {
        super();
        this.mVHPictureActivity = mVHPictureActivity;
    }
    
    public void findView()
    {
        mVHPictureRel = (RelativeLayout) mVHPictureActivity.findViewById(R.id.rel_picture);
        mVHPictureIv = (ImageView) mVHPictureActivity.findViewById(R.id.iv_picture);
        mVHSaveBtn = (Button) mVHPictureActivity.findViewById(R.id.btn_picture_save);
        mVHCancelBtn = (Button) mVHPictureActivity.findViewById(R.id.btn_picture_cancel);
        mVHLeftBtn = (Button) mVHPictureActivity.findViewById(R.id.btn_picture_left);
        mVHRightBtn = (Button) mVHPictureActivity.findViewById(R.id.btn_picture_right);
        mVHUpBtn = (Button) mVHPictureActivity.findViewById(R.id.btn_picture_up);
        mVHDownBtn = (Button) mVHPictureActivity.findViewById(R.id.btn_picture_down);
    }
    
    

}
