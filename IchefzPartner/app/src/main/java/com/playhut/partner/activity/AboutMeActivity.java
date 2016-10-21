package com.playhut.partner.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.eventbus.EditRestaurantInfoEB;
import com.playhut.partner.mvp.presenter.IEditRestaurantInfoPresent;
import com.playhut.partner.mvp.presenter.impl.EditRestaurantInfoPresent;
import com.playhut.partner.mvp.view.EditRestaurantInfoView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.PartnerTitleBar;

import de.greenrobot.event.EventBus;

/**
 *
 */
public class AboutMeActivity extends BaseActivity implements TextWatcher, View.OnClickListener {

    private EditText mAboutMeEt;

    private TextView mNumTv;

    private Button mSaveBtn;

    public static final String ORIGINAL_TEXT_INTENT = "mOriginalAboutMeIntent";

    @Override
    protected void initView() {
        setContentView(R.layout.activity_about_me);
        mAboutMeEt = (EditText) findViewById(R.id.et_about_me);
        mNumTv = (TextView) findViewById(R.id.tv_num);
        mSaveBtn = (Button) findViewById(R.id.btn_save);
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
        mSaveBtn.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            String originalText = intent.getStringExtra(ORIGINAL_TEXT_INTENT);
            if (!TextUtils.isEmpty(originalText)) {
                mAboutMeEt.setText(originalText);
            }
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
            mNumTv.setText(String.valueOf(inputText.length()));
        } else {
            mNumTv.setText("0");
        }
    }

    @Override
    public void onClick(View v) {
        String inputStr = mAboutMeEt.getText().toString().trim();
        if (!TextUtils.isEmpty(inputStr)) {
            editRestaurantInfo(inputStr, 6);
        } else {
            ToastUtils.show("Input text cannot be empty");
        }
    }

    private void editRestaurantInfo(final String content, final int changeType) {
        IEditRestaurantInfoPresent present = new EditRestaurantInfoPresent(this, new EditRestaurantInfoView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_loading), true);
            }

            @Override
            public void loadSuccess() {
                AboutMeActivity.this.finish();
                EditRestaurantInfoEB editRestaurantInfoEB = new EditRestaurantInfoEB();
                editRestaurantInfoEB.changeType = changeType;
                editRestaurantInfoEB.changeContent = content;
                EventBus.getDefault().post(editRestaurantInfoEB);
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
        present.edit(content, changeType);
    }

}
