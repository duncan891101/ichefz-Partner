package com.playhut.partner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.playhut.partner.R;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.entity.SelectPackEntity;
import com.playhut.partner.recyclerview.IRecyclerViewItemClick;
import com.playhut.partner.utils.ImageLoderOptionUtils;
import com.playhut.partner.utils.PartnerUtils;

import java.util.List;

/**
 *
 */
public class SelectPackAdapter extends RecyclerView.Adapter<SelectPackAdapter.Holder> implements View.OnClickListener {

    private Context mContext;

    private List<SelectPackEntity.Packs> mPackList;

    private int mImageHeight;

    private ImageLoader mImageLoader;

    private DisplayImageOptions mOptions;

    private IRecyclerViewItemClick mIRecyclerViewItemClick;

    public SelectPackAdapter(Context context, List<SelectPackEntity.Packs> packList) {
        this.mContext = context;
        this.mPackList = packList;
        int imageWidth = (GlobalConstants.SCREEN_WIDTH - PartnerUtils.dip2px(context, 30)) / 2;
        mImageHeight = imageWidth / 2;
        mImageLoader = ImageLoader.getInstance();
        mOptions = ImageLoderOptionUtils.setOptions(R.drawable.loading_image_shape, 0, R.drawable.loading_image_shape);
    }

    public void setRecyclerViewItemClick(IRecyclerViewItemClick itemClick) {
        this.mIRecyclerViewItemClick = itemClick;
    }

    @Override
    public int getItemCount() {
        return mPackList.size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.select_pack_rv_item, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        holder.mImageView.setTag(position);

        SelectPackEntity.Packs entity = mPackList.get(position);

        holder.mTitleTv.setText(entity.title);

        if (entity.isCheck) {
            holder.mCheckIv.setImageResource(R.mipmap.select_pack_check);
        } else {
            holder.mCheckIv.setImageResource(R.mipmap.select_pack_uncheck);
        }

        mImageLoader.displayImage(entity.img, holder.mImageView, mOptions);
    }

    class Holder extends RecyclerView.ViewHolder {

        private ImageView mImageView;

        private RelativeLayout mImageLayout;

        private TextView mTitleTv;

        private ImageView mCheckIv;

        public Holder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_img);
            mImageLayout = (RelativeLayout) itemView.findViewById(R.id.rl_img);
            mTitleTv = (TextView) itemView.findViewById(R.id.tv_title);
            mCheckIv = (ImageView) itemView.findViewById(R.id.iv_check);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mImageLayout.getLayoutParams();
            params.height = mImageHeight;
            mImageLayout.setLayoutParams(params);

            mImageView.setOnClickListener(SelectPackAdapter.this);
        }
    }

    @Override
    public void onClick(View v) {
        if (mIRecyclerViewItemClick != null) {
            mIRecyclerViewItemClick.onItemClick(v, Integer.parseInt(v.getTag().toString()));
        }
    }

}
