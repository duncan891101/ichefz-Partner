package com.playhut.partner.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.base.PartnerApplication;
import com.playhut.partner.mvp.presenter.IModifyPwdPresent;
import com.playhut.partner.mvp.presenter.impl.ModifyPwdPresent;
import com.playhut.partner.mvp.view.ModifyPwdView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.AccountUtils;
import com.playhut.partner.utils.PwdUtils;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.PartnerTitleBar;

import de.greenrobot.event.EventBus;

/**
 *
 */
public class ModifyPwdActivity extends BaseActivity implements View.OnClickListener{

    private EditText mOldPwdEt;

    private EditText mNewPwdEt;

    private EditText mConfirmNewPwdEt;

    private Button mSaveBtn;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_modify_pwd);
        mOldPwdEt = (EditText) findViewById(R.id.et_original_pwd);
        mNewPwdEt = (EditText) findViewById(R.id.et_new_pwd);
        mConfirmNewPwdEt = (EditText) findViewById(R.id.et_confirm_new_pwd);
        mSaveBtn = (Button) findViewById(R.id.btn_save);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Modify password");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                ModifyPwdActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        mOldPwdEt.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        mNewPwdEt.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        mConfirmNewPwdEt.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);

        mSaveBtn.setOnClickListener(this);
    }

    @Override
    protected void initLogic() {

    }

    @Override
    public void onClick(View v) {
        String oldPwd = mOldPwdEt.getText().toString().trim();
        String newPwd = mNewPwdEt.getText().toString().trim();
        String confirmNewPwd = mConfirmNewPwdEt.getText().toString().trim();
        if (TextUtils.isEmpty(oldPwd)){
            ToastUtils.show("Old password cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(newPwd)){
            ToastUtils.show("New password cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(confirmNewPwd)){
            ToastUtils.show("Confirm new password cannot be empty");
            return;
        }
        if (newPwd.equals(confirmNewPwd)){
            toModifyPwd(oldPwd, newPwd);
        } else {
            ToastUtils.show("The new password is different");
        }
    }

    private void toModifyPwd(String oldPwd, String newPwd){
        IModifyPwdPresent present = new ModifyPwdPresent(this, new ModifyPwdView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_loading), false);
            }

            @Override
            public void loadSuccess(String info) {
                AccountUtils.deleteAccount();
                PartnerApplication.mAccount = null;
                ToastUtils.show("Modify password successfully");
                startActivity(new Intent(ModifyPwdActivity.this, LoginActivity.class));
                ModifyPwdActivity.this.finish();
                EventBus.getDefault().post(PersonInfoActivity.FINISH_PERSON_EVENT_BUS_TAG);
                EventBus.getDefault().post(RestaurantSettingActivity.FINISH_RESTAURANT_EVENT_BUS_TAG);
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
        });
        present.modify(PwdUtils.getEncryptPwd(oldPwd), PwdUtils.getEncryptPwd(newPwd));
    }

}
