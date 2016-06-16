package com.android.base.frame.activity;

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
        initView();
        initData();
    }

    public abstract void initData();

    @Override
    public void onDisConnect() {

    }

    @Override
    public void onConnect(NetWorkUtil.NetWorkType type) {

    }
}
