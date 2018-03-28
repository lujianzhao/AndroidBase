package com.ljz.androidbasedemo.db.presenter;

import android.view.View;

import com.ljz.androidbasedemo.R;
import com.ljz.androidbasedemo.db.contract.DBContract;
import com.ljz.androidbasedemo.db.model.DBModel;
import com.ljz.androidbasedemo.db.model.domains.City;
import com.ljz.base.frame.model.factory.RequiresModel;

import java.util.List;
import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/18 14:50
 * 描述:
 */
@RequiresModel(DBModel.class)
public class DBPresenter extends DBContract.Presenter {


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
        getModel().clearTableDataSync().subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
                getRxManager().add(d);
            }

            @Override
            public void onNext(Boolean data) {
                getView().clearView();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }

        });
    }

    public void query() {
        getModel().queryForAllSync().subscribe(new Observer<List<City>>() {
            @Override
            public void onSubscribe(Disposable d) {
                getRxManager().add(d);
            }

            @Override
            public void onNext(List<City> data) {
                queryFinish(data);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

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
        getView().updateView(sb.toString());
    }


    public void insert() {
        String cityUuid = getUUID();
        City city = new City();
        city.setProvinceName("广东省");
        city.setCityName("东莞市");
        city.setCityNo(cityUuid);

        getModel().insertSync(city).subscribe(new Observer<Boolean>() {

            @Override
            public void onSubscribe(Disposable d) {
                getRxManager().add(d);
            }

            @Override
            public void onNext(Boolean data) {
                //                Toastor.showToast(mActivity, "插入完成");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }

        });
    }

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
    }
}
