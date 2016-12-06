package com.ljz.androidbasedemo.http.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ljz.base.callback.ExecutorUploadCallBack;
import com.ljz.base.common.assist.Toastor;
import com.ljz.base.common.logutils.LogUtils;
import com.ljz.base.frame.model.factory.RequiresModel;
import com.ljz.base.http.progress.domain.ProgressRequest;
import com.ljz.androidbasedemo.http.contract.UploadContract;
import com.ljz.androidbasedemo.http.model.UploadModel;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.GlideImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:50
 * 描述:
 */
@RequiresModel(UploadModel.class)
public class UploadPresenter extends UploadContract.Presenter {

    private ArrayList<ImageItem> imageItems;


    @Override
    protected void onTakeView(UploadContract.View view) {
        super.onTakeView(view);
        LogUtils.d("jaja ");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        if (savedState != null) {
            String a = savedState.getString("A");
            LogUtils.d(""+a);
        }
    }

    @Override
    protected void onSave(Bundle state) {
        state.putString("A","aaaa");
    }


    @Override
    public void selectImage() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setMultiMode(true);   //多选
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setSelectLimit(9);    //最多选择9张
        imagePicker.setCrop(false);       //不进行裁剪

        getView().gotoActivityForResult(ImageGridActivity.class,100);

    }


    @Override
    public void formUpload() {
        if (imageItems == null || imageItems.size() == 0) {
            Toastor.showToast(getContext(), "请选择需上传的图片");
            return;
        }
        getModel().formUpload(imageItems, new ExecutorUploadCallBack<ProgressRequest, ResponseBody>() {
            @Override
            public void onStart() {
                LogUtils.d("开始上传");
            }

            @Override
            public void onNext(ProgressRequest s) {
                getView().upProgress(s.getCurrentBytes(), s.getContentLength());
            }

            @Override
            public void onCompleted(ResponseBody responseData) {
                String string = "";
                try {
                     string = responseData.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LogUtils.d("所有文件上传完成,服务器返回值:"+string);
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
                Toastor.showToast(getContext(), "没有选择图片");
                imgs = "--";
            }
            getView().selectImageResult(imgs);
//            getView().ro();

        }
    }
}
