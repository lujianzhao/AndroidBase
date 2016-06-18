package com.android.base.http;

import android.content.Context;

import com.android.base.common.cookiejar.ClearableCookieJar;
import com.android.base.common.cookiejar.PersistentCookieJar;
import com.android.base.common.cookiejar.cache.SetCookieCache;
import com.android.base.common.cookiejar.persistence.SharedPrefsCookiePersistor;
import com.android.base.http.impl.IRetrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * Created by Administrator on 2016/4/29.
 */
public class BaseRestClient implements IRetrofit {

    private Retrofit mRetrofit;

    @Override
    public void attachBaseUrl(Context context, String baseUrl) {

        //okhttp3 cookie 持久化
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));

        //okhttp3 提供的日志系统
        LoggerInterceptor logging = new LoggerInterceptor(null);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(7676, TimeUnit.MILLISECONDS)
                .connectTimeout(7676, TimeUnit.MILLISECONDS)
                .addInterceptor(logging)
                .cookieJar(cookieJar);

        Interceptor interceptor = getNetworkInterceptor();
        if (interceptor != null) {
            builder.addNetworkInterceptor(interceptor);
        }
        OkHttpClient okHttpClient = builder.build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    protected Interceptor getNetworkInterceptor() {
        return null;
    }

    @Override
    public <T> T createService(Class<T> clz) {
        return mRetrofit.create(clz);
    }

    @Override
    public void destory() {
        mRetrofit = null;
    }


}
