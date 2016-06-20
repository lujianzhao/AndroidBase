package com.liangzhicn.androidbasedemo.http.presenter;

import android.support.annotation.NonNull;

import com.android.base.callback.RequestDataCallBack;
import com.android.base.common.logutils.LogUtils;
import com.liangzhicn.androidbasedemo.http.contract.GetAndPostContract;
import com.liangzhicn.androidbasedemo.http.model.GetAndPostModel;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:50
 * 描述:
 */
public class GetAndPostPresenter extends GetAndPostContract.Presenter<GetAndPostModel> {

    private String mData1;
    private String mData2;
    private String mData3 ="";

    @NonNull
    @Override
    protected Class<GetAndPostModel> getModelClass() {
        return GetAndPostModel.class;
    }

    @Override
    public void start() {
//        test1();
        test2();
    }

    /**
     * 全部请求完毕,再一次性回调onComplete
     */
    private void test2() {
        mView.showLoadingView();
        mModel.getBlend(new RequestDataCallBack<Object>() {

            @Override
            public void onNext(Object data) {
                //保存数据
                mData3 = mData3 + (String) data +"\r\n\r\n";
                LogUtils.d("回调一次:" + data);
            }

            @Override
            public void onComplete() {
                //刷新界面
                mView.showContentView();
                mView.showGet(mData3);
            }

            @Override
            public void onError(Throwable e) {
                //加载错误
                mView.showErrorView();
            }
        });

    }

    /**
     * 单独的请求.每次请求完毕都会回调
     */
    public void test1() {
        mView.showLoadingView();
        mModel.getRequest(new RequestDataCallBack<String>() {

            @Override
            public void onNext(String data) {
                //保存数据
                mData1 = data;
            }

            @Override
            public void onComplete() {
                //刷新界面
                mView.showContentView();
                mView.showGet("get请求结果 : \r\n" + mData1 + "\r\n");
            }

            @Override
            public void onError(Throwable e) {
                //加载错误
                mView.showErrorView();
            }
        });

        mModel.getPost(new RequestDataCallBack<String>() {

            @Override
            public void onNext(String data) {
                //保存数据
                mData2 = data;
            }

            @Override
            public void onComplete() {
                //刷新界面
                mView.showContentView();
                mView.showGet("Post请求结果 : \r\n" + mData2 + "\r\n");
            }

            @Override
            public void onError(Throwable e) {
                //加载错误
                mView.showErrorView();
            }
        });
    }

}
