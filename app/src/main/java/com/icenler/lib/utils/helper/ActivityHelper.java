package com.icenler.lib.utils.helper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.icenler.lib.utils.AppUtil;
import com.icenler.lib.utils.manager.ToastManager;

import java.io.File;

/**
 * Created by iCenler - 2015/9/10.
 * Description：常用系统界面跳转：
 */
public class ActivityHelper {

    /**
     * 拨打电话（拨号权限）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNum));
        if (!AppUtil.isActivityContext(context))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 应用评分
     */
    public static void comment(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            ComponentName cn = new ComponentName("com.qihoo.appstore", "com.qihoo.appstore.activities.SearchDistributionActivity");
            intent.setComponent(cn);
            intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            ToastManager.show(context, "您没有安装应用商城");
        }
    }

    /**
     * 内容分享
     */
    public static void doShare(Context context, String subject, String content) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, content);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            ToastManager.show(context, "无法分享内容");
        }
    }

    /**
     * 安装APK文件
     */
    private static void installApk(Context context, String path) {
        File apkfile = new File(path);
        if (!apkfile.exists()) return;

        try {
            // 通过Intent安装APK文件
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception e) {
            ToastManager.show(context, "安装文件不存在");
        }
    }

}
