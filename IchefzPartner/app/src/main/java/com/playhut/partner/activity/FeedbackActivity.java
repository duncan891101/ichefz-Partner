package com.playhut.partner.activity;

import android.widget.ListView;

import com.playhut.partner.R;
import com.playhut.partner.adapter.FeedbackAdapter;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.business.LoadFailureBusiness;
import com.playhut.partner.constants.NetworkConstants;
import com.playhut.partner.entity.FeedbackEntity;
import com.playhut.partner.mvp.presenter.IFeedbackPresent;
import com.playhut.partner.mvp.presenter.impl.FeedbackPresent;
import com.playhut.partner.mvp.view.FeedbackView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.pullrefresh.ILoadingLayout;
import com.playhut.partner.pullrefresh.PullToRefreshBase;
import com.playhut.partner.pullrefresh.PullToRefreshListView;
import com.playhut.partner.utils.PartnerUtils;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.PartnerTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FeedbackActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener<ListView>{

    private PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;

    private List<FeedbackEntity.Feedback> mList;

    private FeedbackAdapter mAdapter;

    public static final int PAGE_SIZE = 12;

    private int mPage = 1; // 当前所处的页

    private int mTotalPage = 1; // 总页数

    public static final int SHOW_FOOTER_VIEW_LIMIT = 6; // 当所有item的总数小于这个数时，哪怕是最后一页也不显示footer view

    @Override
    protected void initView() {
        setContentView(R.layout.activity_feedback);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_pull_refresh);
        mListView = mPullToRefreshListView.getRefreshableView();
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Feedback");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                FeedbackActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        mPullToRefreshListView.setOnRefreshListener(this);
    }

    @Override
    protected void initLogic() {
        mListView.setDividerHeight(0);
        mAdapter = new FeedbackAdapter(this, mList);
        mListView.setAdapter(mAdapter);

        getList(true);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (!PartnerUtils.checkNetwork(this)) {
            mPullToRefreshListView.onPullDownRefreshComplete();
            ToastUtils.show(NetworkConstants.NETWORK_ERROR_MSG);
            return;
        }
        if (mPullToRefreshListView.mPullUpState == ILoadingLayout.State.REFRESHING) {
            // 当前还处于自动加载更多中，则不能下拉刷新
            mPullToRefreshListView.onPullDownRefreshComplete();
            ToastUtils.show(getString(R.string.pull_to_refresh_loading));
            return;
        }
        mPage = 1;
        mTotalPage = 1;
        getList(false);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        getList(false);
    }

    private void getList(final boolean showLoadingView) {
        IFeedbackPresent present = new FeedbackPresent(this, new FeedbackView() {
            @Override
            public void startLoading() {
                if (showLoadingView) {
                    mPullToRefreshListView.mIchefzStateView.showLoadingView();
                }
            }

            @Override
            public void loadSuccess(FeedbackEntity entity) {
                mPage++;
                int total = entity.total;
                if (total % PAGE_SIZE == 0) {
                    mTotalPage = total / PAGE_SIZE;
                } else {
                    mTotalPage = (total / PAGE_SIZE) + 1;
                }
                mPullToRefreshListView.setScrollLoadEnabled(true);
                if (mPage <= mTotalPage) {
                    mPullToRefreshListView.setHasMoreData(true, true);
                } else {
                    if (total < SHOW_FOOTER_VIEW_LIMIT) {
                        mPullToRefreshListView.setHasMoreData(false, false);
                    } else {
                        mPullToRefreshListView.setHasMoreData(false, true);
                    }
                }
                if (mPage == 2) {
                    mList.clear();
                }
                if (entity.list != null && entity.list.size() > 0) {
                    mList.addAll(entity.list);
                } else {
                    mPullToRefreshListView.mIchefzStateView.showNoItemView("There is no feedback");
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void finishLoading() {
                mPullToRefreshListView.mIchefzStateView.dismissLoadingView();
                mPullToRefreshListView.mIchefzStateView.dismissNetworkErrorView();
                mPullToRefreshListView.mIchefzStateView.dismissLoadFailureView();
                mPullToRefreshListView.mIchefzStateView.dismissNoItemView();

                mPullToRefreshListView.setPullRefreshEnabled(true);
                mPullToRefreshListView.onPullUpRefreshComplete();
                mPullToRefreshListView.onPullDownRefreshComplete();
            }

            @Override
            public void loadFailure(IchefzException exception) {
                if (mPage == 1) {
                    if (mList.size() == 0) {
                        // 第一次获取数据失败，并且原本没有数据，则显示错误和异常界面
                        LoadFailureBusiness.loadFailure(FeedbackActivity.this, exception, mPullToRefreshListView.mIchefzStateView, null);
                    } else {
                        // 第一次获取数据失败，但是原本有数据，则只显示吐司。例如第一次获取成功后，接着下拉刷新时获取失败
                        ToastUtils.show(exception.getErrorMsg());
                    }
                } else {
                    mPullToRefreshListView.setLoadException(exception);
                }
            }
        });
        present.getList(mPage, PAGE_SIZE);
    }

}
