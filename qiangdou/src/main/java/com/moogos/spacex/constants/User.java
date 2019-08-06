package com.moogos.spacex.constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.moogos.spacex.util.CallBack;
import com.moogos.spacex.util.NetUtils;
import com.moogos.spacex.util.Util;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by bobo on 2016/12/25.
 */
public class User {
    public static SimpleDateFormat VIP_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String userId;
    private static String username;
    private static String role_id;
    private static String role_name;

    private static String weixin_name = "";

    private static String role_create_time;
    private static String role_modify_time;

    private static int role = 0; // 0 normal 1 vip

    private static long vipExpireTime = 0;
    private static long lastLoginLocalTime = 0;
    public static long lastLoginServerTime = 0;

    public static long getVipExpireTime() {
        return vipExpireTime;
    }

    public static boolean isLogin = false;
    private static String token;


    public static void setWeixin_name(Context context, String name) {

        if (!name.trim().equalsIgnoreCase("")) {
            weixin_name = name;
            storeUser(context);
        }
    }

    public static String getWeixin_name() {
        return weixin_name;
    }

    public static void setVipExpireTime(Context context, long time) {
        vipExpireTime = time;
        if (vipExpireTime == 0) {
            role = 0;
        } else if (vipExpireTime > lastLoginServerTime) {
            role = 1;
        } else if (vipExpireTime <= lastLoginServerTime) {
            role = 0;
        } else {
            role = 0;
        }
        storeUser(context);
    }

    public static String getToken() {
        return token;
    }

    public static String getUsername() {
        return username;
    }

    private static String mkUsername() {
        long t = Util.getCurTimeMillis();
        return String.valueOf(t).substring(8) + userId;
    }

    public static String getUserId() {
        return userId;
    }

//    public static RoleInfo getRoleInfo() {
//        RoleInfo roleInfo = new RoleInfo();
//        roleInfo.setRole_type(1);
//        roleInfo.setServer_id("1");
//        roleInfo.setServer_name("1");
//        roleInfo.setParty_name("");
//        roleInfo.setRole_id(role_id);
//        roleInfo.setRole_name(role_id);
//        roleInfo.setRole_level("1");
//        roleInfo.setRole_balence(0.0f);
//        roleInfo.setRole_vip("0");
//        roleInfo.setRolelevel_ctime(role_create_time);
//        roleInfo.setRolelevel_mtime(role_modify_time);
//        return roleInfo;
//    }

    public static void updateUser(Context context, CallBack callBack) {
        if (isLogin) {
            NetUtils.login(context, callBack, false);
        }
    }

//    public static int login(final Context context, final CallBack callBack, final CallBack failCallBack) { //0 ok  1 密码错误 2无此用户 3 error 4 登陆中
//        try {
//            boolean isOK = parseJSONString(getData(context));
//            OnLoginListener onLoginListener = new OnLoginListener() {
//                @Override
//                public void loginSuccess(int type, LogincallBack logincallBack) {
//                    if (GlobalConfig.isDebug) {
//                        Log.d(GlobalConfig.TAG, "登陆成功 mem_id =" + logincallBack.mem_id + " user_token=" + logincallBack.user_token);
//                    }
//                    userId = logincallBack.mem_id;
//                    token = logincallBack.user_token;
//                    username = mkUsername();
//                    role_id = userId;
//                    role_name = userId;
//                    isLogin = true;
//                    NetUtils.login(context, callBack, true);
//                    String ts = "" + Util.getCurTime();
//                    role_create_time = ts;
//                    role_modify_time = ts;
//                    lastLoginLocalTime = Util.getCurTime();
//
//                    final RoleInfo roleInfo = getRoleInfo();
//                    MyApplication.sdkManager.setRoleInfo(roleInfo, new SubmitRoleInfoCallBack() {
//                        @Override
//                        public void submitSuccess() {
//                            if (GlobalConfig.isDebug) {
//                                Log.d(GlobalConfig.TAG, "角色信息 Success");
//                            }
//                        }
//
//                        @Override
//                        public void submitFail(String s) {
//                            if (GlobalConfig.isDebug) {
//                                Log.d(GlobalConfig.TAG, "Role Info Submit Fail");
//                            }
//                        }
//                    });
//                    storeUser(context);
////                    MyApplication.sdkManager.showFloatView();
//                }
//
//                @Override
//                public void loginError(int type, LoginErrorMsg loginErrorMsg) {
//                    if (failCallBack != null) {
//                        failCallBack.run(loginErrorMsg.msg);
//                    }
//                    if (GlobalConfig.isDebug) {
//                        Log.e(GlobalConfig.TAG, "loginError code=" + loginErrorMsg.code + "msg =" + loginErrorMsg.msg);
//                    }
//                }
//            };
//            MyApplication.sdkManager.addLoginListener(onLoginListener);
//            if (isOK) {
//                //曾经登陆过，去服务器自动登陆
//                if (GlobalConfig.isDebug) {
//                    Log.d(GlobalConfig.TAG, "fast Login userId = " + userId);
//                }
//                if (MyApplication.sdkManager != null) {
//                    MyApplication.sdkManager.showLogin(true);
//                }
//            } else {
//                //未登录过，需要登陆
//                if (GlobalConfig.isDebug) {
//                    Log.d(GlobalConfig.TAG, "first Login userId or register!");
//                }
//                if (MyApplication.sdkManager != null) {
//                    MyApplication.sdkManager.showLogin(true);
//                }
//            }
//            return 0;
//        } catch (Exception e) {
//            MLog.e("", e);
//            return 1;
//        }
//    }

