package com.playhut.partner.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.asynctack.CleanCacheTask;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.base.PartnerApplication;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.mvp.presenter.ILogoutPresent;
import com.playhut.partner.mvp.presenter.impl.LogoutPresent;
import com.playhut.partner.mvp.view.LogoutView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.AccountUtils;
import com.playhut.partner.utils.DialogUtils;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.PartnerTitleBar;

import de.greenrobot.event.EventBus;

/**
 *
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView mVersionTv;

    private Button mLogoutBtn;

    private Dialog mLogoutDialog;

    private Dialog mContactUsDialog;

    public static final String CONTACT_PHONE_NUMBER = "10086";

    public static final String CONTACT_EMAIL = "1462050620@qq.com";

    @Override
    protected void initView() {
        setContentView(R.layout.activity_setting);
        mVersionTv = (TextView) findViewById(R.id.tv_version);
        mLogoutBtn = (Button) findViewById(R.id.btn_logout);

        findViewById(R.id.rl_feedback_center).setOnClickListener(this);
        findViewById(R.id.rl_clear_cache).setOnClickListener(this);
        findViewById(R.id.rl_contact_us).setOnClickListener(this);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Settings");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                SettingActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        mLogoutBtn.setOnClickListener(this);
    }

    @Override
    protected void initLogic() {
        mVersionTv.setText("V" + GlobalConstants.APP_VERSION);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_feedback_center:
                startActivity(new Intent(SettingActivity.this, FeedbackCenterActivity.class));
                break;
            case R.id.btn_logout:
                showLogoutDialog();
                break;
            case R.id.rl_clear_cache:
                toClearCache();
                break;
            case R.id.rl_contact_us:
                showContactUsDialog();
                break;
        }
    }

    private void showContactUsDialog() {
        if (mContactUsDialog == null || !mContactUsDialog.isShowing()) {
            mContactUsDialog = DialogUtils.showChooseAvatarDialog(this, R.layout.choose_avatar_dialog_layout, true, false);
        }
        TextView cancelTv = (TextView) mContactUsDialog.findViewById(R.id.tv_cancel);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissContactUsDialog();
            }
        });

        TextView phoneTv = (TextView) mContactUsDialog.findViewById(R.id.tv_take_photo);
        phoneTv.setText(CONTACT_PHONE_NUMBER);
        phoneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissContactUsDialog();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + CONTACT_PHONE_NUMBER));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        TextView emailTv = (TextView) mContactUsDialog.findViewById(R.id.tv_from_album);
        emailTv.setText(CONTACT_EMAIL);
        emailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissContactUsDialog();
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + CONTACT_EMAIL));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void dismissContactUsDialog() {
        if (mContactUsDialog != null && mContactUsDialog.isShowing()) {
            mContactUsDialog.dismiss();
            mContactUsDialog = null;
        }
    }

    private void toClearCache() {
        new CleanCacheTask(this).executeOnExecutor(PartnerApplication.mApp.getMultiThreadExecutor());
    }

    private void showLogoutDialog() {
        if (mLogoutDialog == null || !mLogoutDialog.isShowing()) {
            mLogoutDialog = DialogUtils.showConfirmDialog(this, R.layout.confirm_dialog_layout, true);
        }
        TextView titleTv = (TextView) mLogoutDialog.findViewById(R.id.tv_title);
        titleTv.setText("Logout");
        TextView textTv = (TextView) mLogoutDialog.findViewById(R.id.tv_text);
        textTv.setText("Are you sure to logout?");
        TextView confirmTv = (TextView) mLogoutDialog.findViewById(R.id.tv_confirm);
        confirmTv.setText("Logout");
        mLogoutDialog.findViewById(R.id.rl_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消
                dismissLogoutDialog();
            }
        });
        mLogoutDialog.findViewById(R.id.rl_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 退出登录
                dismissLogoutDialog();
                ILogoutPresent present = new LogoutPresent(SettingActivity.this, new LogoutListener());
                present.logout(GlobalConstants.DEVICE_ID);
            }
        });
    }

    private void dismissLogoutDialog() {
        if (mLogoutDialog != null && mLogoutDialog.isShowing()) {
            mLogoutDialog.dismiss();
            mLogoutDialog = null;
        }
    }

    private class LogoutListener extends LogoutView {

        @Override
        public void startLoading() {
            showLoadingDialog(getString(R.string.loading_dialog_logout), false);
        }

        @Override
        public void loadSuccess() {
            AccountUtils.deleteAccount();
            PartnerApplication.mAccount = null;
            startActivity(new Intent(SettingActivity.this, LoginActivity.class));
            SettingActivity.this.finish();
            EventBus.getDefault().post(MainActivity.FINISH_MAIN_EVENT_BUS_TAG);
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
