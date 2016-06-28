package com.liangzhicn.androidbasedemo.http.contract;

import com.android.base.callback.ExecutorCallBack;
import com.android.base.frame.model.impl.BaseModel;
import com.android.base.frame.presenter.impl.ActivityPresenter;
import com.android.base.frame.view.IBaseView;
import com.android.base.http.progress.domain.ProgressRequest;

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

    abstract class Presenter<M extends Model> extends ActivityPresenter<M,View> {

        /**
         * 开始下载文件
         */
        public abstract void fileDownload();
    }

}
