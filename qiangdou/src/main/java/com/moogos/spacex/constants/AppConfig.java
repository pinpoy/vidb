package com.moogos.spacex.constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.moogos.spacex.bean.Login;
import com.moogos.spacex.util.SharedPrefsUtils;

/**
 * Created by xupeng on 2017/12/7.
 */

public class AppConfig {
    private static AppConfig appConfig;
    private Context context;

    private boolean isLogin;            // true: 已登录，false: 未登录
    public static final String PREF_UER_INFO = "app_user";//登录时存储用户信息文件
    public static String vip_type = "0";    //默认无vip
    public static String sum_inviter = "0"; //默认邀请3人
    public static String get_record = "0000";//无领取记录

    /**
     * 是否为vip
     *
     * @return
     */
    public boolean isVip() {
        if (Integer.valueOf(vip_type) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 清除vip信息
     */
    public void clearVipMsg() {
        vip_type = "0";
        sum_inviter = "0";
        get_record = "0000";
    }

    /**
     * 获取实例
     *
     * @param context
     * @return
     */
    public static AppConfig getConfig(Context context) {
        synchronized (AppConfig.class) {
            if (null == appConfig) {
                appConfig = new AppConfig(context);
            }
        }
        return appConfig;
    }

    private AppConfig(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * 保存用户信息
     *
     * @param userInfo
     */
    public void saveUserInfo(Login.UserInfo userInfo) {
        if (userInfo != null) {
            SharedPrefsUtils sharedPrefs = new SharedPrefsUtils(context, SharedPrefsUtils.SP_FILE_NAME_USER);
            sharedPrefs.setObject(SharedPrefsUtils.SP_KEY_USER_INFO, userInfo);
        }
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public Login.UserInfo getUserInfo() {
        SharedPrefsUtils sharedPrefs = new SharedPrefsUtils(context, SharedPrefsUtils.SP_FILE_NAME_USER);
        Login.UserInfo user = sharedPrefs.getObject(SharedPrefsUtils.SP_KEY_USER_INFO, Login.UserInfo.class);
        return user;
    }

    /**
     * 清除用户信息
     */
    public void clearUserInfo() {
        SharedPrefsUtils sharedPrefs = new SharedPrefsUtils(context, SharedPrefsUtils.SP_FILE_NAME_USER);
        sharedPrefs.clearSharedPreferences();
    }


    /**
     * 是否登录
     *
     * @return
     */
    public boolean isLogin() {
        if (null == getUserInfo()) {
            return false;
        }
        isLogin = !TextUtils.isEmpty(getUserInfo().getInvitation_code());
        return isLogin;
    }


    /**
     * 获取使用的次数
     *
     * @return
     */

    public static String getGrabNumber(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_UER_INFO, Context.MODE_PRIVATE);
        return sharedPreferences.getString("grabNumber", null);
    }

    /**
     * 保存使用的次数
     *
     * @param grabNumber 用户id
     */

    public static void saveGrabNumber(Context context, String grabNumber) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_UER_INFO, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("grabNumber", grabNumber).commit();
    }


    /**
     * 跳过之后保存游客的ID
     *
     * @param visitorId
     */
    public static void saveVisitorId(Context context, String visitorId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_UER_INFO, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("visitorId", visitorId).commit();
    }

    /**
     * 获取游客的ID
     *
     * @return
     */
    public static String getVisitorId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_UER_INFO, Context.MODE_PRIVATE);
        return sharedPreferences.getString("visitorId", null);
    }


    /**
     * 保存登陆的时间
     *
     * @param visitorId
     */
    public static void saveLoginTime(Context context, Long visitorId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_UER_INFO, Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong("loginTime", visitorId).commit();
    }

    /**
     * 获取登陆的时间
     *
     * @return
     */
    public static long getLoginTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_UER_INFO, Context.MODE_PRIVATE);
        return sharedPreferences.getLong("loginTime", 0);
    }


    /**
     * 保存注册之后的deviceId
     *
     * @param visitorId
     */
    public static void saveDeviceId(Context context, String visitorId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_UER_INFO, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("deviceId", visitorId).commit();
    }


}
