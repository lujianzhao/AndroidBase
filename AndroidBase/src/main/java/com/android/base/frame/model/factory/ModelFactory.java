package com.android.base.frame.model.factory;


import com.android.base.frame.model.BaseModel;

public interface ModelFactory<P extends BaseModel> {
    P createModel();
}
