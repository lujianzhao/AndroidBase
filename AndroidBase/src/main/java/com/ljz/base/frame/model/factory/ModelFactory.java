package com.ljz.base.frame.model.factory;


import com.ljz.base.frame.model.BaseModel;

public interface ModelFactory<P extends BaseModel> {
    P createModel();
}
