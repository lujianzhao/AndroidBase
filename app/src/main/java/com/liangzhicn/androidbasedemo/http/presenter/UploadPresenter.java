package com.liangzhicn.androidbasedemo.http.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.android.base.callback.ExecutorCallBack;
import com.android.base.common.assist.Toastor;
import com.android.base.common.logutils.LogUtils;
import com.android.base.http.progress.domain.ProgressRequest;
import com.liangzhicn.androidbasedemo.http.contract.UploadContract;
import com.liangzhicn.androidbasedemo.http.model.UploadModel;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.GlideImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.util.ArrayList;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:50
 * 描述:
 */
public class UploadPresenter extends UploadContract.Presenter<UploadContract.Model> {

    private ArrayList<ImageItem> imageItems;

    @NonNull
    @Override
    protected UploadContract.Model getMvpModel() {
        return new UploadModel();
    }

    @Override
    public void start() {

    }

    @Override
    public void selectImage() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setMultiMode(true);   //多选
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setSelectLimit(9);    //最多选择9张
        imagePicker.setCrop(false);       //不进行裁剪
        Intent intent = new Intent(mActivity, ImageGridActivity.class);
        mActivity.startActivityForResult(intent, 100);
    }


    @Override
    public void formUpload() {
        if (imageItems == null || imageItems.size() == 0) {
            Toastor.showToast(mActivity, "请选择需上传的图片");
            return;
        }
        mModel.formUpload(imageItems, new ExecutorCallBack<ProgressRequest>() {
            @Override
            public void onStart() {
                LogUtils.d("开始上传");
            }

            @Override
            public void onNext(ProgressRequest s) {
                mView.upProgress(s.getCurrentBytes(), s.getContentLength());
            }

            @Override
            public void onComplete() {
                LogUtils.d("所有文件上传完成");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.d("上传错误 : " + e.getMessage());
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            String imgs = "";
            if (data != null && requestCode == 100) {
                imageItems = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (imageItems != null && imageItems.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < imageItems.size(); i++) {
                        if (i == imageItems.size() - 1)
                            sb.append("图片").append(i + 1).append(" ： ").append(imageItems.get(i).path);
                        else
                            sb.append("图片").append(i + 1).append(" ： ").append(imageItems.get(i).path).append("\n");
                    }
                    imgs = sb.toString();
                } else {
                    imgs = "--";
                }
            } else {
                Toastor.showToast(mActivity, "没有选择图片");
                imgs = "--";
            }
            mView.selectImageResult(imgs);
        }
    }
}
