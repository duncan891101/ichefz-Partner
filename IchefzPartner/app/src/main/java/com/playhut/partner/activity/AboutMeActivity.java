package com.playhut.partner.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.widget.PartnerTitleBar;

/**
 *
 */
public class AboutMeActivity extends BaseActivity implements TextWatcher{

    private EditText mAboutMeEt;

    private TextView mNumTv;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_about_me);
        mAboutMeEt = (EditText) findViewById(R.id.et_about_me);
        mNumTv = (TextView) findViewById(R.id.tv_num);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("About me");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                AboutMeActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        mAboutMeEt.addTextChangedListener(this);
    }

    @Override
    protected void initLogic() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String inputText = s.toString();
        if (!TextUtils.isEmpty(inputText)){
            mNumTv.setText(String.valueOf(inputText.length()));
        } else {
            mNumTv.setText("0");
        }
    }

}
