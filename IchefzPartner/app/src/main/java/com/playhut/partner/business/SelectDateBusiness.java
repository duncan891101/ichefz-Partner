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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 */
public class SelectDateBusiness {

    private Context mContext;

    private Dialog mDateDialog;

    private List<FinanceDateEntity> mYearList;

    private List<FinanceDateEntity> mMonthList;

    private List<FinanceDateEntity> mDayList;

    final int minYear = 1930;

    private SelectDateOkListener mSelectDateOkListener;

    private boolean mIsShowDayView;

    public SelectDateBusiness(Context context) {
        this.mContext = context;
        mYearList = new ArrayList<>();
        mMonthList = new ArrayList<>();
        mDayList = new ArrayList<>();
    }

    public void showDialog(boolean isShowDayView, SelectDateOkListener selectDateOkListener) {
        this.mIsShowDayView = isShowDayView;
        this.mSelectDateOkListener = selectDateOkListener;
        if (mDateDialog == null || !mDateDialog.isShowing()) {
            mDateDialog = DialogUtils.showBottomDialog(mContext, R.layout.finance_date_dialog_layout, true, false);
        }
        initDialogLayout();
    }

    /**
     * 初始化对话框
     */
    private void initDialogLayout() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        initYearData(year);
        initMonthData(month + 1);
        initDayData(day);

        WheelView yearWv = (WheelView) mDateDialog.findViewById(R.id.wv_year);
        yearWv.setScrollCycle(false);
        final FinanceWheelViewAdapter yearAdapter = new FinanceWheelViewAdapter(mContext, mYearList);
        yearWv.setAdapter(yearAdapter);

        WheelView monthWv = (WheelView) mDateDialog.findViewById(R.id.wv_month);
        monthWv.setScrollCycle(false);
        final FinanceWheelViewAdapter monthAdapter = new FinanceWheelViewAdapter(mContext, mMonthList);
        monthWv.setAdapter(monthAdapter);

        WheelView dayWv = (WheelView) mDateDialog.findViewById(R.id.wv_day);
        dayWv.setVisibility(mIsShowDayView ? View.VISIBLE : View.GONE);
        dayWv.setScrollCycle(false);
        final FinanceWheelViewAdapter dayAdapter = new FinanceWheelViewAdapter(mContext, mDayList);
        dayWv.setAdapter(dayAdapter);

        yearWv.setSelection(year - minYear, true);
        View yearSelectedView = yearWv.getSelectedView();
        ((TextView) yearSelectedView).setTextSize(yearAdapter.getmSelectTextSize());
        ((TextView) yearSelectedView).setTextColor(yearAdapter.getmSelectTextColor());

        monthWv.setSelection(month, true);
        View monthSelectedView = monthWv.getSelectedView();
        ((TextView) monthSelectedView).setTextSize(monthAdapter.getmSelectTextSize());
        ((TextView) monthSelectedView).setTextColor(monthAdapter.getmSelectTextColor());

        dayWv.setSelection(day - 1, true);
        View daySelectedView = dayWv.getSelectedView();
        ((TextView) daySelectedView).setTextSize(dayAdapter.getmSelectTextSize());
        ((TextView) daySelectedView).setTextColor(dayAdapter.getmSelectTextColor());

        // 设置选择监听器
        yearWv.setOnItemSelectedListener(new TosAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {
                setItemSelect(mYearList, position, yearAdapter);
            }

            @Override
            public void onNothingSelected(TosAdapterView<?> parent) {

            }
        });
        monthWv.setOnItemSelectedListener(new TosAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {
                setItemSelect(mMonthList, position, monthAdapter);
            }

            @Override
            public void onNothingSelected(TosAdapterView<?> parent) {

            }
        });
        dayWv.setOnItemSelectedListener(new TosAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {
                setItemSelect(mDayList, position, dayAdapter);
            }

            @Override
            public void onNothingSelected(TosAdapterView<?> parent) {

            }
        });
        // 取消
        TextView cancelTv = (TextView) mDateDialog.findViewById(R.id.tv_cancel);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        // OK
        TextView completeTv = (TextView) mDateDialog.findViewById(R.id.tv_complete);
        completeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                if (mSelectDateOkListener != null) {
                    mSelectDateOkListener.onOk(getSelectString(mYearList), getSelectString(mMonthList), getSelectString(mDayList));
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
        if (mDateDialog != null && mDateDialog.isShowing()) {
            mDateDialog.dismiss();
            mDateDialog = null;
        }
    }

    /**
     * 初始化年数据
     */
    private void initYearData(int currentYear) {
        mYearList.clear();
        for (int i = 0; i <= 120; i++) {
            FinanceDateEntity entity = new FinanceDateEntity();
            entity.setText(String.valueOf(minYear + i));
            if (i == currentYear - minYear) {
                entity.setIsSelect(true);
            } else {
                entity.setIsSelect(false);
            }
            mYearList.add(entity);
        }
    }

    /**
     * 初始化月数据
     */
    private void initMonthData(int currentMonth) {
        mMonthList.clear();
        for (int i = 0; i < 12; i++) {
            FinanceDateEntity entity = new FinanceDateEntity();
            String monthStr = String.valueOf(i + 1);
            if (monthStr.length() == 1) {
                monthStr = "0" + monthStr;
            }
            entity.setText(monthStr);
            if (i == currentMonth - 1) {
                entity.setIsSelect(true);
            } else {
                entity.setIsSelect(false);
            }
            mMonthList.add(entity);
        }
    }

    /**
     * 初始化日数据
     */
    private void initDayData(int currentDay) {
        mDayList.clear();
        for (int i = 0; i < 31; i++) {
            FinanceDateEntity entity = new FinanceDateEntity();
            String dayStr = String.valueOf(i + 1);
            if (dayStr.length() == 1) {
                dayStr = "0" + dayStr;
            }
            entity.setText(dayStr);
            if (i == currentDay - 1) {
                entity.setIsSelect(true);
            } else {
                entity.setIsSelect(false);
            }
            mDayList.add(entity);
        }
    }

    public interface SelectDateOkListener {
        void onOk(String selectYear, String selectMonth, String selectDay);
    }

}
