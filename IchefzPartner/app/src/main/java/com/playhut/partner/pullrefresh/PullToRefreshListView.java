package com.playhut.partner.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.ListView;

import com.playhut.partner.pullrefresh.ILoadingLayout.State;
import com.playhut.partner.utils.PartnerUtils;

/**
 * 这个类实现了ListView下拉刷新，上加载更多和滑到底部自动加载
 */
public class PullToRefreshListView extends PullToRefreshBase<ListView> implements OnScrollListener {

    /**
     * ListView
     */
    private ListView mListView;
    /**
     * 用于滑到底部自动加载的Footer
     */
    private LoadingLayout mLoadMoreFooterLayout;
    /**
     * 滚动的监听器
     */
    private OnScrollListener mScrollListener;

    private Context mContext;

    /**
     * 构造方法
     *
     * @param context context
     */
    public PullToRefreshListView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public PullToRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param context  context
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public PullToRefreshListView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setPullLoadEnabled(false);
    }

    @Override
    protected ListView createRefreshableView(Context context, AttributeSet attrs) {
        ListView listView = new ListView(context);
        mListView = listView;
        listView.setOnScrollListener(this);

        return listView;
    }

    /**
     * 设置是否有更多数据的标志
     *
     * @param hasMoreData   true表示还有更多的数据，false表示没有更多数据了
     */
    public void setHasMoreData(boolean hasMoreData) {
        if (!hasMoreData) {
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.setState(State.NO_MORE_DATA);
            }
        } else {
            mLoadMoreFooterLayout.setState(State.RESET);
        }
    }

    /**
     * 设置显示网络异常
     */
    public void setNetWorkError() {
        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(State.NET_WORK_ERROR);
        }
    }

    /**
     * 设置加载异常的显示
     */
    public void setLoadException() {
        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(State.LOAD_EXCEPTION);
        }
    }

    /**
     * 设置滑动的监听器
     *
     * @param l 监听器
     */
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    protected boolean isReadyForPullUp() {
        return isLastItemVisible();
    }

    @Override
    protected boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    @Override
    protected void startLoading() {
        super.startLoading();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(State.REFRESHING);
        }
    }

    @Override
    public void onPullUpRefreshComplete() {
        super.onPullUpRefreshComplete();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(State.RESET);
        }
    }

    @Override
    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        if (isScrollLoadEnabled() == scrollLoadEnabled) {
            return;
        }

        super.setScrollLoadEnabled(scrollLoadEnabled);

        if (scrollLoadEnabled) {
            // 设置Footer
            if (null == mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout = new PullAutoLoadingLayout(getContext());
                mListView.addFooterView(mLoadMoreFooterLayout, null, false);
            }

            mLoadMoreFooterLayout.show(true);
        } else {
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.show(false);
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isScrollLoadEnabled() && hasMoreData()) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                if (isReadyForPullUp()) {
                    if (PartnerUtils.checkNetwork(mContext)) {
                        startLoading();
                    } else {
                        setNetWorkError();
                    }
                }
            }
        }

        if (null != mScrollListener) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (null != mScrollListener) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }
    }

    @Override
    protected LoadingLayout createHeaderLoadingLayout(Context context,
                                                      AttributeSet attrs) {
        return new PullHeaderLoadingLayout(context);
    }

    /**
     * 表示是否还有更多数据
     *
     * @return true表示还有更多数据
     */
    private boolean hasMoreData() {
        if ((null != mLoadMoreFooterLayout)
                && (mLoadMoreFooterLayout.getState() == State.NO_MORE_DATA)) {
            return false;
        }

        return true;
    }

    /**
     * 判断第一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    public boolean isFirstItemVisible() {
        final Adapter adapter = mListView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        int mostTop = (mListView.getChildCount() > 0) ? mListView.getChildAt(0)
                .getTop() : 0;
        if (mostTop >= 0) {
            return true;
        }

        return false;
    }

    /**
     * 判断最后一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isLastItemVisible() {
        final Adapter adapter = mListView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = mListView.getLastVisiblePosition();

        /**
         * This check should really just be: lastVisiblePosition ==
         * lastItemPosition, but ListView internally uses a FooterView which
         * messes the positions up. For me we'll just subtract one to account
         * for it and rely on the inner condition which checks getBottom().
         */
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition
                    - mListView.getFirstVisiblePosition();
            final int childCount = mListView.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = mListView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= mListView.getBottom();
            }
        }

        return false;
    }

    @Override
    protected LoadingLayout createFooterLoadingLayout(Context context,
                                                      AttributeSet attrs) {
        return new PullFooterLoadingLayout(context);
    }
}
