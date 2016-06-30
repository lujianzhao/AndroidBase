package com.android.base.frame.presenter.impl;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.android.base.common.rx.RxManager;
import com.android.base.frame.fragment.impl.BaseMvpFragment;
import com.android.base.frame.model.impl.BaseModel;
import com.android.base.frame.presenter.IFragmentPresenter;
import com.android.base.frame.view.IBaseView;

/**
 * Created by Administrator on 2016/5/17.
 */
public abstract class FragmentPresenter<M extends BaseModel, V extends IBaseView> implements IFragmentPresenter {

    public V mView;
    public M mModel;
    public Activity mActivity;
    public BaseMvpFragment mFragment;
    public RxManager mRxManager = new RxManager();

    /**
     * 此方法在对象初始化的时候调用一次,其他时间段请直接使用 mModel
     */
    @NonNull
    protected abstract M getMvpModel();

    public abstract void start();


    public void initPresenter(@NonNull BaseMvpFragment fragment, @NonNull V view) {
        this.mActivity = fragment.getActivity();
        this.mFragment = fragment;
        this.mView = view;
        this.mModel = getMvpModel();
        mModel.initModel(mActivity);
    }

    @Override
    public void onActivityCreated() {
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
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroy() {
        if (mModel != null) {
            mModel.onDestroy();
            mModel = null;
        }

        if (mRxManager != null) {
            mRxManager.clear();
            mRxManager = null;
        }

        mView = null;
        mFragment = null;
        mActivity = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }


}
