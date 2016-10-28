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
import com.playhut.partner.activity.RefundActivity;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.constants.OrderState;
import com.playhut.partner.entity.NewOrderEntity;
import com.playhut.partner.eventbus.RefundOrderEB;
import com.playhut.partner.mvp.presenter.IConfirmOrderPresent;
import com.playhut.partner.mvp.presenter.IConfirmRefundPresent;
import com.playhut.partner.mvp.presenter.IRefundOrderPresent;
import com.playhut.partner.mvp.presenter.IRejectRefundPresent;
import com.playhut.partner.mvp.presenter.impl.ConfirmOrderPresent;
import com.playhut.partner.mvp.presenter.impl.ConfirmRefundPresent;
import com.playhut.partner.mvp.presenter.impl.RefundOrderPresent;
import com.playhut.partner.mvp.presenter.impl.RejectRefundPresent;
import com.playhut.partner.mvp.view.ConfirmOrderView;
import com.playhut.partner.mvp.view.ConfirmRefundView;
import com.playhut.partner.mvp.view.RefundOrderView;
import com.playhut.partner.mvp.view.RejectRefundView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.ui.NewOrderPackItem;
import com.playhut.partner.ui.NewOrderSetItem;
import com.playhut.partner.utils.DialogUtils;
import com.playhut.partner.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.Subscribe;

/**
 *
 */
public class NewOrderAdapter extends BaseAdapter {

    private Context mContext;

    private List<NewOrderEntity.Order> mList;

    public static final String CONFIRM_ORDER = "Confirm order";

    public static final String CONFIRM_REFUND = "Confirm refund";

    public static final String CHEF_REFUND_IN_THIS_ORDER = "Chef refund order"; // New order里的Refund按钮和其他订单Confirmed栏里的Refund按钮

    public static final String CUSTOMER_REFUND_IN_THIS_ORDER = "Customer refund order"; // 其他订单Refund栏里的Refund，此状态是厨师已接单，用户发起退款后，订单进入该状态，让厨师去确认是否能退款

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
                holder.mRefundBtn.setTag(CHEF_REFUND_IN_THIS_ORDER);
                holder.mConfirmBtn.setVisibility(View.VISIBLE);
                holder.mConfirmBtn.setText(CONFIRM_ORDER);
                holder.mTagIv.setImageResource(R.mipmap.history_wait_to_confirm);
                break;
            case OrderState.PAID_WAIT_RECEIPT:
                // 厨师已确认接单，等待客户收货
                holder.mRefundBtn.setVisibility(View.VISIBLE);
                holder.mRefundBtn.setTag(CHEF_REFUND_IN_THIS_ORDER);
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
                    holder.mRefundBtn.setTag(CUSTOMER_REFUND_IN_THIS_ORDER);
                    holder.mConfirmBtn.setVisibility(View.VISIBLE);
                    holder.mConfirmBtn.setText(CONFIRM_REFUND);
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
            Button button = (Button) v;
            if (CONFIRM_ORDER.equals(button.getText().toString())) {
                // New Order里的确认接单
                showConfirmDialog("Confirm", "Are you sure to confirm the order?", 1, mPosition);
            } else {
                // 其他订单里的Refund确认退款
                showConfirmDialog("Confirm", "Are you sure to refund the order?", 2, mPosition);
            }
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
            String tag = (String) v.getTag();
            if (CHEF_REFUND_IN_THIS_ORDER.equals(tag)) {
                RefundActivity.actionIntent(mContext, mList.get(mPosition).order_id, mPosition);
            } else {
                showConfirmDialog("Reject", "Are you sure to reject order of refund?", 3, mPosition);
            }
        }
    }

    private void showConfirmDialog(String title, String text, final int type, final int position) {
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
                if (type == 1) {
                    // 确认订单（接单）
                    toConfirmOrder(position, orderId);
                } else if (type == 2) {
                    // 确认退款
                    toConfirmRefund(position, orderId);
                } else {
                    // 拒绝退款
                    toRejectRefund(position, orderId);
                }
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

    private void toConfirmRefund(final int position, String orderId) {
        IConfirmRefundPresent present = new ConfirmRefundPresent(mContext, new ConfirmRefundView() {
            @Override
            public void startLoading() {
                BaseActivity activity = (BaseActivity) mContext;
                activity.showLoadingDialog(mContext.getString(R.string.loading_dialog_confirm), false);
            }

            @Override
            public void loadSuccess(String info) {
                // 进入后台审核状态
                NewOrderEntity.Order order = mList.get(position);
                order.chef_handle = 0;
                order.order_state = OrderState.APPLY_REFUND;
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
        present.refund(orderId);
    }

    private void toRejectRefund(final int position, String orderId) {
        IRejectRefundPresent present = new RejectRefundPresent(mContext, new RejectRefundView() {
            @Override
            public void startLoading() {
                BaseActivity activity = (BaseActivity) mContext;
                activity.showLoadingDialog(mContext.getString(R.string.loading_dialog_loading), false);
            }

            @Override
            public void loadSuccess(String info) {
                NewOrderEntity.Order order = mList.get(position);
                order.order_state = OrderState.REFUND_REJECT;
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
        present.reject(orderId);
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
