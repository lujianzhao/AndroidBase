package com.liangzhicn.androidbasedemo.http.view;

import android.support.annotation.NonNull;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.base.common.logutils.LogUtils;
import com.android.base.frame.activity.impl.BaseMvpActivity;
import com.liangzhicn.androidbasedemo.R;
import com.liangzhicn.androidbasedemo.http.contract.DownloadContract;
import com.liangzhicn.androidbasedemo.http.presenter.DownloadPresenter;
import com.liangzhicn.androidbasedemo.view.NumberProgressBar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/23 13:31
 * 描述:
 */
public class DownloadActivity extends BaseMvpActivity<DownloadContract.Presenter,DownloadContract.View> implements DownloadContract.View {

    @Bind(R.id.fileDownload)
    Button btnFileDownload;

    @Bind(R.id.downloadSize)
    TextView tvDownloadSize;

    @Bind(R.id.tvProgress)
    TextView tvProgress;

    @Bind(R.id.netSpeed)
    TextView tvNetSpeed;

    @Bind(R.id.pbProgress)
    NumberProgressBar pbProgress;

    @NonNull
    @Override
    protected DownloadContract.Presenter getMvpPresenter() {
        return new DownloadPresenter();
    }

    @NonNull
    @Override
    protected DownloadContract.View getMvpView() {
        return this;
    }

    @OnClick(R.id.fileDownload)
    public void fileDownload(View view) {
        mPresenter.fileDownload();
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
    protected int getContentViewId() {
        return R.layout.activity_file_download;
    }

    @Override
    protected void initView() {

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
