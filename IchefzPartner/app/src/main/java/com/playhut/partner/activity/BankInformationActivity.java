package com.playhut.partner.activity;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.widget.PartnerTitleBar;

/**
 *
 */
public class BankInformationActivity extends BaseActivity{

    @Override
    protected void initView() {
        setContentView(R.layout.activity_bank_information);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Bank information");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                BankInformationActivity.this.finish();
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
