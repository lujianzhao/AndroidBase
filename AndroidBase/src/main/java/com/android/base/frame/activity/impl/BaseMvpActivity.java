package com.android.base.frame.activity.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.android.base.frame.presenter.impl.ActivityPresenter;
import com.android.base.frame.view.IBaseView;

/**
 * Created by Administrator on 2016/5/13.
 */
public abstract class BaseMvpActivity<P extends ActivityPresenter> extends SuperActivity {
    public P mPresenter;

    /**
     * 此方法在对象初始化的时候调用一次,其他时间段请直接使用 mPresenter
     */
    @NonNull
    protected abstract P getMvpPresenter();

    @NonNull
    protected abstract IBaseView getMvpView();


    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getMvpPresenter();
        mPresenter.initPresenter(this, getMvpView());
        initView(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.onCreate();
            mPresenter.start();
        }
    }


    @Override
    public void onBackPressedSupport() {
        if (mPresenter != null) {
            if (!mPresenter.onBackPressed()) {
                super.onBackPressedSupport();
            }
        } else {
            super.onBackPressedSupport();
        }

    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    @CallSuper
    protected void onResume() {
        if (mPresenter != null) {
            mPresenter.onResume();
        }
        super.onResume();
    }

    @Override
    @CallSuper
    protected void onPause() {
        if (mPresenter != null) {
            mPresenter.onPause();
        }
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        if (mPresenter != null) {
            mPresenter.onStop();
        }
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
        super.onDestroy();
    }

//    public P getPresenter() {
//        try {
//            return getPresenterClass().newInstance();
//        } catch (Exception e) {
//            throw new RuntimeException("create IDelegate error");
//        }
//    }

}