    private final static String DB = "user";
    private final static String TABLE = "info";

    public static boolean vipIsExpire() {  //不是vip 返回false
        if (vipExpireTime != 0) {
            if (Util.getCurTime() > vipExpireTime || lastLoginServerTime > vipExpireTime) {
                return true;
            }
        }
        return false;
    }

    public static String toJSONString() {
        JSONObject userJson = new JSONObject();
        try {
            userJson.put("username", username);
            userJson.put("userId", userId);
            userJson.put("weixin_name", weixin_name);
            userJson.put("role_id", role_id);
            userJson.put("role_name", role_name);
            userJson.put("vipExpireTime", vipExpireTime);
            userJson.put("role_create_time", role_create_time);
            userJson.put("role_modify_time", role_modify_time);
        } catch (Exception e) {
            if (GlobalConfig.isDebug) {
                e.printStackTrace();
                Log.e(GlobalConfig.TAG, e.getMessage());
            }
        }
        return userJson.toString();
    }

    public static boolean parseJSONString(String userContent) {
        try {
            JSONObject jsonObject = new JSONObject(userContent);
            username = jsonObject.getString("username");
            userId = jsonObject.getString("userId");
            vipExpireTime = jsonObject.getLong("vipExpireTime");
            role_id = jsonObject.getString("role_id");
            role_name = jsonObject.getString("role_name");
            role_create_time = jsonObject.optString("role_create_time");
            role_modify_time = jsonObject.optString("role_modify_time");
            weixin_name = jsonObject.optString("weixin_name");
            return true;
        } catch (Exception e) {
            if (GlobalConfig.isDebug) {
                e.printStackTrace();
                Log.e(GlobalConfig.TAG, "User:parseJSONString" + e.getMessage());
            }
            return false;
        }
    }

    private static void storeUser(Context context) {

        SharedPreferences settings = context.getSharedPreferences(DB, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(TABLE, toJSONString());
        editor.commit();
    }

    private static String getData(Context context) {
        SharedPreferences settings = context.getSharedPreferences(DB, Context.MODE_PRIVATE);
        return settings.getString(TABLE, null);
    }

    public static String getVipExpireTimeString() {
        return VIP_DATE_FORMAT.format(vipExpireTime * 1000);
    }

    public static boolean isVip() {
        switch (role) {
            case 0:
                return false;
            case 1:
                if (System.currentTimeMillis() < lastLoginLocalTime) {
                    return false;
                }
                if (vipIsExpire()) {
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

//    public static void logout(final Context context, final CallBack callBack) {
//        OnLogoutListener onLogoutListener = new OnLogoutListener() {
//            @Override
//            public void logoutSuccess(String code, String msg) {
//                Toast.makeText(context, "退出登陆成功", Toast.LENGTH_LONG).show();
//                isLogin = false;
//                if (callBack != null) {
//                    callBack.run(msg);
//                }
//
//            }
//
//            @Override
//            public void logoutError(String code, String msg) {
//                MLog.e("退出登陆失败 msg =" + msg, null);
//            }
//        };
//        if (MyApplication.sdkManager != null) {
//            MyApplication.sdkManager.logout(onLogoutListener);
//        }
//    }

}
