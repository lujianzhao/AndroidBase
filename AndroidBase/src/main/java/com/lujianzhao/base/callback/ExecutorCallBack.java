package com.lujianzhao.base.callback;

/**
 * Created by lujianzhao on 2016/6/19.
 * <p>
 * Presenter获取数据的统一回调
 *
 */
public abstract class ExecutorCallBack<T>  {

    public void onStart() {
    }

    public void onCompleted() {
    }

    public void onError(Throwable e) {
    }

    public abstract void onNext(T data);
}
