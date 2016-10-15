package com.playhut.partner.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.constants.ChangeTypeConstants;
import com.playhut.partner.widget.PartnerTitleBar;

/**
 *
 */
public class ChangeInfoActivity extends BaseActivity implements TextWatcher, View.OnClickListener {

    private ImageView mCleanIv;

    private EditText mInputEt;

    public static final String CHANGE_TYPE_INTENT = "mChangeTypeIntent";

    public static final String ORIGINAL_CONTENT_INTENT = "mOriginalContentIntent";

    public int mChangeType;

    public String mOriginalContent;

    private PartnerTitleBar titleBar;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_change_info);
        mCleanIv = (ImageView) findViewById(R.id.iv_clean);
        mInputEt = (EditText) findViewById(R.id.et_input);
    }

    @Override
    protected void initTitleBar() {
        titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                ChangeInfoActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        mInputEt.addTextChangedListener(this);
        mCleanIv.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            mChangeType = intent.getIntExtra(CHANGE_TYPE_INTENT, 0);
            mOriginalContent = intent.getStringExtra(ORIGINAL_CONTENT_INTENT);
            if (!TextUtils.isEmpty(mOriginalContent)){
                mInputEt.setText(mOriginalContent);
                mInputEt.setSelection(mInputEt.length());
            }
            setTitleBar();
        }
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
        if (!TextUtils.isEmpty(inputText)) {
            mCleanIv.setVisibility(View.VISIBLE);
        } else {
            mCleanIv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        mInputEt.setText("");

    }

    private void setTitleBar(){
        switch (mChangeType){
            case ChangeTypeConstants.KITCHEN_NAME:
                titleBar.setCenterTvContent("Kitchen name");
                break;
            case ChangeTypeConstants.SUBTITLE:
                titleBar.setCenterTvContent("Subtitle");
                break;
            case ChangeTypeConstants.KITCHEN_SUMMERY:
                titleBar.setCenterTvContent("Kitchen summery");
                break;
            case ChangeTypeConstants.SHIP_DAY:
                titleBar.setCenterTvContent("Ship day");
                break;
            case ChangeTypeConstants.FIRST_NAME:
                titleBar.setCenterTvContent("First name");
                break;
            case ChangeTypeConstants.LAST_NAME:
                titleBar.setCenterTvContent("Last name");
                break;
            case ChangeTypeConstants.PHONE_NUMBER:
                titleBar.setCenterTvContent("Phone number");
                break;
            case ChangeTypeConstants.DESCRIBE_YOURSELF:
                titleBar.setCenterTvContent("Describe yourself");
                break;
        }
    }

}
