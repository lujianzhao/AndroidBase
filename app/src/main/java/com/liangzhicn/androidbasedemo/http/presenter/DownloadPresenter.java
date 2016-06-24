package com.liangzhicn.androidbasedemo.http.presenter;

import android.os.Environment;
import android.support.annotation.NonNull;

import com.android.base.callback.RequestDataCallBack;
import com.android.base.common.logutils.LogUtils;
import com.android.base.http.progress.domain.ProgressRequest;
import com.liangzhicn.androidbasedemo.http.contract.DownloadContract;
import com.liangzhicn.androidbasedemo.http.model.DownloadModel;

import java.io.File;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:50
 * 描述:
 */
public class DownloadPresenter extends DownloadContract.Presenter<DownloadContract.Model> {

    @NonNull
    @Override
    protected DownloadContract.Model getMvpModel() {
        return new DownloadModel();
    }

    @Override
    public void start() {

    }

    @Override
    public void fileDownload() {
        mModel.fileDownload(Environment.getExternalStorageDirectory().getPath() + File.separator + "app_newkey_release_8_4.apk", new RequestDataCallBack<ProgressRequest>() {

            @Override
            public void onStart() {
                LogUtils.d("开始下载");
            }

            @Override
            public void onComplete() {
                LogUtils.d("下载完成");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.d("下载错误: " + e.getMessage());
            }

            @Override
            public void onNext(ProgressRequest data) {
                mView.upProgress(data.getCurrentBytes(), data.getContentLength());
            }
        });


    }
}
