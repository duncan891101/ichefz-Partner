package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IEditBankInfoPresent;
import com.playhut.partner.mvp.view.EditBankInfoView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class EditBankInfoPresent extends BasePresenter implements IEditBankInfoPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private EditBankInfoView mEditBankInfoView;

    public EditBankInfoPresent(Context context, EditBankInfoView editBankInfoView) {
        this.mContext = context;
        mEditBankInfoView = editBankInfoView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void edit(String name, String routingNum, String accountNum, String tax, String ssn) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.editBankInfo(name, routingNum, accountNum, tax, ssn);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mEditBankInfoView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<String>(String.class) {
            @Override
            public void onError(IchefzException exception) {
                mEditBankInfoView.finishLoading();
                mEditBankInfoView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<String> subscriberResponse) {
                mEditBankInfoView.finishLoading();
                mEditBankInfoView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
