/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.moogos.spacex.core;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.jesgoo.sdk.AdSize;
import com.jesgoo.sdk.AdView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.mogo.space.R;
import com.moogos.spacex.constants.ConfigEntry;
import com.moogos.spacex.constants.GlobalConfig;
import com.moogos.spacex.constants.User;
import com.moogos.spacex.core.newfun.BaseActivity;
import com.moogos.spacex.fragment.AboutFragment;
import com.moogos.spacex.fragment.MineFragment;
import com.moogos.spacex.fragment.SettingsFragment;
import com.moogos.spacex.model.RecievedRedBagEntity;
import com.moogos.spacex.model.RedBagEntity;
import com.moogos.spacex.util.CallBack;
import com.moogos.spacex.util.GetVIP;
import com.moogos.spacex.util.MLog;
import com.moogos.spacex.util.ManageKeyguard;
import com.moogos.spacex.util.ManageScreen;
import com.moogos.spacex.util.NetUtils;
import com.moogos.spacex.util.UpdateManager;
import com.moogos.spacex.util.Util;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.util.List;

//import com.baidu.mobads.AdSize;
//import com.baidu.mobads.SplashAd;
//import com.baidu.mobads.SplashAdListener;
//import com.iapppay.interfaces.callback.IPayResultCallback;
//import com.iapppay.sdk.main.IAppPay;
//import com.iapppay.sdk.main.IAppPayOrderUtils;
//import com.jesgoo.sdk.AdView;

/**
 * Starts up the task list that will interact with the AccessibilityService
 * sample.
 */
public class QiangHongBaoActivity extends BaseActivity {

    public final static String MONEY_OF_THIS_TIME = "money";
    public final static String MONEY_FROM = "from";
    public final static String NO_MONEY_REASON = "reason";
    /**
     * An intent for launching the system settings.
     */
    private static final Intent sSettingsIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
  /*  @ViewInject(R.id.status)
    private ImageView status;*/


    @ViewInject(R.id.mine)
    private ImageView mine;

    @ViewInject(R.id.settings)
    private ImageView settings;

    @ViewInject(R.id.share)
    private ImageView share;

    @ViewInject(R.id.about)
    private ImageView about;
    //    @ViewInject(R.id.follow_me)
//    private ImageView follow_me;
    @ViewInject(R.id.vipbtn)
    private ImageView vipbtn;

    @ViewInject(R.id.vip_expire)
    private TextView vip_expire;


    //    @ViewInject(R.id.tv_title1)
//    private TextView tv_title1;
    @ViewInject(R.id.tv_title2)
    private TextView tv_title2;
    @ViewInject(R.id.tv_title3)
    private TextView tv_title3;


    Dialog mEnableServiceHintDialog = null;

    FrameLayout adsParent;
    private static AdView interstialAdView;

