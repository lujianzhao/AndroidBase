package com.ljz.androidbasedemo.http.view;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ljz.base.common.logutils.LogUtils;
import com.ljz.base.common.thread.OverloadPolicy;
import com.ljz.base.common.thread.SchedulePolicy;
import com.ljz.base.common.thread.SmartExecutor;
import com.ljz.base.common.utils.HandlerUtil;
import com.ljz.base.frame.activity.impl.BaseMvpActivity;
import com.ljz.base.frame.presenter.factory.RequiresPresenter;
import com.ljz.androidbasedemo.R;
import com.ljz.androidbasedemo.http.contract.UploadContract;
import com.ljz.androidbasedemo.http.presenter.UploadPresenter;
import com.ljz.androidbasedemo.view.NumberProgressBar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/20 16:22
 * 描述:
 */
@RequiresPresenter(UploadPresenter.class)
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

    protected SmartExecutor mainExecutor;

    private static boolean flag = true;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_upload;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        //        initSmartExecutor();
//
//        if (flag) {
//            flag = false;
//            ro();
//        }
    }


    public void ro() {
        mainExecutor.submit(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);

                HandlerUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                });


                SystemClock.sleep(1000);

                HandlerUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                });
            }
        });
    }

    private void initSmartExecutor() {
        if (mainExecutor == null) {
            // set this temporary parameter, just for test
            // 智能并发调度控制器：设置[最大并发数]，和[等待队列]大小仅供测试，具体根据实际场景设置。
            mainExecutor = SmartExecutor.getInstance();

            // 打开调试和日志，发布时建议关闭。
            mainExecutor.setDebug(true);

            // number of concurrent threads at the same time, recommended core size is CPU count
            mainExecutor.setCoreSize(2);

            // adjust maximum number of waiting queue size by yourself or based on phone performance
            mainExecutor.setQueueSize(100);

            // 任务数量超出[最大并发数]后，自动进入[等待队列]，等待当前执行任务完成后按策略进入执行状态：后进先执行。
            mainExecutor.setSchedulePolicy(SchedulePolicy.LastInFirstRun);

            // 后续添加新任务数量超出[等待队列]大小时，执行过载策略：抛弃队列内最旧任务。
            mainExecutor.setOverloadPolicy(OverloadPolicy.DiscardOldTaskInQueue);

            //GoUtil.showTips(activity, "LiteGo", "Let It Go!");
        }
    }

    @OnClick(R.id.selectImage)
    public void selectImage(View view) {
        getPresenter().selectImage();

    }

    @OnClick(R.id.formUpload)
    public void formUpload(View view) {
        getPresenter().formUpload();
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
        LogUtils.d(( currentSize *1.0F / totalSize )*100.0F+"");
        pbProgress.setMax(100);
        pbProgress.setProgress((int) (( currentSize  / totalSize )*100.0F));
    }
}
