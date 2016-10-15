package com.playhut.partner.activity;

import android.widget.ListView;

import com.playhut.partner.R;
import com.playhut.partner.adapter.NewOrderAdapter;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.constants.OrderState;
import com.playhut.partner.entity.NewOrderEntity;
import com.playhut.partner.pullrefresh.PullToRefreshBase;
import com.playhut.partner.pullrefresh.PullToRefreshListView;
import com.playhut.partner.widget.PartnerTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class NewOrderActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener<ListView> {

    private PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;

    private NewOrderAdapter mAdapter;

    private List<NewOrderEntity.Order> mList;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_new_order);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_pull_refresh);
        mListView = mPullToRefreshListView.getRefreshableView();
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("New orders");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                NewOrderActivity.this.finish();
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
        mAdapter = new NewOrderAdapter(this, mList);
        mListView.setAdapter(mAdapter);

        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                testData1();
            } else {
                testData2();
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void testData1() {
        NewOrderEntity.Order order = new NewOrderEntity.Order();
        order.order_number = "25873491643";
        order.time = "2016/09/29 13:34AM";
        order.customer_first_name = "Bingo";
        order.customer_last_name = "Zhang";
        order.customer_number = "987-020-1354";
        order.customer_address = "aaa bbc eeffa jiiwk hhexx";
        order.subtotal = "25.20";
        order.order_state = OrderState.PAID_WAIT_CONFIRM;
        List<NewOrderEntity.OrderItem> list = new ArrayList<>();
        NewOrderEntity.OrderItem item1 = new NewOrderEntity.OrderItem();
        List<String> urlList = new ArrayList<>();
        urlList.add("drawable://" + R.mipmap.test1);
        item1.picture = urlList;
        item1.mtype = "list";
        item1.item_price = "15.02";
        item1.person = "2";
        item1.quantity = "1";
        item1.title = "Steak and shrimp";
        list.add(item1);
        order.order_items = list;

        mList.add(order);
    }

    private void testData2() {
        NewOrderEntity.Order order = new NewOrderEntity.Order();
        order.order_number = "25873491643";
        order.time = "2016/09/29 13:34AM";
        order.customer_first_name = "Bingo";
        order.customer_last_name = "Zhang";
        order.customer_number = "987-020-1354";
        order.customer_address = "aaa bbc eeffa jiiwk hhexx";
        order.subtotal = "25.20";
        order.order_state = OrderState.PAID_WAIT_CONFIRM;
        order.remark = "many rice xiie eowjow weoowgw woejowjw weowwe wefwfw wefwefwfwf";
        List<NewOrderEntity.OrderItem> list = new ArrayList<>();

        NewOrderEntity.OrderItem item1 = new NewOrderEntity.OrderItem();
        List<String> urlList = new ArrayList<>();
        urlList.add("drawable://" + R.mipmap.test1);
        item1.picture = urlList;
        item1.mtype = "list";
        item1.item_price = "15.02";
        item1.person = "2";
        item1.quantity = "1";
        item1.title = "Steak and shrimp";
        list.add(item1);

        NewOrderEntity.OrderItem item2 = new NewOrderEntity.OrderItem();
        List<String> urlList2 = new ArrayList<>();
        urlList2.add("drawable://" + R.mipmap.test1);
        urlList2.add("drawable://" + R.mipmap.test1);
        urlList2.add("drawable://" + R.mipmap.test1);
        urlList2.add("drawable://" + R.mipmap.test1);
        item2.picture = urlList2;
        item2.mtype = "set";
        item2.item_price = "15.02";
        item2.person = "2";
        item2.quantity = "3";
        item2.title = "Crispy catfish";
        list.add(item2);

        order.order_items = list;

        mList.add(order);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

}
