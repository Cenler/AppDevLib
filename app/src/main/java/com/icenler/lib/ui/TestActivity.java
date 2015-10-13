package com.icenler.lib.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.icenler.lib.R;
import com.icenler.lib.base.BaseActivity;
import com.icenler.lib.utils.ScreenUtil;
import com.icenler.lib.view.SoGouBrowserLoading;

public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(new SoGouBrowserLoading(this));
    }

    private class CanvasView extends View {

        private Paint mPaint = new Paint();

        public CanvasView(Context context) {
            super(context);
        }

        public CanvasView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(getResources().getColor(R.color.color_green_highlight));
            mPaint.setStrokeWidth(ScreenUtil.dp2px(3));
            canvas.translate(canvas.getWidth() / 2, 960);// 设置坐标
            canvas.drawCircle(0, 0, 300, mPaint);

            canvas.save();
            canvas.translate(-250, -250);
            Path path = new Path();
            path.addArc(new RectF(0, 0, 500, 500), -180, 180);
            Paint citePaint = new Paint(mPaint);
            citePaint.setTextSize(ScreenUtil.dp2px(16));
            citePaint.setStrokeWidth(1);
            citePaint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawTextOnPath("http://www.google.com", path, 150, 0, citePaint);
            canvas.restore();

            Paint tmpPaint = new Paint(citePaint); //小刻度画笔对象
            float y = 300f;
            int count = 60;
            for (int i = 0; i < count; i++) {
                if (i % 5 == 0) {
                    canvas.drawLine(0, y, 3, y + 24f, mPaint);
                    canvas.drawText(String.valueOf(i / 5 + 1), -12f, y + 66f, tmpPaint);
                } else {
                    canvas.drawLine(0, y, 0, y + 15f, tmpPaint);
                }

                canvas.rotate(360 / count, 0, 0);
            }

            tmpPaint.setColor(getResources().getColor(R.color.color_grey));
            tmpPaint.setStrokeWidth(4);
            canvas.drawCircle(0, 0, 21, tmpPaint);
            tmpPaint.setStyle(Paint.Style.FILL);
            tmpPaint.setColor(getResources().getColor(R.color.color_green_highlight));
            canvas.drawCircle(0, 0, 12, tmpPaint);
            canvas.drawLine(0, 30, 0, -200, mPaint);
        }
    }

}
