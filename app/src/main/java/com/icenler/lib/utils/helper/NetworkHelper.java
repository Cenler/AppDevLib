package com.icenler.lib.utils.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by iCenler - 2015/9/10.
 * Description：网络相关工具类
 */
public class NetworkHelper {

    public static final int CMCC = 0;// 中国移动
    public static final int CUCC = 1;// 中国联通
    public static final int CTCC = 2;// 中国电信

    /**
     * 手机号类型：移动0、联通1、电信2
     */
    public static int MOBILES_TYPE = -1;


    private static final int NETWORK_TYPE_INVALID = -1; // 无网络
    private static final int NETWORK_TYPE_ETHERNET = 0; // 有线
    private static final int NETWORK_TYPE_WIFI = 1;     // WiFi
    private static final int NETWORK_TYPE_2G = 2;
    private static final int NETWORK_TYPE_3G = 3;
    private static final int NETWORK_TYPE_4G = 4;

    /**
     * @param context
     * @return 当前网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        return getNetworkState(context) != NETWORK_TYPE_INVALID;
    }

    /**
     * @param context
     * @return 当前网络状态
     */
    public static int getNetworkState(Context context) {
        int mNetworkType = NETWORK_TYPE_INVALID;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isAvailable()) {
            if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                // 有线网络
                mNetworkType = NETWORK_TYPE_ETHERNET;
            } else if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // WiFi 网络
                mNetworkType = NETWORK_TYPE_WIFI;
            } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // 移动网络
                mNetworkType = getMobileNetworkType(netInfo);
            }
        }

        return mNetworkType;
    }

    /**
     * @param netInfo
     * @return 当前移动网络类型
     */
    private static int getMobileNetworkType(NetworkInfo netInfo) {
        // ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType();
        int networkType = netInfo.getSubtype();
        String networkName = netInfo.getSubtypeName();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                //case TelephonyManager.NETWORK_TYPE_GSM:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_TYPE_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                //case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                return NETWORK_TYPE_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                //case TelephonyManager.NETWORK_TYPE_IWLAN:
                return NETWORK_TYPE_4G;
            default:
                // 网络制式：2G          3G          4G
                // 移   动：GSM      TD-SCDMA      TD-LTE
                // 联   通：GSM      WCDMA         TD-LTE||FDD-LTE
                // 电   信：CDMA1X   CDMA2000      TD-LTE||FDD-LTE
                if (networkName.equalsIgnoreCase("TD-SCDMA") || networkName.equalsIgnoreCase("WCDMA") || networkName.equalsIgnoreCase("CDMA2000")) {
                    return NETWORK_TYPE_3G;
                } else if (networkName.equalsIgnoreCase("TD-LTE") || networkName.equalsIgnoreCase("FDD-LTE")) {
                    return NETWORK_TYPE_4G;
                } else {
                    return NETWORK_TYPE_2G;
                }
        }
    }

    /**
     * @param context
     * @return 网络服务提供商
     */
    public static int getServiceProvider(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simOpt = telMgr.getSimOperator();
        if ("46000".equals(simOpt) || "46002".equals(simOpt)) {
            return CMCC;//中国移动
        } else if ("46001".equals(simOpt)) {
            return CUCC;//中国联通
        } else if ("46003".equals(simOpt)) {
            return CTCC;//中国电信
        }

        return -1;// 无法识别
    }

    /**
     * @return 当前网络 IP
     */
    public static String getInetAddress() {
        String ipv4 = null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        //if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {
                        ipv4 = inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ipv4;
    }

}
