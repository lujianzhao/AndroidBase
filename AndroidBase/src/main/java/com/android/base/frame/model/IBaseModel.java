package com.android.base.frame.model;

import android.content.Context;

/**
 * Created by Administrator on 2016/5/13.
 */
public interface IBaseModel {
    void onCreate();
    void onDestroy();
    void initModel(Context context);
}
