package com.moogos.spacex.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xupeng on 2017/12/7.
 */

public class SharedPreferencesUtil {
    /**
     * @param context
     * @Description:保存手机序列号
     * @exception:
     */
    public static void saveDeviceId(Context context, String deviceId) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "phoneInfo", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("phoneDeviceId", deviceId).commit();
    }

    /**
     * @param context
     * @Description:获取手机序列号
     * @exception:
     */
    public static String getDeviceId(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "phoneInfo", Context.MODE_PRIVATE);
        return sharedPreferences.getString("phoneDeviceId", "");
    }


    /**
     * @param context
     * @Description:保存手机MAC地址
     * @exception:
     */
    public static void saveMacAddress(Context context, String macAddress) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "phoneInfo", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("phoneMacAddress", macAddress)
                .commit();
    }

    /**
     * @param context
     * @Description:获取手机MAC地址
     * @exception:
     */
    public static String getMacAddress(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "phoneInfo", Context.MODE_PRIVATE);
        return sharedPreferences.getString("phoneMacAddress", "");
    }
}
