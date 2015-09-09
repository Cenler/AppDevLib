package com.icenler.lib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

    /**
     * @param bytes
     * @return 返回字节码的十六进制表示形式
     */
    public static String byte2Hex(byte[] bytes) {
        final String HEX = "0123456789ABCDEF";
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            sb.append(HEX.charAt((b >> 4) & 0x0F));
            sb.append(HEX.charAt(b & 0x0F));
        }

        return sb.toString();
    }

    /**
     * @return 应用版本号
     */
    public static int getAppVersion(Context context) {
        PackageInfo info = null;
        int versionCode = 1;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    /**
     * @return 应用版本名称
     */
    public static String getAppVersionName(Context context) {
        PackageInfo info = null;
        String versionName = "1.0.0";
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

}
