package com.lujianzhao.androidbasedemo.http.contract;

import com.lujianzhao.base.callback.ExecutorUploadCallBack;
import com.lujianzhao.base.frame.model.BaseModel;
import com.lujianzhao.base.frame.presenter.BasePresenter;
import com.lujianzhao.base.frame.view.IBaseView;
import com.lujianzhao.base.http.progress.domain.ProgressRequest;
import com.lzy.imagepicker.bean.ImageItem;

import java.util.ArrayList;

import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2016/4/27.
 */
public interface UploadContract {

    abstract class Model extends BaseModel {

        /**
         * 上传图片
         * @param imageItems
         * @param requestDataCallBack
         */
        public abstract void formUpload(ArrayList<ImageItem> imageItems, ExecutorUploadCallBack<ProgressRequest, ResponseBody> requestDataCallBack);

    }

    interface View extends IBaseView {
        /**
         * 选择图片后显示到界面上
         * @param imgs
         */
        void selectImageResult(String imgs);

        void upProgress(long currentSize, long totalSize);

        void ro();
    }

    abstract class Presenter extends BasePresenter<Model,View> {
        /**
         * 选择图片
         */
        public abstract void selectImage();

        /**
         * 上传选中的图片
         */
        public abstract void formUpload();

    }

}
