package com.playhut.partner.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.playhut.partner.entity.FinanceDateEntity;
import com.playhut.partner.utils.PartnerUtils;
import com.playhut.partner.widget.TosGallery;

import java.util.List;

/**
 *
 */
public class FinanceWheelViewAdapter extends BaseAdapter {

    private Context mContext;

    private List<FinanceDateEntity> mList;

    private int mHeight;

    private int mSelectTextColor;

    private float mSelectTextSize;

    private int mUnselectTextColor;

    private float mUnselectTextSize;

    public FinanceWheelViewAdapter(Context context, List<FinanceDateEntity> list) {
        this.mContext = context;
        this.mList = list;
        mHeight = PartnerUtils.dip2px(mContext, 50) - 15;
        mSelectTextColor = Color.parseColor("#0099CC");
        mSelectTextSize = 19;
        mUnselectTextColor = Color.parseColor("#999999");
        mUnselectTextSize = 15;
    }

    public int getmSelectTextColor() {
        return mSelectTextColor;
    }

    public float getmSelectTextSize() {
        return mSelectTextSize;
    }

    public int getmUnselectTextColor() {
        return mUnselectTextColor;
    }

    public float getmUnselectTextSize() {
        return mUnselectTextSize;
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
        TextView tv = null;
        if (convertView == null) {
            tv = new TextView(mContext);
            tv.setLayoutParams(new TosGallery.LayoutParams(TosGallery.LayoutParams.MATCH_PARENT, mHeight));
            tv.setGravity(Gravity.CENTER);
            tv.getPaint().setFakeBoldText(true);
            convertView = tv;
        } else {
            tv = (TextView) convertView;
        }
        FinanceDateEntity entity = mList.get(position);
        tv.setText(entity.getText());
        if (entity.isSelect()) {
            tv.setTextColor(mSelectTextColor);
            tv.setTextSize(mSelectTextSize);
        } else {
            tv.setTextColor(mUnselectTextColor);
            tv.setTextSize(mUnselectTextSize);
        }
        return convertView;
    }

}
