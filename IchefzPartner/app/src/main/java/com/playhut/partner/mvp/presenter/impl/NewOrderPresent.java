package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.NewOrderEntity;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.INewOrderPresent;
import com.playhut.partner.mvp.view.NewOrderView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class NewOrderPresent extends BasePresenter implements INewOrderPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private NewOrderView mNewOrderView;

    public NewOrderPresent(Context context, NewOrderView newOrderView) {
        this.mContext = context;
        this.mNewOrderView = newOrderView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void getList(String page, String pageSize) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.getNewOrderList(page, pageSize);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mNewOrderView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<NewOrderEntity>(NewOrderEntity.class) {
            @Override
            public void onError(IchefzException exception) {
                mNewOrderView.finishLoading();
                mNewOrderView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<NewOrderEntity> subscriberResponse) {
                mNewOrderView.finishLoading();
                mNewOrderView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
