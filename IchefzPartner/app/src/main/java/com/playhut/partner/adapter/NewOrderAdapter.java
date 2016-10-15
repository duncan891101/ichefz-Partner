package com.playhut.partner.adapter;

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
import com.playhut.partner.constants.OrderState;
import com.playhut.partner.entity.NewOrderEntity;
import com.playhut.partner.ui.NewOrderPackItem;
import com.playhut.partner.ui.NewOrderSetItem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class NewOrderAdapter extends BaseAdapter {

    private Context mContext;

    private List<NewOrderEntity.Order> mList;

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
            holder.mConfirmBtn = (Button) convertView.findViewById(R.id.btn_confirm);
            holder.mTagIv = (ImageView) convertView.findViewById(R.id.iv_tag);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

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
        switch (orderState){
            case OrderState.PAID_WAIT_CONFIRM:
                // 支付完成，等待厨师确认接单
                holder.mRefundBtn.setVisibility(View.VISIBLE);
                holder.mConfirmBtn.setVisibility(View.VISIBLE);
                holder.mConfirmBtn.setText("Confirm order");
                holder.mTagIv.setImageResource(R.mipmap.history_wait_to_confirm);
                break;
            case OrderState.PAID_WAIT_RECEIPT:
                // 厨师已确认接单，等待客户收货
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
                    holder.mConfirmBtn.setVisibility(View.VISIBLE);
                    holder.mConfirmBtn.setText("Confirm refund");
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
