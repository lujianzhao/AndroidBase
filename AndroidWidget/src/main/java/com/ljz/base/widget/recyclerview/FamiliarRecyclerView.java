package com.ljz.base.widget.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.ljz.base.adapter.recyclerview.BaseQuickAdapter;
import com.ljz.base.glide.recyclerview.ListPreloaderQuickAdapter;
import com.ljz.base.glide.recyclerview.RecyclerViewPreloader;
import com.ljz.base.widget.R;
import com.ljz.base.widget.recyclerview.decoration.FamiliarDefaultItemDecoration;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.ViewPreloadSizeProvider;

/**
 * FamiliarRecyclerView
 * Created by iWgang on 15/10/31.
 * https://github.com/iwgang/FamiliarRecyclerView
 */
public class FamiliarRecyclerView extends RecyclerView implements FamiliarDefaultItemDecoration.RecyclerViewListener {
    public static final int LAYOUT_MANAGER_TYPE_LINEAR = 0;
    public static final int LAYOUT_MANAGER_TYPE_GRID = 1;
    public static final int LAYOUT_MANAGER_TYPE_STAGGERED_GRID = 2;

    private static final int DEF_LAYOUT_MANAGER_TYPE = LAYOUT_MANAGER_TYPE_LINEAR;
    private static final int DEF_GRID_SPAN_COUNT = 2;
    private static final int DEF_LAYOUT_MANAGER_ORIENTATION = OrientationHelper.VERTICAL;
    private static final int DEF_DIVIDER_HEIGHT = 30;

    private BaseQuickAdapter mReqAdapter;
    private GridLayoutManager mCurGridLayoutManager;
    private FamiliarDefaultItemDecoration mFamiliarDefaultItemDecoration;

    private Drawable mVerticalDivider;
    private Drawable mHorizontalDivider;
    private int mVerticalDividerHeight;
    private int mHorizontalDividerHeight;
    private int mItemViewBothSidesMargin;
    private boolean isHeaderDividersEnabled = false;
    private boolean isFooterDividersEnabled = false;
    private boolean isDefaultItemDecoration = true;
    private boolean isNotShowGridEndDivider = false;

    private boolean isMarginDividersEnabled = false;

    private int mLayoutManagerType;
    private Drawable mDefAllDivider;
    private int mDefAllDividerHeight;
    private boolean needInitAddItemDescration = false;

    private OnScrollListener mOnScrollListener;

    public FamiliarRecyclerView(Context context) {
        this(context, null);
    }

