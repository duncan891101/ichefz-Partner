package com.playhut.partner.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.business.SelectCountryBusiness;
import com.playhut.partner.constants.ChangeTypeConstants;
import com.playhut.partner.entity.FinanceDateEntity;
import com.playhut.partner.entity.RestaurantEntity;
import com.playhut.partner.eventbus.EditKitchenAddressEB;
import com.playhut.partner.eventbus.EditPersonInfoEB;
import com.playhut.partner.eventbus.EditRestaurantInfoEB;
import com.playhut.partner.mvp.presenter.IEditRestaurantInfoPresent;
import com.playhut.partner.mvp.presenter.IGetRestaurantInfoPresent;
import com.playhut.partner.mvp.presenter.impl.EditRestaurantInfoPresent;
import com.playhut.partner.mvp.presenter.impl.GetRestaurantInfoPresent;
import com.playhut.partner.mvp.view.EditRestaurantInfoView;
import com.playhut.partner.mvp.view.GetRestaurantInfoView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.ImageLoderOptionUtils;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.PartnerTitleBar;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 *
 */
public class RestaurantSettingActivity extends BaseActivity implements View.OnClickListener {

    private List<FinanceDateEntity> mCountryList;

    private SelectCountryBusiness mSelectCountryBusiness;

    private TextView mFoodTypeTv;

    private RestaurantEntity mRestaurantEntity;

    private TextView mNameTv;

    private ImageView mAvatarIv;

    private TextView mKitchenNameTv;

    private TextView mSubtitleTv;

    private TextView mSummeryTv;

    private TextView mShipDayTv;

    private TextView mKitchenAddressTv;

    private TextView mAboutMeTv;

    private ImageLoader mImageLoader;

    private DisplayImageOptions mOptions;

    public static final String FINISH_RESTAURANT_EVENT_BUS_TAG = "mFinishRestaurantEventBusTag";

    @Override
    protected void initView() {
        setContentView(R.layout.activity_restaurant_setting);
        findViewById(R.id.rl_person_info).setOnClickListener(this);
        findViewById(R.id.rl_kitchen_name).setOnClickListener(this);
        findViewById(R.id.rl_subtitle).setOnClickListener(this);
        findViewById(R.id.rl_food_type).setOnClickListener(this);
        findViewById(R.id.rl_kitchen_summery).setOnClickListener(this);
        findViewById(R.id.rl_ship_day).setOnClickListener(this);
        findViewById(R.id.rl_kitchen_address).setOnClickListener(this);
        findViewById(R.id.rl_bank_info).setOnClickListener(this);
        findViewById(R.id.rl_about_me).setOnClickListener(this);

        mNameTv = (TextView) findViewById(R.id.tv_name);
        mAvatarIv = (ImageView) findViewById(R.id.iv_avatar);
        mKitchenNameTv = (TextView) findViewById(R.id.tv_kitchen_name);
        mSubtitleTv = (TextView) findViewById(R.id.tv_subtitle);
        mFoodTypeTv = (TextView) findViewById(R.id.tv_food_type);
        mSummeryTv = (TextView) findViewById(R.id.tv_summery);
        mShipDayTv = (TextView) findViewById(R.id.tv_ship_day);
        mKitchenAddressTv = (TextView) findViewById(R.id.tv_kitchen_address);
        mAboutMeTv = (TextView) findViewById(R.id.tv_about_me);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Restaurant Settings");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                RestaurantSettingActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        mImageLoader = ImageLoader.getInstance();
        mOptions = ImageLoderOptionUtils.setOptions(R.drawable.loading_image_shape, 0, R.drawable.loading_image_shape);
        mCountryList = new ArrayList<>();
        mSelectCountryBusiness = new SelectCountryBusiness(this);
    }

    @Override
    protected void initLogic() {
        IGetRestaurantInfoPresent present = new GetRestaurantInfoPresent(this, new GetRestaurantInfoListener());
        present.get();
    }

    private class GetRestaurantInfoListener extends GetRestaurantInfoView {
        @Override
        public void startLoading() {
            showLoadingDialog(getString(R.string.loading_dialog_loading), true);
        }

