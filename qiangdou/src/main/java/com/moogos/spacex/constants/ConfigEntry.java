package com.moogos.spacex.constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.moogos.spacex.core.QiangHongBaoService;

import org.json.JSONObject;

/**
 * Created by bobo on 2016/12/24.
 */
public class ConfigEntry {
    private static boolean isWeiXinOpen = true;
    private static boolean isQQOpen = true;
    private static boolean autoReply = false;
    //是否学习屏蔽
    private static boolean isShield = false;

    private static String replyThink = "谢谢老板";
    private static String shieldContent = "";
    private static boolean lockedCanVip = false;
    private static int delayCanTimeVip = 0;
    private static int delayReplyCanTimeVip = 0;

    public static boolean serviceIsON = true;

    private final static String DB = "qianghongbao";
    private final static String TABLE = "qhb_table";


    public static void init(Context context) {
        String jso_str = getStringSP(context);
        if (jso_str != null) {
            try {
                JSONObject jso = new JSONObject(jso_str);

                isWeiXinOpen = jso.optBoolean("isWeiXinOpen", true);
                isQQOpen = jso.optBoolean("isQQOpen", true);

                autoReply = jso.optBoolean("autoReply", false);
                replyThink = jso.optString("replyThink", "谢谢老板");

                shieldContent = jso.optString("shieldContent", "");

                lockedCanVip = jso.optBoolean("lockedCanVip", false);
                delayCanTimeVip = jso.optInt("delayCanVip", 0);

                delayReplyCanTimeVip = jso.optInt("delayReplyCanTimeVip", 0);
                serviceIsON = jso.optBoolean("serviceIsON", true);

                isShield = jso.optBoolean("isShield", false);

            } catch (Exception e) {
                if (GlobalConfig.isDebug == true) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setIsWeiXinOpen(Context context, boolean set) {
        isWeiXinOpen = set;
        store(context);
    }

    public static void setIsQQOpen(Context context, boolean set) {
        isQQOpen = set;
        store(context);
    }

    public static void setAutoReply(Context context, boolean set) {
        autoReply = set;
        store(context);
    }

    public static void setShield(Context context, boolean set) {
        isShield = set;
        store(context);
    }

    public static void setReplyThink(Context context, String think) {
        replyThink = think;
        store(context);
    }

    public static void setShieldContent(Context context, String shield) {
        shieldContent = shield;
        store(context);
    }

    public static void setLockedCanVip(Context context, boolean set) {
        lockedCanVip = set;
        store(context);
    }

    public static void setDelayCanTimeVip(Context context, int time) {
        delayCanTimeVip = time;
        store(context);
    }

    public static void setDelayReplyCanTimeVip(Context context, int time) {
        delayReplyCanTimeVip = time;
        store(context);
    }

    public static boolean getIsWeiXinOpen() {
        return isWeiXinOpen;
    }

    public static boolean getIsQQOpen() {
        return isQQOpen;
    }

    public static boolean getAutoReply() {
        return autoReply;
    }

    public static boolean getIsShield() {
        return isShield;
    }

    public static String getReplyThink() {
        return replyThink;
    }

    public static String getShieldContent() {
        return shieldContent;
    }

    public static boolean getLockedCanVip() {
        return lockedCanVip;
    }

    public static int getDelayCanTimeVip() {
        return delayCanTimeVip;
    }

    public static int getDelayReplyCanTimeVip() {
        return delayReplyCanTimeVip;
    }

    public static void storeSettings(Context context) {
        JSONObject jso = new JSONObject();
        try {
            jso.put("isWeiXinOpen", isWeiXinOpen);
            jso.put("isQQOpen", isQQOpen);
            jso.put("autoReply", autoReply);
            jso.put("serviceIsON", serviceIsON);
            jso.put("replyThink", replyThink);
            jso.put("lockedCanVip", lockedCanVip);
            jso.put("delayCanTimeVip", delayCanTimeVip);
            jso.put("delayReplyCanTimeVip", delayReplyCanTimeVip);
            jso.put("serviceIsON", serviceIsON);
            jso.put("isShield", isShield);
            jso.put("shieldContent", shieldContent);
            storeStringSP(context, jso.toString());
        } catch (Exception e) {
            if (GlobalConfig.isDebug == true) {
                e.printStackTrace();
            }
        }
    }

    private static void storeStringSP(Context context, String jsonString) {
        SharedPreferences settings = context.getSharedPreferences(DB, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(TABLE, jsonString);
        editor.commit();
    }

    private static String getStringSP(Context context) {
        SharedPreferences settings = context.getSharedPreferences(DB, Context.MODE_PRIVATE);
        return settings.getString(TABLE, null);
    }

    public static void store(Context context) {
        storeSettings(context);
    }

    public static boolean isAccessibilityEnabled(final Context context) {
        if (!serviceIsON) {
            return false;
        }
        int accessibilityEnabled = 0;
        final String LIGHTFLOW_ACCESSIBILITY_SERVICE = context.getPackageName() + "/"
                + QiangHongBaoService.class.getName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            //Log.d("ACCESSIBILITY: " + accessibilityEnabled);

        } catch (Settings.SettingNotFoundException e) {
            if (GlobalConfig.isDebug) {
                Log.d(GlobalConfig.TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
            }
        }

        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();
                    if (accessabilityService.equalsIgnoreCase(LIGHTFLOW_ACCESSIBILITY_SERVICE)) {
                        return true;
                    }
                }
            }
        } else {
            if (GlobalConfig.isDebug) {
                Log.d(GlobalConfig.TAG, "***ACCESSIBILIY IS DISABLED***");
            }
        }
        return false;
    }

    public static void clearSeeting() {
        isWeiXinOpen = true;
        isQQOpen = true;
        autoReply = false;
        //是否学习屏蔽
        isShield = false;
        replyThink = "谢谢老板";
        shieldContent = "";
        lockedCanVip = false;
        delayCanTimeVip = 0;
        delayReplyCanTimeVip = 0;
    }

}
