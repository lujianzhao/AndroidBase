package com.ljz.androidbasedemo.http.model;

import com.ljz.androidbasedemo.http.contract.DownloadContract;
import com.ljz.androidbasedemo.http.model.repositorys.http.GetAndPostClient;
import com.ljz.androidbasedemo.http.model.repositorys.http.GetAndPostService;
import com.ljz.base.callback.ExecutorCallBack;
import com.ljz.base.common.rx.RxUtil;
import com.ljz.base.http.progress.domain.ProgressRequest;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:51
 * 描述:
 */
public class DownloadModel extends DownloadContract.Model {

    @Override
    public void fileDownload(String path, final ExecutorCallBack<ProgressRequest> requestDataCallBack) {
        GetAndPostService mGetAndPostService = GetAndPostClient.getInstance("http://server.jeasonlzy.com/OkHttpUtils/").createService(GetAndPostService.class);
        getRxManager().add(RxUtil.getDownloadObservable(mGetAndPostService.downloadFile(), path).compose(RxUtil.<ProgressRequest>applySchedulersProgress()).subscribe(requestDataCallBack));
    }
}
