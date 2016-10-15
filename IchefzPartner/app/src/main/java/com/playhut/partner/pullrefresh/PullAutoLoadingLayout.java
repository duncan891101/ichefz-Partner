package com.playhut.partner.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.playhut.partner.R;


/**
 * 自动加载更多布局
 */
public class PullAutoLoadingLayout extends LoadingLayout {
    private TextView mHintView;
    private ImageView mLoadingIv;

    public PullAutoLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    public PullAutoLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mLoadingIv = (ImageView) findViewById(R.id.iv_loading);
        mHintView = (TextView) findViewById(R.id.tv_text);
        setState(State.RESET);
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(R.layout.pull_auto_load_footer_view, null);
        return container;
    }

    @Override
    public int getContentSize() {
        View view = findViewById(R.id.pull_to_load_footer_content);
        if (null != view) {
            return view.getHeight();
        }

        return (int) (getResources().getDisplayMetrics().density * 40);
    }

    @Override
    protected void onStateChanged(State curState, State oldState) {
        super.onStateChanged(curState, oldState);
    }

    @Override
    protected void onReset() {
        mLoadingIv.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.pull_to_refresh_header_hint_loading);
    }

    @Override
    protected void onRefreshing() {
        mLoadingIv.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.pull_to_refresh_header_hint_loading);
    }

    @Override
    protected void onNoMoreData() {
        mLoadingIv.setVisibility(View.GONE);
        mHintView.setText(R.string.pull_to_refresh_no_more_data);
    }

    @Override
    protected void onNetWorkError() {
        mLoadingIv.setVisibility(View.GONE);
        mHintView.setText(R.string.pull_to_refresh_net_work_error);
    }

    @Override
    protected void onLoadException() {
        mLoadingIv.setVisibility(View.GONE);
        mHintView.setText(R.string.pull_to_refresh_load_exception);
    }

}
