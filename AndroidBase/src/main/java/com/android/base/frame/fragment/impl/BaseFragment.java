package com.android.base.frame.fragment.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.base.common.rx.RxManager;

/**
 * Created by Administrator on 2016/5/13.
 */
public abstract class BaseFragment extends SuperFragment {

    private final RxManager mRxManager = new RxManager();

    private boolean mInited = false;

    private Bundle mSavedInstanceState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mSavedInstanceState = savedInstanceState;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            if (!mInited &&!isHidden()) {
                mInited = true;
                initLazyView(null);
            }
        } else {
            // isSupportHidden()仅在saveIns tanceState!=null时有意义,是库帮助记录Fragment状态的方法
            if (!mInited &&!isSupportHidden()) {
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

        onInitView(savedInstanceState);

        onInitData();
    }



    @Override
    protected void onInitView(Bundle savedInstanceState) {
    }

    @Override
    protected void onInitData() {
    }

    protected RxManager getRxManager() {
        return mRxManager;
    }

    @Override
    public void onDestroy() {
        mRxManager.clear();
        super.onDestroy();
    }
}
