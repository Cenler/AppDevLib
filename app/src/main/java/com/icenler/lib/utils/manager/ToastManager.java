package com.icenler.lib.utils.manager;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.icenler.lib.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by iCenler - 2015/7/14.
 * Description：自定义全局 Toast
 * 1、可指定时间显示或常显（通过 cancle() 取消）
 */
public class ToastManager {

    private static final int DEF_NO_ICON = 0;
    private static final int DEF_FAILED_ICON = DEF_NO_ICON;
    private static final int DEF_SUCCESS_ICON = DEF_NO_ICON;

    private static boolean isShowing = false;
    private static Toast mToast;

    private ToastManager() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 常规 Toast
     *
     * @param context
     * @param msg
     */
    public static void show(Context context, String msg) {
        show(context, DEF_NO_ICON, msg);
    }

    public static void show(Context context, int strRes) {
        show(context, context.getString(strRes));
    }

    /**
     * 附带 Icon Toast
     *
     * @param context
     * @param isError
     * @param msg
     */
    public static void show(Context context, boolean isError, String msg) {
        show(context, isError ? DEF_FAILED_ICON : DEF_SUCCESS_ICON, msg);
    }

    public static void show(Context context, boolean isError, int strRes) {
        show(context, isError, context.getString(strRes));
    }

    /**
     * 指定图品资源显示 Toast
     *
     * @param context
     * @param iconRes
     * @param msg
     */
    public static void show(Context context, int iconRes, String msg) {
        Toast toast = new Toast(context);
        toast.setView(getToastView(context, iconRes, msg));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void show(Context context, int iconRes, int strRes) {
        show(context, iconRes, strRes);
    }

    /**
     * @param context
     * @param iconRes
     * @param msg
     * @return 自定义 Toast 布局样式
     */
    private static View getToastView(Context context, int iconRes, String msg) {
        View view = View.inflate(context, R.layout.toast_layout, null);
        ((TextView) view.findViewById(R.id.toast_message)).setText(msg);

        if (iconRes != DEF_NO_ICON) {
            ((ImageView) view.findViewById(R.id.toast_icon)).setImageResource(iconRes);
        } else {
            view.findViewById(R.id.toast_icon).setVisibility(View.GONE);
        }

        return view;
    }

    /**
     * 常显 Toast，可通过 cancel() 取消显示
     *
     * @param context
     * @param message
     */
    public static void showAlways(Context context, String message) {
        if (isShow()) mToast.cancel();
        Field mTNField = null;
        try {
            mTNField = Toast.class.getDeclaredField("mTN");
            mTNField.setAccessible(true);
//            Class clazz = Class.forName("android.widget.Toast");
//            Constructor constructor = clazz.getConstructor(Context.class);
//            constructor.newInstance(context);
            Object mTN = mTNField.get(mToast = new Toast(context));
            try {
                // android 4.0以上系统要设置mT的mNextView属性
                Field mNextViewField = mTN.getClass().getDeclaredField("mNextView");
                mNextViewField.setAccessible(true);
                mNextViewField.set(mTN, getToastView(context, DEF_NO_ICON, message));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            Method showMethod = mTN.getClass().getDeclaredMethod("show", new Class[]{});
            showMethod.setAccessible(true);
            showMethod.invoke(mTN, new Object[]{});
            isShowing = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAlways(Context context, int strRes) {
        showAlways(context, strRes);
    }

    /**
     * 指定显示时间 Toast
     *
     * @param context
     * @param message
     * @param delayMillis
     */
    public static void show(Context context, String message, long delayMillis) {
        showAlways(context, message);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cancel();
            }
        }, delayMillis);
    }

    public static void show(Context context, int strRes, long delayMillis) {
        show(context, strRes, delayMillis);
    }

    public static void cancel() {
        if (mToast != null && isShow()) {
            mToast.cancel();
            isShowing = false;
        }
    }

    /**
     * @return 当前 Toast 是否显示，限于常显和指定时间模式
     */
    public static boolean isShow() {
        return isShowing;
    }

}
