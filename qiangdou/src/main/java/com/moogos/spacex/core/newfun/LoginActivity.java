package com.moogos.spacex.core.newfun;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jesgoo.sdk.AdSize;
import com.jesgoo.sdk.AdView;
import com.mogo.space.R;
import com.moogos.spacex.bean.Login;
import com.moogos.spacex.bean.Skip;
import com.moogos.spacex.constants.ApiServiceFactory;
import com.moogos.spacex.constants.AppConfig;
import com.moogos.spacex.constants.HttpUrls;
import com.moogos.spacex.constants.ParamsHelper;
import com.moogos.spacex.util.DeviceHelper;
import com.moogos.spacex.util.StringUtil;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 登录页面
 * Created by xupeng on 2017/12/5.
 */

public class LoginActivity extends BaseActivity {


    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_password)
    EditText etPassword;
    private String phone;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //网络请求的方法
        //requestData();

        initAdView();
    }

    private AdView bannerAdView;
    private RelativeLayout rl;
    private ImageView ivClose;

    private void initAdView() {
        rl = (RelativeLayout) findViewById(R.id.ad_parent);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        bannerAdView = new AdView(this, AdSize.Banner, "sc1be011");
        bannerAdView.setListener(new AdView.AdViewListener() {
            @Override
            public void onAdReady(AdView adView) {

            }

            @Override
            public void onAdShow() {
                ivClose.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClick() {

            }

            @Override
            public void onAdFailed(String s) {

            }

            @Override
            public void onEvent(String s) {

            }
        });
        rl.addView(bannerAdView);
//        bannerAdView.setBackgroundColor(getResources().getColor(android.R.color.white));
//        bannerAdView.CloseBannerCarousel();
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivClose.setVisibility(View.GONE);
                rl.setVisibility(View.GONE);

            }
        });


    }


    @OnClick({R.id.iv_login, R.id.tv_register, R.id.tv_forget_pwd, R.id.iv_skip})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_login:     //登录
                phone = etPhone.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                if (StringUtil.checkEmpty(phone)) {
                    Toast.makeText(this, "电话号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtil.checkEmpty(password)) {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (DeviceHelper.isNetAvailable(this)) {                        // exist network
                    loginAccount(phone, password);

                } else {                                                        // no net

                    Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_register:      //立即注册
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_forget_pwd:    //忘记密码
                Toast.makeText(this, "忘记密码", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_skip:          //跳过
                Intent intent1 = new Intent(LoginActivity.this, GrabRedActivity.class);
                startActivity(intent1);
                skipToNet();
                break;
        }
    }

    /**
     * 跳过请求网络
     */
    private void skipToNet() {
        ApiServiceFactory.getSpiderApiService(HttpUrls.HTTP_BASE_URL)
                .skip(ParamsHelper.skipMap())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Skip>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.toString();
                    }

                    @Override
                    public void onNext(Skip skip) {
                        if (skip.getCode() == 200) {
                            Toast.makeText(LoginActivity.this, "成功跳过", Toast.LENGTH_SHORT).show();
                            //AppConfig.getConfig(LoginActivity.this).saveVisitorId(LoginActivity.this,skip.getResult().getUser_id());

                        } else {
                            Toast.makeText(LoginActivity.this, skip.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 登录访问
     *
     * @param phone
     * @param password
     */
    private void loginAccount(String phone, String password) {
        ApiServiceFactory.getSpiderApiService(HttpUrls.HTTP_BASE_URL)
                .loginAccount(ParamsHelper.loginAccountMap(phone, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Login>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.toString();
                    }

                    @Override
                    public void onNext(Login response) {
                        if (response.getCode() == 200 && null != response) {
                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                            //保存登录信息
                            AppConfig.getConfig(getApplicationContext()).saveUserInfo(response.getResult());
                            //保存vip的信息
                            AppConfig.vip_type = response.getResult().getVip_type();
                            AppConfig.sum_inviter = response.getResult().getSum_inviter();
                            AppConfig.get_record = response.getResult().getGet_record();
                            //保存登录的毫秒数
                            AppConfig.getConfig(getApplicationContext()).saveLoginTime
                                    (getApplicationContext(), Calendar.getInstance().getTimeInMillis());
                            Intent intent = new Intent(LoginActivity.this, GrabRedActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