    /**
     * 从db中getCoin（）展示，getIntent（）从service获取数据展示
     */
    private void processExtraData() {
        Intent intent = getIntent();
        float sum = 0;

        try {
            DbUtils db = DbUtils.create(this);
            List<RecievedRedBagEntity> list = db.findAll(RecievedRedBagEntity.class);
            MLog.d("processExtraData list size = " + list.size());
            for (RecievedRedBagEntity rrg : list) {
                try {
                    sum += Float.parseFloat(rrg.getCoin());
                } catch (Exception e) {
                }
            }
            db.close();
        } catch (Exception e) {

        }

        tv_title3.setText(String.format(getResources().getText(R.string.money_total_desc).toString(), new DecimalFormat("##0.00").format(sum)));

        try {
            String money = intent.getStringExtra(MONEY_OF_THIS_TIME);
            String reason = intent.getStringExtra(NO_MONEY_REASON);
            String from = intent.getStringExtra(MONEY_FROM);
            if (money.equals(RedBagEntity.DEFAULT_MONEY)) {
                if (reason.equals("")) {
                    tv_title2.setText(getResources().getText(R.string.money_result_desc));
                } else {
                    tv_title2.setText(reason);
                }
            } else {
                tv_title2.setText(String.format("抢到%s红包：%s元", from.equals("qq") ? "QQ" : "微信", money));
                if (!User.isVip() || !User.isLogin) {
                    if (interstialAdView != null) {
                        interstialAdView.showInterstialAd();
                    }
                }
            }

        } catch (Exception e) {
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processExtraData();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.e("BOBO", "Get onCreate savedInstanceState");
        try {
            UpdateManager.checkNeedsUpdate(this);       //更新逻辑
        } catch (Exception e) {
            MLog.e("update fail", e);
        }


//        if (MyApplication.sdkManager == null) {         //sdk登录
//            MyApplication.sdkManager = HuosdkManager.getInstance();
//            MyApplication.sdkManager.initSdk(this, new OnInitSdkListener() {
//                @Override
//                public void initSuccess(String code, String msg) {
//                    if (GlobalConfig.isDebug) {
//                        Log.d(GlobalConfig.TAG, "sdkManager initSuccess");
//                    }
//                    User.login(getApplicationContext(), new CallBack() {
//                        @Override
//                        public void run(String content) {
//                            mainHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (User.vipIsExpire()) {
//                                        Util.showExpiredVipDialog(QiangHongBaoActivity.this);
//                                    }
//                                }
//                            });
//                            updateVipView();    //更新
//                            isLoginFinish = true;
//                        }
//                    }, new CallBack() {
//                        @Override
//                        public void run(String content) {
//                            updateVipView();
//                            isLoginFinish = true;
//                        }
//                    });
//                }
//
//                @Override
//                public void initError(String code, String msg) {
//                    updateVipView();
//                    Log.e(GlobalConfig.TAG, "sdkManager initSdk Error=" + msg + " code =" + code);
//
//                }
//            });
//        }
        setContentView(R.layout.main_layout);

        adsParent = new FrameLayout(this);
        ViewUtils.inject(this);

        processExtraData();

        Util.sendPackageList(this);
    }

    private AdView bannerAdView;

    /**
     * 登陆成功进行更新内容
     */
    public void updateVipView() {
        mainHandler.post(new Runnable() {
                             @Override
                             public void run() {
                                 if (User.isVip() && User.isLogin) {
                                     vip_expire.setVisibility(View.VISIBLE);
                                     vip_expire.setText("VIP会员 " + User.getVipExpireTimeString() + "过期");
                                     final FrameLayout banner_ad = (FrameLayout) findViewById(R.id.main_ad_banner);
                                     if (bannerAdView != null) {
                                         bannerAdView.clearDisappearingChildren();
                                         bannerAdView.destroyDrawingCache();
                                         banner_ad.removeView(bannerAdView);
                                         bannerAdView = null;
                                     }
                                     if (interstialAdView != null) {
                                         interstialAdView.clearDisappearingChildren();
                                         interstialAdView.destroyDrawingCache();
                                         interstialAdView = null;
                                     }
                                     if (getPackageName().equals("com.mogo.space")) {
                                         TextView t = (TextView) findViewById(R.id.tv_title_vip);
                                         t.setText("VIP:尊敬的VIP用户，点击下方\"VIP专用\"按钮，即可获取专属VIP版，专属VIP版可以防止被封号，安装后请将本应用删除。");
                                         final ImageView imageView = (ImageView) findViewById(R.id.status);
                                         imageView.setOnClickListener(new OnClickListener() {
                                             boolean isrun = false;

                                             @Override
                                             public void onClick(View v) {
                                                 if (isrun) {
                                                     imageView.setImageResource(R.drawable.status_on);
                                                     isrun = false;
                                                     QiangHongBaoService.ISRUNNING = false;
                                                 } else if (!ConfigEntry.isAccessibilityEnabled(QiangHongBaoActivity.this)) {
                                                     showEnableServiceHintDialog();
                                                 } else {
                                                     imageView.setImageResource(R.drawable.zzq);
                                                     QiangHongBaoService.ISRUNNING = true;
                                                     isrun = true;
                                                 }
//                                if (isclickone) {
//                                    if (!GlobalConfig.VIP_URL.trim().equals("")) {
//                                        GetVIP getVIP = new GetVIP(QiangHongBaoActivity.this, GlobalConfig.VIP_URL);
//                                        getVIP.showDownloadDialogAndStart();
//                                    } else {
//                                        Toast.makeText(QiangHongBaoActivity.this, "本按钮失效，请联系客服人员下载VIP专用版", Toast.LENGTH_LONG).show();
//                                    }
//                                    isclickone = false;
//                                } else {
//                                    isclickone = true;
//                                    mainHandler.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            isclickone = false;
//                                        }
//                                    }, 1000);
//                                }
                                             }
                                         });
                                         //vip时候的点击事件
                                         ImageView vipdown = (ImageView) findViewById(R.id.vipdown);
                                         vipdown.setOnClickListener(new OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 if (!GlobalConfig.VIP_URL.trim().equals("")) {
                                                     GetVIP getVIP = new GetVIP(QiangHongBaoActivity.this, GlobalConfig.VIP_URL);
                                                     getVIP.showDownloadDialogAndStart();
                                                 } else {
                                                     Toast.makeText(QiangHongBaoActivity.this, "本按钮失效，请联系客服人员下载VIP专用版", Toast.LENGTH_LONG).show();
                                                 }
                                             }
                                         });
                                     }else {
                                         final ImageView imageView = (ImageView) findViewById(R.id.status);
                                         imageView.setOnClickListener(new OnClickListener() {
                                             boolean isrun = false;

                                             @Override
                                             public void onClick(View v) {
                                                 if (isrun) {
                                                     imageView.setImageResource(R.drawable.status_on);
                                                     isrun = false;
                                                     QiangHongBaoService.ISRUNNING = false;
                                                 } else if (!ConfigEntry.isAccessibilityEnabled(QiangHongBaoActivity.this)) {
                                                     showEnableServiceHintDialog();
                                                 } else {
                                                     imageView.setImageResource(R.drawable.zzq);
                                                     QiangHongBaoService.ISRUNNING = true;
                                                     isrun = true;
                                                 }
                                             }
                                         });
                                         ImageView vipdown = (ImageView) findViewById(R.id.vipdown);
                                         vipdown.setVisibility(View.GONE);
                                     }
                                 } else {
                                     if (bannerAdView == null) {
                                         final FrameLayout banner_ad = (FrameLayout) findViewById(R.id.main_ad_banner);
                                         bannerAdView = new AdView(QiangHongBaoActivity.this, AdSize.Banner, GlobalConfig.Banner_ADSLOT);
                                         banner_ad.addView(bannerAdView, new LinearLayout.LayoutParams(-1, -1));
                                     }
                                     vip_expire.setVisibility(View.INVISIBLE);
                                     if (interstialAdView == null) {
                                         interstialAdView = new AdView(QiangHongBaoActivity.this, AdSize.Interstitial, GlobalConfig.Inter_ADSLOT);
                                     }
                                     TextView t = (TextView) findViewById(R.id.tv_title_vip);
                                     t.setText("请购买VIP，您将获得更专业的服务！去广告，防封号你值得拥有。");
                                     final ImageView imageView = (ImageView) findViewById(R.id.status);
                                     imageView.setOnClickListener(new OnClickListener() {
                                         boolean isrun = false;

                                         @Override
                                         public void onClick(View v) {
                                             if (isrun) {
                                                 imageView.setImageResource(R.drawable.status_on);
                                                 isrun = false;
                                                 QiangHongBaoService.ISRUNNING = false;
                                             } else if (!ConfigEntry.isAccessibilityEnabled(QiangHongBaoActivity.this)) {
                                                 showEnableServiceHintDialog();
                                             } else {
                                                 imageView.setImageResource(R.drawable.zzq);
                                                 QiangHongBaoService.ISRUNNING = true;
                                                 isrun = true;
                                             }
                                         }
                                     });
                                     ImageView vipdown = (ImageView) findViewById(R.id.vipdown);
                                     vipdown.setOnClickListener(new OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             GetVIP getVIP = new GetVIP(QiangHongBaoActivity.this, GlobalConfig.VIP_URL);
                                             getVIP.showDownloadDialogAndStart();
                                         }
                                     });
                                 }
                             }

                         }

        );
    }

    Handler mainHandler = new Handler();
    public static boolean isLoginFinish = false;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
