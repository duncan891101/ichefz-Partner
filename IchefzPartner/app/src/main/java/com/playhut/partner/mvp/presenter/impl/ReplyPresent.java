package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IReplyPresent;
import com.playhut.partner.mvp.view.ReplyView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class ReplyPresent extends BasePresenter implements IReplyPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private ReplyView mReplyView;

    public ReplyPresent(Context context, ReplyView replyView) {
        this.mContext = context;
        this.mReplyView = replyView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void reply(String senderId, String content, String parentId) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.replyMessage(senderId, content, parentId);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mReplyView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<String>(null) {
            @Override
            public void onError(IchefzException exception) {
                mReplyView.finishLoading();
                mReplyView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<String> subscriberResponse) {
                mReplyView.finishLoading();
                mReplyView.loadSuccess();
            }
        });
    }

}
