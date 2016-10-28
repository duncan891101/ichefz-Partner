package com.playhut.partner.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.business.ChooseAvatarBusiness;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.mvp.presenter.IAddPackTipsPresent;
import com.playhut.partner.mvp.presenter.impl.AddPackTipsPresent;
import com.playhut.partner.mvp.view.AddPackTipsView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.ui.AddTipsItemView;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.PartnerTitleBar;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AddTipsActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mContainerLayout;

    private RelativeLayout mAddLayout;

    private ChooseAvatarBusiness mChooseAvatarBusiness;

    private static final int MAX_ITEM_NUM = 6;

    private Button mNextBtn;

    public static final String MENU_ID_INTENT = "mMenuIdIntent";

    private String mMenuId;

    private int width;

    private int height;

    private ChooseAvatarBusiness mClickImageChooseBusiness;

    private int mChooseAvatarID = 100;

    public static final int ADD_TIPS_TYPE = 1;

    public static final int ADD_INGREDIENT_TYPE = 2;

    public static final int ADD_INSTRUCTION_TYPE = 3;

    private int mType = 1;

    public static final String ADD_TYPE_INTENT = "mAddTypeIntent";

    private PartnerTitleBar titleBar;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_tips);
        mContainerLayout = (LinearLayout) findViewById(R.id.ll_container);
        mAddLayout = (RelativeLayout) findViewById(R.id.rl_add_content);
        mNextBtn = (Button) findViewById(R.id.btn_next);
    }

    @Override
    protected void initTitleBar() {
        titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setRightTv2Visiable(true);
        titleBar.setRightTv2Content("Jump");
        titleBar.setRightTv2Listener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                jumpToNext();
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mMenuId = intent.getStringExtra(MENU_ID_INTENT);
            mType = intent.getIntExtra(ADD_TYPE_INTENT, ADD_TIPS_TYPE);
        }

        if (mType == ADD_TIPS_TYPE) {
            titleBar.setCenterTvContent(getString(R.string.chef_detail_tips));
        } else if (mType == ADD_INGREDIENT_TYPE) {
            titleBar.setCenterTvContent(getString(R.string.chef_detail_ingredients));
        } else if (mType == ADD_INSTRUCTION_TYPE) {
            titleBar.setCenterTvContent(getString(R.string.chef_detail_instructions));
        }

        width = GlobalConstants.SCREEN_WIDTH;
        height = (int) (width * 0.6f);

        mAddLayout.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mChooseAvatarBusiness = new ChooseAvatarBusiness(this, width, height);
    }

    @Override
    protected void initLogic() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_add_content:
                mChooseAvatarBusiness.showChooseAvatarDialog(new ChooseAvatarBusiness.UserSelectAvatarListener() {
                    @Override
                    public void select(Bitmap bitmap, File file) {
                        AddTipsItemView addTipsItemView = new AddTipsItemView(AddTipsActivity.this);
                        addTipsItemView.setId(mChooseAvatarID);
                        mChooseAvatarID++;
                        addTipsItemView.setImageView(bitmap, file);
                        addTipsItemView.setAddTipDeleteListener(new ItemDeleteListener());
                        addTipsItemView.setAddTipClickListener(new ImageClickListener());
                        mContainerLayout.addView(addTipsItemView);

                        setAddLayoutState();
                    }
                });
                break;
            case R.id.btn_next:
                checkInputInfo();
                break;
        }
    }

    private class ImageClickListener implements AddTipsItemView.AddTipClickListener {
        @Override
        public void onClick(final AddTipsItemView addTipsItemView) {
            int id = addTipsItemView.getId();
            mClickImageChooseBusiness = new ChooseAvatarBusiness(AddTipsActivity.this, width, height, id, id + 400, id + 900);
            mClickImageChooseBusiness.showChooseAvatarDialog(new ChooseAvatarBusiness.UserSelectAvatarListener() {
                @Override
                public void select(Bitmap bitmap, File file) {
                    addTipsItemView.setImageView(bitmap, file);
                }
            });
        }
    }

    private class ItemDeleteListener implements AddTipsItemView.AddTipDeleteListener {
        @Override
        public void onDelete(LinearLayout item) {
            mContainerLayout.removeView(item);
            setAddLayoutState();
        }
    }

    private void checkInputInfo() {
        List<File> fileList = new ArrayList<>();
        JSONArray titleArray = new JSONArray();
        JSONArray descArray = new JSONArray();
        int childCount = mContainerLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            AddTipsItemView addTipsItemView = (AddTipsItemView) mContainerLayout.getChildAt(i);
            File file = addTipsItemView.getImageFile();
            if (file == null || !file.isFile() || !file.exists()) {
                ToastUtils.show("Upload photo cannot be empty");
                break;
            }
            String title = addTipsItemView.getTitle();
            if (TextUtils.isEmpty(title)) {
                ToastUtils.show("Title cannot be empty");
                break;
            }
            String desc = addTipsItemView.getDesc();
            if (TextUtils.isEmpty(desc)) {
                ToastUtils.show("Description cannot be empty");
                break;
            }

            fileList.add(file);
            titleArray.put(title);
            descArray.put(desc);
        }

        if (fileList.size() == childCount && titleArray.length() == childCount && descArray.length() == childCount) {
            toAddPackTips(fileList, titleArray.toString(), descArray.toString());
        }
    }

    private void toAddPackTips(List<File> fileList, String titles, String descs) {
        IAddPackTipsPresent present = new AddPackTipsPresent(this, new AddPackTipsView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_loading), false);
            }

            @Override
            public void loadSuccess() {
                if (mType == ADD_TIPS_TYPE) {
                    ToastUtils.show("Add tips & techniques successfully");
                } else if (mType == ADD_INGREDIENT_TYPE) {
                    ToastUtils.show("Add ingredients successfully");
                } else if (mType == ADD_INSTRUCTION_TYPE) {
                    ToastUtils.show("Add instructions successfully");
                }
                jumpToNext();
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
        present.add(mMenuId, String.valueOf(mType), fileList, titles, descs);
    }

    private void setAddLayoutState() {
        int childCount = mContainerLayout.getChildCount();
        if (childCount >= MAX_ITEM_NUM) {
            mAddLayout.setVisibility(View.GONE);
        } else {
            mAddLayout.setVisibility(View.VISIBLE);
        }
        if (childCount > 0) {
            mNextBtn.setVisibility(View.VISIBLE);
        } else {
            mNextBtn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mChooseAvatarBusiness.onActivityResult(requestCode, resultCode, data);
        if (mClickImageChooseBusiness != null) {
            mClickImageChooseBusiness.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void jumpToNext() {
        if (mType == ADD_TIPS_TYPE){
            Intent intent = new Intent(this, AddTipsActivity.class);
            intent.putExtra(AddTipsActivity.MENU_ID_INTENT, mMenuId);
            intent.putExtra(AddTipsActivity.ADD_TYPE_INTENT, AddTipsActivity.ADD_INGREDIENT_TYPE);
            startActivity(intent);
            AddTipsActivity.this.finish();
        } else if (mType == ADD_INGREDIENT_TYPE){
            Intent intent = new Intent(this, AddTipsActivity.class);
            intent.putExtra(AddTipsActivity.MENU_ID_INTENT, mMenuId);
            intent.putExtra(AddTipsActivity.ADD_TYPE_INTENT, AddTipsActivity.ADD_INSTRUCTION_TYPE);
            startActivity(intent);
            AddTipsActivity.this.finish();
        } else if (mType == ADD_INSTRUCTION_TYPE){
            AddTipsActivity.this.finish();
        }
    }

}
