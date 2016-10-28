package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.Account;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IAddSetPresent;
import com.playhut.partner.mvp.view.AddSetView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class AddSetPresent extends BasePresenter implements IAddSetPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private AddSetView mAddSetView;

    public AddSetPresent(Context context, AddSetView addSetView) {
        this.mContext = context;
        this.mAddSetView = addSetView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void add(String packIds, String setName, String desc, String person2, String person4, String maxQuantity) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.addSet(packIds, setName, desc, person2, person4, maxQuantity);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mAddSetView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<Account>(null) {
            @Override
            public void onError(IchefzException exception) {
                mAddSetView.finishLoading();
                mAddSetView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<Account> subscriberResponse) {
                mAddSetView.finishLoading();
                mAddSetView.loadSuccess();
            }
        });
    }

}
