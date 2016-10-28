package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.Account;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IChangeMenuStatePresent;
import com.playhut.partner.mvp.view.ChangeMenuStateView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class ChangeMenuStatePresent extends BasePresenter implements IChangeMenuStatePresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private ChangeMenuStateView mChangeMenuStateView;

    public ChangeMenuStatePresent(Context context, ChangeMenuStateView changeMenuStateView) {
        this.mContext = context;
        this.mChangeMenuStateView = changeMenuStateView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void change(String menuId, String mtype, String state) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.changeMenuState(menuId, mtype, state);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mChangeMenuStateView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<Account>(null) {
            @Override
            public void onError(IchefzException exception) {
                mChangeMenuStateView.finishLoading();
                mChangeMenuStateView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<Account> subscriberResponse) {
                mChangeMenuStateView.finishLoading();
                mChangeMenuStateView.loadSuccess();
            }
        });
    }


}
