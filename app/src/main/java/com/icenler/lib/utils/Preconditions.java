package com.icenler.lib.utils;

import android.support.annotation.NonNull;

/**
 * Created by iCenler - 2016/4/28.
 * Description：Guava 相关工具类提取
 */
public class Preconditions {

    private Preconditions() {
        throw new UnsupportedOperationException("can not be instantiation");
    }

    public static <T> T checkNotNull(@NonNull final T object) {
        if (object == null)
            throw new NullPointerException();
        return object;
    }

    public static <T> T checkNotNull(@NonNull final T object, String message) {
        if (object == null)
            throw new NullPointerException(message);
        return object;
    }

}
