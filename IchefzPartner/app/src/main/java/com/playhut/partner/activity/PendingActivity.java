package com.playhut.partner.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.adapter.NewOrderAdapter;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.constants.OrderState;
import com.playhut.partner.entity.NewOrderEntity;
import com.playhut.partner.mvp.presenter.IConfirmRefundPresent;
import com.playhut.partner.mvp.presenter.IRejectRefundPresent;
import com.playhut.partner.mvp.presenter.impl.ConfirmRefundPresent;
import com.playhut.partner.mvp.presenter.impl.RejectRefundPresent;
import com.playhut.partner.mvp.view.ConfirmRefundView;
import com.playhut.partner.mvp.view.RejectRefundView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.DialogUtils;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.PartnerTitleBar;

/**
 *
 */
public class PendingActivity extends BaseActivity implements View.OnClickListener {

    private static NewOrderEntity.Order mOrder;

    private TextView mReasonTv;

    private TextView mTotalTv;

    private Button mRefundBtn;

    private Button mRejectBtn;

    private Dialog mConfirmDialog;

    private static NewOrderAdapter mAdapter;

    private Dialog mReplyDialog;

    public static void actionIntent(Context context, NewOrderEntity.Order order, NewOrderAdapter adapter) {
        mOrder = order;
        mAdapter = adapter;
        context.startActivity(new Intent(context, PendingActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_pendding);
        mReasonTv = (TextView) findViewById(R.id.tv_reason);
        mTotalTv = (TextView) findViewById(R.id.tv_total);
        mRefundBtn = (Button) findViewById(R.id.btn_refund);
        mRejectBtn = (Button) findViewById(R.id.btn_reject);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Pending");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                PendingActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(mOrder.refund_reason)) {
            mReasonTv.setText(mOrder.refund_reason);
        }
        mTotalTv.setText(mOrder.subtotal);

        mRefundBtn.setOnClickListener(this);
        mRejectBtn.setOnClickListener(this);
    }

    @Override
    protected void initLogic() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refund:
                showConfirmDialog("Confirm", "Are you sure to refund the order?");
                break;
            case R.id.btn_reject:
                showReplyDialog();
                break;
        }
    }

    private void showReplyDialog() {
        if (mReplyDialog == null || !mReplyDialog.isShowing()) {
            mReplyDialog = DialogUtils.showRelayDialog(this, R.layout.reply_dialog_layout, true, true);
        }

        final EditText replyEt = (EditText) mReplyDialog.findViewById(R.id.et_reply);
        replyEt.setFocusable(true);
        replyEt.requestFocus();

        mReplyDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(replyEt, 0);
            }
        });

        mReplyDialog.show();

        final Button reply = (Button) mReplyDialog.findViewById(R.id.btn_reply);
        reply.setText("Reject");
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissReplyDialog();
                String reason = replyEt.getText().toString().trim();
                if (!TextUtils.isEmpty(reason)) {
                    toRejectRefund(reason);
                } else {
                    ToastUtils.show("Reject reason cannot be empty");
                }
            }
        });
    }

    private void dismissReplyDialog() {
        if (mReplyDialog != null && mReplyDialog.isShowing()) {
            mReplyDialog.dismiss();
            mReplyDialog = null;
        }
    }

    private void showConfirmDialog(String title, String text) {
        if (mConfirmDialog == null || !mConfirmDialog.isShowing()) {
            mConfirmDialog = DialogUtils.showConfirmDialog(this, R.layout.confirm_dialog_layout, true);
        }
        TextView titleTv = (TextView) mConfirmDialog.findViewById(R.id.tv_title);
        titleTv.setText(title);
        TextView textTv = (TextView) mConfirmDialog.findViewById(R.id.tv_text);
        textTv.setText(text);
        TextView confirmTv = (TextView) mConfirmDialog.findViewById(R.id.tv_confirm);
        confirmTv.setText(title);
        mConfirmDialog.findViewById(R.id.rl_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissConfirmDialog();
            }
        });
        mConfirmDialog.findViewById(R.id.rl_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissConfirmDialog();
                toConfirmRefund();
            }
        });
    }

    private void dismissConfirmDialog() {
        if (mConfirmDialog != null && mConfirmDialog.isShowing()) {
            mConfirmDialog.dismiss();
            mConfirmDialog = null;
        }
    }

    private void toConfirmRefund() {
        IConfirmRefundPresent present = new ConfirmRefundPresent(this, new ConfirmRefundView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_confirm), false);
            }

            @Override
            public void loadSuccess(String info) {
                // 进入后台审核状态
                mOrder.order_state = OrderState.APPLY_REFUND;
                mOrder.chef_handle = 0;
                mAdapter.notifyDataSetChanged();
                ToastUtils.show(info);
                PendingActivity.this.finish();
            }

            @Override
            public void finishLoading() {
                dismissLoadingDialog();
            }

            @Override
            public void loadFailure(IchefzException exception) {
                ToastUtils.show(exception.getErrorMsg());
            }
        });
        present.refund(mOrder.order_id);
    }

    private void toRejectRefund(String reason) {
        IRejectRefundPresent present = new RejectRefundPresent(this, new RejectRefundView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_loading), false);
            }

            @Override
            public void loadSuccess(String info) {
                mOrder.order_state = OrderState.REFUND_REJECT;
                mAdapter.notifyDataSetChanged();
                ToastUtils.show(info);
                PendingActivity.this.finish();
            }

            @Override
            public void finishLoading() {
                dismissLoadingDialog();
            }

            @Override
            public void loadFailure(IchefzException exception) {
                ToastUtils.show(exception.getErrorMsg());
            }
        });
        present.reject(mOrder.order_id, reason);
    }

}
