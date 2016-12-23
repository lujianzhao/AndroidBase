package com.ljz.androidbasedemo.db.model;

import com.ljz.androidbasedemo.db.contract.DBContract;
import com.ljz.androidbasedemo.db.model.domains.City;
import com.ljz.base.callback.DBCallBack;
import com.ljz.base.db.BaseRxDao;

import java.util.List;

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
    public void insertSync(City city, DBCallBack<Boolean> dbCallBack) {
        getRxManager().add(mCityDao.insert(city, dbCallBack));
    }

    @Override
    public void queryForAllSync(DBCallBack<List<City>> dbCallBack) {
        getRxManager().add( mCityDao.queryForAll(dbCallBack));
    }

    @Override
    public void clearTableDataSync(DBCallBack<Boolean> dbCallBack) {
        getRxManager().add(mCityDao.clearTableData(dbCallBack));
    }
}
