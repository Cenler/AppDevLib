package com.icenler.lib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.icenler.lib.utils.LogUtil;


/**
 * Created by iCenler on 2016/1/28.
 * Description: 前台切换广播监听
 */
public class ForegroundChangedReceiver extends BroadcastReceiver {

    /**
     * @param context - 使用本地广播是 Context == Application
     *                - 注册广播时 Context == register所用Context
     *                - 静态注册 Context == ReceiverRestrictedContext（备注：android:process=":remote" 可实现跨进程弹窗）
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.i("ReceiverAction: " + intent.getAction());
        // 弹窗实现 dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST) or DialogActivity
    }

}
