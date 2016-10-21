package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.Account;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IMainOpenStatePresent;
import com.playhut.partner.mvp.presenter.ISignUpPresent;
import com.playhut.partner.mvp.view.MainOpenStateView;
import com.playhut.partner.mvp.view.SignUpView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class MainOpenStatePresent extends BasePresenter implements IMainOpenStatePresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private MainOpenStateView mMainOpenStateView;

    public MainOpenStatePresent(Context context, MainOpenStateView mainOpenStateView) {
        this.mContext = context;
        mMainOpenStateView = mainOpenStateView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void setOpenState(int state) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.setOpenState(state);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mMainOpenStateView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<Account>(null) {
            @Override
            public void onError(IchefzException exception) {
                mMainOpenStateView.finishLoading();
                mMainOpenStateView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<Account> subscriberResponse) {
                mMainOpenStateView.finishLoading();
                mMainOpenStateView.loadSuccess();
            }
        });
    }

}
