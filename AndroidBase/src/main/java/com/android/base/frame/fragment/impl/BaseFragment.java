package com.android.base.frame.fragment.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2016/5/13.
 */
public abstract class BaseFragment extends SuperFragment {


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onInitView(savedInstanceState);
        onInitData();
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
    }

    @Override
    protected void onInitData() {
    }
}
