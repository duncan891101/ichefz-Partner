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
import com.playhut.partner.constants.OrderState;
import com.playhut.partner.entity.NewOrderEntity;
import com.playhut.partner.pullrefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class HistoryOrderFragment extends Fragment {

    public static final String CURRENT_TAB_NAME = "mCurrentTab";

    private Context mContext;

    private int mCurrentTab;

    private View mRootView;

    private boolean mIsPrepared = false;

    private boolean mIsFragmentVisible = false;

    private boolean mIsLoaded = false; //是否已加载过

    private PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;

    private List<NewOrderEntity.Order> mList;

    private NewOrderAdapter mAdapter;

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

        mList = new ArrayList<>();
    }

    private void loadData() {
        if (!mIsPrepared || !mIsFragmentVisible || mIsLoaded) {
            return;
        }
        mIsLoaded = true;

        mAdapter = new NewOrderAdapter(mContext, mList);
        mListView.setAdapter(mAdapter);

        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                testData2();
            } else {
                testData1();
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
        if (mCurrentTab == HistoryOrderActivity.CONFIRMED_TAB){
            order.order_state = OrderState.PAID_WAIT_RECEIPT;
        } else if (mCurrentTab == HistoryOrderActivity.FINISHED_TAB){
            order.order_state = OrderState.FINISHED;
        } else {
            order.order_state = OrderState.REFUND_SUCCESS;
        }
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
        if (mCurrentTab == HistoryOrderActivity.CONFIRMED_TAB){
            order.order_state = OrderState.PAID_WAIT_RECEIPT;
        } else if (mCurrentTab == HistoryOrderActivity.FINISHED_TAB){
            order.order_state = OrderState.FINISHED;
        } else {
            order.order_state = OrderState.APPLY_REFUND;
            order.chef_handle = 1;
        }
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

}
