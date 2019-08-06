package com.moogos.spacex.constants;

import com.google.gson.Gson;
import com.moogos.spacex.bean.RedMessage;
import com.moogos.spacex.core.MyApplication;
import com.moogos.spacex.core.SplashActivity;
import com.moogos.spacex.model.RedBagEntity;
import com.moogos.spacex.util.DeviceInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xupeng on 2017/12/5.
 */

public class ParamsHelper {

    /**
     * 登录
     *
     * @param password
     * @param phone
     */
    public static Map<String, String> loginAccountMap(String phone, String password) {
        String deviceId = DeviceInfo.getImeiId(MyApplication.getCurrentApplication());
        String ip = DeviceInfo.getMobileNetIP(MyApplication.getCurrentApplication());
        Map<String, String> map = new HashMap<>();
        map.put("user_phone", phone);
        map.put("user_pass", password);
        map.put("imei", deviceId);
        map.put("ip", ip);
        map.put("device_id", SplashActivity.deviceId);

        return map;
    }


    /**
     * 获取验证码
     *
     * @param phone
     */
    public static Map<String, String> getVerifyCodeMap(String phone) {

        Map<String, String> map = new HashMap<>();

        map.put("user_phone", phone);


        return map;
    }


    /**
     * 注册
     *
     * @param phone
     * @param password
     * @param veriCode
     * @param referrer
     */
    public static Map<String, String> registerForNetMap(String phone, String password, String veriCode, String referrer) {
        String imeiId = DeviceInfo.getImeiId(MyApplication.getCurrentApplication());
        String ip = DeviceInfo.getMobileNetIP(MyApplication.getCurrentApplication());
        Map<String, String> map = new HashMap<>();
        map.put("imei", imeiId);
        map.put("ip", ip);
        map.put("device_id", SplashActivity.deviceId);
        map.put("user_phone", phone);
        map.put("user_pass", password);
        map.put("sms_code", veriCode);
        map.put("inviter_id", referrer);


        return map;


    }

    /**
     * 跳过
     */
    public static Map<String, String> skipMap() {
        String deviceId = DeviceInfo.getImeiId(MyApplication.getCurrentApplication());
        String ip = DeviceInfo.getMobileNetIP(MyApplication.getCurrentApplication());
        Map<String, String> map = new HashMap<>();

        map.put("imei", deviceId);
        map.put("ip", ip);
        map.put("device_id", SplashActivity.deviceId);


        return map;


    }

    /**
     * 发送红包信息参数拼接json的base64加密
     *
     * @return
     */
    public static String sendRedMsgParam(RedMessage redBagEntity) {
        String imeiId = DeviceInfo.getImeiId(MyApplication.getCurrentApplication());
        String ip = DeviceInfo.getMobileNetIP(MyApplication.getCurrentApplication());
        String user_id = "";
        if (null != AppConfig.getConfig(MyApplication.getCurrentApplication()).getUserInfo()) {
            user_id = AppConfig.getConfig(MyApplication.getCurrentApplication()).getUserInfo().getUser_id();

        }
        HashMap map = new HashMap();
        map.put("user_id", user_id);
        map.put("ip", ip);
        map.put("imei", imeiId);
        map.put("device_id", SplashActivity.deviceId);
        map.put("hongbao_channel", redBagEntity.getChannel());
        map.put("hongbao_money", redBagEntity.getMoney());
        map.put("host_nick_name", redBagEntity.getNickName());


        String toJson = new Gson().toJson(map);
        return toJson;
    }

    /**
     * 注册设备号
     *
     * @return
     */
    public static Map<String, String> registerDeviceIdMap() {
        String imeiId = DeviceInfo.getImeiId(MyApplication.getCurrentApplication());
        String androidId = DeviceInfo.getAndroidId(MyApplication.getCurrentApplication());
        String simNumber = DeviceInfo.getSimNumber(MyApplication.getCurrentApplication());
        String localMacAddress = DeviceInfo.getLocalMacAddress(MyApplication.getCurrentApplication());
        String brand = DeviceInfo.getBrand();
        String model = DeviceInfo.getModel();

        Map<String, String> map = new HashMap<>();

        map.put("imei", imeiId);
        map.put("adid", androidId);
        map.put("imsi", simNumber);
        map.put("mac", localMacAddress);
        map.put("brand", brand);
        map.put("model", model);


        return map;


    }


    /**
     * 获取红包的数据
     *
     * @return
     */
    public static Map<String, String> getRedDataMap() {
        String user_id = "";
        if (null != AppConfig.getConfig(MyApplication.getCurrentApplication()).getUserInfo()) {
            user_id = AppConfig.getConfig(MyApplication.getCurrentApplication()).
                    getUserInfo().getUser_id();
        }
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("device_id", SplashActivity.deviceId);
        return map;


    }

    /**
     * 获取vip信息
     *
     * @return
     */
    public static Map<String, String> selectVipMsgMap() {
        String user_id = "";
        if (null != AppConfig.getConfig(MyApplication.getCurrentApplication()).getUserInfo()) {
            user_id = AppConfig.getConfig(MyApplication.getCurrentApplication()).
                    getUserInfo().getUser_id();
        }
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("device_id", SplashActivity.deviceId);
        return map;


    }


    /**
     * 领取vip
     *
     * @param vip_type
     * @param time
     * @return
     */
    public static Map<String, String> getVipFromNetMap(String vip_type, String time) {
        String user_id = "";
        if (null != AppConfig.getConfig(MyApplication.getCurrentApplication()).getUserInfo()) {
            user_id = AppConfig.getConfig(MyApplication.getCurrentApplication()).
                    getUserInfo().getUser_id();
        }
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("vip_type", vip_type);
        map.put("time", time);
        return map;


    }
}
