package com.icenler.lib.utils;

import android.text.TextUtils;

/**
 * Created by iCenler - 2015/7/14.
 * Description：Android 开发常用操作工具类
 */
public class AppUtil {

    private AppUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * @param obj
     * @return 对象是否为 null
     */
    public static boolean isNull(Object obj) {
        if (null == obj) {
            return true;
        } else if (obj instanceof String) {
            return TextUtils.isEmpty((String) obj);
        } else {
            return false;
        }
    }

    /**
     * @param object
     * @return 对象是否非 null
     */
    public static boolean notNull(Object object) {
        return !isNull(object);
    }

}
