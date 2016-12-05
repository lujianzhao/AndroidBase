package com.lujianzhao.base.adapter.recyclerview.listener;

import android.view.View;

import com.lujianzhao.base.adapter.recyclerview.BaseQuickAdapter;


/**
 * create by: allen on 16/8/3.
 */

public abstract class OnItemLongClickListener extends SimpleClickListener {




    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        SimpleOnItemLongClick( adapter,  view,  position);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
    }
    public abstract void SimpleOnItemLongClick(BaseQuickAdapter adapter, View view, int position);
}
