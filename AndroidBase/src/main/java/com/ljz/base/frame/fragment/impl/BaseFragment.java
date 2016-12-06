package com.ljz.base.frame.fragment.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ljz.base.common.rx.RxManager;

/**
 * Created by Administrator on 2016/5/13.
 */
public abstract class BaseFragment extends SuperFragment {

    private final RxManager mRxManager = new RxManager();

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        onInitView(savedInstanceState);
        onInitData();
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
    }

    @Override
    protected void onInitData() {
    }

    protected RxManager getRxManager() {
        return mRxManager;
    }

    @Override
    public void onDestroy() {
        mRxManager.clear();
        super.onDestroy();
    }
}
