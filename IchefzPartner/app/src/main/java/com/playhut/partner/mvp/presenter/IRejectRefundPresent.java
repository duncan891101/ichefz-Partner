package com.playhut.partner.mvp.presenter;

/**
 * 拒绝退款Present接口定义
 */
public interface IRejectRefundPresent {

    void reject(String orderId, String reason);

}
