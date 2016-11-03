package com.playhut.partner.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.playhut.partner.entity.MyMenuSetEntity;
import com.playhut.partner.entity.SelectPackEntity;
import com.playhut.partner.eventbus.AddSetEB;
import com.playhut.partner.mvp.presenter.IAddSetPresent;
import com.playhut.partner.mvp.presenter.IEditSetPresent;
import com.playhut.partner.mvp.presenter.impl.AddSetPresent;
import com.playhut.partner.mvp.presenter.impl.EditSetPresent;
import com.playhut.partner.mvp.view.AddSetView;
import com.playhut.partner.mvp.view.EditSetView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.PartnerTitleBar;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

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

    private EditText mPerson2Et;

    private EditText mPerson4Et;

    private static MyMenuSetEntity.SetInfo mSetInfo;

    private ImageView mPerson2Cb;

    private ImageView mPerson4Cb;

    public static void actionIntent(Context context, MyMenuSetEntity.SetInfo set) {
        mSetInfo = set;
        context.startActivity(new Intent(context, AddSetActivity.class));
    }

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
        mSaveBtn = (Button) findViewById(R.id.btn_save);

        mPerson2Et = (EditText) findViewById(R.id.et_person2);
        mPerson4Et = (EditText) findViewById(R.id.et_person4);

        mPerson2Cb = (ImageView) findViewById(R.id.cb_person2);
        mPerson4Cb = (ImageView) findViewById(R.id.cb_person4);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        if (mSetInfo == null) {
            titleBar.setCenterTvContent("Add more to set");
        } else {
            titleBar.setCenterTvContent("Edit set");
        }
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
        mPerson2Cb.setOnClickListener(this);
        mPerson4Cb.setOnClickListener(this);
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

        if (mSetInfo != null) {
            List<MyMenuSetEntity.PackInfo> list = mSetInfo.sets;
            if (list != null && list.size() > 0) {
                for (MyMenuSetEntity.PackInfo packInfo : list) {
                    mImageUrlList.add(packInfo.pack_img);
                    mSelectIdList.add(packInfo.pack_id);
                }
            }
            mImageGv.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();

            mSetNameStr = mSetInfo.set_title;
            if (!TextUtils.isEmpty(mSetNameStr)) {
                mSetNameTv.setText(mSetNameStr);
            }

            mDescStr = mSetInfo.set_desc;
            if (!TextUtils.isEmpty(mDescStr)) {
                mDescTv.setText(mDescStr);
            }

            String person2 = mSetInfo.person2;
            if ("0".equals(person2)) {
                changePerson2State(false);
            } else {
                changePerson2State(true);
                mPerson2Et.setText(person2);
                mPerson2Et.setSelection(person2.length());
            }

            String person4 = mSetInfo.person4;
            if ("0".equals(person4)) {
                changePerson4State(false);
            } else {
                changePerson4State(true);
                mPerson4Et.setText(person4);
                mPerson4Et.setSelection(person4.length());
            }

            String maxQuantity = mSetInfo.max_quantity;
            if (!TextUtils.isEmpty(maxQuantity)) {
                mMaxQuantityTv.setText(maxQuantity);
            }
        } else {
            mImageGv.setVisibility(View.GONE);
        }
    }

    private void changePerson2State(boolean check) {
        if (check) {
            mPerson2Cb.setImageResource(R.mipmap.menu_price_cb_check);
            mPerson2Et.setVisibility(View.VISIBLE);
        } else {
            mPerson2Cb.setImageResource(R.mipmap.menu_price_cb_uncheck);
            mPerson2Et.setText("");
            mPerson2Et.setVisibility(View.GONE);
        }
    }

    private void changePerson4State(boolean check) {
        if (check) {
            mPerson4Cb.setImageResource(R.mipmap.menu_price_cb_check);
            mPerson4Et.setVisibility(View.VISIBLE);
        } else {
            mPerson4Cb.setImageResource(R.mipmap.menu_price_cb_uncheck);
            mPerson4Et.setText("");
            mPerson4Et.setVisibility(View.GONE);
        }
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
            case R.id.btn_save:
                checkInputInfo();
                break;
            case R.id.cb_person2:
                switch (mPerson2Et.getVisibility()){
                    case View.VISIBLE:
                        changePerson2State(false);
                        break;
                    case View.GONE:
                        changePerson2State(true);
                        break;
                }
                break;
            case R.id.cb_person4:
                switch (mPerson4Et.getVisibility()){
                    case View.VISIBLE:
                        changePerson4State(false);
                        break;
                    case View.GONE:
                        changePerson4State(true);
                        break;
                }
                break;
        }
    }

    private void checkInputInfo() {
        if (mImageUrlList.size() == 0) {
            ToastUtils.show("Please select from pack");
            return;
        }
        if (TextUtils.isEmpty(mSetNameStr)) {
            ToastUtils.show("Set name cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(mDescStr)) {
            ToastUtils.show("Description cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(mMaxQuantityTv.getText().toString())) {
            ToastUtils.show("Maximum quantity cannot be empty");
            return;
        }

        String person2 = mPerson2Et.getText().toString().trim();
        String person4 = mPerson4Et.getText().toString().trim();
        if (TextUtils.isEmpty(person2) && TextUtils.isEmpty(person4)) {
            ToastUtils.show("2 Persons and 4 Persons cannot be empty at the same time");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < mSelectIdList.size(); i++) {
            String id = mSelectIdList.get(i);
            sb.append("\"");
            sb.append(id);
            sb.append("\"");
            if (i != mSelectIdList.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");

        if (mSetInfo == null) {
            toAddSet(sb.toString(), person2, person4);
        } else {
            toEditSet(mSetInfo.set_id, sb.toString(), person2, person4);
        }
    }

    private void toEditSet(String menuId, String ids, String person2, String person4) {
        IEditSetPresent present = new EditSetPresent(this, new EditSetView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_loading), true);
            }

            @Override
            public void loadSuccess() {
                changeSuccess();
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
        present.edit(menuId, ids, mSetNameStr, mDescStr, person2, person4, mMaxQuantityTv.getText().toString());
    }

    private void toAddSet(String ids, String person2, String person4) {
        IAddSetPresent present = new AddSetPresent(this, new AddSetView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_loading), true);
            }

            @Override
            public void loadSuccess() {
                changeSuccess();
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
        present.add(ids, mSetNameStr, mDescStr, person2, person4, mMaxQuantityTv.getText().toString());
    }

    private void changeSuccess(){
        if (mSetInfo == null){
            ToastUtils.show("Add set successfully");
        } else {
            ToastUtils.show("Edit set successfully");
        }
        AddSetActivity.this.finish();
        AddSetEB addSetEB = new AddSetEB();
        addSetEB.tag = AddSetEB.ADD_SUCCESS;
        EventBus.getDefault().post(addSetEB);
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
