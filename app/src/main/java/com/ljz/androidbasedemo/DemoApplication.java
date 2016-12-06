package com.ljz.androidbasedemo;

import com.ljz.base.frame.BaseApplication;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/20 15:32
 * 描述:
 */
public class DemoApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseManager.getInstance().init(this);
    }
}
