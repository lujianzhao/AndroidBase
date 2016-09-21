package com.liangzhicn.androidbasedemo;

import com.android.base.db.OrmLiteDatabaseHelper;
import com.android.base.frame.BaseApplication;
import com.liangzhicn.androidbasedemo.db.DatabaseHelper;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/20 15:32
 * 描述:
 */
public class DemoApplication extends BaseApplication {

    @Override
    public OrmLiteDatabaseHelper getOrmLiteDatabaseHelper() {
        return DatabaseHelper.getInstance(this);
    }
}
