package com.playhut.partner.mvp.presenter.impl;

import android.content.Context;

import com.playhut.partner.base.BaseActivity;
import com.trello.rxlifecycle.ActivityEvent;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 *
 */
public class BasePresenter {

    /**
     * 标准的RxJava调度，目前绑定的是当执行BaseActivity 执行Destory方法，就会自动解绑
     * 如果需要手动解绑，则可以通过Subscription.unsubscribe来解绑
     * @param context BaseActivity的上下文
     * @param apiResponseObservable API网络层返回的Observable
     * @param listener 开始加载前的监听回调，用于加载对话框，如果不需要关心加载对话框可传null
     * @return
     */
    protected Observable<ResponseBody> standardScheduler(Context context, Observable<ResponseBody> apiResponseObservable,
                                                         final StartLoadingListener listener) {
        BaseActivity baseActivity = (BaseActivity) context;
        return apiResponseObservable
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (listener != null) {
                            listener.startLoading();
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(baseActivity.<ResponseBody>bindUntilEvent(ActivityEvent.DESTROY));
    }

    public interface StartLoadingListener {
        void startLoading();
    }

}
