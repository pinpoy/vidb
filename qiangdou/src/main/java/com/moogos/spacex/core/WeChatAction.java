package com.moogos.spacex.core;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.lidroid.xutils.DbUtils;
import com.moogos.spacex.constants.ConfigEntry;
import com.moogos.spacex.constants.GlobalConfig;
import com.moogos.spacex.constants.GlobalConstans;
import com.moogos.spacex.constants.User;
import com.moogos.spacex.model.RecievedRedBagEntity;
import com.moogos.spacex.model.RedBagEntity;
import com.moogos.spacex.util.MLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bobo on 2016/12/26.
 */


public class WeChatAction {

    private Handler handler = new Handler();

//    private boolean weinxinhongbaoClicked = false; //

    private boolean lingquClicked = false;  // 界面 领取红包

    private boolean kaiPressed = false;     //点击开红包

    private boolean writeDone = false;  //输入框 input

    private boolean weinxinhongbaoClicked = false; //开锁屏 抢红包

    private boolean waitingSendDone = false;  // 等待发送感谢语

    private boolean sendDone = false;   //发送感谢语完成

    public boolean retToInLauncherUI = false;  // launcherUI抢到红包返回

    public boolean weChatLate = false;   // 红包流程完成
    public boolean retToInChatUI = false;

    private int depth = 0;

    private QiangHongBaoService service;
    private Context context;

    RedBagEntity entity = null;

    enum EVENT_SEQ {
        GET_NAME_MONEY, GET_QQ_NAME_MONEY
    }

    public WeChatAction(RedBagEntity e, QiangHongBaoService tmp_service) {
        service = tmp_service;
        context = tmp_service.getApplicationContext();
        entity = e;
    }

