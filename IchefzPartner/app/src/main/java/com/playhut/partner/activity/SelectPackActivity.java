package com.playhut.partner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.playhut.partner.R;
import com.playhut.partner.adapter.SelectPackAdapter;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.business.LoadFailureBusiness;
import com.playhut.partner.constants.NetworkConstants;
import com.playhut.partner.entity.SelectPackEntity;
import com.playhut.partner.mvp.presenter.ISelectPackPresent;
import com.playhut.partner.mvp.presenter.impl.SelectPackPresent;
import com.playhut.partner.mvp.view.SelectPackView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.recyclerview.IRecyclerViewItemClick;
import com.playhut.partner.recyclerview.SelectPackItemDe;
import com.playhut.partner.ui.IchefzStateView;
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

    private IchefzStateView mIchefzStateView;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_pack);
        mSelectPackRv = (RecyclerView) findViewById(R.id.rv_select_pack);
        mIchefzStateView = (IchefzStateView) findViewById(R.id.state_view);
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

        getList();
    }

    private void getList(){
        ISelectPackPresent present = new SelectPackPresent(this, new SelectPackView() {
            @Override
            public void startLoading() {
                mIchefzStateView.showLoadingView();
            }

            @Override
            public void loadSuccess(SelectPackEntity entity) {
                List<SelectPackEntity.Packs> list = entity.packs;
                mList.clear();
                if (list != null && list.size() > 0){
                    mList.addAll(list);
                } else {
                    mIchefzStateView.showNoItemView("No pack can select");
                }
                for (SelectPackEntity.Packs packs: mList){
                    if (mSelectIdList != null && mSelectIdList.contains(packs.pack_id)){
                        packs.isCheck = true;
                    } else {
                        packs.isCheck = false;
                    }
                }
                mAdapter.notifyDataSetChanged();
                setOkBtnState();
            }

            @Override
            public void finishLoading() {
                mIchefzStateView.dismissLoadingView();
            }

            @Override
            public void loadFailure(IchefzException exception) {
                LoadFailureBusiness.loadFailure(SelectPackActivity.this, exception, mIchefzStateView, new SelectPackReloadListener());
            }
        });
        present.getList();
    }

    private class SelectPackReloadListener implements LoadFailureBusiness.ReloadListener {
        @Override
        public void onReload() {
            getList();
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        // 必须选择大于等于4个
        SelectPackEntity.Packs packs = mList.get(position);
        packs.isCheck = !packs.isCheck;
        mAdapter.notifyItemChanged(position);
        setOkBtnState();
    }

    private void setOkBtnState(){
        if (getCheckCount() >= 4) {
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
