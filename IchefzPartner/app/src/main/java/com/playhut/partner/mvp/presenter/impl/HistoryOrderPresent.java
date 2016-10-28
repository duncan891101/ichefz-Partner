package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.NewOrderEntity;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IHistoryOrderPresent;
import com.playhut.partner.mvp.view.HistoryOrderView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class HistoryOrderPresent extends BasePresenter implements IHistoryOrderPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private HistoryOrderView mHistoryOrderView;

    public HistoryOrderPresent(Context context, HistoryOrderView historyOrderView) {
        this.mContext = context;
        this.mHistoryOrderView = historyOrderView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void getList(String state, int page, int pageSize) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.getHistoryOrderList(state, page, pageSize);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mHistoryOrderView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<NewOrderEntity>(NewOrderEntity.class) {
            @Override
            public void onError(IchefzException exception) {
                mHistoryOrderView.finishLoading();
                mHistoryOrderView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<NewOrderEntity> subscriberResponse) {
                mHistoryOrderView.finishLoading();
                mHistoryOrderView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
