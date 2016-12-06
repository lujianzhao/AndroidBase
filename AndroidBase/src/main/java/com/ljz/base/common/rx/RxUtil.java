package com.ljz.base.common.rx;


import com.ljz.base.common.logutils.LogUtils;
import com.ljz.base.http.progress.domain.ProgressRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Observable.Transformer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/5/3.
 */
public class RxUtil {

    public static void unsubscribeIfNotNull(Subscription subscription) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public static CompositeSubscription getNewCompositeSubIfUnsubscribed(CompositeSubscription subscription) {
        if (subscription == null || subscription.isUnsubscribed()) {
            return new CompositeSubscription();
        }
        return subscription;
    }

    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return new Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> Observable.Transformer<T, T> applySchedulersProgress() {
        return new Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .sample(200, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io());
            }
        };
    }


    public static <T> Observable.Transformer<T, T> applySchedulersForRetrofit() {
        return new Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io());
            }
        };
    }

    public static <T> Observable<T> getDBObservable(final Callable<T> callable) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(callable.call());
                        } catch (Exception ex) {
                            LogUtils.e(ex);
                        }
                    }
                });
    }



    public static  Observable<ProgressRequest> getDownloadObservable(final Call<ResponseBody> call, final String filePath) {
        return Observable.create(new Observable.OnSubscribe<ProgressRequest>() {
            private InputStream is;
            private FileOutputStream fos;
            @Override
            public void call(Subscriber<? super ProgressRequest> subscriber) {
                try {
                    Response<ResponseBody> response=call.execute();
                    try {
                        if (response != null && response.isSuccessful()) {
                            //文件总长度
                            long fileSize = response.body().contentLength();
                            long fileSizeDownloaded = 0;
                            is = response.body().byteStream();
                            File file = new File(filePath);
                            ProgressRequest progressRequest = new ProgressRequest(file.getName(), file.getAbsolutePath(),fileSizeDownloaded,fileSize);

                            if (file.exists()) {
                                file.delete();
                            } else {
                                file.createNewFile();
                            }
                            fos = new FileOutputStream(file);
                            int count = 0;
                            byte[] buffer = new byte[1024];

                            while ((count = is.read(buffer)) != -1) {
                                fos.write(buffer, 0, count);
                                fileSizeDownloaded += count;
                                progressRequest.setCurrentBytes(fileSizeDownloaded);
                                subscriber.onNext(progressRequest);
                            }
                            fos.flush();
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Exception("接口请求异常"));
                        }
                    } catch (Exception e) {
                        subscriber.onError(e);
                        LogUtils.e(e);
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                LogUtils.e(e);
                            }finally {
                                is = null;
                            }
                        }
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                LogUtils.e(e);
                            }finally {
                                fos = null;
                            }
                        }
                    }
                } catch (IOException e) {
                    LogUtils.e(e);
                    subscriber.onError(e);
                }
            }
        });
    }
}
