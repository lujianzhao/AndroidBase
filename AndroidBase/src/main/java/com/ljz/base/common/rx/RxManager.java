package com.ljz.base.common.rx;

import com.ljz.base.rxbus.Bus;
import com.ljz.base.rxbus.thread.ThreadEnforcer;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.ljz.base.common.rx.RxUtil.getNewCompositeSubIfUnsubscribed;

/**
 * 用于管理RxBus的事件和Rxjava相关代码的生命周期处理
 */
public class RxManager  {

    private Bus mRxBus = new Bus(ThreadEnforcer.ANY);
    private CompositeDisposable mCompositeSubscription;// 管理订阅者者

    public RxManager(Object object){
        mCompositeSubscription = getNewCompositeSubIfUnsubscribed(mCompositeSubscription);
        mRxBus.register(object);
    }

    public void add(Disposable disposable) {
        mCompositeSubscription.add(disposable);
    }

    public void clear(Object object) {
        RxUtil.unsubscribeIfNotNull(mCompositeSubscription);// 取消订阅
        mCompositeSubscription = null;
        mRxBus.unregister(object);
        mRxBus = null;
    }

    public void post(Object event) {
        mRxBus.post(event);
    }
    public void post(String tag, Object event){
        mRxBus.post(tag,event);
    }

}
