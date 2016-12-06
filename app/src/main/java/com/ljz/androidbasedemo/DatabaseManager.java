package com.ljz.androidbasedemo;

import android.content.Context;

import com.ljz.base.db.DatabaseManagerWrapper;
import com.ljz.androidbasedemo.db.model.domains.City;


/**
 * 数据库初始化管理
 * <p/>
 * Created by hzj on 2016/10/9.
 */
public class DatabaseManager extends DatabaseManagerWrapper {

    /**
     * 数据库名称
     */
    private static final String DATABASE_NAME = "setting.db";

    /**
     * 数据库版本号
     */
    private static final int DATABASE_VERSION = 1;


    private static volatile DatabaseManager instance;

    private DatabaseManager() {
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化数据库
     */
    public void init(Context context) {
        super.init(context, DATABASE_NAME, DATABASE_VERSION);
    }


    @Override
    protected void registerTable() {
        addTable(City.class);
    }
}
