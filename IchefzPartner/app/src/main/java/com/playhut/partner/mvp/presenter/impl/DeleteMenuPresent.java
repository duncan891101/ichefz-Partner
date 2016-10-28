package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IDeleteMenuPresent;
import com.playhut.partner.mvp.view.DeleteMenuView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class DeleteMenuPresent extends BasePresenter implements IDeleteMenuPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private DeleteMenuView mDeleteMenuView;

    public DeleteMenuPresent(Context context, DeleteMenuView deleteMenuView) {
        this.mContext = context;
        this.mDeleteMenuView = deleteMenuView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void delete(String menuId, String mtype) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.deleteMenu(menuId, mtype);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mDeleteMenuView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<String>(String.class) {
            @Override
            public void onError(IchefzException exception) {
                mDeleteMenuView.finishLoading();
                mDeleteMenuView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<String> subscriberResponse) {
                mDeleteMenuView.finishLoading();
                mDeleteMenuView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
