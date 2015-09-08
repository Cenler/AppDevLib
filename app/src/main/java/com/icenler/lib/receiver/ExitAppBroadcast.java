package com.icenler.lib.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by iCenler - 2015/7/14.
 * Description：退出应用广播
 */
public class ExitAppBroadcast extends BroadcastReceiver {

    public static final String ACTION_EXIT_APP = "icenler.intent.action.EXIT_APP";

    /**
     * 发送全局退出应用广播
     *
     * @param context
     */
    public static void exitApp(Context context) {
        context.sendBroadcast(new Intent().setAction(ACTION_EXIT_APP));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ((Activity) context).finish();
    }

}
