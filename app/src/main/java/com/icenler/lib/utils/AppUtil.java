package com.icenler.lib.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;

import com.icenler.lib.feature.Constants;
import com.icenler.lib.utils.helper.SharedPrefsHelper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

/**
 * Created by iCenler - 2015/7/14.
 * Description：Android 开发常用操作工具类
 */
public class AppUtil {

    private AppUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * @return 版本号
     */
    public static int getAppVersionCode(Context context) {
        PackageInfo info;
        int versionCode = -1;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return versionCode;
    }

    /**
     * @return 版本名称
     */
    public static String getAppVersionName(Context context) {
        PackageInfo info;
        String versionName = "";
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return versionName;
    }

    /**
     * @return 应用名称
     */
    public String getAppName(Context context) {
        String applicationName = "";
        PackageManager packageManager;
        ApplicationInfo applicationInfo;
        try {
            packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (Exception e) {
        }

        return applicationName;
    }

    /**
     * 检测服务是否运行
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo si : servicesList) {
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    /**
     * 非通话设备无法获取 (权限:READ_PHONE_STATE)
     *
     * @param context
     * @return DeviceID TODO 待验证:Build.SERIAL
     */
    public static String getDeviceID(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telMgr != null ? telMgr.getDeviceId() : null;
    }

    /**
     * @param context
     * @return Sim 卡序列号
     */
    public static String getSimSerialNumber(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telMgr != null ? telMgr.getSimSerialNumber() : null;
    }

    /**
     * 当设备刷机或恢复出厂设置后该值会被重置
     *
     * @param context
     * @return AndroidID
     */
    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 没有 Wifi 和蓝牙硬件设备无法获取
     *
     * @param context
     * @return MacAddress
     */
    public static String getMacAddress(Context context) {
        String macAddress = SharedPrefsHelper.get(Constants.PREFS_MAC_ADDRESS, "");

        if (TextUtils.isEmpty(macAddress)) {
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (null != wifiMgr) {
                WifiInfo info = wifiMgr.getConnectionInfo();
                if (null != info) {
                    macAddress = info.getMacAddress();
                    SharedPrefsHelper.put(Constants.PREFS_MAC_ADDRESS, macAddress);
                }
            }
        }

        return macAddress;
    }

    /**
     * @param context
     * @param appName
     * @return 通用唯一标识
     */
    public static String getUniversalID(Context context) {
        String uuid = SharedPrefsHelper.get(Constants.PREFS_UUID, "");
        if (uuid == null) {
            final String androidId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            try {
                if (!"9774d56d682e549c".equals(androidId)) {
                    uuid = UUID.nameUUIDFromBytes((androidId).getBytes("utf8")).toString();
                } else {
                    uuid = UUID.randomUUID().toString();
                }
            } catch (Exception e) {
                uuid = UUID.randomUUID().toString();
            }
            SharedPrefsHelper.put(Constants.PREFS_UUID, uuid);
        }

        return uuid;
    }

    /**
     * @return IPv6 地址形式
     */
    public static String getLocalIpv6Address() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            LogUtil.e("WifiPreference IpAddress" + ex.toString());
        }

        return null;
    }

    /**
     * @return IPv4 地址形式
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            LogUtil.e("WifiPreference IpAddress" + ex.toString());
        }

        return null;
    }

    public static String getInternetIP() {
        String ip = null;
        try {
            String address = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String tmpString = "";
                StringBuilder retJSON = new StringBuilder();
                while ((tmpString = reader.readLine()) != null) {
                    retJSON.append(tmpString + "\n");
                }

                JSONObject jsonObject = new JSONObject(retJSON.toString());
                String code = jsonObject.getString("code");
                if (code.equals("0")) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    ip = data.getString("ip");
                } else {
                    ip = "";
                    LogUtil.e("IP接口异常，无法获取IP地址！");
                }
            } else {
                ip = "";
                LogUtil.e("网络连接异常，无法获取IP地址！");
            }
        } catch (Exception exception) {
            LogUtil.e("GetNetIp() : exception = " + exception.toString());
        }

        return ip;
    }

    /**
     * @return 判断SDCard是否可用
     */
    public static boolean isSdcardAvailable() {
        return Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * @param path
     * @return 获取指定路径空间大小
     */
    public static long getAvailableBytesForDirectory(String path) {
        return new StatFs(path).getAvailableBytes();
    }

    /**
     * @param number
     * @return 自动格式化空间大小单位
     */
    public static String formatSpaceSize(Context context, long number) {
        return Formatter.formatFileSize(context, number);
    }

}
