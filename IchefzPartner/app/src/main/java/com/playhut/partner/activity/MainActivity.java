package com.playhut.partner.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.playhut.partner.R;
import com.playhut.partner.adapter.MainGridViewAdapter;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.debug.MyLog;
import com.playhut.partner.entity.MainInfoEntity;
import com.playhut.partner.mvp.presenter.IMainInfoPresent;
import com.playhut.partner.mvp.presenter.IMainOpenStatePresent;
import com.playhut.partner.mvp.presenter.impl.MainInfoPresent;
import com.playhut.partner.mvp.presenter.impl.MainOpenStatePresent;
import com.playhut.partner.mvp.view.MainInfoView;
import com.playhut.partner.mvp.view.MainOpenStateView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.BitmapUtils;
import com.playhut.partner.utils.ImageLoderOptionUtils;
import com.playhut.partner.utils.PartnerUtils;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.MeasureGridView;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class MainActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener,
        CompoundButton.OnCheckedChangeListener {

    private ImageView mBgIv;

    private MeasureGridView mMainGv;

    private ImageView mMsgIv;

    private ImageView mSettingIv;

    private boolean mExitFlag = false;

    public static final String SHOW_RESTAURANT_SETTING_INTENT = "mShowRestaurantSettingIntent";

    private ImageView mAvatarIv;

    private ImageLoader mImageLoader;

    private DisplayImageOptions mOptions;

    private TextView mKitchenNameTv;

    private RatingBar mRatingBar;

    private TextView mPhoneTv;

    private TextView mOrderTv;

    private TextView mTurnoverTv;

    private CheckBox mOpenCb;

    public static final String FINISH_MAIN_EVENT_BUS_TAG = "mFinishMainEventBusTag";

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        mBgIv = (ImageView) findViewById(R.id.iv_bg);
        mMainGv = (MeasureGridView) findViewById(R.id.gv_main);
        mMsgIv = (ImageView) findViewById(R.id.iv_msg);
        mSettingIv = (ImageView) findViewById(R.id.iv_setting);
        mAvatarIv = (ImageView) findViewById(R.id.iv_avatar);
        mKitchenNameTv = (TextView) findViewById(R.id.tv_kitchen_name);
        mRatingBar = (RatingBar) findViewById(R.id.rb_kitchen);
        mPhoneTv = (TextView) findViewById(R.id.tv_phone);
        mOrderTv = (TextView) findViewById(R.id.tv_order);
        mTurnoverTv = (TextView) findViewById(R.id.tv_turnover);
        mOpenCb = (CheckBox) findViewById(R.id.cb_open);
    }

    @Override
    protected void initTitleBar() {

    }

    @Override
    protected void initData() {
        mMsgIv.setOnClickListener(this);
        mSettingIv.setOnClickListener(this);
        mMainGv.setOnItemClickListener(this);
        mOpenCb.setOnCheckedChangeListener(this);

        mImageLoader = ImageLoader.getInstance();
        mOptions = ImageLoderOptionUtils.setOptions(R.drawable.loading_image_shape, 0, R.drawable.loading_image_shape);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void initLogic() {
        int maxWidth = GlobalConstants.SCREEN_WIDTH;
        int maxHeight = PartnerUtils.dip2px(this, 200);
        Bitmap bgBitmap = BitmapUtils.getBitmapFromRes(getResources(), R.mipmap.main_top_bg, maxWidth * maxHeight);
        mBgIv.setImageBitmap(bgBitmap);
        mMainGv.setAdapter(new MainGridViewAdapter(this));

        Intent intent = getIntent();
        if (intent != null) {
            boolean showRestaurantSettingActivity = intent.getBooleanExtra(SHOW_RESTAURANT_SETTING_INTENT, false);
            if (showRestaurantSettingActivity) {
                startActivity(new Intent(this, RestaurantSettingActivity.class));
            }
        }
    }

    @Override
    protected void onResume() {
        getMainInfo();
        super.onResume();
    }

    private void getMainInfo() {
        IMainInfoPresent present = new MainInfoPresent(this, new MainInfoListener());
        present.getInfo();
    }

    private class MainInfoListener extends MainInfoView {

        @Override
        public void loadSuccess(MainInfoEntity entity) {
            setMainInfo(entity);
        }

        @Override
        public void loadFailure(IchefzException exception) {
            ToastUtils.show(exception.getErrorMsg());
        }

    }

    private void setMainInfo(MainInfoEntity entity) {
        mImageLoader.displayImage(entity.profile_picture, mAvatarIv, mOptions);
        String kitchenName = entity.kitchen_name;
        if (TextUtils.isEmpty(kitchenName)) {
            kitchenName = entity.first_name + " " + entity.last_name;
        }
        mKitchenNameTv.setText(kitchenName);
        mRatingBar.setVisibility(View.VISIBLE);
        mRatingBar.setRating(entity.level);
        mPhoneTv.setText(entity.phone_number);
        mOrderTv.setText(String.valueOf(entity.order_today));
        mTurnoverTv.setText("$" + entity.turnover_today);
        mOpenCb.setVisibility(View.VISIBLE);
        mOpenCb.setChecked(entity.open_state == 1);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        MyLog.i("======onCheckedChanged======");
        if (buttonView.isPressed()) {
            MyLog.i("======press======");
            int state = isChecked ? 1 : 0;
            IMainOpenStatePresent present = new MainOpenStatePresent(MainActivity.this, new MainOpenStateListener());
            present.setOpenState(state);
        }
    }

    private class MainOpenStateListener extends MainOpenStateView {

        @Override
        public void startLoading() {
            showLoadingDialog(getString(R.string.loading_dialog_setting), true);
        }

        @Override
        public void loadSuccess() {

        }

        @Override
        public void finishLoading() {
            dismissLoadingDialog();
        }

        @Override
        public void loadFailure(IchefzException exception) {
            ToastUtils.show(exception.getErrorMsg());
            // 还原状态
            boolean isCheck = mOpenCb.isChecked();
            mOpenCb.setChecked(!isCheck);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_msg:
                startActivity(new Intent(this, MessageListActivity.class));
                break;
            case R.id.iv_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, NewOrderActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, MyMenuActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, FinanceActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, HistoryOrderActivity.class));
                break;
            case 5:
                startActivity(new Intent(this, RestaurantSettingActivity.class));
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(String tag) {
        if (!TextUtils.isEmpty(tag) && tag.equals(FINISH_MAIN_EVENT_BUS_TAG)) {
            MainActivity.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (mExitFlag) {
            super.onBackPressed();
            finish();
        } else {
            mExitFlag = true;
            ToastUtils.show("Press again to exit app");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mExitFlag = false;
                }
            }, 2500);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
