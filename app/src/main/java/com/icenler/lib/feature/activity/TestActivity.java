package com.icenler.lib.feature.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.icenler.lib.R;
import com.icenler.lib.common.RequestCallback;
import com.icenler.lib.common.VolleyRequest;
import com.icenler.lib.feature.base.BaseActivity;
import com.icenler.lib.utils.LogUtil;
import com.icenler.lib.utils.ScreenUtil;
import com.icenler.lib.utils.manager.ToastManager;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.view.ViewClickEvent;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TestActivity extends BaseActivity {

    private ImageView iv;
    private TextView text;
    private AnimatedVectorDrawable searchToBar;
    private AnimatedVectorDrawable barToSearch;
    private float offset;
    private Interpolator interp;
    private int duration;
    private boolean expanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        iv = (ImageView) findViewById(R.id.search);
        text = (TextView) findViewById(R.id.text);
        searchToBar = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.svg_anim_search_to_bar);
        barToSearch = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.svg_anim_bar_to_search);
        interp = AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in);
        duration = 400;
        // iv is sized to hold the search+bar so when only showing the search icon, translate the
        // whole view left by half the difference to keep it centered
        offset = -71f * (int) getResources().getDisplayMetrics().scaledDensity;
        iv.setTranslationX(offset);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void animate(View view) {
        if (!expanded) {
            iv.setImageDrawable(searchToBar);
            searchToBar.start();
            iv.animate().translationX(0f).setDuration(duration).setInterpolator(interp);
            text.animate().alpha(1f).setStartDelay(duration - 100).setDuration(100).setInterpolator(interp);
        } else {
            iv.setImageDrawable(barToSearch);
            barToSearch.start();
            iv.animate().translationX(offset).setDuration(duration).setInterpolator(interp);
            text.setAlpha(0f);
        }
        expanded = !expanded;
    }

    private void init() {
        // AndroidRx 再探
        File[] folders = new File[5];
        Observable.from(folders).flatMap(new Func1<File, Observable<File>>() {
            @Override
            public Observable<File> call(File file) {
                return Observable.from(file.listFiles());
            }
        }).filter(new Func1<File, Boolean>() {
            @Override
            public Boolean call(File file) {
                return file.getName().endsWith(".png");
            }
        }).map(new Func1<File, Bitmap>() {
            @Override
            public Bitmap call(File file) {
                return getBitmapFromFile(file);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        // 主线程处理
                    }
                });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        };

        // Observer 扩展类 Subscriber
        // 区别 onStart()
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String s) {
            }
        };
        subscriber.isUnsubscribed();// 是否取消订阅
        subscriber.unsubscribe();// 取消订阅

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onStart();
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onCompleted();
            }
        });
        observable.subscribe(observer);// 订阅
        observable.subscribe(subscriber);// 同上，类似监听点击事件

        // 应用一：
        Observable.just(0, 1, 2, 3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LogUtil.d(String.valueOf(integer));
                    }
                });

        // 应用二：
        Observable.just(0).map(new Func1<Integer, Drawable>() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public Drawable call(Integer integer) {
                return getTheme().getDrawable(integer);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastManager.show(getBaseContext(), e.getMessage());
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        // TODO 显示内容
                    }
                });


        RxView.clickEvents(null).throttleFirst(500, TimeUnit.MILLISECONDS)// 防抖动
                .subscribe(new Action1<ViewClickEvent>() {
                    @Override
                    public void call(ViewClickEvent viewClickEvent) {

                    }
                });
    }

    private Bitmap getBitmapFromFile(File file) {
        return null;
    }


    private void volleyGet() {
        String url = "www.baidu.com";
        VolleyRequest.reqGet(url, "getTest", new RequestCallback(getBaseContext()) {
            @Override
            public void onSuccess(String result) {
            }

            @Override
            public void onError(VolleyError error) {
            }
        });
    }

    private void volleyPost() {
        String url = "www.baidu.com";
        Map<String, String> params = null;
        VolleyRequest.reqPost(url, "postTest", params, new RequestCallback(getBaseContext()) {
            @Override
            public void onSuccess(String result) {
            }

            @Override
            public void onError(VolleyError error) {
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private class LoadingView extends View {

        private static final int DEF_DURATION = 1000;

        //paint
        private Paint mCirclePaint;
        private Paint mAccBallPaint;
        private RectF rectF;

        private int mBigCircleColor = getResources().getColor(R.color.color_green_highlight);
        private int mAccBallColor = getResources().getColor(R.color.color_orange_assist);

        private int mBigCircleStroke = ScreenUtil.dp2px(1);
        private int mDuration = DEF_DURATION;

        private float mBitRadius = ScreenUtil.dp2px(50);
        private float mSmallRadius = ScreenUtil.dp2px(10);
        private float startAngle = 0.0f;

        public LoadingView(Context context) {
            this(context, null);
        }

        public LoadingView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initView();
        }

        private void initView() {
            // 圆环
            mCirclePaint = new Paint();
            mCirclePaint.setAntiAlias(true);
            mCirclePaint.setColor(mBigCircleColor);
            mCirclePaint.setStrokeWidth(mBigCircleStroke);
            mCirclePaint.setStyle(Paint.Style.STROKE);

            // 小球
            mAccBallPaint = new Paint();
            mAccBallPaint.setAntiAlias(true);
            mAccBallPaint.setColor(mAccBallColor);
            mAccBallPaint.setStyle(Paint.Style.FILL);

            rectF = new RectF(0, 0, ScreenUtil.getDisplayWidth() >> 1, ScreenUtil.getDisplayWidth() >> 1);
            startRotate(2000);
        }

        private void startRotate(long duration){
            LinearAnimation animation = new LinearAnimation();
            animation.setDuration(duration);
            animation.setRepeatCount(Animation.INFINITE);
            animation.setInterpolator(new LinearInterpolator());
            animation.setLinearAnimationListener(new LinearAnimation.LinearAnimationListener() {
                @Override
                public void applyTans(float interpolatedTime) {
                    startAngle = 360 * interpolatedTime;
                    invalidate();
                }
            });
            startAnimation(animation);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        protected void onDraw(Canvas canvas) {
//            drawSeekbar(canvas, 100);
            drawSlowIndicator(startAngle, canvas);
        }

        private void drawSlowIndicator(float startAngle, Canvas canvas){
            Paint circlePaint = new Paint();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.parseColor("#A8D7A7"));
            circlePaint.setStrokeWidth(7);
            circlePaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(getArcPath(), circlePaint);

            int restoreCount = canvas.save();
            canvas.translate(rectF.centerX(), rectF.centerY());
            circlePaint.setStyle(Paint.Style.FILL);
            canvas.drawPath(getBallPath(startAngle + 90), circlePaint);
            canvas.drawPath(getBallPath(startAngle + 90 + 30 + 90), circlePaint);
            canvas.drawPath(getBallPath(startAngle + 90 + 30 + 90 + 30 + 90), circlePaint);
            canvas.restoreToCount(restoreCount);
        }

        private Path getArcPath() {
            Path path = new Path();
            path.addArc(rectF, startAngle, 90);
            path.addArc(rectF, startAngle + 90 + 30, 90);
            path.addArc(rectF, startAngle + 90 + 90 + 30 + 30, 90);
            return path;
        }


        private void drawSeekbar(Canvas canvas, float startAngle) {
            Paint circlePaint = new Paint();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.parseColor("#FF4444"));
            circlePaint.setStrokeWidth(7);
            circlePaint.setStyle(Paint.Style.STROKE);

            Path path = new Path();
            path.addArc(rectF, 0, startAngle);
            canvas.drawPath(path, circlePaint);

            int restoreCount = canvas.save();

            canvas.translate(rectF.centerX(), rectF.centerY());
            circlePaint.setStyle(Paint.Style.FILL);
            canvas.drawPath(getBallPath(startAngle), circlePaint);

            canvas.restoreToCount(restoreCount);
        }

        private Path getBallPath(float startAngle) {
            double sweepAngle = Math.PI / 180 * startAngle;
            Path path = new Path();
            float x = (float) Math.cos(sweepAngle) * (rectF.width() / 2);
            float y = (float) Math.sin(sweepAngle) * (rectF.width() / 2);
            path.moveTo(x, y);
            path.addCircle(x, y, 10, Path.Direction.CCW);
            return path;
        }
    }

    private static class LinearAnimation extends Animation {
        private LinearAnimationListener mListener = null;

        interface LinearAnimationListener {
            void applyTans(float interpolatedTime);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (mListener != null) {
                mListener.applyTans(interpolatedTime);
            }
        }

        public void setLinearAnimationListener(LinearAnimationListener listener){
            mListener = listener;
        }
    }

}

/**
 * >>> Shader 之图形渲染
 *     - BitmapShader:      图像渲染
 *     - LinearGradient:    线性渐变
 *     - RadialGradient:    环形渐变
 *     - SweepGradient:     扇形渐变
 *     - ComposeShader:     混合渲染，适用于组合操作
 *     使用： mPaint.setShader(XXXShader)
 *
 *     Example：配合 Matrix 实现扇形动态渐变
 *
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(8);
            paint.setStyle(Paint.Style.STROKE);

            int[] f = {Color.parseColor("#00A8D7A7"), Color.parseColor("#ffA8D7A7")};
            float[] p = {.0f, 1.0f};

            SweepGradient sweepGradient = new SweepGradient(rectF.centerX(), rectF.centerX(), f, p);
            Matrix matrix = new Matrix();
            sweepGradient.getLocalMatrix(matrix);
            matrix.postRotate(startAngle, rectF.centerX(), rectF.centerY());
            sweepGradient.setLocalMatrix(matrix);
            paint.setShader(sweepGradient);

            canvas.drawArc(rectF,0, 360, true, paint);
 * */
