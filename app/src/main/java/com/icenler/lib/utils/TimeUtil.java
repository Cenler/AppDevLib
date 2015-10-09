package com.icenler.lib.utils;

import android.os.SystemClock;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by iCenler - 2015/7/14.
 * Description：时间相关工具类
 */
public class TimeUtil {

    private TimeUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * @param serverTime 服务器时间
     * @return 经过服务器时间修正过的系统时间（单位：秒）
     */
    public static long getSystemTimeFixed(long serverTime) {
        long cur = System.currentTimeMillis() / 1000;
        long timeOffset = serverTime - cur;

        return System.currentTimeMillis() / 1000 + timeOffset;
    }

    /**
     * @return 当前时间
     */
    private String getCurrentTimestamp() {
        return new SimpleDateFormat("k:m:s:S a").format(new Date());
    }

    static long mDelayFixedStart;
    static final long MIN_DELAY = 1200L;//加载刷新延迟控制最短时间（）

    /**
     * 延时控制，需要与 doDelayFix 配合使用
     */
    public static void setDelayFix() {
        mDelayFixedStart = System.currentTimeMillis();
    }

    public static void doDelayFix() {
        doDelayFix(MIN_DELAY);
    }

    /**
     * @param minDelayMS 毫秒
     */
    public static void doDelayFix(long minDelayMS) {
        long pass = System.currentTimeMillis() - mDelayFixedStart;
        if (pass >= 0 && pass < minDelayMS) {
            long delayTime = minDelayMS - pass;
            if (delayTime > 90) {
                SystemClock.sleep(delayTime);
            }
        }
    }

}
