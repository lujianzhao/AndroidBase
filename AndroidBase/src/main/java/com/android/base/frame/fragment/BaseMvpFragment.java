package com.android.base.frame.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.base.frame.BaseModelImpl;

/**
 * Created by Administrator on 2016/5/13.
 */
public abstract class BaseMvpFragment<P extends FragmentPresenter, M extends BaseModelImpl> extends SuperFragment{

    public P mPresenter;
    public M mModel;

    protected abstract Class<P> getPresenterClass();

    protected abstract Class<M> getModelClass();

    protected abstract void initPresenter(Bundle savedInstanceState);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = getPresenter();
        mModel = getModel();
        initPresenter(savedInstanceState);
        initView(savedInstanceState);
        if(mPresenter !=null){
            mPresenter.onActivityCreated();
            mPresenter.start();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mPresenter !=null){
            mPresenter.onStart();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mPresenter !=null){
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mPresenter !=null){
            mPresenter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mPresenter !=null){
            mPresenter.onPause();
        }
    }

    @Override
    public void onStop() {
        if(mPresenter !=null){
            mPresenter.onStop();
        }
        super.onStop();
    }


    @Override
    public void onDestroy() {
        if(mPresenter !=null){
            mPresenter.onDestroy();
        }
        super.onDestroy();
    }


    public P getPresenter(){
        try {
            return getPresenterClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("create IDelegate error");
        }
    }

    public M getModel(){
        try {
            return getModelClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("create IDelegate error");
        }
    }




}
