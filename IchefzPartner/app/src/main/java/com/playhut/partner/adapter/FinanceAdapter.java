package com.playhut.partner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.playhut.partner.R;
import com.playhut.partner.entity.FinanceListEntity;
import com.playhut.partner.utils.ImageLoderOptionUtils;

import java.util.List;

/**
 *
 */
public class FinanceAdapter extends BaseAdapter {

    private List<FinanceListEntity.OrderList> mList;

    private Context mContext;

    private ImageLoader mImageLoader;

    private DisplayImageOptions mOptions;

    public FinanceAdapter(Context context, List<FinanceListEntity.OrderList> list) {
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
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.finance_list_item, null);

            holder = new Holder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.iv_img);
            holder.mOrderNumberTv = (TextView) convertView.findViewById(R.id.tv_order_number);
            holder.mTimeTv = (TextView) convertView.findViewById(R.id.tv_time);
            holder.mPriceTv = (TextView) convertView.findViewById(R.id.tv_price);

            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

        FinanceListEntity.OrderList entity = mList.get(position);

        String orderNumberStr = String.format(mContext.getString(R.string.finance_item_order_number), entity.order_number);
        holder.mOrderNumberTv.setText(orderNumberStr);

        holder.mTimeTv.setText(entity.time);

        holder.mPriceTv.setText("+$" + entity.price);

        mImageLoader.displayImage(entity.img, holder.mImageView, mOptions);

        return convertView;
    }

    static class Holder {
        private ImageView mImageView;
        private TextView mOrderNumberTv;
        private TextView mTimeTv;
        private TextView mPriceTv;
    }

}
