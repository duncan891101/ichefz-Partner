package com.playhut.partner.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.playhut.partner.R;
import com.playhut.partner.adapter.MyMenuSetAdapter;
import com.playhut.partner.constants.PackState;
import com.playhut.partner.debug.MyLog;
import com.playhut.partner.entity.MyMenuSetEntity;
import com.playhut.partner.pullrefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MyMenuSetFragment extends Fragment {

    private Context mContext;

    private View mRootView;

    private boolean mIsPrepared = false;

    private boolean mIsFragmentVisible = false;

    private PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;

    private MyMenuSetAdapter mAdapter;

    private List<MyMenuSetEntity.SetInfo> mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null) {
            /*因为公用一个Fragment，所以必须先判断该View是否已有父View*/
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        } else {
            mRootView = inflater.inflate(R.layout.fragment_my_menu, null);
            mIsPrepared = true;
            initView();
            loadData();
        }
        return mRootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsFragmentVisible = isVisibleToUser;
        if (isVisibleToUser) {
            loadData();
        }
    }

    private void initView() {
        mPullToRefreshListView = (PullToRefreshListView) mRootView.findViewById(R.id.lv_pull_refresh);
        mListView = mPullToRefreshListView.getRefreshableView();

        mListView.setDividerHeight(0);
        mList = new ArrayList<>();
        mAdapter = new MyMenuSetAdapter(mContext, mList);
        mListView.setAdapter(mAdapter);
    }

    private void loadData() {
        if (!mIsPrepared || !mIsFragmentVisible) {
            return;
        }

        mList.clear();

        for (int i = 0; i < 8; i++) {
            MyMenuSetEntity.SetInfo setInfo = new MyMenuSetEntity.SetInfo();
            setInfo.set_title = "Shrimp meat with mushroom";
            setInfo.person2 = "8.00";
            setInfo.person4 = "13.88";
            if (i ==0){
                setInfo.set_state = PackState.OPEN_STATE;
            } else if (i == 1){
                setInfo.set_state = PackState.CLOSE_STATE;
            } else if (i == 2){
                setInfo.set_state = PackState.AUDIT_ING_STATE;
            } else if (i == 3){
                setInfo.set_state = PackState.NOT_AUDIT_STATE;
            } else {
                setInfo.set_state = PackState.AUDIT_FAIL_STATE;
            }

            List<MyMenuSetEntity.PackInfo> list = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                MyMenuSetEntity.PackInfo packInfo = new MyMenuSetEntity.PackInfo();
                packInfo.pack_img = "drawable://" + R.mipmap.test1;
                list.add(packInfo);
            }

            setInfo.sets = list;

            mList.add(setInfo);
        }

        mAdapter.notifyDataSetChanged();

    }

}
