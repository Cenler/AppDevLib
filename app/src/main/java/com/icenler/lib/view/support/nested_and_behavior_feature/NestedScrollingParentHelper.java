package com.icenler.lib.view.support.nested_and_behavior_feature;

import android.view.View;
import android.view.ViewGroup;

public class NestedScrollingParentHelper {

    /**
     * NestedScrollingParent
     */
    private final ViewGroup mViewGroup;

    /**
     * 滑动方向
     *
     * @see ViewCompat#SCROLL_AXIS_HORIZONTAL
     * @see ViewCompat#SCROLL_AXIS_VERTICAL
     * @see ViewCompat#SCROLL_AXIS_NONE
     */
    private int mNestedScrollAxes;

    public NestedScrollingParentHelper(ViewGroup viewGroup) {
        mViewGroup = viewGroup;
    }

    public void onNestedScrollAccepted(View child, View target, int axes) {
        mNestedScrollAxes = axes;
    }

    public int getNestedScrollAxes() {
        return mNestedScrollAxes;
    }

    public void onStopNestedScroll(View target) {
        mNestedScrollAxes = 0;
    }

}