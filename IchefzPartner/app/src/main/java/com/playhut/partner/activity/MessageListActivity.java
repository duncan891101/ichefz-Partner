package com.playhut.partner.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.playhut.partner.R;
import com.playhut.partner.adapter.MessageListAdapter;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.entity.MessageListEntity;
import com.playhut.partner.pullrefresh.PullToRefreshListView;
import com.playhut.partner.widget.PartnerTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MessageListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;

    private List<MessageListEntity.Message> mList;

    private MessageListAdapter mAdapter;

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
    }

    @Override
    protected void initLogic() {
        mListView.setDividerHeight(0);
        mAdapter = new MessageListAdapter(this, mList);
        mListView.setAdapter(mAdapter);

        for (int i = 0; i < 4; i++) {
            MessageListEntity.Message message = new MessageListEntity.Message();
            message.first_name = "Bingo";
            message.last_name = "Zhang";
            message.content = "Today is a good day!";
            message.profile_picture = "drawable://" + R.mipmap.test1;
            message.time = "2016-10-10 13:42";
            if (i % 2 == 0) {
                message.status = 1;
            } else {
                message.status = 0;
            }
            mList.add(message);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MessageListEntity.Message message = mList.get(position);
        Intent intent = new Intent(this, MessageDetailActivity.class);
        intent.putExtra(MessageDetailActivity.SENDER_ID_INTENT, message.sender_id);
        startActivity(intent);
    }

}
