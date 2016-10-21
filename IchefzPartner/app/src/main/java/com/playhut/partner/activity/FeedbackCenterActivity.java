package com.playhut.partner.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.mvp.presenter.IFeedbackCenterPresent;
import com.playhut.partner.mvp.presenter.impl.FeedbackCenterPresent;
import com.playhut.partner.mvp.view.FeedbackCenterView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.PartnerTitleBar;

/**
 *
 */
public class FeedbackCenterActivity extends BaseActivity implements View.OnClickListener{

    private EditText mFeedbackEt;

    private Button mSendBtn;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_feedback_center);
        mFeedbackEt = (EditText) findViewById(R.id.et_feedback);
        mSendBtn = (Button) findViewById(R.id.btn_send);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Feedback corner");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                FeedbackCenterActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        mSendBtn.setOnClickListener(this);
    }

    @Override
    protected void initLogic() {

    }

    @Override
    public void onClick(View v) {
        String content = mFeedbackEt.getText().toString().trim();
        if (TextUtils.isEmpty(content)){
            ToastUtils.show("Feedback content cannot be empty");
            return;
        }
        IFeedbackCenterPresent present = new FeedbackCenterPresent(this, new FeedbackCenterListener());
        present.send(content);
    }

    private class FeedbackCenterListener extends FeedbackCenterView {

        @Override
        public void startLoading() {
            showLoadingDialog(getString(R.string.loading_dialog_send), true);
        }

        @Override
        public void loadSuccess(String info) {
            ToastUtils.show(info);
            FeedbackCenterActivity.this.finish();
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
