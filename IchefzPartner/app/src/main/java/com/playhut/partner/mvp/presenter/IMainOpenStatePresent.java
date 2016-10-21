package com.playhut.partner.mvp.presenter;

/**
 * 首页营业状态接口定义
 */
public interface IMainOpenStatePresent {

    /**
     * 1 - 营业     0 - 非营业
     * @param state
     */
    void setOpenState(int state);

}
