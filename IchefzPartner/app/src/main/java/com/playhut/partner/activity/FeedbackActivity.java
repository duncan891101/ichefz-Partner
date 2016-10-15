package com.playhut.partner.activity;

import android.widget.ListView;

import com.playhut.partner.R;
import com.playhut.partner.adapter.FeedbackAdapter;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.entity.FeedbackEntity;
import com.playhut.partner.pullrefresh.PullToRefreshListView;
import com.playhut.partner.widget.PartnerTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FeedbackActivity extends BaseActivity {

    private PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;

    private List<FeedbackEntity.Feedback> mList;

    private FeedbackAdapter mAdapter;

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
    }

    @Override
    protected void initLogic() {
        mListView.setDividerHeight(0);
        mAdapter = new FeedbackAdapter(this, mList);
        mListView.setAdapter(mAdapter);

        for (int i=0; i<3; i++){
            FeedbackEntity.Feedback entity = new FeedbackEntity.Feedback();
            entity.customer_picture = "drawable://" + R.mipmap.avatar_test;
            entity.customer_first_name = "Tim";
            entity.customer_last_name = "Duncan" + i;
            entity.level = 5;
            entity.time = "2016-10-12 11:55";
            entity.content = "Today is a good day, xixi";

            List<String> imageList = new ArrayList<>();
            if (i == 0){
                imageList.add("drawable://" + R.mipmap.test1);
                imageList.add("drawable://" + R.mipmap.test1);
                imageList.add("drawable://" + R.mipmap.test1);

            } else if (i == 2){
                imageList.add("drawable://" + R.mipmap.avatar_test);
                imageList.add("drawable://" + R.mipmap.test1);
                imageList.add("drawable://" + R.mipmap.test1);
                imageList.add("drawable://" + R.mipmap.test1);
            }
            entity.imgs = imageList;
            mList.add(entity);
        }
        mAdapter.notifyDataSetChanged();
    }

}
