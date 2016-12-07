package com.ljz.androidbasedemo.http.presenter;

import android.os.Bundle;

import com.ljz.base.callback.ExecutorCallBack;
import com.ljz.base.common.logutils.LogUtils;
import com.ljz.base.frame.model.factory.RequiresModel;
import com.ljz.androidbasedemo.http.contract.GetAndPostContract;
import com.ljz.androidbasedemo.http.model.GetAndPostModel;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:50
 * 描述:
 */
@RequiresModel(GetAndPostModel.class)
public class GetAndPostPresenter extends GetAndPostContract.Presenter {

    private String mData1;
    private String mData2;
    private String mData3 ="";

    @Override
    public void onStart(Bundle bundle) {
        test1();
//        test2();
    }

    /**
     * 全部请求完毕,再一次性回调onComplete
     */
    private void test2() {

        getModel().getBlend(new ExecutorCallBack<Object>() {

            @Override
            public void onStart() {
                getView().showLoadingView();
            }

            @Override
            public void onNext(Object data) {
                //保存数据
                mData3 = mData3 + (String) data +"\r\n\r\n";
                getView().showGet(mData3);
            }

            @Override
            public void onCompleted() {
                //刷新界面
                getView().showContentView();
            }

            @Override
            public void onError(Throwable e) {
                //加载错误
                getView().showErrorView();
            }
        });

    }

    /**
     * 单独的请求.每次请求完毕都会回调
     */
    public void test1() {
        getModel().getRequest(new ExecutorCallBack<String>() {

            @Override
            public void onStart() {
                getView().showLoadingView();
            }

            @Override
            public void onNext(String data) {
                //保存数据,操作界面显示
                mData1 = data;
                getView().showGet("get请求结果 : \r\n" + mData1 + "\r\n");
            }

            @Override
            public void onCompleted() {
                // 停止加载
                getView().showContentView();
            }

            @Override
            public void onError(Throwable e) {
                //加载错误
                getView().showErrorView();
                LogUtils.d(e);
            }
        });

//        mModel.getPost(new RequestDataCallBack<String>() {
//
//            @Override
//            public void onStart() {
//                mView.showLoadingView();
//            }
//
//            @Override
//            public void onNext(String data) {
//                //保存数据,刷新界面
//                mData2 = data;
//                mView.showGet("Post请求结果 : \r\n" + mData2 + "\r\n");
//            }
//
//            @Override
//            public void onComplete() {
//                // 停止加载
//                mView.showContentView();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                //加载错误
//                mView.showErrorView();
//            }
//        });
    }

}
