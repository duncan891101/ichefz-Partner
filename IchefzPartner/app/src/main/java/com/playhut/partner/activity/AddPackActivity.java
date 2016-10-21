package com.playhut.partner.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.business.ChooseAvatarBusiness;
import com.playhut.partner.business.SelectCountryBusiness;
import com.playhut.partner.constants.ChangeTypeConstants;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.debug.MyLog;
import com.playhut.partner.entity.FinanceDateEntity;
import com.playhut.partner.widget.PartnerTitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AddPackActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout mPackImageLayout;

    private ImageView mCameraIv;

    private ImageView mPackIv;

    private ChooseAvatarBusiness mChooseAvatarBusiness;

    private String mTitleStr;

    private String mBriefStr;

    private String mDescStr;

    private String mHowMadeStr;

    private TextView mTitleTv;

    private TextView mBriefTv;

    private TextView mDescTv;

    private TextView mHowMadeTv;

    private SelectCountryBusiness mSelectCountryBusiness;

    private List<FinanceDateEntity> mMaxQuantityList;

    private String[] mQuantityStrings = {"1", "2", "3", "4", "5"};

    private TextView mMaxQuantityTv;

    private Button mNextBtn;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_pack);
        mPackImageLayout = (FrameLayout) findViewById(R.id.fl_pack);
        mCameraIv = (ImageView) findViewById(R.id.iv_camera);
        mPackIv = (ImageView) findViewById(R.id.iv_pack);

        findViewById(R.id.rl_title).setOnClickListener(this);
        findViewById(R.id.rl_brief).setOnClickListener(this);
        findViewById(R.id.rl_desc).setOnClickListener(this);
        findViewById(R.id.rl_max_quantity).setOnClickListener(this);
        findViewById(R.id.rl_how_made).setOnClickListener(this);

        mTitleTv = (TextView) findViewById(R.id.tv_title);
        mBriefTv = (TextView) findViewById(R.id.tv_brief);
        mDescTv = (TextView) findViewById(R.id.tv_desc);
        mHowMadeTv = (TextView) findViewById(R.id.tv_how_made);
        mMaxQuantityTv = (TextView) findViewById(R.id.tv_max_quantity);
        mNextBtn = (Button) findViewById(R.id.btn_next);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Add more to pack");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                AddPackActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        int width = GlobalConstants.SCREEN_WIDTH;
        int height = width / 2;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPackImageLayout.getLayoutParams();
        params.width = width;
        params.height = height;
        mPackImageLayout.setLayoutParams(params);

        mCameraIv.setOnClickListener(this);
        mPackIv.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mChooseAvatarBusiness = new ChooseAvatarBusiness(this, width, height);
        // 初始化Max Quantity数值
        mSelectCountryBusiness = new SelectCountryBusiness(this);
        mMaxQuantityList = new ArrayList<>();
        for (int i = 0; i < mQuantityStrings.length; i++) {
            FinanceDateEntity entity = new FinanceDateEntity();
            if (i == 0) {
                entity.setIsSelect(true);
            } else {
                entity.setIsSelect(false);
            }
            entity.setText(mQuantityStrings[i]);
            mMaxQuantityList.add(entity);
        }
    }

    @Override
    protected void initLogic() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_camera:
            case R.id.iv_pack:
                mChooseAvatarBusiness.showChooseAvatarDialog(new ChooseAvatarBusiness.UserSelectAvatarListener() {
                    @Override
                    public void select(Bitmap bitmap, File file) {
                        mPackIv.setImageBitmap(bitmap);
                    }
                });
                break;
            case R.id.rl_title:
                Intent titleIntent = new Intent(this, ChangeInfoActivity.class);
                titleIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.ADD_PACK_TITLE);
                startActivityForResult(titleIntent, ChangeInfoActivity.ADD_PACK_REQUEST_CODE);
                break;
            case R.id.rl_brief:
                Intent briefIntent = new Intent(this, ChangeInfoActivity.class);
                briefIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.ADD_PACK_BRIEF);
                startActivityForResult(briefIntent, ChangeInfoActivity.ADD_PACK_REQUEST_CODE);
                break;
            case R.id.rl_desc:
                Intent descIntent = new Intent(this, ChangeInfoActivity.class);
                descIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.ADD_PACK_DESC);
                startActivityForResult(descIntent, ChangeInfoActivity.ADD_PACK_REQUEST_CODE);
                break;
            case R.id.rl_max_quantity:
                mSelectCountryBusiness.showDialog(mMaxQuantityList, new SelectCountryBusiness.SelectCountryOkListener() {
                    @Override
                    public void onOk(String selectCountry) {
                        mMaxQuantityTv.setText(selectCountry);
                    }
                });
                break;
            case R.id.rl_how_made:
                Intent howMadeIntent = new Intent(this, ChangeInfoActivity.class);
                howMadeIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.ADD_PACK_HOW_MADE);
                startActivityForResult(howMadeIntent, ChangeInfoActivity.ADD_PACK_REQUEST_CODE);
                break;
            case R.id.btn_next:
                startActivity(new Intent(this, AddTipsActivity.class));
                AddPackActivity.this.finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mChooseAvatarBusiness.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ChangeInfoActivity.ADD_PACK_REQUEST_CODE && data != null) {
            String saveString = data.getStringExtra(ChangeInfoActivity.SAVE_CONTENT_RETURN_INTENT);
            switch (resultCode) {
                case ChangeInfoActivity.ADD_PACK_TITLE_RESULT_CODE:
                    mTitleStr = saveString;
                    mTitleTv.setText(mTitleStr);
                    break;
                case ChangeInfoActivity.ADD_PACK_BRIEF_RESULT_CODE:
                    mBriefStr = saveString;
                    mBriefTv.setText(mBriefStr);
                    break;
                case ChangeInfoActivity.ADD_PACK_DESC_RESULT_CODE:
                    mDescStr = saveString;
                    mDescTv.setText(mDescStr);
                    break;
                case ChangeInfoActivity.ADD_PACK_HOW_MADE_RESULT_CODE:
                    mHowMadeStr = saveString;
                    mHowMadeTv.setText(mHowMadeStr);
                    break;
            }
        }
    }

}
