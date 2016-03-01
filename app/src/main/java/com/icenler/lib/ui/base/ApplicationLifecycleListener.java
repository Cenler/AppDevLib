package com.icenler.lib.ui.base;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.icenler.lib.utils.LogUtil;

/**
 * Created by iCenler on 2016/1/28.
 * Description: 全局生命周期监听
 */
public class ApplicationLifecycleListener implements Application.ActivityLifecycleCallbacks {

    /**
     * 后台切换广播
     */
    public static final String ACTION_BACKGROUND_CHANGED = "app.intent.action.BACKGROUND_CHANGED";

    /**
     * 前台切换广播
     */
    public static final String ACTION_FOREGROUND_CHANGED = "app.intent.action.FOREGROUND_CHANGED";


    /**
     * 可见 Activity 数量，用于识别前后台切换
     */
    private int activeCount = 0;

    /**
     * 应用是否处于后台
     */
    private boolean isBackground = true;

    /**
     * @return 应用前后台状态
     */
    public boolean isBackground() {
        return isBackground;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LogUtil.i("onActivityCreated: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        LogUtil.i("onActivityStarted: " + activity.getClass().getSimpleName());

        activeCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        LogUtil.i("onActivityResumed: " + activity.getClass().getSimpleName());

        if (isBackground) {
            isBackground = false;
            foreground(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        LogUtil.i("onActivityPaused: " + activity.getClass().getSimpleName());

    }

    @Override
    public void onActivityStopped(Activity activity) {
        LogUtil.i("onActivityStopped: " + activity.getClass().getSimpleName());

        activeCount--;
        if (activeCount == 0) {
            isBackground = true;
            background(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        LogUtil.i("onActivitySaveInstanceState: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        LogUtil.i("onActivityDestroyed: " + activity.getClass().getSimpleName());
    }

    /**
     * 切换后台调用
     */
    private void background(Activity activity) {
        LogUtil.i("The background to switch");
        LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent(ACTION_BACKGROUND_CHANGED));
    }

    /**
     * 切换前台调用
     */
    private void foreground(Activity activity) {
        LogUtil.i("The foreground to switch");
        LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent(ACTION_FOREGROUND_CHANGED));
    }

}
