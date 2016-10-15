package com.playhut.partner.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.playhut.partner.R;
import com.playhut.partner.adapter.MainGridViewAdapter;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.utils.BitmapUtils;
import com.playhut.partner.utils.PartnerUtils;
import com.playhut.partner.widget.MeasureGridView;

public class MainActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView mBgIv;

    private MeasureGridView mMainGv;

    private ImageView mMsgIv;

    private ImageView mSettingIv;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        mBgIv = (ImageView) findViewById(R.id.iv_bg);
        mMainGv = (MeasureGridView) findViewById(R.id.gv_main);
        mMsgIv = (ImageView) findViewById(R.id.iv_msg);
        mSettingIv = (ImageView) findViewById(R.id.iv_setting);
    }

    @Override
    protected void initTitleBar() {

    }

    @Override
    protected void initData() {
        mMsgIv.setOnClickListener(this);
        mSettingIv.setOnClickListener(this);
        mMainGv.setOnItemClickListener(this);
    }

    @Override
    protected void initLogic() {
        int maxWidth = GlobalConstants.SCREEN_WIDTH;
        int maxHeight = PartnerUtils.dip2px(this, 200);
        Bitmap bgBitmap = BitmapUtils.getBitmapFromRes(getResources(), R.mipmap.main_top_bg, maxWidth * maxHeight);
        mBgIv.setImageBitmap(bgBitmap);
        mMainGv.setAdapter(new MainGridViewAdapter(this));
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

}
