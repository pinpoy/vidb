package com.moogos.spacex.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.moogos.spacex.bean.MobileIp;
import com.moogos.spacex.bean.Skip;
import com.moogos.spacex.constants.ApiServiceFactory;
import com.moogos.spacex.constants.HttpUrls;
import com.moogos.spacex.constants.ParamsHelper;
import com.moogos.spacex.core.SplashActivity;
import com.moogos.spacex.logger.SpiderLogger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by xupeng on 2017/12/7.
 */

public class DeviceInfo {
    /**
     * 获取手机序列号IMEI
     *
     * @param context
     * @return
     */
    public static String getImeiId(Context context) {

        String deviceId = SharedPreferencesUtil.getDeviceId(context);

        if (TextUtils.isEmpty(deviceId)) {

            deviceId = getTelePhoneManager(context).getDeviceId();
            //当imei号为空时返回mac地址
            if (StringUtil.checkEmpty(deviceId)) {
                deviceId = getLocalMacAddress(context);
            }
            SharedPreferencesUtil.saveDeviceId(context, deviceId);
        }

        return deviceId;
    }


    /**
     * 得到手机的Mac地址
     *
     * @param context 上下文
     * @return
     */
    public static String getLocalMacAddress(Context context) {

        String macAddress = SharedPreferencesUtil.getMacAddress(context);

        if (TextUtils.isEmpty(macAddress)) {

            try {

                WifiManager wifi = (WifiManager) context
                        .getSystemService(Context.WIFI_SERVICE);

                WifiInfo info = wifi.getConnectionInfo();

                String mac[] = info.getMacAddress().split(":");

                StringBuilder macbuild = new StringBuilder();

                for (int i = 0; i < mac.length; i++) {

                    macbuild.append(mac[i]);

                }

                if (!TextUtils.isEmpty(macbuild)) {

                    macAddress = macbuild.toString();
                    SharedPreferencesUtil.saveMacAddress(context, macAddress);
                }
            } catch (Exception e) {

                macAddress = "";
                SpiderLogger.getLogger().e("DeviceInfo", e.toString());
            }


        }

        return macAddress;

    }


    /**
     * 得到电话的管理类
     *
     * @param context
     * @return
     */
    public static TelephonyManager getTelePhoneManager(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context

                .getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyManager;
    }


    /**
     * 获取手机的ip地址
     *
     * @param context
     * @return
     */
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    /**
     * 获取IP地址
     *
     * @param context
     * @return
     */
    public static String getMobileNetIP(Context context) {
        return StringUtil.checkEmpty(SplashActivity.mobileIpStr) ?
                getIPAddress(context) : SplashActivity.mobileIpStr;
    }

    /**
     * 获取型号model
     *
     * @return
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取品牌brand
     *
     * @return
     */
    public static String getBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取设备名device
     *
     * @return
     */
    public static String getDevice() {
        return android.os.Build.DEVICE;
    }

    /**
     * 获取androidID
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        return android.provider.Settings.Secure.getString
                (context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

    }


    /**
     * 获取Sim 序列号
     * @param context
     * @return
     */
    public static String getSimNumber(Context context) {
        android.telephony.TelephonyManager tm =
                (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimSerialNumber();


    }


}
