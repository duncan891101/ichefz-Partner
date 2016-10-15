package com.playhut.partner.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment的基类
 */
public abstract class BaseFragment extends Fragment {

    protected Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View rootView = initView(inflater);
        initData(rootView);
        initLogic(rootView);
        return rootView;
    }

    /**
     * 初始化视图
     *
     * @param context
     * @param inflater
     * @return
     */
    protected abstract View initView(LayoutInflater inflater);

    /**
     * 设置监听器，初始化数据
     *
     * @param view
     */
    protected abstract void initData(View view);

    /**
     * 业务逻辑处理
     *
     * @param view
     */
    protected abstract void initLogic(View view);

}
