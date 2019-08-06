package com.moogos.spacex.core;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.moogos.spacex.constants.ConfigEntry;
import com.moogos.spacex.constants.GlobalConfig;
import com.moogos.spacex.constants.GlobalConstans;
import com.moogos.spacex.model.RecievedRedBagEntity;
import com.moogos.spacex.model.RedBagEntity;
import com.moogos.spacex.util.MLog;
import com.moogos.spacex.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bobo on 2016/12/26.
 */
public class QQAction {

    private Handler handler = new Handler();
    private boolean qqDianjichaikaiClicked = false;
    private boolean retToSplashActivity = false;
    private boolean qqWriteDone = false;
    private boolean qqSendDone = false;
    public boolean qqLate = false;

    private boolean waitingSendDone = false;
    private int koulingState = 0;
    private boolean qqSplashActivityAndCannotGoOn = false;

    private QiangHongBaoService service;
    private Context context;

    private boolean getQQMoney = false;

    RedBagEntity entity = null;

    enum EVENT_SEQ {
        GET_NAME_MONEY, GET_QQ_NAME_MONEY
    }

    private static int depth = 0;

    public QQAction(RedBagEntity e, QiangHongBaoService tmp_service) {
        service = tmp_service;
        context = tmp_service.getApplicationContext();
        entity = e;
    }

