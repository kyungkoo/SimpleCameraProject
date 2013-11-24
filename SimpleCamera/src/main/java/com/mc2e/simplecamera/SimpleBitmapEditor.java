package com.mc2e.simplecamera;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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

    @Deprecated
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

    /**
     * 타원으로 크롭하는 메소드
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap cropBitmapToOval(Bitmap bitmap, int w, int h) {
        Bitmap output;

        int length = h;

        output = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        int margin = (length - w)/2;

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(margin, 0, w+margin, h);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
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
