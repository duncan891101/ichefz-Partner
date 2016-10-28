package com.playhut.partner.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.eventbus.RefundOrderEB;
import com.playhut.partner.mvp.presenter.IRefundOrderPresent;
import com.playhut.partner.mvp.presenter.impl.RefundOrderPresent;
import com.playhut.partner.mvp.view.RefundOrderView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.utils.ToastUtils;
import com.playhut.partner.widget.PartnerTitleBar;

import de.greenrobot.event.EventBus;

/**
 *
 */
public class RefundActivity extends BaseActivity implements View.OnClickListener{

    private EditText mRefundEt;

    private Button mSubmitBtn;

    private static int mPosition;

    private static String mOrderId;

    public static void actionIntent(Context context, String orderId, int position){
        mOrderId = orderId;
        mPosition = position;
        context.startActivity(new Intent(context, RefundActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_refund);
        mRefundEt = (EditText) findViewById(R.id.et_refund_reason);
        mSubmitBtn = (Button) findViewById(R.id.btn_submit);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Refund");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                RefundActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        mSubmitBtn.setOnClickListener(this);
    }

    @Override
    protected void initLogic() {

    }

    @Override
    public void onClick(View v) {
        String reason = mRefundEt.getText().toString().trim();
        if (TextUtils.isEmpty(reason)){
            ToastUtils.show("Enter the reason of refund");
            return;
        }
        toRefundOrder(reason);
    }

    private void toRefundOrder(final String reason) {
        IRefundOrderPresent present = new RefundOrderPresent(this, new RefundOrderView() {
            @Override
            public void startLoading() {
                showLoadingDialog(getString(R.string.loading_dialog_refund), false);
            }

            @Override
            public void loadSuccess(String info) {
                ToastUtils.show(info);
                RefundActivity.this.finish();
                RefundOrderEB refundOrderEB = new RefundOrderEB();
                refundOrderEB.position = mPosition;
                EventBus.getDefault().post(refundOrderEB);
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
        present.refund(mOrderId, reason);
    }

}
