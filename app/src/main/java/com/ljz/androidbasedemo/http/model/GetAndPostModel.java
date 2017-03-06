package com.ljz.androidbasedemo.http.model;

import com.ljz.androidbasedemo.constant.ApiConfig;
import com.ljz.androidbasedemo.http.contract.GetAndPostContract;
import com.ljz.androidbasedemo.http.model.repositorys.http.GetAndPostClient;
import com.ljz.androidbasedemo.http.model.repositorys.http.GetAndPostService;
import com.ljz.base.callback.ExecutorCallBack;
import com.ljz.base.common.logutils.LogUtils;
import com.ljz.base.common.rx.RxUtil;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:51
 * 描述:
 */
public class GetAndPostModel extends GetAndPostContract.Model {

    private GetAndPostService mGetAndPostService;

    @Override
    public void getRequest(final ExecutorCallBack<String> requestCallBack) {
        if (mGetAndPostService == null) {
            mGetAndPostService = GetAndPostClient.getInstance(ApiConfig.URL_BASE).createService(GetAndPostService.class);
        }
        mGetAndPostService.getGet().compose(RxUtil.<String>applySchedulersForRetrofit())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        LogUtils.d(Thread.currentThread().getName());
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtils.d("解除绑定"+Thread.currentThread().getName());
                    }
                })
                .subscribe(requestCallBack);
    }

    @Override
    public void getPost(final ExecutorCallBack<String> requestCallBack) {
        if (mGetAndPostService == null) {
            mGetAndPostService = GetAndPostClient.getInstance(ApiConfig.URL_BASE).createService(GetAndPostService.class);
        }

        mGetAndPostService.getPost().compose(RxUtil.<String>applySchedulersForRetrofit())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        LogUtils.d(Thread.currentThread().getName());
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtils.d("解除绑定"+Thread.currentThread().getName());
                    }
                })
                .subscribe(requestCallBack);

    }

    @Override
    public void getBlend(final ExecutorCallBack<Object> requestCallBack) {

        if (mGetAndPostService == null) {
            mGetAndPostService = GetAndPostClient.getInstance(ApiConfig.URL_BASE).createService(GetAndPostService.class);
        }
        Observable<String> compose1 = mGetAndPostService.getGet().compose(RxUtil.<String>applySchedulersForRetrofit());

        if (mGetAndPostService == null) {
            mGetAndPostService = GetAndPostClient.getInstance(ApiConfig.URL_BASE).createService(GetAndPostService.class);
        }
        Observable<String> compose2 = mGetAndPostService.getPost().compose(RxUtil.<String>applySchedulersForRetrofit());


        Observable.mergeDelayError(compose1, compose2).subscribe(requestCallBack);

    }

}
