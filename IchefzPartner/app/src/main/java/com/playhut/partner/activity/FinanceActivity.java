package com.playhut.partner.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.business.SelectDateBusiness;
import com.playhut.partner.entity.FinanceListEntity;
import com.playhut.partner.entity.IncomeEntity;
import com.playhut.partner.mvp.presenter.IIncomeOrderPresent;
import com.playhut.partner.mvp.presenter.IIncomePresent;
import com.playhut.partner.mvp.presenter.impl.IncomeOrderPresent;
import com.playhut.partner.mvp.presenter.impl.IncomePresent;
import com.playhut.partner.mvp.view.IncomeOrderView;
import com.playhut.partner.mvp.view.IncomeView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.ui.FinanceItemView;
import com.playhut.partner.utils.PartnerUtils;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.AutoLoadScrollView;
import com.playhut.partner.widget.FinanceChartView;
import com.playhut.partner.widget.PartnerTitleBar;

import java.util.List;

/**
 *
 */
public class FinanceActivity extends BaseActivity implements View.OnClickListener, AutoLoadScrollView.LoadingListener{

    private FinanceChartView mFinanceChartView;

    private AutoLoadScrollView mScrollView;

    private ImageView mDateIv;

    private TextView mDateTv;

    private String mCurrentYear;

    private String mCurrentMonth;

    private SelectDateBusiness mSelectDateBusiness;

    private TextView mAccountTv;

    public static final int PAGE_SIZE = 12;

    private int mPage = 1; // 当前所处的页

    private int mTotalPage = 1; // 总页数

    private TextView mMonthTotalTv;

    private LinearLayout mContainerLayout;

    public static final int SHOW_FOOTER_VIEW_LIMIT = 6; // 当所有item的总数小于这个数时，哪怕是最后一页也不显示footer view

    private TextView mNoItemTv;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_finance);
        mScrollView = (AutoLoadScrollView) findViewById(R.id.sv_auto_load);

        View contentView = View.inflate(this, R.layout.finance_content_layout, null);
        mScrollView.addContentView(contentView);

        mFinanceChartView = (FinanceChartView) contentView.findViewById(R.id.cv_finance);
        mDateIv = (ImageView) contentView.findViewById(R.id.iv_date);
        mDateTv = (TextView) contentView.findViewById(R.id.tv_date);
        mAccountTv = (TextView) contentView.findViewById(R.id.tv_account);
        mMonthTotalTv = (TextView) contentView.findViewById(R.id.tv_month_total);
        mNoItemTv = (TextView) contentView.findViewById(R.id.tv_no_item);
        mContainerLayout = (LinearLayout) contentView.findViewById(R.id.ll_container);
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
        mDateIv.setOnClickListener(this);
        mScrollView.setLoadingListener(this);

        String[] dates = PartnerUtils.getCurrentDate();
        mCurrentYear = dates[0];
        mCurrentMonth = dates[1];
        mDateTv.setText(mCurrentYear + "/" + mCurrentMonth);
    }

    @Override
    protected void initLogic() {
        getIncome();
    }

    private void getIncome() {
        IIncomePresent present = new IncomePresent(this, new IncomeView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_loading), true);
            }

            @Override
            public void loadSuccess(IncomeEntity entity) {
                mAccountTv.setText("$" + entity.no_account_amont);
                List<String> list = entity.income_list;
                if (list != null && list.size() > 0) {
                    float[] data = new float[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        String monthAccount = list.get(i);
                        data[i] = Float.parseFloat(monthAccount);
                    }
                    mFinanceChartView.setData(data);
                }
            }

            @Override
            public void finishLoading() {
                getIncomeOrder();
            }

            @Override
            public void loadFailure(IchefzException exception) {
                ToastUtils.show(exception.getErrorMsg());
            }
        });
        present.getIncome();
    }

    private void getIncomeOrder() {
        IIncomeOrderPresent present = new IncomeOrderPresent(this, new IncomeOrderView() {
            @Override
            public void loadSuccess(FinanceListEntity entity) {
                mMonthTotalTv.setText("$" + entity.total_amount);

                mPage++;
                int total = entity.total;
                if (total % PAGE_SIZE == 0) {
                    mTotalPage = total / PAGE_SIZE;
                } else {
                    mTotalPage = (total / PAGE_SIZE) + 1;
                }
                mScrollView.setAutoLoadEnable(true);
                if (mPage <= mTotalPage) {
                    mScrollView.setHasMoreData(true, true);
                } else {
                    if (total < SHOW_FOOTER_VIEW_LIMIT) {
                        mScrollView.setHasMoreData(false, false);
                    } else {
                        mScrollView.setHasMoreData(false, true);
                    }
                }
                if (mPage == 2){
                    mContainerLayout.removeAllViews();
                }
                if (entity.order_list != null && entity.order_list.size() > 0) {
                    mNoItemTv.setVisibility(View.GONE);
                    addDataToLayout(entity.order_list);
                } else {
                    mNoItemTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void finishLoading() {
                dismissLoadingDialog();
                mScrollView.onLoadComplete();
            }

            @Override
            public void loadFailure(IchefzException exception) {
                if (mPage == 1){
                    ToastUtils.show(exception.getErrorMsg());
                } else {
                    mScrollView.setLoadException(exception);
                }
            }
        });
        present.getList(mCurrentYear + "-" + mCurrentMonth, mPage, PAGE_SIZE);
    }

    private void addDataToLayout(List<FinanceListEntity.OrderList> list){
        for (FinanceListEntity.OrderList order : list){
            FinanceItemView item = new FinanceItemView(this);
            item.setImageView(order.img);
            item.setOrderNumber(order.order_number);
            item.setTime(order.time);
            item.setPrice(order.price);
            mContainerLayout.addView(item);
        }
    }

    @Override
    public void onLoad() {
        getIncomeOrder();
    }

    @Override
    public void onClick(View v) {
        mSelectDateBusiness.showDialog(false, new SelectDateBusiness.SelectDateOkListener() {
            @Override
            public void onOk(String selectYear, String selectMonth, String selectDay) {
                mCurrentYear = selectYear;
                mCurrentMonth = selectMonth;
                mDateTv.setText(selectYear + "/" + selectMonth);

                mPage = 1;
                mTotalPage = 1;
                showLoadingDialog(getString(R.string.loading_dialog_loading), true);
                getIncomeOrder();
            }
        });
    }

}
