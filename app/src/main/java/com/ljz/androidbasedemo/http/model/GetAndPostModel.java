package com.ljz.androidbasedemo.http.model;

import com.ljz.androidbasedemo.constant.ApiConfig;
import com.ljz.androidbasedemo.http.contract.GetAndPostContract;
import com.ljz.androidbasedemo.http.model.repositorys.http.GetAndPostClient;
import com.ljz.androidbasedemo.http.model.repositorys.http.GetAndPostService;
import com.ljz.base.common.rx.RxUtil;

import io.reactivex.Observable;


/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:51
 * 描述:
 */
public class GetAndPostModel extends GetAndPostContract.Model {

    private GetAndPostService mGetAndPostService;

    @Override
    public Observable<String> getRequest() {
        if (mGetAndPostService == null) {
            mGetAndPostService = GetAndPostClient.getInstance(ApiConfig.URL_BASE).createService(GetAndPostService.class);
        }
       return mGetAndPostService.getGet().compose(RxUtil.<String>applySchedulersForRetrofit());
    }

    @Override
    public Observable<String> getPost() {
        if (mGetAndPostService == null) {
            mGetAndPostService = GetAndPostClient.getInstance(ApiConfig.URL_BASE).createService(GetAndPostService.class);
        }

       return mGetAndPostService.getPost().compose(RxUtil.<String>applySchedulersForRetrofit());
    }

    @Override
    public Observable<String> getBlend() {

        if (mGetAndPostService == null) {
            mGetAndPostService = GetAndPostClient.getInstance(ApiConfig.URL_BASE).createService(GetAndPostService.class);
        }
        Observable<String> compose1 = mGetAndPostService.getGet().compose(RxUtil.<String>applySchedulersForRetrofit());

        if (mGetAndPostService == null) {
            mGetAndPostService = GetAndPostClient.getInstance(ApiConfig.URL_BASE).createService(GetAndPostService.class);
        }
        Observable<String> compose2 = mGetAndPostService.getPost().compose(RxUtil.<String>applySchedulersForRetrofit());
        return  Observable.mergeDelayError(compose1, compose2);

    }

}
