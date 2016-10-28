package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IConfirmRefundPresent;
import com.playhut.partner.mvp.view.ConfirmRefundView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class ConfirmRefundPresent extends BasePresenter implements IConfirmRefundPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private ConfirmRefundView mConfirmRefundView;

    public ConfirmRefundPresent(Context context, ConfirmRefundView confirmRefundView) {
        this.mContext = context;
        this.mConfirmRefundView = confirmRefundView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void refund(String orderId) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.confirmRefund(orderId);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mConfirmRefundView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<String>(String.class) {
            @Override
            public void onError(IchefzException exception) {
                mConfirmRefundView.finishLoading();
                mConfirmRefundView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<String> subscriberResponse) {
                mConfirmRefundView.finishLoading();
                mConfirmRefundView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
