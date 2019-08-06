package com.moogos.spacex.core;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jesgoo.sdk.AdSize;
import com.jesgoo.sdk.AdView;
import com.jesgoo.sdk.AdvancedApi;
import com.mogo.space.R;
import com.moogos.spacex.bean.DeviceRegister;
import com.moogos.spacex.bean.MobileIp;
import com.moogos.spacex.bean.VipMsg;
import com.moogos.spacex.constants.ApiServiceFactory;
import com.moogos.spacex.constants.AppConfig;
import com.moogos.spacex.constants.GlobalConfig;
import com.moogos.spacex.constants.HttpUrls;
import com.moogos.spacex.constants.ParamsHelper;
import com.moogos.spacex.core.newfun.BaseActivity;
import com.moogos.spacex.core.newfun.GrabRedActivity;
import com.moogos.spacex.core.newfun.LoginActivity;
import com.moogos.spacex.util.MLog;
import com.moogos.spacex.util.Util;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends BaseActivity {
    Handler mainHandler = new Handler();
    AdvancedApi.AdvancedApiListener advancedApiListener;

    TextView timed;
    int time = 5;
    boolean isClick = false;
    public static boolean splashOK = false;
    Runnable closeRunnable = new Runnable() {
        @Override
        public void run() {
            time--;
            timed.setText(time + "s");
            if (!isClick) {
                if (time <= 0) {
                    goMainActivity();
                } else {
                    mainHandler.postDelayed(this, 1000);
                }
            }
        }
    };

    private void goMainActivity() {
        //已经登录
        if (AppConfig.getConfig(this).isLogin()) {
            //超过7天重新登录
            if (AppConfig.getConfig(this).getLoginTime(this) == 0 ||
                    Calendar.getInstance().getTimeInMillis() < AppConfig.getConfig(this).getLoginTime(this) + 604800000) {
                //自动登录
                // TODO: 2018/1/23 进行vip查询
                requestVipMsg();

                Intent intent = new Intent(this, GrabRedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

        } else {      //未登录
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        splashOK = true;
    }

    /**
     * 请求网络查询vip信息
     */
    private void requestVipMsg() {
        ApiServiceFactory.getSpiderApiService(HttpUrls.HTTP_BASE_URL)
                .getVipMsg(ParamsHelper.selectVipMsgMap())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VipMsg>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(VipMsg vipMsg) {
                        if (vipMsg.getCode() == 200 && null != vipMsg) {
                            //保存vip的信息
                            AppConfig.vip_type = vipMsg.getResult().getVip_type();
                            AppConfig.sum_inviter = vipMsg.getResult().getSum_inviter();
                            AppConfig.get_record = vipMsg.getResult().getGet_record();
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdView.preLoad(this);
        // 去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        final ImageView imageView = (ImageView) findViewById(R.id.splash_img);

        timed = (TextView) findViewById(R.id.splash_time);
        final DisplayMetrics dp = Util.getDisplayMetrics(this);
        final int phone_width = dp.widthPixels;
        final int phone_height = dp.heightPixels;
        final float phone_rate = phone_height / (float) phone_width;
        advancedApiListener = new AdvancedApi.AdvancedApiListener() {
            @Override
            public void onAdReady(final AdvancedApi api) {
                String imgUrl = api.getImgUrl();
                try {
                    InputStream is = (InputStream) new URL(imgUrl).getContent();
                    final Drawable d = Drawable.createFromStream(is, "splash");
                    final int h = d.getIntrinsicHeight();
                    final int w = d.getIntrinsicWidth();
                    final float img_rate = h / (float) w;
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageDrawable(d);
                            if (img_rate < phone_rate && img_rate < 1) {
                                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            } else {
                                if (h > phone_height || w > phone_width) {
                                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                } else {
                                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                }
                            }
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View v) {
                                    isClick = true;
                                    api.setAdClick(v.getContext());
                                }
                            });

                        }
                    });
                } catch (Exception e) {
                    MLog.e("get Img error url =" + imgUrl, e);
                }
            }

            @Override
            public void onAdFailed(JSONObject jsonObject) {
                MLog.d("get onAdFailed =" + jsonObject.toString());
            }

            @Override
            public void onAdLPFinish(JSONObject jsonObject) {
                goMainActivity();
                MLog.d("get onAdLPFinish =" + jsonObject.toString());
            }
        };
        AdvancedApi advancedApi = new AdvancedApi(this, GlobalConfig.Splash_ADSLOT, AdSize.Initial, advancedApiListener);
        mainHandler.postDelayed(closeRunnable, 1000);


        getMobileNetIP();
        registerDeviceId();

    }

    public static String skip_times = "0";    //试玩次数
    public static String deviceId;    //试玩次数

    /**
     * 注册设备号（服务端返回的deviceId存在本地）
     */
    private void registerDeviceId() {
        ApiServiceFactory.getSpiderApiService(HttpUrls.HTTP_BASE_URL)
                .registerDecviceId(ParamsHelper.registerDeviceIdMap())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DeviceRegister>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DeviceRegister deviceRegister) {
                        //成功返回抢到的红包次数
                        if (deviceRegister.getCode() == 200 && null != deviceRegister) {
                            //记录试玩的次数
                            skip_times = deviceRegister.getResult().getSkip_times();
                            deviceId = deviceRegister.getResult().getDevice_id();
                        }
                    }
                });
    }


    /**
     * 获取手机的外网ip
     */
    public static String mobileIpStr = "";

    public void getMobileNetIP() {
        ApiServiceFactory.getSpiderApiService(HttpUrls.HTTP_FOR_IP)
                .getMobileIp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MobileIp>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MobileIp mobileIp) {
                        if (null != mobileIp) {
                            mobileIpStr = mobileIp.getIp();
                        }
                    }
                });


    }

}
