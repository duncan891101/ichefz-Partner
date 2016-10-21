package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.Account;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IEditPersonInfoPresent;
import com.playhut.partner.mvp.view.EditPersonInfoView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class EditPersonInfoPresent extends BasePresenter implements IEditPersonInfoPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private EditPersonInfoView mEditPersonInfoView;

    public EditPersonInfoPresent(Context context, EditPersonInfoView editPersonInfoView) {
        this.mContext = context;
        mEditPersonInfoView = editPersonInfoView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void edit(String content, int changeType) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.editPersonInfo(content, changeType);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mEditPersonInfoView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<Account>(null) {
            @Override
            public void onError(IchefzException exception) {
                mEditPersonInfoView.finishLoading();
                mEditPersonInfoView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<Account> subscriberResponse) {
                mEditPersonInfoView.finishLoading();
                mEditPersonInfoView.loadSuccess();
            }
        });
    }

}
