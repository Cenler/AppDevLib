package com.icenler.lib.feature.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iCenler - 2015/9/9：
 * Description：Activity 管理，需在 BaseActivity 中添加与移出操作
 */
public class ActivityCollector {

    private static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void removeAll() {
        for (Activity a : activities) {
            if (!a.isFinishing()) {
                a.finish();
            }
        }
    }

}
