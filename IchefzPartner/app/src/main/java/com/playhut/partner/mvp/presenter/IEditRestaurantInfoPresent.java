package com.playhut.partner.mvp.presenter;

/**
 * 修改厨房信息Present接口定义
 */
public interface IEditRestaurantInfoPresent {

    /**
     * changeType
     * 1-kitchen name
     * 2-subtitle
     * 3-type of food
     * 4-kitchen summery
     * 5-ship day
     * 6-about me
     */
    void edit(String content, int changeType);

}
