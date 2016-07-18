package com.icenler.lib.receiver.lifecycle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.icenler.lib.utils.LogUtil;

/**
 * Created by iCenler on 2016/1/28.
 * Description: 后台切换广播监听
 * <p/>
 * {@link ApplicationLifecycleListener.ACTION_BACKGROUND_CHANGED}
 */
public class BackgroundChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.i("ReceiverAction: " + intent.getAction());
    }

}
