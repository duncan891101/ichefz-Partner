package com.playhut.partner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.playhut.partner.R;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.utils.ImageLoderOptionUtils;
import com.playhut.partner.utils.PartnerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FeedbackGridViewAdapter extends BaseAdapter {

    private List<String> mUrlList;

    private Context mContext;

    private ImageLoader mImageLoader;

    private DisplayImageOptions mOptions;

    private int mImageWidth;

    private int mImageHeight;

    public FeedbackGridViewAdapter(Context context) {
        this.mContext = context;

        mImageLoader = ImageLoader.getInstance();
        mOptions = ImageLoderOptionUtils.setOptions(R.drawable.loading_image_shape, 0, R.drawable.loading_image_shape);

        mImageWidth = (GlobalConstants.SCREEN_WIDTH - PartnerUtils.dip2px(context, 20)) / 3;
        mImageHeight = mImageWidth / 2;

        mUrlList = new ArrayList<>();
    }

    public void setData(List<String> urlList){
        mUrlList.clear();
        mUrlList.addAll(urlList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mUrlList.size();
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
            convertView = View.inflate(mContext, R.layout.feedback_recycler_view_item, null);
            holder = new Holder();

            holder.mImageView = (ImageView) convertView.findViewById(R.id.iv_img);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.mImageView.getLayoutParams();
            params.height = mImageHeight;
            holder.mImageView.setLayoutParams(params);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        mImageLoader.displayImage(mUrlList.get(position), holder.mImageView, mOptions);

        return convertView;
    }

    static class Holder {
        private ImageView mImageView;
    }

}
