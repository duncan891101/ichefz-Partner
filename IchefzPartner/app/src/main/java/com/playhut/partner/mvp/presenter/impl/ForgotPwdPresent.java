package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.Account;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IForgotPwdPresent;
import com.playhut.partner.mvp.presenter.ILoginPresent;
import com.playhut.partner.mvp.view.ForgotPwdView;
import com.playhut.partner.mvp.view.LoginView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class ForgotPwdPresent extends BasePresenter implements IForgotPwdPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private ForgotPwdView mForgotPwdView;

    public ForgotPwdPresent(Context context, ForgotPwdView forgotPwdView) {
        this.mContext = context;
        this.mForgotPwdView = forgotPwdView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void forgotPwd(String email) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.forgotPwd(email);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mForgotPwdView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<String>(String.class) {
            @Override
            public void onError(IchefzException exception) {
                mForgotPwdView.finishLoading();
                mForgotPwdView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<String> subscriberResponse) {
                mForgotPwdView.finishLoading();
                mForgotPwdView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
