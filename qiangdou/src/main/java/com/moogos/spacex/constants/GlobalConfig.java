package com.moogos.spacex.constants;

import android.os.Build;

import com.mogo.space.BuildConfig;
import com.moogos.spacex.util.Util;

/**
 * Created by Apple on 16/11/1.
 */
public class GlobalConfig {


    public static String Splash_ADSLOT = "s8164526";

    public static String Banner_ADSLOT = "sf0bf973";

    public static String Inter_ADSLOT = "s779e636";

    public static boolean isDebug = BuildConfig.DEBUG;

    public static final String TAG = "VVV";
    //log type
    public static int RED_BAG_DataType = 1;
    //resume app
    public static int RESUME_DataType = 2;

    public static String android_id = "";

    private static String channel = "";
    public static String pkg = "";

    public static String VIP_URL = "";
    private static final float[] MONEY = new float[]{15, 40, 80, 150, 1};
    private static final float[] DEBUG_MONEY = new float[]{0.01f, 0.01f, 0.01f, 0.01f, 0.01f};

    public static float[] getMoney() {
        if (isDebug) {
            return DEBUG_MONEY;
        } else {
            return MONEY;
        }
    }

    public static void setChannel(String channel) {
        GlobalConfig.channel = channel;
    }

    public static String getChannel() {
        return GlobalConfig.channel;
    }

    //    public static String LIGHTFLOW_ACCESSIBILITY_SERVICE;
//
//    public static SimpleDateFormat MINE_BAG_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    public static SimpleDateFormat PING_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
//
//    public static List<Map<String, String>> RANK_DATA = new ArrayList<Map<String, String>>();
//
//
//    public static AtomicBoolean isAutoReply;
//
//    public static AtomicBoolean isMyWordsOn;
//
//    public static AtomicBoolean isOpen;
//
//    public static AtomicBoolean isQQOpen;
//
//
//    public static void init(Context mContext) {
//
//        isAutoReply = new AtomicBoolean();
//        isAutoReply.set(PreUtils.getBool(GlobalConstans.PreKey.AUTOREPLY, GlobalConstans.GlobalDefault.DEFAULT_AUTOREPLY));
//
//        isMyWordsOn = new AtomicBoolean();
//        isMyWordsOn.set(PreUtils.getBool(GlobalConstans.PreKey.MYWORDSON, GlobalConstans.GlobalDefault.DEFAULT_MYWORDSON));
//
//        isOpen = new AtomicBoolean();
//        isOpen.set(PreUtils.getBool(GlobalConstans.PreKey.ISOPEN, GlobalConstans.GlobalDefault.DEFAULT_ISOPEN));
//
//        isQQOpen = new AtomicBoolean();
//        isQQOpen.set(PreUtils.getBool(GlobalConstans.PreKey.ISQQOPEN, GlobalConstans.GlobalDefault.DEFAULT_ISQQOPEN));
//
//
//        LIGHTFLOW_ACCESSIBILITY_SERVICE = mContext.getPackageName() + "/"
//                + QiangHongBaoService.class.getName();
//
//    }
    public static StringBuilder getCommonInfo(int type) {
        StringBuilder sb = new StringBuilder();
        sb.append(type).append("|");
        sb.append(GlobalConfig.pkg).append("|");
        sb.append(GlobalConfig.android_id).append("|");
        sb.append(GlobalConfig.getChannel()).append("|");
        sb.append(User.getUserId()).append("|");
        sb.append(User.isVip()).append("|");
        sb.append(User.getVipExpireTime()).append("|");
        sb.append(Util.getCurTime()).append("|");
        sb.append(User.getWeixin_name());
        return sb;
    }

}
