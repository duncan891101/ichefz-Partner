package com.playhut.partner.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.playhut.partner.constants.NetworkConstants;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.pullrefresh.ScrollViewAutoLoadView;
import com.playhut.partner.utils.PartnerUtils;
import com.playhut.partner.utils.ToastUtils;

/**
 *
 */
public class AutoLoadScrollView extends ScrollView implements View.OnTouchListener {

    private DelayHandler mDelayHandler = new DelayHandler();

    public static final int MSG_WHAT = -21246;

    public static final long DELAY_TIME = 5;

    private int mLastY = 0;

    private boolean mHasMoreData = false;

    private boolean mAutoLoadEnable = false;

    private Context mContext;

    private ScrollViewAutoLoadView mLoadView;

    public LoadingListener mLoadingListener;

    private boolean mIsLoading = false;

    public AutoLoadScrollView(Context context) {
        super(context);
        init(context);
    }

    public AutoLoadScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoLoadScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        setOnTouchListener(this);
        mLoadView = new ScrollViewAutoLoadView(mContext);
    }

    public void addContentView(View rootLayout) {
        addView(rootLayout);
        LinearLayout ll = (LinearLayout) rootLayout;
        ll.addView(mLoadView, -1);
        mLoadView.setVisibility(View.GONE);
    }

    public void setHasMoreData(boolean hasMoreData, boolean isShow) {
        this.mHasMoreData = hasMoreData;
        mLoadView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (!hasMoreData) {
            mLoadView.onNoMoreData();
        } else {
            mLoadView.onReset();
        }
    }

    public void setAutoLoadEnable(boolean autoLoadEnable) {
        this.mAutoLoadEnable = autoLoadEnable;
    }

    /**
     * 设置显示网络异常
     */
    public void setNetWorkError() {
        ToastUtils.show(NetworkConstants.NETWORK_ERROR_MSG);
        mLoadView.onNetWorkError();
    }

    /**
     * 设置加载异常的显示
     */
    public void setLoadException(IchefzException exception) {
        ToastUtils.show(exception.getErrorMsg());
        if (mLoadView != null) {
            mLoadView.onLoadException();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Message msg = Message.obtain();
            msg.what = MSG_WHAT;
            msg.obj = v;
            mDelayHandler.sendMessageDelayed(msg, DELAY_TIME * 3);
        }
        return false;
    }

    private class DelayHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_WHAT) {
                ScrollView scrollView = (ScrollView) msg.obj;
                if (mLastY == scrollView.getScrollY()) {
                    // 滚动停止了
                    if (mAutoLoadEnable && isAtBottom() && mHasMoreData) {
                        // 自动加载开启，并且是底部，并且有下一页数据
                        if (PartnerUtils.checkNetwork(mContext)) {
                            if (!mIsLoading) {
                                mIsLoading = true;
                                startLoading();
                            }
                        } else {
                            setNetWorkError();
                        }
                    }
                } else {
                    Message newMsg = Message.obtain();
                    newMsg.what = MSG_WHAT;
                    newMsg.obj = scrollView;
                    mDelayHandler.sendMessageDelayed(newMsg, DELAY_TIME);
                    mLastY = scrollView.getScrollY();
                }
            }
        }
    }

    private void startLoading() {
        mLoadView.onRefreshing();
        if (mLoadingListener != null) {
            mLoadingListener.onLoad();
        }
    }

    public void onLoadComplete() {
        mIsLoading = false;
        mLoadView.onReset();
    }

    private boolean isAtBottom() {
        View scrollViewChild = getChildAt(0);
        if (null != scrollViewChild) {
            return getScrollY() >= (scrollViewChild.getHeight() - getHeight());
        }
        return false;
    }

    public interface LoadingListener {
        void onLoad();
    }

    public void setLoadingListener(LoadingListener loadingListener) {
        this.mLoadingListener = loadingListener;
    }

}
