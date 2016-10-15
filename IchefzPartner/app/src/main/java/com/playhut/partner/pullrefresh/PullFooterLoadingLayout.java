package com.playhut.partner.pullrefresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playhut.partner.R;

/**
 * 上拉布局
 */
public class PullFooterLoadingLayout extends LoadingLayout {

	private ImageView mLoadingIv;

	/**
	 * 显示的文本
	 */
	private TextView mTextView;
	/**
	 * 箭头
	 */
	private ImageView mArrowImage;

	private static final int ROTATE_ANIM_DURATION = 150;

	private LinearLayout mContentLayout;

	/**
	 * 构造方法
	 *
	 * @param context
	 *            context
	 */
	public PullFooterLoadingLayout(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 构造方法
	 *
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	public PullFooterLoadingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 初始化
	 *
	 * @param context
	 *            context
	 */
	private void init(Context context) {
		mLoadingIv = (ImageView) findViewById(R.id.iv_loading);
		mTextView = (TextView) findViewById(R.id.tv_text);
		mArrowImage = (ImageView) findViewById(R.id.iv_pull_up);
		mContentLayout = (LinearLayout) findViewById(R.id.pull_refresh_footer_content);
		setState(State.RESET);
	}

	@Override
	protected View createLoadingView(Context context, AttributeSet attrs) {
		View container = LayoutInflater.from(context).inflate(
				R.layout.pull_refresh_footer_view, null);
		return container;
	}

	@Override
	public int getContentSize() {
		if (null != mContentLayout) {
			return mContentLayout.getHeight();
		}

		return (int) (getResources().getDisplayMetrics().density * 60);
	}

	@Override
	protected void onStateChanged(State curState, State oldState) {
		super.onStateChanged(curState, oldState);
	}

	@Override
	protected void onReset() {
		mArrowImage.clearAnimation();
		mArrowImage.setVisibility(View.VISIBLE);

		mLoadingIv.clearAnimation();
		mLoadingIv.setVisibility(View.GONE);

		mTextView.setText(R.string.pull_to_refresh_header_hint_normal2);
	}

	@Override
	protected void onPullToRefresh() {
		if (State.RELEASE_TO_REFRESH == getPreState()) {
			mArrowImage.clearAnimation();
			mArrowImage.setVisibility(View.VISIBLE);
			startArrowAnim(mArrowImage, 180, 0);
		}
		mTextView.setText(R.string.pull_to_refresh_header_hint_normal2);
	}

	@Override
	protected void onReleaseToRefresh() {
		mArrowImage.clearAnimation();
		mArrowImage.setVisibility(View.VISIBLE);
		startArrowAnim(mArrowImage, 0, 180);
		mTextView.setText(R.string.pull_to_refresh_footer_hint_ready);
	}

	@Override
	protected void onRefreshing() {
		mArrowImage.clearAnimation();
		mArrowImage.setVisibility(View.GONE);

		mTextView.setText(R.string.pull_to_refresh_header_hint_loading);

		mLoadingIv.clearAnimation();
		mLoadingIv.setVisibility(View.VISIBLE);
		AnimationDrawable ad = (AnimationDrawable) mLoadingIv.getDrawable();
		ad.start();
	}

	@Override
	protected void onNoMoreData() {
		mArrowImage.clearAnimation();
		mArrowImage.setVisibility(View.GONE);

		mLoadingIv.clearAnimation();
		mLoadingIv.setVisibility(View.GONE);

		mTextView.setText(R.string.pull_to_refresh_no_more_data);
	}

	private void startArrowAnim(View v, float fromDegree, float toDegree) {
		RotateAnimation ra = new RotateAnimation(fromDegree, toDegree,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setFillAfter(true);
		ra.setDuration(ROTATE_ANIM_DURATION);
		v.startAnimation(ra);
	}

}
