package com.playhut.partner.mvp.presenter;

/**
 * 拒绝订单Present接口定义
 */
public interface IRefundOrderPresent {

    void refund(String orderId, String reason);

}
