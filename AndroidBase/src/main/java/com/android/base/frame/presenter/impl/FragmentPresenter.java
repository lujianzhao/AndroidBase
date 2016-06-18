package com.android.base.frame.presenter.impl;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.android.base.common.rx.RxManager;
import com.android.base.frame.fragment.impl.BaseMvpFragment;
import com.android.base.frame.model.IBaseModel;
import com.android.base.frame.view.IBaseView;
import com.android.base.frame.presenter.IFragmentPresenter;

/**
 * Created by Administrator on 2016/5/17.
 */
public abstract class FragmentPresenter<V extends IBaseView, M extends IBaseModel> implements IFragmentPresenter {

    public V mView;
    public M mModel;
    public Activity mActivity;
    public BaseMvpFragment mFragment;
    public RxManager mRxManager = new RxManager();

    @NonNull
    protected abstract Class<M> getModelClass();

    public abstract void start();


    public void setVM(@NonNull BaseMvpFragment fragment,@NonNull V view) {
        this.mActivity = fragment.getActivity();
        this.mFragment = fragment;
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
        if (mRxManager != null) {
            mRxManager.clear();
            mRxManager = null;
        }
        if (mModel != null) {
            mModel.onDestroy();
            mModel = null;
        }
        mView = null;
        mFragment = null;
        mActivity = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }


}
