package com.icenler.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.icenler.lib.R;

/**
 * 自定义环形百分比进度条：基于ProgressBar
 *              1、 属性详情参考：res/values/attr_progress_bar.xml - RoundProgressBarWidthNumber
 * Created by Cenler on 2015/2/9.
 */
public class RoundProgressBarWidthNumber extends ProgressBar {

    private static final int DEFAULT_TEXT_SIZE = 32;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFC00D1;
    private static final int DEFAULT_REACHED_COLOR = 0xFFFC00D1;
    private static final int DEFAULT_UNREACHED_COLOR = 0xFFD3D6DA;
    private static final int DEFAULT_REACHED_PROGRESS_BAR_HEIGHT = 5;
    private static final int DEFAULT_UNREACHED_PROGRESS_BAR_HEIGHT = 2;
    private static final int DEFAULT_RADIUS = 50;

    /**
     * 全局绘图画笔
     */
    protected Paint mPaint = new Paint();

    /**
     * 百分比进度颜色
     */
    protected int mTextColor = DEFAULT_TEXT_COLOR;

    /**
     * 百分比进度字体大小:单位（sp）
     */
    protected int mTextSize = sp2px(getContext(), DEFAULT_TEXT_SIZE);

    /**
     * 已完成进度条颜色
     */
    protected int mReachedBarColor = DEFAULT_REACHED_COLOR;

    /**
     * 未完成进度条颜色
     */
    protected int mUnReachedBarColor = DEFAULT_UNREACHED_COLOR;

    /**
     * 已完成进度条高度:单位（dp）
     */
    protected int mReachedProgressBarHeight = dip2px(getContext(), DEFAULT_REACHED_PROGRESS_BAR_HEIGHT);

    /**
     * 未完成进度条高度:单位（dp）
     */
    protected int mUnReachedProgressBarHeight = dip2px(getContext(), DEFAULT_UNREACHED_PROGRESS_BAR_HEIGHT);

    /**
     *  默认环形半径
     */
    protected int mRadius = dip2px(getContext(), DEFAULT_RADIUS);

    /**
     *  进度比控制显示标志
     */
    protected boolean mIfDrawText = true;
    protected static final int VISIBLE = 0;

    private int paintWidth;// 画笔粗细

    public RoundProgressBarWidthNumber(Context context) { this(context, null); }

    public RoundProgressBarWidthNumber(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBarWidthNumber(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.setHorizontalScrollBarEnabled(true);
        getObtainStyleAttributes(attrs);

        mPaint.setTextSize(mTextSize);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        /*
            setAntiAlias:   设置画笔的锯齿效果
            setDither:      设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
            setStrokeCap:   当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆形样式 Cap.ROUND 或方形样式 Cap.SQUARE
            setColor:       设置画笔颜色
            setARGB:        设置画笔的a,r,p,g值
            setAlpha:       设置Alpha值
            setTextSize:    设置字体尺寸
            setStyle:       设置画笔风格，空心或者实心
            setStrokeWidth: 设置空心的边框宽度
            getColor:       得到画笔的颜色
            getAlpha:       得到画笔的Alpha值
        */
    }

    /**
     * 获取设置的属性值
     * @param attrs
     */
    private void getObtainStyleAttributes(AttributeSet attrs) {
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.RoundProgressBarWidthNumber);

        mRadius = (int) attributes.getDimension(R.styleable.RoundProgressBarWidthNumber_radius, mRadius);
        mTextColor = attributes.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_text_color, mTextColor);
        mTextSize = (int) attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_text_size, mTextSize);
        mReachedBarColor = attributes.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_reached_color, mReachedBarColor);
        mUnReachedBarColor = attributes.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_unreached_color, mUnReachedBarColor);
        mReachedProgressBarHeight = (int) attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_reached_bar_height, mReachedProgressBarHeight);
        mUnReachedProgressBarHeight = (int) attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_unreached_bar_height, mUnReachedProgressBarHeight);

        int textVisible = attributes.getInt(R.styleable.HorizontalProgressBarWithNumber_progress_text_visibility, VISIBLE);
        if (textVisible != VISIBLE) {
            mIfDrawText = false;
        }

        /* Give back a previously retrieved StyledAttributes, for later re-use. */
        attributes.recycle();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 画笔粗细
        paintWidth = Math.max(mReachedProgressBarHeight, mUnReachedProgressBarHeight);

        int diameter = (mRadius + paintWidth) * 2; // 直径
        if (widthMode != MeasureSpec.EXACTLY) {
            int exceptWidth = this.getPaddingLeft() + this.getPaddingRight() + diameter;
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(exceptWidth, MeasureSpec.EXACTLY);
        }

        if (heightMode != MeasureSpec.EXACTLY) {
            int exceptHeight = this.getPaddingTop() + this.getPaddingBottom() + diameter;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(exceptHeight, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());

        String text = getProgress() + "%";
        float textWidth = mPaint.measureText(text);
        float textHeight = mPaint.getFontMetrics().descent + mPaint.getFontMetrics().ascent;

        // 未完成进度
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mUnReachedBarColor);
        mPaint.setStrokeWidth(mUnReachedProgressBarHeight);
        canvas.drawCircle(mRadius + paintWidth, mRadius + paintWidth, mRadius, mPaint);

        // 已完成进度
        mPaint.setColor(mReachedBarColor);
        mPaint.setStrokeWidth(mReachedProgressBarHeight);
        float sweepAngle = this.getProgress() * 1.0f / this.getMax() * 360;
        canvas.drawArc(new RectF(paintWidth, paintWidth, mRadius * 2 + paintWidth, mRadius * 2 + paintWidth), 0, sweepAngle, false, mPaint);

        // 百分比
        if (mIfDrawText) {
            int textX = (int) (mRadius + paintWidth - textWidth / 2);
            int textY = (int) (mRadius + paintWidth + Math.abs(textHeight / 2));
            mPaint.setColor(mTextColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(text, textX, textY, mPaint);
        }

        canvas.restore();
        /*  绘制弧形 API 使用：
            public void drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)
            oval :指定圆弧的外轮廓矩形区域。
            startAngle: 圆弧起始角度，单位为度。
            sweepAngle: 圆弧扫过的角度，顺时针方向，单位为度。
            useCenter: 如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形。
            paint: 绘制圆弧的画板属性，如颜色，是否填充等
        */
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 ps 的单位 转成为 px(像素)
     */
    public int sp2px(Context context, float spValue) {

        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;

        return (int) (spValue * fontScale + 0.5f);
    }

}