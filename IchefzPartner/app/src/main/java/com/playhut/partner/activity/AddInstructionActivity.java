package com.playhut.partner.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.business.ChooseAvatarBusiness;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.ui.AddTipsItemView;
import com.playhut.partner.widget.PartnerTitleBar;

import java.io.File;

/**
 *
 */
public class AddInstructionActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mContainerLayout;

    private RelativeLayout mAddLayout;

    private ChooseAvatarBusiness mChooseAvatarBusiness;

    private static final int MAX_ITEM_NUM = 6;

    private Button mNextBtn;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_tips);
        mContainerLayout = (LinearLayout) findViewById(R.id.ll_container);
        mAddLayout = (RelativeLayout) findViewById(R.id.rl_add_content);
        mNextBtn = (Button) findViewById(R.id.btn_next);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent(getString(R.string.chef_detail_instructions));
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                AddInstructionActivity.this.finish();
            }
        });
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
        int width = GlobalConstants.SCREEN_WIDTH;
        int height = width / 2;

        mAddLayout.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mNextBtn.setText("Finish");
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
                        AddTipsItemView addTipsItemView = new AddTipsItemView(AddInstructionActivity.this);
                        addTipsItemView.setImageView(bitmap);
                        addTipsItemView.setAddTipDeleteListener(new ItemDeleteListener());
                        mContainerLayout.addView(addTipsItemView);
                        setAddLayoutState();
                    }
                });
                break;
            case R.id.btn_next:
                jumpToNext();
                break;
        }
    }

    private class ItemDeleteListener implements AddTipsItemView.AddTipDeleteListener {
        @Override
        public void onDelete(LinearLayout item) {
            mContainerLayout.removeView(item);
            setAddLayoutState();
        }
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
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void jumpToNext() {
        AddInstructionActivity.this.finish();
    }

}
