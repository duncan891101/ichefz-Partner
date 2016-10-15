package com.playhut.partner.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.playhut.partner.R;
import com.playhut.partner.entity.MessageDetailEntity;
import com.playhut.partner.utils.ImageLoderOptionUtils;

/**
 *
 */
public class MessageDetailChildView extends LinearLayout implements CompoundButton.OnCheckedChangeListener {

    private CheckBox mCheckBox;

    private ImageView mImageView;

    private TextView mNameTv;

    private TextView mTimeTv;

    private TextView mContentTv;

    private ImageLoader mImageLoader;

    private DisplayImageOptions mOptions;

    private ChildCheckListener mChildCheckListener;

    public MessageDetailChildView(Context context) {
        super(context);
        init(context);
    }

    public MessageDetailChildView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MessageDetailChildView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mImageLoader = ImageLoader.getInstance();
        mOptions = ImageLoderOptionUtils.setOptions(R.drawable.loading_image_shape, 0, R.drawable.loading_image_shape);
        View.inflate(context, R.layout.message_detail_child_view, this);
        mCheckBox = (CheckBox) findViewById(R.id.cb_child);
        mCheckBox.setOnCheckedChangeListener(this);
        mImageView = (ImageView) findViewById(R.id.iv_img);
        mNameTv = (TextView) findViewById(R.id.tv_name);
        mTimeTv = (TextView) findViewById(R.id.tv_time);
        mContentTv = (TextView) findViewById(R.id.tv_content);
    }

    public void setCheckBoxVisible(boolean isShow) {
        mCheckBox.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void setCheckBoxState(boolean isCheck) {
        mCheckBox.setChecked(isCheck);
    }

    public void setImageView(String url) {
        mImageLoader.displayImage(url, mImageView, mOptions);
    }

    public void setInfo(String name, String time, String content) {
        mNameTv.setText(name);
        mTimeTv.setText(time);
        mContentTv.setText(content);
    }

    public void setChildCheckListener(ChildCheckListener childCheckListener) {
        this.mChildCheckListener = childCheckListener;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mChildCheckListener != null) {
            mChildCheckListener.onCheckChange(buttonView, isChecked);
        }
    }

    public interface ChildCheckListener {
        void onCheckChange(CompoundButton buttonView, boolean isChecked);
    }

}
