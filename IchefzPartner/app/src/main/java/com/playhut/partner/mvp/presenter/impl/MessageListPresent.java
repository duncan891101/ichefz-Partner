package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.MessageListEntity;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IMessageListPresent;
import com.playhut.partner.mvp.view.MessageListView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class MessageListPresent extends BasePresenter implements IMessageListPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private MessageListView mMessageListView;

    public MessageListPresent(Context context, MessageListView messageListView) {
        this.mContext = context;
        this.mMessageListView = messageListView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void getList(int page, int pageSize) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.getMessageList(page, pageSize);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mMessageListView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<MessageListEntity>(MessageListEntity.class) {
            @Override
            public void onError(IchefzException exception) {
                mMessageListView.finishLoading();
                mMessageListView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<MessageListEntity> subscriberResponse) {
                mMessageListView.finishLoading();
                mMessageListView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
