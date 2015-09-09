package com.icenler.lib.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

public class TabItem extends View {

    private int mTextSize = 12;
    private int mTextColorSelect = 0xff45c01a;
    private int mTextColorNormal = 0xff777777;
    private Paint mTextPaintNormal;
    private Paint mTextPaintSelect;
    private int mViewHeight, mViewWidth;
    private String mTextValue;
    private Bitmap mIconNormal;
    private Bitmap mIconSelect;
    private Rect mBoundText;
    private Paint mIconPaintSelect;
    private Paint mIconPaintNormal;

    public TabItem(Context context) {
        super(context);
        initView();
        initText();
    }

    private void initView() {
        mBoundText = new Rect();
    }

    private void initText() {
        mTextPaintNormal = new Paint();
        mTextPaintNormal.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, getResources().getDisplayMetrics()));
        mTextPaintNormal.setColor(mTextColorNormal);
        mTextPaintNormal.setAntiAlias(true);
        mTextPaintNormal.setAlpha(0xFF);

        mTextPaintSelect = new Paint();
        mTextPaintSelect.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, getResources().getDisplayMetrics()));
        mTextPaintSelect.setColor(mTextColorSelect);
        mTextPaintSelect.setAntiAlias(true);
        mTextPaintSelect.setAlpha(0x00);

        mIconPaintNormal = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIconPaintNormal.setAlpha(0xFF);// 不透明

        mIconPaintSelect = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIconPaintSelect.setAlpha(0x00);// 透明
    }

    private void measureText() {
        mTextPaintNormal.getTextBounds(mTextValue, 0, mTextValue.length(), mBoundText);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int realWidth = 0, realHeight = 0;

        measureText();
        int contentWidth = Math.max(mBoundText.width(), mIconNormal.getWidth());
        int desiredWidth = getPaddingLeft() + getPaddingRight() + contentWidth;
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                realWidth = Math.min(widthSize, desiredWidth);
                break;
            case MeasureSpec.EXACTLY:
                realWidth = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                realWidth = desiredWidth;
                break;
        }
        int contentHeight = mBoundText.height() + mIconNormal.getHeight();
        int desiredHeight = getPaddingTop() + getPaddingBottom() + contentHeight;
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                realHeight = Math.min(heightSize, desiredHeight);
                break;
            case MeasureSpec.EXACTLY:
                realHeight = heightSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                realHeight = contentHeight;
                break;
        }
        setMeasuredDimension(realWidth, realHeight);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBitmap(canvas);
        drawText(canvas);
    }

    private void drawBitmap(Canvas canvas) {
        float x = (mViewWidth - mIconNormal.getWidth()) / 2.0f;
        float y = (mViewHeight - mIconNormal.getHeight() - mBoundText.height()) / 2.0f;
        canvas.drawBitmap(mIconNormal, x, y, mIconPaintNormal);
        canvas.drawBitmap(mIconSelect, x, y, mIconPaintSelect);
    }

    private void drawText(Canvas canvas) {
        float x = (mViewWidth - mBoundText.width()) / 2.0F;
        float y = (mViewHeight + mIconNormal.getHeight() + mBoundText.height()) / 2.0F;
        canvas.drawText(mTextValue, x, y, mTextPaintNormal);
        canvas.drawText(mTextValue, x, y, mTextPaintSelect);
    }

    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
        mTextPaintNormal.setTextSize(textSize);
        mTextPaintSelect.setTextSize(textSize);
    }

    public void setTextColorSelect(int mTextColorSelect) {
        this.mTextColorSelect = mTextColorSelect;
        mTextPaintSelect.setColor(mTextColorSelect);
        mTextPaintSelect.setAlpha(0);
    }

    public void setTextColorNormal(int mTextColorNormal) {
        this.mTextColorNormal = mTextColorNormal;
        mTextPaintNormal.setColor(mTextColorNormal);
        mTextPaintNormal.setAlpha(0xff);
    }

    public void setTextValue(String TextValue) {
        this.mTextValue = TextValue;
    }

    public void setIconText(int[] iconSelId, String TextValue) {
        this.mIconSelect = BitmapFactory.decodeResource(getResources(), iconSelId[0]);
        this.mIconNormal = BitmapFactory.decodeResource(getResources(), iconSelId[1]);
        this.mTextValue = TextValue;
    }

    public void setTabAlpha(float alpha) {
        int paintAlpha = (int) (alpha * 255);
        mIconPaintSelect.setAlpha(paintAlpha);
        mIconPaintNormal.setAlpha(255 - paintAlpha);
        mTextPaintSelect.setAlpha(paintAlpha);
        mTextPaintNormal.setAlpha(255 - paintAlpha);
        invalidate();
    }
}
