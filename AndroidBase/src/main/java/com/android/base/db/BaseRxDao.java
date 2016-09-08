package com.android.base.db;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.android.base.callback.ExecutorCallBack;
import com.android.base.common.logutils.LogUtils;
import com.android.base.common.rx.RxUtil;
import com.android.base.db.ormlite.DatabaseUtil;
import com.android.base.db.ormlite.DbCache;
import com.android.base.db.ormlite.OrmLiteDao;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by lujianzhao on 2016/2/29.
 * <p/>
 * 提供同步与异步两种方式读写数据库
 * <p/>
 * 如果使用异步方式读写数据库，必须交由RxManager管理Subscription的生命周期
 */
public abstract class BaseRxDao<T> extends OrmLiteDao<T> {

    protected boolean cache;
    protected Class<T> clazz;
    protected String tableName;

    public BaseRxDao(Context context, Class<T> cls) {
        this(context, cls, true);
    }

    /**
     * @param context context
     * @param cls     表结构clazz
     * @param cache   是否缓存，如果设置缓存，数据查询将优先读取缓存
     */
    public BaseRxDao(Context context, Class<T> cls, boolean cache) {
        super(context, cls);
        this.clazz = cls;
        this.cache = cache;
        tableName = DatabaseUtil.extractTableName(cls);
    }

    /**
     * 增加一条记录
     */
    public boolean insert(T t) {
        boolean result = super.insert(t);
        if (result) {
            DbCache.getInstance().clearByTable(tableName);
        }
        return result;
    }

    /**
     * 增加一条记录
     */
    public Subscription insertSync(final T t, final ExecutorCallBack<Boolean> listener) {
        return subscribe(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return insert(t);
            }
        }, new Action1<Boolean>() {
            @Override
            public void call(Boolean result) {
                listener.onNext(result);
            }
        });
    }

    /**
     * 批量插入;
     */
    public boolean insertForBatch(List<T> list) {
        boolean result = super.insertForBatch(list);
        if (result) {
            DbCache.getInstance().clearByTable(tableName);
        }
        return result;
    }

    /**
     * 批量插入
     */
    public Subscription insertForBatchSync(final List<T> list, final ExecutorCallBack<Boolean> listener) {
        return subscribe(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return insertForBatch(list);
            }
        }, new Action1<Boolean>() {
            @Override
            public void call(Boolean result) {
                listener.onNext(result);
            }
        });

    }

    /**
     * 清空数据
     */
    public boolean clearTableData() {
        boolean result = super.clearTableData();
        if (result) {
            DbCache.getInstance().clearByTable(tableName);
        }
        return result;
    }

    /**
     * 清空数据
     */
    public Subscription clearTableDataSync(final ExecutorCallBack<Boolean> listener) {
        return subscribe(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return clearTableData();
            }
        }, new Action1<Boolean>() {
            @Override
            public void call(Boolean result) {
                listener.onNext(result);
            }
        });
    }

    /**
     * 根据id删除记录
     */
    public boolean deleteById(Integer id) {
        boolean result = super.deleteById(id);
        if (result) {
            DbCache.getInstance().clearByTable(tableName);
        }
        return result;
    }

    /**
     * 根据id删除记录
     */
    public Subscription deleteByIdSync(final Integer id, final ExecutorCallBack<Boolean> listener) {
        return subscribe(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return deleteById(id);
            }
        }, new Action1<Boolean>() {
            @Override
            public void call(Boolean result) {
                listener.onNext(result);
            }
        });
    }

    public List<T> queryForAll() {
        if (!cache) {
            return super.queryForAll();
        }
        String json = DbCache.getInstance().getCache(tableName, "queryForAll");
        List<T> result = JSON.parseArray(json, clazz);
        if (result != null) {
            LogUtils.d("---------query from cache--");
            return result;
        }
        result = super.queryForAll();
        DbCache.getInstance().addCache(tableName, "queryForAll", result);
        return result;
    }

    public Subscription queryForAllSync(final ExecutorCallBack<List<T>> listener) {
        return subscribe(new Callable<List<T>>() {
            @Override
            public List<T> call() {
                return queryForAll();
            }
        }, new Action1<List<T>>() {
            @Override
            public void call(List<T> result) {
                listener.onNext(result);
            }
        });
    }

    protected  <R> Subscription subscribe(Callable<R> callable, Action1<R> action) {
        Observable<R> observable = RxUtil.getObservable(callable);
        return observable.compose(RxUtil.<R>applySchedulers()).subscribe(action);
    }

}
