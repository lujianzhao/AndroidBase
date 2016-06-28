package com.liangzhicn.androidbasedemo.http.model;

import com.android.base.callback.ExecutorCallBack;
import com.android.base.common.rx.RxUtil;
import com.android.base.http.progress.ProgressRequestBody;
import com.android.base.http.progress.domain.ProgressRequest;
import com.liangzhicn.androidbasedemo.http.contract.UploadContract;
import com.liangzhicn.androidbasedemo.http.model.repositorys.http.GetAndPostClient;
import com.liangzhicn.androidbasedemo.http.model.repositorys.http.GetAndPostService;
import com.lzy.imagepicker.bean.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:51
 * 描述:
 */
public class UploadModel extends UploadContract.Model {


    @Override
    public void formUpload(ArrayList<ImageItem> imageItems, final ExecutorCallBack<ProgressRequest> requestDataCallBack) {
        // 添加上传进度监听
        final PublishSubject<ProgressRequest> objectPublishSubject = PublishSubject.create();
        // 此处是为了可以自由的选择监听单独文件的进度还是监听总进度.总进度可以使用.map来转变
        mRxManager.add(objectPublishSubject.compose(RxUtil.<ProgressRequest>applySchedulersProgress()).subscribe(new Action1<ProgressRequest>() {
            @Override
            public void call(ProgressRequest progressRequest) {
                requestDataCallBack.onNext(progressRequest);
            }
        }));

        // 添加需要上传的文件
        Map<String, RequestBody> params = new HashMap<>();
        for (int i = 0; i < imageItems.size(); i++) {
            ImageItem imageItem = imageItems.get(i);
            RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), new File(imageItem.path));
            params.put("file[" + i + "]\";filename=\"" + imageItem.name, new ProgressRequestBody(imageItem.name, imageItem.path, body, objectPublishSubject));
        }
        GetAndPostService mGetAndPostService = GetAndPostClient.getInstance("http://server.jeasonlzy.com/OkHttpUtils/").createService(GetAndPostService.class);
        //开始上传,rxJava的生命周期交由mRxManager来管理.
        mRxManager.add(mGetAndPostService.uploadImage(params).compose(RxUtil.<ResponseBody>applySchedulersForRetrofit()).subscribe(new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                requestDataCallBack.onComplete();
            }

            @Override
            public void onStart() {
                requestDataCallBack.onStart();
            }


            @Override
            public void onError(Throwable e) {
                requestDataCallBack.onError(e);
            }

            @Override
            public void onNext(ResponseBody s) {
            }
        }));
    }
}
