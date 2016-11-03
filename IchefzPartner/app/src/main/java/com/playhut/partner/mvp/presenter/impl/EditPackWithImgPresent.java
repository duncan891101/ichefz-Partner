package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.AddPackEntity;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.UploadFileModel;
import com.playhut.partner.mvp.presenter.IEditPackWithImgPresent;
import com.playhut.partner.mvp.view.EditPackWithImgView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import java.io.File;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class EditPackWithImgPresent extends BasePresenter implements IEditPackWithImgPresent {

    private Context mContext;

    private UploadFileModel mUploadFileModel;

    private EditPackWithImgView mEditPackWithImgView;

    public EditPackWithImgPresent(Context context, EditPackWithImgView editPackWithImgView) {
        this.mContext = context;
        this.mEditPackWithImgView = editPackWithImgView;
        mUploadFileModel = new UploadFileModel();
    }

    @Override
    public void edit(File file, String menuId, String title, String brief, String desc, String person2,
                     String person4, String maxQuantity, String howMade) {
        Observable<ResponseBody> apiResponseObservable = mUploadFileModel.editPack(file, menuId, title, brief, desc, person2,
                person4, maxQuantity, howMade);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mEditPackWithImgView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<AddPackEntity>(null) {
            @Override
            public void onError(IchefzException exception) {
                mEditPackWithImgView.finishLoading();
                mEditPackWithImgView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<AddPackEntity> subscriberResponse) {
                mEditPackWithImgView.finishLoading();
                mEditPackWithImgView.loadSuccess();
            }
        });
    }

}
