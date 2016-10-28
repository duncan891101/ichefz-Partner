package com.playhut.partner.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.playhut.partner.R;

/**
 *
 */
public class IchefzStateView extends FrameLayout {

    private Context mContext;

    private View loadingView;

    private View networkErrorView;

    private View loadFailureView;

    private View noItemView;

    public IchefzStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IchefzStateView);
        boolean loadingViewState = typedArray.getBoolean(R.styleable.IchefzStateView_loading_view, false);
        boolean networkErrorViewState = typedArray.getBoolean(R.styleable.IchefzStateView_network_error_view, false);
        boolean loadFailureViewState = typedArray.getBoolean(R.styleable.IchefzStateView_load_failure_view, false);
        boolean noItemViewState = typedArray.getBoolean(R.styleable.IchefzStateView_no_item_view, false);
        if (loadingViewState) {
            loadingView = View.inflate(context, R.layout.loading_view_layout, null);
            loadingView.setVisibility(View.GONE);
            addView(loadingView);
        }
        if (networkErrorViewState) {
            networkErrorView = View.inflate(context, R.layout.network_error_view_layout, null);
            networkErrorView.setVisibility(View.GONE);
            addView(networkErrorView);
        }
        if (loadFailureViewState) {
            loadFailureView = View.inflate(context, R.layout.load_failure_view_layout, null);
            loadFailureView.setVisibility(View.GONE);
            addView(loadFailureView);
        }
        if (noItemViewState) {
            noItemView = View.inflate(context, R.layout.no_item_layout, null);
            noItemView.setVisibility(View.GONE);
            addView(noItemView);
        }
    }

    public void showLoadingView() {
        if (loadingView != null) {
            ImageView imageView = (ImageView) loadingView.findViewById(R.id.iv_loading);
            imageView.clearAnimation();
            AnimationDrawable ad = (AnimationDrawable) imageView.getDrawable();
            ad.start();
            loadingView.setVisibility(View.VISIBLE);
        }
        if (networkErrorView != null) {
            networkErrorView.setVisibility(View.GONE);
        }
        if (loadFailureView != null) {
            loadFailureView.setVisibility(View.GONE);
        }
        if (noItemView != null) {
            noItemView.setVisibility(View.GONE);
        }
    }

    public void dismissLoadingView() {
        if (loadingView != null) {
            ImageView imageView = (ImageView) loadingView.findViewById(R.id.iv_loading);
            imageView.clearAnimation();
            loadingView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示网络错误页面
     *
     * @param reloadListener 如不需要显示reload按钮，则传null
     */
    public void showNetworkErrorView(final ReloadListener reloadListener) {
        if (loadingView != null) {
            ImageView imageView = (ImageView) loadingView.findViewById(R.id.iv_loading);
            imageView.clearAnimation();
            loadingView.setVisibility(View.GONE);
        }
        if (networkErrorView != null) {
            Button reloadBtn = (Button) networkErrorView.findViewById(R.id.btn_reload);
            if (reloadListener != null) {
                reloadBtn.setVisibility(View.VISIBLE);
                reloadBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reloadListener.reload();
                    }
                });
            } else {
                reloadBtn.setVisibility(View.GONE);
            }
            networkErrorView.setVisibility(View.VISIBLE);
        }
        if (loadFailureView != null) {
            loadFailureView.setVisibility(View.GONE);
        }
        if (noItemView != null) {
            noItemView.setVisibility(View.GONE);
        }
    }

    public void dismissNetworkErrorView() {
        if (networkErrorView != null) {
            networkErrorView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示加载失败的页面
     *
     * @param reloadListener 如不需要显示reload按钮，则传null
     */
    public void showLoadFailureView(final ReloadListener reloadListener) {
        if (loadingView != null) {
            ImageView imageView = (ImageView) loadingView.findViewById(R.id.iv_loading);
            imageView.clearAnimation();
            loadingView.setVisibility(View.GONE);
        }
        if (networkErrorView != null) {
            networkErrorView.setVisibility(View.GONE);
        }
        if (loadFailureView != null) {
            Button reloadBtn = (Button) loadFailureView.findViewById(R.id.btn_reload);
            if (reloadListener != null) {
                reloadBtn.setVisibility(View.VISIBLE);
                reloadBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reloadListener.reload();
                    }
                });
            } else {
                reloadBtn.setVisibility(View.GONE);
            }
            loadFailureView.setVisibility(View.VISIBLE);
        }
        if (noItemView != null) {
            noItemView.setVisibility(View.GONE);
        }
    }

    public void dismissLoadFailureView() {
        if (loadFailureView != null) {
            loadFailureView.setVisibility(View.GONE);
        }
    }

    public void showNoItemView(String text) {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (networkErrorView != null) {
            networkErrorView.setVisibility(View.GONE);
        }
        if (loadFailureView != null) {
            loadFailureView.setVisibility(View.GONE);
        }
        if (noItemView != null) {
            noItemView.setVisibility(View.VISIBLE);
            TextView tv = (TextView) noItemView.findViewById(R.id.tv_text);
            tv.setText(text);
        }
    }

    public void dismissNoItemView(){
        if (noItemView != null) {
            noItemView.setVisibility(View.GONE);
        }
    }

    public interface ReloadListener {
        void reload();
    }

}
