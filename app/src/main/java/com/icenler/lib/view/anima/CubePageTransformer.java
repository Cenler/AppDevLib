package com.icenler.lib.view.anima;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by iCenler - 2015/5/4：
 * Description：魔方旋转动画
 */
public class CubePageTransformer implements ViewPager.PageTransformer {

    private static float MIN_SCALE = 0.75f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            ViewHelper.setAlpha(view, 0);
        } else if (position <= 0) { // [-1,0]
            ViewHelper.setPivotX(view, view.getMeasuredWidth());
            ViewHelper.setPivotY(view, view.getMeasuredHeight() * 0.5f);
            ViewHelper.setRotationY(view, 90f * position);
        } else if (position <= 1) { // (0,1]
            ViewHelper.setPivotX(view, 0);
            ViewHelper.setPivotY(view, view.getMeasuredHeight() * 0.5f);
            ViewHelper.setRotationY(view, 90f * position);
        } else { // (1,+Infinity]
            ViewHelper.setAlpha(view, 0);
        }
    }

}
