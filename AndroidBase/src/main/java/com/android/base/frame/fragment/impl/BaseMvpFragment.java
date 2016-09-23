package com.android.base.frame.fragment.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.base.frame.view.ViewWithPresenter;
import com.android.base.frame.presenter.BasePresenter;
import com.android.base.frame.presenter.impl.PresenterLifecycleDelegate;
import com.android.base.frame.presenter.factory.PresenterFactory;
import com.android.base.frame.presenter.factory.ReflectionPresenterFactory;

/**
 * Created by Administrator on 2016/5/13.
 */
public abstract class BaseMvpFragment<P extends BasePresenter> extends SuperFragment implements ViewWithPresenter<P> {

    private boolean mInited = false;

    private Bundle mSavedInstanceState;

    private static final String PRESENTER_STATE_KEY = "presenter_state";
    private PresenterLifecycleDelegate<P> mPresenterDelegate = new PresenterLifecycleDelegate<>(ReflectionPresenterFactory.<P>fromViewClass(getClass()));

    public PresenterFactory<P> getPresenterFactory() {
        return mPresenterDelegate.getPresenterFactory();
    }

    @Override
    public void setPresenterFactory(PresenterFactory<P> presenterFactory) {
        mPresenterDelegate.setPresenterFactory(presenterFactory);
    }

    public P getPresenter() {
        return mPresenterDelegate.getPresenter();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mSavedInstanceState = savedInstanceState;
            mPresenterDelegate.onRestoreInstanceState(mSavedInstanceState.getBundle(PRESENTER_STATE_KEY));
        }

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBundle(PRESENTER_STATE_KEY, mPresenterDelegate.onSaveInstanceState());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            if (!isHidden()) {
                mInited = true;
                initLazyView(null);
            }
        } else {
            // isSupportHidden()仅在saveIns tanceState!=null时有意义,是库帮助记录Fragment状态的方法
            if (!isSupportHidden()) {
                mInited = true;
                initLazyView(savedInstanceState);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!mInited && !hidden) {
            mInited = true;
            initLazyView(mSavedInstanceState);
        }
    }

    @Override
    protected void initData() {
        getPresenter().start();
    }

    private void initLazyView(Bundle savedInstanceState) {

        mPresenterDelegate.onCreate(this, getActivity());

        initView(savedInstanceState);

        initData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPresenterDelegate != null) {
            mPresenterDelegate.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        if (mPresenterDelegate != null) {
            return mPresenterDelegate.onBackPressed();
        }
        return super.onBackPressedSupport();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenterDelegate.onDestroy(!getActivity().isChangingConfigurations());
    }


}
