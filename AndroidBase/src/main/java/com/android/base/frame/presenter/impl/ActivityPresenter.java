package com.android.base.frame.presenter.impl;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.android.base.common.rx.RxManager;
import com.android.base.frame.model.impl.BaseModel;
import com.android.base.frame.presenter.IActivityPresenter;
import com.android.base.frame.view.IBaseView;

/**
 * Created by Administrator on 2016/5/13.
 */
public abstract class ActivityPresenter<M extends BaseModel, V extends IBaseView> implements IActivityPresenter {

    public V mView;
    public M mModel;
    public Activity mActivity;
    public RxManager mRxManager = new RxManager();

    /**
     * 此方法在对象初始化的时候调用一次,其他时间段请直接使用 mModel
     */
    @NonNull
    protected abstract M getMvpModel();

    public abstract void start();


    public void initPresenter(@NonNull Activity activity, @NonNull V view) {
        this.mActivity = activity;
        this.mView = view;
        this.mModel = getMvpModel();
        mModel.initModel(mActivity, mRxManager);
    }

    @Override
    public void onCreate() {
        mModel.onCreate();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroy() {
        if (mModel != null) {
            mModel.onDestroy();
            mModel = null;
        }
        mView = null;
        mActivity = null;
        if (mRxManager != null) {
            mRxManager.clear();
            mRxManager = null;
        }
    }

}
