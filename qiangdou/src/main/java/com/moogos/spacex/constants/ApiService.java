package com.moogos.spacex.constants;

import com.moogos.spacex.bean.DeviceRegister;
import com.moogos.spacex.bean.MobileIp;
import com.moogos.spacex.bean.MyWealth;
import com.moogos.spacex.bean.Register;
import com.moogos.spacex.bean.Response;
import com.moogos.spacex.bean.Login;
import com.moogos.spacex.bean.Skip;
import com.moogos.spacex.bean.VipMsg;

import java.util.Map;

import retrofit.http.Body;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.QueryMap;
import rx.Observable;

/**
 * Created by xupeng on 2017/12/5.
 * * 1. 如果在@GET, @POST...上面加入的常量中包含HOST，会优先选择该HOST作为链接主机
 * 2. 如果不存在HOST，那么就会使用在实例化时的的HOST，作为链接主机
 */

public interface ApiService {

    /**
     * 获取城市列表
     *
     * @return
     */
    @GET("http://ip.chinaz.com/getip.aspx")
    Observable<MobileIp> getMobileIp();

    /**
     * 获取验证码
     *
     * @param param
     * @return
     */
    @GET(ServerApis.API_VERIFY_CODE)
    Observable<Response> getVerifyCode(@QueryMap Map<String, String> param);

    /**
     * 注册账号
     *
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(ServerApis.API_REGISTER)
    Observable<Register> registerForNet(@FieldMap Map<String, String> param);

    /**
     * 登录账号
     *
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(ServerApis.API_LOGIN_ACCOUNT)
    Observable<Login> loginAccount(@FieldMap Map<String, String> param);

    /**
     * 跳过
     *
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(ServerApis.API_SKIP)
    Observable<Skip> skip(@FieldMap Map<String, String> param);


    /**
     * 发送红包信息（抢红包）
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST(ServerApis.API_SEND_RED_MSG)
    Observable<Response> sendRedMessage(@Body String base64ToJson);


    /**
     * 注册设备号
     *
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(ServerApis.API_DEVICE_ID)
    Observable<DeviceRegister> registerDecviceId(@FieldMap Map<String, String> param);



    /**
     * 获取我的红包数据
     *
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(ServerApis.API_GET_RED_DATA)
    Observable<MyWealth> getMyRedData(@FieldMap Map<String, String> param);

    /**
     * 获取账户vip信息
     *
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(ServerApis.API_SELECT_VIP)
    Observable<VipMsg> getVipMsg(@FieldMap Map<String, String> param);


    /**
     * 领取vip
     *
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(ServerApis.API_GET_VIP)
    Observable<Response> getVipFromNet(@FieldMap Map<String, String> param);

}
