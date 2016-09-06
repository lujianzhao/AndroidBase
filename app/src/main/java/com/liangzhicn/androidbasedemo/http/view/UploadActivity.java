package com.liangzhicn.androidbasedemo.http.view;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.base.common.logutils.LogUtils;
import com.android.base.frame.activity.impl.BaseMvpActivity;
import com.android.base.frame.view.IBaseView;
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
public class UploadActivity extends BaseMvpActivity<UploadContract.Presenter>implements UploadContract.View{

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
    protected UploadContract.Presenter getMvpPresenter() {
        return new UploadPresenter();
    }

    @NonNull
    @Override
    protected IBaseView getMvpView() {
        return this;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_upload;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

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

    @Override
    public void upProgress(long currentSize, long totalSize) {
        String downloadLength = Formatter.formatFileSize(getApplicationContext(), currentSize);
        String totalLength = Formatter.formatFileSize(getApplicationContext(), totalSize);
        tvDownloadSize.setText(downloadLength + "/" + totalLength);
        tvProgress.setText(( currentSize *1.0F  / totalSize )*100.0F + "%");
        LogUtils.d(( currentSize  / totalSize )*100.0F+"");
        pbProgress.setMax(100);
        pbProgress.setProgress((int) (( currentSize  / totalSize )*100.0F));
    }
}
