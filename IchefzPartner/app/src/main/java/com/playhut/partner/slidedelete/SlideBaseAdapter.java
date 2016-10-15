package com.playhut.partner.slidedelete;

import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;

/**
 * @author zhangbh
 */
public abstract class SlideBaseAdapter extends BaseAdapter{

    /**
     * 记录滑动出删除按钮的itemView
     */
    public HorizontalScrollView mScrollView;

    /**
     * touch事件锁定,如果已经有滑动出删除按钮的itemView,就屏蔽下一整次(down,move,up)的onTouch操作
     */
    public boolean mLockOnTouch = false;

    /**
     * HorizontalScrollView左右滑动
     */
    public void scrollView(final HorizontalScrollView view, final int parameter) {
        view.post(new Runnable() {
            @Override
            public void run() {
                view.pageScroll(parameter);
            }
        });
    }

}
