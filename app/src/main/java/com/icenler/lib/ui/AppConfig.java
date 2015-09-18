package com.icenler.lib.ui;

import android.graphics.Typeface;

import com.icenler.lib.base.BaseApplication;
import com.icenler.lib.utils.AppUtil;

/**
 * Created by iCenler - 2015/7/17.
 * Description：App 全局配置
 */
public class AppConfig {

    /* SharedPrefrens */
    public static String PREFS_DEVICE_ID = "device_id";
    public static String PREFS_DEVICE_UUID = "device_uuid";

    /* Config Params */
    public static String APP_NAME = AppUtil.getAppVersionName(BaseApplication.getInstance());

    /* Font Typeface */
    public static Typeface FZHHJT;// 方正行黑简体
    public static Typeface MIUI_EN;
    public static Typeface MIUI_CN;

    static {
        // 字体库初始化加载
        // FZHHJT = Typeface.createFromAsset(BaseApplication.getInstance().getAssets(), "fonts/fangzhengjianti.ttf");
        // MIUI_EN = Typeface.createFromAsset(BaseApplication.getInstance().getAssets(), "fonts/Roboto-Regular.ttf");
        // MIUI_CN = Typeface.createFromAsset(BaseApplication.getInstance().getAssets(), "fonts/DroidSansFallback.ttf");
    }

}
