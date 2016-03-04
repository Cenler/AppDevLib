package com.icenler.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.icenler.lib.utils.LogUtil;

/**
 * Created by Fangde on 2016/3/4.
 * Description:
 */
public class TranslationView extends RelativeLayout {

    private float yFraction = 0;
    private float xFraction = 0;

    private ViewTreeObserver.OnPreDrawListener preDrawListener;

    public TranslationView(Context context) {
        super(context);
    }

    public TranslationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TranslationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setYFraction(float fraction) {
        this.yFraction = fraction;
        if (getHeight() == 0) {
            if (preDrawListener == null) {
                preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
                        setYFraction(yFraction);
                        LogUtil.d("setXFraction: " + xFraction);
                        return true;
                    }
                };
                getViewTreeObserver().addOnPreDrawListener(preDrawListener);
            }
            return;
        }
        float translationY = getHeight() * fraction;
        setTranslationY(translationY);
    }

    public void setXFraction(float fraction) {
        this.xFraction = fraction;
        if (getWidth() == 0) {
            if (preDrawListener == null) {
                preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
                        setXFraction(xFraction);
                        LogUtil.d("setXFraction: " + xFraction);
                        return true;
                    }
                };
                getViewTreeObserver().addOnPreDrawListener(preDrawListener);
            }
            return;
        }
        float translationX = getWidth() * fraction;
        setTranslationX(translationX);
    }


    public void setZoomSlideHorizontal(float fraction) {
        setTranslationX(getWidth() * fraction);
        setPivotX(getWidth() / 2);
        setPivotY(getHeight() / 2);
    }

    public void setZoomSlideVertical(float fraction) {
        setTranslationY(getHeight() * fraction);
        setPivotX(getWidth() / 2);
        setPivotY(getHeight() / 2);
    }


    OnInterceptTouchEventListener listener;

    public void setDispatchTouchEventListener(OnInterceptTouchEventListener listener) {
        this.listener = listener;
    }

    public interface OnInterceptTouchEventListener {
        boolean onInterceptTouchEvent(MotionEvent ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (listener != null)
            return listener.onInterceptTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }

}
