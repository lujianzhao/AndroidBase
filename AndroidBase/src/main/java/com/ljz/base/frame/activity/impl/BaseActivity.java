package com.ljz.base.frame.activity.impl;

import android.os.Bundle;
import android.support.annotation.CallSuper;

import com.ljz.base.common.rx.RxManager;
import com.ljz.base.netstate.NetWorkUtil;

/**
 * Created by Administrator on 2016/5/13.
 */
public abstract class BaseActivity extends SuperActivity {

    private final RxManager mRxManager = new RxManager();

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onInitView(savedInstanceState);
        onInitData();
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
    }

    protected RxManager getRxManager() {
        return mRxManager;
    }

    @Override
    protected void onDestroy() {
        mRxManager.clear();
        super.onDestroy();
    }

    @Override
    protected void onInitData() {
    }

    @Override
    public void onNetWorkDisConnect() {

    }

    @Override
    public void onNetWorkConnect(NetWorkUtil.NetWorkType type) {

    }

}
