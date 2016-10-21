package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.UploadFileModel;
import com.playhut.partner.mvp.presenter.IUploadAvatarPresent;
import com.playhut.partner.mvp.view.UploadAvatarView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import java.io.File;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class UploadAvatarPresent extends BasePresenter implements IUploadAvatarPresent {

    private Context mContext;

    private UploadFileModel mUploadFileModel;

    private UploadAvatarView mUploadAvatarView;

    public UploadAvatarPresent(Context context, UploadAvatarView uploadAvatarView) {
        this.mContext = context;
        this.mUploadAvatarView = uploadAvatarView;
        mUploadFileModel = new UploadFileModel();
    }

    @Override
    public void upload(File file) {
        Observable<ResponseBody> apiResponseObservable = mUploadFileModel.uploadAvatar(file);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mUploadAvatarView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<String>(String.class) {
            @Override
            public void onError(IchefzException exception) {
                mUploadAvatarView.finishLoading();
                mUploadAvatarView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<String> subscriberResponse) {
                mUploadAvatarView.finishLoading();
                mUploadAvatarView.loadSuccess(subscriberResponse.getResult());
            }
        });
    }

}
