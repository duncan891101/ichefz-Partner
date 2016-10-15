package com.playhut.partner.network;

import android.content.Intent;
import android.text.TextUtils;

import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.base.PartnerApplication;
import com.playhut.partner.constants.NetworkConstants;

/**
 * 自定义网络Exception
 */
public class IchefzException extends Exception {

    private int mErrorCode; // 错误码

    private String mErrorMsg; // 错误信息

    public int getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(int errorCode) {
        this.mErrorCode = errorCode;
    }

    public String getErrorMsg() {
        return mErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.mErrorMsg = errorMsg;
    }

    public IchefzException(int errorCode) {
        this.mErrorCode = errorCode;
        this.mErrorMsg = getErrorMsgFromLocalCode(errorCode);
    }

    public IchefzException(int errorCode, String errorMsg) {
        this.mErrorCode = errorCode;
        if (!TextUtils.isEmpty(errorMsg)) {
            this.mErrorMsg = errorMsg;
        } else {
            this.mErrorMsg = NetworkConstants.SERVER_ERROR_MSG;
        }
        if (mErrorCode == 202) {
            // 登录过期
            Intent intent = new Intent(BaseActivity.LOGIN_OUT_OF_DATE_ACTION);
            PartnerApplication.mApp.sendBroadcast(intent);
        }
    }

    /**
     * 根据本地错误码获取错误信息
     *
     * @param errorCode
     * @return
     */
    private String getErrorMsgFromLocalCode(int errorCode) {
        String msg;
        switch (errorCode) {
            case NetworkConstants.NETWORK_ERROR_CODE:
                msg = NetworkConstants.NETWORK_ERROR_MSG;
                break;
            case NetworkConstants.TIMEOUT_ERROR_CODE:
                msg = NetworkConstants.TIMEOUT_ERROR_MSG;
                break;
            case NetworkConstants.PARSER_ERROR_CODE:
                msg = NetworkConstants.PARSER_ERROR_MSG;
                break;
            case NetworkConstants.SERVER_ERROR_CODE:
                msg = NetworkConstants.SERVER_ERROR_MSG;
                break;
            case NetworkConstants.UNKNOWN_ERROR_CODE:
            default:
                msg = NetworkConstants.UNKNOWN_ERROR_MSG;
                break;
        }
        return msg;
    }


}
