package com.ljz.androidbasedemo.http.contract;

import com.ljz.base.callback.ExecutorCallBack;
import com.ljz.base.frame.model.BaseModel;
import com.ljz.base.frame.presenter.BasePresenter;
import com.ljz.base.frame.view.IBaseView;
import com.ljz.base.http.progress.domain.ProgressRequest;

/**
 * Created by Administrator on 2016/4/27.
 */
public interface DownloadContract {

    abstract class Model extends BaseModel {

        /**
         * 下载
         * @param path 保存路径
         * @param requestDataCallBack
         */
        public abstract void fileDownload(String path, ExecutorCallBack<ProgressRequest> requestDataCallBack);

    }

    interface View extends IBaseView {

        void upProgress(long currentSize, long totalSize);
    }

    abstract class Presenter extends BasePresenter<Model,View> {

        /**
         * 开始下载文件
         */
        public abstract void fileDownload();
    }

}
