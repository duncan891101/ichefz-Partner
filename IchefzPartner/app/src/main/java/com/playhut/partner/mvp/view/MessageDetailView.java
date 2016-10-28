package com.playhut.partner.mvp.view;

import com.playhut.partner.entity.MessageDetailEntity;

/**
 * 消息明细View定义
 */
public abstract class MessageDetailView extends BaseView{
    public abstract void loadSuccess(MessageDetailEntity entity);
}
