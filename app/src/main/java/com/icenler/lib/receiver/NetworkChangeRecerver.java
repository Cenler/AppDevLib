package com.icenler.lib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by iCenler - 2015/9/10.
 * Description：网络状态改变广播
 */
public class NetworkChangeRecerver extends BroadcastReceiver {

    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetworkInfo;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 当前网络发生变化时调用
    }

}
