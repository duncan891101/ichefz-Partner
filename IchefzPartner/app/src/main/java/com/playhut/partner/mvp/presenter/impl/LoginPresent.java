package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.Account;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.ILoginPresent;
import com.playhut.partner.mvp.presenter.ISignUpPresent;
import com.playhut.partner.mvp.view.LoginView;
import com.playhut.partner.mvp.view.SignUpView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class LoginPresent extends BasePresenter implements ILoginPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private LoginView mLoginView;

    public LoginPresent(Context context, LoginView loginView) {
        this.mContext = context;
        this.mLoginView = loginView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void toLogin(String email, String pwd, String channelId) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.login(email, pwd, channelId);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mLoginView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<Account>(Account.class) {
            @Override
            public void onError(IchefzException exception) {
                mLoginView.finishLoading();
                mLoginView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<Account> subscriberResponse) {
                mLoginView.finishLoading();
                mLoginView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
