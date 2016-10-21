package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.RestaurantEntity;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IGetRestaurantInfoPresent;
import com.playhut.partner.mvp.view.GetRestaurantInfoView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class GetRestaurantInfoPresent extends BasePresenter implements IGetRestaurantInfoPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private GetRestaurantInfoView mGetRestaurantInfoView;

    public GetRestaurantInfoPresent(Context context, GetRestaurantInfoView getRestaurantInfoView) {
        this.mContext = context;
        this.mGetRestaurantInfoView = getRestaurantInfoView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void get() {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.getRestaurantInfo();
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mGetRestaurantInfoView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<RestaurantEntity>(RestaurantEntity.class) {
            @Override
            public void onError(IchefzException exception) {
                mGetRestaurantInfoView.finishLoading();
                mGetRestaurantInfoView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<RestaurantEntity> subscriberResponse) {
                mGetRestaurantInfoView.finishLoading();
                mGetRestaurantInfoView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
