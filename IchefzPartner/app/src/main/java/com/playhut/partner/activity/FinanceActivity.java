package com.playhut.partner.activity;

import android.app.Dialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.adapter.FinanceAdapter;
import com.playhut.partner.adapter.FinanceWheelViewAdapter;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.business.SelectDateBusiness;
import com.playhut.partner.entity.FinanceDateEntity;
import com.playhut.partner.entity.FinanceListEntity;
import com.playhut.partner.pullrefresh.PullToRefreshBase;
import com.playhut.partner.pullrefresh.PullToRefreshScrollView;
import com.playhut.partner.utils.DialogUtils;
import com.playhut.partner.utils.PartnerUtils;
import com.playhut.partner.widget.FinanceChartView;
import com.playhut.partner.widget.MeasureListView;
import com.playhut.partner.widget.PartnerTitleBar;
import com.playhut.partner.widget.TosAdapterView;
import com.playhut.partner.widget.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 */
public class FinanceActivity extends BaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener<ScrollView> {

    private FinanceChartView mFinanceChartView;

    private MeasureListView mMeasureListView;

    private FinanceAdapter mAdapter;

    private List<FinanceListEntity.OrderList> mList;

    private ImageView mDateIv;

    private TextView mDateTv;

    private String mCurrentYear;

    private String mCurrentMonth;

    private PullToRefreshScrollView mPullToRefreshScrollView;

    private ScrollView mScrollView;

    private SelectDateBusiness mSelectDateBusiness;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_finance);
        mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.sv_pull_refresh);
        mScrollView = mPullToRefreshScrollView.getRefreshableView();

        View contentView = View.inflate(this, R.layout.finance_content_layout, null);
        mScrollView.addView(contentView);

        mFinanceChartView = (FinanceChartView) contentView.findViewById(R.id.cv_finance);
        mMeasureListView = (MeasureListView) contentView.findViewById(R.id.lv_measure);
        mDateIv = (ImageView) contentView.findViewById(R.id.iv_date);
        mDateTv = (TextView) contentView.findViewById(R.id.tv_date);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Finance");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                FinanceActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        mSelectDateBusiness = new SelectDateBusiness(this);
        mList = new ArrayList<>();
        mDateIv.setOnClickListener(this);
        mPullToRefreshScrollView.setOnRefreshListener(this);

        String[] dates = PartnerUtils.getCurrentDate();
        mCurrentYear = dates[0];
        mCurrentMonth = dates[1];
        mDateTv.setText(mCurrentYear + "/" + mCurrentMonth);
    }

    @Override
    protected void initLogic() {
        float[] datas = {125.02f, 22.8f, 12.86f, 78.00f, 138.04f, 45.02f, 125.02f, 22.8f, 12.86f, 78.00f, 138.04f, 45.02f};
        mFinanceChartView.setData(datas);

        mMeasureListView.setDividerHeight(0);
        mMeasureListView.setFocusable(false);
        mAdapter = new FinanceAdapter(this, mList);
        mMeasureListView.setAdapter(mAdapter);

        for (int i = 0; i < 10; i++) {
            FinanceListEntity.OrderList entity = new FinanceListEntity.OrderList();
            entity.img = "drawable://" + R.mipmap.test1;
            entity.order_number = "12236654989";
            entity.time = "2016-10-08 14:51PM";
            entity.price = String.valueOf(12.05f + i);
            mList.add(entity);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

    }

    @Override
    public void onClick(View v) {
        mSelectDateBusiness.showDialog(false, new SelectDateBusiness.SelectDateOkListener() {
            @Override
            public void onOk(String selectYear, String selectMonth, String selectDay) {
                mDateTv.setText(selectYear + "/" + selectMonth);
            }
        });
    }

}
