package com.icenler.lib.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * Created by iCenler - 2015/5/14.
 * Description：回弹效果 ScrollView
 */
public class BounceScrollView extends ScrollView {

    private static final int TRANS_ANIMA_DURATION = 200;// 还原执行动画时间

    private View mChildView;
    private Rect mRect = new Rect();
    private TranslateAnimation tranAnima;
    private BounceScrollView.Callback mCallback;

    private boolean isFirst = true;
    private boolean isCallback;
    private float currentY;

    public interface Callback {
        void callback();// 下拉回弹距离超过高度一半执行
    }

    public void setCallback(BounceScrollView.Callback callback) {
        this.mCallback = callback;
    }

    public BounceScrollView(Context context) {
        super(context);
    }

    public BounceScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BounceScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0)
            mChildView = getChildAt(0);
        super.onFinishInflate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mChildView != null)
            handleTouchEvent(ev);

        return super.onTouchEvent(ev);
    }

    private void handleTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 无法捕获按下坐标
                break;
            case MotionEvent.ACTION_MOVE:
                float downY = currentY;
                float moveY = ev.getY();
                int deltaY = (int) (moveY - downY);
                if (isFirst) {
                    isFirst = false;
                    deltaY = 0;
                }
                currentY = moveY;

                if (isNeedMove()) {
                    if (mRect.isEmpty())
                        mRect.set(mChildView.getLeft(), mChildView.getTop(), mChildView.getRight(), mChildView.getBottom());

                    // 移动布局：转换率为 2/3
                    mChildView.layout(
                            mChildView.getLeft(),
                            mChildView.getTop() + deltaY * 2 / 3,
                            mChildView.getRight(),
                            mChildView.getBottom() + deltaY * 2 / 3);

                    if (mCallback != null && isDoCallback(deltaY) && !isCallback) {
                        isCallback = true;
                        restPosition();
                        mCallback.callback();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!mRect.isEmpty())
                    restPosition();
                break;
        }
    }

    /**
     * 是否需要移动布局
     * mChildView.getMeasuredHeight()：  获取的是控件的总高度
     * this.getHeight()：                获取的是屏幕的高度
     * @return
     */
    private boolean isNeedMove() {
        int offset = mChildView.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        // 顶部 与 底部判断
        if (scrollY == 0 || scrollY == offset)
            return true;
        return false;
    }

    /**
     *  重置到初始状态
     */
    private void restPosition() {
        tranAnima = new TranslateAnimation(0, 0, mChildView.getTop(), mRect.top);
        tranAnima.setDuration(TRANS_ANIMA_DURATION);
        tranAnima.setFillAfter(true);
        mChildView.startAnimation(tranAnima);
        mChildView.layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
        mRect.setEmpty();
        isFirst = true;
        isCallback = false;
    }

    /**
     * 是否执行回调
     */
    private boolean isDoCallback(int deltaY) {
        if (deltaY > 0 && mChildView.getTop() > getHeight() / 2)
            return true;

        return false;
    }

}
