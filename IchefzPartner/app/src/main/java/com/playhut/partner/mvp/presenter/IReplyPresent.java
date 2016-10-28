package com.playhut.partner.mvp.presenter;

/**
 * 回复消息Present接口定义
 */
public interface IReplyPresent {

    void reply(String senderId, String content, String parentId);

}
