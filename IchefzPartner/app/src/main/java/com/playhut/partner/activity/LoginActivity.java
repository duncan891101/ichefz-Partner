package com.playhut.partner.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.base.PartnerApplication;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.entity.Account;
import com.playhut.partner.mvp.presenter.ILoginPresent;
import com.playhut.partner.mvp.presenter.impl.LoginPresent;
import com.playhut.partner.mvp.view.LoginView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.AccountUtils;
import com.playhut.partner.utils.BitmapUtils;
import com.playhut.partner.utils.PartnerUtils;
import com.playhut.partner.utils.PwdUtils;
import com.playhut.partner.utils.ToastUtils;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 *
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBgIv;

    private EditText mPwdET;

    private EditText mEmailET;

    private TextView mForgotPwdTv;

    private Button mLoginBtn;

    private TextView mRegisterTv;

    public static final String FINISH_LOGIN_EVENT_BUS_TAG = "mFinishLoginEventBusTag";

    @Override
    protected void initView() {
        setFullScreen();
        setContentView(R.layout.activity_login);
        mBgIv = (ImageView) findViewById(R.id.iv_bg);
        mPwdET = (EditText) findViewById(R.id.et_pwd);
        mEmailET = (EditText) findViewById(R.id.et_email);
        mForgotPwdTv = (TextView) findViewById(R.id.tv_forget);
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        mRegisterTv = (TextView) findViewById(R.id.tv_register);
    }

    @Override
    protected void initTitleBar() {

    }

    @Override
    protected void initData() {
        mForgotPwdTv.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        mRegisterTv.setOnClickListener(this);

        mPwdET.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void initLogic() {
        int maxWidth = GlobalConstants.SCREEN_WIDTH;
        int maxHeight = GlobalConstants.SCREEN_HEIGHT;
        Bitmap bgBitmap = BitmapUtils.getBitmapFromRes(getResources(), R.mipmap.login_bg, maxWidth * maxHeight);
        mBgIv.setImageBitmap(bgBitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget:
                startActivity(new Intent(this, ForgetPwdActivity.class));
                break;
            case R.id.btn_login:
                String email = mEmailET.getText().toString().trim();
                String pwd = mPwdET.getText().toString().trim();
                checkInputInfo(email, pwd);
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private void checkInputInfo(String email, String pwd) {
        if (TextUtils.isEmpty(email)) {
            ToastUtils.show("Email address cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.show("Password cannot be empty");
            return;
        }
        if (!PartnerUtils.isEmail(email)) {
            ToastUtils.show("Enter the valid email address");
            return;
        }
        toLogin(email, pwd);
    }

    private void toLogin(String email, String pwd) {
        ILoginPresent present = new LoginPresent(this, new LoginViewListener());
        present.toLogin(email, PwdUtils.getEncryptPwd(pwd), GlobalConstants.DEVICE_ID);
    }

    private class LoginViewListener extends LoginView {
        @Override
        public void startLoading() {
            showLoadingDialog(getString(R.string.loading_dialog_login), false);
        }

        @Override
        public void loadSuccess(Account account) {
            PartnerApplication.mAccount = account;
            AccountUtils.saveAccount(account);
            ToastUtils.show("Log in successfully");
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            LoginActivity.this.finish();
        }

        @Override
        public void finishLoading() {
            dismissLoadingDialog();
        }

        @Override
        public void loadFailure(IchefzException exception) {
            ToastUtils.show(exception.getErrorMsg());
        }
    }

    @Subscribe
    public void onEventMainThread(String tag) {
        if (!TextUtils.isEmpty(tag) && tag.equals(FINISH_LOGIN_EVENT_BUS_TAG)) {
            LoginActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
