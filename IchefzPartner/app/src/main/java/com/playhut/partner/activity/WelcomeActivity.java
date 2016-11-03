package com.playhut.partner.activity;

import android.content.Intent;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.base.PartnerApplication;
import com.playhut.partner.constants.IntroduceConstants;
import com.playhut.partner.utils.SPUtils;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
public class WelcomeActivity extends BaseActivity {

    private static Timer mTimer;

    public static final long DELAY_TIME = 3 * 1000;

    @Override
    protected void initView() {
        setFullScreen();
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void initTitleBar() {

    }

    @Override
    protected void initData() {
        mTimer = new Timer();
    }

    @Override
    protected void initLogic() {
        // 开始计时
        mTimer.schedule(new DelayTimerTask(this), DELAY_TIME);
    }

    /**
     * 定时TimerTask
     */
    private static class DelayTimerTask extends TimerTask {

        private WeakReference<WelcomeActivity> weakReference;

        public DelayTimerTask(WelcomeActivity welcomeActivity) {
            weakReference = new WeakReference<>(welcomeActivity);
        }

        @Override
        public void run() {
            final WelcomeActivity welcomeActivity = weakReference.get();
            if (welcomeActivity != null) {
                welcomeActivity.runOnMainThread();
            }
        }
    }

    private void runOnMainThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean result = SPUtils.getBooleanWithOther(IntroduceConstants.IS_FIRST_TO_APP);
                if (!result) {
                    startActivity(new Intent(WelcomeActivity.this, IntroduceActivity.class));
                } else {
                    if (PartnerApplication.mAccount != null && PartnerApplication.mAccount.isAccountValid()) {
                        // 已登录
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        // 未登录
                        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    }
                }
                WelcomeActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        super.onDestroy();
    }

}
