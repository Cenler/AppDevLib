package com.icenler.lib.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Cenler - 2015/3/26.
 * Description：屏蔽 ViewPager 事件，通过setPagingEnabled设置是否可用
 */
public class NoSwipeViewPager extends ViewPager {

    private boolean mEnabled = false;

    public NoSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mEnabled ? super.onTouchEvent(ev) : false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mEnabled ? super.onInterceptTouchEvent(ev) : false;
    }

    /**
     * 设置控件是否可滑动
     */
    public void setPagingEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

}
