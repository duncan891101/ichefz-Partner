package com.playhut.partner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.playhut.partner.R;

/**
 *
 */
public class MainGridViewAdapter extends BaseAdapter {

    private Context mContext;

    private int[] mIcons = {R.mipmap.main_new_order, R.mipmap.main_my_menu, R.mipmap.main_feedback,
            R.mipmap.main_finance, R.mipmap.main_handle, R.mipmap.main_restaurant};

    private String[] mName = {"New orders", "My menu", "Feedback", "Finance", "Handled", "Kitchen"};

    public MainGridViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mName.length;
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
            convertView = View.inflate(mContext, R.layout.main_gv_item, null);
            holder = new Holder();

            holder.mIconIv = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.mNameTv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.mVerLine = convertView.findViewById(R.id.line_ver);
            holder.mHorLine = convertView.findViewById(R.id.line_hor);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.mIconIv.setImageResource(mIcons[position]);
        holder.mNameTv.setText(mName[position]);

        if (position % 3 == 2) {
            holder.mVerLine.setVisibility(View.GONE);
        } else {
            holder.mVerLine.setVisibility(View.VISIBLE);
        }

        if (position < 3) {
            holder.mHorLine.setVisibility(View.VISIBLE);
        } else {
            holder.mHorLine.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class Holder {
        private ImageView mIconIv;
        private TextView mNameTv;
        private View mVerLine;
        private View mHorLine;
    }

}
