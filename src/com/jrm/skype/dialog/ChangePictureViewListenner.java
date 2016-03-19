package com.jrm.skype.dialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.jrm.skype.api.SktUtils;
import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.dialog.ChangePictureDialog;
import com.jrm.skype.ui.OptionsActivity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.jrm.skype.ui.R;
import com.jrm.skype.util.SktApiActor;

public class ChangePictureViewListenner implements OnClickListener {
    private ChangePictureDialog mVLChangePictureDialog;
    
    private ChangePictureViewHolder mVLChangePictureViewHolder;
    
    private String mVLFileName;
    
    private Date mVLDate;
    
    private SimpleDateFormat mVLFormat;
    
    public ChangePictureViewListenner(ChangePictureDialog mVLChangePictureDialog,
            ChangePictureViewHolder mVLChangePictureViewHolder) {
        super();
        this.mVLChangePictureDialog = mVLChangePictureDialog;
        this.mVLChangePictureViewHolder = mVLChangePictureViewHolder;
        
        mVLFormat = new SimpleDateFormat("yyyyMMdd_HHmmss"); 
        mVLDate =new Date();
    }

    public void setViewListenner(){
        mVLChangePictureViewHolder.mVHBackTv.setOnClickListener(this);
        mVLChangePictureViewHolder.mVHClearPictureBtn.setOnClickListener(this);
        mVLChangePictureViewHolder.mVHFromAlbumBtn.setOnClickListener(this);
        mVLChangePictureViewHolder.mVHTakePictureBtn.setOnClickListener(this);
        mVLChangePictureViewHolder.mVHClearPictureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent ;
        switch( v.getId() ){
            case R.id.btn_change_picture_album:
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                mVLChangePictureDialog.getOwnerActivity().startActivityForResult(intent,SKYPECONSTANT.OPTIONSPICTURE.FROM_ALBUM);
                mVLChangePictureDialog.dismiss();
                break;
            case R.id.btn_change_picture_camera:
                if (!SktUtils.cameraExists()) {
                    Toast.makeText(mVLChangePictureDialog.getOwnerActivity(), R.string.no_camera,
                            Toast.LENGTH_SHORT).show();
                } else {
                mVLFileName=mVLFormat.format(mVLDate);
                ((OptionsActivity) mVLChangePictureDialog.getOwnerActivity())
                        .setFilePath("/mnt/sdcard/DCIM/Camera/" + mVLFileName + ".jpg");
                File file = new File("/mnt/sdcard/DCIM/Camera/"+mVLFileName+".jpg");
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                mVLChangePictureDialog.getOwnerActivity().startActivityForResult(intent,SKYPECONSTANT.OPTIONSPICTURE.FROM_CAMERA);
                mVLChangePictureDialog.dismiss();
                }
                break;
            case R.id.btn_change_picture_clear:
                byte[] avatar = new byte[0];
                SktApiActor.setAccountAvatar(avatar);
                mVLChangePictureDialog.dismiss();
                break;
            case R.id.tv_change_picture_back:
                mVLChangePictureDialog.dismiss();
                break;
        }
    }

}
