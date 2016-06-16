package com.android.base.adapter.recyclerview;

public interface MultiItemRcvTypeSupport<T> {

    int getLayoutId(int viewType);

    int getItemViewType(int position, T t);

}