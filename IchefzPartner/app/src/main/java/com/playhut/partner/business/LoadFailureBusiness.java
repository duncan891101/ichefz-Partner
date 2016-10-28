package com.playhut.partner.business;

import android.content.Context;

import com.playhut.partner.constants.NetworkConstants;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.ui.IchefzStateView;
import com.playhut.partner.utils.PartnerUtils;
import com.playhut.partner.utils.ToastUtils;

/**
 *
 */
public class LoadFailureBusiness {

    /**
     * @param reloadListener null时表示不显示Reload按钮
     */
    public static void loadFailure(final Context context, IchefzException exception, final IchefzStateView mIchefzStateView,
                                   final ReloadListener reloadListener) {
        ToastUtils.show(exception.getErrorMsg());
        if (exception.getErrorCode() == NetworkConstants.NETWORK_ERROR_CODE) {
            // Network error
            if (reloadListener != null) {
                mIchefzStateView.showNetworkErrorView(new IchefzStateView.ReloadListener() {
                    @Override
                    public void reload() {
                        errorReloadLogic(context, 1, mIchefzStateView, reloadListener);
                    }
                });
            } else {
                // 不显示Reload按钮
                mIchefzStateView.showNetworkErrorView(null);
            }
        } else {
            // Load Failure
            if (reloadListener != null) {
                mIchefzStateView.showLoadFailureView(new IchefzStateView.ReloadListener() {
                    @Override
                    public void reload() {
                        errorReloadLogic(context, 2, mIchefzStateView, reloadListener);
                    }
                });
            } else {
                // 不显示Reload按钮
                mIchefzStateView.showLoadFailureView(null);
            }
        }
    }

    private static void errorReloadLogic(Context context, int type, IchefzStateView mIchefzStateView, ReloadListener reloadListener) {
        if (!PartnerUtils.checkNetwork(context)) {
            ToastUtils.show(NetworkConstants.NETWORK_ERROR_MSG);
            return;
        }
        // 隐藏
        if (type == 1) {
            mIchefzStateView.dismissNetworkErrorView();
        } else {
            mIchefzStateView.dismissLoadFailureView();
        }
        // 获取数据
        reloadListener.onReload();
    }

    public interface ReloadListener {
        void onReload();
    }

}
