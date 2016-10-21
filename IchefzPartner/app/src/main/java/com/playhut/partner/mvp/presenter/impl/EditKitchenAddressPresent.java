package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.Account;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IEditKitchenAddressPresent;
import com.playhut.partner.mvp.presenter.ILoginPresent;
import com.playhut.partner.mvp.view.EditKitchenAddressView;
import com.playhut.partner.mvp.view.LoginView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class EditKitchenAddressPresent extends BasePresenter implements IEditKitchenAddressPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private EditKitchenAddressView mEditKitchenAddressView;

    public EditKitchenAddressPresent(Context context, EditKitchenAddressView editKitchenAddressView) {
        this.mContext = context;
        this.mEditKitchenAddressView = editKitchenAddressView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void edit(String country, String street, String apt, String city, String state, String zipCode) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.editKitchenAddress(country, street, apt, city, state, zipCode);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mEditKitchenAddressView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<String>(String.class) {
            @Override
            public void onError(IchefzException exception) {
                mEditKitchenAddressView.finishLoading();
                mEditKitchenAddressView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<String> subscriberResponse) {
                mEditKitchenAddressView.finishLoading();
                mEditKitchenAddressView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
