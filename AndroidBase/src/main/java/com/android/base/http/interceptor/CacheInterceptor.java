package com.android.base.http.interceptor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by RG on 2016/3/27.
 */
public class CacheInterceptor implements Interceptor {

    private Context mContext;
    private int mOnlineCacheTime; // 在线缓存时间,默认交由服务器端维护
    private int mOfflineCacheTime; // 离线缓存时间,默认1天

    public CacheInterceptor(Context context) {
        this(context, 0, 86400);
    }

    public CacheInterceptor(Context context, int onlineCacheTime, int offlineCacheTime) {
        this.mContext = context;
        this.mOnlineCacheTime = onlineCacheTime;
        this.mOfflineCacheTime = offlineCacheTime;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        //如果没有网络，则启用 FORCE_CACHE
        if (!isNetworkConnected(mContext)) {
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
        }

        Response originalResponse = chain.proceed(request);
        if (isNetworkConnected(mContext)) {
            //有网的时候读接口上的@Headers里的配置
            String cacheControl = request.cacheControl().toString();
            return originalResponse.newBuilder().header("Cache-Control", mOnlineCacheTime == 0 ? cacheControl : String.valueOf(mOnlineCacheTime)).removeHeader("Pragma").build();
        } else {
            return originalResponse.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + mOfflineCacheTime).removeHeader("Pragma").build();
        }
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
