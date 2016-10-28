package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.Account;
import com.playhut.partner.entity.SelectPackEntity;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.ILoginPresent;
import com.playhut.partner.mvp.presenter.ISelectPackPresent;
import com.playhut.partner.mvp.view.LoginView;
import com.playhut.partner.mvp.view.SelectPackView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class SelectPackPresent extends BasePresenter implements ISelectPackPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private SelectPackView mSelectPackView;

    public SelectPackPresent(Context context, SelectPackView selectPackView) {
        this.mContext = context;
        this.mSelectPackView = selectPackView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void getList() {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.selectPack();
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mSelectPackView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<SelectPackEntity>(SelectPackEntity.class) {
            @Override
            public void onError(IchefzException exception) {
                mSelectPackView.finishLoading();
                mSelectPackView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<SelectPackEntity> subscriberResponse) {
                mSelectPackView.finishLoading();
                mSelectPackView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
