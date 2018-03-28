package com.ljz.base.db.ormlite;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.ljz.base.common.logutils.LogUtils;
import com.ljz.base.frame.Base;
import com.ljz.base.frame.BaseApplication;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;


/**
 * 统一的Dao实现，封装了增删查改的统一操作
 */
public class OrmLiteDao<T> {

    protected final Dao<T, Integer> ormLiteDao;

    protected final DatabaseHelper helper;

    public OrmLiteDao(Class<T> cls) {
        BaseApplication context = (BaseApplication) Base.getContext();
        helper = DatabaseHelper.getInstance(context.getApplicationContext());
        ormLiteDao = helper.getDao(cls);
    }

    /**
     * 获取指定类的数据访问对象
     *
     * @return
     */
    public Dao<T, Integer> getDao() {
        return ormLiteDao;
    }

    /**
     * 数据批量处理
     *
     * @param list      要处理的数据集合
     * @param batchType 操作类型
     * @return
     */
    private boolean doBatchInTransaction(final List<T> list, final int batchType) throws SQLException {
        ConnectionSource connectionSource = ormLiteDao.getConnectionSource();
        TransactionManager transactionManager = new TransactionManager(connectionSource);
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return doBatch(list, batchType);
            }
        };
        return transactionManager.callInTransaction(callable);
    }

    /**
     * 数据批量处理的实现
     *
     * @param list      要处理的数据集合
     * @param batchType 操作类型
     * @return
     */
    private boolean doBatch(List<T> list, int batchType) throws SQLException, IllegalAccessException {
        int result = 0;
        for (T t : list) {
            switch (batchType) {
                case DaoOperation.INSERT:
                    result += ormLiteDao.create(t);
                    break;
                case DaoOperation.DELETE:
                    result += ormLiteDao.delete(t);
                    break;
                case DaoOperation.UPDATE:
                    result += updateIfValueNotNull(t);
                    break;
                default:
                    LogUtils.w("no this type.");
                    break;
            }
        }
        return result == list.size();
    }

    /**
     * 增加或更新一条记录
     *
     * @param t 新增或更新数据实体
     * @return
     */
    public Dao.CreateOrUpdateStatus createOrUpdate(T t) throws SQLException {
        return ormLiteDao.createOrUpdate(t);
    }


    /**
     * 增加一条记录
     *
     * @param t
     * @return
     */
    public boolean insert(T t) throws SQLException {
        return ormLiteDao.create(t) > 0;
    }

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    public boolean insertForBatch(List<T> list) throws SQLException {
        return doBatchInTransaction(list, DaoOperation.INSERT);
    }

    /**
     * 清空表数据
     *
     * @return
     */
    public boolean clearTableData() throws SQLException {
        long count = ormLiteDao.countOf();
        if (count == 0) {
            return true;
        }
        DeleteBuilder deleteBuilder = ormLiteDao.deleteBuilder();
        deleteBuilder.where().isNotNull("id");
        return deleteBuilder.delete() > 0;
    }

    /**
     * 根据id删除记录
     *
     * @param id
     * @return
     */
    public boolean deleteById(Integer id) throws SQLException {
        return  ormLiteDao.deleteById(id) > 0;
    }

    /**
     * 按条件删除
     *
     * @param columnName 指定删除条件列名
     * @param value      删除条件对应的值
     * @return
     */
    public boolean deleteByColumnName(String columnName, Object value) throws SQLException {
        DeleteBuilder deleteBuilder = ormLiteDao.deleteBuilder();
        deleteBuilder.where().eq(columnName, value);
        return deleteBuilder.delete() > 0;
    }

    /**
     * 通过表列名来删除
     *
     * @param map key是列名,value是列对应的值
     * @return
     */
    public boolean deleteByColumnName(Map<String, Object> map) throws SQLException {
        DeleteBuilder deleteBuilder = ormLiteDao.deleteBuilder();
        Where where = deleteBuilder.where();
        where.isNotNull("id");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            where.and().eq(entry.getKey(), entry.getValue());
        }
        return deleteBuilder.delete() > 0;
    }

    /**
     * 删除小于指定列的值的所有数据
     *
     * @param columnName 指定列名
     * @param value      指定数值，小于该值的数据将被删除
     * @return 删除的条数
     */
    public int deleteLtValue(String columnName, Object value) throws SQLException {
        DeleteBuilder deleteBuilder = ormLiteDao.deleteBuilder();
        deleteBuilder.where().lt(columnName, value);
        return deleteBuilder.delete();
    }

    /**
     * 批量删除，list中的item必须有id
     *
     * @param list
     */
    public boolean deleteForBatch(List<T> list) throws SQLException {
        return doBatchInTransaction(list, DaoOperation.DELETE);
    }

    /**
     * 获取满足指定条件的记录数
     *
     * @param map 查询条件键值组合
     * @return
     */
    public long getCount(Map<String, Object> map) throws SQLException {
        QueryBuilder queryBuilder = ormLiteDao.queryBuilder();
        queryBuilder.setCountOf(true);
        Where where = queryBuilder.where();
        where.isNotNull("id");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            where.and().eq(entry.getKey(), entry.getValue());
        }
        PreparedQuery<T> preparedQuery = queryBuilder.prepare();
        return ormLiteDao.countOf(preparedQuery);
    }

    public long getCount() throws SQLException {
        QueryBuilder queryBuilder = ormLiteDao.queryBuilder();
        queryBuilder.setCountOf(true);
        Where where = queryBuilder.where();
        where.isNotNull("id");
        PreparedQuery<T> preparedQuery = queryBuilder.prepare();
        return ormLiteDao.countOf(preparedQuery);
    }

    /**
     * 查询全部记录
     *
     * @return
     */
    public List<T> queryForAll() throws SQLException {
        return ormLiteDao.queryForAll();
    }


    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    public T queryById(Integer id) throws SQLException {
        return ormLiteDao.queryForId(id);
    }

    /**
     * 根据表中任意字段进行查询
     *
     * @param map
     * @return
     */
    public List<T> queryByColumnName(Map<String, Object> map) throws SQLException {
        return ormLiteDao.queryForFieldValuesArgs(map);
    }


    /**
     * 通过表列名查询
     *
     * @param columnName
     * @param value
     * @return
     */
    public List<T> queryByColumnName(String columnName, Object value) throws SQLException {
        QueryBuilder queryBuilder = ormLiteDao.queryBuilder();
        queryBuilder.where().eq(columnName, value);
        return queryBuilder.query();

    }

    /**
     * 返回数据库中所有记录的指定列的值
     *
     * @param selectColumns 指定列名
     * @return
     */
    public List<T> queryAllBySelectColumns(String[] selectColumns) throws SQLException {
        QueryBuilder queryBuilder = ormLiteDao.queryBuilder();
        queryBuilder.selectColumns(selectColumns);
        return queryBuilder.query();
    }

    /**
     * 查询大于某个值的记录
     *
     * @param orderColumn 排序的列
     * @param value       某个值
     * @return 大于某个值的记录
     */
    public List<T> queryAllByGt(String orderColumn, Object value) throws SQLException {
        QueryBuilder queryBuilder = ormLiteDao.queryBuilder();
        Where where = queryBuilder.where();
        where.gt(orderColumn, value);
        return queryBuilder.query();
    }

    /**
     * 排序查询
     *
     * @param orderColumn 排序的列
     * @param ascending   true为升序,false为降序
     * @return
     */
    public List<T> queryAllByOrder(String orderColumn, boolean ascending) throws SQLException {
        QueryBuilder queryBuilder = ormLiteDao.queryBuilder();
        Where where = queryBuilder.where();
        where.isNotNull(orderColumn);
        queryBuilder.orderBy(orderColumn, ascending);
        return queryBuilder.query();
    }


    /**
     * 排序查询
     *
     * @param orderColumn 排序的列
     * @param columnName  指定查询条件列名
     * @param value       查询条件值
     * @param ascending   true为升序,false为降序
     * @return
     */
    public List<T> queryByOrder(String orderColumn, String columnName, Object value, boolean ascending) throws SQLException {
        QueryBuilder queryBuilder = ormLiteDao.queryBuilder();
        Where where = queryBuilder.where();
        where.eq(columnName, value);
        queryBuilder.orderBy(orderColumn, ascending);
        return queryBuilder.query();


    }

    /**
     * 排序查询指定条件下，大于指定值的所有记录
     *
     * @param orderColumn 大于的列
     * @param limitValue  大于的值
     * @param columnName  查询条件列名
     * @param value       查询条件值
     * @param ascending   true为升序,false为降序
     * @return
     */
    public List<T> queryGeByOrder(String orderColumn, Object limitValue, String columnName, Object value, boolean ascending) throws SQLException {
        QueryBuilder queryBuilder = ormLiteDao.queryBuilder();
        Where where = queryBuilder.where();
        where.eq(columnName, value);
        where.and().ge(orderColumn, limitValue);
        queryBuilder.orderBy(orderColumn, ascending);
        return queryBuilder.query();
    }

    /**
     * 排序查询小于指定值的所有记录
     *
     * @param orderColumn 小于的列
     * @param limitValue  小于的值
     * @param ascending   true为升序,false为降序
     * @return 查询结果
     */
    public List<T> queryLeByOrder(String orderColumn, Object limitValue, boolean ascending) throws SQLException {
        List<T> list = null;
        QueryBuilder queryBuilder = ormLiteDao.queryBuilder();
        Where where = queryBuilder.where();
        where.le(orderColumn, limitValue);
        queryBuilder.orderBy(orderColumn, ascending);
        return queryBuilder.query();
    }

    /**
     * 分页查询,并按列排序
     *
     * @param orderColumn 排序列名
     * @param ascending   true为升序,false为降序
     * @param offset      搜索下标
     * @param count       搜索条数
     * @return 分页查询后的数据集
     */
    public List<T> queryForPagesByOrder(String orderColumn, boolean ascending, Long offset, Long count) throws SQLException {
        QueryBuilder queryBuilder = ormLiteDao.queryBuilder();
        Where where = queryBuilder.where();
        where.isNotNull(orderColumn);
        queryBuilder.orderBy(orderColumn, ascending);
        queryBuilder.offset(offset);
        queryBuilder.limit(count);
        return queryBuilder.query();
    }


    /**
     * 分页排序查询
     *
     * @param columnName  查询条件列名
     * @param value       查询条件值
     * @param orderColumn 排序列名
     * @param ascending   true为升序,false为降序
     * @param offset      搜索下标
     * @param count       搜索条数
     * @return 分页查询后的数据集
     */
    public List<T> queryForPagesByOrder(String columnName, Object value, String orderColumn, boolean ascending, Long offset, Long count) throws SQLException {
        QueryBuilder queryBuilder = ormLiteDao.queryBuilder();
        Where where = queryBuilder.where();
        where.eq(columnName, value);
        queryBuilder.orderBy(orderColumn, ascending);
        queryBuilder.offset(offset);
        queryBuilder.limit(count);
        return queryBuilder.query();

    }


    /**
     * 按列排序后分页查询
     *
     * @param map         查询的列条件
     * @param offset      查询的下标
     * @param count       查询的条数
     * @param orderColumn 排序的列
     * @param ascending   升序或降序,true为升序,false为降序
     * @return
     */
    public List<T> queryForPagesByOrder(Map<String, Object> map, String orderColumn, boolean ascending, Long offset, Long count) throws SQLException {
        List<T> list = null;
        QueryBuilder queryBuilder = ormLiteDao.queryBuilder();
        Where where = queryBuilder.where();
        queryBuilder.orderBy(orderColumn, ascending);
        queryBuilder.offset(offset);
        queryBuilder.limit(count);
        where.isNotNull("id");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            where.and().eq(entry.getKey(), entry.getValue());
        }
        return queryBuilder.query();
    }

    /**
     * 按条件查询，返回第一条符号要求的记录
     *
     * @param columnName 查询条件列名
     * @param value      查询条件值
     * @return
     */
    public T queryForFirst(String columnName, Object value) throws SQLException {
        QueryBuilder<T, Integer> queryBuilder = ormLiteDao.queryBuilder();
        queryBuilder.where().eq(columnName, value);
        return queryBuilder.queryForFirst();
    }

    /**
     * 通过表列名查询第一条记录
     *
     * @param map
     * @return
     */
    public T queryForFirst(Map<String, Object> map) throws SQLException {
        QueryBuilder<T, Integer> queryBuilder = ormLiteDao.queryBuilder();
        Where where = queryBuilder.where();
        where.isNotNull("id");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            where.and().eq(entry.getKey(), entry.getValue());
        }
        return queryBuilder.queryForFirst();
    }

    /**
     * 排序查询
     *
     * @param map         查询条件键值组合
     * @param orderColumn 排序的列
     * @param ascending   是否升序
     * @return
     */
    public T queryForFirstByOrder(Map<String, Object> map, String orderColumn, boolean ascending) throws SQLException {
        QueryBuilder<T, Integer> queryBuilder = ormLiteDao.queryBuilder();
        Where where = queryBuilder.where();
        queryBuilder.orderBy(orderColumn, ascending);
        where.isNotNull("id");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            where.and().eq(entry.getKey(), entry.getValue());
        }
        return queryBuilder.queryForFirst();
    }

    /**
     * 排序查询
     *
     * @param columnName  查询条件列名
     * @param value       查询条件值
     * @param orderColumn 排序的列
     * @param ascending   是否升序
     * @return
     */
    public T queryForFirstByOrder(String columnName, Object value, String orderColumn, boolean ascending) throws SQLException {
        QueryBuilder<T, Integer> queryBuilder = ormLiteDao.queryBuilder();
        queryBuilder.orderBy(orderColumn, ascending);
        queryBuilder.where().eq(columnName, value);
        return queryBuilder.queryForFirst();
    }

    /**
     * 查询列名不等于指定值的记录
     *
     * @param columnName 列名
     * @param value      指定值
     */
    public List<T> queryNotEqualsByColumnName(String columnName, Object value) throws SQLException {
        QueryBuilder queryBuilder = ormLiteDao.queryBuilder();
        Where where = queryBuilder.where();
        where.or(where.gt(columnName, value), where.lt(columnName, value));
        return queryBuilder.query();
    }


    /**
     * 修改记录，ID不能为空
     *
     * @param t 更新的数据对象
     * @return 是否更新成功
     */
    public boolean update(T t) throws SQLException, IllegalAccessException {
        return updateIfValueNotNull(t) > 0;
    }

    /**
     * @param t          更新的数据对象
     * @param columnName 数据库中的columnName
     * @param value      数据库中columnName的value
     * @return 是否更新成功
     */
    public boolean updateBy(T t, String columnName, Object value) throws SQLException, NoSuchFieldException, IllegalAccessException {
        T t1 = queryForFirst(columnName, value);
        if (t1 == null) {
            throw new SQLException("no find this data in database:" + t);
        }
        setObjectValueIfNotNull(t1, t);
        return ormLiteDao.update(t1) > 0;
    }

    /**
     * @param t     更新的数据对象
     * @param value 数据库中的Map<columnName,value>
     * @return 是否更新成功
     */
    public boolean updateBy(T t, Map<String, Object> value) throws SQLException, NoSuchFieldException, IllegalAccessException {
        T t1 = queryForFirst(value);
        if (t1 == null) {
            throw new SQLException("no find this data in database:" + t);
        }
        setObjectValueIfNotNull(t1, t);
        return ormLiteDao.update(t1) > 0;
    }

    /**
     * 批量修改
     *
     * @param list
     * @return
     */
    public boolean updateForBatch(List<T> list) throws SQLException {
        return doBatchInTransaction(list, DaoOperation.UPDATE);
    }

    /**
     * 如果更新记录字段值为null则忽略不更新
     *
     * @param t
     * @return
     */
    private int updateIfValueNotNull(T t) throws SQLException, IllegalAccessException {
        UpdateBuilder updateBuilder = ormLiteDao.updateBuilder();
        Map<String, Object> map = getFieldsIfValueNotNull(t);
        if (map.isEmpty()) {
            throw new SQLException("all field value is null.");
        }
        if (map.get("id") == null) {
            throw new SQLException("id is null.");
        }
        updateBuilder.where().idEq(map.get("id"));
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equals("id")) {
                continue;
            }
            updateBuilder.updateColumnValue(entry.getKey(), entry.getValue());
        }
        return updateBuilder.update();
    }

    /**
     * 过滤数据实体中DatabaseField注解的字段值为null的字段
     *
     * @param t   过滤后不包含null字段的数据
     * @param obj 被过滤的数据
     */
    private void setObjectValueIfNotNull(T t, Object obj) throws IllegalAccessException, NoSuchFieldException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Class<?> cls = t.getClass();
        for (Field field : fields) {
            field.setAccessible(true);

            if (field.getAnnotation(DatabaseField.class) == null || "id".equals(field.getName())) {
                continue;
            }

            Object valueObj = field.get(obj);
            if (valueObj != null) {
                Field f = cls.getDeclaredField(field.getName());
                if (f != null) {
                    f.setAccessible(true);
                    f.set(t, valueObj);
                } else {
                    throw new IllegalAccessException("no this field:" + field.getName());
                }
            }
        }
    }

    /**
     * 获取对象DatabaseField注解的属性值不为空的属性名称和属性值
     *
     * @param obj 数据实体对象
     * @return 属性名称和属性值键值对集合
     */
    private Map<String, Object> getFieldsIfValueNotNull(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            if (field.getAnnotation(DatabaseField.class) == null) {
                continue;
            }

            Object valueObj = field.get(obj);

            if (valueObj != null) {
                map.put(field.getName(), valueObj);
            }
        }
        return map;
    }

}
