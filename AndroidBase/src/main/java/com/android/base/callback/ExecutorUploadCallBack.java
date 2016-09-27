package com.android.base.callback;

/**
 * Created by lujianzhao on 2016/6/19.
 * <p>
 * Presenter获取数据的统一回调
 *
 */
public abstract class ExecutorUploadCallBack<T,R>  extends ExecutorCallBack<T> {

    public void onStart() {
    }

    /**
     * 上传完毕返回的结果
     * @param data
     */
    public void onComplete(R responseData) {
    }

    public void onError(Throwable e) {
    }

    /**
     * 上传进度
     * @param progressSpeed
     */
    public  void onNext(T progressSpeed){
    }
}
