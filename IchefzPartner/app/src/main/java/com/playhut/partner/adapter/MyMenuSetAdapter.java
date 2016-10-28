package com.playhut.partner.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.activity.LoginActivity;
import com.playhut.partner.activity.MainActivity;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.constants.PackState;
import com.playhut.partner.entity.MyMenuSetEntity;
import com.playhut.partner.mvp.presenter.IDeleteMenuPresent;
import com.playhut.partner.mvp.presenter.impl.DeleteMenuPresent;
import com.playhut.partner.mvp.view.DeleteMenuView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.DialogUtils;
import com.playhut.partner.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 *
 */
public class MyMenuSetAdapter extends BaseAdapter {

    private Context mContext;

    private List<MyMenuSetEntity.SetInfo> mList;

    private Dialog mConfirmDialog;

    private DeleteSuccessListener mDeleteSuccessListener;

    public MyMenuSetAdapter(Context context, List<MyMenuSetEntity.SetInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        ExpandClickListener expandClickListener = null;
        DeleteListener deleteListener = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.my_menu_set_item_layout, null);
            holder = new Holder();

            holder.mRecyclerView = (RecyclerView) convertView.findViewById(R.id.rv_menu);
            // 由于外层ScrollView嵌套了RecyclerView，如果不禁止RecyclerView滑动的话，在5.0以上机器就出现滑动卡顿的现象
            GridLayoutManager manager = new GridLayoutManager(mContext, 2) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            holder.mRecyclerView.setLayoutManager(manager);
            holder.mAdapter = new NewOrderSetAdapter(mContext);
            holder.mRecyclerView.setAdapter(holder.mAdapter);

            holder.mTitleTv = (TextView) convertView.findViewById(R.id.tv_title);
            holder.mPerson2Tv = (TextView) convertView.findViewById(R.id.tv_person2);
            holder.mPerson4Tv = (TextView) convertView.findViewById(R.id.tv_person4);

            holder.mArrowIv = (ImageView) convertView.findViewById(R.id.iv_arrow);
            expandClickListener = new ExpandClickListener();
            holder.mArrowIv.setOnClickListener(expandClickListener);

            holder.mHintLayout = (LinearLayout) convertView.findViewById(R.id.ll_hint_layout);

            holder.mDeleteBtn = (Button) convertView.findViewById(R.id.btn_delete);
            deleteListener = new DeleteListener();
            holder.mDeleteBtn.setOnClickListener(deleteListener);

            holder.mEditBtn = (Button) convertView.findViewById(R.id.btn_edit);
            holder.mStateCb = (CheckBox) convertView.findViewById(R.id.cb_state);
            holder.mEditLayout = (RelativeLayout) convertView.findViewById(R.id.rl_edit);
            holder.mCancelBtn = (Button) convertView.findViewById(R.id.btn_cancel);
            holder.mTagIv = (ImageView) convertView.findViewById(R.id.iv_tag);
            holder.mCbLayout = (RelativeLayout) convertView.findViewById(R.id.rl_cb);

