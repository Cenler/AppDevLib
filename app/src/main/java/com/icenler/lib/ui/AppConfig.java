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
    public static Typeface MIUI_EN;// MIUI 英文
    public static Typeface MIUI_CN;// MIUI 中文
    public static Typeface FA_ICON;// Font-Awesome 图形字体

    static {
        // 字体库初始化加载
        // AssetManager assets = BaseApplication.getInstance().getAssets();
        // FZHHJT = Typeface.createFromAsset(assets, "fonts/fangzhengjianti.ttf");
        // MIUI_EN = Typeface.createFromAsset(assets, "fonts/Roboto-Regular.ttf");
        // MIUI_CN = Typeface.createFromAsset(assets, "fonts/DroidSansFallback.ttf");
        // FA_ICON = Typeface.createFromAsset(assets, "fonts/fontawesome-webfont.ttf");
    }

}
