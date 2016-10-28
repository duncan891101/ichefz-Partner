package com.playhut.partner.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.entity.RestaurantEntity;
import com.playhut.partner.mvp.presenter.IEditBankInfoPresent;
import com.playhut.partner.mvp.presenter.impl.EditBankInfoPresent;
import com.playhut.partner.mvp.view.EditBankInfoView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.PartnerTitleBar;

/**
 *
 */
public class BankInformationActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private CheckBox mStateCb;

    private Button mSaveBtn;

    private EditText mNameEt;

    private EditText mRoutingNumEt;

    private EditText mAccountNumEt;

    private EditText mTaxEt;

    private EditText mSSNEt;

    private static RestaurantEntity mRestaurantEntity;

    public static void actionIntent(Context context, RestaurantEntity restaurantEntity) {
        mRestaurantEntity = restaurantEntity;
        context.startActivity(new Intent(context, BankInformationActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_bank_information);
        mStateCb = (CheckBox) findViewById(R.id.cb_state);
        mSaveBtn = (Button) findViewById(R.id.btn_save);
        mNameEt = (EditText) findViewById(R.id.et_name);
        mRoutingNumEt = (EditText) findViewById(R.id.et_routing_num);
        mAccountNumEt = (EditText) findViewById(R.id.et_account_number);
        mTaxEt = (EditText) findViewById(R.id.et_tax);
        mSSNEt = (EditText) findViewById(R.id.et_ssn);
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
        mStateCb.setOnCheckedChangeListener(this);
        mSaveBtn.setOnClickListener(this);

        if (mRestaurantEntity != null) {
            String name = mRestaurantEntity.bank_name;
            String routingNum = mRestaurantEntity.bank_routing_num;
            String accountNum = mRestaurantEntity.bank_account_num;
            String tax = mRestaurantEntity.bank_tax;
            String ssn = mRestaurantEntity.bank_ssn;

            if (!TextUtils.isEmpty(name)) {
                mNameEt.setText(name);
                mNameEt.setSelection(name.length());
            }

            if (!TextUtils.isEmpty(routingNum)) {
                mRoutingNumEt.setText(routingNum);
                mRoutingNumEt.setSelection(routingNum.length());
            }

            if (!TextUtils.isEmpty(accountNum)) {
                mAccountNumEt.setText(accountNum);
                mAccountNumEt.setSelection(accountNum.length());
            }

            if (!TextUtils.isEmpty(tax)) {
                mTaxEt.setText(tax);
                mTaxEt.setSelection(tax.length());
            }

            if (!TextUtils.isEmpty(ssn)) {
                mSSNEt.setText(ssn);
                mSSNEt.setSelection(ssn.length());
            }
        }
    }

    @Override
    protected void initLogic() {

    }

    @Override
    public void onClick(View v) {
        checkInputInfo();
    }

    private void checkInputInfo() {
        String name = mNameEt.getText().toString().trim();
        String routingNum = mRoutingNumEt.getText().toString().trim();
        String accountNum = mAccountNumEt.getText().toString().trim();
        String tax = mTaxEt.getText().toString().trim();
        String ssn = mSSNEt.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            ToastUtils.show("Partner's name cannot be empty");
            return;
        }

        if (TextUtils.isEmpty(routingNum)) {
            ToastUtils.show("Routing number cannot be empty");
            return;
        }

        if (TextUtils.isEmpty(accountNum)) {
            ToastUtils.show("Account number cannot be empty");
            return;
        }

        if (TextUtils.isEmpty(tax)) {
            ToastUtils.show("Tax classification cannot be empty");
            return;
        }

        if (TextUtils.isEmpty(ssn)) {
            ToastUtils.show("SSN cannot be empty");
            return;
        }

        toEditBankInfo(name, routingNum, accountNum, tax, ssn);
    }

    private void toEditBankInfo(final String name, final String routingNum, final String accountNum, final String tax, final String ssn) {
        IEditBankInfoPresent present = new EditBankInfoPresent(this, new EditBankInfoView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_loading), true);
            }

            @Override
            public void loadSuccess(String info) {
                ToastUtils.show(info);
                BankInformationActivity.this.finish();
                if (mRestaurantEntity != null){
                    mRestaurantEntity.bank_name = name;
                    mRestaurantEntity.bank_routing_num = routingNum;
                    mRestaurantEntity.bank_account_num = accountNum;
                    mRestaurantEntity.bank_tax = tax;
                    mRestaurantEntity.bank_ssn = ssn;
                }
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
        present.edit(name, routingNum, accountNum, tax, ssn);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mSaveBtn.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

}
