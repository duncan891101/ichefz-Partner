package com.playhut.partner.ui;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.adapter.NewOrderSetAdapter;

import java.util.List;

/**
 *
 */
public class NewOrderSetItem extends RelativeLayout {

    private Context mContext;

    private RecyclerView mRecyclerView;

    private TextView mNameTv;

    private TextView mPriceTv;

    private TextView mPersonTv;

    private TextView mQuantityTv;

    public NewOrderSetItem(Context context) {
        super(context);
        init(context);
    }

    public NewOrderSetItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NewOrderSetItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        View.inflate(context, R.layout.new_order_set_item_layout, this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_set);
        // 由于外层ScrollView嵌套了RecyclerView，如果不禁止RecyclerView滑动的话，在5.0以上机器就出现滑动卡顿的现象
        GridLayoutManager manager = new GridLayoutManager(mContext, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerView.setLayoutManager(manager);

        mNameTv = (TextView) findViewById(R.id.tv_name);
        mPriceTv = (TextView) findViewById(R.id.tv_price);
        mPersonTv = (TextView) findViewById(R.id.tv_person);
        mQuantityTv = (TextView) findViewById(R.id.tv_quantity);
    }

    public void setRecyclerView(List<String> urlList) {
        NewOrderSetAdapter adapter = new NewOrderSetAdapter(mContext);
        mRecyclerView.setAdapter(adapter);
        adapter.setData(urlList);
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
