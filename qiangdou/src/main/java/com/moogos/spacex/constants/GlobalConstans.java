package com.moogos.spacex.constants;


/**
 * Created by Apple on 16/11/1.
 */
public class GlobalConstans {


    public static final String EditText = "android.widget.EditText";

    public static final String TextVew = "android.widget.TextView";

    public static final String Button = "android.widget.Button";

    public final static String MONEY_OF_THIS_TIME = "money";

    public final static String MONEY_FROM = "from";

    public final static String NO_MONEY_REASON = "reason";

    public static final String SERACH = "搜索";

    public static final String SEND = "发送";

    public static final String FINISH = "完成";

    public static final String INSTALL = "安装";

    public static final String SURE = "确定";

    public static final String NEXT = "下一步";


    public static final class PayConfig {

        /**
         * 线上环境
         * appid		-- 应用appid
         * privateKey   -- 应用私钥
         * publicKey	-- 平台公钥(platKey)
         * notifyurl	-- 商户服务端接收支付结果通知的地址
         */

        public static final String appid = "3004807420";
        public final static String privateKey = "MIICXQIBAAKBgQCSB1EOMNy/5ojpzM8JFE188pkVPR12qOm44/WdpzPMzV7bYtyiq1lX+UFmqXuteKweTyChfeZWeaNto4z34aYgz5Fsd0aDJuC6785+yVNSCyWgJD5S6ctU/01AFt08d5pRERludP2MI5y/P39PZF/tm+Kq9XwwPxNdnJwqip7c5wIDAQABAoGAV1ltENIFmfyqdT//r+ynFVCAZYXzM+GCPQiPCUjU4XMPhKe0VtGsVcVRa7mBs5h1TIQEftUyjROhwJmOX1Bo8FwhYx+9SZ+4YAxR9zIrYHNzAfEoMHQfPcBLWH28a0tNf66UJd73m7X4k8MckxIkaEi18G4PYUS6QnzIbrXGslECQQDEjPWgL90bL8oT08LZSMXwY+Tt9N7D6tJ92w5QccF3VTklNWxfSiFtxG19mzyg+juJ5QtKjrqpmyUVPiIPb1ObAkEAvjJnL7BIvAWTJS8Leb979M5qxWCm3C2iGifwpef65FCr9l3IJ2a73xNGxoZ0OsLXOGMmJvpeFlQCTr5pCVqOpQJBAJA/65zuw9VKE4LNrXkOgcbVaZSCXGNpGaaoeC7t7dEIyPHX7XtZyoLm4HyIy8xRGhUv9kN30OLdLDAU86ZkS/UCQQC0EkRfgida3GxT6BaVThWt0UCFXtyb2RiAaxAMA3Ymc7pbpq65nyqAKV/41ZN1jsL1P+n/PUuXGDKXGu+XS4WBAkAW/XNw/AoboqfX0h3NaPEH8OyK1YPZG6QJ6MbfQAlvHkI5H/PWXskmdBg4MOboON1BqUsOukjtElCou0cuoUAo";
        public final static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCO56mO8git4THRhfrCSXUj2C0SoBhIAFOXnCbuHYjPww9OO+8vFDpJKEvYQ0qI39nrgHnrrQYIEc9UPrmi+5C1bWOiWvCJyL42j8da1Wal2qVvIxK5jcF/AXHUGNwWNe/LmYMkOb8Artc2bZj78HK+q9MFiMDqkvlylGS1YjPRQQIDAQAB";
        public static final String notifyurl = "http://192.168.0.140:8094/monizhuang/api?type=100";

    }

    public static final class ReasonStr {

        public static final String lateOneMin = "来晚一步";

        public static final String closeMessageDailog = "请关闭QQ锁屏消息弹框";

        public static final String shouManLe = "手慢了";

    }


    public static final class LuckyPackageFrom {
        public static final String fromQQ = "qq";
        public static final String fromWeChat = "wechat";
        public static final String fromAliPay = "alipay";
    }


    public static final class PackageCons {

        public static final String APP_ID = "wxe9f16206a37ab08d";

        public static final String CMWAP = "cmwap";
        public static final String UNIWAP = "uniwap";
        public static final String GWAP3 = "3gwap";

        public static final String ACTION_ID_RESET_STATE = "reset_state";
        public static final String ACTION_ID_DONE_SETTINGS = "done_settings";
        public static final String ACTION_START_SHARE_SUCCESS = "to_share_ok";
        public static final String ACTION_START_SHARE_FAIL = "to_share_fail";


    }


    public static final class QQCons {

        public static final String QQ_PACKAGENAME = "com.tencent.mobileqq";

        public static final String QQ_TEXT_KEY = "[QQ红包]";

        public static final String QQ_OPEN_KEY = "点击拆开";

        public static final String QQ_GET_TEXT_KEY = "查看详情";

        public static final String QQ_MONEY_SPLASHACTIVITY_UI = "com.tencent.mobileqq.activity.SplashActivity";

        public static final String QQ_MONEY_DETAIL_UI = "cooperation.qwallet.plugin.QWalletPluginProxyActivity";

        public static final String QQ_LAUNCHER_UI = "com.tencent.mobileqq.activity.QQLSActivity";


        //qq 口令
        public static final String QQ_KOULIN_LUCKY_PACKAGE = "口令红包";

        public static final String QQ_KOULIN_OPEN_KEY_TXT = "点击输入口令";

        public static final String QQ_GEXING_LUCKY_PACKAGE = "QQ红包个性版";


    }

    public static final class WeChatCons {

