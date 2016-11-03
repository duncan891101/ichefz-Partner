package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.FeedbackEntity;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IFeedbackPresent;
import com.playhut.partner.mvp.view.FeedbackView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class FeedbackPresent extends BasePresenter implements IFeedbackPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private FeedbackView mFeedbackView;

    public FeedbackPresent(Context context, FeedbackView feedbackView) {
        this.mContext = context;
        this.mFeedbackView = feedbackView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void getList(int page, int pageSize) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.getFeedbackList(page, pageSize);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mFeedbackView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<FeedbackEntity>(FeedbackEntity.class) {
            @Override
            public void onError(IchefzException exception) {
                mFeedbackView.finishLoading();
                mFeedbackView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<FeedbackEntity> subscriberResponse) {
                mFeedbackView.finishLoading();
                mFeedbackView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
