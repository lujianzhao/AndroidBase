package com.android.base.frame.model.impl;

import com.android.base.frame.model.BaseModel;
import com.android.base.frame.model.IModel;
import com.android.base.frame.model.factory.ModelFactory;

/**
 * Created by Administrator on 2016/9/19.
 */
public class ModelLifecycleDelegate<M extends BaseModel> implements IModel {
    private ModelFactory<M> modelFactory;
    private M model;

    public ModelLifecycleDelegate(ModelFactory<M> modelFactory) {
        this.modelFactory = modelFactory;
    }

    public ModelFactory<M> getModelFactory() {
        return modelFactory;
    }

    public void setModelFactory(ModelFactory<M> modelFactory) {
        if (model != null)
            throw new IllegalArgumentException("setModelFactory() should be called before onCreate()");
        this.modelFactory = modelFactory;
    }

    public M getModel() {
        if (modelFactory != null) {
            if (this.model == null) {
                this.model = modelFactory.createModel();
                this.model.create();
            }
        }
        return model;
    }

    @Override
    public void onDestroy() {
        if (model != null) {
            model.destroy();
            model = null;
        }
    }


}