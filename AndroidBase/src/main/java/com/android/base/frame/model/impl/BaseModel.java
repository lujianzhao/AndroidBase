package com.android.base.frame.model.impl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.base.common.rx.RxManager;
import com.android.base.frame.model.IBaseModel;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 11:34
 * 描述: Model层,请把所有的rxJava的生命周期都交由 RxManager 管理,调用 add(Subscription) 方法
 */
public abstract class BaseModel implements IBaseModel {

    protected RxManager mRxManager = new RxManager();

    protected Context mContext;

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        if (mRxManager != null) {
            mRxManager.clear();
            mRxManager = null;
        }
        mContext = null;
    }

    @Override
    public void initModel(@NonNull Context context) {
        this.mContext = context;
    }

}
