package com.playhut.partner.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.playhut.partner.R;
import com.playhut.partner.utils.ImageLoderOptionUtils;

/**
 *
 */
public class NewOrderPackItem extends RelativeLayout {

    private Context mContext;

    private ImageView mImageView;

    private ImageLoader mImageLoader;

    private DisplayImageOptions mOptions;

    private TextView mNameTv;

    private TextView mPriceTv;

    private TextView mPersonTv;

    private TextView mQuantityTv;

    public NewOrderPackItem(Context context) {
        super(context);
        init(context);
    }

    public NewOrderPackItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NewOrderPackItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mImageLoader = ImageLoader.getInstance();
        mOptions = ImageLoderOptionUtils.setOptions(R.drawable.loading_image_shape, 0, R.drawable.loading_image_shape);
        View.inflate(context, R.layout.new_order_pack_item_layout, this);
        mImageView = (ImageView) findViewById(R.id.iv_img);
        mNameTv = (TextView) findViewById(R.id.tv_name);
        mPriceTv = (TextView) findViewById(R.id.tv_price);
        mPersonTv = (TextView) findViewById(R.id.tv_person);
        mQuantityTv = (TextView) findViewById(R.id.tv_quantity);
    }

    public void setImageView(String url) {

        mImageLoader.displayImage(url, mImageView, mOptions);
    }

    public void setName(String name) {
        if (!TextUtils.isEmpty(name)) {
            mNameTv.setText(name);
        }
    }

    public void setPrice(String price) {
        if (!TextUtils.isEmpty(price)) {
            mPriceTv.setText("$" + price);
        }
    }

    public void setPerson(String person) {
        if (!TextUtils.isEmpty(person)) {
            String personStr = String.format(mContext.getString(R.string.new_order_person), person);
            mPersonTv.setText(personStr);
        }
    }

    public void setQuantity(String quantity) {
        if (!TextUtils.isEmpty(quantity)) {
            String quantityStr = String.format(mContext.getString(R.string.new_order_quantity), quantity);
            mQuantityTv.setText(quantityStr);
        }
    }

}
