package com.moogos.spacex.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by xupeng on 2017/12/7.
 */

public class SharedPrefsUtils {

    // userInfo
    public static final String SP_FILE_NAME_USER = "spider_user_info";      // 用户信息文件名
    public static final String SP_KEY_USER_INFO = "userInfo";               // 用户信息Key
    // storeInfo
    public static final String SP_FILE_NAME_STORE = "spider_store_info";    // 店铺信息文件名
    public static final String SP_KEY_STORE_INFO = "storeInfo";             // 店铺信息Key
    // token add by JeyZheng
    public static final String SP_FILE_NAME_BASE = "spider_base";           // 基础文件配置信息（classify, example...）
    public static final String SP_KEY_BASE = "base";                        // base
    // search add by wangkang
    public static final String SP_FILE_NAME_SEARCH = "spider_search";       // 搜索文件名
    public static final String SP_KEY_SEARCH = "search";                    // 搜索Key
    // paper add by wangkang
    public static final String SP_FILE_NAME_PAPERINFO = "spider_paperinfo"; // 商品信息文件名
    public static final String SP_KEY_PAPERINFO = "paperinfo";              // 商品Key
    // ... example ...
    public static final String SP_FILE_NAME_OTHER_01 = "spider_other_01";
    public static final String SP_KEY_ORI_UGCS = "oriUgcs";
    // tips add by wangkang
    public static final String SP_FILE_NAME_TIPS = "spider_tips";           // 商品提示信息
    public static final String SP_KEY_TIPS = "tips";                        // tips

    private Context context;
    private String name;

    public SharedPrefsUtils(Context context, String name) {
        this.context = context;
        this.name = name;
    }

    /**
     * 根据key和预期的value类型获取value的值
     *
     * @param key
     * @param clazz
     * @return
     */
    public <T> T getValue(String key, Class<T> clazz) {
        if (context == null) {
            throw new RuntimeException("请先调用带有context，name参数的构造！");
        }
        SharedPreferences sp = this.context.getSharedPreferences(this.name, Context.MODE_PRIVATE);
        return getValue(key, clazz, sp);
    }

    /**
     * 针对复杂类型存储<对象>，序列化
     *
     * @param key
     * @param object
     */
    public void setObject(String key, Object object) {
        SharedPreferences sp = this.context.getSharedPreferences(this.name, Context.MODE_PRIVATE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {

            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, objectVal);
            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(String key, Class<T> clazz) {
        SharedPreferences sp = this.context.getSharedPreferences(this.name, Context.MODE_PRIVATE);
        if (sp.contains(key)) {
            String objectVal = sp.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (InvalidClassException e) {
                sp.edit().clear().commit();
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 对于外部不可见的过渡方法, 返序列化
     *
     * @param key
     * @param clazz
     * @param sp
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> T getValue(String key, Class<T> clazz, SharedPreferences sp) {
        T t;
        try {
            t = clazz.newInstance();

            if (t instanceof Integer) {
                return (T) Integer.valueOf(sp.getInt(key, 0));
            } else if (t instanceof String) {
                return (T) sp.getString(key, "");
            } else if (t instanceof Boolean) {
                return (T) Boolean.valueOf(sp.getBoolean(key, false));
            } else if (t instanceof Long) {
                return (T) Long.valueOf(sp.getLong(key, 0L));
            } else if (t instanceof Float) {
                return (T) Float.valueOf(sp.getFloat(key, 0L));
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
            Log.i("system", "类型输入错误或者复杂类型无法解析[" + e.getMessage() + "]");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.i("system", "类型输入错误或者复杂类型无法解析[" + e.getMessage() + "]");
        }
        Log.i("system", "无法找到" + key + "对应的值");
        return null;
    }

    /**
     * 清除share数据
     */
    public void clearSharedPreferences() {
        SharedPreferences sp = this.context.getSharedPreferences(this.name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }
}
