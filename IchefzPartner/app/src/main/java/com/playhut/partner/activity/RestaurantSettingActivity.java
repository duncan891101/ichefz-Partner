package com.playhut.partner.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.business.SelectCountryBusiness;
import com.playhut.partner.constants.ChangeTypeConstants;
import com.playhut.partner.entity.FinanceDateEntity;
import com.playhut.partner.widget.PartnerTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RestaurantSettingActivity extends BaseActivity implements View.OnClickListener{

    private List<FinanceDateEntity> mCountryList;

    private SelectCountryBusiness mSelectCountryBusiness;

    private TextView mFoodTypeTv;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_restaurant_setting);
        findViewById(R.id.rl_person_info).setOnClickListener(this);
        findViewById(R.id.rl_kitchen_name).setOnClickListener(this);
        findViewById(R.id.rl_subtitle).setOnClickListener(this);
        findViewById(R.id.rl_food_type).setOnClickListener(this);
        findViewById(R.id.rl_kitchen_summery).setOnClickListener(this);
        findViewById(R.id.rl_ship_day).setOnClickListener(this);
        findViewById(R.id.rl_kitchen_address).setOnClickListener(this);
        findViewById(R.id.rl_bank_info).setOnClickListener(this);
        findViewById(R.id.rl_about_me).setOnClickListener(this);

        mFoodTypeTv = (TextView) findViewById(R.id.tv_food_type);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Restaurant Settings");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                RestaurantSettingActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        mCountryList = new ArrayList<>();
        mSelectCountryBusiness = new SelectCountryBusiness(this);
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
        switch (v.getId()){
            case R.id.rl_person_info:
                startActivity(new Intent(this, PersonInfoActivity.class));
                break;
            case R.id.rl_kitchen_name:
                Intent kitchenNameIntent = new Intent(this, ChangeInfoActivity.class);
                kitchenNameIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.KITCHEN_NAME);
                kitchenNameIntent.putExtra(ChangeInfoActivity.ORIGINAL_CONTENT_INTENT, "My kitchen");
                startActivity(kitchenNameIntent);
                break;
            case R.id.rl_subtitle:
                Intent subtitleIntent = new Intent(this, ChangeInfoActivity.class);
                subtitleIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.SUBTITLE);
                startActivity(subtitleIntent);
                break;
            case R.id.rl_food_type:
                mSelectCountryBusiness.showDialog(mCountryList, new SelectCountryBusiness.SelectCountryOkListener() {
                    @Override
                    public void onOk(String selectCountry) {
                        mFoodTypeTv.setText(selectCountry);
                    }
                });
                break;
            case R.id.rl_kitchen_summery:
                Intent kitchenSummeryIntent = new Intent(this, ChangeInfoActivity.class);
                kitchenSummeryIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.KITCHEN_SUMMERY);
                startActivity(kitchenSummeryIntent);
                break;
            case R.id.rl_ship_day:
                Intent shipDayIntent = new Intent(this, ChangeInfoActivity.class);
                shipDayIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.SHIP_DAY);
                startActivity(shipDayIntent);
                break;
            case R.id.rl_kitchen_address:
                startActivity(new Intent(this, KitchenAddressActivity.class));
                break;
            case R.id.rl_bank_info:
                startActivity(new Intent(this, BankInformationActivity.class));
                break;
            case R.id.rl_about_me:
                startActivity(new Intent(this, AboutMeActivity.class));
                break;
        }
    }

}
