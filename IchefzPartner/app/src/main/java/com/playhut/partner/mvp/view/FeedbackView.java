package com.playhut.partner.mvp.view;

import com.playhut.partner.entity.FeedbackEntity;

/**
 * 评论列表View定义
 */
public abstract class FeedbackView extends BaseView{
    public abstract void loadSuccess(FeedbackEntity entity);
}
