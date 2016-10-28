package com.playhut.partner.mvp.presenter;

/**
 * 消息明细Present接口定义
 */
public interface IMessageDetailPresent {

    void getList(String senderId, int page, int pageSize);

}
