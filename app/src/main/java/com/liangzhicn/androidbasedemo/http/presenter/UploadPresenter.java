package com.liangzhicn.androidbasedemo.http.presenter;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.android.base.common.assist.Toastor;
import com.android.base.http.listener.ProgressListener;
import com.android.base.http.progress.ProgressRequestBody;
import com.liangzhicn.androidbasedemo.http.contract.UploadContract;
import com.liangzhicn.androidbasedemo.http.model.UploadModel;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.GlideImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:50
 * 描述:
 */
public class UploadPresenter extends UploadContract.Presenter<UploadModel> {

    private ArrayList<ImageItem> imageItems;

    @NonNull
    @Override
    protected Class<UploadModel> getModelClass() {
        return UploadModel.class;
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
            Toastor.showToast(mActivity,"请选择需上传的图片");
            return;
        }

        String token = "09oMsbnJYREss4vRGdGeurO-C7WhTPHnlLyy-6e5:J8V1qHXvDR51PDN5YaAqHIh9eHc=:eyJzY29wZSI6ImFwcGltYWdlIiwiY2FsbGJhY2tVcmwiOiJodHRwOi8vYXBwYXBpLmlpdGUuY2M6ODA4MC9pdGVldGgvY29tbW9uL3Fpbml1Y2FsbGJhY2siLCJkZWFkbGluZSI6MTc3NDA5OTQ2NCwiY2FsbGJhY2tCb2R5Ijoia2V5XHUwMDNkJChrZXkpXHUwMDI2aGFzaFx1MDAzZCQoZXRhZylcdTAwMjZqc29uQm9keVx1MDAzZCQoeDpqc29uQm9keSkifQ==";
        Map<String, RequestBody> params = new HashMap<>();
        RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "app_newkey_release_8_4.apk"));
        params.put("file", new ProgressRequestBody(body, new ProgressListener() {
            @Override
            public void onProgress(long currentBytes, long contentLength, boolean done) {

            }
        }));

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
                        else sb.append("图片").append(i + 1).append(" ： ").append(imageItems.get(i).path).append("\n");
                    }
                    imgs = sb.toString();
                } else {
                    imgs = "--";
                }
            } else {
                Toastor.showToast(mActivity,"没有选择图片");
                imgs = "--";
            }
            mView.selectImageResult(imgs);
        }
    }
}
