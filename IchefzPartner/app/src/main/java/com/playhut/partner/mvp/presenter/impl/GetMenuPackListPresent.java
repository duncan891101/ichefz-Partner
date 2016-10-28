package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.MyMenuPackEntity;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IGetMenuPackListPresent;
import com.playhut.partner.mvp.view.GetMenuPackListView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class GetMenuPackListPresent extends BasePresenter implements IGetMenuPackListPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private GetMenuPackListView mGetMenuPackListView;

    public GetMenuPackListPresent(Context context, GetMenuPackListView getMenuPackListView) {
        this.mContext = context;
        this.mGetMenuPackListView = getMenuPackListView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void getList(int page, int pageSize) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.getMenuPackList(page, pageSize);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mGetMenuPackListView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<MyMenuPackEntity>(MyMenuPackEntity.class) {
            @Override
            public void onError(IchefzException exception) {
                mGetMenuPackListView.finishLoading();
                mGetMenuPackListView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<MyMenuPackEntity> subscriberResponse) {
                mGetMenuPackListView.finishLoading();
                mGetMenuPackListView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
