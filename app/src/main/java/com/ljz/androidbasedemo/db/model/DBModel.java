package com.ljz.androidbasedemo.db.model;

import com.ljz.androidbasedemo.db.contract.DBContract;
import com.ljz.androidbasedemo.db.model.domains.City;
import com.ljz.base.db.BaseRxDao;

import java.util.List;

import io.reactivex.Observable;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:51
 * 描述:
 */
public class DBModel extends DBContract.Model {

    private BaseRxDao<City> mCityDao;

    @Override
    protected void onCreate() {
        mCityDao = new BaseRxDao<>(City.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCityDao = null;
    }

    @Override
    public Observable<Boolean> insertSync(City city) {
       return mCityDao.insertWithRx(city);
    }

    @Override
    public Observable<List<City>> queryForAllSync() {
        return mCityDao.queryForAllWithRx();
    }

    @Override
    public Observable<Boolean> clearTableDataSync() {
        return mCityDao.clearTableDataWithRx();
    }
}
