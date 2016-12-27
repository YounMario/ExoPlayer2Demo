package com.example.exoplayersample.okhttp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by 龙泉 on 2016/12/27.
 */

public class CacheHttpClient {

    public static OkHttpClient getInstance() {

        return new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return chain.proceed(chain.request());
            }
        }).build();
    }


}
