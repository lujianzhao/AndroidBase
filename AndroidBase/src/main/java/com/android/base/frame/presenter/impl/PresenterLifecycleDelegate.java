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

    private PresenterFactory<P> presenterFactory;
    private P presenter;
    private Bundle bundle;

    private boolean presenterHasView;

    public PresenterLifecycleDelegate(PresenterFactory<P> presenterFactory) {
        this.presenterFactory = presenterFactory;
    }

    public PresenterFactory<P> getPresenterFactory() {
        return presenterFactory;
    }

    public void setPresenterFactory(PresenterFactory<P> presenterFactory) {
        if (presenter != null)
            throw new IllegalArgumentException("setPresenterFactory() should be called before onResume()");
        this.presenterFactory = presenterFactory;
    }

    public P getPresenter() {
        if (presenterFactory != null) {
            if (presenter == null && bundle != null) {
                presenter = PresenterStorage.INSTANCE.getPresenter(bundle.getString(PRESENTER_ID_KEY));
            }

            if (presenter == null) {
                presenter = presenterFactory.createPresenter();
                PresenterStorage.INSTANCE.add(presenter);
                presenter.create(bundle == null ? null : bundle.getBundle(PRESENTER_KEY));
            }
            bundle = null;
        }
        return presenter;
    }

    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        getPresenter();
        if (presenter != null) {
            Bundle presenterBundle = new Bundle();
            presenter.save(presenterBundle);

            bundle.putBundle(PRESENTER_KEY, presenterBundle);
            bundle.putString(PRESENTER_ID_KEY, PresenterStorage.INSTANCE.getId(presenter));
        }
        return bundle;
    }

    public void onRestoreInstanceState(Bundle presenterState) {
        if (presenter != null)
            throw new IllegalArgumentException("onRestoreInstanceState() should be called before onResume()");
        this.bundle = ParcelFn.unmarshall(ParcelFn.marshall(presenterState));
    }

    @Override
    public void onCreate(Object view, Context context) {
        getPresenter();
        if(presenter!=null){
            presenter.takeView((IBaseView) view,context);
            presenterHasView = true;
        }
    }

    /**
     * {@link android.app.Activity#onDestroy()},
     * {@link android.app.Fragment#onDestroy()},
     * {@link android.view.View#onDetachedFromWindow()}
     */
    public void onDestroy(boolean isFinal) {
        if (presenter != null) {
            if (presenterHasView) {
                presenter.dropView();
                presenterHasView = false;
            }

            if (isFinal) {
                presenter.destroy();
                presenter = null;
            }
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(presenter!=null){
            presenter.onActivityResult( requestCode,  resultCode,  data);
        }
    }

    @Override
    public boolean onBackPressed() {
        if(presenter!=null){
            return  presenter.onBackPressed();
        }
        return false;
    }


    /**
     * 网络断开
     */
    public void onNetWorkDisConnect(){
        if (presenter != null) {
            presenter.onNetWorkDisConnect();
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
        if (presenter != null) {
            presenter.onNetWorkConnect(type);
        }
    }
}