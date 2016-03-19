package com.jrm.skype.dialog;

import android.widget.Button;
import android.widget.TextView;

import com.jrm.skype.dialog.ChangePictureDialog;
import com.jrm.skype.ui.R;

public class ChangePictureViewHolder {
    public ChangePictureDialog mVHChangePictureDialog;
    
    public Button mVHFromAlbumBtn;
    
    public Button mVHTakePictureBtn;
    
    public Button mVHClearPictureBtn;
    
    public TextView mVHBackTv;

    public ChangePictureViewHolder(ChangePictureDialog mVHChangePictureDialog) {
        super();
        this.mVHChangePictureDialog = mVHChangePictureDialog;
    }
    
    public void findView()  {
        mVHFromAlbumBtn = (Button) mVHChangePictureDialog.getLayout().findViewById(R.id.btn_change_picture_album);
        mVHTakePictureBtn = (Button) mVHChangePictureDialog.getLayout().findViewById(R.id.btn_change_picture_camera);
        mVHClearPictureBtn = (Button) mVHChangePictureDialog.getLayout().findViewById(R.id.btn_change_picture_clear);
        mVHBackTv = (TextView) mVHChangePictureDialog.getLayout().findViewById(R.id.tv_change_picture_back);
    }
    

}
