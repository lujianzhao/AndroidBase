package com.lujianzhao.base.frame.model.impl;

import com.lujianzhao.base.frame.model.BaseModel;
import com.lujianzhao.base.frame.model.IModel;
import com.lujianzhao.base.frame.model.factory.ModelFactory;

/**
 * Created by Administrator on 2016/9/19.
 */
public class ModelLifecycleDelegate<M extends BaseModel> implements IModel {
    private ModelFactory<M> mModelFactory;
    private M mModel;

    public ModelLifecycleDelegate(ModelFactory<M> modelFactory) {
        this.mModelFactory = modelFactory;
    }

    public ModelFactory<M> getModelFactory() {
        return mModelFactory;
    }

    public void setModelFactory(ModelFactory<M> mModelFactory) {
        if (mModel != null)
            throw new IllegalArgumentException("setmModelFactory() should be called before onCreate()");
        this.mModelFactory = mModelFactory;
    }

    public M getModel() {
        if (mModelFactory != null) {
            if (this.mModel == null) {
                this.mModel = mModelFactory.createModel();
                this.mModel.create();
            }
        }
        return mModel;
    }

    @Override
    public void onDestroy() {
        if (mModel != null) {
            mModel.destroy();
            mModel = null;
        }
        mModelFactory = null;
    }


}