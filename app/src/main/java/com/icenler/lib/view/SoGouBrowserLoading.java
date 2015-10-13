package com.icenler.lib.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.icenler.lib.utils.ScreenUtil;

/**
 * Created by iCenler - 2015/10/13.
 * Description：跳动的小球 —— 加载提示控件
 */
public class SoGouBrowserLoading extends View {

    private static final int DEF_SHADER_COLOR = 0xFFACACAC;
    private static final int DEF_LOADING_COLOR = 0xFF03A9F4;
    private static final int DEF_JUMP_HEIGHT = 36;

    private boolean mIsLoading = true;
    private float density;
    private float jumpSpeed = 1.2f;
    private int startX, startY, endY, currentY = -1;//起点、终点、当前点
    private int loadingColor = DEF_LOADING_COLOR;
    private int shaderColor = DEF_SHADER_COLOR;
    private int jumpHeight = DEF_JUMP_HEIGHT;
    private int radius = 12;

    private RectF rectF;
    private Paint mPaint;//画笔

    public SoGouBrowserLoading(Context context) {
        this(context, null);
    }

    public SoGouBrowserLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        density = ScreenUtil.getDisPlayMetrics().density;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取视图的中心点
        startX = getWidth() >> 1;
        endY = getHeight() >> 1;
        startY = endY - ScreenUtil.dp2px(jumpHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (currentY == -1) {
            playAnimator();
        } else {
            drawShape(canvas);
            drawShader(canvas);
        }
    }

    private void playAnimator() {
        // 取Y轴方向上的变化即可
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startY, endY);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentY = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator(jumpSpeed));
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setDuration(500);
        valueAnimator.start();
    }

    /**
     * 绘制圆形
     *
     * @param canvas
     */
    private void drawShape(Canvas canvas) {
        //当接触到底部时候，我们为了要描绘一种压扁的效果
        mPaint.setColor(loadingColor);
        if (endY - currentY > 12) {
            canvas.drawCircle(startX, currentY, radius * density, mPaint);
        } else {
            rectF = new RectF(
                    startX - radius * density - 3, currentY - radius * density + 6,
                    startX + radius * density + 3, currentY + radius * density);
            canvas.drawOval(rectF, mPaint);
        }
    }

    /**
     * 绘制阴影部分，由椭圆来支持，根据高度比来底部阴影的大小
     */
    private void drawShader(Canvas canvas) {
        int dx = endY - startY;//计算差值高度
        int dx1 = currentY - startY;//计算当前点的高度差值
        float ratio = (float) (dx1 * 1.0 / dx);
        if (ratio <= 0.3) {//当高度比例小于0.3，所在比较高的时候就不进行绘制影子
            return;
        }

        int ovalRadius = (int) (radius * ratio * density);
        mPaint.setColor(shaderColor);//设置倒影的颜色
        rectF = new RectF(startX - ovalRadius, endY + 32, startX + ovalRadius, endY + 38);//绘制椭圆
        canvas.drawOval(rectF, mPaint);
    }

    /**
     * 设置颜色
     *
     * @param color
     */
    public void setColor(int color) {
        this.loadingColor = color;
    }

}
