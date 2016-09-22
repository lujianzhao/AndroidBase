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
    private PresenterLifecycleDelegate<P> presenterDelegate = new PresenterLifecycleDelegate<>(ReflectionPresenterFactory.<P>fromViewClass(getClass()));

    public PresenterFactory<P> getPresenterFactory() {
        return presenterDelegate.getPresenterFactory();
    }

    @Override
    public void setPresenterFactory(PresenterFactory<P> presenterFactory) {
        presenterDelegate.setPresenterFactory(presenterFactory);
    }

    public P getPresenter() {
        return presenterDelegate.getPresenter();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mSavedInstanceState = savedInstanceState;
            presenterDelegate.onRestoreInstanceState(mSavedInstanceState.getBundle(PRESENTER_STATE_KEY));
        }

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBundle(PRESENTER_STATE_KEY, presenterDelegate.onSaveInstanceState());
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

    private void initLazyView(Bundle savedInstanceState) {

        presenterDelegate.onCreate(this, getActivity());

        initView(savedInstanceState);

        initData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (presenterDelegate != null) {
            presenterDelegate.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        if (presenterDelegate != null) {
            return presenterDelegate.onBackPressed();
        }
        return super.onBackPressedSupport();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterDelegate.onDestroy(!getActivity().isChangingConfigurations());
    }


}
