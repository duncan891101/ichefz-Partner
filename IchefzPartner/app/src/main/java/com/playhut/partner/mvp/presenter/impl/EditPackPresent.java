package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.Account;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IEditPackPresent;
import com.playhut.partner.mvp.view.EditPackView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class EditPackPresent extends BasePresenter implements IEditPackPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private EditPackView mEditPackView;

    public EditPackPresent(Context context, EditPackView editPackView) {
        this.mContext = context;
        this.mEditPackView = editPackView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void edit(String menuId, String title, String brief, String desc, String person2, String person4, String maxQuantity, String howMade) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.editPack(menuId, title, brief, desc, person2, person4, maxQuantity, howMade);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mEditPackView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<Account>(null) {
            @Override
            public void onError(IchefzException exception) {
                mEditPackView.finishLoading();
                mEditPackView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<Account> subscriberResponse) {
                mEditPackView.finishLoading();
                mEditPackView.loadSuccess();
            }
        });
    }

}
