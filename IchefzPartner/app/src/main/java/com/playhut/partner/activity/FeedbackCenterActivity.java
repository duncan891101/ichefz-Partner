package com.playhut.partner.activity;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.widget.PartnerTitleBar;

/**
 *
 */
public class FeedbackCenterActivity extends BaseActivity{

    @Override
    protected void initView() {
        setContentView(R.layout.activity_feedback_center);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Feedback corner");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                FeedbackCenterActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initLogic() {

    }

}
