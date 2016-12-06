package com.ljz.androidbasedemo.http.model;

import com.ljz.base.callback.ExecutorUploadCallBack;
import com.ljz.base.common.logutils.LogUtils;
import com.ljz.base.common.rx.RxUtil;
import com.ljz.base.http.progress.ProgressRequestBody;
import com.ljz.base.http.progress.domain.ProgressRequest;
import com.ljz.androidbasedemo.http.contract.UploadContract;
import com.ljz.androidbasedemo.http.model.repositorys.http.GetAndPostClient;
import com.ljz.androidbasedemo.http.model.repositorys.http.GetAndPostService;
import com.lzy.imagepicker.bean.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:51
 * 描述:
 */
public class UploadModel extends UploadContract.Model {


    @Override
    public void formUpload(ArrayList<ImageItem> imageItems, final ExecutorUploadCallBack<ProgressRequest, ResponseBody> requestDataCallBack) {
        Subscriber<ProgressRequest> subscriber = new Subscriber<ProgressRequest>() {
            @Override
            public void onCompleted() {
                this.unsubscribe();
            }

            @Override
            public void onError(Throwable throwable) {
                requestDataCallBack.onError(throwable);
            }

            @Override
            public void onNext(ProgressRequest progressRequest) {
                LogUtils.d("上传了:"+progressRequest.getCurrentBytes()+"/"+progressRequest.getContentLength());
                requestDataCallBack.onNext(progressRequest);
            }
        };
        getRxManager().add(subscriber);

        // 添加需要上传的文件
        Map<String, RequestBody> params = new HashMap<>();
        for (int i = 0; i < imageItems.size(); i++) {
            ImageItem imageItem = imageItems.get(i);
            RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), new File(imageItem.path));
            params.put("file[" + i + "]\";filename=\"" + imageItem.name, new ProgressRequestBody(imageItem.name, imageItem.path, body, subscriber));
        }
        GetAndPostService mGetAndPostService = GetAndPostClient.getInstance("http://server.jeasonlzy.com/OkHttpUtils/").createService(GetAndPostService.class);
        //开始上传,rxJava的生命周期交由mRxManager来管理.
        getRxManager().add(mGetAndPostService.uploadImage(params).compose(RxUtil.<ResponseBody>applySchedulersForRetrofit()).subscribe(new Subscriber<ResponseBody>() {

            @Override
            public void onStart() {
                requestDataCallBack.onStart();
            }


            @Override
            public void onCompleted() {
                requestDataCallBack.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                requestDataCallBack.onError(e);
            }

            @Override
            public void onNext(ResponseBody s) {
                requestDataCallBack.onCompleted(s);
            }
        }));
    }
}
