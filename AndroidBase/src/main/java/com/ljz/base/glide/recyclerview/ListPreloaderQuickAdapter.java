package com.ljz.base.glide.recyclerview;

import com.ljz.base.adapter.recyclerview.BaseQuickAdapter;
import com.ljz.base.adapter.recyclerview.BaseViewHolder;
import com.bumptech.glide.ListPreloader;

import java.util.List;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/08/23 15:12
 * 描述:
 */
public abstract class ListPreloaderQuickAdapter<T> extends BaseQuickAdapter<T,BaseViewHolder> implements ListPreloader.PreloadModelProvider<T>{


    public ListPreloaderQuickAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
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
