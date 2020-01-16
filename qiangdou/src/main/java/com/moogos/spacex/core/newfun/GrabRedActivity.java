package com.moogos.spacex.core.newfun;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jesgoo.sdk.AdSize;
import com.jesgoo.sdk.AdView;
import com.mogo.space.R;
import com.moogos.spacex.bean.RedMessage;
import com.moogos.spacex.bean.Response;
import com.moogos.spacex.constants.ApiServiceFactory;
import com.moogos.spacex.constants.AppConfig;
import com.moogos.spacex.constants.GlobalConfig;
import com.moogos.spacex.constants.HttpUrls;
import com.moogos.spacex.constants.ParamsHelper;
import com.moogos.spacex.core.SplashActivity;
import com.moogos.spacex.manager.ActivityStack;
import com.moogos.spacex.pop.PersonalCenterPop;
import com.moogos.spacex.pop.RegisterPrompPop;
import com.moogos.spacex.util.Util;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 抢红包的页面
 * Created by xupeng on 2017/12/5.
 */

public class GrabRedActivity extends BaseActivity implements
        PersonalCenterPop.PersonalCenterListener, RegisterPrompPop.RegisterPromListener {
    @Bind(R.id.tv_prompt_message)
    TextView tvPromptMessage;
    @Bind(R.id.iv_grab_red)
    ImageView ivGrabRed;
    private PersonalCenterPop mPersonalCenterPop;
    Dialog mEnableServiceHintDialog = null;
    private static final Intent sSettingsIntent =
            new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);

    public final static String MONEY_OF_THIS_TIME = "money";
    public final static String MONEY_FROM = "from";
    public final static String NO_MONEY_REASON = "reason";

    public final static String RED_MESSAGE = "red_message"; //记录红包信息


    public static int grabNumber;       //抢到的次数
    private boolean clickFlag;          //点击的标识
    private RegisterPrompPop registerPrompPop;
    public static boolean serviceOPen = true;   //重启app提示开启辅助功能的flag
    private Integer number = Integer.valueOf(SplashActivity.skip_times);    //抢到的次数

    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            alertRegisterPop();

        }
    };

    private static AdView interstialAdView;
    private BroadcastMain receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grab_red);
        ButterKnife.bind(this);


//        try {
//            UpdateManager.checkNeedsUpdate(this);       //更新逻辑
//        } catch (Exception e) {
//            MLog.e("update fail", e);
//        }

//        Beta.checkUpgrade();

        //注册广播
        //新添代码，在代码中注册广播接收程序
        receiver = new BroadcastMain();
        IntentFilter filter = new IntentFilter();
        filter.addAction("polly.liu.Image");
        registerReceiver(receiver, filter);

    }

    //内部类，实现BroadcastReceiver
    public class BroadcastMain extends BroadcastReceiver {
        //必须要重载的方法，用来监听是否有广播发送
        @Override
        public void onReceive(Context context, Intent intent) {
            ivGrabRed.setImageResource(R.drawable.stop_grab);
            EnvelopeService.ISRUNNING = true;   //开启抢红包
            clickFlag = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);   //取消广播
    }

    @OnClick({R.id.iv_grab_red, R.id.iv_personal_center})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_grab_red:
                MobclickAgent.onEvent(this, "grab_red"); // eventId 事件ID，自己定义

