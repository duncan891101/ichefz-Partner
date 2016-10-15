package com.playhut.partner.slidedelete;

import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * 侧滑触摸监听
 *
 * @author zhangbh
 */
public class ItemSlideSVListener implements View.OnTouchListener {
    /**
     * 记录开始时的坐标
     */
    private float startX = 0;

    private SlideBaseAdapter mAdapter;

    private float mOffset;

    private int mPosition;

    private ClickListener mClickListener;

    public ItemSlideSVListener(SlideBaseAdapter adapter, float offset, ClickListener clickListener) {
        this.mAdapter = adapter;
        this.mOffset = offset;
        this.mClickListener = clickListener;
    }

    public void setPosition(int position){
        this.mPosition = position;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 如果有划出删除按钮的itemView,就让他滑回去并且锁定本次touch操作,解锁会在父组件的dispatchTouchEvent中进行
                if (mAdapter.mScrollView != null) {
                    mAdapter.scrollView(mAdapter.mScrollView, HorizontalScrollView.FOCUS_LEFT);
                    mAdapter.mScrollView = null;
                    mAdapter.mLockOnTouch = true;
                    return true;
                }
                startX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float currentX = event.getX();
                HorizontalScrollView view = (HorizontalScrollView) v;
                if (Math.abs(currentX - startX) < 20) {
                    mAdapter.scrollView(view, HorizontalScrollView.FOCUS_LEFT);
                    if(mClickListener != null) {
                        mClickListener.click(mPosition);
                    }
                    break;
                }
                if (startX > (currentX + mOffset)) {
                    startX = 0;// 因为公用一个事件处理对象,防止错乱,还原startX值
                    mAdapter.scrollView(view, HorizontalScrollView.FOCUS_RIGHT);
                    mAdapter.mScrollView = view;
                } else {
                    mAdapter.scrollView(view, HorizontalScrollView.FOCUS_LEFT);
                }
                break;
        }
        return false;
    }

    public interface ClickListener {
        public void click(int position);
    }

}
