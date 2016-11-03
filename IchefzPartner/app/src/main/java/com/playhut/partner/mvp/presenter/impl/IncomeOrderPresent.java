package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.FinanceListEntity;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IIncomeOrderPresent;
import com.playhut.partner.mvp.view.IncomeOrderView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class IncomeOrderPresent extends BasePresenter implements IIncomeOrderPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private IncomeOrderView mIncomeOrderView;

    public IncomeOrderPresent(Context context, IncomeOrderView incomeOrderView) {
        this.mContext = context;
        this.mIncomeOrderView = incomeOrderView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void getList(String date, int page, int pageSize) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.getIncomeOrderList(date, page, pageSize);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mIncomeOrderView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<FinanceListEntity>(FinanceListEntity.class) {
            @Override
            public void onError(IchefzException exception) {
                mIncomeOrderView.finishLoading();
                mIncomeOrderView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<FinanceListEntity> subscriberResponse) {
                mIncomeOrderView.finishLoading();
                mIncomeOrderView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
