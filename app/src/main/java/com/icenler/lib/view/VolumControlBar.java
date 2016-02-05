package com.icenler.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.icenler.lib.R;
import com.icenler.lib.utils.ScreenUtil;

/**
 * Created by iCenler - 2015/5/11.
 * Description：圆形音量控件
 */
public class VolumControlBar extends View {

    private static final int DEFAULT_VOLUM_REACHED_COLOR = 0xCF000000;
    private static final int DEFAULT_VOLUM_UNREACHED_COLOR = 0xCFFFFFFF;
    private static final int DEFAULT_VOLUM_DOT_COUNT = 12;
    private static final int DEFAULT_VOLUM_DOT_MARGIN = 8;
    private static final int DEFAULT_VOLUM_DOT_STROKE = 10;

    protected Paint mPaint;
    protected RectF mRectF;

    /**
     * 已达到的样式背景色
     */
    protected int mVolumReachedColor = DEFAULT_VOLUM_REACHED_COLOR;

    /**
     * 未达到的样式背景色
     */
    protected int mVolumUnReachedColor = DEFAULT_VOLUM_UNREACHED_COLOR;

    /**
     * 可调节数量
     */
    protected int mVolumDotCount = DEFAULT_VOLUM_DOT_COUNT;

    /**
     * 调节指示器间距
     */
    protected int mVolumDotMargin = DEFAULT_VOLUM_DOT_MARGIN;

    /**
     * 调节指示器厚度
     */
    protected int mVolumDotStroke = ScreenUtil.dp2px(DEFAULT_VOLUM_DOT_STROKE);

    /**
     * 中心图标
     */
    protected Bitmap mVolumIcon;

    public VolumControlBar(Context context) {
        this(context, null);
    }

    public VolumControlBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VolumControlBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        getObtainStyledAttributes(attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStrokeWidth(mVolumDotStroke);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);// 核心：定义线段断点形状为圆头

        float center = getWidth() * 0.5f;
        float radius = center - mVolumDotStroke;

        drawOval(canvas, center, radius);// 绘制圆点

        /** 内切正方形的距离顶部 - 计算方式：假设 R 为直径，设边长为 X
         *  X*X + X*X = R*R
         *  2*X*X = R*R
         *  x√2 = R
         *  x = √2 / 2 * R
         * */
        mRectF.left = (float) (radius - Math.sqrt(2) * 0.5f * radius + mVolumDotStroke);
        mRectF.top = (float) (radius - Math.sqrt(2) * 0.5f * radius + mVolumDotStroke);
        mRectF.bottom = (float) (mRectF.left + Math.sqrt(2) * radius);
        mRectF.right = (float) (mRectF.left + Math.sqrt(2) * radius);

        /**
         * 如果图片比较小，那么根据图片的尺寸放置到正中心
         */
        if (mVolumIcon.getWidth() < Math.sqrt(2) * radius) {
            mRectF.left = (float) (mRectF.left + Math.sqrt(2) * radius * 0.5f - mVolumIcon.getWidth() * 0.5f);
            mRectF.top = (float) (mRectF.top + Math.sqrt(2) * radius * 0.5f - mVolumIcon.getHeight() * 0.5f);
            mRectF.right = mRectF.left + mVolumIcon.getWidth();
            mRectF.bottom = mRectF.top + mVolumIcon.getHeight();
        }

        canvas.drawBitmap(mVolumIcon, null, mRectF, mPaint);
    }

    /**
     * 绘制指示器
     *
     * @param canvas
     * @param centre
     * @param radius
     */
    private void drawOval(Canvas canvas, float centre, float radius) {
        /**
         * 根据需要画的个数以及间隙计算每个块块所占的比例*360
         */
        float itemSize = (360 * 1.0f - mVolumDotCount * mVolumDotMargin) / mVolumDotCount;

        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限

        mPaint.setColor(mVolumUnReachedColor); // 设置圆环的颜色
        for (int i = 0; i < mVolumDotCount; i++) {
            canvas.drawArc(oval, i * (itemSize + mVolumDotMargin) - 90, itemSize, false, mPaint); // 根据进度画圆弧
        }

        mPaint.setColor(mVolumReachedColor); // 设置圆环的颜色
        for (int i = 0; i < mCurrentDot; i++) {
            canvas.drawArc(oval, i * (itemSize + mVolumDotMargin) - 90, itemSize, false, mPaint); // 根据进度画圆弧
        }
    }

    /**
     * 获取属性集
     *
     * @param attrs
     */
    private void getObtainStyledAttributes(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.VolumControlBar);
        int length = array.length();
        for (int i = 0; i < length; i++) {
            int index = array.getIndex(i);
            switch (index) {
                case R.styleable.VolumControlBar_volum_reached_color:
                    mVolumReachedColor = array.getColor(index, mVolumReachedColor);
                    break;
                case R.styleable.VolumControlBar_volum_unreached_color:
                    mVolumUnReachedColor = array.getColor(index, mVolumUnReachedColor);
                    break;
                case R.styleable.VolumControlBar_volum_src:
                    mVolumIcon = BitmapFactory.decodeResource(getResources(), array.getResourceId(index, 0));
                    break;
                case R.styleable.VolumControlBar_volum_dot_count:
                    mVolumDotCount = array.getInteger(index, mVolumDotCount);
                    break;
                case R.styleable.VolumControlBar_volum_dot_stroke:
                    mVolumDotStroke = array.getDimensionPixelSize(index, mVolumDotStroke);
                    break;
                case R.styleable.VolumControlBar_volum_dot_margin:
                    mVolumDotMargin = array.getInteger(index, mVolumDotMargin);
                    break;

                default:
                    break;
            }
        }

        array.recycle();
    }

    private int xDown, xUp;
    private int mCurrentDot = 3;// 默认指示器填充个数

    /**
     * 当前数量+1
     */
    public void handleUp() {
        if (mCurrentDot != mVolumDotCount) {
            mCurrentDot++;
            postInvalidate();
        }
    }

    /**
     * 当前数量-1
     */
    public void handleDown() {
        if (mCurrentDot != 0) {
            mCurrentDot--;
            postInvalidate();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                xUp = (int) event.getY();
                // 下滑
                if (xUp > xDown)
                    handleDown();
                else
                    handleUp();
                break;
        }

        return true;
    }

}