//        IAppPay.init(this, 1, PayConfig.appid);
        ViewUtils.inject(this);

        NetUtils.postLog(GlobalConfig.getCommonInfo(GlobalConfig.RESUME_DataType).toString());
//        Util.httpPing(this, Util.SERVER_URL_ACTIVATE + Util.getCommonParam(this))

//        Log.e("BOBO", "conf entry =" + ConfigEntry.isAccessibilityEnabled(this));
//        || !QiangHongBaoService.RUN
        if (!ConfigEntry.isAccessibilityEnabled(this)) {
            showEnableServiceHintDialog();
        }
        ManageKeyguard.reenableKeyguard();
        ManageScreen.lightOff(this.getApplicationContext());
        if (isLoginFinish) {        //判断是否登录进行更新
            updateVipView();
        }
        boolean isOpen = ConfigEntry.getIsWeiXinOpen() || ConfigEntry.getIsQQOpen();
//        status.setBackgroundResource(isOpen ? R.drawable.status_on : R.drawable.status_off);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        try {
            if (mEnableServiceHintDialog != null) {
                mEnableServiceHintDialog.dismiss();
                mEnableServiceHintDialog = null;
            }

        } catch (Exception e) {
        }
    }

    @OnClick({R.id.mine, R.id.settings, R.id.about, R.id.vipbtn, R.id.share})
    public void imgClicked(View v) {
        final Intent intent = new Intent(this, SecondaryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        switch (v.getId()) {

            case R.id.mine:

                if (!ConfigEntry.isAccessibilityEnabled(this)) {
                    Toast.makeText(this, "请先开启辅助功能 " + getResources().getText(R.string.app_name), Toast.LENGTH_LONG).show();
                    showEnableServiceHintDialog();
                    return;
                } else {
                    intent.putExtra("frag_name", MineFragment.class.getName());
                }
                break;
            case R.id.settings:         //设置按钮
                if (!ConfigEntry.isAccessibilityEnabled(this)) {
                    Toast.makeText(this, "请先开启辅助功能 " + getResources().getText(R.string.app_name), Toast.LENGTH_LONG).show();
                    showEnableServiceHintDialog();
                    return;
                } else {
                    intent.putExtra("frag_name", SettingsFragment.class.getName());
                }
                break;
            case R.id.about:
                final AlertDialog.Builder builder0 = new AlertDialog.Builder(this);
                builder0.setTitle("提示");
                builder0.setMessage("为了方便您的使用，请选择您需要的帮助形式！");
                builder0.setNegativeButton("文字介绍", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        intent.putExtra("frag_name", AboutFragment.class.getName());
                        intent.putExtra("url", NetUtils.HELP_URL);
                        startActivity(intent);
                    }
                }).setPositiveButton("视频介绍", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        intent.putExtra("frag_name", AboutFragment.class.getName());
                        intent.putExtra("url", NetUtils.VIEDO_URL);
                        startActivity(intent);
                    }
                });

                Dialog noticeDialog0 = builder0.create();
                noticeDialog0.show();

                return;
            case R.id.vipbtn:
