package com.playhut.partner.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.base.PartnerApplication;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.entity.Account;
import com.playhut.partner.mvp.presenter.ISignUpPresent;
import com.playhut.partner.mvp.presenter.impl.SignUpPresent;
import com.playhut.partner.mvp.view.SignUpView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.AccountUtils;
import com.playhut.partner.utils.BitmapUtils;
import com.playhut.partner.utils.PartnerUtils;
import com.playhut.partner.utils.PwdUtils;
import com.playhut.partner.utils.ToastUtils;

import de.greenrobot.event.EventBus;

/**
 *
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackIv;

    private ImageView mBgIv;

    private EditText mFirstNameEt;

    private EditText mLastNameEt;

    private EditText mPhoneEt;

    private EditText mEmailEt;

    private EditText mPwdEt;

    private Button mNextBtn;

    @Override
    protected void initView() {
        setFullScreen();
        setContentView(R.layout.activity_register);
        mBgIv = (ImageView) findViewById(R.id.iv_bg);
        mBackIv = (ImageView) findViewById(R.id.iv_back);
        mFirstNameEt = (EditText) findViewById(R.id.et_first_name);
        mLastNameEt = (EditText) findViewById(R.id.et_last_name);
        mPhoneEt = (EditText) findViewById(R.id.et_phone);
        mEmailEt = (EditText) findViewById(R.id.et_email);
        mPwdEt = (EditText) findViewById(R.id.et_pwd);
        mNextBtn = (Button) findViewById(R.id.btn_next);
    }

    @Override
    protected void initTitleBar() {

    }

    @Override
    protected void initData() {
        mBackIv.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mPwdEt.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
    }

    @Override
    protected void initLogic() {
        int maxWidth = GlobalConstants.SCREEN_WIDTH;
        int maxHeight = GlobalConstants.SCREEN_HEIGHT;
        Bitmap bgBitmap = BitmapUtils.getBitmapFromRes(getResources(), R.mipmap.register_bg, maxWidth * maxHeight);
        mBgIv.setImageBitmap(bgBitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                RegisterActivity.this.finish();
                break;
            case R.id.btn_next:
                String firstName = mFirstNameEt.getText().toString().trim();
                String lastName = mLastNameEt.getText().toString().trim();
                String phone = mPhoneEt.getText().toString().trim();
                String email = mEmailEt.getText().toString().trim();
                String pwd = mPwdEt.getText().toString().trim();
                checkInputInfo(firstName, lastName, phone, email, pwd);
                break;
        }
    }

    private void checkInputInfo(String firstName, String lastName, String phone, String email, String pwd) {
        if (TextUtils.isEmpty(firstName)) {
            ToastUtils.show("First name cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(lastName)) {
            ToastUtils.show("Last name cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show("Phone number cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            ToastUtils.show("Email address cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.show("Password cannot be empty");
            return;
        }
        if (phone.length() != 10) {
            ToastUtils.show("Enter the valid phone number");
            return;
        }
        if (!PartnerUtils.isEmail(email)) {
            ToastUtils.show("Enter the valid email address");
            return;
        }
        if (pwd.length() < 6) {
            ToastUtils.show("Password length can not less than 6");
            return;
        }
        toSignUp(firstName, lastName, phone, email, pwd);
    }

    private void toSignUp(String firstName, String lastName, String phone, String email, String pwd) {
        ISignUpPresent present = new SignUpPresent(this, new SignUpView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_sign), false);
            }

            @Override
            public void loadSuccess(Account account) {
                PartnerApplication.mAccount = account;
                AccountUtils.saveAccount(account);
                ToastUtils.show("Sign up successfully");
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.SHOW_RESTAURANT_SETTING_INTENT, true);
                startActivity(intent);
                RegisterActivity.this.finish();
                EventBus.getDefault().post(LoginActivity.FINISH_LOGIN_EVENT_BUS_TAG);
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
        present.toSignUp(email, PwdUtils.getEncryptPwd(pwd), GlobalConstants.DEVICE_ID, firstName, lastName, phone);
    }

}
