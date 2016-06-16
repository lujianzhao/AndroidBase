package com.android.base.frame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;

import com.android.base.frame.BaseModelImpl;

/**
 * Created by Administrator on 2016/5/13.
 */
public abstract class BaseMvpActivity<P extends ActivityPresenter, M extends BaseModelImpl> extends SuperActivity{
    public P mPresenter;
    public M mModel;


    protected abstract void initPresenter();

    protected abstract Class<P> getPresenterClass();

    protected abstract Class<M> getModelClass();


    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
        mModel = getModel();
        initPresenter();
        initView();
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
            mPresenter.onActivityResult(requestCode,resultCode,data);
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
        }
        super.onDestroy();
    }

    public P getPresenter() {
        try {
            return getPresenterClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("create IDelegate error");
        }
    }

    public M getModel() {
        try {
            return getModelClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("create IDelegate error");
        }
    }


}
