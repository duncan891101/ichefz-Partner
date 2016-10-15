package com.playhut.partner.activity;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.adapter.MessageDetailAdapter;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.entity.MessageDetailEntity;
import com.playhut.partner.pullrefresh.PullToRefreshListView;
import com.playhut.partner.utils.DialogUtils;
import com.playhut.partner.widget.PartnerTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MessageDetailActivity extends BaseActivity {

    private PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;

    private PartnerTitleBar mTitleBar;

    public static final String SENDER_ID_INTENT = "mSenderIdIntent";

    private String mSenderId;

    private List<MessageDetailEntity.Message> mList;

    private MessageDetailAdapter mAdapter;

    private boolean mEditState = false;

    private Dialog mDeleteDialog;

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
                toDeleteMsg();
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
    }

    @Override
    protected void initLogic() {
        mListView.setDividerHeight(0);
        mAdapter = new MessageDetailAdapter(this, mList);
        mListView.setAdapter(mAdapter);


        for (int i = 0; i < 6; i++) {
            MessageDetailEntity.Message message = new MessageDetailEntity.Message();

            if (i % 2 == 0) {
                message.first_name = "Bingo";
                message.last_name = "Zhang";
                message.profile_picture = "drawable://" + R.mipmap.avatar_test;
                message.time = "2016-10-10 15:38";
                message.content = "Today is good day";

                List<MessageDetailEntity.Child> childList = new ArrayList<>();
                for (int j = 0; j < 2; j++) {
                    MessageDetailEntity.Child child = new MessageDetailEntity.Child();
                    child.first_name = "Duncan";
                    child.last_name = "";
                    child.profile_picture = "drawable://" + R.mipmap.test1;
                    child.time = "2016-10-10 16:00";
                    child.content = "Hi, My name is XX";
                    childList.add(child);
                }

                message.child = childList;
            } else {
                message.first_name = "Bingo";
                message.last_name = "Zhang";
                message.profile_picture = "drawable://" + R.mipmap.avatar_test;
                message.time = "2016-10-10 15:38";
                message.content = "Today is good day";

                List<MessageDetailEntity.Child> childList = new ArrayList<>();
                message.child = childList;
            }

            mList.add(message);
        }
        mAdapter.notifyDataSetChanged();
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
                // 登录
                dismissDeleteDialog();
            }
        });
    }

    private void dismissDeleteDialog() {
        if (mDeleteDialog != null && mDeleteDialog.isShowing()) {
            mDeleteDialog.dismiss();
            mDeleteDialog = null;
        }
    }

}
