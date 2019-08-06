package com.moogos.spacex.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mogo.space.R;
import com.moogos.spacex.constants.GlobalConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Created by bobo on 2017/1/5.
 */
public class GetVIP {

    private boolean interceptFlag = false;
    private Dialog downloadDialog;
    private int progress = 0;

    private static final int DOWN_UPDATE = 0;

    private ProgressBar mProgress;
    private Context mContext;

    private String apkurl;
    private Thread downLoadThread;

    public GetVIP(Context context, String url) {
        this.mContext = context;
        this.apkurl = url;
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

    public void showDownloadDialogAndStart() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("正在更新");

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.update_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);

        builder.setView(v);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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

    Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkurl);
                InputStream is;
                int length;
                if (apkurl.startsWith("https")) {
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

                File file = new File(UpdateManager.savePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String apkFile = UpdateManager.savePath + "qhb_vip.apk";
                File ApkFile = new File(apkFile);
                if (ApkFile.exists()) {
                    ApkFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        Util.installApk(mContext, apkFile);
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

    private static TrustAnyTrustManager trustAnyTrustManager = new TrustAnyTrustManager();
    private static TrustAnyHostnameVerifier trustAnyHostnameVerifier = new TrustAnyHostnameVerifier();

}
