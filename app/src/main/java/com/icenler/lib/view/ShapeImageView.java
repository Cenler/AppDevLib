package com.icenler.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.icenler.lib.R;
import com.icenler.lib.utils.ScreenUtil;

import java.lang.ref.WeakReference;

/**
 * Created by iCenler - 2015/5/18.
 * Description：圆角、圆形图片控件
 */
public class ShapeImageView extends ImageView {

    protected static final int DEFAULT_CORNER_RADIO = 8;
    protected static final int TYPE_CIRCLE = 0;
    protected static final int TYPE_ROUND = 1;

    protected Paint mPaint;
    protected Bitmap mSrc;
    protected Xfermode mXfermode;
    protected WeakReference<Bitmap> mWeakBitmap;
    protected int mCornerRadio = ScreenUtil.dp2px(DEFAULT_CORNER_RADIO);
    protected int mImageType = TYPE_CIRCLE;

    public ShapeImageView(Context context) {
        this(context, null);
    }

    public ShapeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        getObtainStyledAttributes(attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 圆形保持宽度与高度一致
        if (mImageType == TYPE_CIRCLE) {
            int minWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            setMeasuredDimension(minWidth, minWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = mWeakBitmap == null ? null : mWeakBitmap.get();
        if (bitmap == null || bitmap.isRecycled()) {
            Drawable drawable = getDrawable();
            if (drawable != null) {
                int width = drawable.getMinimumWidth();
                int height = drawable.getMinimumHeight();

                bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                Canvas drawCanvas = new Canvas(bitmap);

                float scale = 1.0f;
                if (mImageType == TYPE_ROUND)
                    scale = Math.max(getWidth() * 1.0f / width, getHeight() * 1.0f / height);
                else
                    scale = getWidth() * 1.0f / Math.min(width, height);

                drawable.setBounds(0, 0, (int) (scale * width), (int) (scale * height));
                drawable.draw(drawCanvas);

                if (mSrc == null || mSrc.isRecycled())
                    mSrc = getBitmap();

                mPaint.reset();
                mPaint.setFilterBitmap(false);
                mPaint.setXfermode(mXfermode);
                drawCanvas.drawBitmap(mSrc, 0, 0, mPaint);

                mPaint.setXfermode(null);
                canvas.drawBitmap(bitmap, 0, 0, null);
                mWeakBitmap = new WeakReference<Bitmap>(bitmap);
            }
        } else {
            mPaint.setXfermode(null);
            canvas.drawBitmap(bitmap, 0, 0, mPaint);
        }
    }

    /**
     * 绘制形状
     */
    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);

        if (mImageType == TYPE_ROUND)
            canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), mCornerRadio, mCornerRadio, paint);
        else
            canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, paint);

        return bitmap;
    }

    private void getObtainStyledAttributes(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ShapeImageView);
        final int N = array.length();
        for (int i = 0; i < N; i++) {
            int index = array.getIndex(i);
            switch (index) {
                case R.styleable.ShapeImageView_image_type:
                    mImageType = array.getInteger(index, mImageType);
                    break;
                case R.styleable.ShapeImageView_corner_radius:
                    mCornerRadio = array.getDimensionPixelSize(index, mCornerRadio);
                    break;

                default:
                    break;
            }
        }

        array.recycle();
    }

}
