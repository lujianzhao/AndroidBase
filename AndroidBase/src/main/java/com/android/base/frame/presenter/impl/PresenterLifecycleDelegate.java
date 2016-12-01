package com.android.base.frame.presenter.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.base.common.utils.ParcelFn;
import com.android.base.frame.presenter.BasePresenter;
import com.android.base.frame.presenter.IPresenter;
import com.android.base.frame.presenter.factory.PresenterFactory;
import com.android.base.frame.presenter.factory.PresenterStorage;
import com.android.base.frame.view.IBaseView;
import com.android.base.netstate.NetWorkUtil;

/**
 * Created by Administrator on 2016/9/19.
 */
public class PresenterLifecycleDelegate<P extends BasePresenter> implements IPresenter {

    private static final String PRESENTER_KEY = "presenter";
    private static final String PRESENTER_ID_KEY = "presenter_id";

    private PresenterFactory<P> mPresenterFactory;
    private P mPresenter;
    private Bundle mBundle;

    private boolean mPresenterHasView;

    public PresenterLifecycleDelegate(PresenterFactory<P> presenterFactory) {
        this.mPresenterFactory = presenterFactory;
    }

    public PresenterFactory<P> getPresenterFactory() {
        return mPresenterFactory;
    }

    public void setPresenterFactory(PresenterFactory<P> presenterFactory) {
        if (mPresenter != null)
            throw new IllegalArgumentException("setPresenterFactory() should be called before onResume()");
        this.mPresenterFactory = presenterFactory;
    }

    public P getPresenter() {
        if (mPresenterFactory != null) {
            if (mPresenter == null && mBundle != null) {
                mPresenter = PresenterStorage.INSTANCE.getPresenter(mBundle.getString(PRESENTER_ID_KEY));
            }

            if (mPresenter == null) {
                mPresenter = mPresenterFactory.createPresenter();
                PresenterStorage.INSTANCE.add(mPresenter);
                mPresenter.create(mBundle == null ? null : mBundle.getBundle(PRESENTER_KEY));
            }
            mBundle = null;
        }
        return mPresenter;
    }

    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        getPresenter();
        if (mPresenter != null) {
            Bundle presenterBundle = new Bundle();
            mPresenter.save(presenterBundle);

            bundle.putBundle(PRESENTER_KEY, presenterBundle);
            bundle.putString(PRESENTER_ID_KEY, PresenterStorage.INSTANCE.getId(mPresenter));
        }
        return bundle;
    }

    public void onRestoreInstanceState(Bundle presenterState) {
        if (mPresenter != null)
            throw new IllegalArgumentException("onRestoreInstanceState() should be called before onResume()");
        this.mBundle = ParcelFn.unmarshall(ParcelFn.marshall(presenterState));
    }

    @Override
    public void onCreate(Object view, Context context) {
        getPresenter();
        if(mPresenter!=null){
            mPresenter.takeView((IBaseView) view,context);
            mPresenterHasView = true;
        }
    }

    /**
     * {@link android.app.Activity#onDestroy()},
     * {@link android.app.Fragment#onDestroy()},
     * {@link android.view.View#onDetachedFromWindow()}
     */
    public void onDestroy(boolean isFinal) {
        if (mPresenter != null) {
            if (mPresenterHasView) {
                mPresenter.dropView();
                mPresenterHasView = false;
            }

            if (isFinal) {
                mPresenter.destroy();
                mPresenter = null;
            }
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mPresenter!=null){
            mPresenter.onActivityResult( requestCode,  resultCode,  data);
        }
    }

    @Override
    public boolean onBackPressed() {
        if(mPresenter!=null){
            return  mPresenter.onBackPressed();
        }
        return false;
    }

    @Override
    public void onResume() {
        if(mPresenter!=null){
            mPresenter.onResume();
        }
    }

    @Override
    public void onPause() {
        if(mPresenter!=null){
            mPresenter.onPause();
        }
    }


    /**
     * 网络断开
     */
    public void onNetWorkDisConnect(){
        if (mPresenter != null) {
            mPresenter.onNetWorkDisConnect();
        }
    }

    /**
     * 网络连接上
     *
     * @param type 当前的网络状态:
     *             UnKnown(-1),没有网络
     *             Wifi(1),WIFI网络
     *             Net2G(2),2G网络
     *             Net3G(3),3G网络
     *             Net4G(4);4G网络
     */
    public void onNetWorkConnect(NetWorkUtil.NetWorkType type){
        if (mPresenter != null) {
            mPresenter.onNetWorkConnect(type);
        }
    }
}