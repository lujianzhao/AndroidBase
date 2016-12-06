package com.ljz.base.frame.model.factory;

import android.support.annotation.Nullable;

import com.ljz.base.frame.model.BaseModel;

/**
 * This class represents a {@link ModelFactory} that creates a model using {@link Class#newInstance()} method.
 *
 * @param <M> the type of the model.
 */
public class ReflectionModelFactory<M extends BaseModel> implements ModelFactory<M> {

    private Class<M> mModelClass;

    /**
     * This method returns a {@link ReflectionModelFactory} instance if a given view class has
     * a {@link RequiresModel} annotation, or null otherwise.
     *
     * @param presenterClass a class of the presenter
     * @param <M>       a type of the model
     * @return a {@link ReflectionModelFactory} instance that is supposed to create a presenter from {@link RequiresModel} annotation.
     */
    @Nullable
    public static <M extends BaseModel> ReflectionModelFactory<M> fromViewClass(Class<?> presenterClass) {
        RequiresModel annotation = presenterClass.getAnnotation(RequiresModel.class);
        //noinspection unchecked
        Class<M> modelClass = annotation == null ? null : (Class<M>)annotation.value();
        return modelClass == null ? null : new ReflectionModelFactory<>(modelClass);
    }

    public ReflectionModelFactory(Class<M> presenterClass) {
        this.mModelClass = presenterClass;
    }

    @Override
    public M createModel() {
        try {
            return mModelClass.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
