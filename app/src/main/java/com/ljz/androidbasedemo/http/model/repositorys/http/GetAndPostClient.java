package com.ljz.androidbasedemo.http.model.repositorys.http;

import android.text.TextUtils;

import com.ljz.base.frame.Base;
import com.ljz.base.http.impl.BaseClient;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by huangwm on 2016/4/30.
 */
public class GetAndPostClient extends BaseClient {

    private static GetAndPostClient mInstance;

    public static GetAndPostClient getInstance(String baseUrl) {
        if (mInstance == null) {
            synchronized (GetAndPostClient.class) {
                if (mInstance == null) {
                    mInstance = new GetAndPostClient(baseUrl);
                }
            }
        }
        return mInstance;
    }

    public GetAndPostClient(String baseUrl){
        attachBaseUrl(Base.getContext(), baseUrl);
        // 使用缓存
        enableCache(true,Base.getContext());

        //添加Cookie操作类,增加token字段
        addExtraInterceptor(new AddCookiesInterceptor());
    }

    /**
     * 操作Cookie或者头的示例Demo
     */
    public class AddCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request requestOrigin = chain.request();
            Headers headersOrigin = requestOrigin.headers();
            String cookie = headersOrigin.get("Cookie");
            if (TextUtils.isEmpty(cookie)) {
                cookie = "token=haha";
            } else {
                cookie = "token=haha;"+cookie;
            }
            Headers headers = headersOrigin.newBuilder().set("Cookie", cookie).build();
            Request request = requestOrigin.newBuilder().headers(headers).build();
            return chain.proceed(request);
        }
    }

}
