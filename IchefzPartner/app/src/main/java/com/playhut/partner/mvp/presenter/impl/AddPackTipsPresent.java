package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.Account;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.UploadFileModel;
import com.playhut.partner.mvp.presenter.IAddPackTipsPresent;
import com.playhut.partner.mvp.view.AddPackTipsView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import java.io.File;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class AddPackTipsPresent extends BasePresenter implements IAddPackTipsPresent {

    private Context mContext;

    private UploadFileModel mUploadFileModel;

    private AddPackTipsView mAddPackTipsView;

    public AddPackTipsPresent(Context context, AddPackTipsView addPackTipsView) {
        this.mContext = context;
        this.mAddPackTipsView = addPackTipsView;
        mUploadFileModel = new UploadFileModel();
    }

    @Override
    public void add(String menuId, String type, List<File> fileList, String titles, String descs) {
        Observable<ResponseBody> apiResponseObservable = mUploadFileModel.addPackTips(menuId, type, fileList, titles, descs);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mAddPackTipsView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<Account>(null) {
            @Override
            public void onError(IchefzException exception) {
                mAddPackTipsView.finishLoading();
                mAddPackTipsView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<Account> subscriberResponse) {
                mAddPackTipsView.finishLoading();
                mAddPackTipsView.loadSuccess();
            }
        });
    }

}
