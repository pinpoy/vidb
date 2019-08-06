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

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.moogos.spacex.constants.ConfigEntry;
import com.moogos.spacex.constants.GlobalConfig;
import com.moogos.spacex.core.newfun.GrabRedActivity;
import com.moogos.spacex.model.RedBagEntity;
import com.moogos.spacex.util.MLog;
import com.moogos.spacex.util.ManageKeyguard;
import com.moogos.spacex.util.ManageScreen;
import com.moogos.spacex.util.Util;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * This class demonstrates how an accessibility service can query window content
 * to improve the feedback given to the user.
 */
@SuppressWarnings("deprecation")
public class QiangHongBaoService extends AccessibilityService {

    private Handler handler = new Handler();

    private static String pageClassName;

    private static int obj = -1;//0:微信红包  1：qq红包

    public static boolean ISRUNNING = false;//正在抢红包；
    //	private static int state = 0;
    public static boolean RUN = false;

    RedBagEntity entity = new RedBagEntity();

    private Context appContext;

    private static QQAction qqAction = null;
    private static WeChatAction weChatAction = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onServiceConnected() {
        appContext = getApplicationContext();
        GrabRedActivity.serviceOPen = false;
        RUN = true;
//        Toast.makeText(this.getApplicationContext(), "onServiceConnected...", Toast.LENGTH_LONG).show();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
                | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.notificationTimeout = 100;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);

        obj = -1;