    public FamiliarRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FamiliarRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FamiliarRecyclerView);
        mDefAllDivider = ta.getDrawable(R.styleable.FamiliarRecyclerView_frv_divider);
        mDefAllDividerHeight = (int) ta.getDimension(R.styleable.FamiliarRecyclerView_frv_dividerHeight, -1);
        mVerticalDivider = ta.getDrawable(R.styleable.FamiliarRecyclerView_frv_dividerVertical);
        mHorizontalDivider = ta.getDrawable(R.styleable.FamiliarRecyclerView_frv_dividerHorizontal);
        mVerticalDividerHeight = (int) ta.getDimension(R.styleable.FamiliarRecyclerView_frv_dividerVerticalHeight, -1);
        mHorizontalDividerHeight = (int) ta.getDimension(R.styleable.FamiliarRecyclerView_frv_dividerHorizontalHeight, -1);
        mItemViewBothSidesMargin = (int) ta.getDimension(R.styleable.FamiliarRecyclerView_frv_itemViewBothSidesMargin, 0);
        isHeaderDividersEnabled = ta.getBoolean(R.styleable.FamiliarRecyclerView_frv_headerDividersEnabled, false);
        isFooterDividersEnabled = ta.getBoolean(R.styleable.FamiliarRecyclerView_frv_footerDividersEnabled, false);
        isNotShowGridEndDivider = ta.getBoolean(R.styleable.FamiliarRecyclerView_frv_isNotShowGridEndDivider, false);
        isMarginDividersEnabled = ta.getBoolean(R.styleable.FamiliarRecyclerView_frv_isMarginDividersEnabled, false);
        if (ta.hasValue(R.styleable.FamiliarRecyclerView_frv_layoutManager)) {
            int layoutManagerType = ta.getInt(R.styleable.FamiliarRecyclerView_frv_layoutManager, DEF_LAYOUT_MANAGER_TYPE);
            int layoutManagerOrientation = ta.getInt(R.styleable.FamiliarRecyclerView_frv_layoutManagerOrientation, DEF_LAYOUT_MANAGER_ORIENTATION);
            boolean isReverseLayout = ta.getBoolean(R.styleable.FamiliarRecyclerView_frv_isReverseLayout, false);
            int gridSpanCount = ta.getInt(R.styleable.FamiliarRecyclerView_frv_spanCount, DEF_GRID_SPAN_COUNT);

            switch (layoutManagerType) {
                case LAYOUT_MANAGER_TYPE_LINEAR:
                    setLayoutManager(new LinearLayoutManager(context, layoutManagerOrientation, isReverseLayout));
                    break;
                case LAYOUT_MANAGER_TYPE_GRID:
                    setLayoutManager(new GridLayoutManager(context, gridSpanCount, layoutManagerOrientation, isReverseLayout));
                    break;
                case LAYOUT_MANAGER_TYPE_STAGGERED_GRID:
                    setLayoutManager(new StaggeredGridLayoutManager(gridSpanCount, layoutManagerOrientation));
                    break;
            }
        }
        ta.recycle();

        mOnScrollListener= new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case SCROLL_STATE_IDLE:
                    case SCROLL_STATE_DRAGGING:
                        mReqAdapter.onScrollStop();
                        break;

                    case SCROLL_STATE_SETTLING:
                        mReqAdapter.onScrolling();
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        };
    }

    private void processDefDivider(boolean isLinearLayoutManager, int layoutManagerOrientation) {
        if (!isDefaultItemDecoration)
            return;

        if ((null == mVerticalDivider || null == mHorizontalDivider) && null != mDefAllDivider) {
            if (isLinearLayoutManager) {
                if (layoutManagerOrientation == OrientationHelper.VERTICAL && null == mHorizontalDivider) {
                    mHorizontalDivider = mDefAllDivider;
                } else if (layoutManagerOrientation == OrientationHelper.HORIZONTAL && null == mVerticalDivider) {
                    mVerticalDivider = mDefAllDivider;
                }
            } else {
                if (null == mVerticalDivider) {
                    mVerticalDivider = mDefAllDivider;
                }

                if (null == mHorizontalDivider) {
                    mHorizontalDivider = mDefAllDivider;
                }
            }
        }

        if (mVerticalDividerHeight > 0 && mHorizontalDividerHeight > 0)
            return;

        if (mDefAllDividerHeight > 0) {
            if (isLinearLayoutManager) {
                if (layoutManagerOrientation == OrientationHelper.VERTICAL && mHorizontalDividerHeight <= 0) {
                    mHorizontalDividerHeight = mDefAllDividerHeight;
                } else if (layoutManagerOrientation == OrientationHelper.HORIZONTAL && mVerticalDividerHeight <= 0) {
                    mVerticalDividerHeight = mDefAllDividerHeight;
                }
            } else {
                if (mVerticalDividerHeight <= 0) {
                    mVerticalDividerHeight = mDefAllDividerHeight;
                }

                if (mHorizontalDividerHeight <= 0) {
                    mHorizontalDividerHeight = mDefAllDividerHeight;
                }
            }
        } else {
            if (isLinearLayoutManager) {
                if (layoutManagerOrientation == OrientationHelper.VERTICAL && mHorizontalDividerHeight <= 0) {
                    if (null != mHorizontalDivider) {
                        if (mHorizontalDivider.getIntrinsicHeight() > 0) {
                            mHorizontalDividerHeight = mHorizontalDivider.getIntrinsicHeight();
                        } else {
                            mHorizontalDividerHeight = DEF_DIVIDER_HEIGHT;
                        }
                    }
                } else if (layoutManagerOrientation == OrientationHelper.HORIZONTAL && mVerticalDividerHeight <= 0) {
                    if (null != mVerticalDivider) {
                        if (mVerticalDivider.getIntrinsicHeight() > 0) {
                            mVerticalDividerHeight = mVerticalDivider.getIntrinsicHeight();
                        } else {
                            mVerticalDividerHeight = DEF_DIVIDER_HEIGHT;
                        }
                    }
                }
            } else {
                if (mVerticalDividerHeight <= 0 && null != mVerticalDivider) {
                    if (mVerticalDivider.getIntrinsicHeight() > 0) {
                        mVerticalDividerHeight = mVerticalDivider.getIntrinsicHeight();
                    } else {
                        mVerticalDividerHeight = DEF_DIVIDER_HEIGHT;
                    }
                }

                if (mHorizontalDividerHeight <= 0 && null != mHorizontalDivider) {
                    if (mHorizontalDivider.getIntrinsicHeight() > 0) {
                        mHorizontalDividerHeight = mHorizontalDivider.getIntrinsicHeight();
                    } else {
                        mHorizontalDividerHeight = DEF_DIVIDER_HEIGHT;
                    }
                }
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {

        if (adapter instanceof BaseQuickAdapter) {
            mReqAdapter = (BaseQuickAdapter) adapter;
            removeOnScrollListener(mOnScrollListener);
            addOnScrollListener(mOnScrollListener);
        }

        if (adapter instanceof ListPreloaderQuickAdapter) {
            ListPreloaderQuickAdapter tempAdapter = (ListPreloaderQuickAdapter)adapter;
            RecyclerViewPreloader preloader =new RecyclerViewPreloader(Glide.with(getContext()), tempAdapter, new ViewPreloadSizeProvider(), tempAdapter.getMaxPreload());
            addOnScrollListener(preloader);
        }

        super.setAdapter(adapter);

        if (needInitAddItemDescration && null != mFamiliarDefaultItemDecoration) {
            needInitAddItemDescration = false;
            super.addItemDecoration(mFamiliarDefaultItemDecoration);
        }

    }


    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);

        if (null == layout)
            return;

        if (layout instanceof GridLayoutManager) {
            mCurGridLayoutManager = ((GridLayoutManager) layout);
            mCurGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position < getHeaderViewsCount() || (null != mReqAdapter && position >= mReqAdapter.getItemCount() + getHeaderViewsCount())) {
                        // header or footer span
                        return mCurGridLayoutManager.getSpanCount();
                    } else {
                        // default item span
                        return 1;
                    }
                }
            });

            mLayoutManagerType = LAYOUT_MANAGER_TYPE_GRID;
            processDefDivider(false, mCurGridLayoutManager.getOrientation());
            initDefaultItemDecoration();
        } else if (layout instanceof StaggeredGridLayoutManager) {
            mLayoutManagerType = LAYOUT_MANAGER_TYPE_STAGGERED_GRID;
            processDefDivider(false, ((StaggeredGridLayoutManager) layout).getOrientation());
            initDefaultItemDecoration();
        } else if (layout instanceof LinearLayoutManager) {
            mLayoutManagerType = LAYOUT_MANAGER_TYPE_LINEAR;
            processDefDivider(true, ((LinearLayoutManager) layout).getOrientation());
            initDefaultItemDecoration();
        }
    }

    @Override
    public void addItemDecoration(ItemDecoration decor) {
        if (null == decor)
            return;

        // remove default ItemDecoration
        if (null != mFamiliarDefaultItemDecoration) {
            removeItemDecoration(mFamiliarDefaultItemDecoration);
            mFamiliarDefaultItemDecoration = null;
        }

        isDefaultItemDecoration = false;

        super.addItemDecoration(decor);
    }

    private void initDefaultItemDecoration() {
        if (!isDefaultItemDecoration)
            return;

        if (null != mFamiliarDefaultItemDecoration) {
            super.removeItemDecoration(mFamiliarDefaultItemDecoration);
            mFamiliarDefaultItemDecoration = null;
        }

        mFamiliarDefaultItemDecoration = new FamiliarDefaultItemDecoration(this, mVerticalDivider, mHorizontalDivider, mVerticalDividerHeight, mHorizontalDividerHeight);
        mFamiliarDefaultItemDecoration.setItemViewBothSidesMargin(mItemViewBothSidesMargin);
        mFamiliarDefaultItemDecoration.setHeaderDividersEnabled(isHeaderDividersEnabled);
        mFamiliarDefaultItemDecoration.setFooterDividersEnabled(isFooterDividersEnabled);
        mFamiliarDefaultItemDecoration.setNotShowGridEndDivider(isNotShowGridEndDivider);
        mFamiliarDefaultItemDecoration.setMarginDividersEnabled(isMarginDividersEnabled);

        if (null != getAdapter()) {
            needInitAddItemDescration = false;
            super.addItemDecoration(mFamiliarDefaultItemDecoration);
        } else {
            needInitAddItemDescration = true;
        }
    }

    public boolean isShowEmptyView() {
        return null != mReqAdapter && mReqAdapter.getEmptyView() != null && mReqAdapter.getItemCount() > 0;
    }


    public void setDivider(int height, Drawable divider) {
        if (!isDefaultItemDecoration || height <= 0)
            return;

        this.mVerticalDividerHeight = height;
        this.mHorizontalDividerHeight = height;

        if (this.mVerticalDivider != divider) {
            this.mVerticalDivider = divider;
        }

        if (this.mHorizontalDivider != divider) {
            this.mHorizontalDivider = divider;
        }

        if (null != mFamiliarDefaultItemDecoration) {
            mFamiliarDefaultItemDecoration.setVerticalDividerDrawableHeight(mVerticalDividerHeight);
            mFamiliarDefaultItemDecoration.setHorizontalDividerDrawableHeight(mHorizontalDividerHeight);

            mFamiliarDefaultItemDecoration.setVerticalDividerDrawable(mVerticalDivider);
            mFamiliarDefaultItemDecoration.setHorizontalDividerDrawable(mHorizontalDivider);

            getAdapter().notifyDataSetChanged();

        }
    }

    public void setDivider(Drawable divider) {
        if (!isDefaultItemDecoration || (mVerticalDividerHeight <= 0 && mHorizontalDividerHeight <= 0))
            return;

        if (this.mVerticalDivider != divider) {
            this.mVerticalDivider = divider;
        }

        if (this.mHorizontalDivider != divider) {
            this.mHorizontalDivider = divider;
        }

        if (null != mFamiliarDefaultItemDecoration) {
            mFamiliarDefaultItemDecoration.setVerticalDividerDrawable(mVerticalDivider);
            mFamiliarDefaultItemDecoration.setHorizontalDividerDrawable(mHorizontalDivider);

            getAdapter().notifyDataSetChanged();
        }
    }

    public void setDivider(Drawable dividerVertical, Drawable dividerHorizontal) {
        if (!isDefaultItemDecoration || (mVerticalDividerHeight <= 0 && mHorizontalDividerHeight <= 0))
            return;

        if (this.mVerticalDivider != dividerVertical) {
            this.mVerticalDivider = dividerVertical;
        }

        if (this.mHorizontalDivider != dividerHorizontal) {
            this.mHorizontalDivider = dividerHorizontal;
        }

        if (null != mFamiliarDefaultItemDecoration) {
            mFamiliarDefaultItemDecoration.setVerticalDividerDrawable(mVerticalDivider);
            mFamiliarDefaultItemDecoration.setHorizontalDividerDrawable(mHorizontalDivider);

            getAdapter().notifyDataSetChanged();
        }
    }

    public void setDividerVertical(Drawable dividerVertical) {
        if (!isDefaultItemDecoration || mVerticalDividerHeight <= 0)
            return;

        if (this.mVerticalDivider != dividerVertical) {
            this.mVerticalDivider = dividerVertical;
        }

        if (null != mFamiliarDefaultItemDecoration) {
            mFamiliarDefaultItemDecoration.setVerticalDividerDrawable(mVerticalDivider);

            getAdapter().notifyDataSetChanged();
        }
    }

    public void setDividerHorizontal(Drawable dividerHorizontal) {
        if (!isDefaultItemDecoration || mHorizontalDividerHeight <= 0)
            return;

        if (this.mHorizontalDivider != dividerHorizontal) {
            this.mHorizontalDivider = dividerHorizontal;
        }

        if (null != mFamiliarDefaultItemDecoration) {
            mFamiliarDefaultItemDecoration.setHorizontalDividerDrawable(mHorizontalDivider);

            getAdapter().notifyDataSetChanged();
        }
    }

    public void setDividerHeight(int height) {
        if (!isDefaultItemDecoration)
            return;

        this.mVerticalDividerHeight = height;
        this.mHorizontalDividerHeight = height;

        if (null != mFamiliarDefaultItemDecoration) {
            mFamiliarDefaultItemDecoration.setVerticalDividerDrawableHeight(mVerticalDividerHeight);
            mFamiliarDefaultItemDecoration.setHorizontalDividerDrawableHeight(mHorizontalDividerHeight);

            getAdapter().notifyDataSetChanged();
        }
    }

    public void setDividerVerticalHeight(int height) {
        if (!isDefaultItemDecoration)
            return;

        this.mVerticalDividerHeight = height;

        if (null != mFamiliarDefaultItemDecoration) {
            mFamiliarDefaultItemDecoration.setVerticalDividerDrawableHeight(mVerticalDividerHeight);

            getAdapter().notifyDataSetChanged();
        }
    }

    public void setDividerHorizontalHeight(int height) {
        if (!isDefaultItemDecoration)
            return;

        this.mHorizontalDividerHeight = height;

        if (null != mFamiliarDefaultItemDecoration) {
            mFamiliarDefaultItemDecoration.setHorizontalDividerDrawableHeight(mHorizontalDividerHeight);

            getAdapter().notifyDataSetChanged();
        }
    }

    public void setItemViewBothSidesMargin(int bothSidesMargin) {
        if (!isDefaultItemDecoration)
            return;

        this.mItemViewBothSidesMargin = bothSidesMargin;

        if (null != mFamiliarDefaultItemDecoration) {
            mFamiliarDefaultItemDecoration.setItemViewBothSidesMargin(mItemViewBothSidesMargin);

            getAdapter().notifyDataSetChanged();
        }
    }

    public int getHeaderViewsCount() {
        return null != mReqAdapter ? mReqAdapter.getHeaderViewsCount() : 0;
    }

    public int getFooterViewsCount() {
        return null != mReqAdapter ? mReqAdapter.getFooterViewsCount() : 0;
    }

    public int getFirstVisiblePosition() {
        LayoutManager layoutManager = getLayoutManager();

        if (null == layoutManager)
            return 0;

        int ret = -1;

        switch (mLayoutManagerType) {
            case LAYOUT_MANAGER_TYPE_LINEAR:
                ret = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition() - getHeaderViewsCount();
                break;
            case LAYOUT_MANAGER_TYPE_GRID:
                ret = ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition() - getHeaderViewsCount();
                break;
            case LAYOUT_MANAGER_TYPE_STAGGERED_GRID:
                StaggeredGridLayoutManager tempStaggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int[] firstVisibleItemPositions = new int[tempStaggeredGridLayoutManager.getSpanCount()];
                tempStaggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(firstVisibleItemPositions);
                ret = firstVisibleItemPositions[0] - getHeaderViewsCount();
                break;
        }

        return ret < 0 ? 0 : ret;
    }

    public int getLastVisiblePosition() {
        LayoutManager layoutManager = getLayoutManager();
        if (null == layoutManager)
            return -1;

        int curItemCount = (null != mReqAdapter ? mReqAdapter.getItemCount() - 1 : 0);
        int ret = -1;

        switch (mLayoutManagerType) {
            case LAYOUT_MANAGER_TYPE_LINEAR:
                ret = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition() - getHeaderViewsCount();
                if (ret > curItemCount) {
                    ret -= getFooterViewsCount();
                }
                break;
            case LAYOUT_MANAGER_TYPE_GRID:
                ret = ((GridLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition() - getHeaderViewsCount();
                if (ret > curItemCount) {
                    ret -= getFooterViewsCount();
                }
                break;
            case LAYOUT_MANAGER_TYPE_STAGGERED_GRID:
                StaggeredGridLayoutManager tempStaggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int[] lastVisibleItemPositions = new int[tempStaggeredGridLayoutManager.getSpanCount()];
                tempStaggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastVisibleItemPositions);
                if (lastVisibleItemPositions.length > 0) {
                    int maxPos = lastVisibleItemPositions[0];
                    for (int curPos : lastVisibleItemPositions) {
                        if (curPos > maxPos)
                            maxPos = curPos;
                    }
                    ret = maxPos - getHeaderViewsCount();
                    if (ret > curItemCount) {
                        ret -= getFooterViewsCount();
                    }
                }
                break;
        }

        return ret < 0 ? (null != mReqAdapter ? mReqAdapter.getItemCount() - 1 : 0) : ret;
    }

    public void setHeaderDividersEnabled(boolean isHeaderDividersEnabled) {
        this.isHeaderDividersEnabled = isHeaderDividersEnabled;
        if (isDefaultItemDecoration && null != mFamiliarDefaultItemDecoration) {
            mFamiliarDefaultItemDecoration.setHeaderDividersEnabled(isHeaderDividersEnabled);

            getAdapter().notifyDataSetChanged();
        }
    }

    public void setFooterDividersEnabled(boolean isFooterDividersEnabled) {
        this.isFooterDividersEnabled = isFooterDividersEnabled;
        if (isDefaultItemDecoration && null != mFamiliarDefaultItemDecoration) {
            mFamiliarDefaultItemDecoration.setFooterDividersEnabled(isFooterDividersEnabled);

            getAdapter().notifyDataSetChanged();
        }
    }

    public void setNotShowGridEndDivider(boolean isNotShowGridEndDivider) {
        this.isNotShowGridEndDivider = isNotShowGridEndDivider;
        if (isDefaultItemDecoration && null != mFamiliarDefaultItemDecoration) {
            mFamiliarDefaultItemDecoration.setNotShowGridEndDivider(isNotShowGridEndDivider);

            getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public int getCurLayoutManagerType() {
        return mLayoutManagerType;
    }

    @Override
    public LayoutManager getCurLayoutManager() {
        return getLayoutManager();
    }

}