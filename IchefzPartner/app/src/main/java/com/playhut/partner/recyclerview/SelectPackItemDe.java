package com.playhut.partner.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 */
public class SelectPackItemDe extends RecyclerView.ItemDecoration {

    private int mLeftSpace;

    private int mRightSpace;

    public SelectPackItemDe(int leftSpace, int rightSpace) {
        this.mLeftSpace = leftSpace;
        this.mRightSpace = rightSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            // 每行的第一个
            outRect.left = mLeftSpace;
            outRect.right = mRightSpace;
        } else {
            outRect.left = mRightSpace;
            outRect.right = mLeftSpace;
        }
    }
}
