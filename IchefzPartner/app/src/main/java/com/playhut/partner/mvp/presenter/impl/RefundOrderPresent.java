package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IRefundOrderPresent;
import com.playhut.partner.mvp.view.RefundOrderView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class RefundOrderPresent extends BasePresenter implements IRefundOrderPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private RefundOrderView mRefundOrderView;

    public RefundOrderPresent(Context context, RefundOrderView refundOrderView) {
        this.mContext = context;
        this.mRefundOrderView = refundOrderView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void refund(String orderId, String reason) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.refundOrder(orderId, reason);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mRefundOrderView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<String>(String.class) {
            @Override
            public void onError(IchefzException exception) {
                mRefundOrderView.finishLoading();
                mRefundOrderView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<String> subscriberResponse) {
                mRefundOrderView.finishLoading();
                mRefundOrderView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
