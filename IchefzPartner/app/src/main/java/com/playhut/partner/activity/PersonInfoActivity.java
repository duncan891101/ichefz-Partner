package com.playhut.partner.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.business.SelectCountryBusiness;
import com.playhut.partner.business.SelectDateBusiness;
import com.playhut.partner.constants.ChangeTypeConstants;
import com.playhut.partner.entity.FinanceDateEntity;
import com.playhut.partner.widget.PartnerTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PersonInfoActivity extends BaseActivity implements View.OnClickListener{

    private SelectDateBusiness mSelectDateBusiness;

    private TextView mBirthdayTv;

    private SelectCountryBusiness mSelectCountryBusiness;

    private List<FinanceDateEntity> mGenderList;

    private TextView mGenderTv;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_person_info);
        findViewById(R.id.rl_avatar).setOnClickListener(this);
        findViewById(R.id.rl_first_name).setOnClickListener(this);
        findViewById(R.id.rl_last_name).setOnClickListener(this);
        findViewById(R.id.rl_gender).setOnClickListener(this);
        findViewById(R.id.rl_birthday).setOnClickListener(this);
        findViewById(R.id.rl_phone_number).setOnClickListener(this);
        findViewById(R.id.rl_modify_pwd).setOnClickListener(this);
        findViewById(R.id.rl_describe_yourself).setOnClickListener(this);

        mBirthdayTv = (TextView) findViewById(R.id.tv_birthday);
        mGenderTv = (TextView) findViewById(R.id.tv_gender);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Restaurant settings");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                PersonInfoActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        mSelectDateBusiness = new SelectDateBusiness(this);
        mSelectCountryBusiness = new SelectCountryBusiness(this);
        mGenderList = new ArrayList<>();
    }

    @Override
    protected void initLogic() {
        FinanceDateEntity entity1 = new FinanceDateEntity();
        entity1.setIsSelect(true);
        entity1.setText("Male");
        mGenderList.add(entity1);

        FinanceDateEntity entity2 = new FinanceDateEntity();
        entity2.setText("Female");
        mGenderList.add(entity2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_avatar:

                break;
            case R.id.rl_first_name:
                Intent firstNameIntent = new Intent(this, ChangeInfoActivity.class);
                firstNameIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.FIRST_NAME);
                startActivity(firstNameIntent);
                break;
            case R.id.rl_last_name:
                Intent lastNameIntent = new Intent(this, ChangeInfoActivity.class);
                lastNameIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.LAST_NAME);
                startActivity(lastNameIntent);
                break;
            case R.id.rl_gender:
                mSelectCountryBusiness.showDialog(mGenderList, new SelectCountryBusiness.SelectCountryOkListener() {
                    @Override
                    public void onOk(String selectCountry) {
                        mGenderTv.setText(selectCountry);
                    }
                });
                break;
            case R.id.rl_birthday:
                mSelectDateBusiness.showDialog(true, new SelectDateBusiness.SelectDateOkListener() {
                    @Override
                    public void onOk(String selectYear, String selectMonth, String selectDay) {
                        mBirthdayTv.setText(selectYear + "-" + selectMonth + "-" + selectDay);
                    }
                });
                break;
            case R.id.rl_phone_number:
                Intent phoneNumberIntent = new Intent(this, ChangeInfoActivity.class);
                phoneNumberIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.PHONE_NUMBER);
                startActivity(phoneNumberIntent);
                break;
            case R.id.rl_modify_pwd:
                startActivity(new Intent(this, ModifyPwdActivity.class));
                break;
            case R.id.rl_describe_yourself:
                Intent descIntent = new Intent(this, ChangeInfoActivity.class);
                descIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.DESCRIBE_YOURSELF);
                startActivity(descIntent);
                break;
        }
    }
}