        ConfigEntry.serviceIsON = true;
        qqAction = new QQAction(entity, this);
        weChatAction = new WeChatAction(entity, this);
        Util.httpPing(QiangHongBaoService.this, Util.SERVER_URL_START + Util.getCommonParam(QiangHongBaoService.this));
        MobclickAgent.onEvent(this, "startservice");
        ConfigEntry.init(appContext);
        if (SplashActivity.splashOK) {
            Toast.makeText(this.getApplicationContext(), "抢红包已开启...请点击抢", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, GrabRedActivity.class);    //启动辅助功能回到新版抢红包页面
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    private static boolean resetState = true;

    private void resetState() {
        final long period = ConfigEntry.getDelayCanTimeVip() + ConfigEntry.getDelayReplyCanTimeVip() + 15000;
        new Thread(new Runnable() {
            @Override
            public void run() {
                long cur = System.currentTimeMillis();
                while (resetState) {
                    SystemClock.sleep(1000);
                    if (System.currentTimeMillis() - cur > period) {
                        obj = -1;
                        resetState = false;
//                        Util.httpPing(appContext, Util.ACTION_URL + Util.getCommonParam(appContext) + "&clkid=" + Util.ACTION_ID_RESET_STATE + "&status=" +
//                                entity.getStatus() + "&page=" + pageClassName);
                        entity.setReason("暂时未抢到哦~");
                        gotoMyActivity("2.reset State");
                        ManageScreen.lightOff(appContext);
                        ManageKeyguard.reenableKeyguard();
                    }
                }
            }
        }).start();
    }

    /**
     * 开屏操作
     */
    private void lightScreenOn() {
        try {
            ManageScreen.lightOn(appContext);
//            if (ManageKeyguard.inKeyguardRestrictedInputMode(appContext) && ConfigEntry.getLockedCanVip()) {
//                ManageKeyguard.disableKeyguard(this);
//            }
        } catch (Exception e) {
            if (GlobalConfig.isDebug) {
                e.printStackTrace();
                Log.d(GlobalConfig.TAG, "Exception:" + e.getMessage());
            }
        }
    }

    private void closeScreen() {

        ManageKeyguard.reenableKeyguard();
        ManageScreen.lightOff(this.getApplicationContext());

    }


    /**
     * 抢红包的重要逻辑---分析通知
     *
     * @param event
     */
    private boolean analyzeNotif(AccessibilityEvent event) {
        //测试关键字屏蔽
//        ConfigEntry.setShieldContent(getApplicationContext(), "你好");
//        ConfigEntry.setShield(getApplicationContext(), true);

        List<CharSequence> texts = event.getText();
        boolean isOpenShied = ConfigEntry.getIsShield();
        if (isOpenShied) {                      //查询关键字，如果有返回true
            if (null != event && null != event.getText()) {
                String shied = ConfigEntry.getShieldContent();
                String[] shieds = shied.split(" ");
                for (CharSequence cs : texts) {
                    String aString = cs.toString();
                    for (String s : shieds) {
                        int index = aString.indexOf(s);
                        if (index >= 0) {
                            return true;
                        }
                    }
                }
            }
        }


        for (CharSequence cs : texts) {
            if (cs != null) {
                String aString = cs.toString();
                MLog.d("analyzeNotif:" + aString);

                int index = aString.indexOf("[微信红包]");
                if (index >= 0 && ConfigEntry.getIsWeiXinOpen()) {
                    clickNotif(0, event, aString, index);
                    entity.setFrom("wechat");
                    break;

                } else {
                    int qqIndex = aString.indexOf("[QQ红包]");
                    // Log.d("TYPE_NOTIFICATION_STATE_CHANGED:" + aString + " " + qqIndex);
                    if (qqIndex >= 0 && ConfigEntry.getIsQQOpen()) {
                        clickNotif(1, event, aString, qqIndex);
                        entity.setFrom("qq");
                        break;
                    }
                }
            }
        }

        return false;
    }


    private void clickNotif(final int which, AccessibilityEvent event, String aString, int index) {
        //判断是否锁屏
//        if (ManageKeyguard.inKeyguardRestrictedInputMode(appContext)) {
//            return;
//        }
        //lightScreenOn();            //开屏抢红包
        if (isKeyguardLocked())
            return;
        wakeAndUnlock(true);    //唤醒屏幕（点亮）

        //获取昵称并保存
        String temp = aString.substring(0, index - 1).trim();
        String nickName = temp;
        try {
            if (temp.endsWith(":")) {
                nickName = temp.substring(0, temp.length() - 1);
            }
        } catch (Exception e) {
        }
        entity.setSend_nickname(nickName);

        final Notification parcelable = (Notification) event.getParcelableData();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    parcelable.contentIntent.send();
//                    playSound();

                    resetState = true;
                    resetState();
                    if (which == 0) {
                        obj = 0;
                    } else {
                        obj = 1;
                    }
                } catch (Exception e1) {

                }

            }
        }, 0); //ConfigEntry.getDelayCanTimeVip() * 1000
    }


    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onAccessibilityEvent(AccessibilityEvent event) {
        RUN = true;
        int type = event.getEventType();
        //是否是屏蔽关键字的情况
        boolean isShield = false;
        if (type == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            pageClassName = event.getClassName().toString();
        }
        //微信QQ的开关
        if (!ConfigEntry.getIsWeiXinOpen() && !ConfigEntry.getIsQQOpen()) {
            return;
        }
        //微信
        if (weChatAction.weChatLate || weChatAction.retToInLauncherUI) {
            if (pageClassName != null && pageClassName.startsWith("com.tencent.mm")) {
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            } else {
                gotoMyActivity("4. late for wechat");
            }
            return;
        }
        //QQ
        if (qqAction.qqLate) {
            if (pageClassName != null && pageClassName.startsWith("com.tencent.mobileqq")) {
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            } else {
                gotoMyActivity("7.qq late for qq");
            }
            return;
        }


        if (type == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED && obj == -1 && ISRUNNING) {
            isShield = analyzeNotif(event);
        }
        if (!isShield) {        //屏蔽关键字
            if (obj == 0 && ConfigEntry.getIsWeiXinOpen() && weChatAction != null) {
                weChatAction.handleWechat(pageClassName, event);
            } else if (obj == 1 && ConfigEntry.getIsQQOpen() && qqAction != null) {
                qqAction.handleQQ(pageClassName, event);    // TODO: 延时
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void gotoMyActivity(String cause) {
        obj = -1;
        resetState = false;

//        initVariables();
        qqAction.reset();
        weChatAction.reset();
        MLog.d("Goto Activity money=" + entity.getMoney() + " from=" + entity.getFrom() + " Reason=" + entity.getReason() + " cause=" + cause);
        Intent intent = new Intent(this, GrabRedActivity.class);
        intent.putExtra(GrabRedActivity.MONEY_OF_THIS_TIME, entity.getMoney());
        intent.putExtra(GrabRedActivity.MONEY_FROM, entity.getFrom());
        intent.putExtra(GrabRedActivity.NO_MONEY_REASON, entity.getReason());
        intent.putExtra(GrabRedActivity.RED_MESSAGE, entity);   //传递红包信息
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        entity.reset();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        obj = -1;
        ConfigEntry.serviceIsON = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onInterrupt() {
        obj = -1;
        ConfigEntry.serviceIsON = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        obj = -1;
        ConfigEntry.serviceIsON = false;
    }

    private PowerManager.WakeLock mWakeLock;
    private KeyguardManager mKeyguardManager;
    private KeyguardManager.KeyguardLock mKeyguardLock;

    private boolean isKeyguardLocked() {
        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        return mKeyguardManager.isKeyguardLocked();
    }

    private void wakeAndUnlock(boolean unlock) {
        if (unlock) {
            PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = manager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "money");
            mWakeLock.acquire();
            if (mKeyguardManager != null) {
                mKeyguardLock = mKeyguardManager.newKeyguardLock("unLock");
                mKeyguardLock.disableKeyguard();

            }
        } else {
            if (mKeyguardLock != null) {
                mKeyguardLock.reenableKeyguard();
            }
            if (mWakeLock != null) {
                mWakeLock.release();
            }
        }
    }
}
