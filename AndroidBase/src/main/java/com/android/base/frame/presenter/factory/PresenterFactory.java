package com.android.base.frame.presenter.factory;


import com.android.base.frame.presenter.BasePresenter;

public interface PresenterFactory<P extends BasePresenter> {
    P createPresenter();
}
