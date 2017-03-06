package com.ljz.base.db;

import com.j256.ormlite.dao.Dao;
import com.ljz.base.callback.ExecutorCallBack;
import com.ljz.base.common.rx.RxUtil;
import com.ljz.base.db.ormlite.OrmLiteDao;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


/**
 * Created by lujianzhao on 2016/2/29.
 * <p/>
 * 提供同步与异步两种方式读写数据库
 * <p/>
 * 如果使用异步方式读写数据库，必须交由RxManager管理Subscription的生命周期
 */
public class BaseRxDao<T> extends OrmLiteDao<T> {

    /**
     * @param cls 表结构clazz
     */
    public BaseRxDao(Class<T> cls) {
        super(cls);
    }

    /**
     * 增加或更新一条记录
     */
    public void createOrUpdateSync(final T t, ExecutorCallBack<Dao.CreateOrUpdateStatus> observer) {
        Observable.create(new ObservableOnSubscribe<Dao.CreateOrUpdateStatus>() {
            @Override
            public void subscribe(ObservableEmitter<Dao.CreateOrUpdateStatus> e) throws Exception {
                e.onNext(createOrUpdate(t));
                e.onComplete();
            }
        }).compose(RxUtil.<Dao.CreateOrUpdateStatus>applySchedulers()).subscribe(observer);
    }

