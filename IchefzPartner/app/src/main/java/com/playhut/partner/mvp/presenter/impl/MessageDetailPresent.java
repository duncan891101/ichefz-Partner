package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.MessageDetailEntity;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IMessageDetailPresent;
import com.playhut.partner.mvp.view.MessageDetailView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class MessageDetailPresent extends BasePresenter implements IMessageDetailPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private MessageDetailView mMessageDetailView;

    public MessageDetailPresent(Context context, MessageDetailView messageDetailView) {
        this.mContext = context;
        this.mMessageDetailView = messageDetailView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void getList(String senderId, int page, int pageSize) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.getMessageDetail(senderId, page, pageSize);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mMessageDetailView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<MessageDetailEntity>(MessageDetailEntity.class) {
            @Override
            public void onError(IchefzException exception) {
                mMessageDetailView.finishLoading();
                mMessageDetailView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<MessageDetailEntity> subscriberResponse) {
                mMessageDetailView.finishLoading();
                mMessageDetailView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
