package com.icenler.lib.view.anima;

import android.app.Activity;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Layout Transition 动画演示
 */
public class Part4TransitionController extends TransitionController {

    // 定义需要变换的控件应用
    private static final int[] VIEW_IDS = {};

    Part4TransitionController(WeakReference<Activity> activityWeakReference, AnimatorBuilder animatorBuilder) {
        super(activityWeakReference, animatorBuilder);
    }

    public static TransitionController newInstance(Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        AnimatorBuilder animatorBuilder = AnimatorBuilder.newInstance(activity);
        return new Part4TransitionController(activityWeakReference, animatorBuilder);
    }

    @Override
    protected void enterInputMode(Activity activity) {
        createTransitionAnimator(activity);
        activity.setContentView(0);
    }

    @Override
    protected void exitInputMode(Activity activity) {
        createTransitionAnimator(activity);
        activity.setContentView(0);
    }

    private void createTransitionAnimator(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        TransitionAnimator.begin(parent, VIEW_IDS);
    }
    
}