//                MobclickAgent.onEvent(this, "clickvip0");
//                AlertDialog.Builder builder = new AlertDialog.Builder(QiangHongBaoActivity.this);
//                builder.setIcon(R.drawable.logo_small);
//                builder.setTitle("请选择套餐:\n(充值成功后请重新启动应用)");
//                final String[] types = {"  15元/月", "  40元/3月", "  80元/半年", "  150元/年", "  友情赞助1元"};
//                builder.setItems(types, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, final int which) {
//                        if (GlobalConfig.isDebug) {
//                            Log.d(GlobalConfig.TAG, "pay dialog select = " + which);
//                        }
//
//                        if (!User.isLogin) {
//                            User.login(getApplicationContext(), new CallBack() {
//                                @Override
//                                public void run(String content) {
//                                    mainHandler.post(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            showPayD(which, types);
//                                        }
//                                    });
//                                }
//                            }, null);
//                        } else {
//                            showPayD(which, types);
//                        }
//                    }
//                });
//                builder.show();
                return;
            case R.id.share:
                Util.share(this);
                return;
//            case R.id.follow_me:
//                final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//                builder1.setTitle("提示");
//                builder1.setMessage("1.点击前往微信\n" +
//                        "2.关注公众号：热词百科\n" +
//                        "3.按提示加入「抢红包福利群」\n" +
//                        " 官方群每天会发送现金红包哦! ");
//                builder1.setNegativeButton("算了", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).setPositiveButton("复制公众号前往微信", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                        cmb.setText("热词百科");
//                        Toast.makeText(builder1.getContext(), "已复制", Toast.LENGTH_LONG).show();
//
//                        dialog.dismiss();
//
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        ComponentName cn = new ComponentName("com.tencent.mm", "com.tencent.mm.plugin.base.stub.WXCustomSchemeEntryActivity");
//                        intent.setData(Uri.parse("weixin://dl/officialaccounts"));
//                        intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
//                        intent.setComponent(cn);
//                        startActivity(intent);
//                    }
//                });
//
//                Dialog noticeDialog1 = builder1.create();
//                noticeDialog1.show();
//
//                return;


            default:
                break;
        }
        startActivity(intent);
    }

