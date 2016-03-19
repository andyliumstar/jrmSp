
package com.jrm.skype.ui;

import java.io.ByteArrayOutputStream;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import com.jrm.skype.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.jrm.skype.ui.R;
import com.jrm.skype.util.BitmapHelper;
import com.jrm.skype.util.SktApiActor;
import com.jrm.skype.view.ViewFrame;
import com.skype.api.Skype.VALIDATERESULT;
import com.skype.api.Skype.ValidateAvatarResult;

public class PictureActivityViewListenner implements OnClickListener {
    private PictureActivity mVLPictureActivity;

    private PictureActivityViewHolder mVLPictureActivityViewHolder;

    private Bitmap mVLBitmap;

    private ViewFrame mVlFrame;

    private final int LENGTH_OF_AVATAT = 96;

    private final int MAX_SQUARE_BITMAP = 640 * 480;

    private int mVLPxOfIv;// the x point of Imageview (left) comparative parent

    private int mVLPyOfIv;// the y point of Imageview (top) comparative parent

    private int mVLPxOfFrame; // the x point of Frame (left) comparative parent

    private int mVLPyOfFrame; // the x point of Frame (top) comparative parent

    private int mVLLenOfFrame; // the length of frame

    public PictureActivityViewListenner(PictureActivity mVLPictureActivity,
            PictureActivityViewHolder mVLPictureActivityViewHolder) {
        super();
        this.mVLPictureActivity = mVLPictureActivity;
        this.mVLPictureActivityViewHolder = mVLPictureActivityViewHolder;
    }

    public void setViewListenner() {
        mVLPictureActivityViewHolder.mVHSaveBtn.setOnClickListener(this);
        mVLPictureActivityViewHolder.mVHCancelBtn.setOnClickListener(this);
        mVLPictureActivityViewHolder.mVHLeftBtn.setOnClickListener(this);
        mVLPictureActivityViewHolder.mVHRightBtn.setOnClickListener(this);
        mVLPictureActivityViewHolder.mVHUpBtn.setOnClickListener(this);
        mVLPictureActivityViewHolder.mVHDownBtn.setOnClickListener(this);
    }

    public void initVar(Intent intent) {
        if (null != intent.getStringExtra("ImagePathString")) {
            mVLBitmap = BitmapHelper.decodeFileDescriptor(intent.getStringExtra("ImagePathString"),
                    MAX_SQUARE_BITMAP);
        } else if (null != intent.getStringExtra("ImageUriString")) {
            mVLBitmap = BitmapHelper.decodeFileDescriptor(
                    Uri.parse(intent.getStringExtra("ImageUriString")), MAX_SQUARE_BITMAP,
                    mVLPictureActivity.getApplicationContext());
        } else {
            mVLBitmap = null;
        }

        if (null != mVLBitmap) {
            mVLPictureActivityViewHolder.mVHPictureIv.setImageBitmap(mVLBitmap);
        } else {
            Toast.makeText(mVLPictureActivity, R.string.picture_wrong, Toast.LENGTH_SHORT).show();
            mVLPictureActivity.finish();
        }

    }

