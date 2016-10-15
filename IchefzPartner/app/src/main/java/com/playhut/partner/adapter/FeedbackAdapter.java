package com.playhut.partner.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.playhut.partner.R;
import com.playhut.partner.debug.MyLog;
import com.playhut.partner.entity.FeedbackEntity;
import com.playhut.partner.utils.ImageLoderOptionUtils;
import com.playhut.partner.widget.MeasureGridView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FeedbackAdapter extends BaseAdapter {

    private Context mContext;

    private List<FeedbackEntity.Feedback> mList;

    private ImageLoader mImageLoader;

    private DisplayImageOptions mOptions;

    public FeedbackAdapter(Context context, List<FeedbackEntity.Feedback> list) {
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
            convertView = View.inflate(mContext, R.layout.feedback_item_layout, null);
            holder = new Holder();

            holder.mImageView = (ImageView) convertView.findViewById(R.id.iv_img);
            holder.mNameTv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.mTimeTv = (TextView) convertView.findViewById(R.id.tv_time);
            holder.mRatingBar = (RatingBar) convertView.findViewById(R.id.rb_kitchen);
            holder.mFeedbackTv = (TextView) convertView.findViewById(R.id.tv_feedback);

            holder.mImageGv = (MeasureGridView) convertView.findViewById(R.id.gv_img);
            holder.mAdapter = new FeedbackGridViewAdapter(mContext);
            holder.mImageGv.setAdapter(holder.mAdapter);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        FeedbackEntity.Feedback entity = mList.get(position);

        holder.mNameTv.setText(entity.customer_first_name + " " + entity.customer_last_name);

        holder.mTimeTv.setText(entity.time);

        holder.mRatingBar.setRating(entity.level);

        holder.mFeedbackTv.setText(entity.content);

        mImageLoader.displayImage(entity.customer_picture, holder.mImageView, mOptions);

        List<String> imageList = entity.imgs;
        if (imageList != null && imageList.size() > 0) {
            holder.mImageGv.setVisibility(View.VISIBLE);
            holder.mAdapter.setData(imageList);
        } else {
            holder.mImageGv.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class Holder {
        private ImageView mImageView;
        private TextView mNameTv;
        private TextView mTimeTv;
        private RatingBar mRatingBar;
        private TextView mFeedbackTv;
        private GridView mImageGv;
        private FeedbackGridViewAdapter mAdapter;
    }

}
