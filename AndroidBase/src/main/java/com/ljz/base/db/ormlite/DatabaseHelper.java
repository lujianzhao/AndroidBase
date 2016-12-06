package com.ljz.base.db.ormlite;

import android.content.Context;

import com.j256.ormlite.logger.LocalLog;

/**
 * 操作数据库Helper
 */
public class DatabaseHelper extends OrmLiteDatabaseHelper {

    /**
     * 数据库名称
     */
    public static String DATABASE_NAME = null;

    /**
     * 数据库版本号
     */
    public static int DATABASE_VERSION = -1;

    private static volatile DatabaseHelper instance;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "FATAL");
    }

    public static DatabaseHelper getInstance(Context context) {
        if (null == DATABASE_NAME || DATABASE_VERSION < 0) {
            throw new IllegalStateException("database name or database version not initialize");
        }
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    /**
     * 设置数据库名称
     *
     * @param databaseName 数据库名称
     */
    public static void setDbName(String databaseName) {
        DATABASE_NAME = databaseName;
    }

    /**
     * 设置数据库版本号
     *
     * @param databaseVersion 数据库版本号
     */
    public static void setDbVersion(int databaseVersion) {
        DATABASE_VERSION = databaseVersion;
    }
}
