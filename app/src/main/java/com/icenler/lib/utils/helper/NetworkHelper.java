package com.icenler.lib.utils.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.icenler.lib.base.BaseApplication;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Description：网络相关帮助工具类
 */
public class NetworkHelper {

    /**
     * 判断网络是否畅通
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager
                connectivity =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                int l = info.length;
                for (int i = 0; i < l; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static String getServiceProvider(Context context) throws Exception {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operatorString = telephonyManager.getSimOperator();
        if (operatorString == null) {
            return null;
        }

        if (operatorString.equals("46000") || operatorString.equals("46002")) {
            //中国移动
            return "CMCC";
        } else if (operatorString.equals("46001")) {
            //中国联通
            return "CUCC";
        } else if (operatorString.equals("46003")) {
            //中国电信
            return "CTCC";
        }

        //error
        return null;
    }

    /**
     * 没有网络
     */
    public static final int NETWORKTYPE_INVALID = 0;
    /**
     * 2G网络
     */
    public static final int NETWORKTYPE_2G = 2;
    /**
     * 3G和3G以上网络，或统称为快速网络
     */
    public static final int NETWORKTYPE_3G = 3;
    /**
     * wifi网络
     */
    public static final int NETWORKTYPE_WIFI = 4;
    private static final String CAT_NET_DEV = "cat /proc/net/dev";
    private static final int RECEIVE_BYTES = 0;
    private static final int RECEIVE_PACKETS = 1;
    private static final int TRANSMIT_BYTES = 2;
    private static final int TRANSMIT_PACKETS = 3;
    private static final String NETCFG = "netcfg";
    private static final String LABEL_LINE_UP = "UP";
    public static String NETWORT_2G = "2G";
    public static String NETWORT_3G = "3G";
    public static String NETWORT_WIFI = "WIFI";

    public NetworkHelper(Context context) {
        super();
    }

    private static String[] parseInfo(String line, String split, int length) {
        String[] data = line.trim().split(split);
        //L.i(data.length);
        return data.length == length ? data : null;
    }

    private static long getMobileTraffic(int trafficType) {
        long result = 0;
        try {
            Runtime runtime = Runtime.getRuntime();
            String label = getUpInterface(runtime);
            Process pro = runtime.exec(CAT_NET_DEV);
            InputStream is = pro.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String[] data;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(label)) {
                    line = line.replaceAll("\\s+", " ");
                    data = parseInfo(line, " ", 17);
                    if (data != null) {
                        switch (trafficType) {
                            case RECEIVE_BYTES:
                                result = Long.valueOf(data[1]);
                                break;
                            case RECEIVE_PACKETS:
                                result = Long.valueOf(data[2]);
                                break;
                            case TRANSMIT_BYTES:
                                result = Long.valueOf(data[9]);
                                break;
                            case TRANSMIT_PACKETS:
                                result = Long.valueOf(data[10]);
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                }
            }
            is.close();
            pro.destroy();
        } catch (Exception e) {
        }
        return result;
    }

    public static String getUpInterface(Runtime runtime) {
        String result = "lo";
        try {
            Process proc = runtime.exec(NETCFG);
            InputStream is = proc.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            String[] data;
            while ((line = reader.readLine()) != null) {
                if (line.contains(LABEL_LINE_UP) && !line.contains(result)) {
                    line = line.replaceAll("\\s+", " ");
                    data = parseInfo(line, " ", 5);
                    if (data != null) {
                        result = data[0];
                    }

                }
            }
            is.close();
            data = null;
            proc.destroy();
        } catch (Exception e) {
        }
        return result;
    }

    public static long getMobileRxBytes() {
        return getMobileTraffic(RECEIVE_BYTES);
    }


    /**
     * 获取网络状态，wifi,wap,2g,3g.
     *
     * @param context 上下文
     * @return int 网络状态 {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G}, {@link
     * #NETWORKTYPE_INVALID},  <p/> {@link #NETWORKTYPE_WIFI}
     */

    public static int getNetWork(Context context) {
        int mNetWorkType = NETWORKTYPE_WIFI;
        ConnectivityManager
                manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();

            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost = android.net.Proxy.getDefaultHost();

                mNetWorkType =
                        TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context)
                                ? NETWORKTYPE_3G : NETWORKTYPE_2G)
                                : NETWORKTYPE_2G;
            }
        } else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }

        return mNetWorkType;
    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager
                telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }


    /**
     * 检查当前网络是2G还是3G,或者wifi
     */
    public static String getNetWorkType() {

        String netCode = NETWORT_3G;

        WifiManager mWifiManager = (WifiManager) BaseApplication.getInstance().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();

        if (mWifiManager.isWifiEnabled() && ipAddress != 0) {

            netCode = NETWORT_WIFI;

        } else {

            TelephonyManager telMgr = (TelephonyManager) BaseApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);

            int networkType = 0;
            if (telMgr != null) {
                networkType = telMgr.getNetworkType();
                switch (networkType) {
                    // 3G
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A: {
                        netCode = NETWORT_3G;
                    }
                    break;
                    // 2G
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA: {
                        netCode = NETWORT_2G;
                    }
                    break;

                    default:
                        break;
                }
            }
        }

        return netCode;
    }


    /**
     * 获取网络类型 ：不区分2G、3G网络，只区分移动网络和wifi网络
     */
    public static boolean isMobileNetwork(Context context) throws Exception {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();

        if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
            // NETWORK_TYPE_EVDO_A是电信3G
            // NETWORK_TYPE_EVDO_A是中国电信3G的getNetworkType
            // NETWORK_TYPE_CDMA电信2G是CDMA
            // 移动2G卡 + CMCC + 2//type = NETWORK_TYPE_EDGE
            // 联通的2G经过测试 China Unicom 1 NETWORK_TYPE_GPRS
            //
            // if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS
            // || info.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA
            // || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE){
            // MyLog.error(Utils.class, "mobile connected");
            // return true;
            // }
            // else{
            // MyLog.error(Utils.class, "not mobile");
            //
            // return false;
            // }

            return true;
        } else {
            return false;
        }
    }


    /**
     * @return
     */
    public static String getIpAddress() {
        try {
            String ipv4 = null;
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = inetAddress.getHostAddress())) {
                        return ipv4;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //0:WIFI 2:2G 3:3G 4:4G
    public static String convertNetworkType(String accessType) {
        if (accessType != null) {
            if (accessType.equals(NetworkHelper.NETWORT_2G)) {
                accessType = "2";
            } else if (accessType.equals(NetworkHelper.NETWORT_3G)) {
                accessType = "3";
            } else if (accessType.equals(NetworkHelper.NETWORT_WIFI)) {
                accessType = "0";
            }
        }

        return "0";
    }

}
