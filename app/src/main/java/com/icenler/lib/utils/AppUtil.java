package com.icenler.lib.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.icenler.lib.R;
import com.icenler.lib.feature.AppConfig;
import com.icenler.lib.feature.base.BaseApplication;
import com.icenler.lib.utils.helper.SharedPrefsHelper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
     * @return 应用版本号
     */
    public static int getAppVersionCode(Context context) {
        PackageInfo info = null;
        int versionCode = 1;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    /**
     * @return 应用版本名称
     */
    public static String getAppVersionName(Context context) {
        PackageInfo info = null;
        String versionName = "1.0.0";
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

    /**
     * @return 应用名称
     */
    public String getAppName(Context context) {
        String applicationName;
        PackageManager packageManager;
        ApplicationInfo applicationInfo;
        try {
            packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (Exception e) {
            applicationName = context.getString(R.string.app_name);
        }

        return applicationName;
    }

    /**
     * 获取当前SDK版本
     */
    public static int getAndroidSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取设备认证码
     */
    public static String getIMEI(Context context) {
        requestPermission(Manifest.permission.READ_PHONE_STATE);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        return tm == null ? null : tm.getDeviceId();
    }

    /**
     * 当前线程是否为主线程
     */
    public static boolean isMainLooper() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * context 是否是 Activity 例例
     */
    public static boolean isActivityContext(Context context) {
        return context instanceof Activity;
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
     * 查看特殊权限否否申明
     */
    public static void requestPermission(String permission) {
        Context context = BaseApplication.getInstance();
        if (PackageManager.PERMISSION_GRANTED != context.getPackageManager().checkPermission(permission, context.getPackageName())) {
            throw new UnsupportedOperationException("missing permission \"" + "android.permission.READ_PHONE_STATE " + "\" in manifest.xml!");
        }
    }

    /**
     * 获取终端唯一标识
     */
    public static String getDeviceID(Context context) {
        String deviceID = SharedPrefsHelper.get(AppConfig.PREFS_DEVICE_ID, "");
        if (!TextUtils.isEmpty(deviceID)) {
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            // Use the Android ID unless it's broken, in which case fallback on deviceId,
            // unless it's not available, then fallback on a random number which we store
            // to a prefs file
            try {
                if (!"9774d56d682e549c".equals(androidId)) {
                    deviceID = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                } else {
                    deviceID = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    deviceID = deviceID != null ? UUID.nameUUIDFromBytes(deviceID.getBytes("utf8")).toString() : UUID.randomUUID().toString();
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            SharedPrefsHelper.put(AppConfig.PREFS_DEVICE_ID, deviceID);
        }

        return deviceID;
    }


    /**
     * 获取应用唯一标识
     *
     * @param context
     * @param appName
     * @return
     */
    public static String getDeviceUUID(Context context, String appName) {
        String pullToken = SharedPrefsHelper.get(AppConfig.PREFS_DEVICE_UUID, "");
        if (pullToken == null) {
            UUID uuid;
            final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            // Use the Android ID unless it's broken, in which case fallback on deviceId,
            // unless it's not available, then fallback on a random number which we store
            // to a prefs file
            try {
                if (!"9774d56d682e549c".equals(androidId)) {
                    uuid = UUID.nameUUIDFromBytes((androidId + appName).getBytes("utf8"));
                } else {
                    final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    uuid = deviceId != null ? UUID.nameUUIDFromBytes((deviceId + appName).getBytes("utf8")) : UUID.randomUUID();
                }
            } catch (Exception e) {
                uuid = UUID.randomUUID();
            }
            pullToken = uuid.toString();
            SharedPrefsHelper.put(AppConfig.PREFS_DEVICE_UUID, pullToken);
        }

        return pullToken;
    }

    /**
     * @param context
     * @return 无线网卡 MAC 地址
     */
    public String getMacAddress(Context context) {
        String macAddress = SharedPrefsHelper.get(AppConfig.PREFS_MAC_ADDRESS, "");

        if (TextUtils.isEmpty(macAddress)) {
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
            macAddress = (info == null ? null : info.getMacAddress());

            SharedPrefsHelper.put(AppConfig.PREFS_MAC_ADDRESS, macAddress);
        }

        return macAddress;
    }

    /**
     * @return IPV6 地址形式
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
     * @return IPV4 地址形式
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

    public static String getNetIp() {
        String IP = "";
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
                    IP = data.getString("ip");
                } else {
                    IP = "";
                    LogUtil.e("IP接口异常，无法获取IP地址！");
                }
            } else {
                IP = "";
                LogUtil.e("网络连接异常，无法获取IP地址！");
            }
        } catch (Exception exception) {
            IP = "";
            LogUtil.e("GetNetIp() : exception = " + exception.toString());
        }

        return IP;
    }

}
