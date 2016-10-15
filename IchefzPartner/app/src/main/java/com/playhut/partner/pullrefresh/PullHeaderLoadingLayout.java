package com.playhut.partner.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.utils.PartnerUtils;


/**
 * 这个类封装了下拉刷新的布局
 */
public class PullHeaderLoadingLayout extends LoadingLayout {
    /**
     * Header的容器
     */
    private LinearLayout mHeaderContainer;

    private HeaderCircleAnimView mHeaderCircleAnimView;

    private float cx, cy, radius;

    private ImageView mArrowIv;

    private TextView mTextView;

    /**
     * anim1是从看见到看不见
     * anim2是从看不见到看见
     */
    private AlphaAnimation mArrowAnim1, mArrowAnim2;

    private RotateAnimation mHeaderCircleAnim;

    /**
     * 构造方法
     *
     * @param context context
     */
    public PullHeaderLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public PullHeaderLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        mHeaderContainer = (LinearLayout) findViewById(R.id.ll_header_container);
        mHeaderCircleAnimView = (HeaderCircleAnimView) findViewById(R.id.view_header_circle);
        mArrowIv = (ImageView) findViewById(R.id.iv_pull_down);
        mTextView = (TextView) findViewById(R.id.tv_text);
        /*为了和旋转动画中的0.5f位置一致而做出的处理，否则旋转时的中心点不是圆心*/
        float px = PartnerUtils.dip2px(context, 30);
        if (px % 2 != 0) {
            px += 1;
        }
        cy = px / 2;
        cx = px / 2;

        px = PartnerUtils.dip2px(context, 27);
        if (px % 2 != 0) {
            px += 1;
        }
        radius = px / 2;

        mArrowAnim1 = new AlphaAnimation(1.0f, 0); // 箭头透明度变化动画
        mArrowAnim1.setDuration(200);
        mArrowAnim1.setFillAfter(true);

        mArrowAnim2 = new AlphaAnimation(0, 1.0f); // 箭头透明度变化动画
        mArrowAnim2.setDuration(200);
        mArrowAnim2.setFillAfter(true);

        initHeaderCircleAnim();
    }

    /**
     * 初始化旋转动画
     */
    private void initHeaderCircleAnim() {
        mHeaderCircleAnim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mHeaderCircleAnim.setFillAfter(true);
        mHeaderCircleAnim.setDuration(800);
        mHeaderCircleAnim.setRepeatCount(-1);
        mHeaderCircleAnim.setInterpolator(new LinearInterpolator());
    }

    @Override
    public int getContentSize() {
        if (null != mHeaderContainer) {
            return mHeaderContainer.getHeight();
        }

        return (int) (getResources().getDisplayMetrics().density * 60);
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(R.layout.pull_refresh_header_view, null);
        return container;
    }

    @Override
    public void onPull(float scale) {
        // scale 1.0表示释放刷新临界点
        if (scale <= 1.0f) {
            mHeaderCircleAnimView.updateCircle(cx, cy, radius, -80, scale * 340);
        } else {
            mHeaderCircleAnimView.updateCircle(cx, cy, radius, -80, 340);
        }
    }

    @Override
    protected void onStateChanged(State curState, State oldState) {
        super.onStateChanged(curState, oldState);
    }

    @Override
    protected void onReset() {
        mArrowIv.clearAnimation();
        mArrowIv.setVisibility(View.VISIBLE);
        mHeaderCircleAnimView.clearAnimation();
        mTextView.setText(R.string.pull_to_refresh_header_hint_normal);
    }

    @Override
    protected void onPullToRefresh() {
        if (State.RELEASE_TO_REFRESH == getPreState()) {
            mArrowIv.clearAnimation();
            mArrowIv.setVisibility(View.VISIBLE);
            mArrowIv.startAnimation(mArrowAnim2);
        }
        mTextView.setText(R.string.pull_to_refresh_header_hint_normal);
    }

    @Override
    protected void onReleaseToRefresh() {
        mArrowIv.clearAnimation();
        mArrowIv.setVisibility(View.VISIBLE);
        mArrowIv.startAnimation(mArrowAnim1);
        mTextView.setText(R.string.pull_to_refresh_header_hint_ready);
    }

    @Override
    protected void onRefreshing() {
        mArrowIv.setVisibility(View.GONE);
        mHeaderCircleAnimView.startAnimation(mHeaderCircleAnim);
        mTextView.setText(R.string.pull_to_refresh_header_hint_refresh);
    }
}
