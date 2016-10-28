package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IDeleteMsgPresent;
import com.playhut.partner.mvp.view.DeleteMsgView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class DeleteMsgPresent extends BasePresenter implements IDeleteMsgPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private DeleteMsgView mDeleteMsgView;

    public DeleteMsgPresent(Context context, DeleteMsgView deleteMsgView) {
        this.mContext = context;
        this.mDeleteMsgView = deleteMsgView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void delete(String messageId) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.deleteMsg(messageId);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mDeleteMsgView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<String>(null) {
            @Override
            public void onError(IchefzException exception) {
                mDeleteMsgView.finishLoading();
                mDeleteMsgView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<String> subscriberResponse) {
                mDeleteMsgView.finishLoading();
                mDeleteMsgView.loadSuccess();
            }
        });
    }

}
