package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.Account;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IEditRestaurantInfoPresent;
import com.playhut.partner.mvp.view.EditRestaurantInfoView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class EditRestaurantInfoPresent extends BasePresenter implements IEditRestaurantInfoPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private EditRestaurantInfoView mEditRestaurantInfoView;

    public EditRestaurantInfoPresent(Context context, EditRestaurantInfoView editRestaurantInfoView) {
        this.mContext = context;
        this.mEditRestaurantInfoView = editRestaurantInfoView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void edit(String content, int changeType) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.editRestaurantInfo(content, changeType);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mEditRestaurantInfoView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<Account>(null) {
            @Override
            public void onError(IchefzException exception) {
                mEditRestaurantInfoView.finishLoading();
                mEditRestaurantInfoView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<Account> subscriberResponse) {
                mEditRestaurantInfoView.finishLoading();
                mEditRestaurantInfoView.loadSuccess();
            }
        });
    }

}
