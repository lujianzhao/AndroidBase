package com.lujianzhao.base.frame.model.factory;


import com.lujianzhao.base.frame.model.BaseModel;

public interface ModelFactory<P extends BaseModel> {
    P createModel();
}
