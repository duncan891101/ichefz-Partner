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

import java.util.List;

/**
 *
 */
public class AddSetAdapter extends BaseAdapter {

    private List<String> mImageUrlList;

    private Context mContext;

    private int mImageHeight;

    private ImageLoader mImageLoader;

    private DisplayImageOptions mOptions;

    public AddSetAdapter(Context context, List<String> imageUrlList) {
        this.mContext = context;
        this.mImageUrlList = imageUrlList;
        mImageHeight = GlobalConstants.SCREEN_WIDTH / 4;
        mImageLoader = ImageLoader.getInstance();
        mOptions = ImageLoderOptionUtils.setOptions(R.drawable.loading_image_shape, 0, R.drawable.loading_image_shape);
    }

    @Override
    public int getCount() {
        return mImageUrlList.size();
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
            convertView = View.inflate(mContext, R.layout.add_set_gv_layout, null);
            holder = new Holder();

            holder.mImageView = (ImageView) convertView.findViewById(R.id.iv_img);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.mImageView.getLayoutParams();
            params.height = mImageHeight;
            holder.mImageView.setLayoutParams(params);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        mImageLoader.displayImage(mImageUrlList.get(position), holder.mImageView, mOptions);

        return convertView;
    }

    static class Holder {
        private ImageView mImageView;
    }

}
