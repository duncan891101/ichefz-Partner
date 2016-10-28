package com.playhut.partner.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.playhut.partner.entity.AddPackEntity;
import com.playhut.partner.entity.FinanceDateEntity;
import com.playhut.partner.eventbus.AddPackEB;
import com.playhut.partner.mvp.presenter.IAddPackPresent;
import com.playhut.partner.mvp.presenter.impl.AddPackPresent;
import com.playhut.partner.mvp.view.AddPackView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.PartnerTitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

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

    private File mPackImgFile;

    private EditText mPerson2Et;

    private EditText mPerson4Et;

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

        mPerson2Et = (EditText) findViewById(R.id.et_person2);
        mPerson4Et = (EditText) findViewById(R.id.et_person4);
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
                        mPackImgFile = file;
                        mPackIv.setImageBitmap(bitmap);
                    }
                });
                break;
            case R.id.rl_title:
                Intent titleIntent = new Intent(this, ChangeInfoActivity.class);
                titleIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.ADD_PACK_TITLE);
                if (!TextUtils.isEmpty(mTitleStr)) {
                    titleIntent.putExtra(ChangeInfoActivity.ORIGINAL_CONTENT_INTENT, mTitleStr);
                }
                startActivityForResult(titleIntent, ChangeInfoActivity.ADD_PACK_REQUEST_CODE);
                break;
            case R.id.rl_brief:
                Intent briefIntent = new Intent(this, ChangeInfoActivity.class);
                briefIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.ADD_PACK_BRIEF);
                if (!TextUtils.isEmpty(mBriefStr)) {
                    briefIntent.putExtra(ChangeInfoActivity.ORIGINAL_CONTENT_INTENT, mBriefStr);
                }
                startActivityForResult(briefIntent, ChangeInfoActivity.ADD_PACK_REQUEST_CODE);
                break;
            case R.id.rl_desc:
                Intent descIntent = new Intent(this, ChangeInfoActivity.class);
                descIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.ADD_PACK_DESC);
                if (!TextUtils.isEmpty(mDescStr)) {
                    descIntent.putExtra(ChangeInfoActivity.ORIGINAL_CONTENT_INTENT, mDescStr);
                }
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
                if (!TextUtils.isEmpty(mHowMadeStr)) {
                    howMadeIntent.putExtra(ChangeInfoActivity.ORIGINAL_CONTENT_INTENT, mHowMadeStr);
                }
                startActivityForResult(howMadeIntent, ChangeInfoActivity.ADD_PACK_REQUEST_CODE);
                break;
            case R.id.btn_next:
                checkInputInfo();
                break;
        }
    }

    private void checkInputInfo() {
        if (mPackImgFile == null || !mPackImgFile.isFile() || !mPackImgFile.exists()) {
            ToastUtils.show("Upload photo cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(mTitleStr)) {
            ToastUtils.show("Title of dish cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(mBriefStr)) {
            ToastUtils.show("Brief introduction cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(mDescStr)) {
            ToastUtils.show("Description cannot be empty");
            return;
        }

        String person2 = mPerson2Et.getText().toString().trim();
        String person4 = mPerson4Et.getText().toString().trim();
        if (TextUtils.isEmpty(person2)) {
            ToastUtils.show("2 Persons cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(person4)) {
            ToastUtils.show("4 Persons cannot be empty");
            return;
        }
        String maxQuantity = mMaxQuantityTv.getText().toString();
        if (TextUtils.isEmpty(maxQuantity)) {
            ToastUtils.show("Maximum quantity cannot be empty");
            return;
        }

        toAddPack(person2, person4, maxQuantity);
    }

    private void toAddPack(String person2, String person4, String maxQuantity) {
        IAddPackPresent present = new AddPackPresent(this, new AddPackView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_loading), false);
            }

            @Override
            public void loadSuccess(AddPackEntity entity) {
                ToastUtils.show("Add pack successfully. Please perfect the other information");
                Intent intent = new Intent(AddPackActivity.this, AddTipsActivity.class);
                intent.putExtra(AddTipsActivity.MENU_ID_INTENT, entity.menu_id);
                intent.putExtra(AddTipsActivity.ADD_TYPE_INTENT, AddTipsActivity.ADD_TIPS_TYPE);
                startActivity(intent);
                AddPackActivity.this.finish();
                AddPackEB addPackEB = new AddPackEB();
                addPackEB.tag = AddPackEB.ADD_SUCCESS;
                EventBus.getDefault().post(addPackEB);
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
        present.add(mPackImgFile, mTitleStr, mBriefStr, mDescStr, person2, person4, maxQuantity, mHowMadeStr);
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