//                Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
//                startActivity(intent);
//                Toast.makeText(this, "找到抢红包，然后开启服务即可", Toast.LENGTH_LONG).show();
                grabRed();

                break;
            case R.id.iv_personal_center:   //个人中心
                showPop();
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (EnvelopeService.serviceConnected && clickFlag) {
//            ivGrabRed.setImageResource(R.drawable.stop_grab);
//            EnvelopeService.ISRUNNING = true;   //开启抢红包
//            clickFlag = true;
//        }

    }

    /**
     * 抢红包
     */
    private void grabRed() {
        if (serviceOPen) {//!ConfigEntry.isAccessibilityEnabled(this)
            Toast.makeText(this, "请先开启辅助功能 " + getResources().getText(R.string.app_name), Toast.LENGTH_LONG).show();
            showEnableServiceHintDialog();

            return;
        } else {
            if (clickFlag) {                             //再次点击关闭开抢功能
                ivGrabRed.setImageResource(R.drawable.grab_red);
                EnvelopeService.ISRUNNING = false;   //关闭抢红包
                clickFlag = false;
                Toast.makeText(this, "关闭抢红包", Toast.LENGTH_SHORT).show();
                return;
            }
            //登录
//            if (AppConfig.getConfig(this).isLogin()) {    //TODO :取消登录状态  AppConfig.getConfig(this).isLogin()
//                ivGrabRed.setImageResource(R.drawable.stop_grab);
//                clickFlag = true;
//                EnvelopeService.ISRUNNING = true;   //开启抢红包
//                Toast.makeText(this, "抢红包", Toast.LENGTH_SHORT).show();
//            } else {  //未登录
//                //试玩三次
//                if (number <= 2000) {      //TODO:进行免费尝试
//                    ivGrabRed.setImageResource(R.drawable.stop_grab);
//                    EnvelopeService.ISRUNNING = true;   //开启抢红包
//                    clickFlag = true;
//                    Toast.makeText(this, "抢红包", Toast.LENGTH_SHORT).show();
//                } else {
//                    EnvelopeService.ISRUNNING = false;
//                    Toast.makeText(this, "请去注册", Toast.LENGTH_SHORT).show();
//                    alertRegisterPop();
//
//                }
//            }


        }
    }

    /**
     * 弹出提示窗
     */
    private void alertRegisterPop() {
        if (null == registerPrompPop) {
            registerPrompPop = RegisterPrompPop.getInstance(this);
        }
        registerPrompPop.setRegisterPromListener(this);
        registerPrompPop.showPopupView(getWindow().getDecorView().findViewById(android.R.id.content));
    }


    /**
     * pop的展示
     */
    private void showPop() {
        if (null == mPersonalCenterPop) {
            mPersonalCenterPop = PersonalCenterPop.getInstance(this);
        }
        mPersonalCenterPop.setPersonalCenterListener(this);
        mPersonalCenterPop.showPopupView(getWindow().getDecorView().findViewById(android.R.id.content));
    }

    /**
     * 从servicer抢到红包会掉到此页面接收数据
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);  //赋值

        processExtraData();

    }


    /**
     * 回显数据
     */
    private void processExtraData() {

        final RedMessage redMessage = (RedMessage)
                getIntent().getSerializableExtra(GrabRedActivity.RED_MESSAGE);
        if (null == redMessage)
            return;
        tvPromptMessage.setText(String.format("抢到%s红包：%s元",
                redMessage.getChannel().equals("qq") ? "QQ" : "微信", redMessage.getMoney()));

//        number++;
//        if (!AppConfig.getConfig(this).isLogin() && number >= 3) {     //非登录且超过3次试玩
//            EnvelopeService.ISRUNNING = false;   //关闭抢红包
//            ivGrabRed.setImageResource(R.drawable.grab_red);
//            Toast.makeText(this, "请去注册", Toast.LENGTH_SHORT).show();
//            handler.sendEmptyMessageDelayed(0, 1000);
//        }

        //非登录状态弹出广告
//        if (interstialAdView == null) {
//            interstialAdView = new AdView(GrabRedActivity.this, AdSize.Interstitial, GlobalConfig.Inter_ADSLOT);
//        }
//        if (!AppConfig.getConfig(this).isLogin()) {
//            interstialAdView.showInterstialAd();
//        }
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                sendRedMessage(redMessage);
//            }
//        }, 1000);


//        Intent intent = getIntent();
//        try {
//            String money = intent.getStringExtra(MONEY_OF_THIS_TIME);
//            String reason = intent.getStringExtra(NO_MONEY_REASON);
//            String from = intent.getStringExtra(MONEY_FROM);
//            if (money.equals(RedBagEntity.DEFAULT_MONEY)) {
//                if (reason.equals("")) {
//                    tvPromptMessage.setText(getResources().getText(R.string.money_result_desc));
//                } else {
//                    tvPromptMessage.setText(reason);
//                }
//            } else {
//                //抢到红包记录抢到的次数
//
//                grabNumber++;
//                AppConfig.getConfig(this).saveGrabNumber(this, String.valueOf(grabNumber));
//                tvPromptMessage.setText(String.format("抢到%s红包：%s元", from.equals("qq") ? "QQ" : "微信", money));
//                //抢包的时候超过三次提示,并且未登录
//                if (!AppConfig.getConfig(this).isLogin() && Integer.valueOf(AppConfig.getConfig(this).getGrabNumber(this)) >= 3
//                        && Integer.valueOf(SplashActivity.skip_times) >= 3) {
//                    QiangHongBaoService.ISRUNNING = false;   //关闭抢红包
//                    ivGrabRed.setImageResource(R.drawable.grab_red);
//                    Toast.makeText(this, "请去注册", Toast.LENGTH_SHORT).show();
//                    handler.sendEmptyMessageDelayed(0, 1000);
//                }
//
//
//                //非登录状态弹出广告
//                if (interstialAdView == null) {
//                    interstialAdView = new AdView(GrabRedActivity.this, AdSize.Interstitial, GlobalConfig.Inter_ADSLOT);
//                }
//                if (!AppConfig.getConfig(this).isLogin()) {
//                    interstialAdView.showInterstialAd();
//                }
//
//
/////               if (!User.isVip() || !User.isLogin) {     //非会员或未登录展示广告
////                    if (interstialAdView != null) {
////                        interstialAdView.showInterstialAd();
////                    }
////                }
//            }
//
//        } catch (Exception e) {
//        }
//
//
//        if (null != redMessage)
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    sendRedMessage(redMessage);
//                }
//            }, 1000);
    }

    /**
     * 访问网络发送红包信息
     */
    private void sendRedMessage(RedMessage redBagEntity) {
        ApiServiceFactory.getSpiderApiService(HttpUrls.HTTP_BASE_URL)
                .sendRedMessage(ParamsHelper.sendRedMsgParam(redBagEntity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response skip) {
                        //返回重新登陆的信息
                        if (null != skip && skip.getCode() == 500) {
                            toast("用户需重新登录");
                            ActivityStack.getInstanse().removeAll();        //activity移栈
                            finish();
                            startActivity(new Intent(GrabRedActivity.this, LoginActivity.class));
                        }
                    }
                });
    }

    /**
     * 提示开启服务的窗口
     */
    private void showEnableServiceHintDialog() {

        mEnableServiceHintDialog = new Dialog(this, R.style.selectorDialog);
        mEnableServiceHintDialog.setContentView(R.layout.enable_service_hint_dialog);
        mEnableServiceHintDialog.findViewById(R.id.icon).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(sSettingsIntent, 0x123);
                mEnableServiceHintDialog.dismiss();
            }
        });
        mEnableServiceHintDialog.findViewById(R.id.parent).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mEnableServiceHintDialog.dismiss();
            }
        });

        ViewGroup.LayoutParams lay = mEnableServiceHintDialog.getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Rect rect = new Rect();
        View view = getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(rect);
        lay.height = dm.heightPixels - rect.top;
        lay.width = dm.widthPixels;

        mEnableServiceHintDialog.setCanceledOnTouchOutside(true);
        mEnableServiceHintDialog.show();
    }


    @Override
    public void onMyRed() {
        Intent intent = new Intent(this, MyRedEnvelope.class);
        startActivity(intent);
    }

    @Override
    public void onTask() {
        Intent intent = new Intent(this, TaskBarActivity.class);
        startActivity(intent);


    }

    /**
     * 进入设置页面
     */
    @Override
    public void onSeetting() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onShare() {
        Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
        Util.shareDetail(this);
    }

    /**
     * 进入帮助页
     */
    @Override
    public void onHelp() {
        Intent intent = new Intent(this, HelpWebviewActivity.class);
        startActivity(intent);
    }

    /**
     * 去注册
     */
    @Override
    public void onRegisterTips() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * 监听back键，当activity为task根back键退居后台（相当于home键）
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
        }
        return super.onKeyDown(keyCode, event);
    }

}
