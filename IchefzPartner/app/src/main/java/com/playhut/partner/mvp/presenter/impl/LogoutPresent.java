package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.Account;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.ILogoutPresent;
import com.playhut.partner.mvp.view.LogoutView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class LogoutPresent extends BasePresenter implements ILogoutPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private LogoutView mLogoutView;

    public LogoutPresent(Context context, LogoutView logoutView) {
        this.mContext = context;
        this.mLogoutView = logoutView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void logout(String channelId) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.logout(channelId);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mLogoutView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<Account>(null) {
            @Override
            public void onError(IchefzException exception) {
                mLogoutView.finishLoading();
                mLogoutView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<Account> subscriberResponse) {
                mLogoutView.finishLoading();
                mLogoutView.loadSuccess();
            }
        });
    }

}