        public static final String WECHAT_PACKAGENAME = "com.tencent.mm";

        public static final String WECHAT_TEXT_KEY = "[微信红包]";

        public static final String WECHAT_GET_TEXT_KEY = "领取红包";
        public static final String WECHAT_GET_VIEW_ID = "com.tencent.mm:id/bm4";

        public static final String WECHAT_OPEN_LUCKY_PACKAGE = "开";

        public static final String WECHAT_MONEY_RECEIVE_UI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
        //        public static final String WECHAT_MONEY_RECEIVE_UI = "com.tencent.mm.ui.base.q";
//        com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f
        public static final String WECHAT_MONEY_START_UI = "com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f";

        public static final String WECHAT_MONEY_DETAIL_UI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";

        public static final String WECHAT_LAUNCHER_UI = "com.tencent.mm.ui.LauncherUI";

    }

    public static final class AliPayCons {

        public static final String ALIPAY_PACKAGENAME = "com.eg.android.AlipayGphone";

        public static final String ALIPAY_TEXT_KEY = "[红包]";//Lucky Money

        public static final String ALIPAY_GET_TEXT_KEY = "查看红包";


        public static final String ALIPAY_MONEY_RECEIVE_UI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";

        public static final String ALIPAY_MONEY_DETAIL_UI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";

        public static final String ALIPAY_LAUNCHER_UI = "com.tencent.mm.ui.LauncherUI";


    }


    public final class PreKey {
        public static final String PINGDATE = "pingDate";
        public static final String CONFIG = "config";
        public static final String THANKSLOGAN = "thankSlogan";
        public static final String QIANGDELAYED = "qiangDelayed";
        public static final String SENDDELAYED = "sendDelayed";
        public static final String AUTOREPLY = "autoReply";
        public static final String MYWORDSON = "myWordsOn";
        public static final String DISGUARD = "disguard";
        public static final String ISVOICE = "isVoice";
        public static final String CLOSEAD = "closeAd";
        public static final String ISOPEN = "isOpen";
        public static final String ISQQOPEN = "isQQOpen";
        public static final String SWITCHEDSENT = "switchedSent";
        public static final String PKGSENT = "pkgSent";
        public static final String ISSHARED = "isShared";
        public static final String PHONE = "phone";
        public static final String DL = "dl";

    }

    public final class GlobalDefault {
        public static final String DEFAULT_PINGDATE = "2017-01-01";
        public static final String DEFAULT_CONFIG = "{}";
        public static final String DEFAULT_THANKSLOGAN = "谢谢老板";
        public static final long DEFAULT_QIANGDELAYED = 0;
        public static final long DEFAULT_SENDDELAYED = 0;
        public static final boolean DEFAULT_AUTOREPLY = true;
        public static final boolean DEFAULT_MYWORDSON = false;
        public static final boolean DEFAULT_DISGUARD = false;
        public static final boolean DEFAULT_ISVOICE = true;
        public static final boolean DEFAULT_CLOSEAD = false;
        public static final boolean DEFAULT_ISOPEN = true;
        public static final boolean DEFAULT_ISQQOPEN = true;
        public static final boolean DEFAULT_SWITCHEDSENT = false;
        public static final boolean DEFAULT_PKGSENT = false;
        public static final boolean DEFAULT_ISSHARED = false;
        public static final String DEFAULT_PHONE = "-1";

    }


    public static final class URL {
//        public static String SERVER_URL_ADD = "http://hongb.wotever.cn/item/add?";
//        public static String SERVER_URL_START = "http://hongb.wotever.cn/status/startservice?";
//        public static String SERVER_URL_ACTIVATE = "http://hongb.wotever.cn/status/active?";
//        public static String SERVER_URL_CFG = "http://hongb.wotever.cn/status/cfg?";
//        public static String SERVER_URL_PING = "http://hongb.wotever.cn/status/ping?";
//        public static String SERVER_URL_LOGIN = "http://hongb.wotever.cn/status/login?";
//        public static String SERVER_URL_PRELOGIN = "http://hongb.wotever.cn/status/prelogin?";
//        public static String INSTALL_PING_URL = "http://hongb.wotever.cn/status/inst?";
//        public static String DOWNLOAD_PING_URL = "http://hongb.wotever.cn/status/dl?";
//        public static String BADSTATE = "http://hongb.wotever.cn/status/bad?";
//        public static String ACTION_URL = "http://hongb.wotever.cn/status/click?";//clkid=xxx&aid=xxx
//        public static String ACTION_MONITOR_URL = "http://hongb.wotever.cn/status/monitor?";//eve=1
//        public static String PACKAGE_URL = "http://hongb.wotever.cn/dlist?";
//        public static String PAID_URL = "http://hongb.wotever.cn/preorder?";
//        public static String VIP_APK_URL = "http://c3.moogos.com/sdk/app/vip/com.example.veryip.apk?";
//        public static String UPDATE_URL = "http://hongb.wotever.cn/update/version.json?";
//        public static String UPDATE_URL_TEST = "http://192.168.2.211:8099/update/version.json?";
//        public static String SERVER_EXCEPTION = "http://hongb.wotever.cn/status/error?";
//        public static String ITEMS_QUERY_URL = "http://hongb.wotever.cn/items/query?";
//        public static String RANK_URL = "http://hongb.wotever.cn/rank/query";
//        public static String SHARE_URL = "http://hongb.wotever.cn/hongb?pkg=so08";
//        public static String VIEDO_URL = "http://hongb.wotever.cn/helpvideo.html";
//        public static String HELP_URL = "http://hongb.wotever.cn/help.html";
    }


}
