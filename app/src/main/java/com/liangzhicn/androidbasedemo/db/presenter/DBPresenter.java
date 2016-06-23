package com.liangzhicn.androidbasedemo.db.presenter;

import android.support.annotation.NonNull;
import android.view.View;

import com.android.base.common.assist.Toastor;
import com.android.base.callback.RequestDataCallBack;
import com.liangzhicn.androidbasedemo.R;
import com.liangzhicn.androidbasedemo.db.contract.DBContract;
import com.liangzhicn.androidbasedemo.db.model.DBModel;
import com.liangzhicn.androidbasedemo.db.model.domains.City;

import java.util.List;
import java.util.UUID;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:50
 * 描述:
 */
public class DBPresenter extends DBContract.Presenter<DBContract.Model> {

    @Override
    public void start() {

    }

    @NonNull
    @Override
    protected DBContract.Model getMvpModel() {
        return new  DBModel();
    }


    @Override
    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.insert:
                insert();
                break;
            case R.id.query:
                query();
                break;
            case R.id.clear:
                clear();
                break;
            default:
                break;
        }
    }

    public void clear() {
        mModel.clearTableDataSync(new RequestDataCallBack<Boolean>() {
                                      @Override
                                      public void onNext(Boolean data) {
                                          mView.clearView();
                                      }
                                  }

        );
    }

    public void query() {
        mModel.queryForAllSync(new RequestDataCallBack<List<City>>() {
            @Override
            public void onNext(List<City> data) {
                queryFinish(data);
            }
        });

    }

    private void queryFinish(List<City> list) {
        StringBuilder sb = new StringBuilder("查询结果：\n");
        if (list == null || list.size() <= 0) {
            sb.append("空");
        } else {
            sb.append("查询到的总条数").append(list.size()).append("\n\n");
            sb.append("第一条记录为：\n");
            sb.append(list.get(0).toString()).append("\n\n");
            sb.append("最后一条记录为：\n");
            sb.append(list.get(list.size() - 1).toString()).append("\n\n");
        }
        mView.updateView(sb.toString());
    }


    public void insert() {
        String cityUuid = getUUID();
        City city = new City();
        city.setProvinceName("广东省");
        city.setCityName("东莞市");
        city.setCityNo(cityUuid);

        mModel.insertSync(city, new RequestDataCallBack<Boolean>() {

            @Override
            public void onNext(Boolean data) {
                Toastor.showToast(mActivity, "插入完成");
            }
        });
    }

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
    }
}
