package com.liangzhicn.androidbasedemo.http.model;

import com.android.base.callback.RequestDataCallBack;
import com.android.base.common.logutils.LogUtils;
import com.android.base.common.rx.RxUtil;
import com.android.base.http.progress.domain.ProgressRequest;
import com.liangzhicn.androidbasedemo.http.contract.DownloadContract;
import com.liangzhicn.androidbasedemo.http.model.repositorys.http.GetAndPostClient;
import com.liangzhicn.androidbasedemo.http.model.repositorys.http.GetAndPostService;

import rx.Subscriber;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:51
 * 描述:
 */
public class DownloadModel extends DownloadContract.Model {

    @Override
    public void fileDownload(String path, final RequestDataCallBack<ProgressRequest> requestDataCallBack) {
        GetAndPostService mGetAndPostService = GetAndPostClient.getInstance("http://server.jeasonlzy.com/OkHttpUtils/").createService(GetAndPostService.class);
        mRxManager.add(RxUtil.getDownloadObservable(mGetAndPostService.downloadFile(), path).compose(RxUtil.<ProgressRequest>applySchedulersProgress()).subscribe(new Subscriber<ProgressRequest>() {
            @Override
            public void onStart() {
                requestDataCallBack.onStart();
            }

            @Override
            public void onCompleted() {
                LogUtils.d("下载完成 onCompleted");
                requestDataCallBack.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.d("下载失败 : " + e.getMessage());
                requestDataCallBack.onError(e);
            }

            @Override
            public void onNext(ProgressRequest file) {
                LogUtils.d("下载进度: " + file.getCurrentBytes() + " / " + file.getContentLength());
                requestDataCallBack.onNext(file);
            }
        }));
    }
}
