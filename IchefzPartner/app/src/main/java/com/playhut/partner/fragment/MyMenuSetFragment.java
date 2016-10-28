package com.playhut.partner.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.playhut.partner.R;
import com.playhut.partner.adapter.MyMenuSetAdapter;
import com.playhut.partner.business.LoadFailureBusiness;
import com.playhut.partner.entity.MyMenuSetEntity;
import com.playhut.partner.mvp.presenter.IGetMenuSetListPresent;
import com.playhut.partner.mvp.presenter.impl.GetMenuSetListPresent;
import com.playhut.partner.mvp.view.GetMenuSetListView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.pullrefresh.PullToRefreshBase;
import com.playhut.partner.pullrefresh.PullToRefreshListView;
import com.playhut.partner.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MyMenuSetFragment extends Fragment implements PullToRefreshBase.OnRefreshListener<ListView> {

    private Context mContext;

    private View mRootView;

    private boolean mIsPrepared = false;

    private boolean mIsFragmentVisible = false;

    private PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;

    private MyMenuSetAdapter mAdapter;

    private List<MyMenuSetEntity.SetInfo> mList;

    public static final int PAGE_SIZE = 10;

    private int mPage = 1; // 当前所处的页

    private int mTotalPage = 1; // 总页数

    public static final int SHOW_FOOTER_VIEW_LIMIT = 6; // 当所有item的总数小于这个数时，哪怕是最后一页也不显示footer view

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
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
            mRootView = inflater.inflate(R.layout.fragment_my_menu, null);
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

        mListView.setDividerHeight(0);
        mList = new ArrayList<>();
        mAdapter = new MyMenuSetAdapter(mContext, mList);
        mAdapter.setDeleteSuccessListener(new MenuDeleteSuccessListener());
        mListView.setAdapter(mAdapter);
    }

    private void loadData() {
        if (!mIsPrepared || !mIsFragmentVisible) {
            return;
        }

        mPage = 1;
        mTotalPage = 1;
        toGetMenuSetList(true);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        toGetMenuSetList(false);
    }

    private void toGetMenuSetList(final boolean showLoadingView) {
        IGetMenuSetListPresent present = new GetMenuSetListPresent(mContext, new GetMenuSetListView() {
            @Override
            public void startLoading() {
                if (showLoadingView) {
                    mPullToRefreshListView.mIchefzStateView.showLoadingView();
                }
            }

            @Override
            public void loadSuccess(MyMenuSetEntity entity) {
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
                if (entity.set_list != null && entity.set_list.size() > 0) {
                    mList.addAll(entity.set_list);
                } else {
                    showNoItemView();
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
        present.getList(mPage, PAGE_SIZE);
    }

    /**
     * 删除菜单成功的回调
     */
    private class MenuDeleteSuccessListener implements MyMenuSetAdapter.DeleteSuccessListener {
        @Override
        public void delete(String info, int position) {
            ToastUtils.show(info);
            mList.remove(position);
            mAdapter.notifyDataSetChanged();
            if (mList.size() == 0) {
                showNoItemView();
            }
        }
    }

    public void showNoItemView() {
        mPullToRefreshListView.mIchefzStateView.showNoItemView("There is no set item");
    }

}
