package com.playhut.partner.pullrefresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playhut.partner.R;

/**
 *
 */
public class ScrollViewAutoLoadView extends LinearLayout{

    private TextView mHintView;

    private ImageView mLoadingIv;

    public ScrollViewAutoLoadView(Context context) {
        super(context);
        init(context);
    }

    public ScrollViewAutoLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollViewAutoLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View.inflate(context, R.layout.scrollview_auto_load_view, this);
        mLoadingIv = (ImageView) findViewById(R.id.iv_loading);
        mHintView = (TextView) findViewById(R.id.tv_text);
        onReset();
    }

    public void onReset() {
        mLoadingIv.clearAnimation();
        mLoadingIv.setVisibility(View.VISIBLE);
        AnimationDrawable ad = (AnimationDrawable) mLoadingIv.getDrawable();
        ad.start();
        mHintView.setText(R.string.pull_to_refresh_header_hint_loading);
    }

    public void onRefreshing() {
        mLoadingIv.clearAnimation();
        mLoadingIv.setVisibility(View.VISIBLE);
        AnimationDrawable ad = (AnimationDrawable) mLoadingIv.getDrawable();
        ad.start();
        mHintView.setText(R.string.pull_to_refresh_header_hint_loading);
    }

    public void onNoMoreData() {
        mLoadingIv.clearAnimation();
        mLoadingIv.setVisibility(View.GONE);
        mHintView.setText(R.string.pull_to_refresh_no_more_data);
    }

    public void onNetWorkError() {
        mLoadingIv.clearAnimation();
        mLoadingIv.setVisibility(View.GONE);
        mHintView.setText(R.string.pull_to_refresh_net_work_error);
    }

    public void onLoadException() {
        mLoadingIv.clearAnimation();
        mLoadingIv.setVisibility(View.GONE);
        mHintView.setText(R.string.pull_to_refresh_load_exception);
    }

}
