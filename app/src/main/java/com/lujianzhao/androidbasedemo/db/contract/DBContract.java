package com.lujianzhao.androidbasedemo.db.contract;

import com.lujianzhao.androidbasedemo.db.model.domains.City;
import com.lujianzhao.base.callback.ExecutorCallBack;
import com.lujianzhao.base.frame.model.BaseModel;
import com.lujianzhao.base.frame.presenter.BasePresenter;
import com.lujianzhao.base.frame.view.IBaseView;

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
        public abstract void insertSync(City city, ExecutorCallBack<Boolean> dbCallBack);

        /**
         * 查询所有数据
         * @param dbCallBack
         */
        public abstract void queryForAllSync(ExecutorCallBack<List<City>> dbCallBack);

        /**
         * 删除数据
         * @param dbCallBack
         */
        public abstract void clearTableDataSync(ExecutorCallBack<Boolean> dbCallBack);
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
