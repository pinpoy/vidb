package com.moogos.spacex.core.newfun;

/**
 * Desc
 * Created by xupeng on 2017/12/20.
 */

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.moogos.spacex.bean.RedMessage;
import com.moogos.spacex.constants.ConfigEntry;
import com.moogos.spacex.core.SplashActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by Administrator</p>
 * <p/>
 * 抢红包外挂服务
 */
public class EnvelopeService extends AccessibilityService {

    static final String TAG = "Jackie";
    public static boolean ISRUNNING = true;//正在抢红包(默认开启)
    public static boolean serviceConnected = false;//正在抢红包(默认开启)

    /**
     * 微信的包名
     */
    static final String WECHAT_PACKAGENAME = "com.tencent.mm";
    /**
     * 红包消息的关键字
     */
    static final String ENVELOPE_TEXT_KEY = "[微信红包]";
    static final String QQ_TEXT_KEY = "[QQ红包]";
    static final String QQ_BOTTOM_LEFT = "QQ红包";    //红包左下角的文字
    private static final String OPEN_ID = "com.tencent.mm:id/ba_";
    private static final String OPEN_LUCKY_MONEY_BUTTON_ID = "com.tencent.mm:id/c2i";//bx4

    Handler handler = new Handler();
    private RedMessage redMessage = new RedMessage();
    private boolean hasShieldContent = false;       //是否符合关键字的屏蔽标识
    private String className;
    private boolean isLastWeb;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final int eventType = event.getEventType();

        Log.d(TAG, "事件---->" + event);
        if (!ISRUNNING) {
            return;
        }

