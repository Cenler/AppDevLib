package com.icenler.lib.ui;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.android.volley.VolleyError;
import com.icenler.lib.R;
import com.icenler.lib.common.RequestCallback;
import com.icenler.lib.common.VolleyRequest;
import com.icenler.lib.ui.base.BaseActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(new LoadingView(this));
        ButterKnife.bind(this);
        // init();
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

    private class LoadingView extends View {

        int bigRadius, smallRadius;
        int centerX, centerY;
        float angle = 0;

        Paint mCirclePaint;
        Paint mBallPaint;

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
            centerX = ScreenUtil.getDisplayWidth() >> 1;
            centerY = ScreenUtil.getDisplayHeight() >> 1;
            bigRadius = centerX / 5;
            smallRadius = bigRadius / 5;

            mCirclePaint = new Paint();
            mCirclePaint.setAntiAlias(true);
            mCirclePaint.setDither(true);
            mCirclePaint.setStrokeWidth(3f);
            mCirclePaint.setStyle(Paint.Style.STROKE);
            mCirclePaint.setColor(getResources().getColor(R.color.color_red_assist));

            mBallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBallPaint.setStyle(Paint.Style.FILL);
            mBallPaint.setColor(getResources().getColor(R.color.color_green_highlight));

            AngleTypeEvaluator evaluator = new AngleTypeEvaluator();
            ValueAnimator animator = ValueAnimator.ofObject(evaluator, 0f, 360f);
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(2000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    angle = Float.valueOf((Float) animation.getAnimatedValue());
                    invalidate();
                }
            });
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.start();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawCircle(centerX, centerY, bigRadius, mCirclePaint);

            final double sweepAngle = Math.PI / 180 * angle;
            float x = (float) (Math.sin(sweepAngle) * bigRadius);
            float y = (float) (Math.cos(sweepAngle) * bigRadius);

            int restoreToCount = canvas.save();

            canvas.translate(centerX, centerY);
            canvas.drawCircle(x, y, smallRadius, mBallPaint);

            canvas.restoreToCount(restoreToCount);
        }
    }

    private class AngleTypeEvaluator implements TypeEvaluator<Float> {
        @Override
        public Float evaluate(float fraction, Float startValue, Float endValue) {
            return fraction * (endValue - startValue);
        }
    }

}
