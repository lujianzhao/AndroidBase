package com.ljz.androidbasedemo.http.view;

import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ljz.base.common.logutils.LogUtils;
import com.ljz.base.frame.activity.impl.BaseMvpActivity;
import com.ljz.base.frame.presenter.factory.RequiresPresenter;
import com.ljz.androidbasedemo.R;
import com.ljz.androidbasedemo.http.contract.DownloadContract;
import com.ljz.androidbasedemo.http.presenter.DownloadPresenter;
import com.ljz.androidbasedemo.view.NumberProgressBar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/23 13:31
 * 描述:
 */
@RequiresPresenter(DownloadPresenter.class)
public class DownloadActivity extends BaseMvpActivity<DownloadContract.Presenter> implements DownloadContract.View {

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



    @OnClick(R.id.fileDownload)
    public void fileDownload(View view) {
        getPresenter().fileDownload();
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
