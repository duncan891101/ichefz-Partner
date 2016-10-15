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
import com.playhut.partner.entity.MessageListEntity;
import com.playhut.partner.utils.ImageLoderOptionUtils;

import java.util.List;

/**
 *
 */
public class MessageListAdapter extends BaseAdapter {

    private Context mContext;

    private List<MessageListEntity.Message> mList;

    private ImageLoader mImageLoader;

    private DisplayImageOptions mOptions;

    public MessageListAdapter(Context context, List<MessageListEntity.Message> list) {
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
            convertView = View.inflate(mContext, R.layout.message_list_item_layout, null);
            holder = new Holder();

            holder.mImageView = (ImageView) convertView.findViewById(R.id.iv_img);
            holder.mNameTv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.mContentTv = (TextView) convertView.findViewById(R.id.tv_content);
            holder.mTimeTv = (TextView) convertView.findViewById(R.id.tv_time);
            holder.mRedDotView = convertView.findViewById(R.id.view_red_dot);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        MessageListEntity.Message entity = mList.get(position);

        holder.mNameTv.setText(entity.first_name + " " + entity.last_name);

        holder.mContentTv.setText(entity.content);

        holder.mTimeTv.setText(entity.time);

        if (entity.status == 0) {
            // 未读
            holder.mRedDotView.setVisibility(View.VISIBLE);
        } else {
            // 已读
            holder.mRedDotView.setVisibility(View.GONE);
        }

        mImageLoader.displayImage(entity.profile_picture, holder.mImageView, mOptions);

        return convertView;
    }

    static class Holder {
        private ImageView mImageView;
        private TextView mNameTv;
        private TextView mContentTv;
        private TextView mTimeTv;
        private View mRedDotView;
    }

}
