package com.ljz.base.frame.hodler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljz.base.common.rx.RxManager;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.ButterKnife;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/14 15:51
 * 描述:
 */
public abstract class UIViewHolder<T> {

    private View mConvertView;

    protected Context mContext;

    private final RxManager mRxManager = new RxManager();

    public abstract void refreshUI(T data);

    public UIViewHolder(Context context, ViewGroup parent, int layoutId) {
        mContext = context;
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ButterKnife.bind(this, mConvertView);
        AutoUtils.autoSize(mConvertView);
    }

    public View getConvertView() {
        return mConvertView;
    }

    protected RxManager getRxManager() {
        return mRxManager;
    }

    public void onDestroy() {
        ButterKnife.unbind(this);

        mConvertView = null;
        mContext = null;

        mRxManager.clear();
    }

}
