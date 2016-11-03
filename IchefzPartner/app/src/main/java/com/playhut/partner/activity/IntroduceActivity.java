package com.playhut.partner.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.playhut.partner.R;
import com.playhut.partner.adapter.IntroduceVpAdapter;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.constants.IntroduceConstants;
import com.playhut.partner.utils.BitmapUtils;
import com.playhut.partner.utils.PartnerUtils;
import com.playhut.partner.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class IntroduceActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;

    private List<View> mList;

    private int[] mIntroduceImages = {R.mipmap.introduce1, R.mipmap.introduce2, R.mipmap.introduce3, R.mipmap.introduce4};

    private LinearLayout mContainerLayout;

    @Override
    protected void initView() {
        setFullScreen();
        setContentView(R.layout.activity_introduce);
        mViewPager = (ViewPager) findViewById(R.id.vp_introduce);
        mContainerLayout = (LinearLayout) findViewById(R.id.ll_container);
    }

    @Override
    protected void initTitleBar() {

    }

    @Override
    protected void initData() {
        mViewPager.setOnPageChangeListener(this);
        mList = new ArrayList<>();
        int screenWidth = GlobalConstants.SCREEN_WIDTH;
        int screenHeight = GlobalConstants.SCREEN_HEIGHT;
        for (int i = 0; i < mIntroduceImages.length; i++) {
            View view = View.inflate(this, R.layout.introduce_vp_view, null);
            ImageView iv = (ImageView) view.findViewById(R.id.iv_img);
            Button btn = (Button) view.findViewById(R.id.btn_enter);
            Bitmap bitmap = BitmapUtils.getBitmapFromRes(getResources(), mIntroduceImages[i], screenWidth * screenHeight);
            iv.setImageBitmap(bitmap);
            if (i == mIntroduceImages.length - 1) {
                btn.setVisibility(View.VISIBLE);
                btn.setOnClickListener(this);
            } else {
                btn.setVisibility(View.GONE);
            }
            mList.add(view);


            View dot = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(PartnerUtils.dip2px(this, 9), PartnerUtils.dip2px(this, 9));
            params.leftMargin = PartnerUtils.dip2px(this, 9);
            dot.setLayoutParams(params);
            if (i == 0) {
                dot.setBackgroundResource(R.drawable.introduce_dot_check_shape);
            } else {
                dot.setBackgroundResource(R.drawable.introduce_dot_uncheck_shape);
            }
            mContainerLayout.addView(dot);
        }
    }

    @Override
    protected void initLogic() {
        mViewPager.setAdapter(new IntroduceVpAdapter(mList));
    }

    @Override
    public void onClick(View v) {
        SPUtils.saveBooleanWithOther(IntroduceConstants.IS_FIRST_TO_APP, true);
        startActivity(new Intent(this, LoginActivity.class));
        IntroduceActivity.this.finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int childCount = mContainerLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mContainerLayout.getChildAt(i);
            if (i == position) {
                view.setBackgroundResource(R.drawable.introduce_dot_check_shape);
            } else {
                view.setBackgroundResource(R.drawable.introduce_dot_uncheck_shape);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
