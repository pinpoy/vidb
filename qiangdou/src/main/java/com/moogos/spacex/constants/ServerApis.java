package com.moogos.spacex.constants;

/**
 * Created by xupeng on 2017/12/5.
 */

public interface ServerApis {

    String API_CITY = "cityList.html";
    String API_VERIFY_CODE = "user/sms_send/";
    String API_REGISTER = "user/register";
    String API_LOGIN_ACCOUNT = "user/Login";
    String API_SKIP = "user/skip";//跳过不注册
    String API_SEND_RED_MSG = "hongbao/rap_hongbao";//抢红包---发送红包信息
    String API_DEVICE_ID = "user/get_device_info";//设备
    String API_GET_RED_DATA = "hongbao/record";//设备
    String API_SELECT_VIP = "vip/select_vip";//查询vip信息
    String API_GET_VIP = "vip/get_vip";//领取vip
}
