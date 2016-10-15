package com.playhut.partner.activity;

import android.view.View;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.business.SelectCountryBusiness;
import com.playhut.partner.entity.FinanceDateEntity;
import com.playhut.partner.widget.PartnerTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class KitchenAddressActivity extends BaseActivity implements View.OnClickListener {

    private SelectCountryBusiness mSelectCountryBusiness;

    private List<FinanceDateEntity> mCountryList;

    private TextView mCountryTv;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_kitchen_address);
        findViewById(R.id.rl_country).setOnClickListener(this);
        mCountryTv = (TextView) findViewById(R.id.tv_country);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Kitchen address");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                KitchenAddressActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        mSelectCountryBusiness = new SelectCountryBusiness(this);
        mCountryList = new ArrayList<>();
    }

    @Override
    protected void initLogic() {
        FinanceDateEntity entity1 = new FinanceDateEntity();
        entity1.setText("Chinese");
        entity1.setIsSelect(true);
        mCountryList.add(entity1);

        FinanceDateEntity entity2 = new FinanceDateEntity();
        entity2.setText("France");
        mCountryList.add(entity2);

        FinanceDateEntity entity3 = new FinanceDateEntity();
        entity3.setText("American");
        mCountryList.add(entity3);

        FinanceDateEntity entity4 = new FinanceDateEntity();
        entity4.setText("Japan");
        mCountryList.add(entity4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_country:
                mSelectCountryBusiness.showDialog(mCountryList, new SelectCountryBusiness.SelectCountryOkListener() {
                    @Override
                    public void onOk(String selectCountry) {
                        mCountryTv.setText(selectCountry);
                    }
                });
                break;
        }
    }

}
