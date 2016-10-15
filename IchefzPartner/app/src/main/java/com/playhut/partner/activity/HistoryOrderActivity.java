package com.playhut.partner.activity;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.adapter.HistoryVpAdapter;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.debug.MyLog;
import com.playhut.partner.fragment.HistoryOrderFragment;
import com.playhut.partner.utils.PartnerUtils;
import com.playhut.partner.widget.PartnerTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class HistoryOrderActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private View mAnimView;

    private ViewPager mOrderVp;

    private List<Fragment> mFragmentList;

    public static final int CONFIRMED_TAB = 1;

    public static final int FINISHED_TAB = 2;

    public static final int REFUND_TAB = 3;

    private TextView[] mTabTextViews = new TextView[3];

    private int mCurrentTab = 1;

    private int tabWidth;

    private int marginLeft;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_history_order);
        mAnimView = findViewById(R.id.view_anim);
        mOrderVp = (ViewPager) findViewById(R.id.vp_order);
        mTabTextViews[0] = (TextView) findViewById(R.id.tv_confirm);
        mTabTextViews[1] = (TextView) findViewById(R.id.tv_finish);
        mTabTextViews[2] = (TextView) findViewById(R.id.tv_refund);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Order management");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                HistoryOrderActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        initAnimViewState();
        initFragment();
        mOrderVp.setOnPageChangeListener(this);
        mTabTextViews[0].setOnClickListener(this);
        mTabTextViews[1].setOnClickListener(this);
        mTabTextViews[2].setOnClickListener(this);
    }

    @Override
    protected void initLogic() {
        mOrderVp.setOffscreenPageLimit(1);
        mOrderVp.setAdapter(new HistoryVpAdapter(getSupportFragmentManager(), mFragmentList));
    }

    private void initAnimViewState() {
        tabWidth = GlobalConstants.SCREEN_WIDTH / 3;
        marginLeft = (tabWidth - PartnerUtils.dip2px(this, 70)) / 2;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mAnimView.getLayoutParams();
        params.setMargins(marginLeft, 0, 0, 0);
        mAnimView.setLayoutParams(params);
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(HistoryOrderFragment.getInstance(CONFIRMED_TAB));
        mFragmentList.add(HistoryOrderFragment.getInstance(FINISHED_TAB));
        mFragmentList.add(HistoryOrderFragment.getInstance(REFUND_TAB));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                if (mCurrentTab == CONFIRMED_TAB)
                    return;
                mOrderVp.setCurrentItem(CONFIRMED_TAB - 1, true);
                break;
            case R.id.tv_finish:
                if (mCurrentTab == FINISHED_TAB)
                    return;
                mOrderVp.setCurrentItem(FINISHED_TAB - 1, true);
                break;
            case R.id.tv_refund:
                if (mCurrentTab == REFUND_TAB)
                    return;
                mOrderVp.setCurrentItem(REFUND_TAB - 1, true);
                break;
        }
    }

    private void changeTabTextColor() {
        for (int i = 0; i < mTabTextViews.length; i++) {
            if (i == mCurrentTab - 1) {
                mTabTextViews[i].setTextColor(Color.parseColor("#0099CC"));
            } else {
                mTabTextViews[i].setTextColor(Color.parseColor("#666666"));
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        startLineAnim((mCurrentTab - 1) * 1.0f, position * 1.0f);
        mCurrentTab = position + 1;
        changeTabTextColor();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void startLineAnim(float fromX, float toX) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(fromX, toX);
        valueAnimator.setDuration(350);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mAnimView.getLayoutParams();
                params.setMargins((int) (marginLeft + value * tabWidth), 0, 0, 0);
                mAnimView.setLayoutParams(params);
            }
        });
    }

}
