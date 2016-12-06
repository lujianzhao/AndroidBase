package com.ljz.androidbasedemo.db.view;

import android.view.View;
import android.widget.TextView;

import com.ljz.base.frame.activity.impl.BaseMvpActivity;
import com.ljz.base.frame.presenter.factory.RequiresPresenter;
import com.ljz.androidbasedemo.R;
import com.ljz.androidbasedemo.db.contract.DBContract;
import com.ljz.androidbasedemo.db.presenter.DBPresenter;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:49
 * 描述:
 */
@RequiresPresenter(DBPresenter.class)
public class DBActivity extends BaseMvpActivity<DBContract.Presenter> implements DBContract.View{

    @Bind(R.id.text_city)
    TextView textView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_db;
    }

    @OnClick({R.id.insert,R.id.query,R.id.clear})
    public void onBtnClick(View view) {
        getPresenter().onBtnClick(view);
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
    public void clearView() {
        textView.setText("数据已清空");
    }

    @Override
    public void updateView(String result) {
        textView.setText(result);
    }

}
