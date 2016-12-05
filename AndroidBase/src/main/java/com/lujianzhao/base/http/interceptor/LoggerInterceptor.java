package com.lujianzhao.base.http.interceptor;

import android.text.TextUtils;
import android.util.Log;

import com.lujianzhao.base.common.logutils.LogUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LoggerInterceptor implements Interceptor {
    public static final String TAG = "OkHttpUtils";
    private String tag = TAG;
    private boolean showLog = LogUtils.getLogConfig().isEnable();

    public LoggerInterceptor() {
    }

    public LoggerInterceptor(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            this.tag = tag;
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (showLog) {
            logForRequest(request);
        }
        Response response = chain.proceed(request);
        if (showLog) {
            response =logForResponse(response);
        }

        return response;
    }

    private void logForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();

            Log.e(tag, "---------------------request log onStart---------------------");
            Log.e(tag, "method : " + request.method());
            Log.e(tag, "url : " + url);
            if (headers != null && headers.size() > 0) {
                Log.e(tag, "headers : \n");
                Log.e(tag, headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    Log.e(tag, "contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                        Log.e(tag, "content : " + bodyToString(request));
                    } else {
                        Log.e(tag, "content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.e(tag, "---------------------request log end-----------------------");
        }
    }

    private Response logForResponse(Response response) {
        try {
            Log.e(tag, "---------------------response log onStart---------------------");
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            Log.e(tag, "url : " + clone.request().url());
            Log.e(tag, "code : " + clone.code());
            Log.e(tag, "protocol : " + clone.protocol());
            if (!TextUtils.isEmpty(clone.message()))
                Log.e(tag, "message : " + clone.message());
            ResponseBody body = clone.body();
            if (body != null) {
                MediaType mediaType = body.contentType();
                if (mediaType != null) {
                    Log.e(tag, "contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                        String resp = body.string();
                        Log.e(tag, "content : " + resp);
                        body = ResponseBody.create(mediaType, resp);
                        return response.newBuilder().body(body).build();
                    } else {
                        Log.e(tag, "content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.e(tag, "---------------------response log end-----------------------");
        }

        return response;
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")) //
                return true;
        }
        return false;
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}
