package com.playhut.partner.mvp.view;

import com.playhut.partner.entity.MessageListEntity;

/**
 * 用户消息列表View定义
 */
public abstract class MessageListView extends BaseView{
    public abstract void loadSuccess(MessageListEntity entity);
}
