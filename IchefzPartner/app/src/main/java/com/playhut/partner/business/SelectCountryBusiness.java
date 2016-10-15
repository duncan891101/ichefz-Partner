package com.playhut.partner.business;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.adapter.FinanceWheelViewAdapter;
import com.playhut.partner.entity.FinanceDateEntity;
import com.playhut.partner.utils.DialogUtils;
import com.playhut.partner.widget.TosAdapterView;
import com.playhut.partner.widget.WheelView;

import java.util.List;

/**
 *
 */
public class SelectCountryBusiness {

    private Context mContext;

    private List<FinanceDateEntity> mCountryList;

    private Dialog mCountryDialog;

    private SelectCountryOkListener mSelectCountryOkListener;

    public SelectCountryBusiness(Context context) {
        this.mContext = context;
    }

    public void showDialog(List<FinanceDateEntity> countryList, SelectCountryOkListener selectCountryOkListener) {
        this.mCountryList = countryList;
        this.mSelectCountryOkListener = selectCountryOkListener;
        if (mCountryList != null && mCountryList.size() > 0) {
            if (mCountryDialog == null || !mCountryDialog.isShowing()) {
                mCountryDialog = DialogUtils.showBottomDialog(mContext, R.layout.finance_date_dialog_layout, true, false);
            }
            initDialogLayout();
        }
    }

    /**
     * 初始化对话框
     */
    private void initDialogLayout() {

        WheelView monthWv = (WheelView) mCountryDialog.findViewById(R.id.wv_month);
        monthWv.setVisibility(View.GONE);
        WheelView dayWv = (WheelView) mCountryDialog.findViewById(R.id.wv_day);
        dayWv.setVisibility(View.GONE);

        WheelView countryWv = (WheelView) mCountryDialog.findViewById(R.id.wv_year);
        countryWv.setScrollCycle(false);
        final FinanceWheelViewAdapter countryAdapter = new FinanceWheelViewAdapter(mContext, mCountryList);
        countryWv.setAdapter(countryAdapter);

        countryWv.setSelection(0, true);
        View countrySelectedView = countryWv.getSelectedView();
        ((TextView) countrySelectedView).setTextSize(countryAdapter.getmSelectTextSize());
        ((TextView) countrySelectedView).setTextColor(countryAdapter.getmSelectTextColor());

        // 设置选择监听器
        countryWv.setOnItemSelectedListener(new TosAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {
                setItemSelect(mCountryList, position, countryAdapter);
            }

            @Override
            public void onNothingSelected(TosAdapterView<?> parent) {

            }
        });
        // 取消
        TextView cancelTv = (TextView) mCountryDialog.findViewById(R.id.tv_cancel);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        // OK
        TextView completeTv = (TextView) mCountryDialog.findViewById(R.id.tv_complete);
        completeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                if (mSelectCountryOkListener != null) {
                    mSelectCountryOkListener.onOk(getSelectString(mCountryList));
                }
            }
        });
    }

    private void setItemSelect(List<FinanceDateEntity> list, int position, FinanceWheelViewAdapter adapter) {
        for (int i = 0; i < list.size(); i++) {
            FinanceDateEntity entity = list.get(i);
            if (i == position) {
                entity.setIsSelect(true);
            } else {
                entity.setIsSelect(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private String getSelectString(List<FinanceDateEntity> list) {
        String result = "";
        for (FinanceDateEntity entity : list) {
            if (entity.isSelect()) {
                result = entity.getText();
                break;
            }
        }
        return result;
    }

    public void dismissDialog() {
        if (mCountryDialog != null && mCountryDialog.isShowing()) {
            mCountryDialog.dismiss();
            mCountryDialog = null;
        }
    }

    public interface SelectCountryOkListener {
        void onOk(String selectCountry);
    }

}
