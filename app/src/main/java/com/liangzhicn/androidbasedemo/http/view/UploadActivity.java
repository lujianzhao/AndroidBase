package com.liangzhicn.androidbasedemo.http.view;


import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.base.frame.activity.impl.BaseMvpActivity;
import com.liangzhicn.androidbasedemo.R;
import com.liangzhicn.androidbasedemo.http.contract.UploadContract;
import com.liangzhicn.androidbasedemo.http.presenter.UploadPresenter;
import com.liangzhicn.androidbasedemo.view.NumberProgressBar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/20 16:22
 * 描述:
 */
public class UploadActivity extends BaseMvpActivity<UploadPresenter,UploadContract.View>implements UploadContract.View{

    @Bind(R.id.formUpload)
    Button btnFormUpload;

    @Bind(R.id.downloadSize)
    TextView tvDownloadSize;

    @Bind(R.id.tvProgress)
    TextView tvProgress;

    @Bind(R.id.netSpeed)
    TextView tvNetSpeed;

    @Bind(R.id.pbProgress)
    NumberProgressBar pbProgress;

    @Bind(R.id.images)
    TextView tvImages;


    @NonNull
    @Override
    protected Class<UploadPresenter> getPresenterClass() {
        return UploadPresenter.class;
    }

    @NonNull
    @Override
    protected UploadContract.View getMvpView() {
        return this;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_upload;
    }

    @OnClick(R.id.selectImage)
    public void selectImage(View view) {
        mPresenter.selectImage();

    }

    @OnClick(R.id.formUpload)
    public void formUpload(View view) {
        mPresenter.formUpload();
    }

    @Override
    protected void initView() {

    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showErrorView() {

    }

    @Override
    public void showContentView() {

    }

    @Override
    public void selectImageResult(String imgs) {
        tvImages.setText(imgs);
    }
}
