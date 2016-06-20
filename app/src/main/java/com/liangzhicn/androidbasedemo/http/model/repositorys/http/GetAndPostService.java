package com.liangzhicn.androidbasedemo.http.model.repositorys.http;

import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by huangwm on 2016/4/30.
 */
public interface GetAndPostService {

    @GET("method")
    Observable<String> getGet();

    @POST("method")
    Observable<String> getPost();
}
