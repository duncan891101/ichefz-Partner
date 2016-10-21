package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.Account;
import com.playhut.partner.entity.MainInfoEntity;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.ILoginPresent;
import com.playhut.partner.mvp.presenter.IMainInfoPresent;
import com.playhut.partner.mvp.view.LoginView;
import com.playhut.partner.mvp.view.MainInfoView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class MainInfoPresent extends BasePresenter implements IMainInfoPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private MainInfoView mMainInfoView;

    public MainInfoPresent(Context context, MainInfoView mainInfoView) {
        this.mContext = context;
        this.mMainInfoView = mainInfoView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void getInfo() {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.getMainInfo();
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mMainInfoView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<MainInfoEntity>(MainInfoEntity.class) {
            @Override
            public void onError(IchefzException exception) {
                mMainInfoView.finishLoading();
                mMainInfoView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<MainInfoEntity> subscriberResponse) {
                mMainInfoView.finishLoading();
                mMainInfoView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
