package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.Account;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IDeleteTipPresent;
import com.playhut.partner.mvp.view.DeleteTipView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class DeleteTipPresent extends BasePresenter implements IDeleteTipPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private DeleteTipView mDeleteTipView;

    public DeleteTipPresent(Context context, DeleteTipView deleteTipView) {
        this.mContext = context;
        this.mDeleteTipView = deleteTipView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void delete(String menuId, int type, String id) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.deleteTip(menuId, type, id);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mDeleteTipView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<Account>(null) {
            @Override
            public void onError(IchefzException exception) {
                mDeleteTipView.finishLoading();
                mDeleteTipView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<Account> subscriberResponse) {
                mDeleteTipView.finishLoading();
                mDeleteTipView.loadSuccess();
            }
        });
    }

}
