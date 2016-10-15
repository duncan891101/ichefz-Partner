package com.playhut.partner.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.utils.BitmapUtils;

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
        mEmailEt = (EditText) findViewById(R.id.et_email);
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

                break;
        }
    }
}
