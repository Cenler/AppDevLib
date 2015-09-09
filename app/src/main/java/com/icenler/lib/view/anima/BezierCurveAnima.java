package com.icenler.lib.view.anima;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by iCenler - 2015/5/24.
 * Description：贝塞尔曲线动画
 */
public class BezierCurveAnima {

    /**
     * 根据三个点的坐标以及目标控件开始执行动画
     *
     * @param targetView
     * @param startPoint
     * @param controlPoint
     * @param endPoint
     */
    public void startBezierAnimation(final ImageView targetView, PointF startPoint, PointF controlPoint, PointF endPoint) {
        AnimatorSet animSet = new AnimatorSet();
        // 设置动画的估值器、并根据开始坐标和结束坐标进行特定轨迹运动
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new BezierEvaluator(controlPoint), startPoint, endPoint);
        valueAnimator.setRepeatCount(0);
        valueAnimator.setTarget(targetView);
        valueAnimator.setInterpolator(new LinearInterpolator());// 填值器（作用：匀速运动）
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // 获取计算后的参考坐标
                PointF pointF = (PointF) valueAnimator.getAnimatedValue();
                targetView.setX(pointF.x);
                targetView.setY(pointF.y);
            }
        });

        // 小球缩放
        ObjectAnimator animScaleX = ObjectAnimator.ofFloat(targetView, "scaleX", 1.0f, 0.8f, 0.3f);
        ObjectAnimator animScaleY = ObjectAnimator.ofFloat(targetView, "scaleY", 1.0f, 0.8f, 0.3f);

        animSet.playTogether(valueAnimator, animScaleX, animScaleY);
        animSet.setDuration(1000);
        animSet.start();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ((ViewGroup) targetView.getParent()).removeView(targetView);// 删除动画控件
            }
        });
    }

    /**
     * 二次贝塞尔曲线估值器（根据时间来计算运行轨迹）
     */
    class BezierEvaluator implements TypeEvaluator<PointF> {

        private float controlX, controlY;

        public BezierEvaluator(PointF controlPoint) {
            this.controlX = controlPoint.x;
            this.controlY = controlPoint.y;
        }

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            final float t = fraction;
            float oneMinusT = 1.0f - t;
            PointF point = new PointF();// 参考坐标

            PointF point0 = startValue;// 起始点

            PointF point1 = new PointF();// 控制点
            point1.set(controlX, controlY);

            PointF point2 = endValue;// 结束点

            // 二次曲线算法：B(t) = (1 - t)^2 * P0 + 2t(1 - t) * P1 + t^2 * P2  （t[0,1]）
            // 三次曲线算法：B(t) = (1 - t)^3 * P0 + 3t(1 - t)^2 * P1 +　3t^2 * (1 - t) * P2 + t^3 * P3
            // 高阶曲线算法：查阅资料
            point.x = oneMinusT * oneMinusT * (point0.x)
                    + 2 * oneMinusT * t * (point1.x)
                    + t * t * (point2.x);

            point.y = oneMinusT * oneMinusT * (point0.y)
                    + 2 * oneMinusT * t * (point1.y)
                    + t * t * (point2.y);

            return point;
        }
    }

}
