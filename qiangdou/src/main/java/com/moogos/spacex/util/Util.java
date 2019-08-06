package com.moogos.spacex.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.os.Parcelable;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.moogos.spacex.constants.GlobalConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Util {


    public static boolean DEBUG = true;
    public static String SERVER_URL_ADD = "http://51oneone.com/hongbao/item/add?";

    public static String SERVER_URL_START = "http://51oneone.com/hongbao/status/startservice?";
    public static String SERVER_URL_ACTIVATE = "http://51oneone.com/hongbao/status/active?";
    public static String SERVER_URL_CFG = "http://51oneone.com/hongbao/status/cfg?";
    public static String SERVER_URL_PING = "http://51oneone.com/hongbao/status/ping?";
    public static String SERVER_URL_LOGIN = "http://51oneone.com/hongbao/status/Login?";
    public static String SERVER_URL_PRELOGIN = "http://51oneone.com/hongbao/status/prelogin?";
    public static String INSTALL_PING_URL = "http://51oneone.com/hongbao/status/inst?";
    public static String DOWNLOAD_PING_URL = "http://51oneone.com/hongbao/status/dl?";
    public static String BADSTATE = "http://51oneone.com/hongbao/status/bad?";
    public static String ACTION_URL = "http://51oneone.com/hongbao/status/click?";//clkid=xxx&aid=xxx
    public static String ACTION_MONITOR_URL = "http://51oneone.com/hongbao/status/monitor?";//eve=1

    public static String PACKAGE_URL = "http://51oneone.com/hongbao/dlist?";

    public static String PAID_URL = "http://51oneone.com/hongbao/preorder?";
    public static String VIP_APK_URL = "http://c3.moogos.com/sdk/app/vip/com.example.veryip.apk?";
    public static String UPDATE_URL = "http://51oneone.com/hongbao/update/version.json";
    public static String UPDATE_URL_TEST = "http://192.168.2.211:8099/update/version.json";
    public static String SERVER_EXCEPTION = "http://51oneone.com/hongbao/status/error?";


    public static String ITEMS_QUERY_URL = "http://51oneone.com/hongbao/items/query?";
    public static String RANK_URL = "http://51oneone.com/hongbao/rank/query";

    public static String SHARE_URL = "http://51oneone.com/hongbao/hongb?pkg=so08";


    public static final String ACTION_ID_RESET_STATE = "reset_state";
    public static final String ACTION_ID_DONE_SETTINGS = "done_settings";

    public static final String ACTION_START_SHARE_SUCCESS = "to_share_ok";
    public static final String ACTION_START_SHARE_FAIL = "to_share_fail";

    public static SimpleDateFormat MINE_BAG_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat PING_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
    ;

    public static String formatDate(long time) {
        return MINE_BAG_DATEFORMAT.format(time);
    }

    static String pingDate = "";

    private static String standard_base64_code = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    private static String self_base64_code = "QrfzshiqxbpXZGuPV4_HL62on8YwSWITAc-eyO3djvKgNa5mE7tD10BFCMkJUl9R=";
    //红包
    private static String red_base64_code = "0KajD7AZcF2QnPr5fwiHRNygmupUTIXx69BWb-hMCGJo_V8Eskz1YdvL34letqSO";

    private static Map<Character, Character> b64Map;
    private static Map<Character, Character> de_b64Map;

//    private static Map<Character, Character> red_b64Map;    //自己的红包base64加密
//    private static List<Character> red_b64;    //自己的红包base64加密

    static {
        initMap();
    }

    private static void initMap() {
        b64Map = new HashMap<Character, Character>();
        de_b64Map = new HashMap<Character, Character>();
        //red_b64Map = new HashMap<Character, Character>();
        //red_b64 = new LinkedList<Character>();
        char[] std = standard_base64_code.toCharArray();
        char[] self = self_base64_code.toCharArray();
        //char[] red64 = red_base64_code.toCharArray();
        //char[] red64 = red_base64_code.toCharArray();
        for (int i = 0; i < self.length; i++) {
            char k = self[i];
            char v = std[i];
            b64Map.put(k, v);
        }

        for (int i = 0; i < std.length; i++) {
            char k = std[i];
            char v = self[i];
            de_b64Map.put(k, v);
        }

//        for (int i = 0; i < std.length; i++) {
//            char k = std[i];
//            char v = red64[i];
//            red_b64Map.put(k, v);
//        }

//        for (int i = 0; i < red64.length; i++) {
//            char k = red64[i];
//            red_b64.add(k);
//        }
    }

    public static String base64Encode(String data) {
        try {
            byte[] v1 = Base64.encode(data.getBytes(), Base64.NO_WRAP);
            String v = new String(v1, "UTF-8");
            MLog.d("before encode = " + data + " encodeToString = " + v);
            char[] v_char = v.toCharArray();
            for (int i = 0; i < v_char.length; i++) {
                char t = v_char[i];
                v_char[i] = de_b64Map.get(t);
            }
            return new String(v_char);

        } catch (Exception e) {
            MLog.e("base64Encode data=" + data, e);
            return null;
        }
    }

    /**
     * 红包的base64加密
     *
     * @return
     */
//    public static String Redbase64Encode(String data) {
//        try {
//            byte[] v1 = Base64.encode(data.getBytes(), Base64.NO_WRAP);
//            String v = new String(v1, "UTF-8");
//            MLog.d("before encode = " + data + " encodeToString = " + v);
//            char[] v_char = v.toCharArray();
//            for (int i = 0; i < v_char.length; i++) {
//                char t = v_char[i];
//                v_char[i] = red_b64.get(t);
//            }
//            return new String(v_char);
//
//        } catch (Exception e) {
//            MLog.e("base64Encode data=" + data, e);
//            return null;
//        }
//    }
    public static String base64Decode(String en_string) {
        try {
            char[] vv = en_string.toCharArray();
            for (int i = 0; i < en_string.length(); i++) {
                char t = vv[i];
                vv[i] = b64Map.get(t);
            }
            String d = new String(vv);
            MLog.d("base 64 deocode = " + d);
            return BZBase64Decode(d);
        } catch (Exception e) {
            MLog.e("base64 decode failed", e);
        }
        return null;
    }

    public static long getCurTime() {
        return System.currentTimeMillis() / 1000;
    }

    public static long getCurTimeMillis() {
        return System.currentTimeMillis();
    }

    public static String BZBase64Decode(String en_String) {
        byte[] v = null;
        try {
            v = Base64.decode(en_String, Base64.NO_WRAP);
        } catch (Exception e) {
            if (GlobalConfig.isDebug) {
                e.printStackTrace();
                Log.e(GlobalConfig.TAG, "BZBase64Decode error =" + e.getMessage());
            }
        }
        try {
            return new String(v, "utf-8");
        } catch (Exception e) {
            return new String(v);
        }
    }

    public static void setPingDate(Context context) {
        String newDate = PING_DATEFORMAT.format(System.currentTimeMillis());
        SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
        Editor editor = settings.edit();
        editor.putString("pingDate", newDate);
        editor.commit();
        pingDate = newDate;
    }

    public static String getPingDate(Context context) {
        if (!"".equals(pingDate)) {
            return pingDate;
        }
        SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
        pingDate = settings.getString("pingDate", "2016-01-01");
        return pingDate;
    }
//
//	public static void setConfig(Context context, JSONObject cfg) {
//		if(cfg == null){
//			return;
//		}
//		SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//		Editor editor = settings.edit();
//		editor.putString("config", cfg.toString());
//		editor.commit();
//		config = cfg;
//
//	}

//	private static JSONObject config = new JSONObject();
//
//	public static JSONObject getConfig(Context context) {
//		if ("{}".equals(config.toString())) {
//			SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//			try {
//				config = new JSONObject(settings.getString("config", "{}"));
//			}catch (Exception e){
//				config = new JSONObject();
//			}
//		}
//		return config;
//	}
//	public static void setThankSlogan(Context context, String words) {
//		SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//		Editor editor = settings.edit();
//		editor.putString("thankSlogan", words);
//		editor.commit();
//		myWords = words;
//	}


//	private static String myWords = "";
//
//	public static String getThankSlogan(Context context) {
//		if (isMyWordsOn != null && !isMyWordsOn.get()) {
//			return "谢谢老板";
//		}
//		if (myWords == null || myWords.equals("")) {
//			SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//			myWords = settings.getString("thankSlogan", "谢谢老板");
//		}
//		return myWords;
//	}

//	static long qiangDelayed = -1;
//	public static void setQiangDelayed(Context context, long delayed) {
//
//		SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//		Editor editor = settings.edit();
//		editor.putLong("qiangDelayed", delayed);
//		editor.commit();
//		qiangDelayed = delayed;
//	}

//	public static long getQiangDelayed(Context context) {
//		if(qiangDelayed == -1){
//			SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//			qiangDelayed = settings.getLong("qiangDelayed", 0);
//		}
//
//		return qiangDelayed;
//	}

//	static long sendDelayed = -1;
//	public static void setSendDelayed(Context context, long delayed) {
//		SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//		Editor editor = settings.edit();
//		editor.putLong("sendDelayed", delayed);
//		editor.commit();
//		sendDelayed = delayed;
//	}
//
//	public static long getSendDelayed(Context context) {
//		if(sendDelayed == -1) {
//			SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//			sendDelayed = settings.getLong("sendDelayed", 0);
//		}
//		return sendDelayed;
//	}

//	private static AtomicBoolean isAutoReply = null;
//
//	public static boolean isAutoReply(Context context) {
//		if (isAutoReply == null) {
//			isAutoReply = new AtomicBoolean();
//			SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//			isAutoReply.set(settings.getBoolean("autoReply", true));
//		}
//		return isAutoReply.get();
//
//	}
//
//	public static void setAutoReply(Context context, boolean autoReply) {
//		SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//		Editor editor = settings.edit();
//		editor.putBoolean("autoReply", autoReply);
//		editor.commit();
//		if (isAutoReply == null) {
//			isAutoReply = new AtomicBoolean();
//		}
//		isAutoReply.set(autoReply);
//
//	}
//
//	private static AtomicBoolean isMyWordsOn = null;
//
//	public static boolean isMyWordsOn(Context context) {
//		if (isMyWordsOn == null) {
//			isMyWordsOn = new AtomicBoolean();
//			SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//			isMyWordsOn.set(settings.getBoolean("myWordsOn", false));
//		}
//		return isMyWordsOn.get();
//
//	}
//
//	public static void setMyWordsOn(Context context, boolean myWordsOn) {
//		SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//		Editor editor = settings.edit();
//		editor.putBoolean("myWordsOn", myWordsOn);
//		editor.commit();
//		if (isMyWordsOn == null) {
//			isMyWordsOn = new AtomicBoolean();
//		}
//		isMyWordsOn.set(myWordsOn);
//	}

    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
//	public static void setDisguard(Context context, boolean disguard) {
//		SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//		Editor editor = settings.edit();
//		editor.putBoolean("disguard", disguard);
//		editor.commit();
//
//	}
//
//	public static boolean isVoice(Context context) {
//		SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//		return settings.getBoolean("isVoice", true);
//	}
//
//	public static void setVoice(Context context, boolean isVoice) {
//		SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//		Editor editor = settings.edit();
//		editor.putBoolean("isVoice", isVoice);
//		editor.commit();
//
//	}
//	public static boolean isCloseAd(Context context) {
//		SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//		return settings.getBoolean("closeAd", false);
//	}
//
//	public static void setCloseAd(Context context, boolean isVoice) {
//		SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//		Editor editor = settings.edit();
//		editor.putBoolean("closeAd", isVoice);
//		editor.commit();
//
//	}

//
//	public static AtomicBoolean isOpen = null;
//	public static AtomicBoolean isQQOpen = null;
//
//	public static boolean isOpen(Context context) {
//		if (isOpen == null) {
//			isOpen = new AtomicBoolean();
//			SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//			isOpen.set(settings.getBoolean("isOpen", true));
//		}
//		return isOpen.get();
//	}
//
//	public static void setOpen(Context context, boolean enable) {
//		if (isOpen == null) {
//			isOpen = new AtomicBoolean();
//		}
//		isOpen.set(enable);
//		SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//		Editor editor = settings.edit();
//		editor.putBoolean("isOpen", enable);
//		editor.commit();
//	}
//
//	public static boolean isQQOpen(Context context) {
//		if (isQQOpen == null) {
//			isQQOpen = new AtomicBoolean();
//			SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//			isQQOpen.set(settings.getBoolean("isQQOpen", true));
//		}
//		return isQQOpen.get();
//
//	}
//
//	public static void setQQOpen(Context context, boolean enable) {
//		if (isQQOpen == null) {
//			isQQOpen = new AtomicBoolean();
//		}
//		isQQOpen.set(enable);
//		SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//		Editor editor = settings.edit();
//		editor.putBoolean("isQQOpen", enable);
//		editor.commit();
//	}

//	public static boolean isSwitchedOnSent(Context context) {
//		SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//		return settings.getBoolean("switchedSent", false);
//	}
//
//	public static void setSwitchedOnSent(Context context, boolean isSent) {
//		SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//		Editor editor = settings.edit();
//		editor.putBoolean("switchedSent", isSent);
//		editor.commit();
//	}

//	public static boolean seriviceIsON = true;
//
//	public static boolean isAccessibilityEnabled(final Context context) {
//		if (!seriviceIsON) {
//			return false;
//		}
//		int accessibilityEnabled = 0;
//		final String LIGHTFLOW_ACCESSIBILITY_SERVICE = context.getPackageName() + "/"
//				+ QiangHongBaoService.class.getName();
//		try {
//			accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
//					android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
//			//Log.d("ACCESSIBILITY: " + accessibilityEnabled);
//
//		} catch (SettingNotFoundException e) {
//			//Log.d("Error finding setting, default accessibility to not found: " + e.getMessage());
//		}
//
//		TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
//
//		if (accessibilityEnabled == 1) {
//		//	Log.d("***ACCESSIBILIY IS ENABLED***: ");
//
//			String settingValue = Settings.Secure.getString(context.getContentResolver(),
//					Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
//		//	Log.d("Setting: " + settingValue);
//			if (settingValue != null) {
//				TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
//				splitter.setString(settingValue);
//				while (splitter.hasNext()) {
//					String accessabilityService = splitter.next();
//				//	Log.d("Setting: " + accessabilityService);
//					if (accessabilityService.equalsIgnoreCase(LIGHTFLOW_ACCESSIBILITY_SERVICE)) {
//						//Log.d("We've found the correct setting - accessibility is switched on! ");
//
//
//						return true;
//					}
//				}
//			}
//
//			//Log.d("***END***");
//		} else {
//			//Log.d("***ACCESSIBILIY IS DISABLED***");
//		}
//		return false;
//	}


    private static final Proxy mainProxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80)); //
    private static final Proxy otherProxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("10.0.0.200", 80));

    public static HttpURLConnection getHttpConnection(Context context, String reqUrl, int connTimeout, int readTimeout)
            throws IOException {
        URL url = new URL(reqUrl);
        HttpURLConnection conn = null;

        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = conManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = conManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifi != null && wifi.isAvailable()) {
            //Log.d("", "WIFI is available");
            conn = (HttpURLConnection) url.openConnection();
        } else if (mobile != null && mobile.isAvailable()) {
            String apn = mobile.getExtraInfo();
            if (apn != null) {
                apn = apn.toLowerCase();
            } else {
                apn = "";
            }
            //	Log.d("current APN", apn);

            if (apn.startsWith("cmwap") || apn.startsWith("uniwap") || apn.startsWith("3gwap")) {
                conn = (HttpURLConnection) url.openConnection(mainProxy);
            } else if (apn.startsWith("ctwap")) {
                conn = (HttpURLConnection) url.openConnection(otherProxy);
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
        } else {// @fix while not in wifi and mobile state
            conn = (HttpURLConnection) url.openConnection();
        }
        conn.setConnectTimeout(connTimeout);
        conn.setReadTimeout(readTimeout);

        return conn;
    }

    public static boolean httpPingForceInMain(final Context context, final String reqUrl) {
        try {

            HttpURLConnection conn = getHttpConnection(context, reqUrl, 10000, 10000);
            conn.connect();
            //Log.d("httpPing_url:" + reqUrl + " status:" + conn.getResponseCode());
            conn.disconnect();
            return true;
        } catch (Exception e) {
            //Log.d("httpPing_url:" + reqUrl + "  exception:" + e);
            return false;
        }
    }

    public static boolean httpPing(final Context context, final String reqUrl) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            try {

                HttpURLConnection conn = getHttpConnection(context, reqUrl, 10000, 10000);
                conn.connect();
                //	Log.d("httpPing_url_main:" + reqUrl + " status:" + conn.getResponseCode());
                conn.disconnect();
                return true;
            } catch (Exception e) {
                //Log.w("httpPing_url_main:" + reqUrl + "  exception:" + e);
                return false;
            }
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        HttpURLConnection conn = getHttpConnection(context, reqUrl, 10000, 10000);
                        conn.connect();
                        //	Log.d("httpPing_url_new:" + reqUrl + " status:" + conn.getResponseCode());
                        conn.disconnect();

                    } catch (Exception e) {
                        //	Log.w("httpPing_url_new:" + reqUrl + "  exception:" + e);

                    }
                }
            }).start();
        }

        return true;
    }

    public static String getAndroidId(Context context) {
        return android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
    }


    public static String getHttpData(Context context, String reqUrl) {
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = null;

            ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobile = conManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi = conManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (wifi != null && wifi.isAvailable()) {
                conn = (HttpURLConnection) url.openConnection();
            } else if (mobile != null && mobile.isAvailable()) {
                String apn = mobile.getExtraInfo();
                if (apn != null) {
                    apn = apn.toLowerCase();
                } else {
                    apn = "";
                }

                if (apn.startsWith("cmwap") || apn.startsWith("uniwap") || apn.startsWith("3gwap")) {
                    conn = (HttpURLConnection) url.openConnection(new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80)));
                } else if (apn.startsWith("ctwap")) {
                    conn = (HttpURLConnection) url.openConnection(new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("10.0.0.200", 80)));
                } else {
                    conn = (HttpURLConnection) url.openConnection();
                }
            } else {// @fix while not in wifi and mobile state
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(20000);
            conn.connect();
            byte[] buffer = new byte[2048];
            InputStream is = conn.getInputStream();

            StringBuilder sb = new StringBuilder();
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, len, "utf-8"));
            }

            return sb.toString();
        } catch (Exception e) {
//			e.printStackTrace();
            return "{}";
        }
    }

    public static String channel = "";

    public static String getMeteDataByKey(Context mContext, String key) {
        if ("app_channel".equals(key)) {
            if (!TextUtils.isEmpty(channel)) {
                return channel;
            }
        }
        try {
            ApplicationInfo appInfo = mContext.getApplicationContext().getPackageManager()
                    .getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);

            if (appInfo.metaData != null) {
                Object tmp = appInfo.metaData.get(key);
                if (tmp != null) {
                    String tmpValue = tmp.toString();
                    if (tmpValue.startsWith("<a>")) {
                        if ("app_channel".equals(key)) {
                            channel = tmpValue.substring(3);
                        }
                        return tmpValue.substring(3);
                    } else {
                        if ("app_channel".equals(key)) {
                            channel = tmpValue;
                        }
                        return tmpValue;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            //  Log.d("e:"+e);
        }
        return "";
    }

    public static String getCommonParam(Context context) {
        double localVer = 1.0;

        String model = "default";
        String p_ver = "1.0.0";
        boolean is_wifi = false;
        String pkg = "";

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            localVer = Double.parseDouble(packInfo.versionName);
            pkg = packInfo.packageName;

            model = URLEncoder.encode(Build.MODEL, "UTF-8");
            p_ver = URLEncoder.encode(Build.VERSION.RELEASE, "UTF-8");
            is_wifi = isWifi(context);

        } catch (Exception e) {
        }


        return "aid=" + getAndroidId(context) + "&ch=" + getMeteDataByKey(context, "app_channel") + "&ver=" + localVer + "&model=" + model + "&pkg="
                + pkg + "&p_ver=" + p_ver + "&is_wifi=" + is_wifi;
    }


//    static String TAG = "screenTAG";
//    private static BroadcastReceiver screenReceiver =  null;

//    public static void registerScreenOnAndOffCallback(Context context ){
//        unregisterScreenOnAndOffCallback(context);
//        Log.d("registerScreenOnAndOffCallback:");
//
//        BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(final Context context, final Intent intent) {
//                Log.d(TAG, "onReceive");
//                String action = intent.getAction();
//
//                if (Intent.ACTION_SCREEN_ON.equals(action)) {
//                    Log.d(TAG, "screen on");
//                    try {
//                        KeyguardManager mKeyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
//                        if (mKeyguardManager.inKeyguardRestrictedInputMode() && Util.isDisguard(context)) {
//                            KeyguardManager.KeyguardLock mKeyguardLock = mKeyguardManager.newKeyguardLock("");
//                            mKeyguardLock.disableKeyguard();
//
//                            Log.d(TAG,"TAG:disableKeyguard");
//                        }
//                    } catch (Exception e) {
//                    }
//
//                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
//                    Log.d(TAG, "screen off");
//                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
//                    Log.d(TAG, "screen unlock");
//                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
//                    Log.i(TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
//                }
//            }
//        };
//        screenReceiver = mBatInfoReceiver;
//        final IntentFilter filter = new IntentFilter();
//        // 屏幕灭屏广播
//        filter.addAction(Intent.ACTION_SCREEN_OFF);
//        // 屏幕亮屏广播
//        filter.addAction(Intent.ACTION_SCREEN_ON);
//        // 屏幕解锁广播
//        filter.addAction(Intent.ACTION_USER_PRESENT);
//        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
//        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
//        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
//        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//
//        Log.d(TAG, "registerReceiver");
//        context.registerReceiver(screenReceiver, filter);
//    }
//    public static void unregisterScreenOnAndOffCallback(Context context){
//        if(screenReceiver!=null){
//            try {
//                context.unregisterReceiver(screenReceiver);
//                screenReceiver = null;
//            }catch(Exception e){
//                screenReceiver = null;
//            }
//        }
//        Log.d("unregisterScreenOnAndOffCallback:");
//
//    }


    static PowerManager.WakeLock mWakeLock;

    public static void doAlwaysAwake(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "");
        mWakeLock.acquire();
    }

    public static void cancelAlwaysAwake() {
        if (mWakeLock != null) {
            try {
                mWakeLock.release();
                mWakeLock = null;
            } catch (Exception e) {

            }
        }
    }

    public static String getVersion(Context mContext) {
        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            return packInfo.versionName;
        } catch (Exception e) {

        }
        return "1.0";
    }

    public static void sendPackageList(Context context) {
        if (!isPkgSent(context)) {
            StringBuilder sb = new StringBuilder();
            List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < packages.size(); i++) {
                PackageInfo packageInfo = packages.get(i);
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    sb.append(packageInfo.packageName);
                    sb.append(",");
                }
            }
            httpPing(context, PACKAGE_URL + getCommonParam(context) + "&app=" + sb);
            setPkgSent(context, true);
        }


    }

