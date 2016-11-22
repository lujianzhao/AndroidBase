package com.android.base.db.ormlite;

/**
 * 数据库操作回调
 */
public interface DbCallBack<T> {

    void onComplete(T data);
}
