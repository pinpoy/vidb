package com.moogos.spacex.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.moogos.spacex.constants.GlobalConfig;
import com.moogos.spacex.constants.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by bobo on 2016/12/28.
 */
public class NetUtils {
    public static String SERVER = "www.51oneone.com";
    public static String PUT_USER_URL = "http://" + SERVER + "/v3/Login";
    public static String RANK_URL = "http://" + SERVER + "/v3/rank";
    public static String RECORD_URL = "http://" + SERVER + "/v3/record";
    public static String LOG_URL = "http://" + SERVER + "/v3/log";

    public static String HELP_URL = "http://51oneone.com/hongbao/help.html";
    public static String VIEDO_URL = "http://51oneone.com/hongbao/helpvideo.html";

    public static String DOWN_URL = "http://51oneone.com/hongbao/";

    public static void putRecord(String order_id, int type, float cash, final CallBack callBack) {
//        if record.Type == 1 { //月卡
//            t = t + 2592000
//        } else if record.Type == 2 { //季卡
//            t = t + 7776000
//        } else if record.Type == 3 { // 半年卡
//            t = t + 15552000
//        } else if record.Type == 4 { //年卡
//            t = t + 31536000
//        } else if record.Type == 5 { // 赞助
//            t = t + 86400
        JSONObject jso = new JSONObject();
        try {
            jso.put("order_id", order_id);
            jso.put("user_id", User.getUserId());
            jso.put("type", type);
            jso.put("cash", cash);
            final String req = mkReqParam(jso.toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = httpPost(NetUtils.RECORD_URL, req);
                    try {
                        JSONObject json = new JSONObject(result);
                        String status = json.getString("");
                        if (status.equalsIgnoreCase("0")) {
                            if (callBack != null) {
                                callBack.run(status);
                            }
                        } else {
                            MLog.d("Put Record Failed , result=" + result);
                        }
                    } catch (Exception e) {
                        MLog.e("Put Record Failed , req=" + req, e);
                    }
                }
            }).start();
        } catch (Exception e) {
            MLog.e("Put Record To json Failed", e);
        }

    }

    private static long last_login_time = 0;

    public static void login(final Context context, final CallBack callBack, boolean isForce) {
        JSONObject user = new JSONObject();
        long now = Util.getCurTime();
        if (now - last_login_time <= 5 * 60) {
            MLog.d("登陆时间太短，last Login =" + last_login_time);
            if (!isForce) {
                if (callBack != null) {
                    callBack.run("");
                }
                return;
            }
        } else {
            last_login_time = now;
        }
        try {
            user.put("user_id", User.getUserId());
            user.put("user_name", User.getUsername());
            user.put("token", User.getToken());
            user.put("ts", Util.getCurTime());

            final String req = mkReqParam(user.toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String result = httpPost(NetUtils.PUT_USER_URL, req);
                        JSONObject j = new JSONObject(result);
                        String status = j.getString("status");
                        MLog.d("PUT_USER_URL Result = " + result + " req = " + req);
                        if (status.equals("0")) {
                            String p = j.getString("P");
                            JSONObject user = new JSONObject(Util.base64Decode(p));
                            if (user.getString("token").equalsIgnoreCase(User.getToken())) {
                                User.lastLoginServerTime = user.getLong("ts");
                                User.setVipExpireTime(context, user.getLong("vip_expire_time"));
                                User.isLogin = true;
                                MLog.d("User Login Response =" + user.toString());
                                if (callBack != null) {
                                    callBack.run(status);
                                }
                            } else {
                                MLog.d("Token不一致");
                            }
                        } else {
                            if (GlobalConfig.isDebug) {
                                Log.e(GlobalConfig.TAG, "Login response error ;result =" + result);
                            }
                        }
                    } catch (Exception e) {
                        MLog.e("Put User Url Failed req = " + req, e);
                    }
                }
            }).start();
        } catch (Exception e) {
            if (GlobalConfig.isDebug) {
                e.printStackTrace();
            }
        }
    }

    private static String mkReqParam(String p) {
        JSONObject jso = new JSONObject();
        String p1 = Util.base64Encode(p);
        try {
            jso.put("P", p1);
        } catch (Exception e) {
            if (GlobalConfig.isDebug) {
                e.printStackTrace();
                Log.e(GlobalConfig.TAG, "p=" + p + " p1=" + p1);
            }
        }
        return jso.toString();
    }

    public static void postLog(String content) {
        final String j = mkReqParam(content);
//        System.out.println("==========postLog :"+content);
        new Thread(new Runnable() {
            @Override
            public void run() {
                httpPost(NetUtils.LOG_URL, j);
            }
        }).start();
    }

    public static JSONArray getRankFromSP(Context context) {
        SharedPreferences settings = context.getSharedPreferences("ranks", Context.MODE_PRIVATE);
        try {
            String p = settings.getString("data", null);
            if (p != null) {
                JSONArray jsa = new JSONArray(p);
                return jsa;
            }
        } catch (Exception e) {
            MLog.e("Get getRankFromSP", e);
        }
        return new JSONArray();
    }

    public static void getRank(final Context context, final CallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = httpGet(NetUtils.RANK_URL, "");
                try {
                    JSONObject j = new JSONObject(result);
                    MLog.d("Get Rank Result =" + j.toString());
                    String status = j.getString("status");
                    if (status.equals("0")) {
                        String p = j.getString("P");
                        JSONArray jsa = new JSONArray(Util.base64Decode(p));
                        SharedPreferences settings = context.getSharedPreferences("ranks", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("data", jsa.toString());
                        editor.commit();
                        MLog.d("Rank jsa Array =" + jsa.toString());
                        if (callBack != null) {
                            callBack.run(jsa.toString());
                        }
                    } else {
                        if (GlobalConfig.isDebug) {
                            Log.d(GlobalConfig.TAG, "Get Rank response ERR = " + result);
                        }
                    }
                } catch (Exception e) {
                    if (GlobalConfig.isDebug) {
                        e.printStackTrace();
                        Log.e(GlobalConfig.TAG, "Get Rank err =" + e.getMessage());
                    }
                }

            }
        }).start();
    }

    private static String httpPost(String reqUrl, String content) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(60000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(content);
            out.flush();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            if (GlobalConfig.isDebug) {
                e.printStackTrace();
                Log.e(GlobalConfig.TAG, "reqUrl =" + reqUrl + " err =" + e.getMessage());
            }
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
        return result;

    }

    public static String httpGet(String reqUrl, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = reqUrl + "?" + param;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();

            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            if (GlobalConfig.isDebug) {
                e.printStackTrace();
                Log.e(GlobalConfig.TAG, "reqUrl =" + reqUrl + " err =" + e.getMessage());
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
            }
        }
        return result;
    }
}
