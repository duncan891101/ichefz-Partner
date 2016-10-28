package com.playhut.partner.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.constants.ChangeTypeConstants;
import com.playhut.partner.eventbus.EditPersonInfoEB;
import com.playhut.partner.eventbus.EditRestaurantInfoEB;
import com.playhut.partner.mvp.presenter.IEditPersonInfoPresent;
import com.playhut.partner.mvp.presenter.IEditRestaurantInfoPresent;
import com.playhut.partner.mvp.presenter.impl.EditPersonInfoPresent;
import com.playhut.partner.mvp.presenter.impl.EditRestaurantInfoPresent;
import com.playhut.partner.mvp.view.EditPersonInfoView;
import com.playhut.partner.mvp.view.EditRestaurantInfoView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.PartnerTitleBar;

import de.greenrobot.event.EventBus;

/**
 *
 */
public class ChangeInfoActivity extends BaseActivity implements TextWatcher, View.OnClickListener {

    private ImageView mCleanIv;

    private EditText mInputEt;

    public static final String CHANGE_TYPE_INTENT = "mChangeTypeIntent";

    public static final String ORIGINAL_CONTENT_INTENT = "mOriginalContentIntent";

    public int mChangeType;

    public String mOriginalContent;

    private PartnerTitleBar titleBar;

    private Button mSaveBtn;

    public static final int ADD_PACK_REQUEST_CODE = 19646;

    public static final int ADD_PACK_TITLE_RESULT_CODE = 15864;

    public static final int ADD_PACK_BRIEF_RESULT_CODE = 15865;

    public static final int ADD_PACK_DESC_RESULT_CODE = 15866;

    public static final int ADD_PACK_HOW_MADE_RESULT_CODE = 15867;

    public static final String SAVE_CONTENT_RETURN_INTENT = "mSaveContentReturnIntent";

    public static final int ADD_SET_REQUEST_CODE = 29944;

    public static final int ADD_SET_NAME_RESULT_CODE = 29945;

