package com.android.base.frame.activity.impl;

import android.os.Bundle;
import android.support.annotation.CallSuper;

import com.android.base.netstate.NetWorkUtil;

/**
 * Created by Administrator on 2016/5/13.
 */
public abstract class BaseActivity extends SuperActivity {

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
