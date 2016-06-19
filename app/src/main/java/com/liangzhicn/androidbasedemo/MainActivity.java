package com.liangzhicn.androidbasedemo;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.base.frame.activity.impl.BaseActivity;
import com.liangzhicn.androidbasedemo.db.view.DBActivity;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.title)
    TextView mTitle;

    @Bind(R.id.container)
    LinearLayout mContainer;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }


    @Override
    public void initData() {
        mTitle.setText("老哦 test");
        String[] bttxt = getResources().getStringArray(R.array.test_list);
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
                gotoActivity(DBActivity.class, false);
                break;
            case 1:
                //gotoActivity(FragmentTabHostActivity.class, false);
                break;
            default:
                //showShortToast("还在开发中...");
                break;
        }

    }
}
