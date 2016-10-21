package com.playhut.partner.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.adapter.AddSetAdapter;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.business.SelectCountryBusiness;
import com.playhut.partner.constants.ChangeTypeConstants;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.entity.FinanceDateEntity;
import com.playhut.partner.entity.SelectPackEntity;
import com.playhut.partner.widget.PartnerTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AddSetActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private FrameLayout mSetImageLayout;

    private ImageView mCameraIv;

    private String mSetNameStr;

    private String mDescStr;

    private TextView mSetNameTv;

    private TextView mDescTv;

    private SelectCountryBusiness mSelectCountryBusiness;

    private List<FinanceDateEntity> mMaxQuantityList;

    private String[] mQuantityStrings = {"1", "2", "3", "4", "5"};

    private TextView mMaxQuantityTv;

    private Button mSaveBtn;

    private GridView mImageGv;

    private List<String> mImageUrlList;

    private AddSetAdapter mAdapter;

    private ArrayList<String> mSelectIdList;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_set);
        mSetImageLayout = (FrameLayout) findViewById(R.id.fl_set);
        mCameraIv = (ImageView) findViewById(R.id.iv_camera);
        mImageGv = (GridView) findViewById(R.id.gv_img);

        findViewById(R.id.rl_set_name).setOnClickListener(this);
        findViewById(R.id.rl_desc).setOnClickListener(this);
        findViewById(R.id.rl_max_quantity).setOnClickListener(this);

        mSetNameTv = (TextView) findViewById(R.id.tv_set_name);
        mDescTv = (TextView) findViewById(R.id.tv_desc);
        mMaxQuantityTv = (TextView) findViewById(R.id.tv_max_quantity);
        mSaveBtn = (Button) findViewById(R.id.btn_next);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Add more to set");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                AddSetActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        int width = GlobalConstants.SCREEN_WIDTH;
        int height = width / 2;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mSetImageLayout.getLayoutParams();
        params.width = width;
        params.height = height;
        mSetImageLayout.setLayoutParams(params);

        mCameraIv.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
        mImageGv.setOnItemClickListener(this);
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
        mSelectIdList = new ArrayList<>();
        mImageUrlList = new ArrayList<>();
        mAdapter = new AddSetAdapter(this, mImageUrlList);
        mImageGv.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_camera:
                Intent intent = new Intent(this, SelectPackActivity.class);
                startActivityForResult(intent, SelectPackActivity.SELECT_PACK_REQUEST_CODE);
                break;
            case R.id.rl_set_name:
                Intent setNameIntent = new Intent(this, ChangeInfoActivity.class);
                setNameIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.ADD_SET_NAME);
                startActivityForResult(setNameIntent, ChangeInfoActivity.ADD_SET_REQUEST_CODE);
                break;
            case R.id.rl_desc:
                Intent descIntent = new Intent(this, ChangeInfoActivity.class);
                descIntent.putExtra(ChangeInfoActivity.CHANGE_TYPE_INTENT, ChangeTypeConstants.ADD_SET_DESC);
                startActivityForResult(descIntent, ChangeInfoActivity.ADD_SET_REQUEST_CODE);
                break;
            case R.id.rl_max_quantity:
                mSelectCountryBusiness.showDialog(mMaxQuantityList, new SelectCountryBusiness.SelectCountryOkListener() {
                    @Override
                    public void onOk(String selectCountry) {
                        mMaxQuantityTv.setText(selectCountry);
                    }
                });
                break;
            case R.id.btn_next:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ChangeInfoActivity.ADD_SET_REQUEST_CODE && data != null) {
            String saveString = data.getStringExtra(ChangeInfoActivity.SAVE_CONTENT_RETURN_INTENT);
            switch (resultCode) {
                case ChangeInfoActivity.ADD_SET_NAME_RESULT_CODE:
                    mSetNameStr = saveString;
                    mSetNameTv.setText(mSetNameStr);
                    break;
                case ChangeInfoActivity.ADD_SET_DESC_RESULT_CODE:
                    mDescStr = saveString;
                    mDescTv.setText(mDescStr);
                    break;
            }
        }
        if (requestCode == SelectPackActivity.SELECT_PACK_REQUEST_CODE
                && resultCode == SelectPackActivity.SELECT_PACK_RESULT_CODE
                && data != null) {
            List<SelectPackEntity.Packs> selectPackList = (List<SelectPackEntity.Packs>) data.getSerializableExtra(SelectPackActivity.SELECT_PACK_RETURN_INTENT);
            mImageUrlList.clear();
            mSelectIdList.clear();
            if (selectPackList != null && selectPackList.size() > 0) {
                mImageGv.setVisibility(View.VISIBLE);
                for (SelectPackEntity.Packs packs : selectPackList) {
                    mImageUrlList.add(packs.img);
                    mSelectIdList.add(packs.pack_id);
                }
            } else {
                mImageGv.setVisibility(View.GONE);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, SelectPackActivity.class);
        if (mSelectIdList != null && mSelectIdList.size() > 0) {
            intent.putStringArrayListExtra(SelectPackActivity.SELECT_PACK_INIT_ID_INTENT, mSelectIdList);
        }
        startActivityForResult(intent, SelectPackActivity.SELECT_PACK_REQUEST_CODE);
    }

}
