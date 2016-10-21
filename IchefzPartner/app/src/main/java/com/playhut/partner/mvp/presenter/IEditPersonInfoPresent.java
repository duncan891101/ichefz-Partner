package com.playhut.partner.mvp.presenter;

/**
 * 修改个人信息Present接口定义
 */
public interface IEditPersonInfoPresent {

    /**
     * changeType
     * 1-first name
     * 2-last name
     * 3-gender
     * 4-birthday
     * 5-phone number
     * 6-describe yourself
     */
    void edit(String content, int changeType);

}
