package com.moogos.spacex.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Apple on 16/8/16.
 */
public class PreUtils {

    private static Context mContext;

    private static SharedPreferences preferences;

    private static final String sharePreKey = "qianghongbao";

    public static void init(Context context) {
        mContext = context;
        preferences = mContext.getSharedPreferences(sharePreKey, Context.MODE_PRIVATE);
    }

    /**
     * with no answer
     *
     * @param key
     * @param value
     */
    public static void savaData(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    /**
     * @param key
     * @param value
     */
    public static void saveData(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    /**
     * @param key
     * @param value
     */
    public static void saveData(String key, float value) {
        preferences.edit().putFloat(key, value).apply();
    }

    /**
     * @param key
     * @param value
     */
    public static void saveData(String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    /**
     * @param key
     * @param value
     */
    public static void saveData(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    /**
     * with answer
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean saveDataWithAnswer(String key, String value) {
        return preferences.edit().putString(key, value).commit();
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public static boolean saveDataWithAnswer(String key, int value) {
        return preferences.edit().putInt(key, value).commit();
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public static boolean saveDataWithAnswer(String key, float value) {
        return preferences.edit().putFloat(key, value).commit();
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public static boolean saveDataWithAnswer(String key, long value) {
        return preferences.edit().putLong(key, value).commit();
    }

    /**
     * @param key
     * @return
     */
    public static String getString(String key, String defaultStr) {
        return preferences.getString(key, defaultStr);
    }

    /**
     * @param key
     * @return
     */
    public static int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    /**
     * @param key
     * @return
     */
    public static boolean getBool(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    /**
     * @param key
     * @return
     */
    public static float getFloat(String key) {
        return preferences.getFloat(key, 0);
    }

    /**
     * @param key
     * @return
     */
    public static long getLong(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }
}
