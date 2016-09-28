package com.android.base.http.interceptor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;

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
    private String mUserAgent;

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
            request = request.newBuilder().header("User-Agent", getUserAgent()).cacheControl(CacheControl.FORCE_CACHE).build();
        } else {
            request = request.newBuilder().header("User-Agent", getUserAgent()).build();
        }

        Response originalResponse = chain.proceed(request);
        if (isNetworkConnected(mContext)) {
            //有网的时候读接口上的@Headers里的配置
            String cacheControl = request.cacheControl().toString();
            return originalResponse.newBuilder()
                    .header("Cache-Control", mOnlineCacheTime == 0 ? cacheControl : String.valueOf(mOnlineCacheTime))
                    .removeHeader("Pragma")
                    .build();
        } else {
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + mOfflineCacheTime)
                    .removeHeader("Pragma")
                    .build();
        }
    }


    private String getUserAgent() {
        if (TextUtils.isEmpty(mUserAgent)) {
            String webUserAgent = null;
            try {
                Class<?> sysResCls = Class.forName("com.android.internal.R$string");
                Field webUserAgentField = sysResCls.getDeclaredField("web_user_agent");
                Integer resId = (Integer) webUserAgentField.get(null);
                webUserAgent = mContext.getString(resId);
            } catch (Exception e) {
                // We have nothing to do
            }
            if (TextUtils.isEmpty(webUserAgent)) {
                webUserAgent = "Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/533.1 (KHTML, like Gecko) Version/5.0 %sSafari/533.1";
            }

            Locale locale = Locale.getDefault();
            StringBuffer buffer = new StringBuffer();
            // Add version
            final String version = Build.VERSION.RELEASE;
            if (version.length() > 0) {
                buffer.append(version);
            } else {
                // default to "1.0"
                buffer.append("1.0");
            }
            buffer.append("; ");
            final String language = locale.getLanguage();
            if (language != null) {
                buffer.append(language.toLowerCase(locale));
                final String country = locale.getCountry();
                if (!TextUtils.isEmpty(country)) {
                    buffer.append("-");
                    buffer.append(country.toLowerCase(locale));
                }
            } else {
                // default to "en"
                buffer.append("en");
            }
            // add the model for the release build
            if ("REL".equals(Build.VERSION.CODENAME)) {
                final String model = Build.MODEL;
                if (model.length() > 0) {
                    buffer.append("; ");
                    buffer.append(model);
                }
            }
            final String id = Build.ID;
            if (id.length() > 0) {
                buffer.append(" Build/");
                buffer.append(id);
            }
            mUserAgent = String.format(webUserAgent, buffer, "Mobile ");
        }
        return mUserAgent;
    }

    private  boolean isNetworkConnected(Context context) {
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
