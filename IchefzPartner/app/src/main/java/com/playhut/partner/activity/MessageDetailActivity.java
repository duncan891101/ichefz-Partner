package com.playhut.partner.activity;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.adapter.MessageDetailAdapter;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.business.LoadFailureBusiness;
import com.playhut.partner.constants.NetworkConstants;
import com.playhut.partner.debug.MyLog;
import com.playhut.partner.entity.MessageDetailEntity;
import com.playhut.partner.mvp.presenter.IDeleteMsgPresent;
import com.playhut.partner.mvp.presenter.IMessageDetailPresent;
import com.playhut.partner.mvp.presenter.impl.DeleteMsgPresent;
import com.playhut.partner.mvp.presenter.impl.MessageDetailPresent;
import com.playhut.partner.mvp.view.DeleteMsgView;
import com.playhut.partner.mvp.view.MessageDetailView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.pullrefresh.ILoadingLayout;
import com.playhut.partner.pullrefresh.PullToRefreshBase;
import com.playhut.partner.pullrefresh.PullToRefreshListView;
import com.playhut.partner.utils.DialogUtils;
import com.playhut.partner.utils.PartnerUtils;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.PartnerTitleBar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class MessageDetailActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener<ListView> {

    private PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;

    private PartnerTitleBar mTitleBar;

    public static final String SENDER_ID_INTENT = "mSenderIdIntent";

    private String mSenderId;

    private List<MessageDetailEntity.Message> mList;

    private MessageDetailAdapter mAdapter;

    private boolean mEditState = false;

    private Dialog mDeleteDialog;

    public static final int PAGE_SIZE = 10;

    private int mPage = 1; // 当前所处的页

    private int mTotalPage = 1; // 总页数

    public static final int SHOW_FOOTER_VIEW_LIMIT = 5; // 当所有item的总数小于这个数时，哪怕是最后一页也不显示footer view

    @Override
    protected void initView() {
        setContentView(R.layout.activity_message_detail);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_pull_refresh);
        mListView = mPullToRefreshListView.getRefreshableView();
    }

    @Override
    protected void initTitleBar() {
        mTitleBar = new PartnerTitleBar(this);
        mTitleBar.setCenterTvVisiable(true);
        mTitleBar.setCenterTvContent("In box");
        mTitleBar.setLeftIvVisiable(true);
        mTitleBar.setLeftIvContent(R.mipmap.back_black);
        mTitleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                MessageDetailActivity.this.finish();
            }
        });
        mTitleBar.setRightTv2Visiable(true);
        mTitleBar.setRightTv2Content("Edit");
        mTitleBar.setRightTv1Visiable(false);
        mTitleBar.setRightTv1Content("Delete");
        mTitleBar.setRightTv2Listener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                mEditState = !mEditState;
                if (mEditState) {
                    // 进入编辑模式
                    setInEditMode();
                } else {
                    // 恢复至正常模式
                    setInNormalMode();
                }
            }
        });
        mTitleBar.setRightTv1Listener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                int checkCount = getCheckCount();
                if (checkCount > 0) {
                    toDeleteMsg();
                } else {
                    ToastUtils.show("There is no selected message");
                }
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mSenderId = intent.getStringExtra(SENDER_ID_INTENT);
        }
        mList = new ArrayList<>();
        mPullToRefreshListView.setOnRefreshListener(this);
    }

    @Override
    protected void initLogic() {
        mListView.setDividerHeight(0);
        mAdapter = new MessageDetailAdapter(this, mList);
        mListView.setAdapter(mAdapter);

        getMessageDetailList(true);
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
        getMessageDetailList(false);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        getMessageDetailList(false);
    }

    private void getMessageDetailList(final boolean showLoadingView) {
        IMessageDetailPresent present = new MessageDetailPresent(this, new MessageDetailView() {
            @Override
            public void startLoading() {
                if (showLoadingView) {
                    mPullToRefreshListView.mIchefzStateView.showLoadingView();
                }
            }

            @Override
            public void loadSuccess(MessageDetailEntity entity) {
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
                        LoadFailureBusiness.loadFailure(MessageDetailActivity.this, exception, mPullToRefreshListView.mIchefzStateView, null);
                    } else {
                        // 第一次获取数据失败，但是原本有数据，则只显示吐司。例如第一次获取成功后，接着下拉刷新时获取失败
                        ToastUtils.show(exception.getErrorMsg());
                    }
                } else {
                    mPullToRefreshListView.setLoadException(exception);
                }
            }
        });
        present.getList(mSenderId, mPage, PAGE_SIZE);
    }

    public void doRefresh() {
        mPullToRefreshListView.doPullRefreshing(true, 100);
    }

    private void setInEditMode() {
        mTitleBar.setRightTv2Content("Cancel");
        mTitleBar.setRightTv1Visiable(true);

        for (MessageDetailEntity.Message message : mList) {
            message.isShow = true;
        }
        mAdapter.notifyDataSetChanged();
    }

    private void setInNormalMode() {
        mTitleBar.setRightTv2Content("Edit");
        mTitleBar.setRightTv1Visiable(false);

        for (MessageDetailEntity.Message message : mList) {
            message.isShow = false;
            message.isCheck = false;
            for (MessageDetailEntity.Child child : message.child) {
                child.isCheck = false;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void toDeleteMsg() {
        if (mDeleteDialog == null || !mDeleteDialog.isShowing()) {
            mDeleteDialog = DialogUtils.showConfirmDialog(this, R.layout.confirm_dialog_layout, true);
        }
        TextView titleTv = (TextView) mDeleteDialog.findViewById(R.id.tv_title);
        titleTv.setText("Delete");
        TextView textTv = (TextView) mDeleteDialog.findViewById(R.id.tv_text);
        textTv.setText("Are you sure to delete the selected message?");
        TextView confirmTv = (TextView) mDeleteDialog.findViewById(R.id.tv_confirm);
        confirmTv.setText("Delete");
        mDeleteDialog.findViewById(R.id.rl_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消
                dismissDeleteDialog();
            }
        });
        mDeleteDialog.findViewById(R.id.rl_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除
                dismissDeleteDialog();
                deleteMsg();
            }
        });
    }

    private void deleteMsg() {
        String messageId = getMessageId();
        IDeleteMsgPresent present = new DeleteMsgPresent(this, new DeleteMsgView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_delete), true);
            }

            @Override
            public void loadSuccess() {
                mTitleBar.setRightTv2Content("Edit");
                mTitleBar.setRightTv1Visiable(false);

                Iterator<MessageDetailEntity.Message> it = mList.iterator();
                while (it.hasNext()) {
                    MessageDetailEntity.Message message = it.next();
                    message.isShow = false;
                    if (message.isCheck) {
                        it.remove();
                    }
                    List<MessageDetailEntity.Child> childList = message.child;
                    Iterator<MessageDetailEntity.Child> childIterator = childList.iterator();
                    while (childIterator.hasNext()) {
                        MessageDetailEntity.Child child = childIterator.next();
                        if (child.isCheck) {
                            childIterator.remove();
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void finishLoading() {
                dismissLoadingDialog();
            }

            @Override
            public void loadFailure(IchefzException exception) {
                ToastUtils.show(exception.getErrorMsg());
            }
        });
        present.delete(messageId);
    }

    private int getCheckCount() {
        int count = 0;
        for (MessageDetailEntity.Message message : mList) {
            if (message.isCheck)
                count++;
            List<MessageDetailEntity.Child> childList = message.child;
            for (MessageDetailEntity.Child child : childList) {
                if (child.isCheck)
                    count++;
            }
        }
        return count;
    }

    private String getMessageId() {
        StringBuilder sb = new StringBuilder();
        for (MessageDetailEntity.Message message : mList) {
            if (message.isCheck) {
                sb.append(message.message_id);
                sb.append(",");
            }
            List<MessageDetailEntity.Child> childList = message.child;
            for (MessageDetailEntity.Child child : childList) {
                if (child.isCheck) {
                    sb.append(child.message_id);
                    sb.append(",");
                }
            }
        }
        String result = sb.toString();
        result = result.substring(0, result.length() - 1);
        return result;
    }

    private void dismissDeleteDialog() {
        if (mDeleteDialog != null && mDeleteDialog.isShowing()) {
            mDeleteDialog.dismiss();
            mDeleteDialog = null;
        }
    }

}
