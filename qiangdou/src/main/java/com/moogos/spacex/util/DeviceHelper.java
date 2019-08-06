package com.moogos.spacex.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by xiaokewang on 2017/12/5.
 */

public class DeviceHelper {

    /**
     * 判断手机的网络是否可用
     *
     * @param context
     * @return true可用
     */
    public static boolean isNetAvailable(Context context) {
        NetworkInfo networkInfo = getNetWorkInfo(context);

        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 主要获取网络信息类
     *
     * @param context
     * @return
     */
    public static NetworkInfo getNetWorkInfo(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi, net等连接的管理）
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null == connectivityManager) {
            return null;
        }

        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo;
    }
}