    public static final int ADD_SET_DESC_RESULT_CODE = 29946;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_change_info);
        mCleanIv = (ImageView) findViewById(R.id.iv_clean);
        mInputEt = (EditText) findViewById(R.id.et_input);
        mSaveBtn = (Button) findViewById(R.id.btn_save);
    }

    @Override
    protected void initTitleBar() {
        titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                ChangeInfoActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        mInputEt.addTextChangedListener(this);
        mCleanIv.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            mChangeType = intent.getIntExtra(CHANGE_TYPE_INTENT, 0);
            mOriginalContent = intent.getStringExtra(ORIGINAL_CONTENT_INTENT);
            if (!TextUtils.isEmpty(mOriginalContent)) {
                mInputEt.setText(mOriginalContent);
                mInputEt.setSelection(mInputEt.length());
            }
            setTitleBar();
        }
    }

    @Override
    protected void initLogic() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String inputText = s.toString();
        if (!TextUtils.isEmpty(inputText)) {
            mCleanIv.setVisibility(View.VISIBLE);
        } else {
            mCleanIv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clean:
                mInputEt.setText("");
                break;
            case R.id.btn_save:
                String inputStr = mInputEt.getText().toString().trim();
                if (!TextUtils.isEmpty(inputStr)) {
                    saveInfo(inputStr);
                } else {
                    ToastUtils.show(getString(R.string.change_info_save_empty));
                }
                break;
        }
    }

    private void saveInfo(String inputStr) {
        switch (mChangeType) {
            case ChangeTypeConstants.FIRST_NAME:
                editPersonInfo(inputStr, 1);
                break;
            case ChangeTypeConstants.LAST_NAME:
                editPersonInfo(inputStr, 2);
                break;
            case ChangeTypeConstants.PHONE_NUMBER:
                if (inputStr.length() == 10) {
                    editPersonInfo(inputStr, 5);
                } else {
                    ToastUtils.show("Enter the valid phone number");
                }
                break;
            case ChangeTypeConstants.DESCRIBE_YOURSELF:
                editPersonInfo(inputStr, 6);
                break;
            case ChangeTypeConstants.KITCHEN_NAME:
                editRestaurantInfo(inputStr, 1);
                break;
            case ChangeTypeConstants.SUBTITLE:
                editRestaurantInfo(inputStr, 2);
                break;
            case ChangeTypeConstants.KITCHEN_SUMMERY:
                editRestaurantInfo(inputStr, 4);
                break;
            case ChangeTypeConstants.SHIP_DAY:
                try {
                    Integer.parseInt(inputStr);
                    editRestaurantInfo(inputStr, 5);
                } catch (NumberFormatException e) {
                    ToastUtils.show("Enter the valid number");
                }
                break;
            case ChangeTypeConstants.ADD_PACK_TITLE:
                Intent titleIntent = new Intent();
                titleIntent.putExtra(SAVE_CONTENT_RETURN_INTENT, inputStr);
                setResult(ADD_PACK_TITLE_RESULT_CODE, titleIntent);
                ChangeInfoActivity.this.finish();
                break;
            case ChangeTypeConstants.ADD_PACK_BRIEF:
                Intent briefIntent = new Intent();
                briefIntent.putExtra(SAVE_CONTENT_RETURN_INTENT, inputStr);
                setResult(ADD_PACK_BRIEF_RESULT_CODE, briefIntent);
                ChangeInfoActivity.this.finish();
                break;
            case ChangeTypeConstants.ADD_PACK_DESC:
                Intent descIntent = new Intent();
                descIntent.putExtra(SAVE_CONTENT_RETURN_INTENT, inputStr);
                setResult(ADD_PACK_DESC_RESULT_CODE, descIntent);
                ChangeInfoActivity.this.finish();
                break;
            case ChangeTypeConstants.ADD_PACK_HOW_MADE:
                Intent howMadeIntent = new Intent();
                howMadeIntent.putExtra(SAVE_CONTENT_RETURN_INTENT, inputStr);
                setResult(ADD_PACK_HOW_MADE_RESULT_CODE, howMadeIntent);
                ChangeInfoActivity.this.finish();
                break;
            case ChangeTypeConstants.ADD_SET_NAME:
                Intent setNameIntent = new Intent();
                setNameIntent.putExtra(SAVE_CONTENT_RETURN_INTENT, inputStr);
                setResult(ADD_SET_NAME_RESULT_CODE, setNameIntent);
                ChangeInfoActivity.this.finish();
                break;
            case ChangeTypeConstants.ADD_SET_DESC:
                Intent setDescIntent = new Intent();
                setDescIntent.putExtra(SAVE_CONTENT_RETURN_INTENT, inputStr);
                setResult(ADD_SET_DESC_RESULT_CODE, setDescIntent);
                ChangeInfoActivity.this.finish();
                break;
        }
    }

    private void setTitleBar() {
        switch (mChangeType) {
            case ChangeTypeConstants.KITCHEN_NAME:
                titleBar.setCenterTvContent("Kitchen name");
                break;
            case ChangeTypeConstants.SUBTITLE:
                titleBar.setCenterTvContent("Subtitle");
                break;
            case ChangeTypeConstants.KITCHEN_SUMMERY:
                titleBar.setCenterTvContent("Kitchen summary");
                break;
            case ChangeTypeConstants.SHIP_DAY:
                titleBar.setCenterTvContent("Ship day");
                break;
            case ChangeTypeConstants.FIRST_NAME:
                titleBar.setCenterTvContent("First name");
                break;
            case ChangeTypeConstants.LAST_NAME:
                titleBar.setCenterTvContent("Last name");
                break;
            case ChangeTypeConstants.PHONE_NUMBER:
                titleBar.setCenterTvContent("Phone number");
                break;
            case ChangeTypeConstants.DESCRIBE_YOURSELF:
                titleBar.setCenterTvContent("Describe yourself");
                break;
            case ChangeTypeConstants.ADD_PACK_TITLE:
                titleBar.setCenterTvContent("Title");
                break;
            case ChangeTypeConstants.ADD_PACK_BRIEF:
                titleBar.setCenterTvContent("Brief introduction");
                break;
            case ChangeTypeConstants.ADD_PACK_DESC:
                titleBar.setCenterTvContent("Description");
                break;
            case ChangeTypeConstants.ADD_PACK_HOW_MADE:
                titleBar.setCenterTvContent("How it's made");
                break;
            case ChangeTypeConstants.ADD_SET_NAME:
                titleBar.setCenterTvContent("Set name");
                break;
            case ChangeTypeConstants.ADD_SET_DESC:
                titleBar.setCenterTvContent("Description");
                break;
        }
    }

    private void editRestaurantInfo(final String content, final int changeType) {
        IEditRestaurantInfoPresent present = new EditRestaurantInfoPresent(this, new EditRestaurantInfoView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_loading), true);
            }

            @Override
            public void loadSuccess() {
                ChangeInfoActivity.this.finish();
                EditRestaurantInfoEB editRestaurantInfoEB = new EditRestaurantInfoEB();
                editRestaurantInfoEB.changeContent = content;
                editRestaurantInfoEB.changeType = changeType;
                EventBus.getDefault().post(editRestaurantInfoEB);
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

    private void editPersonInfo(final String content, final int changeType) {
        IEditPersonInfoPresent present = new EditPersonInfoPresent(this, new EditPersonInfoView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_loading), true);
            }

            @Override
            public void loadSuccess() {
                ChangeInfoActivity.this.finish();
                EditPersonInfoEB editPersonInfoEB = new EditPersonInfoEB();
                editPersonInfoEB.changeContent = content;
                editPersonInfoEB.changeType = changeType;
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
        present.edit(content, changeType);
    }

}
