package com.android.base.frame.fragment;

import android.app.Activity;
import android.content.Intent;

import com.android.base.common.rx.RxManager;
import com.android.base.frame.fragment.impl.FragmentPresenterImpl;

/**
 * Created by Administrator on 2016/5/17.
 */
public abstract class FragmentPresenter<V, M> implements FragmentPresenterImpl {

    public V mView;
    public M mModel;
    public Activity mActivity;
    public SuperFragment mFragment;
    public RxManager mRxManager = new RxManager();

    public abstract void start();

    public void setVM(SuperFragment fragment,V view, M model) {
        this.mFragment = fragment;
        this.mActivity = fragment.getActivity();
        this.mView = view;
        this.mModel = model;
    }

    @Override
    public void onActivityCreated() {

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
    public void onDestroy() {
        if (mRxManager != null) {
            mRxManager.clear();
            mRxManager = null;
        }
        mFragment = null;
        mActivity = null;
        mView = null;
        mModel = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }



}