        //通知栏事件
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            List<CharSequence> texts = event.getText();
            if (!texts.isEmpty()) {

                if (ConfigEntry.getIsShield()) {        //屏蔽关键字段
                    for (CharSequence t : event.getText()) {
                        String text = String.valueOf(t);
                        if (text.contains(ConfigEntry.getShieldContent())) {
                            hasShieldContent = true;    //是否符合关键字的屏蔽标识
                            return;
                        }
                    }
                }

                for (CharSequence t : texts) {
                    String text = String.valueOf(t);
                    if (text.contains(ENVELOPE_TEXT_KEY) && ConfigEntry.getIsWeiXinOpen()) {     //微信(开关)
                        redMessage.setChannel("weChat");
                        openNotification(event);
                        break;
                    }
                    if (text.contains(QQ_TEXT_KEY) && ConfigEntry.getIsQQOpen()) {           //QQ(开关)
                        redMessage.setChannel("qq");
                        openNotification(event);
                        break;
                    }

                }
            }
        } else if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (hasShieldContent) {     //加固判断
                hasShieldContent = false;
                return;
            }

            if ("weChat".equals(redMessage.getChannel()) && ConfigEntry.getIsWeiXinOpen() && ISRUNNING) {
                openEnvelope(event);
            } else if ("qq".equals(redMessage.getChannel()) && ConfigEntry.getIsQQOpen() && ISRUNNING) {
                openEnvelopeToQQ(event);
            }

        }
    }


    /**
     * 打开通知栏消息
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openNotification(AccessibilityEvent event) {
        // TODO: 开屏抢红包
        if (isKeyguardLocked())
            return;
        wakeAndUnlock(true);    //唤醒屏幕（点亮）


        if (event.getParcelableData() == null || !(event.getParcelableData() instanceof Notification)) {
            return;
        }
        //以下是精华，将微信的通知栏消息打开
        Notification notification = (Notification) event.getParcelableData();
        PendingIntent pendingIntent = notification.contentIntent;
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * QQ的页面监听
     *
     * @param event com.tencent.mobileqq.activity.SplashActivity
     *              com.tenpay.android.qqplugin.util.s
     *              cooperation.qwallet.plugin.QWalletPluginProxyActivity
     *              com.tenpay.android.qqplugin.control.CircleProgressDialog
     *              cooperation.qwallet.plugin.QWalletPluginProxyActivity     //
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openEnvelopeToQQ(AccessibilityEvent event) {
        if ("com.tencent.mobileqq.activity.SplashActivity".equals(event.getClassName())) {
            //点中了红包，下一步就是去拆红包
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkKey3();
                }
            }, ConfigEntry.getDelayCanTimeVip() * 1000);          // TODO: 延时抢红包
        } else if ("cooperation.qwallet.plugin.QWalletPluginProxyActivity".equals(event.getClassName())) {

//            getNameAndMoney();     //获取哦红包昵称和金钱
//            if (ConfigEntry.getAutoReply()) {
//                autoReply();                //自动回复
//            } else {
//                goToRedWeb();
//            }
            doMoneyDetai();

        }
    }

    /**
     * 微信的页面监听（注意：微信的每一次更新版本classname和控件的id都会变动，这些逻辑都需要变动）
     *
     * @param event
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)//com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f
    private void openEnvelope(AccessibilityEvent event) {
        className = event.getClassName().toString();
        if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI".equals(event.getClassName())) {
            //点中了红包，下一步就是去拆红包
//            openLuckyMoney();
            openLuckyMoney2(event);
        } else if ("com.tencent.mm.ui.LauncherUI".equals(event.getClassName())) {

            //在聊天界面,去点中红包
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkKey2();
                }
            }, ConfigEntry.getDelayCanTimeVip() * 1000);          // TODO: 延时抢红包
            //红包页面（上个版本的红包页面）
        } else if ("com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f".equals(event.getClassName())) {
            openLuckyMoney2(event);
            //红包点开详情页
        } else if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI".equals(event.getClassName())) {

            doMoneyDetai();

        }

    }

    /**
     * 打开红包（通过查找控件button的id来打开抢红包）----微信
     */
    private void openLuckyMoney() {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode != null) {
            List<AccessibilityNodeInfo> nodes =
                    rootNode.findAccessibilityNodeInfosByViewId(OPEN_LUCKY_MONEY_BUTTON_ID);
            for (AccessibilityNodeInfo node : nodes) {
                if (node.isClickable()) {
                    Log.i(TAG, "open LuckyMoney");
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }

//        //跳转到红包详情页面进行操作
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                doMoneyDetai();
//            }
//        }, 1000);

    }

    /**
     * 打开微信红包第二种思路：通过遍历页面的控件找到button，然后在抢红包
     *
     * @param event
     */
    private void openLuckyMoney2(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        List<AccessibilityNodeInfo> nodes = new ArrayList<AccessibilityNodeInfo>();
        recycleGetButton(source, nodes);
        for (AccessibilityNodeInfo node : nodes) {
            if (node.isClickable()) {
                Log.i(TAG, "open LuckyMoney");
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }

//        List<AccessibilityNodeInfo> nodes = new ArrayList<AccessibilityNodeInfo>();
//        for (int i = 0; i < source.getChildCount(); i++) {
//            nodes.add(source.getChild(i));
//        }
//        for (AccessibilityNodeInfo node : nodes) {
//            if (node.isClickable()) {
//                Log.i(TAG, "open LuckyMoney");
//                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//            }
//        }
    }

    private void doMoneyDetai() {
        getNameAndMoney();     //获取哦红包昵称和金钱

        if (ConfigEntry.getAutoReply()) {
            autoReply();                //自动回复
        } else {
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);   // 返回
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToRedWeb();
                }
            }, 500);

        }
    }

    private void getNameAndMoney() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        ArrayList<AccessibilityNodeInfo> nodeInfos = new ArrayList<AccessibilityNodeInfo>();
        recycle(nodeInfo, nodeInfos);
        setNameAndMoney(nodeInfos);  //存蓄money
    }

    /**
     * 自动回复的逻辑
     */
    private void autoReply() {
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);   // 返回
        handler.postDelayed(new Runnable() { // 返回主界面，这里延迟执行，为了有更好的交互
            @Override
            public void run() {
                if (fillInputBar(ConfigEntry.getReplyThink())) { // 找到输入框，即EditText
                    findAndPerformAction("android.widget.Button", "发送"); // 点击发送
                    goToRedWeb();
                }
            }
        }, ConfigEntry.getDelayReplyCanTimeVip() * 1000 + 1000);   // TODO: 延时回复
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void recycle(AccessibilityNodeInfo nodeInfo, List<AccessibilityNodeInfo> nodeInfos) {
        if (nodeInfo == null) {
            return;
        }
        String textString = nodeInfo.getText() == null ? "" : nodeInfo.getText().toString();
        if (!textString.equals("")) {
            nodeInfos.add(nodeInfo);
        }
        if (nodeInfo.getChildCount() > 0) {
            for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                recycle(nodeInfo.getChild(i), nodeInfos);
            }
        }

    }

    /**
     * 保存金钱和昵称
     *
     * @param nodeInfos
     */
    private void setNameAndMoney(List<AccessibilityNodeInfo> nodeInfos) {
        if (null == nodeInfos || nodeInfos.size() == 0)
            return;
        redMessage.setNickName(nodeInfos.get(0).getText().toString().trim());
        for (int i = nodeInfos.size() - 1; i > 0; i--) {
            String txt = nodeInfos.get(i) != null ? nodeInfos.get(i).getText().toString().trim() : "";
            //为QQ红包设计，金额实 在"已存入余额"的前面
            if (txt.equals("元")) {
                if ("qq".equals(redMessage.getChannel())) {
                    redMessage.setMoney(nodeInfos.get(i - 2).getText().toString().trim());
                } else {
                    redMessage.setMoney(nodeInfos.get(i - 1).getText().toString().trim());
                }
                return;
            }


        }
    }

    /**
     * 填充输入框
     */
    private boolean fillInputBar(String reply) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode != null) {
            return findInputBar(rootNode, reply);
        }
        return false;
    }


    /**
     * 查找EditText控件
     *
     * @param rootNode 根结点
     * @param reply    回复内容
     * @return 找到返回true, 否则返回false
     */
    private boolean findInputBar(AccessibilityNodeInfo rootNode, String reply) {
        int count = rootNode.getChildCount();
        for (int i = 0; i < count; i++) {
            AccessibilityNodeInfo node = rootNode.getChild(i);

            if ("android.widget.EditText".equals(node.getClassName())) {   // 找到输入框并输入文本
                setText(node, reply);
                return true;
            }

            if (findInputBar(node, reply)) {    // 递归查找
                return true;
            }
        }
        return false;
    }

    /**
     * 设置文本
     */
    private void setText(AccessibilityNodeInfo node, String reply) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle args = new Bundle();
            args.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                    reply);
            node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args);
        } else {
            ClipData data = ClipData.newPlainText("reply", reply);
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(data);
            node.performAction(AccessibilityNodeInfo.ACTION_FOCUS); // 获取焦点
            node.performAction(AccessibilityNodeInfo.ACTION_PASTE); // 执行粘贴
        }
    }

    /**
     * 查找UI控件并点击
     *
     * @param widget 控件完整名称, 如android.widget.Button, android.widget.TextView
     * @param text   控件文本
     */
    private void findAndPerformAction(String widget, String text) {
        // 取得当前激活窗体的根节点
        if (getRootInActiveWindow() == null) {
            return;
        }

        // 通过文本找到当前的节点
        List<AccessibilityNodeInfo> nodes = getRootInActiveWindow().findAccessibilityNodeInfosByText(text);
        if (nodes != null) {
            for (AccessibilityNodeInfo node : nodes) {
                if (node.getClassName().equals(widget) && node.isEnabled()) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK); // 执行点击
                    break;
                }
            }
        }
    }


    /**
     * 去抢红包的页面
     */
    private void goToRedWeb() {
        Intent intent = new Intent(this, GrabRedActivity.class);
        intent.putExtra(GrabRedActivity.RED_MESSAGE, redMessage);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void checkKey1() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            Log.w(TAG, "rootWindow为空");
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("開");
        for (AccessibilityNodeInfo n : list) {
            n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }

    }

    /**
     * 微信找到红包并打开（双重监听控件的text）
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void checkKey2() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            Log.w(TAG, "rootWindow为空");
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("领取红包");
        if (list.isEmpty()) {
            list = nodeInfo.findAccessibilityNodeInfosByText(ENVELOPE_TEXT_KEY);
            for (AccessibilityNodeInfo n : list) {
                Log.i(TAG, "-->微信红包:" + n);
                n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
        } else {
            //最新的红包领起
            for (int i = list.size() - 1; i >= 0; i--) {
                AccessibilityNodeInfo parent = list.get(i).getParent();
                Log.i(TAG, "-->领取红包:" + parent);
                if (parent != null) {
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                }
            }
        }
    }

    /**
     * QQ打开红包（双重监听控件的text）
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void checkKey3() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }
        //查找红包左下角的小logo"QQ红包"
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(QQ_BOTTOM_LEFT);
        if (list.isEmpty()) {
            list = nodeInfo.findAccessibilityNodeInfosByText(QQ_TEXT_KEY);
            for (AccessibilityNodeInfo n : list) {
                n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
        } else {
            //最新的红包领起
            for (int i = list.size() - 1; i >= 0; i--) {
                AccessibilityNodeInfo parent = list.get(i).getParent();
                if (parent != null) {
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                }
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void recycleGetButton(AccessibilityNodeInfo nodeInfo, List<AccessibilityNodeInfo> list) {
        if (nodeInfo == null) {
            return;
        }

        String nodeName = nodeInfo.getClassName().toString();
        if ("android.widget.Button".equals(nodeName)) {
            list.add(nodeInfo);
        }

        if (nodeInfo.getChildCount() > 0) {
            for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                recycleGetButton(nodeInfo.getChild(i), list);
            }
        }
    }

    /**
     * 锁屏唤醒
     */
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

    private void sendNotificationEvent() {
        AccessibilityManager manager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (!manager.isEnabled()) {
            return;
        }
        AccessibilityEvent event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED);
        event.setPackageName(WECHAT_PACKAGENAME);
        event.setClassName(Notification.class.getName());
        CharSequence tickerText = ENVELOPE_TEXT_KEY;
        event.getText().add(tickerText);
        manager.sendAccessibilityEvent(event);
    }


    @Override
    public void onInterrupt() {
        serviceConnected = false;
        Toast.makeText(this, "中断抢红包服务", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
//        serviceConnected = true;
        GrabRedActivity.serviceOPen = false;
        if (SplashActivity.splashOK) {
            Toast.makeText(this, "连接抢红包服务", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, GrabRedActivity.class);    //启动辅助功能回到新版抢红包页面
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


        Intent intent = new Intent();
        intent.setAction("polly.liu.Image");//用隐式意图来启动广播
        intent.putExtra("msg", "辅助服务已开启");
        sendBroadcast(intent);

    }


}