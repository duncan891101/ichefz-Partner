package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.MyMenuSetEntity;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IGetMenuSetListPresent;
import com.playhut.partner.mvp.view.GetMenuSetListView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class GetMenuSetListPresent extends BasePresenter implements IGetMenuSetListPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private GetMenuSetListView mGetMenuSetListView;

    public GetMenuSetListPresent(Context context, GetMenuSetListView getMenuSetListView) {
        this.mContext = context;
        this.mGetMenuSetListView = getMenuSetListView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void getList(int page, int pageSize) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.getMenuSetList(page, pageSize);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mGetMenuSetListView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<MyMenuSetEntity>(MyMenuSetEntity.class) {
            @Override
            public void onError(IchefzException exception) {
                mGetMenuSetListView.finishLoading();
                mGetMenuSetListView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<MyMenuSetEntity> subscriberResponse) {
                mGetMenuSetListView.finishLoading();
                mGetMenuSetListView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
