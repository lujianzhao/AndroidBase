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
public abstract class ActivityPresenter<V extends IBaseView, M extends BaseModel> implements IActivityPresenter {

    public V mView;
    public M mModel;
    public Activity mActivity;
    public RxManager mRxManager = new RxManager();

    @NonNull
    protected abstract Class<M> getModelClass();

    public abstract void start();


    public void setVM(@NonNull Activity activity,@NonNull V view) {
        this.mActivity = activity;
        this.mView = view;
        this.mModel = getModel();
        mModel.init(mActivity,mRxManager);
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
