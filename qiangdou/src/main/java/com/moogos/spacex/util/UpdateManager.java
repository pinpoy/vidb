package com.moogos.spacex.util;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mogo.space.R;
import com.moogos.spacex.constants.GlobalConfig;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class UpdateManager {

    private Context mContext;


    private Dialog noticeDialog;

    private Dialog downloadDialog;
    /* 下载包安装路径 */
    public static final String savePath = "/sdcard/kxqhb/";

    private static double remoteVer = 1.0;
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;

    private static final int DOWN_UPDATE = 1;

    private int progress;

    private Thread downLoadThread;

    private boolean interceptFlag = false;

    public static void checkNeedsUpdate(final Context context) { // 这个方法没有用到什么类的成员变量，加static，效率更高些
//		if (!UpdateManager.inited) {
//
//			UpdateManager.inited = true;
//
// }

        new UpdateManager(context);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 检查更新
     * @param context
     */
    public UpdateManager(final Context context) {
        this.mContext = context;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PackageManager packageManager = mContext.getPackageManager();
                    PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
                    double localVer = Double.parseDouble(packInfo.versionName);
                    String result = NetUtils.httpGet(Util.UPDATE_URL, Util.getCommonParam(context));//GlobalConfig.isDebug ? Util.UPDATE_URL_TEST :
                    JSONObject resJson = new JSONObject(result);
                    int status = resJson.getInt("status");
                    if (status == 0) {
                        String P = resJson.getString("P");
                        final JSONObject json = new JSONObject(Util.base64Decode(P));
                        MLog.d("updateData==" + json);
                        remoteVer = json.optDouble("version", 1.0);
                        GlobalConfig.VIP_URL = json.optString("vip", "");  //vip的url
                        boolean force = json.optBoolean("force", false);
                        if (localVer < remoteVer || force) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    UpdateManager.this.showNoticeDialog(json);
                                }
                            });
                        } else {
                            MLog.d("Get Update Version Failed");
                        }
                    }


                } catch (Exception e) {
                    Log.d("UpdateManager", e.toString());
                }
            }
        }).start();
    }

    private static String apkUrl = "";

    private void showNoticeDialog(JSONObject json) {
        String details = json.optString("details", "");
        apkUrl = json.optString("url", "");
        if (!apkUrl.equals("")) {
            Builder builder = new Builder(mContext);
            builder.setTitle("软件版本更新");
            builder.setMessage("亲,升级新版更赞哦\n" + details);
            builder.setPositiveButton("下载", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    showDownloadDialog();
                }
            });
            builder.setNegativeButton("以后再说", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            noticeDialog = builder.create();
            noticeDialog.show();
        }
    }

    private void showDownloadDialog() {
        Builder builder = new Builder(mContext);
        builder.setTitle("正在更新");

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.update_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);

        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        downloadDialog = builder.create();
        downloadDialog.show();

        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    private static TrustAnyTrustManager trustAnyTrustManager = new TrustAnyTrustManager();
    private static TrustAnyHostnameVerifier trustAnyHostnameVerifier = new TrustAnyHostnameVerifier();

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);
                InputStream is;
                int length;
                if (apkUrl.startsWith("https")) {
                    SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init(null, new TrustManager[]{trustAnyTrustManager}, new java.security.SecureRandom());
                    HttpsURLConnection urlCon = (HttpsURLConnection) url.openConnection();
                    urlCon.setDoInput(true);
                    urlCon.setSSLSocketFactory(sc.getSocketFactory());
                    urlCon.setHostnameVerifier(trustAnyHostnameVerifier);
                    urlCon.setRequestMethod("GET");
                    urlCon.setUseCaches(false);
                    urlCon.setConnectTimeout(30000);
                    urlCon.setReadTimeout(60000);
                    urlCon.connect();
                    length = urlCon.getContentLength();
                    Log.e(GlobalConfig.TAG, "length = " + length);
                    is = urlCon.getInputStream();
                } else {
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    length = conn.getContentLength();
                    is = conn.getInputStream();
                }

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String apkFile = savePath + "qhb_" + remoteVer + ".apk";
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        installApk(apkFile);
                        downloadDialog.dismiss();
                        break;
                    }
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载.

                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "下载出错了 %>_<%", Toast.LENGTH_LONG).show();
                    }

                });
            }
        }
    };


    /**
     * 安装apk
     *
     * @param filePath
     */
    private void installApk(String filePath) {
        File apkfile = new File(filePath);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);

    }

}
