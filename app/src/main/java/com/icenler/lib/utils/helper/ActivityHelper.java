package com.icenler.lib.utils.helper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.icenler.lib.utils.AppUtil;
import com.icenler.lib.utils.manager.ToastManager;

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
            if (!AppUtil.isActivityContext(context))
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            ToastManager.show(context, "您没有安装应用商城");
        }
    }

}
