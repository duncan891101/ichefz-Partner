package com.playhut.partner.adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.activity.PendingActivity;
import com.playhut.partner.activity.RefundActivity;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.constants.OrderState;
import com.playhut.partner.entity.NewOrderEntity;
import com.playhut.partner.mvp.presenter.IConfirmOrderPresent;
import com.playhut.partner.mvp.presenter.impl.ConfirmOrderPresent;
import com.playhut.partner.mvp.view.ConfirmOrderView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.ui.NewOrderPackItem;
import com.playhut.partner.ui.NewOrderSetItem;
import com.playhut.partner.utils.DialogUtils;
import com.playhut.partner.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class NewOrderAdapter extends BaseAdapter {

    private Context mContext;

    private List<NewOrderEntity.Order> mList;

    public static final String REFUND = "Refund";

    public static final String PENDING = "Pending";

    private Dialog mConfirmDialog;

    public NewOrderAdapter(Context context, List<NewOrderEntity.Order> list) {
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
        ConfirmListener confirmListener = null;
        RefundListener refundListener = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.new_order_lv_item, null);
            holder = new Holder();
            holder.mOrderNumberTv = (TextView) convertView.findViewById(R.id.tv_order_number);
            holder.mTimeTv = (TextView) convertView.findViewById(R.id.tv_time);
            holder.mNameTv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.mPhoneTv = (TextView) convertView.findViewById(R.id.tv_phone);
            holder.mAddressTv = (TextView) convertView.findViewById(R.id.tv_address);
            holder.mNoteTv = (TextView) convertView.findViewById(R.id.tv_note);
            holder.mSubtotalTv = (TextView) convertView.findViewById(R.id.tv_subtotal);
            holder.mContainerLayout = (LinearLayout) convertView.findViewById(R.id.ll_container);

            holder.mRefundBtn = (Button) convertView.findViewById(R.id.btn_refund);
            refundListener = new RefundListener();
            holder.mRefundBtn.setOnClickListener(refundListener);

            holder.mConfirmBtn = (Button) convertView.findViewById(R.id.btn_confirm);
            confirmListener = new ConfirmListener();
            holder.mConfirmBtn.setOnClickListener(confirmListener);

            holder.mTagIv = (ImageView) convertView.findViewById(R.id.iv_tag);

            convertView.setTag(holder);
            convertView.setTag(holder.mConfirmBtn.getId(), confirmListener);
            convertView.setTag(holder.mRefundBtn.getId(), refundListener);
        } else {
            holder = (Holder) convertView.getTag();
            confirmListener = (ConfirmListener) convertView.getTag(holder.mConfirmBtn.getId());
            refundListener = (RefundListener) convertView.getTag(holder.mRefundBtn.getId());
        }

        confirmListener.setPosition(position);
        refundListener.setPosition(position);

        NewOrderEntity.Order entity = mList.get(position);

        String orderNumberStr = String.format(mContext.getString(R.string.new_order_number), entity.order_number);
        holder.mOrderNumberTv.setText(orderNumberStr);

        holder.mTimeTv.setText(entity.time);
        holder.mNameTv.setText(entity.customer_first_name + " " + entity.customer_last_name);
        holder.mPhoneTv.setText(entity.customer_number);
        holder.mAddressTv.setText(entity.customer_address);
        if (TextUtils.isEmpty(entity.remark)) {
            String noteStr = String.format(mContext.getString(R.string.new_order_notes), "No notes");
            holder.mNoteTv.setText(noteStr);
        } else {
            String noteStr = String.format(mContext.getString(R.string.new_order_notes), entity.remark);
            holder.mNoteTv.setText(noteStr);
        }
        holder.mSubtotalTv.setText("$" + entity.subtotal);

        holder.mRefundBtn.setVisibility(View.GONE);
        holder.mConfirmBtn.setVisibility(View.GONE);
        int orderState = entity.order_state;
        switch (orderState) {
            case OrderState.PAID_WAIT_CONFIRM:
                // 支付完成，等待厨师确认接单
                holder.mRefundBtn.setVisibility(View.VISIBLE);
                holder.mRefundBtn.setText(REFUND);
                holder.mConfirmBtn.setVisibility(View.VISIBLE);
                holder.mTagIv.setImageResource(R.mipmap.history_wait_to_confirm);
                break;
            case OrderState.PAID_WAIT_RECEIPT:
                // 厨师已确认接单，等待客户收货
                holder.mRefundBtn.setVisibility(View.VISIBLE);
                holder.mRefundBtn.setText(REFUND);
                holder.mTagIv.setImageResource(R.mipmap.history_confirm);
                break;
            case OrderState.FINISHED:
                // 订单完成
                holder.mTagIv.setImageResource(R.mipmap.history_finish);
                break;
            case OrderState.APPLY_REFUND:
                int chefHandle = entity.chef_handle;
                if (chefHandle == 0) {
                    // 不是厨师处理
                    holder.mTagIv.setImageResource(R.mipmap.history_refund_being);
                } else {
                    // 厨师处理
                    holder.mRefundBtn.setVisibility(View.VISIBLE);
                    holder.mRefundBtn.setText(PENDING);
                    holder.mTagIv.setImageResource(R.mipmap.history_refund_chef_isprogress);
                }
                break;
            case OrderState.REFUND_SUCCESS:
                // 退款成功
                holder.mTagIv.setImageResource(R.mipmap.history_refund_complete);
                break;
            case OrderState.REFUND_REJECT:
                // 退款拒绝
                holder.mTagIv.setImageResource(R.mipmap.history_refund_reject);
                break;
        }

        holder.mContainerLayout.removeAllViews();
        List<NewOrderEntity.OrderItem> itemList = entity.order_items;
        if (itemList != null && itemList.size() > 0) {
            for (NewOrderEntity.OrderItem item : itemList) {
                if ("list".equals(item.mtype)) {
                    NewOrderPackItem newOrderPackItem = new NewOrderPackItem(mContext);

                    List<String> urlList = item.picture;
                    if (urlList != null && urlList.size() > 0) {
                        newOrderPackItem.setImageView(urlList.get(0));
                    }

                    newOrderPackItem.setName(item.title);
                    newOrderPackItem.setPrice(item.item_price);
                    newOrderPackItem.setPerson(item.person);
                    newOrderPackItem.setQuantity(item.quantity);

                    holder.mContainerLayout.addView(newOrderPackItem);
                }
            }

            for (NewOrderEntity.OrderItem item : itemList) {
                if ("set".equals(item.mtype)) {
                    NewOrderSetItem newOrderSetItem = new NewOrderSetItem(mContext);

                    List<String> list = new ArrayList<>();
                    List<String> urlList = item.picture;
                    if (urlList != null && urlList.size() > 0) {
                        list.addAll(urlList);
                    }
                    newOrderSetItem.setRecyclerView(list);

                    newOrderSetItem.setName(item.title);
                    newOrderSetItem.setPrice(item.item_price);
                    newOrderSetItem.setPerson(item.person);
                    newOrderSetItem.setQuantity(item.quantity);

                    holder.mContainerLayout.addView(newOrderSetItem);
                }
            }
        }
        return convertView;
    }

    /**
     * 蓝色的确认按钮点击
     */
    private class ConfirmListener implements View.OnClickListener {

        private int mPosition;

        public void setPosition(int position) {
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            // New Order里的确认接单
            showConfirmDialog("Confirm", "Are you sure to confirm the order?", mPosition);
        }
    }

    /**
     * 灰色的退款按钮点击
     */
    private class RefundListener implements View.OnClickListener {

        private int mPosition;

        public void setPosition(int position) {
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            if (REFUND.equals(button.getText().toString())) {
                RefundActivity.actionIntent(mContext, mList.get(mPosition).order_id, mPosition);
            } else {
                // Pending
                NewOrderEntity.Order entity = mList.get(mPosition);
                PendingActivity.actionIntent(mContext, entity, NewOrderAdapter.this);
            }
        }
    }

    private void showConfirmDialog(String title, String text, final int position) {
        if (mConfirmDialog == null || !mConfirmDialog.isShowing()) {
            mConfirmDialog = DialogUtils.showConfirmDialog(mContext, R.layout.confirm_dialog_layout, true);
        }
        TextView titleTv = (TextView) mConfirmDialog.findViewById(R.id.tv_title);
        titleTv.setText(title);
        TextView textTv = (TextView) mConfirmDialog.findViewById(R.id.tv_text);
        textTv.setText(text);
        TextView confirmTv = (TextView) mConfirmDialog.findViewById(R.id.tv_confirm);
        confirmTv.setText(title);
        mConfirmDialog.findViewById(R.id.rl_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissConfirmDialog();
            }
        });
        mConfirmDialog.findViewById(R.id.rl_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissConfirmDialog();
                String orderId = mList.get(position).order_id;
                // 确认订单（接单）
                toConfirmOrder(position, orderId);
            }
        });
    }

    private void dismissConfirmDialog() {
        if (mConfirmDialog != null && mConfirmDialog.isShowing()) {
            mConfirmDialog.dismiss();
            mConfirmDialog = null;
        }
    }

    private void toConfirmOrder(final int position, String orderId) {
        IConfirmOrderPresent present = new ConfirmOrderPresent(mContext, new ConfirmOrderView() {
            @Override
            public void startLoading() {
                BaseActivity activity = (BaseActivity) mContext;
                activity.showLoadingDialog(mContext.getString(R.string.loading_dialog_confirm), false);
            }

            @Override
            public void loadSuccess(String info) {
                mList.remove(position);
                notifyDataSetChanged();
                ToastUtils.show(info);
            }

            @Override
            public void finishLoading() {
                BaseActivity activity = (BaseActivity) mContext;
                activity.dismissLoadingDialog();
            }

            @Override
            public void loadFailure(IchefzException exception) {
                ToastUtils.show(exception.getErrorMsg());
            }
        });
        present.confirm(orderId);
    }

    static class Holder {
        private TextView mOrderNumberTv;
        private TextView mTimeTv;
        private TextView mNameTv;
        private TextView mPhoneTv;
        private TextView mAddressTv;
        private TextView mNoteTv;
        private TextView mSubtotalTv;
        private LinearLayout mContainerLayout;
        private Button mRefundBtn;
        private Button mConfirmBtn;
        private ImageView mTagIv;
    }

}
