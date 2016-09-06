package com.android.base.glide.recyclerview;

import android.view.View;

import com.android.base.adapter.recyclerview.BaseQuickAdapter;
import com.bumptech.glide.ListPreloader;

import java.util.List;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/08/23 15:12
 * 描述:
 */
public abstract class ListPreloaderQuickAdapter<T> extends BaseQuickAdapter<T> implements ListPreloader.PreloadModelProvider<T>{


    public ListPreloaderQuickAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    public ListPreloaderQuickAdapter(List<T> data) {
        super(data);
    }

    public ListPreloaderQuickAdapter(View contentView, List<T> data) {
        super(contentView, data);
    }

    @Override
    public List<T> getPreloadItems(int position) {
        int index = position + 1;
        return  mData.subList(position, index >= mData.size() ? mData.size() :index);
    }

    /**
     * 最大预览数
     * @return
     */
    public int getMaxPreload() {
        return 5;
    }


}
