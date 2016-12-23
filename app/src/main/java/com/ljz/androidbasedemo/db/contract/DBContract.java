package com.ljz.androidbasedemo.db.contract;

import com.ljz.androidbasedemo.db.model.domains.City;
import com.ljz.base.callback.DBCallBack;
import com.ljz.base.frame.model.BaseModel;
import com.ljz.base.frame.presenter.BasePresenter;
import com.ljz.base.frame.view.IBaseView;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public interface DBContract {

    abstract class Model extends BaseModel {
        /**
         * 插入一条数据
         * @param city 需要插入的数据
         * @param dbCallBack 完成的回调
         */
        public abstract void insertSync(City city, DBCallBack<Boolean> dbCallBack);

        /**
         * 查询所有数据
         * @param dbCallBack
         */
        public abstract void queryForAllSync(DBCallBack<List<City>> dbCallBack);

        /**
         * 删除数据
         * @param dbCallBack
         */
        public abstract void clearTableDataSync(DBCallBack<Boolean> dbCallBack);
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
