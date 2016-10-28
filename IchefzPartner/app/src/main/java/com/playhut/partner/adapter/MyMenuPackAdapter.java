package com.playhut.partner.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.constants.PackState;
import com.playhut.partner.entity.MyMenuPackEntity;
import com.playhut.partner.mvp.presenter.IChangeMenuStatePresent;
import com.playhut.partner.mvp.presenter.IDeleteMenuPresent;
import com.playhut.partner.mvp.presenter.impl.ChangeMenuStatePresent;
import com.playhut.partner.mvp.presenter.impl.DeleteMenuPresent;
import com.playhut.partner.mvp.view.ChangeMenuStateView;
import com.playhut.partner.mvp.view.DeleteMenuView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.DialogUtils;
import com.playhut.partner.utils.ImageLoderOptionUtils;
import com.playhut.partner.utils.ToastUtils;

import java.util.List;

/**
 *
 */
public class MyMenuPackAdapter extends BaseAdapter {

    private Context mContext;

    private List<MyMenuPackEntity.PackInfo> mList;

    private ImageLoader mImageLoader;

    private DisplayImageOptions mOptions;

    private Dialog mConfirmDialog;

    private DeleteSuccessListener mDeleteSuccessListener;

    public static final String CANCEL_BUTTON_TEXT = "Cancel";

    public static final String SUBMIT_BUTTON_TEXT = "Submit";

    public MyMenuPackAdapter(Context context, List<MyMenuPackEntity.PackInfo> list) {
        this.mContext = context;
        this.mList = list;
        mImageLoader = ImageLoader.getInstance();
        mOptions = ImageLoderOptionUtils.setOptions(R.drawable.loading_image_shape, 0, R.drawable.loading_image_shape);
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
        CancelListener cancelListener = null;
        CheckBoxChangeListener checkBoxChangeListener = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.my_menu_pack_item_layout, null);
            holder = new Holder();

            holder.mImageView = (ImageView) convertView.findViewById(R.id.iv_menu);
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

            holder.mStateCb = (ImageView) convertView.findViewById(R.id.cb_state);
            checkBoxChangeListener = new CheckBoxChangeListener();
            holder.mStateCb.setOnClickListener(checkBoxChangeListener);

            holder.mEditLayout = (RelativeLayout) convertView.findViewById(R.id.rl_edit);

            holder.mCancelBtn = (Button) convertView.findViewById(R.id.btn_cancel);
            cancelListener = new CancelListener();
            holder.mCancelBtn.setOnClickListener(cancelListener);

            holder.mTagIv = (ImageView) convertView.findViewById(R.id.iv_tag);
            holder.mCbLayout = (RelativeLayout) convertView.findViewById(R.id.rl_cb);

