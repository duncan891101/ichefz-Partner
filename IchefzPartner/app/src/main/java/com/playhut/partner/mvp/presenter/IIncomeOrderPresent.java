package com.playhut.partner.mvp.presenter;

/**
 * 厨师收入订单列表 Present接口定义
 */
public interface IIncomeOrderPresent {

    void getList(String date, int page, int pageSize);

}