    public void reset() {
        writeDone = false;
        sendDone = false;
        lingquClicked = false;
        weinxinhongbaoClicked = false;
        kaiPressed = false;
        retToInLauncherUI = false;
        weChatLate = false;
        retToInChatUI = false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void handleWechat(String pageClassName, AccessibilityEvent event) {
        try {
            MLog.d("pageClassName= " + pageClassName + " type= " + event.getEventType());
            int type = event.getEventType();
            if (type == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {  // 是否进入微信界面
                MLog.d("TYPE_WINDOW_STATE_CHANGEDclass:handleWechat：" + pageClassName);
                if (GlobalConstans.WeChatCons.WECHAT_LAUNCHER_UI.equals(pageClassName)) {
                    AccessibilityNodeInfo nodeInfo = event.getSource();
                    doInLauncherUI(nodeInfo, type);
                } else if (GlobalConstans.WeChatCons.WECHAT_MONEY_RECEIVE_UI.equals(pageClassName)) {
                    MLog.d("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI===" + pageClassName);
                    AccessibilityNodeInfo nodeInfo = event.getSource();
                    doInLuckyMoneyReceiveUI(nodeInfo, type);
                } else if (GlobalConstans.WeChatCons.WECHAT_MONEY_DETAIL_UI.equals(pageClassName)) {
                    AccessibilityNodeInfo nodeInfo = event.getSource();
                    //微信保存money
                    doInLuckyMoneyDetailUI(nodeInfo, type);
                } else if (GlobalConstans.WeChatCons.WECHAT_MONEY_START_UI.equals(pageClassName)) {
                    AccessibilityNodeInfo nodeInfo = event.getSource();
                    doInLuckyMoneyReceiveUI(nodeInfo, type);
                }
            } else if (type == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                MLog.d("MAIN CONTENT pageClassName= " + pageClassName + " type= " + event.getEventType() + " size=" + event.getSource().findAccessibilityNodeInfosByText("领取红包").size());
                if (GlobalConstans.WeChatCons.WECHAT_LAUNCHER_UI.equals(pageClassName)) {
                    AccessibilityNodeInfo nodeInfo = event.getSource();
                    while (nodeInfo != null && nodeInfo.getParent() != null) {// 拿到根节点
                        nodeInfo = nodeInfo.getParent();
                    }
                    doInLauncherUI(nodeInfo, type);
                } else if (GlobalConstans.WeChatCons.WECHAT_MONEY_RECEIVE_UI.equals(pageClassName)) {
                    AccessibilityNodeInfo nodeInfo = event.getSource();
                    while (nodeInfo != null && nodeInfo.getParent() != null) {// 拿到根节点
                        nodeInfo = nodeInfo.getParent();
                    }
                    doInLuckyMoneyReceiveUI(nodeInfo, type);
                }
            }
        } catch (Exception e) {

        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void doInLuckyMoneyReceiveUI(AccessibilityNodeInfo nodeInfo, int eventtype) {
        List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByText(GlobalConstans.ReasonStr.shouManLe);//NullPointer
        if (nodeInfos.size() > 0) {
            entity.setReason(GlobalConstans.ReasonStr.lateOneMin);
            // Log.d("doInLuckyMoneyReceiveUI eventtype:" + eventtype + "nodeInfo:" + nodeInfo.getClass() + " LuckyMoneyReceiveUI: 手慢了 cnt:" + nodeInfos.size());
            weChatLate = true;
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);

        } else {
            // Log.d("doInLuckyMoneyReceiveUI eventtype:"+eventtype+"nodeInfo:"+nodeInfo.getClass()+" kaiPressed: kaiPressed:" + kaiPressed);
            if (!kaiPressed) {
                List<AccessibilityNodeInfo> nodes = new ArrayList<AccessibilityNodeInfo>();
                recycleGetButton(nodeInfo, nodes);
                final List<AccessibilityNodeInfo> nodes1 = nodes;
                MLog.d("doInLuckyMoneyReceiveUI eventtype:" + eventtype + " LuckyMoneyReceiveUI 拆红包/開 cnt:" + nodes.size());
                long t = ConfigEntry.getDelayCanTimeVip() * 1000;
                if (nodes.size() > 0) {
                    if (t == 0) {
                        nodes.get(nodes.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        kaiPressed = true;
                    } else {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                nodes1.get(nodes1.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                kaiPressed = true;
                            }
                        }, t);
                    }
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void doInLauncherUI(final AccessibilityNodeInfo nodeInfo, int eventtype) {

        if (kaiPressed && retToInLauncherUI && sendDone) {
            service.gotoMyActivity(" 3.get money & gotoMyActivity ");
            return;
        }

        if ((sendDone || !ConfigEntry.getAutoReply()) && retToInChatUI && nodeInfo.findAccessibilityNodeInfosByText("领取红包").size() > 0) {
            retToInLauncherUI = true;
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }

        if (!weinxinhongbaoClicked && !lingquClicked) {
            try {
                if (User.getWeixin_name().equalsIgnoreCase("")) {
//                    MLog.d("update weixin_name c=" + nodeInfo.findAccessibilityNodeInfosByText("微信号").size());
                    String v = "";
                    if (nodeInfo.findAccessibilityNodeInfosByText("微信号").size() > 0) {
                        v = nodeInfo.findAccessibilityNodeInfosByText("微信号").get(0).getText().toString();
//                        MLog.d("update weixin_name name=" + nodeInfo.findAccessibilityNodeInfosByText("微信号").get(0).getText());
                    }
                    if (!v.trim().equalsIgnoreCase("")) {
//                        MLog.d("update weixin_name v=" + v);
                        User.setWeixin_name(context, v.replace("微信号", "").replace("：", ""));
//                        MLog.d("update weixin_name v=" + v.replace("微信号", "").replace("：", ""));
                    }
                }
            } catch (Exception e) {
                MLog.e("getWeixin_name  ", e);
            }
        }

        if (!weinxinhongbaoClicked) {
            List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByText(GlobalConstans.WeChatCons.WECHAT_TEXT_KEY);
//            MLog.d("doInLauncherUI_ 00 eventtype:" + eventtype + " doInLauncherUI: [微信红包] cnt:" + nodeInfos.size());
            if (nodeInfos.size() > 0 && nodeInfos.get(nodeInfos.size() - 1).isClickable()) {
                //列表页
                nodeInfos.get(nodeInfos.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                weinxinhongbaoClicked = true;
//                MLog.d("doInLauncherUI_ 01 eventtype:" + eventtype + " doInLauncherUI: [微信红包] really");
            }
        }
        MLog.d("childCount = " + nodeInfo.getChildCount() + " 微信号 = " + User.getWeixin_name());
//        Util.listALl(nodeInfo);
        if (!lingquClicked) {
            //聊天界面

            List<AccessibilityNodeInfo> nodeInfos0 = nodeInfo.findAccessibilityNodeInfosByText(GlobalConstans.WeChatCons.WECHAT_GET_TEXT_KEY);
            MLog.d("doInLauncherUI_ 10 eventtype:" + eventtype + " doInLauncherUI: 领取红包 cnt:" + nodeInfos0.size());
            if (nodeInfos0.size() > 0) {
                for (int i = nodeInfos0.size() - 1; i >= 0; i--) {
                    AccessibilityNodeInfo parent = nodeInfos0.get(i).getParent();
                    if (parent != null) {
                        parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                        break;
                    }
                }
//                nodeInfos0.get(nodeInfos0.size() - 1).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);//NullPointer
                lingquClicked = true;
                MLog.d("doInLauncherUI_ 11 eventtype:" + eventtype + " doInLauncherUI: 领取红包 really");
            }
        }
        MLog.d("doInLauncherUI lingquClicked=" + lingquClicked + " weinxinhongbaoClicked=" + weinxinhongbaoClicked);


        if (!kaiPressed) {
            final List<AccessibilityNodeInfo> nodes = new ArrayList<AccessibilityNodeInfo>();
            MLog.d("doInLuckyMoneyReceiveUI eventtype:" + eventtype + " LuckyMoneyReceiveUI 拆红包/開 cnt:" + nodes.size());
            recycleGetButton(nodeInfo, nodes);
            if (nodes.size() > 0) {
                nodes.get(nodes.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                kaiPressed = true;
            }
        }


//        if (!lingquClicked && !weinxinhongbaoClicked) {
//            MLog.d("enter childCount = " + nodeInfo.getChildCount());
//            for (int i = 0; i < nodeInfo.getChildCount(); i++) {
//                MLog.d("enter 2 childNode = " + nodeInfo.getChild(i).isClickable() + " " + nodeInfo.getChild(i).getClassName() + " " + nodeInfo.getChild(i).findAccessibilityNodeInfosByText("微信").size());
//                if (nodeInfo.getChild(i).getClassName().equals("com.tencent.mm.ui.mogic.WxViewPager")) {
//                    AccessibilityNodeInfo n = nodeInfo.getChild(i);
//                    MLog.d(n.getClassName() + " = android.widget.TextView text=" + " " + n.getChildCount());
//                    if (n.getChildCount() > 0) {
//                        AccessibilityNodeInfo m = n.getChild(0);
//                        if (m.getClassName().equals("android.widget.ListView")) {
//                            if (m.getChildCount() > 0) {
//                                AccessibilityNodeInfo o = m.getChild(0);
//                                if (o.getClassName().equals("android.widget.LinearLayout") && o.getChildCount() == 0) {
//                                    MLog.d("is CLick = " + o.isClickable());
//                                    o.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
        if (kaiPressed && !sendDone && !entity.getMoney().equals(RedBagEntity.DEFAULT_MONEY)) {
            if (kaiPressed && eventtype == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                if (ConfigEntry.getAutoReply()) {
                    recycleWrite(nodeInfo);
                    writeDone = true;
//                      MLog.d(" doInLauncherUI 2 TYPE_WINDOW_CONTENT_CHANGEDClass: writeThanks "+" page:"+pageClassName);//NullPointer
                } else {//不必自动回复
//                    if (retToInChatUI) {
//                        service.gotoMyActivity(" 1.no need to auto reply");
//                    }else{
//                        retToInChatUI = true;
//                    }
                }
            } else if (writeDone && !waitingSendDone && eventtype == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                final List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(GlobalConstans.SEND);//NullPointer
                //  Log.d(" doInLauncherUI TYPE_WINDOW_CONTENT_CHANGEDClass: 发送 nodes:" + list.size()+" page:"+pageClassName);//NullPointer
                if (list.size() > 0) {
                    long t = ConfigEntry.getDelayReplyCanTimeVip() * 1000;

                    if (t == 0) {
                        sendDone = true;

                        list.get(list.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);

                    } else {
                        waitingSendDone = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    sendDone = true;
                                    list.get(list.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    waitingSendDone = false;
                                } catch (Exception e) {
                                }
                            }
                        }, t);
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void doInLuckyMoneyDetailUI(AccessibilityNodeInfo nodeInfo, int eventtype) {
        ArrayList<AccessibilityNodeInfo> nodeInfos = new ArrayList<AccessibilityNodeInfo>();
        recycle(nodeInfo, EVENT_SEQ.GET_NAME_MONEY, nodeInfos);
        boolean hasGotMoney = setNameAndMoney1(nodeInfos);  //存蓄money
        if (hasGotMoney) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            retToInChatUI = true;
//            retToInLauncherUI = true;
            // Log.d("doInLuckyMoneyDetailUI: getMoney entity: "+entity);
        }
    }

    /**
     * 记录红包的money
     * @param nodeInfos
     * @return
     */
    private boolean setNameAndMoney1(List<AccessibilityNodeInfo> nodeInfos) {

        RecievedRedBagEntity recievedRedBagEntity = new RecievedRedBagEntity();
        DbUtils db = DbUtils.create(context);
        int liuyan = 0;
        int yuan = 0;
        for (int i = nodeInfos.size() - 1; i > 0; i--) {

            String txt = nodeInfos.get(i) != null ? nodeInfos.get(i).getText().toString().trim() : "";
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
                MLog.e("put recievedRedBagEntity failed", e);
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
