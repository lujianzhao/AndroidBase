package com.android.base.http.impl;

import android.content.Context;
import android.os.Environment;

import com.android.base.common.cookiejar.ClearableCookieJar;
import com.android.base.common.cookiejar.PersistentCookieJar;
import com.android.base.common.cookiejar.cache.SetCookieCache;
import com.android.base.common.cookiejar.persistence.SharedPrefsCookiePersistor;
import com.android.base.http.IRetrofit;
import com.android.base.http.interceptor.CacheInterceptor;
import com.android.base.http.interceptor.LoggerInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * Created by Administrator on 2016/4/29.
 */
public class BaseRestClient implements IRetrofit {
    //okhttp build对象
    private OkHttpClient.Builder mOkhttpBuilder;

    //Retrofit build对象
    private Retrofit.Builder mRetrofitBuilder;

    @Override
    public void attachBaseUrl(Context context, String baseUrl) {

        //okhttp3 cookie 持久化
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));

        //okhttp3 提供的日志系统
        LoggerInterceptor logging = new LoggerInterceptor();
        mOkhttpBuilder = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .cookieJar(cookieJar);

        mRetrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create());
    }

    /**
     * 是否添加缓存
     * @param flag
     * @param context
     */
    public void enableCache(boolean flag, Context context) {
        if (flag) {
            //缓存拦截器
            mOkhttpBuilder
                    .addInterceptor(new CacheInterceptor(context))
                    .addNetworkInterceptor(new CacheInterceptor(context))
                    //设置缓存路径以及大小
                    .cache(new Cache(new File(Environment.getExternalStorageDirectory().getPath() + "/retrofit2demo"), 1024 * 1024 * 100));
            mOkhttpBuilder.interceptors().add(new CacheInterceptor(context));
        }
    }

    /**
     * 添加额外的拦截器
     * @param interceptor
     * @return
     */
    public void addExtraInterceptor(Interceptor interceptor) {
        mOkhttpBuilder.interceptors().add(interceptor);
    }


    @Override
    public <T> T createService(Class<T> clz) {
        return mRetrofitBuilder.client(mOkhttpBuilder.build()).build().create(clz);
    }

    @Override
    public void destory() {
        mRetrofitBuilder = null;
        mOkhttpBuilder = null;
    }


}
