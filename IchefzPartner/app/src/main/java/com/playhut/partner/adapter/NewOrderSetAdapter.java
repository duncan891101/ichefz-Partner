package com.playhut.partner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.playhut.partner.R;
import com.playhut.partner.utils.ImageLoderOptionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class NewOrderSetAdapter extends RecyclerView.Adapter<NewOrderSetAdapter.Holder>{

    private Context mContext;

    private ImageLoader mImageLoader;

    private DisplayImageOptions mOptions;

    private List<String> mUrlList;

    public NewOrderSetAdapter(Context context){
        this.mContext = context;
        mImageLoader = ImageLoader.getInstance();
        mOptions = ImageLoderOptionUtils.setOptions(R.drawable.loading_image_shape, 0, R.drawable.loading_image_shape);
        mUrlList = new ArrayList<>();
    }

    public void setData(List<String> urlList){
        mUrlList.clear();
        mUrlList.addAll(urlList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mUrlList.size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.new_order_set_rv_item, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        String url = mUrlList.get(position);
        mImageLoader.displayImage(url, holder.mImageView, mOptions);
    }

    class Holder extends RecyclerView.ViewHolder {

        private ImageView mImageView;

        public Holder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_img);
        }
    }
}
