package com.icenler.lib.view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by iCenler - 2015/7/6.
 * Description：Drawable 高级用法（可扩展任意图形，只需对宽高进行控制即可）
 *              - setBounds             绘制矩形区域
 *              - getIntrinsicWidth     宽度
 *              - getIntrinsicHeight    高度
 */
public class ShapeImageDrawable extends Drawable {

    protected Bitmap mBitmap;
    protected Paint mPaint;
    protected RectF mRectF;

    public ShapeImageDrawable(Bitmap bitmap) {
        this.mBitmap = bitmap;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);// 锯齿
        this.mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(mRectF, 100f, 100f, mPaint);
    }

    @Override
    public void setAlpha(int alpha) { mPaint.setAlpha(alpha); }

    @Override
    public void setColorFilter(ColorFilter cf) { mPaint.setColorFilter(cf); }

    @Override
    public int getOpacity() { return PixelFormat.TRANSLUCENT; }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        this.mRectF = new RectF(left, top, right, bottom);
    }

    @Override
    public int getIntrinsicWidth() { return mBitmap.getWidth(); }

    @Override
    public int getIntrinsicHeight() { return mBitmap.getHeight(); }

}
