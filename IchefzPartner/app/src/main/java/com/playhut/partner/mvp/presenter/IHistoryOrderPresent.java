package com.playhut.partner.mvp.presenter;

/**
 * 其他订单Present接口定义
 */
public interface IHistoryOrderPresent {

    /**
     * state: 1-confirm order    2-finish order   3-refund order
     */
    void getList(String state, int page, int pageSize);

}
