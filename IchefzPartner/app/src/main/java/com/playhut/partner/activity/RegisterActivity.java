package com.playhut.partner.activity;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.utils.BitmapUtils;

/**
 *
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackIv;

    private ImageView mBgIv;

    private EditText mPwdEt;

    @Override
    protected void initView() {
        setFullScreen();
        setContentView(R.layout.activity_register);
        mBgIv = (ImageView) findViewById(R.id.iv_bg);
        mBackIv = (ImageView) findViewById(R.id.iv_back);
        mPwdEt = (EditText) findViewById(R.id.et_pwd);
    }

    @Override
    protected void initTitleBar() {

    }

    @Override
    protected void initData() {
        mBackIv.setOnClickListener(this);
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
        }
    }

}
