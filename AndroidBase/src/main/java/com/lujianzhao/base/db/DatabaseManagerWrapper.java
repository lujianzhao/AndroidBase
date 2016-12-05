package com.lujianzhao.base.db;

import android.content.Context;

import com.lujianzhao.base.db.ormlite.DatabaseHelper;

/**
 * 数据库初始化Manager
 * 在Application中调用{@link DatabaseManagerWrapper#init(Context, String, int)}方法，完成数据库初始化
 * 子类继承该类，调用{@link DatabaseManagerWrapper#init(Context, String, int)}方法，完成数据库的注册
 * 实现{@link DatabaseManagerWrapper#registerTable()}，调用{@link DatabaseManagerWrapper#addTable(Class)}完成数据库的注册
 * 注意：{@link DatabaseManagerWrapper#addTable(Class)}是新建一张表，当数据库中已含有该表时，请勿调用。
 */
public abstract class DatabaseManagerWrapper {

    private DatabaseHelper helper;

    /**
     * 初始化数据库
     *
     * @param context
     */
    public void init(Context context, String databaseName, int databaseVersion) {
        // 数据库名与数据库版本号必须首先设置
        setDbName(databaseName);
        setDbVersion(databaseVersion);

        initDatabaseHelper(context);
        registerTable();
    }

    private void initDatabaseHelper(Context context) {
        helper = DatabaseHelper.getInstance(context);
    }

    public DatabaseHelper getHelper(){
        return helper;
    }

    /**
     * 设置数据库名称
     *
     * @param databaseName 数据库名称
     */
    private void setDbName(String databaseName) {
        DatabaseHelper.setDbName(databaseName);
    }

    /**
     * 设置数据库版本号
     *
     * @param databaseVersion 数据库版本号
     */
    private void setDbVersion(int databaseVersion) {
        DatabaseHelper.setDbVersion(databaseVersion);
    }

    /**
     * 添加数据表
     */
    protected <T> void addTable(Class<T> clazz) {
        helper.registerTable(clazz);
    }

    /**
     * 注册数据表
     */
    protected abstract void registerTable();
}
