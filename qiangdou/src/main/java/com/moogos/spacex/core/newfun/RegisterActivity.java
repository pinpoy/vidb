package com.moogos.spacex.core.newfun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.mogo.space.R;
import com.moogos.spacex.bean.Register;
import com.moogos.spacex.bean.Response;
import com.moogos.spacex.constants.ApiServiceFactory;
import com.moogos.spacex.constants.HttpUrls;
import com.moogos.spacex.constants.ParamsHelper;
import com.moogos.spacex.pop.SucsRegisterPop;
import com.moogos.spacex.util.StringUtil;
import com.moogos.spacex.views.CountDownView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 注册页面
 * Created by xupeng on 2017/12/5.
 */

public class RegisterActivity extends BaseActivity implements SucsRegisterPop.SucsRegisterListener {
    @Bind(R.id.cdv_get_verify_code)
    CountDownView cdvGetVerifyCode;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_veri_code)
    EditText etVeriCode;
    @Bind(R.id.et_referrer)
    EditText etReferrer;

    private String phone;
    private String password;
    private String veriCode;
    private String referrer;
    private SucsRegisterPop sucsRegisterPop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        initVerifyCodeBtn();
    }

    @OnClick({R.id.cdv_get_verify_code, R.id.iv_register})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.cdv_get_verify_code:      //获取验证码
                String phone = etPhone.getText().toString().trim();
                if (!StringUtil.isNumber(phone)) {
                    Toast.makeText(this, "请输入正确的手机号!", Toast.LENGTH_SHORT).show();
                    return;
                }
                cdvGetVerifyCode.start();
                getVerifyCode(phone);
                break;

            case R.id.iv_register:                 //注册
                //cdvGetVerifyCode.start();
                registerAccount();
                break;

        }
    }

    /**
     * 网络请求---获取验证
     *
     * @param phone 手机号码
     */
    private void getVerifyCode(String phone) {
        ApiServiceFactory.getSpiderApiService(HttpUrls.HTTP_BASE_URL)
                .getVerifyCode(ParamsHelper.getVerifyCodeMap(phone))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("onError", e.toString());
                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.getCode() == 200) {
                            Toast.makeText(RegisterActivity.this, "短信发送成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    /**
     * 注册账号
     */
    private void registerAccount() {
        phone = etPhone.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        veriCode = etVeriCode.getText().toString().trim();
        referrer = etReferrer.getText().toString().trim();
        if (StringUtil.checkEmpty(phone)) {
            Toast.makeText(this, "电话号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.checkEmpty(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.checkEmpty(veriCode)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        registerForNet(phone, password, veriCode, referrer);

    }

    /**
     * 网络请求---注册
     *
     * @param phone
     * @param password
     * @param veriCode
     * @param referrer
     */
    private void registerForNet(String phone, String password, String veriCode, String referrer) {

        ApiServiceFactory.getSpiderApiService(HttpUrls.HTTP_BASE_URL)
                .registerForNet(ParamsHelper.registerForNetMap(phone, password, veriCode, referrer))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Register>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Register response) {
                        if (response.getCode() == 200) {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            SucsRegisterPop(response.getResult().getInvitation_code());

                        } else {
                            Toast.makeText(RegisterActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    /**
     * 成功注册的pop
     * @param user_id
     */
    private void SucsRegisterPop(String user_id) {
        if (null == sucsRegisterPop) {
            sucsRegisterPop = SucsRegisterPop.getInstance(this);
        }
        sucsRegisterPop.setUserId(user_id);    //设置userid
        sucsRegisterPop.setSucsRegisterListener(this);
        sucsRegisterPop.showPopupView(getWindow().getDecorView().findViewById(android.R.id.content));
    }


    //填充验证码
    private void initVerifyCodeBtn() {
        String defaultText = getString(R.string.get_verify_code);
        String finishText = getString(R.string.get_verify_code_again);
        String startText = getString(R.string.second_get_again);

        cdvGetVerifyCode.init(60, defaultText, startText, finishText,
                R.color.color_00, R.color.color_00,
                R.color.black, R.color.black);
        // 不自动倒数
        cdvGetVerifyCode.setAutoStart(false);
    }

    /**
     * pop的点击事件
     */
    @Override
    public void onSucsRegisterTips() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
