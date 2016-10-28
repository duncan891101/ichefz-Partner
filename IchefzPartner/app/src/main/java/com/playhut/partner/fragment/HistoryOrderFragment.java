package com.playhut.partner.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.playhut.partner.R;
import com.playhut.partner.activity.HistoryOrderActivity;
import com.playhut.partner.adapter.NewOrderAdapter;
import com.playhut.partner.business.LoadFailureBusiness;
import com.playhut.partner.entity.NewOrderEntity;
import com.playhut.partner.eventbus.RefundOrderEB;
import com.playhut.partner.mvp.presenter.IHistoryOrderPresent;
import com.playhut.partner.mvp.presenter.impl.HistoryOrderPresent;
import com.playhut.partner.mvp.view.HistoryOrderView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.pullrefresh.PullToRefreshBase;
import com.playhut.partner.pullrefresh.PullToRefreshListView;
import com.playhut.partner.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 *
 */
public class HistoryOrderFragment extends Fragment implements PullToRefreshBase.OnRefreshListener<ListView>{

    public static final String CURRENT_TAB_NAME = "mCurrentTab";

    private Context mContext;

    private int mCurrentTab;

    private View mRootView;

    private boolean mIsPrepared = false;

    private boolean mIsFragmentVisible = false;

//    private boolean mIsLoaded = false; //是否已加载过

    private PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;

    private List<NewOrderEntity.Order> mList;

    private NewOrderAdapter mAdapter;

    public static final int PAGE_SIZE = 8;

    private int mPage = 1; // 当前所处的页

    private int mTotalPage = 1; // 总页数

    public static final int SHOW_FOOTER_VIEW_LIMIT = 2; // 当所有item的总数小于这个数时，哪怕是最后一页也不显示footer view

    public static HistoryOrderFragment getInstance(int currentTab) {
        HistoryOrderFragment fragment = new HistoryOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CURRENT_TAB_NAME, currentTab);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCurrentTab = bundle.getInt(CURRENT_TAB_NAME);
            if (mCurrentTab == HistoryOrderActivity.CONFIRMED_TAB){
                EventBus.getDefault().register(this);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null) {
            /*因为公用一个Fragment，所以必须先判断该View是否已有父View*/
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        } else {
            mRootView = inflater.inflate(R.layout.fragment_history_order, null);
            mIsPrepared = true;
            initView();
            loadData();
        }
        return mRootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsFragmentVisible = isVisibleToUser;
        if (isVisibleToUser) {
            loadData();
        }
    }

    private void initView() {
        mPullToRefreshListView = (PullToRefreshListView) mRootView.findViewById(R.id.lv_pull_refresh);
        mListView = mPullToRefreshListView.getRefreshableView();
        mPullToRefreshListView.setOnRefreshListener(this);

        mList = new ArrayList<>();
        mListView.setDividerHeight(0);
        mAdapter = new NewOrderAdapter(mContext, mList);
        mListView.setAdapter(mAdapter);
    }

    private void loadData() {
        if (!mIsPrepared || !mIsFragmentVisible /*|| mIsLoaded*/) {
            return;
        }
//        mIsLoaded = true;
        mPage = 1;
        mTotalPage = 1;
        toGetHistoryOrder(true);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        toGetHistoryOrder(false);
    }

    private void toGetHistoryOrder(final boolean showLoadingView) {
        IHistoryOrderPresent present = new HistoryOrderPresent(mContext, new HistoryOrderView() {
            @Override
            public void startLoading() {
                if (showLoadingView) {
                    mPullToRefreshListView.mIchefzStateView.showLoadingView();
                }
            }

            @Override
            public void loadSuccess(NewOrderEntity entity) {
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
                if (entity.order_list != null && entity.order_list.size() > 0) {
                    mList.addAll(entity.order_list);
                } else {
                    String noItem;
                    if (mCurrentTab == HistoryOrderActivity.CONFIRMED_TAB) {
                        noItem = "There is no confirm order";
                    } else if (mCurrentTab == HistoryOrderActivity.FINISHED_TAB) {
                        noItem = "There is no finished order";
                    } else {
                        noItem = "There is no refund order";
                    }
                    mPullToRefreshListView.mIchefzStateView.showNoItemView(noItem);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void finishLoading() {
                mPullToRefreshListView.mIchefzStateView.dismissLoadingView();
                mPullToRefreshListView.mIchefzStateView.dismissNetworkErrorView();
                mPullToRefreshListView.mIchefzStateView.dismissLoadFailureView();
                mPullToRefreshListView.mIchefzStateView.dismissNoItemView();

                mPullToRefreshListView.onPullUpRefreshComplete();
            }

            @Override
            public void loadFailure(IchefzException exception) {
                if (mPage == 1) {
                    if (mList.size() == 0) {
                        // 第一次获取数据失败，并且原本没有数据，则显示错误和异常界面
                        LoadFailureBusiness.loadFailure(mContext, exception, mPullToRefreshListView.mIchefzStateView, null);
                    } else {
                        // 第一次获取数据失败，但是原本有数据，则只显示吐司。例如第一次获取成功后，接着下拉刷新时获取失败
                        ToastUtils.show(exception.getErrorMsg());
                    }
                } else {
                    mPullToRefreshListView.setLoadException(exception);
                }
            }
        });
        present.getList(String.valueOf(mCurrentTab), mPage, PAGE_SIZE);
    }

    @Subscribe
    public void onEventMainThread(RefundOrderEB refundOrderEB) {
        if (refundOrderEB != null && mCurrentTab == HistoryOrderActivity.CONFIRMED_TAB) {
            mList.remove(refundOrderEB.position);
            mAdapter.notifyDataSetChanged();
            if (mList.size() == 0){
                mPullToRefreshListView.mIchefzStateView.showNoItemView("There is no confirm order");
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mCurrentTab == HistoryOrderActivity.CONFIRMED_TAB){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
