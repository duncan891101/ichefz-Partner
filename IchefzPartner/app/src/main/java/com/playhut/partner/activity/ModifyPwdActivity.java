package com.playhut.partner.activity;

import android.graphics.Typeface;
import android.widget.EditText;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.widget.PartnerTitleBar;

/**
 *
 */
public class ModifyPwdActivity extends BaseActivity {

    private EditText mOldPwdEt;

    private EditText mNewPwdEt;

    private EditText mConfirmNewPwdEt;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_modify_pwd);
        mOldPwdEt = (EditText) findViewById(R.id.et_original_pwd);
        mNewPwdEt = (EditText) findViewById(R.id.et_new_pwd);
        mConfirmNewPwdEt = (EditText) findViewById(R.id.et_confirm_new_pwd);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Modify password");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                ModifyPwdActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        mOldPwdEt.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        mNewPwdEt.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        mConfirmNewPwdEt.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
    }

    @Override
    protected void initLogic() {

    }

}
