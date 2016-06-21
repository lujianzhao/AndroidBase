package com.android.base.frame.model;

import android.content.Context;

import com.android.base.common.rx.RxManager;

/**
 * Created by Administrator on 2016/5/13.
 */
public interface IBaseModel {
    void onCreate();
    void onDestroy();
    void initModel(Context context,RxManager rxManager);
}
