package com.ljz.base.widget.tabhost;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 作者: lujianzhao
 * 创建时间: 16/5/7 09:16
 * 描述: 含有快速点击按钮的tabHost
 */
public class SimpleFragmentTabHost extends FragmentTabHost {

    private String mCurrentTag;

    private String mNoTabChangedTag;

    public SimpleFragmentTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onTabChanged(String tag) {

        if (tag.equals(mNoTabChangedTag)) {
            setCurrentTabByTag(mCurrentTag);
        } else {
            super.onTabChanged(tag);
            mCurrentTag = tag;
        }
    }

    public void setNoTabChangedTag(String tag) {
        this.mNoTabChangedTag = tag;
    }

}