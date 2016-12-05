package com.lujianzhao.androidbasedemo.http.contract;

import com.lujianzhao.base.callback.ExecutorCallBack;
import com.lujianzhao.base.frame.model.BaseModel;
import com.lujianzhao.base.frame.presenter.BasePresenter;
import com.lujianzhao.base.frame.view.IBaseView;
import com.lujianzhao.base.http.progress.domain.ProgressRequest;

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
