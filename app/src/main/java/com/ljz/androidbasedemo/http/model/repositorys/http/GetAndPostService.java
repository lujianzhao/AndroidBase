package com.ljz.androidbasedemo.http.model.repositorys.http;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Streaming;
import rx.Observable;

/**
 * Created by huangwm on 2016/4/30.
 */
public interface GetAndPostService {

    @GET("method")
    Observable<String> getGet();

    @POST("method")
    Observable<String> getPost();

    @Multipart
    @POST("upload")
    Observable<ResponseBody> uploadImage(@PartMap Map<String, RequestBody> params);

    @Streaming
    @GET("download")
    Call<ResponseBody> downloadFile();

}
