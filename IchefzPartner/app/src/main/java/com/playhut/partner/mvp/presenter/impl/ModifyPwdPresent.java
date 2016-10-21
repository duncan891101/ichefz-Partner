package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.NetworkModel;
import com.playhut.partner.mvp.presenter.IModifyPwdPresent;
import com.playhut.partner.mvp.view.ModifyPwdView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class ModifyPwdPresent extends BasePresenter implements IModifyPwdPresent {

    private Context mContext;

    private NetworkModel mNetworkModel;

    private ModifyPwdView mModifyPwdView;

    public ModifyPwdPresent(Context context, ModifyPwdView modifyPwdView) {
        this.mContext = context;
        this.mModifyPwdView = modifyPwdView;
        mNetworkModel = new NetworkModel();
    }

    @Override
    public void modify(String oldPwd, String newPwd) {
        Observable<ResponseBody> apiResponseObservable = mNetworkModel.modifyPwd(oldPwd, newPwd);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mModifyPwdView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<String>(String.class) {
            @Override
            public void onError(IchefzException exception) {
                mModifyPwdView.finishLoading();
                mModifyPwdView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<String> subscriberResponse) {
                mModifyPwdView.finishLoading();
                mModifyPwdView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
