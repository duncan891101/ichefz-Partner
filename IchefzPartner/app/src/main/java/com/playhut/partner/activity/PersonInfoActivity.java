package com.playhut.partner.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.business.ChooseAvatarBusiness;
import com.playhut.partner.business.SelectCountryBusiness;
import com.playhut.partner.business.SelectDateBusiness;
import com.playhut.partner.constants.ChangeTypeConstants;
import com.playhut.partner.debug.MyLog;
import com.playhut.partner.entity.FinanceDateEntity;
import com.playhut.partner.entity.RestaurantEntity;
import com.playhut.partner.eventbus.EditPersonInfoEB;
import com.playhut.partner.mvp.presenter.IEditPersonInfoPresent;
import com.playhut.partner.mvp.presenter.IUploadAvatarPresent;
import com.playhut.partner.mvp.presenter.impl.EditPersonInfoPresent;
import com.playhut.partner.mvp.presenter.impl.UploadAvatarPresent;
import com.playhut.partner.mvp.view.EditPersonInfoView;
import com.playhut.partner.mvp.view.UploadAvatarView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.ImageLoderOptionUtils;
import com.playhut.partner.utils.PartnerUtils;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.PartnerTitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 *
 */
public class PersonInfoActivity extends BaseActivity implements View.OnClickListener {

    private SelectDateBusiness mSelectDateBusiness;

    private TextView mBirthdayTv;

    private SelectCountryBusiness mSelectCountryBusiness;

    private List<FinanceDateEntity> mGenderList;

    private TextView mGenderTv;

    private ChooseAvatarBusiness mChooseAvatarBusiness;

    private ImageView mAvatarIv;

    private static RestaurantEntity mRestaurantEntity;

    private TextView mNameTv;

    private TextView mFirstNameTv;

    private TextView mLastNameTv;

    private TextView mEmailTv;

    private TextView mPhoneTv;

    private TextView mDescTv;

    private ImageLoader mImageLoader;

    private DisplayImageOptions mOptions;

    public static final String FINISH_PERSON_EVENT_BUS_TAG = "mFinishPersonEventBusTag";

