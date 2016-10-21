package com.playhut.partner.activity;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.mvp.presenter.IForgotPwdPresent;
import com.playhut.partner.mvp.presenter.impl.ForgotPwdPresent;
import com.playhut.partner.mvp.view.ForgotPwdView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.BitmapUtils;
import com.playhut.partner.utils.PartnerUtils;
import com.playhut.partner.utils.ToastUtils;

/**
 *
 */
public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackIv;

    private EditText mEmailEt;

    private Button mSubmitBtn;

    private ImageView mBgIv;

    @Override
    protected void initView() {
        setFullScreen();
        setContentView(R.layout.activity_forget_pwd);
        mBgIv = (ImageView) findViewById(R.id.iv_bg);
        mBackIv = (ImageView) findViewById(R.id.iv_back);
        mEmailEt = (EditText) findViewById(R.id.et_forget_pwd);
        mSubmitBtn = (Button) findViewById(R.id.btn_submit);
    }

    @Override
    protected void initTitleBar() {

    }

    @Override
    protected void initData() {
        mBackIv.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);
    }

    @Override
    protected void initLogic() {
        int maxWidth = GlobalConstants.SCREEN_WIDTH;
        int maxHeight = GlobalConstants.SCREEN_HEIGHT;
        Bitmap bgBitmap = BitmapUtils.getBitmapFromRes(getResources(), R.mipmap.forgot_pwd_bg, maxWidth * maxHeight);
        mBgIv.setImageBitmap(bgBitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                ForgetPwdActivity.this.finish();
                break;
            case R.id.btn_submit:
                String email = mEmailEt.getText().toString().trim();
                checkInputInfo(email);
                break;
        }
    }

    private void checkInputInfo(String email) {
        if (TextUtils.isEmpty(email)) {
            ToastUtils.show("Email address cannot be empty");
            return;
        }
        if (!PartnerUtils.isEmail(email)) {
            ToastUtils.show("Enter the valid email address");
            return;
        }
        submitForgotPwd(email);
    }

    private void submitForgotPwd(String email) {
        IForgotPwdPresent present = new ForgotPwdPresent(this, new ForgotPwdListener());
        present.forgotPwd(email);
    }

    private class ForgotPwdListener extends ForgotPwdView {
        @Override
        public void startLoading() {
            showLoadingDialog(getString(R.string.loading_dialog_send), false);
        }

        @Override
        public void loadSuccess(String info) {
            ToastUtils.show(info);
            ForgetPwdActivity.this.finish();
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

}
