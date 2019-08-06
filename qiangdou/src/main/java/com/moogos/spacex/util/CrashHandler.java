package com.moogos.spacex.util;

import android.content.Context;
import android.os.StrictMode;

import com.moogos.spacex.constants.GlobalConfig;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLEncoder;

/**
 * 自定义的 异常处理类 , 实现了 UncaughtExceptionHandler接口  
 * 
 */  
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    // 需求是 整个应用程序 只有一个 MyCrash-Handler   
    private static CrashHandler INSTANCE ;  
    private Context context;
      
    //1.私有化构造方法  
    private CrashHandler(){  
          
    }  
      
    public static synchronized CrashHandler getInstance(){  
        if (INSTANCE == null)  
            INSTANCE = new CrashHandler();  
        return INSTANCE;
    }

    public void init(Context context){  
        this.context = context;
    }  
      
  
    public void uncaughtException(Thread arg0,final Throwable ex) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        try {
            String err = getErrorInfo(ex);
//            Log.d("exception uploads:" + err);
            Util.httpPingForceInMain(context, Util.SERVER_EXCEPTION + Util.getCommonParam(context) + "&error=" + URLEncoder.encode(err,"UTF-8"));
        }catch (Exception e){
            if (GlobalConfig.isDebug){
                e.printStackTrace();
            }
        }
        ex.printStackTrace();
        //干掉当前的程
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        pw.close();
        String error= writer.toString();
        return error;
    }
}  