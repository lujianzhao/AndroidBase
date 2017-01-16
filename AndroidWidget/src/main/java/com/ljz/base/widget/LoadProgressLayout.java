package com.ljz.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zhy.autolayout.AutoRelativeLayout;

/**
 * Created by Administrator on 2016/5/4.
 */
public class LoadProgressLayout extends RelativeLayout {

    private static final int NOT_SET = -1;

    private static final String LOADING_TAG = "ProgressLayout.loading_tag";
    private static final String NONE_TAG = "ProgressLayout.none_tag";
    private static final String ERROR_TAG = "ProgressLayout.error_tag";
    private static final String CONTENT_TAG = "ProgressLayout.content_tag";

    @LayoutRes
    private int mEmptyViewLayoutResId;
    @LayoutRes
    private int mErrorViewLayoutResId;
    @LayoutRes
    private int mLoadingViewLayoutResId;

    private LayoutInflater layoutInflater;

    private View emptyView;

    private View errorView;

    private View loadingView;

    private ViewGroup contentView;

    public LoadProgressLayout(Context context) {
        this(context, null);
    }

    public LoadProgressLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadProgressLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadProgressLayout, defStyleAttr, 0);
        try {
            mEmptyViewLayoutResId = a.getResourceId(R.styleable.LoadProgressLayout_emptyView, NOT_SET);
            mErrorViewLayoutResId = a.getResourceId(R.styleable.LoadProgressLayout_errorView, NOT_SET);
            mLoadingViewLayoutResId = a.getResourceId(R.styleable.LoadProgressLayout_loadingView, NOT_SET);
        } finally {
            a.recycle();
        }

        contentView = new AutoRelativeLayout(context, attrs, defStyleAttr);
        contentView.setTag(CONTENT_TAG);
        LoadProgressLayout.this.addView(contentView);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child.getTag() == null || (!child.getTag().equals(CONTENT_TAG) && !child.getTag().equals(LOADING_TAG) && !child.getTag().equals(NONE_TAG) && !child.getTag().equals(ERROR_TAG))) {
            contentView.addView(child, index, params);
        } else {
            super.addView(child, index, params);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (mEmptyViewLayoutResId != NOT_SET) {
            setEmptyView();
        }

        if (mErrorViewLayoutResId != NOT_SET) {
            setErrorView();
        }

        if (mLoadingViewLayoutResId != NOT_SET) {
            setLoadingView();
        }
    }

    public void setEmptyView() {
        if (this.emptyView == null) {

            if (mEmptyViewLayoutResId == NOT_SET) {
                throw new IllegalStateException("cannot call showNoneView() when noneId was NO_SET which value is -1");
            }

            this.emptyView = this.layoutInflater.inflate(mEmptyViewLayoutResId, LoadProgressLayout.this, false);
            this.emptyView.setTag(NONE_TAG);

            LayoutParams layoutParams = (LayoutParams) emptyView.getLayoutParams();
            layoutParams.addRule(CENTER_IN_PARENT);

            LoadProgressLayout.this.addView(emptyView, layoutParams);

            this.emptyView.setVisibility(GONE);
        }

    }

    public void setErrorView() {
        if (this.errorView == null) {

            if (mErrorViewLayoutResId == NOT_SET) {
                throw new IllegalStateException("cannot call showNetErrorView() when networkErrorId was NO_SET which value is -1");
            }

            this.errorView = this.layoutInflater.inflate(mErrorViewLayoutResId, LoadProgressLayout.this, false);
            this.errorView.setTag(ERROR_TAG);

            LayoutParams layoutParams = (LayoutParams) errorView.getLayoutParams();
            layoutParams.addRule(CENTER_IN_PARENT);

            LoadProgressLayout.this.addView(errorView, layoutParams);

            this.errorView.setVisibility(GONE);

        }
    }

    public void setLoadingView() {
        if (this.loadingView == null) {

            if (mLoadingViewLayoutResId == NOT_SET) {
                throw new IllegalStateException("cannot call showLoadingView() when loadingId was NO_SET which value is -1");
            }

            this.loadingView = this.layoutInflater.inflate(mLoadingViewLayoutResId, LoadProgressLayout.this, false);
            this.loadingView.setTag(LOADING_TAG);

            LayoutParams layoutParams = (LayoutParams) loadingView.getLayoutParams();
            layoutParams.addRule(CENTER_IN_PARENT);

            LoadProgressLayout.this.addView(loadingView, layoutParams);

            this.loadingView.setVisibility(GONE);
        }
    }

    public void showEmptyView() {
        LoadProgressLayout.this.emptyView.setVisibility(VISIBLE);

        LoadProgressLayout.this.hideLoadingView();
        LoadProgressLayout.this.hideErrorView();

        LoadProgressLayout.this.setContentVisibility(false);
    }

    public void showErrorView() {
        LoadProgressLayout.this.errorView.setVisibility(VISIBLE);

        LoadProgressLayout.this.hideLoadingView();
        LoadProgressLayout.this.hideEmptyView();

        LoadProgressLayout.this.setContentVisibility(false);
    }

    public void showLoadingView() {
        LoadProgressLayout.this.loadingView.setVisibility(VISIBLE);

        LoadProgressLayout.this.hideErrorView();
        LoadProgressLayout.this.hideEmptyView();

        LoadProgressLayout.this.setContentVisibility(false);
    }

    public void showContentView() {
        LoadProgressLayout.this.hideLoadingView();
        LoadProgressLayout.this.hideEmptyView();
        LoadProgressLayout.this.hideErrorView();

        LoadProgressLayout.this.setContentVisibility(true);
    }

    public boolean isShowLoadingViewOrErrorView() {
        boolean flag = false;
        if (loadingView.getVisibility() == View.VISIBLE || errorView.getVisibility() == View.VISIBLE) {
            flag = true;
        }
        return flag;
    }

    private void hideLoadingView() {
        if (loadingView != null && loadingView.getVisibility() != GONE) {
            this.loadingView.setVisibility(GONE);
        }
    }

    private void hideEmptyView() {
        if (emptyView != null && emptyView.getVisibility() != GONE) {
            this.emptyView.setVisibility(GONE);
        }
    }

    private void hideErrorView() {
        if (errorView != null && errorView.getVisibility() != GONE) {
            this.errorView.setVisibility(GONE);
        }
    }

    private void setContentVisibility(boolean visible) {
        contentView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
