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

public class SoGouBrowserLoading extends View {

    private static final int DEF_SHADER_COLOR = 0xFFACACAC;
    private static final int DEF_LOADING_COLOR = 0xFF03A9F4;

    private float density;
    private int color = DEF_LOADING_COLOR;//颜色
    private int radius = 10;//半径
    private int startY, startX, endY, currentY;//起点、终点、当前点

    private RectF rectF;
    private Paint mPaint;//画笔

    public SoGouBrowserLoading(Context context) {
        this(context, null);
    }

    public SoGouBrowserLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = ScreenUtil.getDisPlayMetrics().density;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取视图的中心点
        startX = getWidth() / 2;
        endY = getHeight() / 2;
        startY = endY * 5 / 6;
        mPaint.setColor(DEF_LOADING_COLOR);
        if (currentY == 0) {
            playAnimator();
        } else {
            drawCircle(canvas);
            drawShader(canvas);
        }
    }

    //动画执行
    private void playAnimator() {
        //我们只需要取Y轴方向上的变化即可
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startY, endY);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentY = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator(1.2f));
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
    private void drawCircle(Canvas canvas) {
        //当接触到底部时候，我们为了要描绘一种压扁的效果
        if (endY - currentY > 10) {
            canvas.drawCircle(startX, currentY, radius * density, mPaint);
        } else {
            rectF = new RectF(
                    startX - radius * density - 2, currentY - radius * density + 5,
                    startX + radius * density + 2, currentY + radius * density);
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
        mPaint.setColor(DEF_SHADER_COLOR);//设置倒影的颜色
        rectF = new RectF(startX - ovalRadius, endY + 10, startX + ovalRadius, endY + 15);//绘制椭圆
        canvas.drawOval(rectF, mPaint);
    }

    /**
     * 设置颜色
     *
     * @param colorRes
     */
    public void setColor(int colorRes) {
        this.color = colorRes;
    }

}
