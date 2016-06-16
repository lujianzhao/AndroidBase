package com.android.base.frame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.autolayout.utils.AutoUtils;

import butterknife.ButterKnife;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/14 15:51
 * 描述:
 */
public abstract class UIViewHolder<T> {

    private View mConvertView;

    public abstract void refreshUI(T data);

    public UIViewHolder(Context context, ViewGroup parent, int layoutId) {
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ButterKnife.bind(this,mConvertView);
        //对于listview，注意添加这一行，即可在item上使用高度
        AutoUtils.autoSize(mConvertView);
    }

    public View getConvertView() {
        return mConvertView;
    }

}
