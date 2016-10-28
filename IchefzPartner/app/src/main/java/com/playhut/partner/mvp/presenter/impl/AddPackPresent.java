package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.AddPackEntity;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.UploadFileModel;
import com.playhut.partner.mvp.presenter.IAddPackPresent;
import com.playhut.partner.mvp.view.AddPackView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import java.io.File;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class AddPackPresent extends BasePresenter implements IAddPackPresent {

    private Context mContext;

    private UploadFileModel mUploadFileModel;

    private AddPackView mAddPackView;

    public AddPackPresent(Context context, AddPackView addPackView) {
        this.mContext = context;
        this.mAddPackView = addPackView;
        mUploadFileModel = new UploadFileModel();
    }

    @Override
    public void add(File file, String title, String brief, String desc, String person2, String person4, String maxQuantity, String howMade) {
        Observable<ResponseBody> apiResponseObservable = mUploadFileModel.addPack(file, title, brief, desc, person2, person4, maxQuantity, howMade);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mAddPackView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<AddPackEntity>(AddPackEntity.class) {
            @Override
            public void onError(IchefzException exception) {
                mAddPackView.finishLoading();
                mAddPackView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<AddPackEntity> subscriberResponse) {
                mAddPackView.finishLoading();
                mAddPackView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
