package com.ljz.androidbasedemo.http;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ljz.base.frame.activity.impl.BaseActivity;
import com.ljz.androidbasedemo.R;
import com.ljz.androidbasedemo.http.view.DownloadActivity;
import com.ljz.androidbasedemo.http.view.GetAndPostActivity;
import com.ljz.androidbasedemo.http.view.UploadActivity;

import butterknife.Bind;

public class HttpActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.title)
    TextView mTitle;

    @Bind(R.id.container)
    LinearLayout mContainer;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitData() {
        mTitle.setText("Http test");
        String[] bttxt = getResources().getStringArray(R.array.http_list);
        if (bttxt != null) {
            for (int i = 0; i < bttxt.length; i++) {
                Button bt = new Button(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                int margin = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
                lp.setMargins(margin, margin, margin, margin);
                bt.setId(i);
                bt.setText(bttxt[i]);
                bt.setOnClickListener(this);
                bt.setLayoutParams(lp);
                mContainer.addView(bt);
            }
        }
    }



    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case 0:
                //get和post请求
                gotoActivity(GetAndPostActivity.class, false);
                break;

            case 1:
                // 上传图片
                gotoActivity(UploadActivity.class, false);
                break;

            case 2:
                // 下载文件
                gotoActivity(DownloadActivity.class, false);
                break;

            default:
                break;
        }

    }
}
