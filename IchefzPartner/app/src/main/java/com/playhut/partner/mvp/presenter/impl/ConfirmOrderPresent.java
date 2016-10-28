package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IConfirmOrderPresent;
import com.playhut.partner.mvp.view.ConfirmOrderView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class ConfirmOrderPresent extends BasePresenter implements IConfirmOrderPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private ConfirmOrderView mConfirmOrderView;

    public ConfirmOrderPresent(Context context, ConfirmOrderView confirmOrderView) {
        this.mContext = context;
        this.mConfirmOrderView = confirmOrderView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void confirm(String orderId) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.confirmOrder(orderId);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mConfirmOrderView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<String>(String.class) {
            @Override
            public void onError(IchefzException exception) {
                mConfirmOrderView.finishLoading();
                mConfirmOrderView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<String> subscriberResponse) {
                mConfirmOrderView.finishLoading();
                mConfirmOrderView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
