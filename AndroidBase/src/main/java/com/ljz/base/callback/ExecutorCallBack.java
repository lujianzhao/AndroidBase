package com.ljz.base.callback;

import rx.Subscriber;

/**
 * Created by lujianzhao on 2016/6/19.
 * <p>
 * Presenter获取数据的统一回调
 */
public abstract class ExecutorCallBack<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }
}