    /**
     * @param rw the Width of imageView,s parent RelativeLayout
     * @param rh the Height of imageView,s parent RelativeLayout
     * @param iw the Width of imageView
     * @param ih the Height of imageView
     */
    public void initViewFrame(int rw, int rh, int iw, int ih) {
        mVLPxOfIv = (rw - iw) / 2;
        mVLPyOfIv = (rh - ih) / 2;

        mVLLenOfFrame = (iw < ih ? iw : ih) - 4;
        mVLPxOfFrame = (rw - mVLLenOfFrame) / 2;
        mVLPyOfFrame = (rh - mVLLenOfFrame) / 2;

        mVlFrame = new ViewFrame(mVLPictureActivity, mVLPxOfFrame, mVLPyOfFrame, mVLLenOfFrame);
        mVLPictureActivityViewHolder.mVHPictureRel.removeView(mVlFrame);
        mVLPictureActivityViewHolder.mVHPictureRel.addView(mVlFrame);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_picture_save:
                save();
                break;
            case R.id.btn_picture_cancel:
                mVLPictureActivity.finish();
                break;
            case R.id.btn_picture_left:
                left();
                break;
            case R.id.btn_picture_right:
                right();
                break;
            case R.id.btn_picture_up:
                up();
                break;
            case R.id.btn_picture_down:
                down();
                break;
            default:
                break;
        }

    }

    public void save() {
        // create the select area to a bitmap
        mVLBitmap = Bitmap.createBitmap(mVLBitmap, mVLPxOfFrame - mVLPxOfIv, mVLPyOfFrame
                - mVLPyOfIv, mVLLenOfFrame, mVLLenOfFrame);
        if (null != mVLBitmap) {
            // make the bitmap to 96*96
            mVLBitmap = ThumbnailUtils.extractThumbnail(mVLBitmap, LENGTH_OF_AVATAT,
                    LENGTH_OF_AVATAT);
            if (null != mVLBitmap) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] avatar;
                mVLBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                avatar = baos.toByteArray();
                ValidateAvatarResult result;
                result = SktApiActor.checkAvatar(avatar);
                Log.v("PictureActivityViewListenner", "bitmap size---------" + avatar.length
                        + "---w:" + mVLBitmap.getWidth());
                if (null != result && VALIDATERESULT.VALIDATED_OK == result.result) {
                    SktApiActor.setAccountAvatar(avatar);
                } else {
                    Toast.makeText(mVLPictureActivity, R.string.picture_wrong, Toast.LENGTH_SHORT)
                            .show();
                }
            }
            mVLPictureActivity.finish();
            return;

        }  
        Toast.makeText(mVLPictureActivity, R.string.picture_wrong, Toast.LENGTH_SHORT).show();
        mVLPictureActivity.finish();
    }

    public void left() {
        if (mVLPxOfFrame <= mVLPxOfIv + 2)
            return;

        mVLPxOfFrame -= 5;
        mVLPxOfFrame = (mVLPxOfFrame <= mVLPxOfIv + 2 ? mVLPxOfIv + 2 : mVLPxOfFrame);

        mVLPictureActivityViewHolder.mVHPictureRel.removeView(mVlFrame);
        mVlFrame = new ViewFrame(mVLPictureActivity, mVLPxOfFrame, mVLPyOfFrame, mVLLenOfFrame);
        mVLPictureActivityViewHolder.mVHPictureRel.addView(mVlFrame);
    }

    public void right() {
        if (mVLPxOfFrame + mVLLenOfFrame >= mVLPxOfIv
                + mVLPictureActivityViewHolder.mVHPictureIv.getWidth() - 2)
            return;

        mVLPxOfFrame += 5;
        if (mVLPxOfFrame + mVLLenOfFrame >= mVLPxOfIv
                + mVLPictureActivityViewHolder.mVHPictureIv.getWidth() - 2)
            mVLPxOfFrame = mVLPxOfIv + mVLPictureActivityViewHolder.mVHPictureIv.getWidth() - 2
                    - mVLLenOfFrame;

        mVLPictureActivityViewHolder.mVHPictureRel.removeView(mVlFrame);
        mVlFrame = new ViewFrame(mVLPictureActivity, mVLPxOfFrame, mVLPyOfFrame, mVLLenOfFrame);
        mVLPictureActivityViewHolder.mVHPictureRel.addView(mVlFrame);
    }

    public void up() {
        if (mVLPyOfFrame <= mVLPyOfIv + 2)
            return;

        mVLPyOfFrame -= 5;
        mVLPyOfFrame = (mVLPyOfFrame <= mVLPyOfIv + 2 ? mVLPyOfIv + 2 : mVLPyOfFrame);

        mVLPictureActivityViewHolder.mVHPictureRel.removeView(mVlFrame);
        mVlFrame = new ViewFrame(mVLPictureActivity, mVLPxOfFrame, mVLPyOfFrame, mVLLenOfFrame);
        mVLPictureActivityViewHolder.mVHPictureRel.addView(mVlFrame);
    }

    public void down() {
        if (mVLPyOfFrame + mVLLenOfFrame >= mVLPyOfIv
                + mVLPictureActivityViewHolder.mVHPictureIv.getHeight() - 2)
            return;

        mVLPyOfFrame += 5;
        if (mVLPyOfFrame + mVLLenOfFrame >= mVLPyOfIv
                + mVLPictureActivityViewHolder.mVHPictureIv.getHeight() - 2)
            mVLPyOfFrame = mVLPyOfIv + mVLPictureActivityViewHolder.mVHPictureIv.getHeight() - 2
                    - mVLLenOfFrame;

        mVLPictureActivityViewHolder.mVHPictureRel.removeView(mVlFrame);
        mVlFrame = new ViewFrame(mVLPictureActivity, mVLPxOfFrame, mVLPyOfFrame, mVLLenOfFrame);
        mVLPictureActivityViewHolder.mVHPictureRel.addView(mVlFrame);
    }

}
