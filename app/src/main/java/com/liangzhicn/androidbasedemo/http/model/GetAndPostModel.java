package com.liangzhicn.androidbasedemo.http.model;

import com.android.base.callback.RequestDataCallBack;
import com.android.base.common.rx.RxUtil;
import com.liangzhicn.androidbasedemo.constant.ApiConfig;
import com.liangzhicn.androidbasedemo.http.contract.GetAndPostContract;
import com.liangzhicn.androidbasedemo.http.model.repositorys.http.GetAndPostClient;
import com.liangzhicn.androidbasedemo.http.model.repositorys.http.GetAndPostService;

import rx.Observable;
import rx.Subscriber;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:51
 * 描述:
 */
public class GetAndPostModel extends GetAndPostContract.Model {

    private GetAndPostService mGetAndPostService;

    @Override
    public void getRequest(final RequestDataCallBack<String> requestCallBack) {
        if (mGetAndPostService == null) {
            mGetAndPostService = GetAndPostClient.getInstance(ApiConfig.URL_BASE).createService(GetAndPostService.class);
        }

        mRxManager.add(mGetAndPostService.getGet().compose(RxUtil.<String>applySchedulersForRetrofit()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                requestCallBack.onComplete();
            }

            @Override
            public void onStart() {
                requestCallBack.onStart();
            }

            @Override
            public void onError(Throwable e) {
                requestCallBack.onError(e);
            }

            @Override
            public void onNext(String s) {
                requestCallBack.onNext(s);
            }
        }));
    }

    @Override
    public void getPost(final RequestDataCallBack<String> requestCallBack) {
        if (mGetAndPostService == null) {
            mGetAndPostService = GetAndPostClient.getInstance(ApiConfig.URL_BASE).createService(GetAndPostService.class);
        }

        mRxManager.add(mGetAndPostService.getPost().compose(RxUtil.<String>applySchedulersForRetrofit()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                requestCallBack.onComplete();
            }

            @Override
            public void onStart() {
                requestCallBack.onStart();
            }


            @Override
            public void onError(Throwable e) {
                requestCallBack.onError(e);
            }

            @Override
            public void onNext(String s) {
                requestCallBack.onNext(s);
            }
        }));

    }

    @Override
    public void getBlend(final RequestDataCallBack<Object> requestCallBack) {

        if (mGetAndPostService == null) {
            mGetAndPostService = GetAndPostClient.getInstance(ApiConfig.URL_BASE).createService(GetAndPostService.class);
        }
        Observable<String> compose1 = mGetAndPostService.getGet().compose(RxUtil.<String>applySchedulersForRetrofit());

        if (mGetAndPostService == null) {
            mGetAndPostService = GetAndPostClient.getInstance(ApiConfig.URL_BASE).createService(GetAndPostService.class);
        }
        Observable<String> compose2 = mGetAndPostService.getPost().compose(RxUtil.<String>applySchedulersForRetrofit());


        mRxManager.add(Observable.mergeDelayError(compose1, compose2)
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        requestCallBack.onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestCallBack.onError(e);
                    }

                    @Override
                    public void onStart() {
                        requestCallBack.onStart();
                    }


                    @Override
                    public void onNext(Object o) {
                        requestCallBack.onNext(o);
                    }
                }));
    }

}
