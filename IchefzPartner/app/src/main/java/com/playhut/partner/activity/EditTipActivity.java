package com.playhut.partner.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.business.ChooseAvatarBusiness;
import com.playhut.partner.constants.AddTipConstants;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.entity.PackTipEntity;
import com.playhut.partner.eventbus.AddPackEB;
import com.playhut.partner.mvp.presenter.GetPackTipPresent;
import com.playhut.partner.mvp.presenter.IDeleteTipPresent;
import com.playhut.partner.mvp.presenter.IEditTipPresent;
import com.playhut.partner.mvp.presenter.IGetPackTipPresent;
import com.playhut.partner.mvp.presenter.impl.DeleteTipPresent;
import com.playhut.partner.mvp.presenter.impl.EditTipPresent;
import com.playhut.partner.mvp.view.DeleteTipView;
import com.playhut.partner.mvp.view.EditTipView;
import com.playhut.partner.mvp.view.GetPackTipView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.ui.AddTipsItemView;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.PartnerTitleBar;

import org.json.JSONArray;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 *
 */
public class EditTipActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mContainerLayout;

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

    private TextView mNoDataTv;

    private RelativeLayout mNoDataLayout;

    private int mItemNum;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_edit_tip);
        mContainerLayout = (LinearLayout) findViewById(R.id.ll_container);
        mNextBtn = (Button) findViewById(R.id.btn_next);
        mNoDataTv = (TextView) findViewById(R.id.tv_no_data);
        mNoDataLayout = (RelativeLayout) findViewById(R.id.rl_no_data);
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
                EditTipActivity.this.finish();
            }
        });
        titleBar.setRightTv2Visiable(false);
        titleBar.setRightTv2Content("Add");
        titleBar.setRightTv2Listener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(EditTipActivity.this, EditAddTipActivity.class);
                intent.putExtra(EditAddTipActivity.MENU_ID_INTENT, mMenuId);
                intent.putExtra(EditAddTipActivity.ADD_TYPE_INTENT, mType);
                intent.putExtra(EditAddTipActivity.HAD_ADD_ITEM_NUM, mItemNum);
                startActivity(intent);
                EditTipActivity.this.finish();
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

        mNextBtn.setOnClickListener(this);
    }

    @Override
    protected void initLogic() {
        getPackTip();
    }

    private void getPackTip() {
        IGetPackTipPresent present = new GetPackTipPresent(this, new GetPackTipView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_loading), true);
            }

            @Override
            public void loadSuccess(PackTipEntity entity) {
                addTipDataToLayout(entity);
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
        present.get(mMenuId, mType);
    }

    private void addTipDataToLayout(PackTipEntity entity) {
        List<PackTipEntity.Tip> list = entity.apron_imgs;
        if (list != null) {
            mItemNum = list.size();
            if (list.size() > 0) {
                for (PackTipEntity.Tip tip : list) {
                    AddTipsItemView addTipsItemView = new AddTipsItemView(this);
                    addTipsItemView.setChooseAvatarId(mChooseAvatarID);
                    mChooseAvatarID++;
                    addTipsItemView.setImageView(tip.img);
                    addTipsItemView.setAddTipDeleteListener(new ItemDeleteListener());
                    addTipsItemView.setAddTipClickListener(new ImageClickListener());
                    addTipsItemView.setTitle(tip.title);
                    addTipsItemView.setDesc(tip.description);
                    addTipsItemView.setItemId(tip.id);
                    mContainerLayout.addView(addTipsItemView);
                }
            }
        }
        setLayoutState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                checkEditInputInfo();
                break;
        }
    }

    private class ImageClickListener implements AddTipsItemView.AddTipClickListener {
        @Override
        public void onClick(final AddTipsItemView addTipsItemView) {
            int id = addTipsItemView.getChooseAvatarId();
            mClickImageChooseBusiness = new ChooseAvatarBusiness(EditTipActivity.this, width, height, id, id + 400, id + 900);
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
        public void onDelete(AddTipsItemView item) {
            toDeleteTip(item.getItemId(), item);
        }
    }

    private void toDeleteTip(String id, final AddTipsItemView item) {
        IDeleteTipPresent present = new DeleteTipPresent(this, new DeleteTipView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_loading), true);
            }

            @Override
            public void loadSuccess() {
                mContainerLayout.removeView(item);
                setLayoutState();
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
        present.delete(mMenuId, mType, id);
    }

    private void checkEditInputInfo() {
        int childCount = mContainerLayout.getChildCount();
        JSONArray idArray = new JSONArray();
        JSONArray titleArray = new JSONArray();
        JSONArray descArray = new JSONArray();
        for (int i = 0; i < childCount; i++) {
            AddTipsItemView addTipsItemView = (AddTipsItemView) mContainerLayout.getChildAt(i);
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
            idArray.put(addTipsItemView.getItemId());
            titleArray.put(title);
            descArray.put(desc);
        }

        if (idArray.length() == childCount && titleArray.length() == childCount && descArray.length() == childCount) {
            // 所有数据都不是空
            Map<String, File> map = new HashMap<>();
            for (int i = 0; i < childCount; i++) {
                AddTipsItemView addTipsItemView = (AddTipsItemView) mContainerLayout.getChildAt(i);
                File file = addTipsItemView.getImageFile();
                if (file != null && file.isFile() && file.exists()) {
                    // 编辑过该张图片
                    map.put("image" + (i + 1), file);
                }
            }
            toEditPackTips(idArray.toString(), map, titleArray.toString(), descArray.toString());
        }
    }

    private void toEditPackTips(String ids, Map<String, File> map, String titles, String descs) {
        IEditTipPresent present = new EditTipPresent(this, new EditTipView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_loading), true);
            }

            @Override
            public void loadSuccess() {
                if (mType == ADD_TIPS_TYPE) {
                    ToastUtils.show("Edit tips & techniques successfully");
                } else if (mType == ADD_INGREDIENT_TYPE) {
                    ToastUtils.show("Edit ingredients successfully");
                } else {
                    ToastUtils.show("Edit instructions successfully");
                }

                EditTipActivity.this.finish();

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
        present.edit(mMenuId, mType, ids, map, titles, descs);
    }

    private void setLayoutState() {
        int childCount = mContainerLayout.getChildCount();
        if (childCount > 0) {
            setNoItemState(false);
        } else {
            setNoItemState(true);
        }

        if (childCount >= AddTipConstants.MAX_ITEM_NUM) {
            // 添加按钮不可见
            titleBar.setRightTv2Visiable(false);
        } else {
            titleBar.setRightTv2Visiable(true);
        }
    }

    private void setNoItemState(boolean noData) {
        if (noData) {
            mNoDataLayout.setVisibility(View.VISIBLE);
            if (mType == ADD_TIPS_TYPE) {
                mNoDataTv.setText("There is no tips & techniques");
            } else if (mType == ADD_INGREDIENT_TYPE) {
                mNoDataTv.setText("There is no ingredients");
            } else {
                mNoDataTv.setText("There is no instructions");
            }
            mNextBtn.setVisibility(View.GONE);
        } else {
            mNoDataLayout.setVisibility(View.GONE);
            mNextBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mClickImageChooseBusiness != null) {
            mClickImageChooseBusiness.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
