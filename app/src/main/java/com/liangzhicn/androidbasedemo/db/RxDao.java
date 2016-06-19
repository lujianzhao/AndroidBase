package com.liangzhicn.androidbasedemo.db;

import android.content.Context;

import com.android.base.db.BaseRxDao;
import com.j256.ormlite.dao.Dao;
import com.liangzhicn.androidbasedemo.DatabaseHelper;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/19 11:59
 * 描述:
 */
public class RxDao<T> extends BaseRxDao<T> {

    public RxDao(Context context, Class<T> cls) {
        super(context, cls);
    }

    public RxDao(Context context, Class<T> cls, boolean cache) {
        super(context, cls, cache);
    }

    @Override
    public Dao<T, Integer> getOrmLiteDao(Context context, Class<T> cls) {
        return DatabaseHelper.getInstance(context).getDao(cls);
    }
}