    public void reset() {
        qqDianjichaikaiClicked = false;
        retToSplashActivity = false;
        qqWriteDone = false;
        qqSendDone = false;
        qqLate = false;

        getQQMoney = false;
        koulingState = 0;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void handleQQ(String pageClassName, AccessibilityEvent event) {
        try {

            int type = event.getEventType();

            if (type == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                // Log.d("handleQQ TYPE_WINDOW_STATE_CHANGEDclass:：" + pageClassName);
                if ("com.tencent.mobileqq.activity.SplashActivity".equals(pageClassName)) {
                    AccessibilityNodeInfo nodeInfo = event.getSource();
                    doInSplashActivity(nodeInfo, type);
                } else if ("cooperation.qwallet.plugin.QWalletPluginProxyActivity".equals(pageClassName)) {
                    AccessibilityNodeInfo nodeInfo = event.getSource();
                    //QQ保存money
                    doInQWalletPluginProxyActivity(nodeInfo, type);
                } else if ("com.tencent.mobileqq.activity.QQLSActivity".equals(pageClassName)) {
                    entity.setReason("请关闭QQ锁屏消息弹框");
                    Toast.makeText(context, "取消QQ\"锁屏显示消息弹框\"抢到红包概率更高哦！", Toast.LENGTH_LONG).show();
                }
            } else if (type == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                //  Log.d("handleQQ TYPE_WINDOW_CONTENT_CHANGEDClass:" + event.getClassName() + " pageClassName:" + pageClassName );
                if ("com.tencent.mobileqq.activity.SplashActivity".equals(pageClassName)) {
                    AccessibilityNodeInfo nodeInfo = event.getSource();
                    while (nodeInfo != null && nodeInfo.getParent() != null) {// 拿到根节点
                        nodeInfo = nodeInfo.getParent();
                    }
                    doInSplashActivity(nodeInfo, type);
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            //Log.d("printStackTrace:"+e);
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void doInQWalletPluginProxyActivity(AccessibilityNodeInfo nodeInfo, int eventtype) {
        List<AccessibilityNodeInfo> nodeInfos0 = nodeInfo.findAccessibilityNodeInfosByText("来晚一步");//NullPointer
        if (nodeInfos0.size() > 0) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            retToSplashActivity = true;
            qqLate = true;
            entity.setReason("来晚一步");
            // Log.d("doInQWalletPluginProxyActivity: 来晚一步 ");
        } else {
            if (!getQQMoney) {
                ArrayList<AccessibilityNodeInfo> nodeInfos = new ArrayList<AccessibilityNodeInfo>();
                recycle(nodeInfo, EVENT_SEQ.GET_QQ_NAME_MONEY, nodeInfos);
                boolean hasGotMoney = setNameAndMoney1(nodeInfos);
                if (hasGotMoney) {
                    service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    retToSplashActivity = true;
                    getQQMoney = true;
                    //  Log.d("doInQWalletPluginProxyActivity: getMoney entity: "+entity);
                }
            }

        }
    }

    private boolean setNameAndMoney1(List<AccessibilityNodeInfo> nodeInfos) {

        RecievedRedBagEntity recievedRedBagEntity = new RecievedRedBagEntity();
        DbUtils db = DbUtils.create(context);

        int liuyan = 0;
        int yuan = 0;
        for (int i = nodeInfos.size() - 1; i > 0; i--) {

            String txt = nodeInfos.get(i) != null ? nodeInfos.get(i).getText().toString().trim() : "";
//            Log.d("txttxttxttxttxttxt:"+txt);
            MLog.d("txt = " + txt);
            if (txt.equals("留言")) {
                liuyan = i;
            }
            if (i == liuyan - 1) {
                entity.setNickname(txt.indexOf("已存入零钱") >= 0 ? "" : txt);
            }
            if (txt.equals("元")) {
                yuan = i;
            }
            if (i == yuan - 1) {
                try {
                    Float.parseFloat(txt);
                    entity.setMoney(txt);
                    entity.setStatus(0);
                } catch (Exception e) {

                }
            }

        }
        entity.sendRedBag(GlobalConfig.RED_BAG_DataType);
        if (!RedBagEntity.DEFAULT_MONEY.equals(entity.getMoney())) {//异常数据不予存储
            try {
                recievedRedBagEntity.setName(entity.getSend_nickname());
                recievedRedBagEntity.setCoin(entity.getMoney());
                recievedRedBagEntity.setTime(System.currentTimeMillis());
                recievedRedBagEntity.setFrom(entity.getFrom());
//                Log.d("db.save:"+entity.toString());
                db.save(recievedRedBagEntity);
                db.close();
                return true;
            } catch (Exception e) {
//                Log.w("db.save:" + entity.toString() + "   exception:"+e);
            }

        }

        return false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void recycle(AccessibilityNodeInfo nodeInfo, EVENT_SEQ type, List<AccessibilityNodeInfo> nodeInfos) {
        if (nodeInfo == null) {
            return;
        }

        String log = "";
        for (int i = 0; i < depth; i++) {
            log += "  ";
        }
        String textString = nodeInfo.getText() == null ? "" : nodeInfo.getText().toString();
        switch (type) {
            case GET_NAME_MONEY:

                if (!textString.equals("")) {
//                    Log.d("GET_NAME_MONEY：" + textString);
                    nodeInfos.add(nodeInfo);
                }

                break;
            case GET_QQ_NAME_MONEY:
                if (!textString.equals("")) {
//                    Log.d("GET_QQ_NAME_MONEY：" + textString);
                    nodeInfos.add(nodeInfo);
                }
                break;
            default:
                break;
        }

        if (nodeInfo.getChildCount() > 0) {
            depth++;
            for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                recycle(nodeInfo.getChild(i), type, nodeInfos);
            }
            depth--;
        }

    }

    private void doInSplashActivity(AccessibilityNodeInfo nodeInfo, int eventtype) {
        if (!qqSplashActivityAndCannotGoOn) {
            final List<AccessibilityNodeInfo> list1 = nodeInfo.findAccessibilityNodeInfosByText(GlobalConstans.SERACH);
            if (list1.size() > 0) {
                for (AccessibilityNodeInfo an : list1) {
                    if (an.getClassName().toString().equals(GlobalConstans.EditText)) {
                        qqSplashActivityAndCannotGoOn = true;
                        entity.setReason("您停留在QQ页");
                        service.gotoMyActivity(" 8.doInSplashActivity qqSplashActivityAndCannotGoOn");
                        Util.httpPing(context, Util.BADSTATE + Util.getCommonParam(context) + "&reason=qqSplashActivityAndCannotGoOn");
                    }
                }

            }
        }
//        recycleTest(nodeInfo);
        switch (koulingState) {//口令红包后续处理
            case 1:
                final List<AccessibilityNodeInfo> list1 = nodeInfo.findAccessibilityNodeInfosByText(GlobalConstans.QQCons.QQ_KOULIN_OPEN_KEY_TXT);//NullPointer
                if (list1.size() > 0) {
                    list1.get(list1.size() - 1).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    //  Log.d("doInSplashActivity qq红包 eventtype: 点击输入口令 clicked" );
                    koulingState = 2;
                }
                break;
            case 2:
                final List<AccessibilityNodeInfo> list2 = nodeInfo.findAccessibilityNodeInfosByText(GlobalConstans.SEND);
                if (list2.size() > 0) {
                    list2.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    qqDianjichaikaiClicked = true;
                    koulingState = 0;
                    // Log.d("doInSplashActivity qq红包 eventtype: 发送 clicked" );
                }
                break;
            default:
                break;
        }
        if (!qqDianjichaikaiClicked && koulingState == 0) {
            final List<AccessibilityNodeInfo> list0 = nodeInfo.findAccessibilityNodeInfosByText("QQ红包");
            //  Log.d("doInSplashActivity TYPE_WINDOW_CONTENT_CHANGED: list0: "+list0.size());
            final List<AccessibilityNodeInfo> list = list0.get(list0.size() - 1).getParent().findAccessibilityNodeInfosByText("口令红包");
            if (list != null && list.size() > 0) {
                //   Log.d("doInSplashActivity TYPE_WINDOW_CONTENT_CHANGED: 口令红包 ");
                list.get(list.size() - 1).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                koulingState = 1;
            } else {
                List<AccessibilityNodeInfo> nodeInfos0 = nodeInfo.findAccessibilityNodeInfosByText("点击拆开");
                //  Log.d("doInSplashActivity eventtype:" + eventtype + " doInSplashActivity: 点击拆开 cnt:" + nodeInfos0.size());
                if (nodeInfos0.size() > 0) {
                    nodeInfos0.get(nodeInfos0.size() - 1).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    qqDianjichaikaiClicked = true;
                } else {
                    List<AccessibilityNodeInfo> nodeInfos1 = nodeInfo.findAccessibilityNodeInfosByText("QQ红包个性版");
                    //  Log.d("doInSplashActivity eventtype:" + eventtype + " doInSplashActivity: 个性红包 cnt:" + nodeInfos1.size());
                    if (nodeInfos1.size() > 0) {
                        nodeInfos1.get(nodeInfos1.size() - 1).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        qqDianjichaikaiClicked = true;
                    }
                }
            }
        }
        if (retToSplashActivity) {
            if (!qqWriteDone) {
                if (ConfigEntry.getAutoReply()) {
                    recycleWrite(nodeInfo);
                    qqWriteDone = true;
                    // Log.d("doInSplashActivity qqWriteDone thanks");
                } else {//不必自动回复
//                    service.gotoMyActivity(" 6.qq no need to auto reply");
                    qqSendDone = true;
                }
            }
            if (!qqSendDone && !waitingSendDone) {
                final List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("发送");
                // Log.d("TYPE_WINDOW_CONTENT_CHANGEDClass: 发送 nodes:" + list.size()+" page:"+pageClassName);
                if (list.size() > 0) {
                    long t = ConfigEntry.getDelayReplyCanTimeVip() * 1000;
                    if (t == 0) {
                        list.get(list.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        qqSendDone = true;
                    } else {
                        waitingSendDone = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    list.get(list.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    qqSendDone = true;
                                    // Log.d("doInSplashActivity send thanks");
                                    waitingSendDone = false;
                                } catch (Exception e) {

                                }
                            }
                        }, t);
                    }
                }

            }
            if (qqSendDone) {
                service.gotoMyActivity(" 5.doInSplashActivity qq get money & gotoMyActivity");
            }

        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void recycleWrite(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }

        String nodeName = nodeInfo.getClassName().toString();
        if (nodeName.equals("android.widget.EditText")) {
            String words = ConfigEntry.getReplyThink();
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            ClipboardManager clipboard = (ClipboardManager) service.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", words);
            clipboard.setPrimaryClip(clip);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
            return;
        }

        if (nodeInfo.getChildCount() > 0) {
            for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                recycleWrite(nodeInfo.getChild(i));
            }
        }
    }
}
