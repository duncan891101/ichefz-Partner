package com.playhut.partner.widget;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playhut.partner.R;

/**
 * TitleBar
 */
public class PartnerTitleBar {

    private ImageView mLeftIv, mRightIv;

    private TextView mRightTv1, mRightTv2;

    private TextView mCenterTv;

    public PartnerTitleBar(Activity activity) {
        mCenterTv = (TextView) activity.findViewById(R.id.tv_center);
        mLeftIv = (ImageView) activity.findViewById(R.id.iv_left);
        mRightIv = (ImageView) activity.findViewById(R.id.iv_right);
        mRightTv1 = (TextView) activity.findViewById(R.id.tv_right1);
        mRightTv2 = (TextView) activity.findViewById(R.id.tv_right2);
    }

    public PartnerTitleBar(View view) {
        mCenterTv = (TextView) view.findViewById(R.id.tv_center);
        mLeftIv = (ImageView) view.findViewById(R.id.iv_left);
        mRightIv = (ImageView) view.findViewById(R.id.iv_right);
        mRightTv1 = (TextView) view.findViewById(R.id.tv_right1);
        mRightTv2 = (TextView) view.findViewById(R.id.tv_right2);
    }

    public void setLeftIvVisiable(boolean visible) {
        mLeftIv.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setCenterTvVisiable(boolean visible) {
        mCenterTv.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setRightIvVisiable(boolean visible) {
        mRightIv.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setRightTv1Visiable(boolean visible) {
        mRightTv1.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setRightTv2Visiable(boolean visible) {
        mRightTv2.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setLeftIvContent(int resId) {
        mLeftIv.setImageResource(resId);
    }

    public void setCenterTvContent(String title) {
        mCenterTv.setText(title);
    }

    public void setRightIvContent(int resId) {
        mRightIv.setImageResource(resId);
    }

    public void setRightTv1Content(String rightText) {
        mRightTv1.setText(rightText);
    }

    public void setRightTv2Content(String rightText) {
        mRightTv2.setText(rightText);
    }

    public void setLeftIvClickListener(final TitleBarClickListener titleBarClickListener) {
        mLeftIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleBarClickListener.onClick();
            }
        });
    }

    public void setRightIvListener(final TitleBarClickListener titleBarClickListener) {
        mRightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleBarClickListener.onClick();
            }
        });
    }

    public void setRightTv1Listener(final TitleBarClickListener titleBarClickListener) {
        mRightTv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleBarClickListener.onClick();
            }
        });
    }

    public void setRightTv2Listener(final TitleBarClickListener titleBarClickListener) {
        mRightTv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleBarClickListener.onClick();
            }
        });
    }

    public interface TitleBarClickListener {
        void onClick();
    }

    public ImageView getRightIv(){
        return mRightIv;
    }

}
