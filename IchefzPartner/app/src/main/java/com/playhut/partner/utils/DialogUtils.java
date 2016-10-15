package com.playhut.partner.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.constants.GlobalConstants;

/**
 * 对话框工具类
 */
public class DialogUtils {

    /**
     * 加载对话框
     *
     * @param context
     * @param backKeyFinishDialog
     * @return
     */
    public static Dialog showLoadingDialog(Context context, String msg, boolean backKeyFinishDialog) {
        Activity activity = (Activity) context;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        Dialog dialog = new Dialog(activity, R.style.loading_dialog_style);
        dialog.setContentView(R.layout.loading_dialog_layout);

        TextView msgTv = (TextView) dialog.findViewById(R.id.tv_loading_msg);
        msgTv.setText(msg);

        ImageView loadingImage = (ImageView) dialog.findViewById(R.id.iv_anim);
        AnimationDrawable ad = (AnimationDrawable) loadingImage.getDrawable();
        ad.start();

        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        dialog.setCancelable(backKeyFinishDialog); // 返回键是否取消对话框
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    public static Dialog showBottomDialog(Context context, int layoutId, boolean backKeyFinishDialog, boolean touchOutsideFinish) {
        Activity activity = (Activity) context;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        Dialog dialog = new Dialog(activity, R.style.bottom_dialog_style);
        dialog.setContentView(layoutId);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);

        params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        dialog.setCancelable(backKeyFinishDialog);
        dialog.setCanceledOnTouchOutside(touchOutsideFinish);

        dialog.show();
        return dialog;
    }

    public static Dialog showConfirmDialog(Context context, int layoutId, boolean backKeyCanCancel) {
        Activity activity = (Activity) context;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }

        Dialog dialog = new Dialog(activity, R.style.confirm_dialog_no_anim);
        dialog.setContentView(layoutId);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        window.setGravity(Gravity.CENTER);

        params.width = GlobalConstants.SCREEN_WIDTH - PartnerUtils.dip2px(context, 30) * 2;
        params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        dialog.setCancelable(backKeyCanCancel);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
        return dialog;
    }

    /**
     * 回复消息时用的Dialog
     */
    public static Dialog showSendMessageDialog(Context context, int layoutId, boolean backKeyFinishDialog, boolean touchOutsideFinish) {
        Activity activity = (Activity) context;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        Dialog dialog = new Dialog(activity, R.style.send_msg_dialog_style);
        dialog.setContentView(layoutId);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        window.setGravity(Gravity.CENTER);

        params.width = GlobalConstants.SCREEN_WIDTH - PartnerUtils.dip2px(context, 20);
        params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        dialog.setCancelable(backKeyFinishDialog);
        dialog.setCanceledOnTouchOutside(touchOutsideFinish);

        dialog.show();
        return dialog;
    }


}
