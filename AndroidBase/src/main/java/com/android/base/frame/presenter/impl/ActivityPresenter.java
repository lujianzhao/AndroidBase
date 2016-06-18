package com.android.base.frame.presenter.impl;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.android.base.common.rx.RxManager;
import com.android.base.frame.model.IBaseModel;
import com.android.base.frame.view.IBaseView;
import com.android.base.frame.presenter.IActivityPresenter;

/**
 * Created by Administrator on 2016/5/13.
 */
public abstract class ActivityPresenter<V extends IBaseView, M extends IBaseModel> implements IActivityPresenter {

    public V mView;
    public M mModel;
    public Activity mActivity;
    public RxManager mRxManager = new RxManager();

    public abstract void start();

    @NonNull
    protected abstract Class<M> getModelClass();


    public void setVM(@NonNull Activity activity,@NonNull V view) {
        this.mActivity = activity;
        this.mView = view;
        this.mModel = getModel();
        mModel.setRxManager(mRxManager);
    }

    public M getModel() {
        try {
            return getModelClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("create IDelegate error");
        }
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
        if (mRxManager != null) {
            mRxManager.clear();
            mRxManager = null;
        }
        if (mModel != null) {
            mModel.onDestroy();
            mModel = null;
        }
        mView = null;
        mActivity = null;
    }






}
