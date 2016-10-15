package com.playhut.partner.constants;

/**
 *
 */
public class OrderState {
    // 1已付款，等待厨师确认订单
    public static final int PAID_WAIT_CONFIRM = 1;
    // 2已付款，厨师已确认订单，等待客户确认收货
    public static final int PAID_WAIT_RECEIPT = 2;
    // 3申请退款
    public static final int APPLY_REFUND = 3;
    // 4退款成功
    public static final int REFUND_SUCCESS = 4;
    // 5退款驳回
    public static final int REFUND_REJECT = 5;
    // 8已完成
    public static final int FINISHED = 8;
}
