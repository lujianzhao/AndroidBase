package com.android.base.db;

import com.alibaba.fastjson.JSON;
import com.android.base.callback.ExecutorCallBack;
import com.android.base.common.logutils.LogUtils;
import com.android.base.common.rx.RxUtil;
import com.android.base.db.ormlite.DatabaseUtil;
import com.android.base.db.ormlite.DbCache;
import com.android.base.db.ormlite.OrmLiteDao;
import com.j256.ormlite.dao.Dao;

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
public class BaseRxDao<T> extends OrmLiteDao<T> {

    protected boolean cache;
    protected Class<T> clazz;
    protected String tableName;

    public BaseRxDao(Class<T> cls) {
        this(cls, true);
    }

    /**
     * @param cls   表结构clazz
     * @param cache 是否缓存，如果设置缓存，数据查询将优先读取缓存
     */
    public BaseRxDao(Class<T> cls, boolean cache) {
        super(cls);
        this.clazz = cls;
        this.cache = cache;
        tableName = DatabaseUtil.extractTableName(cls);
    }


    /**
     * 增加或更新一条记录
     */
    public Dao.CreateOrUpdateStatus createOrUpdate(T t) {
        Dao.CreateOrUpdateStatus result = super.createOrUpdate(t);
        boolean flag = result.isCreated() || (result.isUpdated() && result.getNumLinesChanged() > 0);
        if (flag) {
            DbCache.getInstance().clearByTable(tableName);
        }
        return result;
    }

    /**
     * 增加或更新一条记录
     */
    public Subscription createOrUpdateSync(final T t, final ExecutorCallBack<Dao.CreateOrUpdateStatus> listener) {
        return subscribe(new Callable<Dao.CreateOrUpdateStatus>() {
            @Override
            public Dao.CreateOrUpdateStatus call() {
                return createOrUpdate(t);
            }
        }, new Action1<Dao.CreateOrUpdateStatus>() {
            @Override
            public void call(Dao.CreateOrUpdateStatus result) {
                listener.onNext(result);
            }
        });
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

    /**
     * 分页排序查询
     *
     * @param orderColumn 排序列名
     * @param ascending   true为升序,false为降序
     * @param offset      搜索下标
     * @param count       搜索条数
     * @return
     */
    public List<T> queryForAllForPagesByOrder(String orderColumn, boolean ascending, Long offset, Long count) {
        if (!cache) {
            return super.queryForAllForPagesByOrder(orderColumn,ascending,offset,count);
        }
        String json = DbCache.getInstance().getCache(tableName, "queryForAllForPagesByOrder"+orderColumn+String.valueOf(ascending)+String.valueOf(offset)+String.valueOf(count));
        List<T> result = JSON.parseArray(json, clazz);
        if (result != null) {
            LogUtils.d("---------query from cache--");
            return result;
        }
        result = super.queryForAllForPagesByOrder(orderColumn,ascending,offset,count);
        DbCache.getInstance().addCache(tableName, "queryForAllForPagesByOrder"+orderColumn+String.valueOf(ascending)+String.valueOf(offset)+String.valueOf(count), result);
        return result;
    }

    /**
     * 分页排序查询
     *
     * @param orderColumn 排序列名
     * @param ascending   true为升序,false为降序
     * @param offset      搜索下标
     * @param count       搜索条数
     * @return
     */
    public Subscription queryForAllForPagesByOrderSync(final String orderColumn, final boolean ascending, final Long offset, final Long count, final ExecutorCallBack<List<T>> listener) {
        return subscribe(new Callable<List<T>>() {
            @Override
            public List<T> call() {
                return queryForAllForPagesByOrder(orderColumn,ascending,offset,count);
            }
        }, new Action1<List<T>>() {
            @Override
            public void call(List<T> result) {
                listener.onNext(result);
            }
        });
    }


    /**
     * 排序查询
     *
     * @param orderColumn 排序的列
     * @param ascending   true为升序,false为降序
     * @return
     */
    public List<T> queryForAllByOrder(String orderColumn, boolean ascending) {
        if (!cache) {
            return super.queryForAllByOrder(orderColumn,ascending);
        }
        String json = DbCache.getInstance().getCache(tableName, "queryForAllByOrder"+orderColumn+String.valueOf(ascending));
        List<T> result = JSON.parseArray(json, clazz);
        if (result != null) {
            LogUtils.d("---------query from cache--");
            return result;
        }
        result = super.queryForAllByOrder(orderColumn,ascending);
        DbCache.getInstance().addCache(tableName, "queryForAllByOrder"+orderColumn+String.valueOf(ascending), result);
        return result;
    }

    /**
     * 排序查询
     *
     * @param orderColumn 排序的列
     * @param ascending   true为升序,false为降序
     * @return
     */
    public Subscription queryForAllByOrderSync(final String orderColumn, final boolean ascending, final ExecutorCallBack<List<T>> listener) {
        return subscribe(new Callable<List<T>>() {
            @Override
            public List<T> call() {
                return queryForAllByOrder(orderColumn,ascending);
            }
        }, new Action1<List<T>>() {
            @Override
            public void call(List<T> result) {
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

    protected <R> Subscription subscribe(Callable<R> callable, Action1<R> action) {
        Observable<R> observable = RxUtil.getObservable(callable);
        return observable.compose(RxUtil.<R>applySchedulers()).subscribe(action);
    }

}
