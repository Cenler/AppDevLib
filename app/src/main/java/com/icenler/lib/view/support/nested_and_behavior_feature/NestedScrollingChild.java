package com.icenler.lib.view.support.nested_and_behavior_feature;

public interface NestedScrollingChild {

    /**
     * 设置嵌套滑动是否可用
     *
     * @param enabled
     */
    public void setNestedScrollingEnabled(boolean enabled);

    /**
     * 嵌套滑动是否可用
     *
     * @return
     */
    public boolean isNestedScrollingEnabled();

    /**
     * 开始嵌套滑动,
     *
     * @param axes 表示方向 有一下两种值
     *             ViewCompat.SCROLL_AXIS_HORIZONTAL 横向哈东
     *             ViewCompat.SCROLL_AXIS_VERTICAL 纵向滑动
     */
    public boolean startNestedScroll(int axes);

    /**
     * 停止嵌套滑动
     */
    public void stopNestedScroll();

    /**
     * 是否有父View 支持 嵌套滑动,  会一层层的网上寻找父View
     *
     * @return
     */
    public boolean hasNestedScrollingParent();

    /**
     * 在处理滑动之后调用
     *
     * @param dxConsumed     x轴上 被消费的距离
     * @param dyConsumed     y轴上 被消费的距离
     * @param dxUnconsumed   x轴上 未被消费的距离
     * @param dyUnconsumed   y轴上 未被消费的距离
     * @param offsetInWindow view 的移动距离
     * @return
     */
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
                                        int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow);

    /**
     * 一般在滑动之前调用, 在 onTouch 中计算出滑动距离, 然后调用该方法, 就给支持的嵌套的父 View 处理滑动事件
     *
     * @param dx             x 轴上滑动的距离, 相对于上一次事件, 不是相对于 down事件的 那个距离
     * @param dy             y 轴上滑动的距离
     * @param consumed       一个数组, 可以传一个空的数组, 表示 x 方向 或 y 方向的事件是否有被消费
     * @param offsetInWindow 支持嵌套滑动到父 View 消费滑动事件后导致本 View 的移动距离
     * @return 支持的嵌套的父View 是否处理了滑动事件
     */
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow);

    /**
     * @param velocityX x 轴上的滑动速度
     * @param velocityY y 轴上的滑动速度
     * @param consumed  是否被消费
     * @return
     */
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed);

    /**
     * @param velocityX x 轴上的滑动速度
     * @param velocityY y 轴上的滑动速度
     * @return
     */
    public boolean dispatchNestedPreFling(float velocityX, float velocityY);
}  