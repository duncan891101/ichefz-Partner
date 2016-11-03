package com.playhut.partner.mvp.presenter;

import android.content.Context;

import com.playhut.partner.entity.PackTipEntity;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.impl.BasePresenter;
import com.playhut.partner.mvp.view.GetPackTipView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class GetPackTipPresent extends BasePresenter implements IGetPackTipPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private GetPackTipView mGetPackTipView;

    public GetPackTipPresent(Context context, GetPackTipView getPackTipView) {
        this.mContext = context;
        this.mGetPackTipView = getPackTipView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void get(String menuId, int type) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.getPackTip(menuId, type);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mGetPackTipView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<PackTipEntity>(PackTipEntity.class) {
            @Override
            public void onError(IchefzException exception) {
                mGetPackTipView.finishLoading();
                mGetPackTipView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<PackTipEntity> subscriberResponse) {
                mGetPackTipView.finishLoading();
                mGetPackTipView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
