package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.Account;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.ISignUpPresent;
import com.playhut.partner.mvp.view.SignUpView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class SignUpPresent extends BasePresenter implements ISignUpPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private SignUpView mSignUpView;

    public SignUpPresent(Context context, SignUpView signUpView) {
        this.mContext = context;
        this.mSignUpView = signUpView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void toSignUp(String email, String pwd, String channelId, String firstName, String lastName, String phoneNumber) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.signUp(email, pwd, channelId, firstName, lastName, phoneNumber);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mSignUpView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<Account>(Account.class) {
            @Override
            public void onError(IchefzException exception) {
                mSignUpView.finishLoading();
                mSignUpView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<Account> subscriberResponse) {
                mSignUpView.finishLoading();
                mSignUpView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
