package com.icenler.lib.receiver.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.icenler.lib.utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by iCenler - 2015/9/10.
 * Description：网络状态改变广播
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    final static String ANDROID_NETWORK_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private volatile static boolean isNetworkAvailable = false;

    private static NetworkHelper.NetType mNetType;
    private static ArrayList<NetworkChangeObserver> mNetworkChangeObservers =
            new ArrayList<>();

    public static void registerNetworkStateReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ANDROID_NETWORK_CHANGE_ACTION);
        context.registerReceiver(new NetworkChangeReceiver(), filter);
    }

    public static void registerObserver(NetworkChangeObserver observer) {
        if (mNetworkChangeObservers == null) {
            mNetworkChangeObservers = new ArrayList<NetworkChangeObserver>();
        }
        mNetworkChangeObservers.add(observer);
    }

    public static void removeObserver(NetworkChangeObserver observer) {
        if (mNetworkChangeObservers != null) {
            if (mNetworkChangeObservers.contains(observer)) {
                mNetworkChangeObservers.remove(observer);
            }
        }
    }

    public static boolean isNetworkAvailable() {
        return isNetworkAvailable;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(ANDROID_NETWORK_CHANGE_ACTION)) {
            if (!NetworkHelper.isNetworkAvailable(context)) {
                LogUtil.i("<--- network disconnected --->");
                isNetworkAvailable = false;
            } else {
                LogUtil.i("<--- network connected --->");
                isNetworkAvailable = true;
                mNetType = NetworkHelper.getAPNType(context);
            }

            notifyObserver();
        }
    }

    public static NetworkHelper.NetType getAPNType() {
        return mNetType;
    }

    private void notifyObserver() {
        if (!mNetworkChangeObservers.isEmpty()) {
            final int count = mNetworkChangeObservers.size();
            for (int i = 0; i < count; i++) {
                NetworkChangeObserver observer = mNetworkChangeObservers.get(i);
                if (observer != null) {
                    if (isNetworkAvailable()) {
                        observer.onNetworkConnected(mNetType);
                    } else {
                        observer.onNetworkDisconnect();
                    }
                }
            }
        }
    }

}
