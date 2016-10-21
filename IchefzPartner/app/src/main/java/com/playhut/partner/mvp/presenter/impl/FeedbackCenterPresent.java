package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.Account;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IFeedbackCenterPresent;
import com.playhut.partner.mvp.presenter.ILoginPresent;
import com.playhut.partner.mvp.view.FeedbackCenterView;
import com.playhut.partner.mvp.view.LoginView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class FeedbackCenterPresent extends BasePresenter implements IFeedbackCenterPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private FeedbackCenterView mFeedbackCenterView;

    public FeedbackCenterPresent(Context context, FeedbackCenterView feedbackCenterView) {
        this.mContext = context;
        this.mFeedbackCenterView = feedbackCenterView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void send(String content) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.feedbackCenter(content);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mFeedbackCenterView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<String>(String.class) {
            @Override
            public void onError(IchefzException exception) {
                mFeedbackCenterView.finishLoading();
                mFeedbackCenterView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<String> subscriberResponse) {
                mFeedbackCenterView.finishLoading();
                mFeedbackCenterView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
