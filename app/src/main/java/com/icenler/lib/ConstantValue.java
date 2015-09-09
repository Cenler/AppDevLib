package com.icenler.lib;

import android.graphics.Typeface;

public class ConstantValue {

    public static Typeface FZHHJT;// 方正行黑简体
    public static Typeface MIUI_EN;
    public static Typeface MIUI_CN;

    static {
        // 字体库初始化加载
        // FZHHJT = Typeface.createFromAsset(BaseApplication.getInstance().getAssets(), "fonts/fangzhengjianti.ttf");
        // MIUI_EN = Typeface.createFromAsset(BaseApplication.getInstance().getAssets(), "fonts/Roboto-Regular.ttf");
        // MIUI_CN = Typeface.createFromAsset(BaseApplication.getInstance().getAssets(), "fonts/DroidSansFallback.ttf");
    }

    /**
     *  默认字符编码
     */
    public static final String CHARSET_ENCODING = "UTF-8";

    /**
     *  手机号类型：移动0、联通1、电信2
     */
    public static int MOBILES_TYPE = -1;

}
