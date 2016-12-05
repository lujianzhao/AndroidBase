package com.lujianzhao.base.http.impl;

import android.content.Context;

import com.lujianzhao.base.common.cookiejar.ClearableCookieJar;
import com.lujianzhao.base.common.cookiejar.PersistentCookieJar;
import com.lujianzhao.base.common.cookiejar.cache.SetCookieCache;
import com.lujianzhao.base.common.cookiejar.persistence.SharedPrefsCookiePersistor;
import com.lujianzhao.base.common.utils.FileUtil;
import com.lujianzhao.base.http.IRetrofit;
import com.lujianzhao.base.http.interceptor.CacheInterceptor;
import com.lujianzhao.base.http.interceptor.LoggerInterceptor;

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
public class BaseClient implements IRetrofit {

    private static final String DEFAULT_DISK_CACHE_DIR = "http_disk_cache";

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
                .readTimeout(7676, TimeUnit.SECONDS)
                .writeTimeout(7676, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
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
    protected void enableCache(boolean flag, Context context) {
        if (flag) {
            File file = new File(FileUtil.getCacheDir(), DEFAULT_DISK_CACHE_DIR);
            FileUtil.createDirs(file);

            //缓存拦截器
            mOkhttpBuilder
                    .addInterceptor(new CacheInterceptor(context))
                    .addNetworkInterceptor(new CacheInterceptor(context))
                    //设置缓存路径以及大小
                    .cache(new Cache(file, 1024 * 1024 * 100));
        }
    }

    /**
     * 添加额外的拦截器
     * @param interceptor
     * @return
     */
    protected void addExtraInterceptor(Interceptor interceptor) {
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