//	private static boolean isFirst = true;
//	public static boolean isFirst(Context context) {
//		if(isFirst){
//			SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//			isFirst = settings.getBoolean("isFirst", true);
//		}
//		return isFirst;
//	}
//
//	public static void setIsFirst(Context context, boolean isFirst) {
//		SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
//		Editor editor = settings.edit();
//		editor.putBoolean("isFirst", isFirst);
//		editor.commit();
//	}


    private static boolean isPkgSent = false;

    private static boolean isPkgSent(Context context) {
        if (!isPkgSent) {
            SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
            isPkgSent = settings.getBoolean("pkgSent", false);
        }
        return isPkgSent;
    }

    private static void setPkgSent(Context context, boolean enable) {
        SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
        Editor editor = settings.edit();
        editor.putBoolean("pkgSent", enable);
        editor.commit();
    }


    public static boolean isShared(Context context) {
        SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
        return settings.getBoolean("isShared", false);
    }

    public static void setShared(Context context, boolean enable) {
        SharedPreferences settings = context.getSharedPreferences("qianghongbao", Context.MODE_PRIVATE);
        Editor editor = settings.edit();
        editor.putBoolean("isShared", enable);
        editor.commit();
    }

    public static Activity from = null;

//    public static void share(IWXAPI api, Activity context) {
//        from = context;
//        WXWebpageObject webpage = new WXWebpageObject();
//        webpage.webpageUrl = Util.SHARE_URL;
//        WXMediaMessage msg = new WXMediaMessage(webpage);
//        msg.title = "神器帮你抢红包啦！";
//        msg.description = "锁屏、睡觉都能自动抢红包的神器，QQ、微信红包一个也不错过。";
//        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
//        msg.setThumbImage(thumb);
//
//
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = "webpage" + System.currentTimeMillis();
//        req.message = msg;
//        req.scene = SendMessageToWX.Req.WXSceneSession;
//        api.sendReq(req);
//        MLog.d("Share Message Api Activity");
//
//        boolean ret = api.sendReq(req);
//        //Log.d("hongbao", "send to wx . ret is - " + ret);
//    }

    public static void share(Context context) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "强手抢红包，一款功能性强大的抢红包软件，您值得拥有，下载链接：" + NetUtils.DOWN_URL);
        shareIntent.setType("text/plain");

        //设置分享列表的标题，并且每次都显示分享列表
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    public static void shareDetail(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        // 查询所有可以分享的Activity
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (!resInfo.isEmpty()) {
            List<Intent> targetedShareIntents = new ArrayList<Intent>();
            for (ResolveInfo info : resInfo) {
                Intent targeted = new Intent(Intent.ACTION_SEND);
                targeted.setType("text/plain");
                ActivityInfo activityInfo = info.activityInfo;
                Log.v("logcat", "packageName=" + activityInfo.packageName + "Name=" + activityInfo.name);
                // 分享出去的内容
                targeted.putExtra(Intent.EXTRA_TEXT, "强手抢红包，一款功能性强大的抢红包软件，您值得拥有，下载链接：" + NetUtils.DOWN_URL);
                // 分享出去的标题
                targeted.putExtra(Intent.EXTRA_SUBJECT, "主题");
                targeted.setPackage(activityInfo.packageName);
                targeted.setClassName(activityInfo.packageName, info.activityInfo.name);
                PackageManager pm = context.getPackageManager();
                // 微信有2个怎么区分-。- 朋友圈还有微信
                if (info.activityInfo.applicationInfo.loadLabel(pm).toString().equals("微信") && !info.loadLabel(pm).toString().contains("添加到微信收藏")) {
                    targetedShareIntents.add(targeted);
                }
                if (info.activityInfo.applicationInfo.loadLabel(pm).toString().equals("QQ") && !info.loadLabel(pm).toString().contains("发送到我的电脑")
                        && !info.loadLabel(pm).toString().contains("保存到QQ收藏")) {
                    targetedShareIntents.add(targeted);
                }
                if (info.activityInfo.applicationInfo.loadLabel(pm).toString().equals("微博")) {
                    targetedShareIntents.add(targeted);
                }
            }
            // 选择分享时的标题
            if (targetedShareIntents.size() == 0)
                return;
            Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "选择分享");
            if (chooserIntent == null) {
                return;
            }
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
            try {
                context.startActivity(chooserIntent);
            } catch (android.content.ActivityNotFoundException ex) {

                Toast.makeText(context, "找不到该分享应用组件", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    private static DisplayMetrics dm = null;

    public static DisplayMetrics getDisplayMetrics(Context cx) {
        if (dm == null) {
            dm = new DisplayMetrics();
            dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        }
        return dm;
    }

    public static boolean isInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    public static void showExpiredVipDialog(Context context) {
        final AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
        builder2.setTitle("会员不可用了");
        builder2.setMessage("原因：会员已经过期");
        builder2.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        Dialog noticeDialog2 = builder2.create();
        noticeDialog2.show();
    }

    public static void listALl(int a, AccessibilityNodeInfo nodeInfo) {
        MLog.d("idx=" + a + " cls=" + nodeInfo.getClassName() + " txt=" + nodeInfo.getText() + " c=" + nodeInfo.getChildCount() + " e=" + nodeInfo.getViewIdResourceName());
        if (nodeInfo.getChildCount() > 0) {
            for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                listALl(i, nodeInfo.getChild(i));
            }
        }
    }

    public static void installApk(Context mContext, String filePath) {
        File apkfile = new File(filePath);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);

    }
}
