package com.android.base.db.ormlite;

/**
 * Created by huangzj on 2016/3/1.
 * <p/>
 * 数据库操作回调
 */
public interface DbCallBack<T> {

    void onComplete(T data);
}
