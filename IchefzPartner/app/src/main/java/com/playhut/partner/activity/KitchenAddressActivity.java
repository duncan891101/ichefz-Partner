package com.playhut.partner.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.business.SelectCountryBusiness;
import com.playhut.partner.entity.FinanceDateEntity;
import com.playhut.partner.entity.FinanceListEntity;
import com.playhut.partner.entity.RestaurantEntity;
import com.playhut.partner.eventbus.EditKitchenAddressEB;
import com.playhut.partner.mvp.presenter.IEditKitchenAddressPresent;
import com.playhut.partner.mvp.presenter.impl.EditKitchenAddressPresent;
import com.playhut.partner.mvp.view.EditKitchenAddressView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.PartnerTitleBar;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 *
 */
public class KitchenAddressActivity extends BaseActivity implements View.OnClickListener {

    private SelectCountryBusiness mSelectCountryBusiness;

    private List<FinanceDateEntity> mCountryList;

    private TextView mCountryTv;

    private static RestaurantEntity mRestaurantEntity;

    private EditText mStreetEt;

    private EditText mAptEt;

    private EditText mCityEt;

    private EditText mStateEt;

    private EditText mZipCodeEt;

    private Button mSaveBtn;

    public static void actionIntent(Context context, RestaurantEntity restaurantEntity) {
        mRestaurantEntity = restaurantEntity;
        context.startActivity(new Intent(context, KitchenAddressActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_kitchen_address);
        findViewById(R.id.rl_country).setOnClickListener(this);
        mCountryTv = (TextView) findViewById(R.id.tv_country);
        mStreetEt = (EditText) findViewById(R.id.et_street);
        mAptEt = (EditText) findViewById(R.id.et_apt);
        mCityEt = (EditText) findViewById(R.id.et_city);
        mStateEt = (EditText) findViewById(R.id.et_state);
        mZipCodeEt = (EditText) findViewById(R.id.et_zip_code);
        mSaveBtn = (Button) findViewById(R.id.btn_save);
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
        mSaveBtn.setOnClickListener(this);
    }

    @Override
    protected void initLogic() {
        initCountry();
        initAddress();
    }

    private void initCountry() {
        if (mRestaurantEntity != null) {
            mCountryList.clear();
            List<String> allCountry = mRestaurantEntity.all_country;
            if (allCountry != null && allCountry.size() > 0) {
                for (int i = 0; i < allCountry.size(); i++) {
                    FinanceDateEntity entity = new FinanceDateEntity();
                    if (i == 0) {
                        entity.setIsSelect(true);
                    } else {
                        entity.setIsSelect(false);
                    }
                    entity.setText(allCountry.get(i));
                    mCountryList.add(entity);
                }
            }

            if (!TextUtils.isEmpty(mRestaurantEntity.country)) {
                mCountryTv.setText(mRestaurantEntity.country);
            }
        }
    }

    private void initAddress() {
        if (mRestaurantEntity != null) {
            if (!TextUtils.isEmpty(mRestaurantEntity.street_address)) {
                mStreetEt.setText(mRestaurantEntity.street_address);
            }

            if (!TextUtils.isEmpty(mRestaurantEntity.apt)) {
                mAptEt.setText(mRestaurantEntity.apt);
            }

            if (!TextUtils.isEmpty(mRestaurantEntity.city)) {
                mCityEt.setText(mRestaurantEntity.city);
            }

            if (!TextUtils.isEmpty(mRestaurantEntity.state)) {
                mStateEt.setText(mRestaurantEntity.state);
            }

            if (!TextUtils.isEmpty(mRestaurantEntity.zip_code)) {
                mZipCodeEt.setText(mRestaurantEntity.zip_code);
            }
        }
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
            case R.id.btn_save:
                checkInputInfo();
                break;
        }
    }

    private void checkInputInfo() {
        String country = mCountryTv.getText().toString();
        String street = mStreetEt.getText().toString().trim();
        String apt = mAptEt.getText().toString().trim();
        String city = mCityEt.getText().toString().trim();
        String state = mStateEt.getText().toString().trim();
        String zipCode = mZipCodeEt.getText().toString().trim();

        if (TextUtils.isEmpty(country)) {
            ToastUtils.show("Country cannot be empty");
            return;
        }

        if (TextUtils.isEmpty(street)) {
            ToastUtils.show("Street address cannot be empty");
            return;
        }

        if (TextUtils.isEmpty(city)) {
            ToastUtils.show("City cannot be empty");
            return;
        }

        if (TextUtils.isEmpty(state)) {
            ToastUtils.show("State cannot be empty");
            return;
        }

        if (TextUtils.isEmpty(zipCode)) {
            ToastUtils.show("ZIP Code cannot be empty");
            return;
        }

        editKitchenAddress(country, street, apt, city, state, zipCode);
    }

    private void editKitchenAddress(final String country, final String street, final String apt,
                                    final String city, final String state, final String zipCode) {
        IEditKitchenAddressPresent present = new EditKitchenAddressPresent(this, new EditKitchenAddressView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_loading), true);
            }

            @Override
            public void loadSuccess(String info) {
                ToastUtils.show(info);
                KitchenAddressActivity.this.finish();
                EditKitchenAddressEB editKitchenAddressEB = new EditKitchenAddressEB();
                editKitchenAddressEB.country = country;
                editKitchenAddressEB.street = street;
                editKitchenAddressEB.apt = apt;
                editKitchenAddressEB.city = city;
                editKitchenAddressEB.state = state;
                editKitchenAddressEB.zipCode = zipCode;
                EventBus.getDefault().post(editKitchenAddressEB);
            }

            @Override
            public void finishLoading() {
                dismissLoadingDialog();
            }

            @Override
            public void loadFailure(IchefzException exception) {
                ToastUtils.show(exception.getErrorMsg());
            }
        });
        present.edit(country, street, apt, city, state, zipCode);
    }

}