            convertView.setTag(holder);
            convertView.setTag(holder.mArrowIv.getId(), expandClickListener);
            convertView.setTag(holder.mDeleteBtn.getId(), deleteListener);
        } else {
            holder = (Holder) convertView.getTag();
            expandClickListener = (ExpandClickListener) convertView.getTag(holder.mArrowIv.getId());
            deleteListener = (DeleteListener) convertView.getTag(holder.mDeleteBtn.getId());
        }

        expandClickListener.setPosition(position);
        deleteListener.setPosition(position);

        MyMenuSetEntity.SetInfo setInfo = mList.get(position);

        List<String> urlList = new ArrayList<>();
        List<MyMenuSetEntity.PackInfo> packList = setInfo.sets;
        if (packList != null && packList.size() > 0) {
            for (MyMenuSetEntity.PackInfo packInfo : packList) {
                urlList.add(packInfo.pack_img);
            }
        }
        holder.mAdapter.setData(urlList);

        holder.mTitleTv.setText(setInfo.set_title);

        String person2Str = String.format(mContext.getString(R.string.my_menu_person2), setInfo.person2);
        holder.mPerson2Tv.setText(person2Str);

        String person4Str = String.format(mContext.getString(R.string.my_menu_person4), setInfo.person4);
        holder.mPerson4Tv.setText(person4Str);

        boolean expandState = setInfo.expandState;
        if (expandState) {
            // 展开
            holder.mArrowIv.setImageResource(R.mipmap.arrow_up);
            holder.mHintLayout.setVisibility(View.VISIBLE);
        } else {
            holder.mArrowIv.setImageResource(R.mipmap.arrow_down);
            holder.mHintLayout.setVisibility(View.GONE);
        }

        int setState = setInfo.set_state;
        switch (setState) {
            case PackState.CLOSE_STATE:
                // 审核成功，下架状态
                holder.mTagIv.setVisibility(View.GONE);
                holder.mCbLayout.setVisibility(View.VISIBLE);
                holder.mStateCb.setVisibility(View.VISIBLE);
                holder.mStateCb.setChecked(false);
                holder.mCancelBtn.setVisibility(View.GONE);
                holder.mEditLayout.setVisibility(View.VISIBLE);
                break;
            case PackState.OPEN_STATE:
                // 审核成功，上架状态
                holder.mTagIv.setVisibility(View.GONE);
                holder.mCbLayout.setVisibility(View.VISIBLE);
                holder.mStateCb.setVisibility(View.VISIBLE);
                holder.mStateCb.setChecked(true);
                holder.mCancelBtn.setVisibility(View.GONE);
                holder.mEditLayout.setVisibility(View.VISIBLE);
                break;
            case PackState.NOT_AUDIT_STATE:
                // 未审核
                holder.mTagIv.setVisibility(View.VISIBLE);
                holder.mTagIv.setImageResource(R.mipmap.to_be_verified);
                holder.mCbLayout.setVisibility(View.VISIBLE);
                holder.mStateCb.setVisibility(View.GONE);
                holder.mCancelBtn.setVisibility(View.VISIBLE);
                holder.mCancelBtn.setText("Submit");
                holder.mEditLayout.setVisibility(View.VISIBLE);
                break;
            case PackState.AUDIT_ING_STATE:
                // 审核中
                holder.mTagIv.setVisibility(View.VISIBLE);
                holder.mTagIv.setImageResource(R.mipmap.being_verified);
                holder.mCbLayout.setVisibility(View.VISIBLE);
                holder.mStateCb.setVisibility(View.GONE);
                holder.mCancelBtn.setVisibility(View.VISIBLE);
                holder.mCancelBtn.setText("Cancel");
                holder.mEditLayout.setVisibility(View.GONE);
                break;
            case PackState.AUDIT_FAIL_STATE:
                // 审核失败
                holder.mTagIv.setVisibility(View.VISIBLE);
                holder.mTagIv.setImageResource(R.mipmap.verification_rejected);
                holder.mCbLayout.setVisibility(View.GONE);
                holder.mStateCb.setVisibility(View.GONE);
                holder.mCancelBtn.setVisibility(View.GONE);
                holder.mEditLayout.setVisibility(View.VISIBLE);
                break;
        }

        return convertView;
    }

    private class ExpandClickListener implements View.OnClickListener {

        private int mPosition;

        public void setPosition(int position) {
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            MyMenuSetEntity.SetInfo setInfo = mList.get(mPosition);
            setInfo.expandState = !setInfo.expandState;
            notifyDataSetChanged();
        }

    }

    private class DeleteListener implements View.OnClickListener {

        private int mPosition;

        public void setPosition(int position) {
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            showConfirmDialog(mPosition);
        }
    }

    private void showConfirmDialog(final int position) {
        if (mConfirmDialog == null || !mConfirmDialog.isShowing()) {
            mConfirmDialog = DialogUtils.showConfirmDialog(mContext, R.layout.confirm_dialog_layout, true);
        }
        TextView titleTv = (TextView) mConfirmDialog.findViewById(R.id.tv_title);
        titleTv.setText("Delete");
        TextView textTv = (TextView) mConfirmDialog.findViewById(R.id.tv_text);
        textTv.setText("Are you sure to delete the set menu?");
        TextView confirmTv = (TextView) mConfirmDialog.findViewById(R.id.tv_confirm);
        confirmTv.setText("Delete");
        mConfirmDialog.findViewById(R.id.rl_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消
                dismissConfirmDialog();
            }
        });
        mConfirmDialog.findViewById(R.id.rl_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 登录
                dismissConfirmDialog();
                toDelete(position);
            }
        });
    }

    private void toDelete(final int position) {
        String menuId = mList.get(position).set_id;
        IDeleteMenuPresent present = new DeleteMenuPresent(mContext, new DeleteMenuView() {
            @Override
            public void startLoading() {
                BaseActivity baseActivity = (BaseActivity) mContext;
                baseActivity.showLoadingDialog(mContext.getString(R.string.loading_dialog_delete), true);
            }

            @Override
            public void loadSuccess(String info) {
                if (mDeleteSuccessListener != null){
                    mDeleteSuccessListener.delete(info, position);
                }
            }

            @Override
            public void finishLoading() {
                BaseActivity baseActivity = (BaseActivity) mContext;
                baseActivity.dismissLoadingDialog();
            }

            @Override
            public void loadFailure(IchefzException exception) {
                ToastUtils.show(exception.getErrorMsg());
            }
        });
        present.delete(menuId, "set");
    }

    private void dismissConfirmDialog() {
        if (mConfirmDialog != null && mConfirmDialog.isShowing()) {
            mConfirmDialog.dismiss();
            mConfirmDialog = null;
        }
    }

    public interface DeleteSuccessListener {
        void delete(String info, int position);
    }

    public void setDeleteSuccessListener(DeleteSuccessListener deleteSuccessListener){
        this.mDeleteSuccessListener = deleteSuccessListener;
    }

    static class Holder {
        private RecyclerView mRecyclerView;
        private TextView mTitleTv;
        private TextView mPerson2Tv;
        private TextView mPerson4Tv;
        private ImageView mArrowIv;
        private LinearLayout mHintLayout;
        private Button mDeleteBtn;
        private Button mEditBtn;
        private CheckBox mStateCb;
        private NewOrderSetAdapter mAdapter;
        private RelativeLayout mEditLayout;
        private Button mCancelBtn;
        private ImageView mTagIv;
        private RelativeLayout mCbLayout;
    }

}
