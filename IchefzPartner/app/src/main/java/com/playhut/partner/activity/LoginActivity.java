package com.playhut.partner.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.utils.BitmapUtils;

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
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

}
