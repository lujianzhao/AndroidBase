package com.liangzhicn.androidbasedemo.http.contract;

import com.android.base.frame.model.impl.BaseModel;
import com.android.base.frame.presenter.impl.ActivityPresenter;
import com.android.base.frame.view.IBaseView;

/**
 * Created by Administrator on 2016/4/27.
 */
public interface UploadContract {

    abstract class Model extends BaseModel {

    }

    interface View extends IBaseView {
        /**
         * 选择图片后显示到界面上
         * @param imgs
         */
        void selectImageResult(String imgs);
    }

    abstract class Presenter<M extends Model> extends ActivityPresenter<M,View> {
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
