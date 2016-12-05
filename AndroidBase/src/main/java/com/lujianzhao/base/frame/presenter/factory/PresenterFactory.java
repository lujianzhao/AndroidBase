package com.lujianzhao.base.frame.presenter.factory;


import com.lujianzhao.base.frame.presenter.BasePresenter;

public interface PresenterFactory<P extends BasePresenter> {
    P createPresenter();
}
