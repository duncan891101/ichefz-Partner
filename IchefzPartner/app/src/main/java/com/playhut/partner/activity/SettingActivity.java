package com.playhut.partner.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.widget.PartnerTitleBar;

/**
 *
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener{

    private TextView mVersionTv;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_setting);
        mVersionTv = (TextView) findViewById(R.id.tv_version);
        findViewById(R.id.rl_feedback_center).setOnClickListener(this);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Settings");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                SettingActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initLogic() {
        mVersionTv.setText("V" + GlobalConstants.APP_VERSION);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_feedback_center:
                startActivity(new Intent(SettingActivity.this, FeedbackCenterActivity.class));
                break;
        }
    }

}