    public static void actionIntent(Context context, RestaurantEntity entity) {
        mRestaurantEntity = entity;
        context.startActivity(new Intent(context, PersonInfoActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_person_info);
        findViewById(R.id.rl_avatar).setOnClickListener(this);
        findViewById(R.id.rl_first_name).setOnClickListener(this);
        findViewById(R.id.rl_last_name).setOnClickListener(this);
        findViewById(R.id.rl_gender).setOnClickListener(this);
        findViewById(R.id.rl_birthday).setOnClickListener(this);
        findViewById(R.id.rl_phone_number).setOnClickListener(this);
        findViewById(R.id.rl_modify_pwd).setOnClickListener(this);
        findViewById(R.id.rl_describe_yourself).setOnClickListener(this);

        mNameTv = (TextView) findViewById(R.id.tv_name);
        mAvatarIv = (ImageView) findViewById(R.id.iv_avatar);
        mFirstNameTv = (TextView) findViewById(R.id.tv_first_name);
        mLastNameTv = (TextView) findViewById(R.id.tv_last_name);
        mGenderTv = (TextView) findViewById(R.id.tv_gender);
        mBirthdayTv = (TextView) findViewById(R.id.tv_birthday);
        mEmailTv = (TextView) findViewById(R.id.tv_email);
        mPhoneTv = (TextView) findViewById(R.id.tv_phone);
        mDescTv = (TextView) findViewById(R.id.tv_desc);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Restaurant settings");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                PersonInfoActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        mImageLoader = ImageLoader.getInstance();
        mOptions = ImageLoderOptionUtils.setOptions(R.drawable.loading_image_shape, 0, R.drawable.loading_image_shape);
        EventBus.getDefault().register(this);

        mSelectDateBusiness = new SelectDateBusiness(this);
        mSelectCountryBusiness = new SelectCountryBusiness(this);
        mGenderList = new ArrayList<>();
        mChooseAvatarBusiness = new ChooseAvatarBusiness(this, PartnerUtils.dip2px(this, 80), PartnerUtils.dip2px(this, 80));
    }

    @Override
    protected void initLogic() {
        initGender();
        setRestaurantInfo();
    }

    private void initGender() {
        FinanceDateEntity entity1 = new FinanceDateEntity();
        entity1.setIsSelect(true);
        entity1.setText("Male");
        mGenderList.add(entity1);

        FinanceDateEntity entity2 = new FinanceDateEntity();
        entity2.setText("Female");
        mGenderList.add(entity2);
    }

    private void setRestaurantInfo() {
        if (mRestaurantEntity != null) {
            mNameTv.setText(mRestaurantEntity.first_name + " " + mRestaurantEntity.last_name);
            mImageLoader.displayImage(mRestaurantEntity.profile_picture, mAvatarIv, mOptions);
            mFirstNameTv.setText(mRestaurantEntity.first_name);
            mLastNameTv.setText(mRestaurantEntity.last_name);

            String gender = mRestaurantEntity.gender;
            if (!TextUtils.isEmpty(gender)) {
                mGenderTv.setText(gender);
            }

            String birthday = mRestaurantEntity.birthday;
            if (!TextUtils.isEmpty(birthday)) {
                mBirthdayTv.setText(birthday);
            }

            mEmailTv.setText(mRestaurantEntity.email);

            String phoneNumber = mRestaurantEntity.phone_number;
            if (!TextUtils.isEmpty(phoneNumber)) {
                mPhoneTv.setText(phoneNumber);
            }

            String descYourself = mRestaurantEntity.desc_yourself;
            if (!TextUtils.isEmpty(descYourself)) {
                mDescTv.setText(descYourself);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_avatar:
                mChooseAvatarBusiness.showChooseAvatarDialog(new ChooseAvatarBusiness.UserSelectAvatarListener() {
                    @Override
                    public void select(Bitmap bitmap, File file) {
                        uploadAvatar(bitmap, file);
                    }
                });
                break;
            case R.id.rl_first_name:
                Intent firstNameIntent = new Intent(this, ChangeInfoActivity.class);
                firstNameIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.FIRST_NAME);
                if (!TextUtils.isEmpty(mFirstNameTv.getText().toString())) {
                    firstNameIntent.putExtra(ChangeInfoActivity.ORIGINAL_CONTENT_INTENT, mFirstNameTv.getText().toString());
                }
                startActivity(firstNameIntent);
                break;
            case R.id.rl_last_name:
                Intent lastNameIntent = new Intent(this, ChangeInfoActivity.class);
                lastNameIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.LAST_NAME);
                if (!TextUtils.isEmpty(mLastNameTv.getText().toString())) {
                    lastNameIntent.putExtra(ChangeInfoActivity.ORIGINAL_CONTENT_INTENT, mLastNameTv.getText().toString());
                }
                startActivity(lastNameIntent);
                break;
            case R.id.rl_gender:
                mSelectCountryBusiness.showDialog(mGenderList, new SelectCountryBusiness.SelectCountryOkListener() {
                    @Override
                    public void onOk(String selectCountry) {
                        editPersonInfo(selectCountry, 3);
                    }
                });
                break;
            case R.id.rl_birthday:
                mSelectDateBusiness.showDialog(true, new SelectDateBusiness.SelectDateOkListener() {
                    @Override
                    public void onOk(String selectYear, String selectMonth, String selectDay) {
                        editPersonInfo(selectYear + "-" + selectMonth + "-" + selectDay, 4);
                    }
                });
                break;
            case R.id.rl_phone_number:
                Intent phoneNumberIntent = new Intent(this, ChangeInfoActivity.class);
                phoneNumberIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.PHONE_NUMBER);
                if (!TextUtils.isEmpty(mPhoneTv.getText().toString())) {
                    phoneNumberIntent.putExtra(ChangeInfoActivity.ORIGINAL_CONTENT_INTENT, mPhoneTv.getText().toString());
                }
                startActivity(phoneNumberIntent);
                break;
            case R.id.rl_modify_pwd:
                startActivity(new Intent(this, ModifyPwdActivity.class));
                break;
            case R.id.rl_describe_yourself:
                Intent descIntent = new Intent(this, ChangeInfoActivity.class);
                descIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.DESCRIBE_YOURSELF);
                if (!TextUtils.isEmpty(mDescTv.getText().toString())) {
                    descIntent.putExtra(ChangeInfoActivity.ORIGINAL_CONTENT_INTENT, mDescTv.getText().toString());
                }
                startActivity(descIntent);
                break;
        }
    }

    private void uploadAvatar(final Bitmap bitmap, File file) {
        IUploadAvatarPresent present = new UploadAvatarPresent(this, new UploadAvatarView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_upload), false);
            }

            @Override
            public void loadSuccess(String url) {
                mImageLoader.displayImage(url, mAvatarIv, mOptions);
                if (mRestaurantEntity != null) {
                    mRestaurantEntity.profile_picture = url;
                }
                // 刷新外层的头像
                EditPersonInfoEB editPersonInfoEB = new EditPersonInfoEB();
                editPersonInfoEB.changeType = 7;
                editPersonInfoEB.changeContent = url;
                EventBus.getDefault().post(editPersonInfoEB);
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
        present.upload(file);
    }

    private void editPersonInfo(final String content, final int changeType) {
        IEditPersonInfoPresent present = new EditPersonInfoPresent(this, new EditPersonInfoView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_loading), true);
            }

            @Override
            public void loadSuccess() {
                if (changeType == 3) {
                    mGenderTv.setText(content);
                    if (mRestaurantEntity != null) {
                        mRestaurantEntity.gender = content;
                    }
                } else if (changeType == 4) {
                    mBirthdayTv.setText(content);
                    if (mRestaurantEntity != null) {
                        mRestaurantEntity.birthday = content;
                    }
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mChooseAvatarBusiness.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe
    public void onEventMainThread(EditPersonInfoEB editPersonInfoEB) {
        if (editPersonInfoEB == null)
            return;
        int changeType = editPersonInfoEB.changeType;
        String content = editPersonInfoEB.changeContent;
        switch (changeType) {
            case 1:
                // had change first name
                mFirstNameTv.setText(content);
                String lastName = "";
                if (mRestaurantEntity != null) {
                    mRestaurantEntity.first_name = content;
                    lastName = mRestaurantEntity.last_name;
                }
                mNameTv.setText(content + " " + lastName);
                break;
            case 2:
                // had change last name
                mLastNameTv.setText(content);
                String firstName = "";
                if (mRestaurantEntity != null) {
                    mRestaurantEntity.last_name = content;
                    firstName = mRestaurantEntity.first_name;
                }
                mNameTv.setText(firstName + " " + content);
                break;
            case 5:
                // had change phone number
                mPhoneTv.setText(content);
                if (mRestaurantEntity != null) {
                    mRestaurantEntity.phone_number = content;
                }
                break;
            case 6:
                // had change describe yourself
                mDescTv.setText(content);
                if (mRestaurantEntity != null) {
                    mRestaurantEntity.desc_yourself = content;
                }
                break;

        }
    }

    @Subscribe
    public void onEventMainThread(String tag) {
        if (!TextUtils.isEmpty(tag) && tag.equals(FINISH_PERSON_EVENT_BUS_TAG)) {
            PersonInfoActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
