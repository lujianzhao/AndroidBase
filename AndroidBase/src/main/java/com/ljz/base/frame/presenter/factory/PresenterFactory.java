package com.ljz.base.frame.presenter.factory;


import com.ljz.base.frame.presenter.BasePresenter;

public interface PresenterFactory<P extends BasePresenter> {
    P createPresenter();
}
