package com.icenler.lib.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.icenler.lib.R;
import com.icenler.lib.ui.base.BaseActivity;
import com.icenler.lib.ui.base.BaseApplication;
import com.icenler.lib.utils.LogUtil;
import com.icenler.lib.utils.ScreenUtil;
import com.icenler.lib.utils.manager.ToastManager;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.view.ViewClickEvent;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
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
        super.setContentView(R.layout.activity_test);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ToastManager.show(getBaseContext(), response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastManager.show(getBaseContext(), error.getMessage());
            }
        });
        stringRequest.setTag("getString_Tag");
        BaseApplication.getHttpQueues().add(stringRequest);

        JSONObject params = null;// 携带请求参数
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ToastManager.show(getBaseContext(), response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastManager.show(getBaseContext(), error.getMessage());
            }
        });
        jsonRequest.setTag("getJson_Tag");
        BaseApplication.getHttpQueues().add(jsonRequest);
    }

    private void volleyPost() {
        String url = "www.baidu.com";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ToastManager.show(getBaseContext(), response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastManager.show(getBaseContext(), error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap map = new HashMap();
                return map;
            }
        };
        stringRequest.setTag("postString_Tag");
        BaseApplication.getHttpQueues().add(stringRequest);

        Map map = new HashMap();
        JSONObject params = new JSONObject(map);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ToastManager.show(getBaseContext(), response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastManager.show(getBaseContext(), error.getMessage());
            }
        });
        jsonRequest.setTag("postString_Tag");
        BaseApplication.getHttpQueues().add(jsonRequest);
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