    /**
     * 增加一条记录
     */
    public void insert(final T t, ExecutorCallBack<Boolean> observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(insert(t));
                e.onComplete();
            }
        }).compose(RxUtil.<Boolean>applySchedulers()).subscribe(observer);
    }

    /**
     * 批量插入
     */
    public void insertForBatch(final List<T> list, ExecutorCallBack<Boolean> observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(insertForBatch(list));
                e.onComplete();
            }
        }).compose(RxUtil.<Boolean>applySchedulers()).subscribe(observer);
    }

    /**
     * 清空数据
     */
    public void clearTableData(ExecutorCallBack<Boolean> observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(clearTableData());
                e.onComplete();
            }
        }).compose(RxUtil.<Boolean>applySchedulers()).subscribe(observer);
    }

    /**
     * 根据id删除记录
     */
    public void deleteById(final Integer id, ExecutorCallBack<Boolean> observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(deleteById(id));
                e.onComplete();
            }
        }).compose(RxUtil.<Boolean>applySchedulers()).subscribe(observer);
    }

    /**
     * 按列删除
     *
     * @param columnName 指定条件列名
     * @param value      值
     */
    public void deleteByColumnName(final String columnName, final Object value, ExecutorCallBack<Boolean> observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(deleteByColumnName(columnName, value));
                e.onComplete();
            }
        }).compose(RxUtil.<Boolean>applySchedulers()).subscribe(observer);
    }

    /**
     * 通过列的键值组合删除
     *
     * @param map      列的键值组合
     * @param observer
     */
    public void deleteByColumnName(final Map<String, Object> map, ExecutorCallBack<Boolean> observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(deleteByColumnName(map));
                e.onComplete();
            }
        }).compose(RxUtil.<Boolean>applySchedulers()).subscribe(observer);
    }

    /**
     * 删除小于指定列指定值的数据
     *
     * @param columnName 列名
     * @param value      列值，删除小于该值的所有数据将被删除
     * @param observer   回调接口
     */
    public void deleteLtValue(final String columnName, final Object value, ExecutorCallBack<Integer> observer) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(deleteLtValue(columnName, value));
                e.onComplete();
            }
        }).compose(RxUtil.<Integer>applySchedulers()).subscribe(observer);
    }

    /**
     * 批量删除
     */
    public void deleteForBatch(final List<T> list, ExecutorCallBack<Boolean> observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(deleteForBatch(list));
                e.onComplete();
            }
        }).compose(RxUtil.<Boolean>applySchedulers()).subscribe(observer);
    }

    /**
     * 修改记录
     *
     * @param t 新的数据实体,ID不能为空
     */
    public void update(final T t, ExecutorCallBack<Boolean> observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(update(t));
                e.onComplete();
            }
        }).compose(RxUtil.<Boolean>applySchedulers()).subscribe(observer);
    }

    /**
     * 修改记录
     *
     * @param t          新的数据实体
     * @param columnName 指定查询条件列名
     * @param value      查询条件值
     * @param observer
     */
    public void updateBy(final T t, final String columnName, final Object value, ExecutorCallBack<Boolean> observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(updateBy(t, columnName, value));
                e.onComplete();
            }
        }).compose(RxUtil.<Boolean>applySchedulers()).subscribe(observer);
    }

    /**
     * 按条件更新
     *
     * @param t        新的数据实体
     * @param value    更新条件
     * @param observer
     */
    public void updateBy(final T t, final Map<String, Object> value, ExecutorCallBack<Boolean> observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(updateBy(t, value));
                e.onComplete();
            }
        }).compose(RxUtil.<Boolean>applySchedulers()).subscribe(observer);
    }

    /**
     * 批量修改
     *
     * @param list     记录集合
     * @param observer
     */
    public void updateForBatch(final List<T> list, ExecutorCallBack<Boolean> observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(updateForBatch(list));
                e.onComplete();
            }
        }).compose(RxUtil.<Boolean>applySchedulers()).subscribe(observer);
    }

    /**
     * 查询符号指定条件的记录数
     *
     * @param map      查询条件的键值组合
     * @param observer
     */
    public void getCount(final Map<String, Object> map, ExecutorCallBack<Long> observer) {
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> e) throws Exception {
                e.onNext(getCount(map));
                e.onComplete();
            }
        }).compose(RxUtil.<Long>applySchedulers()).subscribe(observer);
    }

    /**
     * 查询数据库所有的记录数
     *
     * @param observer 回调
     */
    public void getCount(ExecutorCallBack<Long> observer) {
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> e) throws Exception {
                e.onNext(getCount());
                e.onComplete();
            }
        }).compose(RxUtil.<Long>applySchedulers()).subscribe(observer);
    }

    /**
     * 查询表中所有数据
     */
    public void queryForAll(ExecutorCallBack<List<T>> observer) {
        Observable.create(new ObservableOnSubscribe<List<T>>() {
            @Override
            public void subscribe(ObservableEmitter<List<T>> e) throws Exception {
                e.onNext(queryForAll());
                e.onComplete();
            }
        }).compose(RxUtil.<List<T>>applySchedulers()).subscribe(observer);
    }

    /**
     * 根据表中键值组合行查询
     *
     * @param map 查询条件的键值组合
     */
    public void queryByColumnName(final Map<String, Object> map, ExecutorCallBack<List<T>> observer) {
        Observable.create(new ObservableOnSubscribe<List<T>>() {
            @Override
            public void subscribe(ObservableEmitter<List<T>> e) throws Exception {
                e.onNext(queryByColumnName(map));
                e.onComplete();
            }
        }).compose(RxUtil.<List<T>>applySchedulers()).subscribe(observer);
    }

    /**
     * 列名查询
     *
     * @param columnName 指定查询条件列名
     * @param value      查询条件值
     */
    public void queryByColumnName(final String columnName, final Object value, ExecutorCallBack<List<T>> observer) {
        Observable.create(new ObservableOnSubscribe<List<T>>() {
            @Override
            public void subscribe(ObservableEmitter<List<T>> e) throws Exception {
                e.onNext(queryByColumnName(columnName, value));
                e.onComplete();
            }
        }).compose(RxUtil.<List<T>>applySchedulers()).subscribe(observer);
    }

    /**
     * 排序查询
     *
     * @param orderColumn 排序的列
     * @param columnName  指定查询条件列名
     * @param value       查询条件值
     * @param ascending   true为升序,false为降序
     */
    public void queryByOrder(final String orderColumn, final String columnName, final Object value, final boolean ascending, ExecutorCallBack<List<T>> observer) {
        Observable.create(new ObservableOnSubscribe<List<T>>() {
            @Override
            public void subscribe(ObservableEmitter<List<T>> e) throws Exception {
                e.onNext(queryByOrder(orderColumn, columnName, value, ascending));
                e.onComplete();
            }
        }).compose(RxUtil.<List<T>>applySchedulers()).subscribe(observer);
    }

    /**
     * 返回数据库中所有记录的指定列的值
     *
     * @param selectColumns 指定列名
     * @param observer
     */
    public void queryAllBySelectColumns(final String[] selectColumns, ExecutorCallBack<List<T>> observer) {
        Observable.create(new ObservableOnSubscribe<List<T>>() {
            @Override
            public void subscribe(ObservableEmitter<List<T>> e) throws Exception {
                e.onNext(queryAllBySelectColumns(selectColumns));
                e.onComplete();
            }
        }).compose(RxUtil.<List<T>>applySchedulers()).subscribe(observer);
    }

    /**
     * 排序查询指定条件下，大于指定值的所有记录
     *
     * @param orderColumn 大于的列
     * @param limitValue  大于的值
     * @param columnName  查询条件列名
     * @param value       查询条件值
     * @param ascending   true为升序,false为降序
     */
    public void queryGeByOrder(final String orderColumn, final Object limitValue, final String columnName, final Object value, final boolean ascending, ExecutorCallBack<List<T>> observer) {
        Observable.create(new ObservableOnSubscribe<List<T>>() {
            @Override
            public void subscribe(ObservableEmitter<List<T>> e) throws Exception {
                e.onNext(queryGeByOrder(orderColumn, limitValue, columnName, value, ascending));
                e.onComplete();
            }
        }).compose(RxUtil.<List<T>>applySchedulers()).subscribe(observer);
    }

    /**
     * 排序查询小于指定值的所有记录
     *
     * @param orderColumn 小于的列
     * @param limitValue  小于的值
     * @param ascending   true为升序,false为降序
     */
    public void queryLeByOrder(final String orderColumn, final Object limitValue, final boolean ascending, ExecutorCallBack<List<T>> observer) {
        Observable.create(new ObservableOnSubscribe<List<T>>() {
            @Override
            public void subscribe(ObservableEmitter<List<T>> e) throws Exception {
                e.onNext(queryLeByOrder(orderColumn, limitValue, ascending));
                e.onComplete();
            }
        }).compose(RxUtil.<List<T>>applySchedulers()).subscribe(observer);
    }

    /**
     * 分页查询,并按列排序
     *
     * @param orderColumn 排序列名
     * @param ascending   true为升序,false为降序
     * @param offset      搜索下标
     * @param count       搜索条数
     */
    public void queryForPagesByOrder(final String orderColumn, final boolean ascending, final Long offset, final Long count, ExecutorCallBack<List<T>> observer) {
        Observable.create(new ObservableOnSubscribe<List<T>>() {
            @Override
            public void subscribe(ObservableEmitter<List<T>> e) throws Exception {
                e.onNext(queryForPagesByOrder(orderColumn, ascending, offset, count));
                e.onComplete();
            }
        }).compose(RxUtil.<List<T>>applySchedulers()).subscribe(observer);
    }

    /**
     * 分页查询,并按列排序
     *
     * @param columnName  查询条件列名
     * @param value       查询条件值
     * @param orderColumn 排序列名
     * @param ascending   true为升序,false为降序
     * @param offset      搜索下标
     * @param count       搜索条数
     */
    public void queryForPagesByOrder(final String columnName, final Object value, final String orderColumn, final boolean ascending, final Long offset, final Long count, ExecutorCallBack<List<T>> observer) {
        Observable.create(new ObservableOnSubscribe<List<T>>() {
            @Override
            public void subscribe(ObservableEmitter<List<T>> e) throws Exception {
                e.onNext(queryForPagesByOrder(columnName, value, orderColumn, ascending, offset, count));
                e.onComplete();
            }
        }).compose(RxUtil.<List<T>>applySchedulers()).subscribe(observer);
    }

    /**
     * 分页查询,并按列排序
     *
     * @param map         查询的列条件
     * @param offset      查询的下标
     * @param count       查询的条数
     * @param orderColumn 排序的列
     * @param ascending   升序或降序,true为升序,false为降序
     */
    public void queryForPagesByOrder(final Map<String, Object> map, final String orderColumn, final boolean ascending, final Long offset, final Long count, ExecutorCallBack<List<T>> observer) {
        Observable.create(new ObservableOnSubscribe<List<T>>() {
            @Override
            public void subscribe(ObservableEmitter<List<T>> e) throws Exception {
                e.onNext(queryForPagesByOrder(map, orderColumn, ascending, offset, count));
                e.onComplete();
            }
        }).compose(RxUtil.<List<T>>applySchedulers()).subscribe(observer);
    }

    /**
     * 按条件查询，返回第一条符号要求的记录
     *
     * @param columnName 查询条件列名
     * @param value      查询条件值
     */
    public void queryForFirst(final String columnName, final Object value, ExecutorCallBack<T> observer) {
        Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                e.onNext(queryForFirst(columnName, value));
                e.onComplete();
            }
        }).compose(RxUtil.<T>applySchedulers()).subscribe(observer);
    }

    /**
     * 通过表列名查询第一条记录
     *
     * @param map 查询条件键值组合
     */
    public void queryForFirst(final Map<String, Object> map, ExecutorCallBack<T> observer) {
        Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                e.onNext(queryForFirst(map));
                e.onComplete();
            }
        }).compose(RxUtil.<T>applySchedulers()).subscribe(observer);
    }

    /**
     * 排序查询
     *
     * @param map         查询条件键值组合
     * @param orderColumn 排序的列
     * @param ascending   是否升序
     */
    public void queryForFirstByOrder(final Map<String, Object> map, final String orderColumn, final boolean ascending, ExecutorCallBack<T> observer) {
        Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                e.onNext(queryForFirstByOrder(map, orderColumn, ascending));
                e.onComplete();
            }
        }).compose(RxUtil.<T>applySchedulers()).subscribe(observer);
    }

    /**
     * 排序查询
     *
     * @param columnName  查询条件列名
     * @param value       查询条件值
     * @param orderColumn 排序的列
     * @param ascending   是否升序
     */
    public void queryForFirstByOrder(final String columnName, final Object value, final String orderColumn, final boolean ascending, ExecutorCallBack<T> observer) {
        Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                e.onNext(queryForFirstByOrder(columnName, value, orderColumn, ascending));
                e.onComplete();
            }
        }).compose(RxUtil.<T>applySchedulers()).subscribe(observer);
    }

}
