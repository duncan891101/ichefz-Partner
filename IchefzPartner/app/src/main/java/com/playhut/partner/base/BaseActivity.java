package com.playhut.partner.base;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.utils.AccountUtils;
import com.playhut.partner.utils.DialogUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Activity基类
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    protected Dialog mLoadingDialog;

    public static final String LOGIN_OUT_OF_DATE_ACTION = "com.playhut.partner.login.out.date.action";

    private BroadcastReceiver mLoginOutDateReceiver;

    private Dialog mOutDateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startPageEnterAnim();
        initView();
        initTitleBar();
        initData();
        initLogic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerLoginOutDate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLoginOutDateReceiver != null) {
            unregisterReceiver(mLoginOutDateReceiver);
            mLoginOutDateReceiver = null;
        }
    }

    public void showLoadingDialog(String loadMsg, boolean backKeyFinishDialog) {
        if (mLoadingDialog == null || !mLoadingDialog.isShowing()) {
            mLoadingDialog = DialogUtils.showLoadingDialog(this, loadMsg, backKeyFinishDialog);
        }
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    /**
     * 注册登录过期广播
     */
    private void registerLoginOutDate() {
        if (mLoginOutDateReceiver == null) {
            mLoginOutDateReceiver = new LoginOutDateReceiver();
            IntentFilter filter = new IntentFilter(LOGIN_OUT_OF_DATE_ACTION);
            registerReceiver(mLoginOutDateReceiver, filter);
        }
    }

    /**
     * 登录过期广播
     */
    private class LoginOutDateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null)
                return;
            if (LOGIN_OUT_OF_DATE_ACTION.equals(intent.getAction())) {
                showLoginOutDateDialog();
            }
        }
    }

    /**
     * 显示登录过期
     */
    private void showLoginOutDateDialog() {
        if (mOutDateDialog == null || !mOutDateDialog.isShowing()) {
            mOutDateDialog = DialogUtils.showConfirmDialog(this, R.layout.confirm_dialog_layout, false);
        }
        TextView titleTv = (TextView) mOutDateDialog.findViewById(R.id.tv_title);
        titleTv.setText("Login");
        TextView textTv = (TextView) mOutDateDialog.findViewById(R.id.tv_text);
        textTv.setText("User identity authentication failed. Please login again");
        TextView confirmTv = (TextView) mOutDateDialog.findViewById(R.id.tv_confirm);
        confirmTv.setText("Login");
        mOutDateDialog.findViewById(R.id.rl_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消
                dismissOutDateDialog();
            }
        });
        mOutDateDialog.findViewById(R.id.rl_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 登录
                dismissOutDateDialog();
            }
        });
    }

    private void dismissOutDateDialog() {
        AccountUtils.deleteAccount();
        PartnerApplication.mAccount = null;
        if (mOutDateDialog != null && mOutDateDialog.isShowing()) {
            mOutDateDialog.dismiss();
            mOutDateDialog = null;
        }
    }

    /**
     * 初始化视图，findViewById
     */
    protected abstract void initView();

    /**
     * 初始化标题栏
     */
    protected abstract void initTitleBar();

    /**
     * 初始化数据，包括setListener
     */
    protected abstract void initData();

    /**
     * 逻辑处理
     */
    protected abstract void initLogic();

    @Override
    public void finish() {
        super.finish();
        startPageExitAnim();
    }

    /**
     * 设置进入页面的动画
     */
    protected void startPageEnterAnim() {
        overridePendingTransition(R.anim.activity_to_left_enter_anim, R.anim.activity_to_left_exit_anim);
    }

    /**
     * 设置关闭页面时的动画
     */
    protected void startPageExitAnim() {
        overridePendingTransition(R.anim.activity_to_right_enter_anim, R.anim.activity_to_right_exit_anim);
    }

    /**
     * 设置全屏模式
     */
    public void setFullScreen(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}
