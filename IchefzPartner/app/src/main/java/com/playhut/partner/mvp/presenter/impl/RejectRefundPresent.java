package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IRejectRefundPresent;
import com.playhut.partner.mvp.view.RejectRefundView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class RejectRefundPresent extends BasePresenter implements IRejectRefundPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private RejectRefundView mRejectRefundView;

    public RejectRefundPresent(Context context, RejectRefundView rejectRefundView) {
        this.mContext = context;
        this.mRejectRefundView = rejectRefundView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void reject(String orderId) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.rejectRefund(orderId);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mRejectRefundView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<String>(String.class) {
            @Override
            public void onError(IchefzException exception) {
                mRejectRefundView.finishLoading();
                mRejectRefundView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<String> subscriberResponse) {
                mRejectRefundView.finishLoading();
                mRejectRefundView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
