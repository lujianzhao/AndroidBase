package com.lujianzhao.androidbasedemo.http.presenter;

import android.os.Environment;

import com.lujianzhao.base.callback.ExecutorCallBack;
import com.lujianzhao.base.common.logutils.LogUtils;
import com.lujianzhao.base.frame.model.factory.RequiresModel;
import com.lujianzhao.base.http.progress.domain.ProgressRequest;
import com.lujianzhao.androidbasedemo.http.contract.DownloadContract;
import com.lujianzhao.androidbasedemo.http.model.DownloadModel;

import java.io.File;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:50
 * 描述:
 */
@RequiresModel(DownloadModel.class)
public class DownloadPresenter extends DownloadContract.Presenter{

    @Override
    public void fileDownload() {
        getModel().fileDownload(Environment.getExternalStorageDirectory().getPath() + File.separator + "app_newkey_release_8_4.apk", new ExecutorCallBack<ProgressRequest>() {

            @Override
            public void onStart() {
                LogUtils.d("开始下载");
            }

            @Override
            public void onCompleted() {
                LogUtils.d("下载完成");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.d("下载错误: " + e.getMessage());
            }

            @Override
            public void onNext(ProgressRequest data) {
                getView().upProgress(data.getCurrentBytes(), data.getContentLength());
            }
        });


    }
}
