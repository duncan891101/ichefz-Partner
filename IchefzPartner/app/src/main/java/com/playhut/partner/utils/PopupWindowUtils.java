package com.playhut.partner.utils;

import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.PopupWindow;

/**
 * 泡泡窗口工具类
 */
public class PopupWindowUtils {

    public static class LocationParams {
        public View parentView;
        public int gravity;
        public int offX;
        public int offY;
        public boolean showAtLocation;
    }

    /**
     * 显示在某个固定位置
     *
     * @param popupView
     * @param popupViewWidth
     * @param popupViewHeight
     * @param params
     * @param animationStyle  -1表示不需要动画
     * @return
     */
    public static PopupWindow show(View popupView, int popupViewWidth, int popupViewHeight, LocationParams params,
                                   int animationStyle) {
        PopupWindow popupWindow = new PopupWindow(popupView, popupViewWidth, popupViewHeight);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        if (animationStyle != -1) {
            popupWindow.setAnimationStyle(animationStyle);
        }
        if (params.showAtLocation) {
            popupWindow.showAtLocation(params.parentView, params.gravity, params.offX, params.offY);
        } else {
            popupWindow.showAsDropDown(params.parentView, params.offX, params.offY);
        }
        return popupWindow;
    }

}
