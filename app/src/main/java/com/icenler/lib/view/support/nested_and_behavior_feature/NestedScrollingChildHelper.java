package com.icenler.lib.view.support.nested_and_behavior_feature;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewParentCompat;
import android.view.View;
import android.view.ViewParent;

public class NestedScrollingChildHelper {

    /**
     * 嵌套滑动的子 View
     */
    private final View mView;

    /**
     * 支持嵌套滑动的父 View
     */
    private ViewParent mNestedScrollingParent;

    /**
     * 是否支持嵌套滑动
     */
    private boolean mIsNestedScrollingEnabled;

    /**
     * 是否被消费的变量
     */
    private int[] mTempNestedScrollConsumed;

    public NestedScrollingChildHelper(View view) {
        mView = view;
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        if (mIsNestedScrollingEnabled) {
            ViewCompat.stopNestedScroll(mView);
        }
        mIsNestedScrollingEnabled = enabled;
    }

    public boolean isNestedScrollingEnabled() {
        return mIsNestedScrollingEnabled;
    }

    public boolean hasNestedScrollingParent() {
        return mNestedScrollingParent != null;
    }

    /**
     * 开始嵌套滑动
     *
     * @param axes 滑动方向
     * @return 是否有父view 支持嵌套滑动
     */
    public boolean startNestedScroll(int axes) {
        if (hasNestedScrollingParent()) {
            // 如果已经找到了嵌套滑动的父 View
            // Already in progress  
            return true;
        }
        if (isNestedScrollingEnabled()) {
            ViewParent p = mView.getParent();
            View child = mView;
            // 递归向上寻找 支持 嵌套滑动的父View  
            while (p != null) {
                // 这里会调用 父View 的NestedScrollingParent.onStartNestedScroll 方法  
                // 如果父 View 返回 false  则再次向上寻找父 View , 直到找到支持的父 View
                if (ViewParentCompat.onStartNestedScroll(p, child, mView, axes)) {
                    mNestedScrollingParent = p;
                    // 这里回调 父View 的onNestedScrollAccepted 方法 表示开始接收 嵌套滑动  
                    ViewParentCompat.onNestedScrollAccepted(p, child, mView, axes);
                    return true;
                }
                if (p instanceof View) {
                    child = (View) p;
                }
                p = p.getParent();
            }
        }
        // 没有找到 支持嵌套滑动的父View  则返回false  
        return false;
    }

    /**
     * 停止 嵌套滑动, 一般 在 cancel up 事件中 调用
     */
    public void stopNestedScroll() {
        if (mNestedScrollingParent != null) {
            ViewParentCompat.onStopNestedScroll(mNestedScrollingParent, mView);
            mNestedScrollingParent = null;
        }
    }

    /**
     * @param dxConsumed     x 上被消费的距离
     * @param dyConsumed     y 上被消费的距离
     * @param dxUnconsumed   x 上未被消费的距离
     * @param dyUnconsumed   y 上未被消费的距离
     * @param offsetInWindow 子View 位置的移动距离
     * @return
     */
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
                                        int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        if (isNestedScrollingEnabled() && mNestedScrollingParent != null) {
            if (dxConsumed != 0 || dyConsumed != 0 || dxUnconsumed != 0 || dyUnconsumed != 0) {
                int startX = 0;
                int startY = 0;
                if (offsetInWindow != null) {
                    mView.getLocationInWindow(offsetInWindow);
                    startX = offsetInWindow[0];
                    startY = offsetInWindow[1];
                }

                // 父View 回调 onNestedScroll 方法, 该放在 主要会处理  dxUnconsumed dyUnconsumed 数据  
                ViewParentCompat.onNestedScroll(mNestedScrollingParent, mView, dxConsumed,
                        dyConsumed, dxUnconsumed, dyUnconsumed);

                if (offsetInWindow != null) {
                    // 计算 子View的移动距离  
                    mView.getLocationInWindow(offsetInWindow);
                    offsetInWindow[0] -= startX;
                    offsetInWindow[1] -= startY;
                }
                return true;
            } else if (offsetInWindow != null) {
                // No motion, no dispatch. Keep offsetInWindow up to date.  
                offsetInWindow[0] = 0;
                offsetInWindow[1] = 0;
            }
        }
        return false;
    }

    /**
     * consumed[0]  为0 时 表示 x 轴方向上事件 没有被消费
     * 不为0 时 表示 x 轴方向上事件 被消费了, 值表示 被消费的滑动距离
     * consumed[1]  为0 时 表示 y 轴方向上事件 没有被消费
     * 不为0 时 表示 y 轴方向上事件 被消费了, 值表示 被消费的滑动距离
     *
     * @param dx
     * @param dy
     * @param consumed
     * @param offsetInWindow
     * @return
     */
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        if (isNestedScrollingEnabled() && mNestedScrollingParent != null) {
            if (dx != 0 || dy != 0) {
                int startX = 0;
                int startY = 0;
                // 获取 当前View 初始位置  
                if (offsetInWindow != null) {
                    mView.getLocationInWindow(offsetInWindow);
                    startX = offsetInWindow[0];
                    startY = offsetInWindow[1];
                }

                // 初始化是否被消费数据  
                if (consumed == null) {
                    if (mTempNestedScrollConsumed == null) {
                        mTempNestedScrollConsumed = new int[2];
                    }
                    consumed = mTempNestedScrollConsumed;
                }
                consumed[0] = 0;
                consumed[1] = 0;

                // 这里回调 父View 的 onNestedPreScroll 方法,  
                // 父View 或许会处理 相应的滑动事件,  
                // 如果 处理了 则 consumed 会被赋予 相应的值  
                ViewParentCompat.onNestedPreScroll(mNestedScrollingParent, mView, dx, dy, consumed);

                if (offsetInWindow != null) {
                    // 父View 处理了相应的滑动,  很可能导致 子View 的位置的移动  
                    // 这里计算出  父view 消费 滑动事件后,  导致 子View 的移动距离  
                    mView.getLocationInWindow(offsetInWindow);
                    // 这里 子View 的移动距离  
                    offsetInWindow[0] -= startX;
                    offsetInWindow[1] -= startY;
                }
                // 如果  xy 方向 上 有不为0 的表示消费了 则返回true  
                return consumed[0] != 0 || consumed[1] != 0;
            } else if (offsetInWindow != null) {
                offsetInWindow[0] = 0;
                offsetInWindow[1] = 0;
            }
        }
        return false;
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        if (isNestedScrollingEnabled() && mNestedScrollingParent != null) {
            return ViewParentCompat.onNestedFling(mNestedScrollingParent, mView, velocityX,
                    velocityY, consumed);
        }
        return false;
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        if (isNestedScrollingEnabled() && mNestedScrollingParent != null) {
            return ViewParentCompat.onNestedPreFling(mNestedScrollingParent, mView, velocityX,
                    velocityY);
        }
        return false;
    }

    public void onDetachedFromWindow() {
        ViewCompat.stopNestedScroll(mView);
    }

    public void onStopNestedScroll(View child) {
        ViewCompat.stopNestedScroll(mView);
    }
}  