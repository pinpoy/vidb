package com.moogos.spacex.constants;


import android.util.Log;

import com.mogo.space.BuildConfig;
import com.moogos.spacex.logger.SpiderLogger;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.WeakHashMap;


import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * ProjectName: RetrofitManager
 * Description: Retrofit管理类
 * <p>
 * review by xupeng
 */
public class RetrofitManager {
    private static String TAG = "RetrofitManager";
    private static RetrofitManager manager;

    private WeakHashMap<String, Retrofit> retrofitMap = new WeakHashMap<>();
    private OkHttpClient okHttpClient;
    private final int DEFAULT_TIMEOUT = 30;
    private final int READ_AND_WRITE_TIMEOUT = 30;

    public static RetrofitManager getManager() {
        if (manager == null) {
            synchronized (RetrofitManager.class) {
                if (manager == null) {
                    manager = new RetrofitManager();
                }
            }
        }
        return manager;
    }

    private RetrofitManager() {
        okHttpClient = new OkHttpClient();



        // 添加拦截器，打印网络请求信息(正式打包去掉，否会敏感数据会泄漏)
        if (HttpUrls.BT_DEBUG.equalsIgnoreCase(BuildConfig.BUILD_TYPE)) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient.interceptors().add(interceptor);
        }


        okHttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                long t1 = System.nanoTime();
                Request request = chain.request();
                Log.i(TAG, String.format("Sending request %s on %s%n%s",
                        request.url(), chain.connection(), request.headers()));

                Response response = chain.proceed(request);

                long t2 = System.nanoTime();
                Log.i(TAG, String.format("Received response for %s in %.1fms%n%s",
                        request.url(), (t2 - t1) / 1e6d, response.headers()));
                return response;
            }
        });



    }

    private OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    private Retrofit getRetrofit(String baseUrl) {
        // new retrofit
        Retrofit retrofit = retrofitMap.get(baseUrl);
        SpiderLogger.getLogger().i(TAG, baseUrl);
        if (retrofit == null) {
            OkHttpClient tempOkHttpClient = okHttpClient;


            retrofit = new Retrofit.Builder()
                    .client(tempOkHttpClient)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(StringConverterFactory.create())       // 字符串请求或响应转换工厂
                    .addConverterFactory(GsonConverterFactory.create())         // google gson请求或响应转换工厂
                    .baseUrl(baseUrl)
                    .build();

            retrofitMap.put(baseUrl, retrofit);
        }
        return retrofit;
    }

    /**
     * 通过url获取retrofit实例；若为空，则创建retrofit实例
     *
     * @param baseUrl 服务器地址
     * @return
     */
    public static Retrofit get(String baseUrl) {
        return getManager().getRetrofit(baseUrl);
    }

    public static OkHttpClient getOkClient() {
        return getManager().getOkHttpClient();
    }
}