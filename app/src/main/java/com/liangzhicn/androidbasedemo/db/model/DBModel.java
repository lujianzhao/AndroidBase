package com.liangzhicn.androidbasedemo.db.model;

import com.android.base.db.BaseRxDao;
import com.android.base.db.ormlite.DbCallBack;
import com.liangzhicn.androidbasedemo.db.RxDao;
import com.liangzhicn.androidbasedemo.db.contract.DBContract;
import com.liangzhicn.androidbasedemo.db.model.domains.City;

import java.util.List;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:51
 * 描述:
 */
public class DBModel extends DBContract.Model {

    private BaseRxDao mCityDao;

    @Override
    public void onCreate() {
        mCityDao = new RxDao<>(mContext, City.class);
    }


    @Override
    public void onResume() {
        mCityDao.subscribe();
    }

    @Override
    public void onPause() {
        mCityDao.unsubscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCityDao = null;
    }

    @Override
    public void insertSync(City city, DbCallBack<Boolean> dbCallBack) {
        mCityDao.insertSync(city, dbCallBack);
    }

    @Override
    public void queryForAllSync(DbCallBack<List<City>> dbCallBack) {
        mCityDao.queryForAllSync(dbCallBack);
    }

    @Override
    public void clearTableDataSync(DbCallBack<Boolean> dbCallBack) {
        mCityDao.clearTableDataSync(dbCallBack);
    }
}
