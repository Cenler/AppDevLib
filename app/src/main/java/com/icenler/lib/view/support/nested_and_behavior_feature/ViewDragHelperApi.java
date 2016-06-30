package com.icenler.lib.view.support.nested_and_behavior_feature;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * ViewDragHelper.shouldInterceptTouchEvent(ev)
 * DOWN:
 * -------> getOrderedChildIndex(findTopChildUnder)
 * -------> onEdgeTouched
 * MOVE:
 * -------> getOrderedChildIndex(findTopChildUnder)
 * -------> getViewHorizontalDragRange &
 * -------> getViewVerticalDragRange(checkTouchSlop)(MOVE中可能不止一次)
 * -------> clampViewPositionHorizontal&
 * -------> clampViewPositionVertical
 * -------> onEdgeDragStarted
 * -------> tryCaptureView
 * -------> onViewCaptured
 * -------> onViewDragStateChanged
 * ***********************************************************
 * ViewDragHelper.processTouchEvent(ev)
 * DOWN:
 * -------> getOrderedChildIndex(findTopChildUnder)
 * -------> tryCaptureView
 * -------> onViewCaptured
 * -------> onViewDragStateChanged
 * -------> onEdgeTouched
 * MOVE:
 * -------> STATE==DRAGGING:dragTo
 * -------> STATE!=DRAGGING:
 * -------> onEdgeDragStarted
 * -------> getOrderedChildIndex(findTopChildUnder)
 * -------> getViewHorizontalDragRange&
 * -------> getViewVerticalDragRange(checkTouchSlop)
 * -------> tryCaptureView
 * -------> onViewCaptured
 * -------> onViewDragStateChanged
 */
public class ViewDragHelperApi extends ViewGroup {

    private ViewDragHelper mDragHelper;

    public ViewDragHelperApi(Context context) {
        super(context);
    }

    public ViewDragHelperApi(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewDragHelperApi(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {
        mDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            /**
             * @param child     子控件
             * @param pointerId 触摸点
             * @return 是否捕获此 View
             */
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return false;
            }

            /**
             * 滑动状态改变回调
             *
             * @param state
             */
            @Override
            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);
            }

            /**
             * 正在操作的控件位置发生变化回调
             *
             * @param changedView
             * @param left
             * @param top
             * @param dx
             * @param dy
             */
            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
            }

            /**
             * 捕获完成回调
             *
             * @param capturedChild
             * @param activePointerId
             */
            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
            }

            /**
             * 滑动事件结束回调, 通常配合 ViewDragHelper.settleCapturedViewAt(
             *                       mAutoBackOriginPos.x, mAutoBackOriginPos.y);
             *                       invalidate(); 实现位置重置
             *
             * @param releasedChild 当前操作的 View
             * @param xvel
             * @param yvel
             */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
            }

            /**
             * 边界按下回调
             *
             * @param edgeFlags
             * @param pointerId
             */
            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
            }

            /**
             * @param edgeFlags 边界标识
             * @return 是否锁定边界
             */
            @Override
            public boolean onEdgeLock(int edgeFlags) {
                return super.onEdgeLock(edgeFlags);
            }

            /**
             * 边界移动操作时回调, 可配合 ViewDragHelper.captureChildView(view, pointerId);
             * 对子控件进行捕获后操作, ViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
             * 可对边界操作进行限定
             *
             * @param edgeFlags 边界操作的方向
             * @param pointerId
             */
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                super.onEdgeDragStarted(edgeFlags, pointerId);
            }

            /**
             * @param index
             * @return 查看源码理解
             */
            @Override
            public int getOrderedChildIndex(int index) {
                return super.getOrderedChildIndex(index);
            }

            /**
             * @param child
             * @return 大于 0 捕获横向滑动事件
             */
            @Override
            public int getViewHorizontalDragRange(View child) {
                return super.getViewHorizontalDragRange(child);
            }

            /**
             * @param child
             * @return 大于 0 捕获纵向滑动事件
             */
            @Override
            public int getViewVerticalDragRange(View child) {
                return super.getViewVerticalDragRange(child);
            }

            /**
             * @param child
             * @param left  即将滑动到左侧的位置
             * @param dx
             * @return 纵向滑动的边界最大 or 最小值
             */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return super.clampViewPositionHorizontal(child, left, dx);
            }

            /**
             * @param child
             * @param top   即将滑动到顶部的位置
             * @param dy
             * @return 横向滑动的边界最大 or 最小值
             */
            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return super.clampViewPositionVertical(child, top, dy);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);

        /** MotionEvent Api */
        // 相对当前控件
        event.getX();
        event.getY();
        // 相对屏幕
        event.getRawX();
        event.getRawY();

        int action = event.getAction();// 可处理多点事件
        int pointerAction = event.getActionMasked();// 只处理单点事件
        int actionIndex = event.getActionIndex();// 触摸点信息
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:

            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
        }


        // ACTION_DOWN 时获取当前坐标点所在边界
        int edgeFlags = event.getEdgeFlags();

        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

}
