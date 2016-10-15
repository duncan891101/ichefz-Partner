package com.playhut.partner.activity;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.adapter.HistoryVpAdapter;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.fragment.MyMenuPackFragment;
import com.playhut.partner.fragment.MyMenuSetFragment;
import com.playhut.partner.utils.PartnerUtils;
import com.playhut.partner.utils.PopupWindowUtils;
import com.playhut.partner.widget.PartnerTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MyMenuActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private View mAnimView;

    private ViewPager mMenuVp;

    private List<Fragment> mFragmentList;

    public static final int SETS_TAB = 1;

    public static final int PACKS_TAB = 2;

    private TextView[] mTabTextViews = new TextView[2];

    private int mCurrentTab = 1;

    private int tabWidth;

    private int marginLeft;

    private PopupWindow mPopupWindow;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_menu);
        mAnimView = findViewById(R.id.view_anim);
        mMenuVp = (ViewPager) findViewById(R.id.vp_menu);
        mTabTextViews[0] = (TextView) findViewById(R.id.tv_set);
        mTabTextViews[1] = (TextView) findViewById(R.id.tv_pack);
    }

    @Override
    protected void initTitleBar() {
        final PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("My menu");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                MyMenuActivity.this.finish();
            }
        });
        titleBar.setRightIvVisiable(true);
        titleBar.setRightIvContent(R.mipmap.my_menu_add);
        titleBar.setRightIvListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                showPopupWindow(titleBar.getRightIv());
            }
        });
    }

    @Override
    protected void initData() {
        initAnimViewState();
        initFragment();
        mMenuVp.setOnPageChangeListener(this);
        mTabTextViews[0].setOnClickListener(this);
        mTabTextViews[1].setOnClickListener(this);
    }

    @Override
    protected void initLogic() {
        mMenuVp.setOffscreenPageLimit(1);
        mMenuVp.setAdapter(new HistoryVpAdapter(getSupportFragmentManager(), mFragmentList));
    }

    private void initAnimViewState() {
        tabWidth = GlobalConstants.SCREEN_WIDTH / 2;
        marginLeft = (tabWidth - PartnerUtils.dip2px(this, 50)) / 2;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mAnimView.getLayoutParams();
        params.setMargins(marginLeft, 0, 0, 0);
        mAnimView.setLayoutParams(params);
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new MyMenuSetFragment());
        mFragmentList.add(new MyMenuPackFragment());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_set:
                if (mCurrentTab == SETS_TAB)
                    return;
                mMenuVp.setCurrentItem(SETS_TAB - 1, true);
                break;
            case R.id.tv_pack:
                if (mCurrentTab == PACKS_TAB)
                    return;
                mMenuVp.setCurrentItem(PACKS_TAB - 1, true);
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

    private void showPopupWindow(View parentView) {
        if (mPopupWindow == null || !mPopupWindow.isShowing()) {
            View view = View.inflate(this, R.layout.my_menu_add_pop_layout, null);
            PopupWindowUtils.LocationParams params = new PopupWindowUtils.LocationParams();
            params.showAtLocation = false;
            params.parentView = parentView;
            params.offY = -PartnerUtils.dip2px(this, 2);
            params.offX = 0;
            mPopupWindow = PopupWindowUtils.show(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                    params, -1);
            TextView addPackTv = (TextView) view.findViewById(R.id.tv_add_pack);
            TextView addSetTv = (TextView) view.findViewById(R.id.tv_add_set);
            addPackTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissPopupWindow();

                }
            });
            addSetTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissPopupWindow();

                }
            });
        }
    }

    private void dismissPopupWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

}