            convertView.setTag(holder);
            convertView.setTag(holder.mArrowIv.getId(), expandClickListener);
            convertView.setTag(holder.mDeleteBtn.getId(), deleteListener);
            convertView.setTag(holder.mCancelBtn.getId(), cancelListener);
            convertView.setTag(holder.mStateCb.getId(), checkBoxChangeListener);
        } else {
            holder = (Holder) convertView.getTag();
            expandClickListener = (ExpandClickListener) convertView.getTag(holder.mArrowIv.getId());
            deleteListener = (DeleteListener) convertView.getTag(holder.mDeleteBtn.getId());
            cancelListener = (CancelListener) convertView.getTag(holder.mCancelBtn.getId());
            checkBoxChangeListener = (CheckBoxChangeListener) convertView.getTag(holder.mStateCb.getId());
        }

        expandClickListener.setPosition(position);
        deleteListener.setPosition(position);
        cancelListener.setPosition(position);
        checkBoxChangeListener.setPosition(position);

        MyMenuPackEntity.PackInfo packInfo = mList.get(position);

        mImageLoader.displayImage(packInfo.pack_img, holder.mImageView, mOptions);

        holder.mTitleTv.setText(packInfo.pack_title);

        String person2Str = String.format(mContext.getString(R.string.my_menu_person2), packInfo.person2);
        holder.mPerson2Tv.setText(person2Str);

        String person4Str = String.format(mContext.getString(R.string.my_menu_person4), packInfo.person4);
        holder.mPerson4Tv.setText(person4Str);

        boolean expandState = packInfo.expandState;
        if (expandState) {
            // 展开
            holder.mArrowIv.setImageResource(R.mipmap.arrow_up);
            holder.mHintLayout.setVisibility(View.VISIBLE);
        } else {
            holder.mArrowIv.setImageResource(R.mipmap.arrow_down);
            holder.mHintLayout.setVisibility(View.GONE);
        }

        int packState = packInfo.pack_state;
        switch (packState) {
            case PackState.CLOSE_STATE:
                // 审核成功，下架状态
                holder.mTagIv.setVisibility(View.GONE);
                holder.mCbLayout.setVisibility(View.VISIBLE);
                holder.mStateCb.setVisibility(View.VISIBLE);
                holder.mStateCb.setImageResource(R.mipmap.main_close);
                holder.mCancelBtn.setVisibility(View.GONE);
                holder.mEditLayout.setVisibility(View.VISIBLE);
                break;
            case PackState.OPEN_STATE:
                // 审核成功，上架状态
                holder.mTagIv.setVisibility(View.GONE);
                holder.mCbLayout.setVisibility(View.VISIBLE);
                holder.mStateCb.setVisibility(View.VISIBLE);
                holder.mStateCb.setImageResource(R.mipmap.main_open);
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
                holder.mCancelBtn.setText(SUBMIT_BUTTON_TEXT);
                holder.mEditLayout.setVisibility(View.VISIBLE);
                break;
            case PackState.AUDIT_ING_STATE:
                // 审核中
                holder.mTagIv.setVisibility(View.VISIBLE);
                holder.mTagIv.setImageResource(R.mipmap.being_verified);
                holder.mCbLayout.setVisibility(View.VISIBLE);
                holder.mStateCb.setVisibility(View.GONE);
                holder.mCancelBtn.setVisibility(View.VISIBLE);
                holder.mCancelBtn.setText(CANCEL_BUTTON_TEXT);
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
            MyMenuPackEntity.PackInfo packInfo = mList.get(mPosition);
            packInfo.expandState = !packInfo.expandState;
            notifyDataSetChanged();
        }

    }

    /**
     * 删除按钮点击
     */
    private class DeleteListener implements View.OnClickListener {

        private int mPosition;

        public void setPosition(int position) {
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            showConfirmDialog("Delete", "Are you sure to delete the pack menu?", "Delete", 1, mPosition);
        }
    }

    private void showConfirmDialog(String title, String text, String btnText, final int type, final int position) {
        if (mConfirmDialog == null || !mConfirmDialog.isShowing()) {
            mConfirmDialog = DialogUtils.showConfirmDialog(mContext, R.layout.confirm_dialog_layout, true);
        }
        TextView titleTv = (TextView) mConfirmDialog.findViewById(R.id.tv_title);
        titleTv.setText(title);
        TextView textTv = (TextView) mConfirmDialog.findViewById(R.id.tv_text);
        textTv.setText(text);
        TextView confirmTv = (TextView) mConfirmDialog.findViewById(R.id.tv_confirm);
        confirmTv.setText(btnText);
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
                // 确定
                dismissConfirmDialog();
                if (type == 1) {
                    toDelete(position);
                } else if (type == 2) {
                    // 取消审核，进入未审核状态
                    toCancel(position, String.valueOf(PackState.NOT_AUDIT_STATE));
                } else if (type == 3) {
                    // 重新提交审核，进入审核中状态
                    toCancel(position, String.valueOf(PackState.AUDIT_ING_STATE));
                }
            }
        });
    }

    private void toDelete(final int position) {
        String menuId = mList.get(position).pack_id;
        IDeleteMenuPresent present = new DeleteMenuPresent(mContext, new DeleteMenuView() {
            @Override
            public void startLoading() {
                BaseActivity baseActivity = (BaseActivity) mContext;
                baseActivity.showLoadingDialog(mContext.getString(R.string.loading_dialog_delete), true);
            }

            @Override
            public void loadSuccess(String info) {
                if (mDeleteSuccessListener != null) {
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
        present.delete(menuId, "list");
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

    public void setDeleteSuccessListener(DeleteSuccessListener deleteSuccessListener) {
        this.mDeleteSuccessListener = deleteSuccessListener;
    }

    private class CancelListener implements View.OnClickListener {

        private int mPosition;

        public void setPosition(int position) {
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            if (button.getText().toString().equals(CANCEL_BUTTON_TEXT)) {
                showConfirmDialog("Cancel verified", "Are you sure to cancel verified?", "Confirm", 2, mPosition);
            } else {
                showConfirmDialog("Submit verified", "Are you sure to submit verified?", "Confirm", 3, mPosition);
            }
        }
    }

    private void toCancel(final int position, final String state) {
        String menuId = mList.get(position).pack_id;
        IChangeMenuStatePresent present = new ChangeMenuStatePresent(mContext, new ChangeMenuStateView() {
            @Override
            public void startLoading() {
                BaseActivity baseActivity = (BaseActivity) mContext;
                baseActivity.showLoadingDialog(mContext.getString(R.string.loading_dialog_loading), true);
            }

            @Override
            public void loadSuccess() {
                MyMenuPackEntity.PackInfo packInfo = mList.get(position);
                if (state.equals(String.valueOf(PackState.NOT_AUDIT_STATE))) {
                    packInfo.pack_state = PackState.NOT_AUDIT_STATE;
                    packInfo.expandState = false;
                    notifyDataSetChanged();
                    ToastUtils.show("Cancel verified successfully");
                } else if (state.equals(String.valueOf(PackState.AUDIT_ING_STATE))) {
                    packInfo.pack_state = PackState.AUDIT_ING_STATE;
                    packInfo.expandState = false;
                    notifyDataSetChanged();
                    ToastUtils.show("Submit verified successfully");
                } else if (state.equals(String.valueOf(PackState.OPEN_STATE))) {
                    packInfo.pack_state = PackState.OPEN_STATE;
                    notifyDataSetChanged();
                } else if (state.equals(String.valueOf(PackState.CLOSE_STATE))) {
                    packInfo.pack_state = PackState.CLOSE_STATE;
                    notifyDataSetChanged();
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
        present.change(menuId, "list", state);
    }

    private class CheckBoxChangeListener implements View.OnClickListener {

        private int mPosition;

        public void setPosition(int position) {
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            int state = mList.get(mPosition).pack_state;
            if (state == PackState.CLOSE_STATE) {
                toCancel(mPosition, String.valueOf(PackState.OPEN_STATE));
            } else if (state == PackState.OPEN_STATE) {
                toCancel(mPosition, String.valueOf(PackState.CLOSE_STATE));
            }
        }

    }

    static class Holder {
        private ImageView mImageView;
        private TextView mTitleTv;
        private TextView mPerson2Tv;
        private TextView mPerson4Tv;
        private ImageView mArrowIv;
        private LinearLayout mHintLayout;
        private Button mDeleteBtn;
        private Button mEditBtn;
        private ImageView mStateCb;
        private RelativeLayout mEditLayout;
        private Button mCancelBtn;
        private ImageView mTagIv;
        private RelativeLayout mCbLayout;
    }

}
