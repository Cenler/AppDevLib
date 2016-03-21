package com.icenler.lib.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Method;

/**
 * Created by iCenler - 2015/9/14.
 * Description：其他相关工具类
 */
public class OtherUtil {

    private OtherUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * ScrollView 嵌套 ListView 时，高度计算
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;

        int totalHeight = 0;
        View listItem = null;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 48;// 48 为设置padding所出现的高度
        listView.setLayoutParams(params);
    }

//    TODO 5.0 不兼容
//    public static void convertActivityToTranslucent(Activity activity) {
//        try {
//            Class<?>[] classes = Activity.class.getDeclaredClasses();
//            Class<?> translucentConversionListenerClazz = null;
//            for (Class clazz : classes) {
//                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
//                    translucentConversionListenerClazz = clazz;
//                }
//            }
//            Method method = Activity.class.getDeclaredMethod("convertToTranslucent",
//                    translucentConversionListenerClazz);
//            method.setAccessible(true);
//            method.invoke(activity, new Object[] {
//                    null
//            });
//        } catch (Throwable t) {
//        }
//    }

//    public static void convertActivityFromTranslucent(Activity activity) {
//        try {
//            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
//            method.setAccessible(true);
//            method.invoke(activity);
//        } catch (Throwable t) {
//        }
//    }

    /**
     * Android 实现SwipeBack（右滑退出）效果(兼容 4.0-5.0)
     */
    public static void convertActivityToTranslucent(Activity activity) {
        try {
            Class[] t = Activity.class.getDeclaredClasses();
            Class translucentConversionListenerClazz = null;
            Class[] method = t;
            int len = t.length;

            for (int i = 0; i < len; ++i) {
                Class clazz = method[i];
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                    break;
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Method var8 = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz, ActivityOptions.class);
                var8.setAccessible(true);
                var8.invoke(activity, new Object[]{null, null});
            } else {
                Method var8 = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz);
                var8.setAccessible(true);
                var8.invoke(activity, new Object[]{null});
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 动态修改tab的模式
     *
     * @param tabLayout
     */
    public static void dynamicSetTablayoutMode(TabLayout tabLayout) {
        int tabTotalWidth = 0;
        for (int i = 0; i < tabLayout.getChildCount(); i++) {
            final View view = tabLayout.getChildAt(i);
            view.measure(0, 0);
            tabTotalWidth += view.getMeasuredWidth();
        }
        if (tabTotalWidth <= ScreenUtil.getDisplayWidth()) {
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

}
