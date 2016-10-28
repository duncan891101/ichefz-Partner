package com.playhut.partner.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.playhut.partner.R;
import com.playhut.partner.adapter.MessageListAdapter;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.business.LoadFailureBusiness;
import com.playhut.partner.constants.NetworkConstants;
import com.playhut.partner.entity.MessageListEntity;
import com.playhut.partner.mvp.presenter.IMessageListPresent;
import com.playhut.partner.mvp.presenter.impl.MessageListPresent;
import com.playhut.partner.mvp.view.MessageListView;
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
public class MessageListActivity extends BaseActivity implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener<ListView> {

    private PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;

    private List<MessageListEntity.Message> mList;

    private MessageListAdapter mAdapter;

    public static final int PAGE_SIZE = 15;

    private int mPage = 1; // 当前所处的页

    private int mTotalPage = 1; // 总页数

    public static final int SHOW_FOOTER_VIEW_LIMIT = 8; // 当所有item的总数小于这个数时，哪怕是最后一页也不显示footer view

    @Override
    protected void initView() {
        setContentView(R.layout.activity_message_list);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_pull_refresh);
        mListView = mPullToRefreshListView.getRefreshableView();
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("In box");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                MessageListActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        mListView.setOnItemClickListener(this);
        mPullToRefreshListView.setOnRefreshListener(this);
    }

    @Override
    protected void initLogic() {
        mListView.setDividerHeight(0);
        mAdapter = new MessageListAdapter(this, mList);
        mListView.setAdapter(mAdapter);

        getMessageList(true);
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
        getMessageList(false);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        getMessageList(false);
    }

    private void getMessageList(final boolean showLoadingView) {
        IMessageListPresent present = new MessageListPresent(this, new MessageListView() {
            @Override
            public void startLoading() {
                if (showLoadingView) {
                    mPullToRefreshListView.mIchefzStateView.showLoadingView();
                }
            }

            @Override
            public void loadSuccess(MessageListEntity entity) {
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
                if (entity.message_list != null && entity.message_list.size() > 0) {
                    mList.addAll(entity.message_list);
                } else {
                    mPullToRefreshListView.mIchefzStateView.showNoItemView("There is no messages");
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
                        LoadFailureBusiness.loadFailure(MessageListActivity.this, exception, mPullToRefreshListView.mIchefzStateView, null);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MessageListEntity.Message message = mList.get(position);
        if (message.status == 0) {
            // 如果原本是未读，那么就置为已读
            message.status = 1;
            mAdapter.notifyDataSetChanged();
        }
        Intent intent = new Intent(this, MessageDetailActivity.class);
        intent.putExtra(MessageDetailActivity.SENDER_ID_INTENT, message.sender_id);
        startActivity(intent);
    }

}
