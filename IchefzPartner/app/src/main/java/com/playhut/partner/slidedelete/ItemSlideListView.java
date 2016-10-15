package com.playhut.partner.slidedelete;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 *
 */
public class ItemSlideListView extends ListView {
    private SlideBaseAdapter mAdapter;

    public ItemSlideListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        /**
         * 如果说当前已经被锁定了,那么这次滑动ListView操作就需要截断掉
         */
        if (mAdapter.mLockOnTouch) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN
                    || ev.getAction() == MotionEvent.ACTION_MOVE) {
                //让滑动出删除按钮的那个itemView退回去
                if (mAdapter.mScrollView != null) {
                    mAdapter.scrollView(mAdapter.mScrollView,
                            HorizontalScrollView.FOCUS_LEFT);
                    mAdapter.mScrollView = null;
                }
                return true;
            }
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                mAdapter.mLockOnTouch = false;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = (SlideBaseAdapter) adapter;
    }
}
