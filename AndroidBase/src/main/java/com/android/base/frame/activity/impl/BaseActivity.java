package com.android.base.frame.activity.impl;

import android.os.Bundle;
import android.support.annotation.CallSuper;

/**
 * Created by Administrator on 2016/5/13.
 */
public abstract class BaseActivity extends SuperActivity {

    public abstract void initData();

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView(savedInstanceState);
        initData();
    }

}
