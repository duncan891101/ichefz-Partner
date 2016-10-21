package com.playhut.partner.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.playhut.partner.R;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.debug.MyLog;

/**
 *
 */
public class AddTipsItemView extends LinearLayout {

    private ImageView mImageView;

    private ImageView mDeleteIv;

    private EditText mTitleEt;

    private EditText mDescEt;

    private AddTipDeleteListener mAddTipDeleteListener;

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

    private void init(Context context) {
        View.inflate(context, R.layout.add_tips_item_layout, this);

        RelativeLayout imageLayout = (RelativeLayout) findViewById(R.id.rl_img);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageLayout.getLayoutParams();
        params.width = GlobalConstants.SCREEN_WIDTH;
        params.height = GlobalConstants.SCREEN_WIDTH / 2;
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
    }

    public void setImageView(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }

    public void setAddTipDeleteListener(AddTipDeleteListener addTipDeleteListener) {
        this.mAddTipDeleteListener = addTipDeleteListener;
    }

    public interface AddTipDeleteListener {
        void onDelete(LinearLayout item);
    }

}
