package com.icenler.lib.view.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.icenler.lib.R;
import com.icenler.lib.utils.LogUtil;
import com.icenler.lib.utils.ScreenUtil;

/**
 * Created by Fangde on 2016/1/22.
 * Description: 蜘蛛网分布图 示例——待完善扩充
 */
public class RadarView extends View {

    private float mRaduis;
    private float mAngle;            // 弧度数据
    private int centerX, centerY;
    private int shapeCnt = 6;
    private int textSize = ScreenUtil.sp2px(12);
    private String[] titles = {"A", "B", "C", "D", "E", "F"};
    private int[] percentFactor = {95, 80, 92, 88, 90, 85};

    private Paint mShapePaint;
    private Paint mTextPaint;
    private Paint mRegionPaint;
    private Path mShapePath;

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化相关参数
     */
    private void init() {
        mShapePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShapePaint.setStrokeWidth(1);
        mShapePaint.setColor(getResources().getColor(R.color.color_grey));
        mShapePaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(getResources().getColor(R.color.color_ink));

        mRegionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRegionPaint.setColor(getResources().getColor(R.color.color_blue));
        mRegionPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mRegionPaint.setAlpha(180);

        mShapePath = new Path();

        mAngle = (float) (Math.PI * 2 / shapeCnt);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mRaduis = (Math.min(w, h) >> 1) * 0.9f;
        centerX = w >> 1;
        centerY = h >> 1;
        LogUtil.d("Raduis: " + mRaduis + ", CenterX: " + centerX + ", CenterY: " + centerY);

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPolygon(canvas);
        drawLine(canvas);
        drawText(canvas);
        drawRegion(canvas);
    }

    /**
     * 绘制正多边形
     *
     * @param canvas
     */
    private void drawPolygon(Canvas canvas) {
        float dis = mRaduis / (shapeCnt - 1);
        for (int i = 1; i < shapeCnt; i++) {
            float r = dis * i;// 半径
            LogUtil.i("Current Radius: " + r);

            mShapePath.reset();
            for (int j = 0; j < shapeCnt; j++) {
                float x, y;
                if (j == 0) {
                    // 起始坐标
                    x = centerX + r;
                    y = centerY;
                    mShapePath.moveTo(x, y);
                } else {
                    // 三角函数计算坐标
                    x = (float) (centerX + Math.cos(mAngle * j) * r);
                    y = (float) (centerY + Math.sin(mAngle * j) * r);
                    mShapePath.lineTo(x, y);
                }

                LogUtil.d("Point - " + j + " (" + x + ", " + y + ")");
            }
            mShapePath.close();

            canvas.drawPath(mShapePath, mShapePaint);
        }
    }

    /**
     * 绘制等分线
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        for (int i = 0; i < shapeCnt; i++) {
            mShapePath.reset();
            mShapePath.moveTo(centerX, centerY);

            float x = (float) (centerX + Math.cos(mAngle * i) * mRaduis);
            float y = (float) (centerY + Math.sin(mAngle * i) * mRaduis);

            mShapePath.lineTo(x, y);
            LogUtil.d("Point - " + i + " (" + x + ", " + y + ")");

            canvas.drawPath(mShapePath, mShapePaint);
        }
    }

    /**
     * 绘制区域文本
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;

        for (int i = 0; i < shapeCnt; i++) {
            float radian = mAngle * i;
            float baseX = (float) (centerX + Math.cos(radian) * (mRaduis + 24));
            float baseY = (float) (centerY + Math.sin(radian) * (mRaduis + 24));

            // 绘制基线坐标偏移量
            float offsetY = (Math.abs(fontMetrics.ascent + fontMetrics.descent)) / 2;

            // 根据弧度数判断所在象限
            if (radian >= 0 && radian <= Math.PI) {
                canvas.drawText(titles[i], baseX, baseY + offsetY, mTextPaint);
            } else {
                canvas.drawText(titles[i], baseX, baseY, mTextPaint);
            }
        }
    }

    /**
     * 分布区域图
     *
     * @param canvas
     */
    private void drawRegion(Canvas canvas) {
        mShapePath.reset();
        for (int i = 0; i < shapeCnt; i++) {
            float p = percentFactor[i] * 1.0f / 100;// 百分比系数
            float x = (float) (centerX + Math.cos(mAngle * i) * (mRaduis * p));
            float y = (float) (centerY + Math.sin(mAngle * i) * (mRaduis * p));
            if (i == 0) {
                mShapePath.moveTo(x, centerY);
            } else {
                mShapePath.lineTo(x, y);
            }

            LogUtil.d("(" + x + ", " + y + ")");
        }
        mShapePath.close();

        canvas.drawPath(mShapePath, mRegionPaint);
    }

}
