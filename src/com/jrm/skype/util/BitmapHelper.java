package com.jrm.skype.util;

import java.io.File;
import java.io.FileInputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import com.jrm.skype.util.Log;

public class BitmapHelper {
    public final static String TAG = "BitmapHelper";
    
    public final static int MAX_LENGTH = 10000;
    
    public final static int AVA_LENGTH = 7000;
    
    public final static int MIN_SIDE_LENGTH = -1;
    
    public static int computeSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxSquare) {
        
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxSquare);
        int roundedSize;

        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize <= initialSize) {
                roundedSize <<= 1;
             }
            roundedSize >>= 1;
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
        
    }

    /**
     * get the init sample size
     * @param options
     * @param minSideLength
     * @param maxSquare , the mix size(W*H) of the image
     * @return the sample size
     */
    public static int computeInitialSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxSquare) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxSquare == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxSquare));
        
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxSquare == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * check the image ,whether it is too large
     * @param width ,the width of image
     * @param height, the height of image
     * @return 
     */
    public static boolean checkImage(int width, int height)
    {
        if(width <= 0 || height <= 0)
        {
            return false;
        }
        
        int max = width > height ? width : height;
        if(max > MAX_LENGTH)
        {
            return false;
        }
        
        if(width == height)
        {
            if(width > AVA_LENGTH)
            {
                return false;
            }
        }   
        
        if(width * height > AVA_LENGTH * AVA_LENGTH)
        {
            return false;
        }
        
        return true;
    }
    /**
     * 
     * @param path, the image path
     * @param maxSquare , the mix size(W*H) of the image
     * @return the decode Bitmap
     */
    
    public static Bitmap decodeFileDescriptor(String path, int maxSquare) 
    {
        FileInputStream  fileInputStream;
        try 
        {
            File file = new File (path);
            fileInputStream = new FileInputStream(file);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }

        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        
        try 
        {
            BitmapFactory.decodeStream(fileInputStream, null, opts);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            closeFs(fileInputStream);
            
            return null;
        }

        int w = opts.outWidth;
        int h = opts.outHeight;
     
        if(!checkImage(w, h))
        {
            closeFs(fileInputStream);
            Log.e(TAG, "picture too large");
            return null;
        }
        
        opts.inSampleSize = computeSampleSize(opts, MIN_SIDE_LENGTH, maxSquare);
        
        opts.inJustDecodeBounds = false;
        // Disable Dithering mode
        opts.inDither = false;
        opts.inPreferredConfig = Config.RGB_565;
         
        opts.inPurgeable = false;
       
        try 
        {
            bitmap = BitmapFactory.decodeFileDescriptor(
                    fileInputStream.getFD(), null, opts);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            closeFs(fileInputStream);
            return null;
        }
        
        if (bitmap != null) 
        {
            if (w == bitmap.getWidth() && opts.inSampleSize > 1)
            {
                try 
                {
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, w / opts.inSampleSize, h / opts.inSampleSize);
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        }
        
        return bitmap;
    }
    
    public static Bitmap decodeFileDescriptor(Uri uri, int maxSquare,Context context) 
    {
        Bitmap bitmap = null;
        FileInputStream fs = null;

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        
        try 
        {
            fs = (FileInputStream) context.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(fs, null, opts);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            closeFs(fs);
            
            return null;
        }

        int w = opts.outWidth;
        int h = opts.outHeight;
     
        if(!checkImage(w, h))
        {
           closeFs(fs);
            
            return null;
        }
        
        opts.inSampleSize = computeSampleSize(opts, -1, maxSquare);
        
        opts.inJustDecodeBounds = false;
        // Disable Dithering mode
        opts.inDither = false;
        opts.inPreferredConfig = Config.RGB_565;
         
        opts.inPurgeable = false;

        try 
        {
            bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, opts);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
        finally 
        {
            closeFs(fs);
        }
        
        if (bitmap != null) 
        {
            if (w == bitmap.getWidth() && opts.inSampleSize > 1)
            {
                try 
                {
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, w / opts.inSampleSize, h / opts.inSampleSize);
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        }
        
        return bitmap;
    }
    
    public static void closeFs(FileInputStream fs)
    {
        try
        {
            if (fs != null)
            {
                fs.close();
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        fs = null;
    }
 
}


