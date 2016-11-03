package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.IncomeEntity;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IIncomePresent;
import com.playhut.partner.mvp.view.IncomeView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class IncomePresent extends BasePresenter implements IIncomePresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private IncomeView mIncomeView;

    public IncomePresent(Context context, IncomeView incomeView) {
        this.mContext = context;
        this.mIncomeView = incomeView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void getIncome() {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.getIncome();
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mIncomeView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<IncomeEntity>(IncomeEntity.class) {
            @Override
            public void onError(IchefzException exception) {
                mIncomeView.finishLoading();
                mIncomeView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<IncomeEntity> subscriberResponse) {
                mIncomeView.finishLoading();
                mIncomeView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
