package com.android.base.frame.model.impl;

import android.support.annotation.NonNull;

import com.android.base.common.rx.RxManager;
import com.android.base.frame.model.IBaseModel;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 11:34
 * 描述:
 */
public abstract class BaseModel implements IBaseModel {

    private RxManager mRxManager;

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        mRxManager = null;
    }

    @Override
    public void setRxManager(@NonNull RxManager rxManager) {
        this.mRxManager = rxManager;
    }
}