        @Override
        public void loadSuccess(RestaurantEntity entity) {
            mRestaurantEntity = entity;
            setRestaurantInfo();
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

    private void setRestaurantInfo() {
        if (mRestaurantEntity != null) {
            mNameTv.setText(mRestaurantEntity.first_name + " " + mRestaurantEntity.last_name);
            mImageLoader.displayImage(mRestaurantEntity.profile_picture, mAvatarIv, mOptions);

            String kitchenName = mRestaurantEntity.kitchen_name;
            if (!TextUtils.isEmpty(kitchenName)) {
                mKitchenNameTv.setText(kitchenName);
            }

            String subtitle = mRestaurantEntity.subtitle;
            if (!TextUtils.isEmpty(subtitle)) {
                mSubtitleTv.setText(subtitle);
            }

            String typeOfFood = mRestaurantEntity.type_food;
            if (!TextUtils.isEmpty(typeOfFood)) {
                mFoodTypeTv.setText(typeOfFood);
            }
            List<String> allType = mRestaurantEntity.all_type_food;
            if (allType != null && allType.size() > 0) {
                for (int i = 0; i < allType.size(); i++) {
                    FinanceDateEntity entity = new FinanceDateEntity();
                    if (i == 0) {
                        entity.setIsSelect(true);
                    } else {
                        entity.setIsSelect(false);
                    }
                    entity.setText(allType.get(i));
                    mCountryList.add(entity);
                }
            }

            String summery = mRestaurantEntity.kitchen_summery;
            if (!TextUtils.isEmpty(summery)) {
                mSummeryTv.setText(summery);
            }

            String shipDay = mRestaurantEntity.ship_day;
            if (!TextUtils.isEmpty(shipDay)) {
                mShipDayTv.setText(shipDay);
            }

            String street = mRestaurantEntity.street_address;
            String city = mRestaurantEntity.city;
            String state = mRestaurantEntity.state;
            String country = mRestaurantEntity.country;
            StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty(street)) {
                sb.append(street);
                sb.append(" ");
            }
            if (!TextUtils.isEmpty(city)) {
                sb.append(city);
                sb.append(" ");
            }
            if (!TextUtils.isEmpty(state)) {
                sb.append(state);
                sb.append(" ");
            }
            if (!TextUtils.isEmpty(country)) {
                sb.append(country);
            }
            if (!TextUtils.isEmpty(sb.toString())) {
                mKitchenAddressTv.setText(sb.toString());
            }

            String aboutMe = mRestaurantEntity.about_me;
            if (!TextUtils.isEmpty(aboutMe)) {
                mAboutMeTv.setText(aboutMe);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_person_info:
                PersonInfoActivity.actionIntent(this, mRestaurantEntity);
                break;
            case R.id.rl_kitchen_name:
                Intent kitchenNameIntent = new Intent(this, ChangeInfoActivity.class);
                kitchenNameIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.KITCHEN_NAME);
                if (!TextUtils.isEmpty(mKitchenNameTv.getText().toString())) {
                    kitchenNameIntent.putExtra(ChangeInfoActivity.ORIGINAL_CONTENT_INTENT, mKitchenNameTv.getText().toString());
                }
                startActivity(kitchenNameIntent);
                break;
            case R.id.rl_subtitle:
                Intent subtitleIntent = new Intent(this, ChangeInfoActivity.class);
                subtitleIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.SUBTITLE);
                if (!TextUtils.isEmpty(mSubtitleTv.getText().toString())) {
                    subtitleIntent.putExtra(ChangeInfoActivity.ORIGINAL_CONTENT_INTENT, mSubtitleTv.getText().toString());
                }
                startActivity(subtitleIntent);
                break;
            case R.id.rl_food_type:
                mSelectCountryBusiness.showDialog(mCountryList, new SelectCountryBusiness.SelectCountryOkListener() {
                    @Override
                    public void onOk(String selectCountry) {
                        editRestaurantInfo(selectCountry, 3);
                    }
                });
                break;
            case R.id.rl_kitchen_summery:
                Intent kitchenSummeryIntent = new Intent(this, ChangeInfoActivity.class);
                kitchenSummeryIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.KITCHEN_SUMMERY);
                if (!TextUtils.isEmpty(mSummeryTv.getText().toString())) {
                    kitchenSummeryIntent.putExtra(ChangeInfoActivity.ORIGINAL_CONTENT_INTENT, mSummeryTv.getText().toString());
                }
                startActivity(kitchenSummeryIntent);
                break;
            case R.id.rl_ship_day:
                Intent shipDayIntent = new Intent(this, ChangeInfoActivity.class);
                shipDayIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.SHIP_DAY);
                if (!TextUtils.isEmpty(mShipDayTv.getText().toString())) {
                    shipDayIntent.putExtra(ChangeInfoActivity.ORIGINAL_CONTENT_INTENT, mShipDayTv.getText().toString());
                }
                startActivity(shipDayIntent);
                break;
            case R.id.rl_kitchen_address:
                KitchenAddressActivity.actionIntent(this, mRestaurantEntity);
                break;
            case R.id.rl_bank_info:
                BankInformationActivity.actionIntent(this, mRestaurantEntity);
                break;
            case R.id.rl_about_me:
                Intent aboutMeIntent = new Intent(this, AboutMeActivity.class);
                if (!TextUtils.isEmpty(mAboutMeTv.getText().toString())) {
                    aboutMeIntent.putExtra(AboutMeActivity.ORIGINAL_TEXT_INTENT, mAboutMeTv.getText().toString());
                }
                startActivity(aboutMeIntent);
                break;
        }
    }

    private void editRestaurantInfo(final String content, int changeType) {
        IEditRestaurantInfoPresent present = new EditRestaurantInfoPresent(this, new EditRestaurantInfoView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_loading), true);
            }

            @Override
            public void loadSuccess() {
                mFoodTypeTv.setText(content);
                if (mRestaurantEntity != null) {
                    mRestaurantEntity.type_food = content;
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

    @Subscribe
    public void onEventMainThread(EditPersonInfoEB editPersonInfoEB) {
        if (editPersonInfoEB == null)
            return;
        int changeType = editPersonInfoEB.changeType;
        String content = editPersonInfoEB.changeContent;
        switch (changeType) {
            case 1:
                // had change first name
                String lastName = "";
                if (mRestaurantEntity != null) {
                    lastName = mRestaurantEntity.last_name;
                }
                mNameTv.setText(content + " " + lastName);
                break;
            case 2:
                // had change last name
                String firstName = "";
                if (mRestaurantEntity != null) {
                    firstName = mRestaurantEntity.first_name;
                }
                mNameTv.setText(firstName + " " + content);
                break;
            case 7:
                // 里层上传了头像，这一层要跟着修改为最新的
                mImageLoader.displayImage(content, mAvatarIv, mOptions);
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EditRestaurantInfoEB editRestaurantInfoEB) {
        if (editRestaurantInfoEB == null)
            return;
        int changeType = editRestaurantInfoEB.changeType;
        String content = editRestaurantInfoEB.changeContent;
        switch (changeType) {
            case 1:
                // had change kitchen name
                mKitchenNameTv.setText(content);
                if (mRestaurantEntity != null) {
                    mRestaurantEntity.kitchen_name = content;
                }
                break;
            case 2:
                // had change subtitle
                mSubtitleTv.setText(content);
                if (mRestaurantEntity != null) {
                    mRestaurantEntity.subtitle = content;
                }
                break;
            case 4:
                // had change kitchen summery
                mSummeryTv.setText(content);
                if (mRestaurantEntity != null) {
                    mRestaurantEntity.kitchen_summery = content;
                }
                break;
            case 5:
                // had change ship day
                mShipDayTv.setText(content);
                if (mRestaurantEntity != null) {
                    mRestaurantEntity.ship_day = content;
                }
                break;
            case 6:
                mAboutMeTv.setText(content);
                if (mRestaurantEntity != null) {
                    mRestaurantEntity.about_me = content;
                }
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EditKitchenAddressEB editKitchenAddressEB) {
        if (editKitchenAddressEB == null)
            return;
        StringBuilder sb = new StringBuilder();
        sb.append(editKitchenAddressEB.street);
        sb.append(" ");
        sb.append(editKitchenAddressEB.city);
        sb.append(" ");
        sb.append(editKitchenAddressEB.state);
        sb.append(" ");
        sb.append(editKitchenAddressEB.country);
        mKitchenAddressTv.setText(sb.toString());
        if (mRestaurantEntity != null) {
            mRestaurantEntity.country = editKitchenAddressEB.country;
            mRestaurantEntity.state = editKitchenAddressEB.state;
            mRestaurantEntity.city = editKitchenAddressEB.city;
            mRestaurantEntity.street_address = editKitchenAddressEB.street;
            mRestaurantEntity.zip_code = editKitchenAddressEB.zipCode;
            mRestaurantEntity.apt = editKitchenAddressEB.apt;
        }
    }

    @Subscribe
    public void onEventMainThread(String tag) {
        if (!TextUtils.isEmpty(tag) && tag.equals(FINISH_RESTAURANT_EVENT_BUS_TAG)) {
            RestaurantSettingActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
