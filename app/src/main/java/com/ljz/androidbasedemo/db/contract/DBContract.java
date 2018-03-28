package com.ljz.androidbasedemo.db.contract;

import com.ljz.androidbasedemo.db.model.domains.City;
import com.ljz.base.frame.model.BaseModel;
import com.ljz.base.frame.presenter.BasePresenter;
import com.ljz.base.frame.view.IBaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2016/4/27.
 */
public interface DBContract {

    abstract class Model extends BaseModel {
        /**
         * 插入一条数据
         * @param city 需要插入的数据
         */
        public abstract Observable<Boolean> insertSync(City city);

        /**
         * 查询所有数据
         */
        public abstract Observable<List<City>> queryForAllSync();

        /**
         * 删除数据
         */
        public abstract Observable<Boolean> clearTableDataSync();
    }

    interface View extends IBaseView {

        void clearView();

        /**
         * 刷新界面
         * @param s
         */
        void updateView(String s);
    }

    abstract class Presenter extends BasePresenter<Model,View> {

        public abstract void onBtnClick(android.view.View view);
    }

}
