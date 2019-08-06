package com.moogos.spacex.util;

import android.util.Log;

import com.moogos.spacex.constants.GlobalConfig;

/**
 * Created by bobo on 2016/12/29.
 */
public class MLog {

    public static void d(String content) {
        if (GlobalConfig.isDebug) {
            Log.d(GlobalConfig.TAG, content);
        }
    }

    public static void e(String content, Throwable e) {
        if (GlobalConfig.isDebug) {
            if (e != null) {
                e.printStackTrace();
                Log.e(GlobalConfig.TAG, content + " err=" + e.getMessage());
            } else {
                Log.e(GlobalConfig.TAG, content);
            }
        }
    }
}
