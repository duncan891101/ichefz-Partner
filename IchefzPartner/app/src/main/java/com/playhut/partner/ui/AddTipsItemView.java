package com.playhut.partner.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.playhut.partner.R;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.utils.ImageLoderOptionUtils;

import java.io.File;

/**
 *
 */
public class AddTipsItemView extends LinearLayout {

    private ImageView mImageView;

    private ImageView mDeleteIv;

    private EditText mTitleEt;

    private EditText mDescEt;

    private File mFile;

    private AddTipDeleteListener mAddTipDeleteListener;

    private AddTipClickListener mAddTipClickListener;

    private int mChooseAvatarId;

    private String mItemId;

    public AddTipsItemView(Context context) {
        super(context);
        init(context);
    }

    public AddTipsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddTipsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        View.inflate(context, R.layout.add_tips_item_layout, this);

        final int width = GlobalConstants.SCREEN_WIDTH;
        final int height = (int) (width * 0.6f);

        RelativeLayout imageLayout = (RelativeLayout) findViewById(R.id.rl_img);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageLayout.getLayoutParams();
        params.width = width;
        params.height = height;
        imageLayout.setLayoutParams(params);

        mImageView = (ImageView) findViewById(R.id.iv_img);
        mDeleteIv = (ImageView) findViewById(R.id.iv_delect);
        mTitleEt = (EditText) findViewById(R.id.et_title);
        mDescEt = (EditText) findViewById(R.id.et_desc);

        mDeleteIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddTipDeleteListener != null) {
                    mAddTipDeleteListener.onDelete(AddTipsItemView.this);
                }
            }
        });
        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更换别的图片
                if (mAddTipClickListener != null) {
                    mAddTipClickListener.onClick(AddTipsItemView.this);
                }
            }
        });
    }

    public void setImageView(Bitmap bitmap, File file) {
        this.mFile = file;
        mImageView.setImageBitmap(bitmap);
    }

    public void setImageView(String url) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = ImageLoderOptionUtils.setOptions(0, 0, 0);
        imageLoader.displayImage(url, mImageView, options);
    }

    public void setChooseAvatarId(int chooseAvatarId) {
        this.mChooseAvatarId = chooseAvatarId;
    }

    public int getChooseAvatarId() {
        return mChooseAvatarId;
    }

    public File getImageFile() {
        return mFile;
    }

    public String getTitle() {
        return mTitleEt.getText().toString().trim();
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTitleEt.setText(title);
            mTitleEt.setSelection(title.length());
        }
    }

    public String getDesc() {
        return mDescEt.getText().toString().trim();
    }

    public void setDesc(String desc) {
        if (!TextUtils.isEmpty(desc)) {
            mDescEt.setText(desc);
            mDescEt.setSelection(desc.length());
        }
    }

    public String getItemId() {
        return mItemId;
    }

    public void setItemId(String mItemId) {
        this.mItemId = mItemId;
    }

    public void setAddTipDeleteListener(AddTipDeleteListener addTipDeleteListener) {
        this.mAddTipDeleteListener = addTipDeleteListener;
    }

    public interface AddTipDeleteListener {
        void onDelete(AddTipsItemView item);
    }

    public interface AddTipClickListener {
        void onClick(AddTipsItemView addTipsItemView);
    }

    public void setAddTipClickListener(AddTipClickListener addTipClickListener) {
        this.mAddTipClickListener = addTipClickListener;
    }

}
