package com.playhut.partner.networkapi;


import com.playhut.partner.constants.NetworkConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * 获取Retrofit
 */
public class ApiManager {

    private static Retrofit retrofit = null;

    private ApiManager() {
    }

    /**
     * 单例获取Retrofit对象
     *
     * @return
     */
    private static synchronized Retrofit getInstance() {
        if (retrofit == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.connectTimeout(NetworkConstants.NET_CONNECT_TIMEOUT, TimeUnit.SECONDS);
            clientBuilder.readTimeout(NetworkConstants.NET_READ_TIMEOUT, TimeUnit.SECONDS);
            retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkConstants.MAIN_SERVER_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(clientBuilder.build())
                    .build();
        }
        return retrofit;
    }

    public static <T> T create(Class<T> cls) {
        return getInstance().create(cls);
    }

}
