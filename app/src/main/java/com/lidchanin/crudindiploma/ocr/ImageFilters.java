package com.lidchanin.crudindiploma.ocr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.lidchanin.crudindiploma.R;

/**
 * Created by Alexander Destroyed on 12.05.2017.
 */

public class ImageFilters {
    public Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness)
    {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });
        cm.setSaturation(0);

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return cropBitmap1(ret);
    }
    private Bitmap cropBitmap1(Bitmap bitmap) {
        Bitmap bmOverlay = Bitmap.createBitmap(800 , 600, Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bitmap, -150, -400, null);

        return bmOverlay;
    }

}