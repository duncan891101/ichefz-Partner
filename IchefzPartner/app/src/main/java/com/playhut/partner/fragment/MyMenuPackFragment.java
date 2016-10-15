package com.playhut.partner.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.playhut.partner.R;
import com.playhut.partner.adapter.MyMenuPackAdapter;
import com.playhut.partner.adapter.MyMenuSetAdapter;
import com.playhut.partner.constants.PackState;
import com.playhut.partner.debug.MyLog;
import com.playhut.partner.entity.MyMenuPackEntity;
import com.playhut.partner.entity.MyMenuSetEntity;
import com.playhut.partner.pullrefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MyMenuPackFragment extends Fragment {

    private Context mContext;

    private View mRootView;

    private boolean mIsPrepared = false;

    private boolean mIsFragmentVisible = false;

    private PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;

    private List<MyMenuPackEntity.PackInfo> mList;

    private MyMenuPackAdapter mAdapter;

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
        mAdapter = new MyMenuPackAdapter(mContext, mList);
        mListView.setAdapter(mAdapter);
    }

    private void loadData() {
        if (!mIsPrepared || !mIsFragmentVisible) {
            return;
        }

        mList.clear();

        for (int i = 0; i < 5; i++) {
            MyMenuPackEntity.PackInfo packInfo = new MyMenuPackEntity.PackInfo();
            packInfo.pack_title = "Pack meat with mushroom";
            packInfo.person2 = "22.00";
            packInfo.person4 = "32.00";
            if (i ==0){
                packInfo.pack_state = PackState.AUDIT_FAIL_STATE;
            } else if (i == 1){
                packInfo.pack_state = PackState.CLOSE_STATE;
            } else if (i == 2){
                packInfo.pack_state = PackState.AUDIT_ING_STATE;
            } else if (i == 3){
                packInfo.pack_state = PackState.NOT_AUDIT_STATE;
            } else {
                packInfo.pack_state = PackState.OPEN_STATE;
            }

            packInfo.pack_img = "drawable://" + R.mipmap.test1;

            mList.add(packInfo);
        }
        mAdapter.notifyDataSetChanged();
    }

}
