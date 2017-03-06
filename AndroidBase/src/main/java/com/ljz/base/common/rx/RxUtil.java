package com.ljz.base.common.rx;


import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/3.
 */
public class RxUtil {

    public static void unsubscribeIfNotNull(CompositeDisposable disposable) {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public static CompositeDisposable getNewCompositeSubIfUnsubscribed(CompositeDisposable disposable) {
        if (null == disposable ||  disposable.isDisposed()) {
            return new CompositeDisposable();
        }
        return disposable;
    }

    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> applySchedulersProgress() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.sample(200, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io());
            }
        };
    }


    public static <T> ObservableTransformer<T, T> applySchedulersForRetrofit() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io());
            }
        };
    }

    public static <T> Observable<T> getDBObservable(final Callable<T> callable) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                e.onNext(callable.call());
            }
        }).compose(RxUtil.<T>applySchedulers());
    }

}
