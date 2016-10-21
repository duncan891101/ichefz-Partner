package com.playhut.partner.mvp.view;

import com.playhut.partner.network.IchefzException;

/**
 *
 */
public abstract class BaseView {

    public void startLoading() {

    }

    public void finishLoading() {

    }

    public abstract void loadFailure(IchefzException exception);

}
