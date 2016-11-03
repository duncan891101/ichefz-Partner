package com.playhut.partner.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.playhut.partner.R;
import com.playhut.partner.utils.ImageLoderOptionUtils;

/**
 *
 */
public class FinanceItemView extends LinearLayout {

    private ImageView mImageView;

    private TextView mOrderNumberTv;

    private TextView mTimeTv;

    private TextView mPriceTv;

    private ImageLoader mImageLoader;

    private DisplayImageOptions mOptions;

    private Context mContext;

    public FinanceItemView(Context context) {
        super(context);
        init(context);
    }

    public FinanceItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FinanceItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mImageLoader = ImageLoader.getInstance();
        mOptions = ImageLoderOptionUtils.setOptions(R.drawable.loading_image_shape, 0, R.drawable.loading_image_shape);
        View.inflate(context, R.layout.finance_list_item, this);
        mImageView = (ImageView) findViewById(R.id.iv_img);
        mOrderNumberTv = (TextView) findViewById(R.id.tv_order_number);
        mTimeTv = (TextView) findViewById(R.id.tv_time);
        mPriceTv = (TextView) findViewById(R.id.tv_price);
    }

    public void setImageView(String url) {
        mImageLoader.displayImage(url, mImageView, mOptions);
    }

    public void setOrderNumber(String number) {
        String s = String.format(mContext.getString(R.string.finance_item_order_number), number);
        mOrderNumberTv.setText(s);
    }

    public void setTime(String time) {
        mTimeTv.setText(time);
    }

    public void setPrice(String price) {
        mPriceTv.setText("+$" + price);
    }

}