//    private void showPayD(final int which, final String[] types) {
//        final String order_id = "QU" + User.getUserId() + System.currentTimeMillis() / 1000;
//        CustomPayParam payParam = new CustomPayParam();
//        final float[] money = GlobalConfig.getMoney();
//        payParam.setCp_order_id(order_id);
//        payParam.setProduct_price(money[which]);
//        payParam.setProduct_count(1);
//        payParam.setProduct_id("1");
//        payParam.setProduct_name("购买VIP");
//        payParam.setProduct_desc(types[which]);
//        payParam.setExchange_rate(0);
//        payParam.setCurrency_name("");
//        payParam.setExt("ext:" + User.getToken());
//        RoleInfo roleInfo = User.getRoleInfo();
//        payParam.setRoleinfo(roleInfo);
//        MyApplication.sdkManager.showPay(payParam, new OnPaymentListener() {
//            @Override
//            public void paymentSuccess(PaymentCallbackInfo callbackInfo) {
//                NetUtils.putRecord(order_id, which + 1, callbackInfo.money, new CallBack() {
//                    @Override
//                    public void run(String content) {
//                        NetUtils.login(getApplicationContext(), null, true);
//                    }
//                });
//                if (GlobalConfig.isDebug) {
//                    Log.d(GlobalConfig.TAG, "paymentSuccess " + "充值金额数：" +
//                            callbackInfo.money + " 消息提示：" + callbackInfo.msg);
//                }
//            }
//
//            @Override
//            public void paymentError(PaymentErrorMsg errorMsg) {
//                if (GlobalConfig.isDebug) {
//                    Log.d(GlobalConfig.TAG, "充值失败：code:" +
//                            errorMsg.code + "  ErrorMsg:" + errorMsg.msg +
//                            "  预充值的金额：" + errorMsg.money);
//                }
//            }
//        });
//    }

    private void showEnableServiceHintDialog() {

        mEnableServiceHintDialog = new Dialog(this, R.style.selectorDialog);
        mEnableServiceHintDialog.setContentView(R.layout.enable_service_hint_dialog);
        mEnableServiceHintDialog.findViewById(R.id.icon).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(sSettingsIntent, 0x123);
                mEnableServiceHintDialog.dismiss();
            }
        });
        mEnableServiceHintDialog.findViewById(R.id.parent).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mEnableServiceHintDialog.dismiss();
            }
        });

        LayoutParams lay = mEnableServiceHintDialog.getWindow().getAttributes();
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

    static long lastBack = System.currentTimeMillis();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - lastBack < 2000) {
                return super.onKeyDown(keyCode, event);
            } else {
                lastBack = System.currentTimeMillis();
                Toast.makeText(this, "需退出请再按一次返回", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
