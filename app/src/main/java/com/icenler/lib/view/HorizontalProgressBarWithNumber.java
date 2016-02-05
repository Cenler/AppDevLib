package com.icenler.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.icenler.lib.R;

/**
 * 自定义横向百分比进度条：基于ProgressBar
 * 1、 该控件包含基本的 ProgressBar 属性，同时也设置如下属性
 *      - 百分比文本大小、颜色、是否显示以及文本左右偏移空白区
 *      - 已完成进度条颜色及高度
 *      - 未完成进度条颜色及高度
 * 2、 属性详情参考：res/values/attr_progress_bar.xml - HorizontalProgressBarWithNumber
 */
public class HorizontalProgressBarWithNumber extends ProgressBar {

    private static final int DEFAULT_TEXT_SIZE = 10;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFC00D1;
    private static final int DEFAULT_REACHED_COLOR = 0xFFFC00D1;
    private static final int DEFAULT_UNREACHED_COLOR = 0xFFD3D6DA;
    private static final int DEFAULT_REACHED_PROGRESS_BAR_HEIGHT = 2;
    private static final int DEFAULT_UNREACHED_PROGRESS_BAR_HEIGHT = 2;
    private static final int DEFAULT_TEXT_OFFSET = 3;

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
     * 百分比数值偏移系数:单位（dp）
     */
    protected int mTextOffset = dip2px(getContext(), DEFAULT_TEXT_OFFSET);

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
     * 进度条实际宽度
     */
    protected int mRealWidth;

    /**
     * 进度百分比控制显示标志
     */
    protected boolean mIfDrawText = true;
    protected static final int VISIBLE = 0;

    public HorizontalProgressBarWithNumber(Context context) {
        super(context);
    }

    public HorizontalProgressBarWithNumber(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressBarWithNumber(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.setHorizontalScrollBarEnabled(true);
        getObtainStyleAttributes(attrs);

        mPaint.setTextSize(mTextSize);
    }

    /**
     * 获取设置的属性值
     *
     * @param attrs
     */
    private void getObtainStyleAttributes(AttributeSet attrs) {
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBarWithNumber);

        mTextColor = attributes.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_text_color, mTextColor);
        mTextSize = (int) attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_text_size, mTextSize);
        mTextOffset = (int) attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_text_offset, mTextOffset);
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
        /* specMode 对应类型 specSize = MeasureSpec.getSize(heightMeasureSpec) */
        /* AT_MOST     -- specSize 代表的是最大可获得的空间 -- wrap_content */
        /* EXACTLY     -- specSize 代表的是精确的尺寸      -- 明确值（or match_parent） */
        /* UNSPECIFIED -- 0 */
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            float textHeight = (mPaint.getFontMetrics().descent - mPaint.getFontMetrics().ascent);
            int exceptHeight = (int) (this.getPaddingTop() + this.getPaddingBottom() +
                    Math.max(Math.max(mReachedProgressBarHeight, mUnReachedProgressBarHeight), textHeight));
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(exceptHeight, MeasureSpec.EXACTLY);
        }
        /*
        * 基线（baeseline），坡顶（ascenter or top）,坡底（descenter or bottom）
        * 上坡度（ascent），下坡度（descent）
        * 行间距（leading）：坡底到下一行坡顶的距离
        * 字体的高度＝上坡度＋下坡度
        * Ps:top and bottom 即为 ascent and descent 的最大值，且 ascent 和 top 都是负数
        * */
        //  example:ÀÁÂABCfghijpqy
        /*
            Paint.ascent() = -66.83203
            Paint.descent() = 16.980469
            paint.getFontSpacing() = 83.8125
            Paint.getFontMetrics().ascent = -66.83203
            Paint.getFontMetrics().descent = 16.980469
            Paint.getFontMetrics().leading = 0.0
            Paint.getFontMetrics().top = -75.44531
            Paint.getFontMetrics().bottom = 19.511719
        */
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 获取控件真实宽度
        mRealWidth = w - this.getPaddingRight() - this.getPaddingLeft();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        boolean noNeedBg = false;

        canvas.save();
        canvas.translate(this.getPaddingLeft(), this.getHeight() / 2);

        String text = getProgress() + "%";
        float textWidth = mPaint.measureText(text);
        float textHeight = mPaint.getFontMetrics().descent + mPaint.getFontMetrics().ascent;

        float radio = this.getProgress() * 1.0f / this.getMax();
        float progressPosX = (int) ((mRealWidth - textWidth - mTextOffset) * radio + 0.5);

        // 进度临界值处理
        if (progressPosX + textWidth > mRealWidth) {
            progressPosX = mRealWidth - textWidth;
            noNeedBg = true;
        }

        // 已完成进度
        float endX = progressPosX - mTextOffset;
        if (endX > 0) {
            mPaint.setColor(mReachedBarColor);
            mPaint.setStrokeWidth(mReachedProgressBarHeight);
            canvas.drawLine(0, 0, endX, 0, mPaint);
        }

        // 百分比文本
        if (mIfDrawText) {
            mPaint.setColor(mTextColor);
            canvas.drawText(text, progressPosX, Math.abs(textHeight / 2), mPaint);
        }

        // 未完成进度
        if (!noNeedBg) {
            float start = progressPosX + mTextOffset + textWidth;
            mPaint.setColor(mUnReachedBarColor);
            mPaint.setStrokeWidth(mUnReachedProgressBarHeight);
            canvas.drawLine(start, 0, mRealWidth, 0, mPaint);
        }

        canvas.restore();
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
