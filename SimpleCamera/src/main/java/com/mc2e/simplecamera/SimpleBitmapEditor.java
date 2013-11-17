package com.mc2e.simplecamera;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

/**
 * Created by mc2e on 2013. 11. 17..
 */
public class SimpleBitmapEditor {

    public static Bitmap resizeBitmap(Bitmap src, int max) {
        if(src == null)
            return null;

        int width = src.getWidth();
        int height = src.getHeight();
        float rate = 0.0f;

        if (width > height) {
            rate = max / (float) width;
            height = (int) (height * rate);
            width = max;
        } else {
            rate = max / (float) height;
            width = (int) (width * rate);
            height = max;
        }

        return Bitmap.createScaledBitmap(src, width, height, true);
    }

    public static Bitmap cropBitmapToSize(Bitmap bitmap, int w, int h) {

        if(bitmap == null)
            return null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if(width < w && height < h)
            return bitmap;

        int x = 0;
        int y = 0;

        if(width > w)
            x = (width - w)/2;

        if(height > h)
            y = (height - h)/2;

        int cw = w; // crop width
        int ch = h; // crop height

        if(w > width)
            cw = width;

        if(h > height)
            ch = height;

        return Bitmap.createBitmap(bitmap, x, y, cw, ch);
    }

    public static Bitmap imgRotate(Bitmap bmp, int rotate){
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);

        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        bmp.recycle();

        return resizedBitmap;
    }
}
