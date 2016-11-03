package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.entity.AddPackEntity;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.mvp.model.UploadFileModel;
import com.playhut.partner.mvp.presenter.IEditTipPresent;
import com.playhut.partner.mvp.view.EditTipView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.network.IchefzSubscriber;

import java.io.File;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 *
 */
public class EditTipPresent extends BasePresenter implements IEditTipPresent {

    private Context mContext;

    private UploadFileModel mUploadFileModel;

    private EditTipView mEditTipView;

    public EditTipPresent(Context context, EditTipView editTipView) {
        this.mContext = context;
        this.mEditTipView = editTipView;
        mUploadFileModel = new UploadFileModel();
    }

    @Override
    public void edit(String menuId, int type, String ids, Map<String, File> map, String titles, String descs) {
        Observable<ResponseBody> apiResponseObservable = mUploadFileModel.editTip(menuId, type, ids, map, titles, descs);
        standardScheduler(mContext, apiResponseObservable, new StartLoadingListener() {
            @Override
            public void startLoading() {
                mEditTipView.startLoading();
            }
        }).subscribe(new IchefzSubscriber<AddPackEntity>(null) {
            @Override
            public void onError(IchefzException exception) {
                mEditTipView.finishLoading();
                mEditTipView.loadFailure(exception);
            }

            @Override
            public void onSuccess(SubscriberResponse<AddPackEntity> subscriberResponse) {
                mEditTipView.finishLoading();
                mEditTipView.loadSuccess();
            }
        });
    }

}
