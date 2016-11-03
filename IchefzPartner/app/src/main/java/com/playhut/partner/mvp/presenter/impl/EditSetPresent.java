package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.Account;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IEditSetPresent;
import com.playhut.partner.mvp.view.EditSetView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class EditSetPresent extends BasePresenter implements IEditSetPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private EditSetView mEditSetView;

    public EditSetPresent(Context context, EditSetView editSetView) {
        this.mContext = context;
        this.mEditSetView = editSetView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void edit(String menuId, String ids, String title, String desc, String person2, String person4, String maxQuantity) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.editSet(menuId, ids, title, desc, person2, person4, maxQuantity);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mEditSetView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<Account>(null) {
            @Override
            public void onError(IchefzException exception) {
                mEditSetView.finishLoading();
                mEditSetView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<Account> subscriberResponse) {
                mEditSetView.finishLoading();
                mEditSetView.loadSuccess();
            }
        });
    }

}
