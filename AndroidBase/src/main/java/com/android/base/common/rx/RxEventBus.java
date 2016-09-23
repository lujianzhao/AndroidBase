package com.android.base.common.rx;

import android.support.annotation.NonNull;

import com.android.base.common.logutils.LogUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * 用RxJava实现的EventBus
 */
public class RxEventBus {
    private static RxEventBus sInstance;

    public static  RxEventBus getInstance() {
        if (null == sInstance) {
            synchronized (RxEventBus.class) {
                if (null == sInstance) {
                    sInstance = new RxEventBus();
                }
            }
        }
        return sInstance;
    }

    private RxEventBus() {
    }

    @SuppressWarnings("rawtypes")
    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<Object, List<Subject>>();

    /**
     * 订阅事件源
     *
     * @param mObservable
     * @param mAction1
     * @return
     */
    public RxEventBus OnEvent(Observable<?> mObservable, Action1<Object> mAction1) {
        mObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(mAction1, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
                LogUtils.e(throwable);
            }
        });
        return getInstance();
    }

    /**
     * 注册事件源
     *
     * @param tag
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    public <T> Observable<T> register(@NonNull Object tag) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new CopyOnWriteArrayList<>();
            subjectMapper.put(tag, subjectList);
        }
        Subject<T, T> subject = new SerializedSubject<>(BehaviorSubject.<T>create());
        subjectList.add(subject);
        LogUtils.d("register" + tag + "  size:" + subjectList.size());
        return subject;
    }

    @SuppressWarnings("rawtypes")
    public void unregister(@NonNull Object tag) {
        List<Subject> subjects = subjectMapper.get(tag);
        if (null != subjects) {
            subjectMapper.remove(tag);
        }
    }


    /**
     * 取消监听
     *
     * @param tag
     * @param observable
     * @return
     */
    @SuppressWarnings("rawtypes")
    public RxEventBus unregister(@NonNull Object tag, @NonNull Observable<?> observable) {
        if (null == observable) {
            return getInstance();
        }

        List<Subject> subjects = subjectMapper.get(tag);
        if (null != subjects) {
            subjects.remove((Subject) observable);
            if (isEmpty(subjects)) {
                subjectMapper.remove(tag);
                LogUtils.d("unregister" + tag + "  size:" + subjects.size());
            }
        }
        return getInstance();
    }


    public void post(@NonNull Object content) {
        post(content.getClass().getName(), content);
    }

    /**
     * 触发事件
     *
     * @param content
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void post(@NonNull Object tag, @NonNull Object content) {
        LogUtils.d("post eventName: " + tag);
        List<Subject> subjectList = subjectMapper.get(tag);
        if (!isEmpty(subjectList)) {
            for (Subject subject : subjectList) {
                subject.onNext(content);
                LogUtils.d("onEvent eventName: " + tag);
            }
        }
    }



    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection<Subject> collection) {
        return null == collection || collection.isEmpty();
    }
}