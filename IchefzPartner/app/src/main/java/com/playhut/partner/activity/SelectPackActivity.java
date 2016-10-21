package com.playhut.partner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.playhut.partner.R;
import com.playhut.partner.adapter.SelectPackAdapter;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.entity.SelectPackEntity;
import com.playhut.partner.recyclerview.IRecyclerViewItemClick;
import com.playhut.partner.recyclerview.SelectPackItemDe;
import com.playhut.partner.utils.PartnerUtils;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.PartnerTitleBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SelectPackActivity extends BaseActivity implements IRecyclerViewItemClick {

    private RecyclerView mSelectPackRv;

    private PartnerTitleBar titleBar;

    private SelectPackAdapter mAdapter;

    private List<SelectPackEntity.Packs> mList;

    public static final int SELECT_PACK_REQUEST_CODE = 11996;

    public static final int SELECT_PACK_RESULT_CODE = 11999;

    public static final String SELECT_PACK_RETURN_INTENT = "mSelectPackReturnIntent";

    public static final String SELECT_PACK_INIT_ID_INTENT = "mSelectPackInitIdIntent";

    private ArrayList<String> mSelectIdList;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_pack);
        mSelectPackRv = (RecyclerView) findViewById(R.id.rv_select_pack);
    }

    @Override
    protected void initTitleBar() {
        titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Select from pack");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                SelectPackActivity.this.finish();
            }
        });
        titleBar.setRightTv2Visiable(false);
        titleBar.setRightTv2Content("Ok");
        titleBar.setRightTv2Listener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                selectImageFinish();
            }
        });
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            mSelectIdList = intent.getStringArrayListExtra(SELECT_PACK_INIT_ID_INTENT);
        }

        mSelectPackRv.setLayoutManager(new GridLayoutManager(this, 2));
        mSelectPackRv.addItemDecoration(new SelectPackItemDe(PartnerUtils.dip2px(this, 10), PartnerUtils.dip2px(this, 5)));
    }

    @Override
    protected void initLogic() {
        mAdapter = new SelectPackAdapter(this, mList);
        mAdapter.setRecyclerViewItemClick(this);
        mSelectPackRv.setAdapter(mAdapter);

        for (int i = 0; i < 7; i++) {
            SelectPackEntity.Packs packs = new SelectPackEntity.Packs();
            packs.pack_id = String.valueOf(i + 2);
            if (i % 2 == 0) {
                packs.img = "drawable://" + R.mipmap.test1;
            } else {
                packs.img = "drawable://" + R.mipmap.avatar_test;
            }
            packs.title = "Burgers & Oven fries";
            if (mSelectIdList != null && mSelectIdList.size() > 0) {
                if (mSelectIdList.contains(packs.pack_id)) {
                    packs.isCheck = true;
                } else {
                    packs.isCheck = false;
                }
            } else {
                packs.isCheck = false;
            }
            mList.add(packs);
        }
        mAdapter.notifyDataSetChanged();

        setOkBtnState();
    }

    @Override
    public void onItemClick(View v, int position) {
        // 必须选择4个
        SelectPackEntity.Packs packs = mList.get(position);
        packs.isCheck = !packs.isCheck;
        int checkCount = getCheckCount();
        if (checkCount > 4) {
            packs.isCheck = false;
            ToastUtils.show(getString(R.string.select_pack_must_4));
        }
        mAdapter.notifyItemChanged(position);

        setOkBtnState();
    }

    private void setOkBtnState(){
        if (getCheckCount() == 4) {
            titleBar.setRightTv2Visiable(true);
        } else {
            titleBar.setRightTv2Visiable(false);
        }
    }

    private int getCheckCount() {
        int count = 0;
        for (SelectPackEntity.Packs packs : mList) {
            if (packs.isCheck)
                count++;
        }
        return count;
    }

    private void selectImageFinish() {
        List<SelectPackEntity.Packs> checkList = new ArrayList<>();
        for (SelectPackEntity.Packs packs : mList) {
            if (packs.isCheck) {
                checkList.add(packs);
            }
        }
        Intent intent = new Intent();
        intent.putExtra(SELECT_PACK_RETURN_INTENT, (Serializable) checkList);
        setResult(SELECT_PACK_RESULT_CODE, intent);
        SelectPackActivity.this.finish();
    }

}
