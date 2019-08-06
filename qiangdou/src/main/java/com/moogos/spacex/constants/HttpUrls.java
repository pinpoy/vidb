package com.moogos.spacex.constants;

/**
 * Created by xupeng on 2017/12/7.
 */

public class HttpUrls {
    public static final String BT_DEBUG = "debug";
    public static final String BT_RELEASE = "release";

    public static final String HTTP_BASE_URL = getLocalHost();
    public static final String HTTP_FOR_IP = "http://ip.chinaz.com/getip.aspx";

    private static final String getLocalHost() {
//        if (BT_RELEASE.equalsIgnoreCase(BuildConfig.BUILD_TYPE)) {              // release(生产), forbid viewing the log and request
//            return "http://red.0i-i0.com/";
//        } else if (BT_DEBUG.equalsIgnoreCase(BuildConfig.BUILD_TYPE)) {     // debugRelease, for viewing the log and request
//            return "http://192.168.2.120:3003/";
//        }

        return "http://red.0i-i0.com/";     //http://red.0i-i0.com/
    }
}
