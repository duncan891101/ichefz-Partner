package com.playhut.partner.utils;

import com.playhut.partner.network.Base64;

/**
 * 密码加密处理
 */
public class PwdUtils {

    /**
     * 获取加密的密码
     *
     * @param pwd
     * @return
     */
    public static String getEncryptPwd(String pwd) {
        String result;
        String base64Pwd = Base64.base64Digest(pwd);
        if (base64Pwd.length() >= 10) {
            result = base64Pwd.substring(0, 9) + "I" + base64Pwd.subSequence(9, base64Pwd.length());
        } else {
            result = base64Pwd.substring(0, 2) + "I" + base64Pwd.subSequence(2, base64Pwd.length());
        }
        return result;
    }

}

