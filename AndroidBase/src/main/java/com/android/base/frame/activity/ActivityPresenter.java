package com.android.base.frame.activity;

import android.app.Activity;
import android.content.Intent;

import com.android.base.common.rx.RxManager;
import com.android.base.frame.activity.impl.ActivityPresenterImpl;

/**
 * Created by Administrator on 2016/5/13.
 */
public abstract class ActivityPresenter<V, M> implements ActivityPresenterImpl {

    public V mView;
    public M mModel;
    public Activity mActivity;
    public RxManager mRxManager = new RxManager();

    public abstract void start();

    public void setVM(Activity activity, V view, M model) {
        this.mActivity = activity;
        this.mView = view;
        this.mModel = model;
    }

    @Override
    public void onCreate() {

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
        mActivity = null;
        mView = null;
        mModel = null;
    }




}
