package com.playhut.partner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.playhut.partner.R;
import com.playhut.partner.constants.PackState;
import com.playhut.partner.entity.MyMenuPackEntity;
import com.playhut.partner.utils.ImageLoderOptionUtils;

import java.util.List;

/**
 *
 */
public class MyMenuPackAdapter extends BaseAdapter {

    private Context mContext;

    private List<MyMenuPackEntity.PackInfo> mList;

    private ImageLoader mImageLoader;

    private DisplayImageOptions mOptions;

    public MyMenuPackAdapter(Context context, List<MyMenuPackEntity.PackInfo> list) {
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
        ExpandClickListener expandClickListener = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.my_menu_pack_item_layout, null);
            holder = new Holder();

            holder.mImageView = (ImageView) convertView.findViewById(R.id.iv_menu);
            holder.mTitleTv = (TextView) convertView.findViewById(R.id.tv_title);
            holder.mPerson2Tv = (TextView) convertView.findViewById(R.id.tv_person2);
            holder.mPerson4Tv = (TextView) convertView.findViewById(R.id.tv_person4);

            holder.mArrowIv = (ImageView) convertView.findViewById(R.id.iv_arrow);
            expandClickListener = new ExpandClickListener();
            holder.mArrowIv.setOnClickListener(expandClickListener);

            holder.mHintLayout = (LinearLayout) convertView.findViewById(R.id.ll_hint_layout);
            holder.mDeleteBtn = (Button) convertView.findViewById(R.id.btn_delete);
            holder.mEditBtn = (Button) convertView.findViewById(R.id.btn_edit);
            holder.mStateCb = (CheckBox) convertView.findViewById(R.id.cb_state);
            holder.mEditLayout = (RelativeLayout) convertView.findViewById(R.id.rl_edit);
            holder.mCancelBtn = (Button) convertView.findViewById(R.id.btn_cancel);
            holder.mTagIv = (ImageView) convertView.findViewById(R.id.iv_tag);
            holder.mCbLayout = (RelativeLayout) convertView.findViewById(R.id.rl_cb);

            convertView.setTag(holder);
            convertView.setTag(holder.mArrowIv.getId(), expandClickListener);
        } else {
            holder = (Holder) convertView.getTag();
            expandClickListener = (ExpandClickListener) convertView.getTag(holder.mArrowIv.getId());
        }

        expandClickListener.setPosition(position);

        MyMenuPackEntity.PackInfo packInfo = mList.get(position);

        mImageLoader.displayImage(packInfo.pack_img, holder.mImageView, mOptions);

        holder.mTitleTv.setText(packInfo.pack_title);

        String person2Str = String.format(mContext.getString(R.string.my_menu_person2), packInfo.person2);
        holder.mPerson2Tv.setText(person2Str);

        String person4Str = String.format(mContext.getString(R.string.my_menu_person4), packInfo.person4);
        holder.mPerson4Tv.setText(person4Str);

        boolean expandState = packInfo.expandState;
        if (expandState) {
            // 展开
            holder.mArrowIv.setImageResource(R.mipmap.arrow_up);
            holder.mHintLayout.setVisibility(View.VISIBLE);
        } else {
            holder.mArrowIv.setImageResource(R.mipmap.arrow_down);
            holder.mHintLayout.setVisibility(View.GONE);
        }

        int packState = packInfo.pack_state;
        switch (packState) {
            case PackState.CLOSE_STATE:
                // 审核成功，下架状态
                holder.mTagIv.setVisibility(View.GONE);
                holder.mCbLayout.setVisibility(View.VISIBLE);
                holder.mStateCb.setVisibility(View.VISIBLE);
                holder.mStateCb.setChecked(false);
                holder.mCancelBtn.setVisibility(View.GONE);
                holder.mEditLayout.setVisibility(View.VISIBLE);
                break;
            case PackState.OPEN_STATE:
                // 审核成功，上架状态
                holder.mTagIv.setVisibility(View.GONE);
                holder.mCbLayout.setVisibility(View.VISIBLE);
                holder.mStateCb.setVisibility(View.VISIBLE);
                holder.mStateCb.setChecked(true);
                holder.mCancelBtn.setVisibility(View.GONE);
                holder.mEditLayout.setVisibility(View.VISIBLE);
                break;
            case PackState.NOT_AUDIT_STATE:
                // 未审核
                holder.mTagIv.setVisibility(View.VISIBLE);
                holder.mTagIv.setImageResource(R.mipmap.to_be_verified);
                holder.mCbLayout.setVisibility(View.VISIBLE);
                holder.mStateCb.setVisibility(View.GONE);
                holder.mCancelBtn.setVisibility(View.VISIBLE);
                holder.mCancelBtn.setText("Submit");
                holder.mEditLayout.setVisibility(View.VISIBLE);
                break;
            case PackState.AUDIT_ING_STATE:
                // 审核中
                holder.mTagIv.setVisibility(View.VISIBLE);
                holder.mTagIv.setImageResource(R.mipmap.being_verified);
                holder.mCbLayout.setVisibility(View.VISIBLE);
                holder.mStateCb.setVisibility(View.GONE);
                holder.mCancelBtn.setVisibility(View.VISIBLE);
                holder.mCancelBtn.setText("Cancel");
                holder.mEditLayout.setVisibility(View.GONE);
                break;
            case PackState.AUDIT_FAIL_STATE:
                // 审核失败
                holder.mTagIv.setVisibility(View.VISIBLE);
                holder.mTagIv.setImageResource(R.mipmap.verification_rejected);
                holder.mCbLayout.setVisibility(View.GONE);
                holder.mStateCb.setVisibility(View.GONE);
                holder.mCancelBtn.setVisibility(View.GONE);
                holder.mEditLayout.setVisibility(View.VISIBLE);
                break;
        }

        return convertView;
    }

    private class ExpandClickListener implements View.OnClickListener {

        private int mPosition;

        public void setPosition(int position) {
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            MyMenuPackEntity.PackInfo packInfo = mList.get(mPosition);
            packInfo.expandState = !packInfo.expandState;
            notifyDataSetChanged();
        }

    }

    static class Holder {
        private ImageView mImageView;
        private TextView mTitleTv;
        private TextView mPerson2Tv;
        private TextView mPerson4Tv;
        private ImageView mArrowIv;
        private LinearLayout mHintLayout;
        private Button mDeleteBtn;
        private Button mEditBtn;
        private CheckBox mStateCb;
        private RelativeLayout mEditLayout;
        private Button mCancelBtn;
        private ImageView mTagIv;
        private RelativeLayout mCbLayout;
    }

}
