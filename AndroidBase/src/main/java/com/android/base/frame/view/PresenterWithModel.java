package com.android.base.frame.view;

import com.android.base.frame.model.BaseModel;
import com.android.base.frame.model.factory.ModelFactory;

/**
 * Created by Administrator on 2016/9/19.
 */
public interface PresenterWithModel<M extends BaseModel> {

    /**
     * Returns a current model factory.
     */
    ModelFactory<M> getModelFactory();

    /**
     * Sets a model factory.
     * Call this method before onCreate/onFinishInflate to override default {#@link ReflectionModelFactory} model factory.
     * Use this method for model dependency injection.
     */
    void setModelFactory(ModelFactory<M> modelFactory);

    /**
     * Returns a current attached model.
     * This method is guaranteed to return a non-null value between
     * onResume/onPause and onAttachedToWindow/onDetachedFromWindow calls
     * if the model factory returns a non-null value.
     *
     * @return a currently attached model or null.
     */
    M getModel();
}
