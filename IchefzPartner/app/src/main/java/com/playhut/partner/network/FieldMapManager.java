package com.playhut.partner.network;

import com.playhut.partner.base.PartnerApplication;
import com.playhut.partner.constants.NetworkConstants;
import com.playhut.partner.debug.MyLog;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 通过该类可以获取网络请求传递的参数
 */
public class FieldMapManager {

    /**
     * 获取包含所有(公共和特定)请求参数Map
     *
     * @param privateMap，如果该接口没有特定参数，则传null
     * @return
     */
    public static Map<String, String> getFieldMap(Map<String, String> privateMap) {
        Map<String, String> fieldMap = new TreeMap<>();
        if (privateMap != null) {
            Set<String> keys = privateMap.keySet();
            for (String key : keys) {
                String value = privateMap.get(key);
                fieldMap.put(key, value);
            }
        }
        putPublicParamsToFieldMap(fieldMap);
        putSignParamsToFieldMap(fieldMap);
        logFieldMap(fieldMap);

        return fieldMap;
    }

    /**
     * 公共参数加入到请求参数中
     *
     * @param fieldMap
     * @return
     */
    private static void putPublicParamsToFieldMap(Map<String, String> fieldMap) {
        fieldMap.put(NetworkConstants.API_VERSION_NAME, NetworkConstants.API_VERSION_VALUE);
        fieldMap.put(NetworkConstants.TIME_STAMP_NAME, String.valueOf(System.currentTimeMillis()));
        fieldMap.put(NetworkConstants.CLIENT_TYPE_NAME, NetworkConstants.CLIENT_TYPE_VALUE);
        if (PartnerApplication.mAccount != null && PartnerApplication.mAccount.isAccountValid()) {
            // 用户已登录，已登录用户则需要传递Token
            fieldMap.put(NetworkConstants.API_TOKEN_NAME, PartnerApplication.mAccount.getApi_token());
            fieldMap.put("chef_id", PartnerApplication.mAccount.getChef_id());
        }
    }

    /**
     * 生成签名信息，加入到请求参数中
     *
     * @param fieldMap
     */
    private static void putSignParamsToFieldMap(Map<String, String> fieldMap) {
        String requestContentStr = SignRequestContent.getRequestContent(fieldMap);
        String md5Sign = Md5.md5Digest(NetworkConstants.API_KEY + requestContentStr);
        fieldMap.put(NetworkConstants.API_SIGN_NAME, md5Sign.toUpperCase());
    }

    /**
     * 打印请求参数
     *
     * @param fieldMap
     */
    private static void logFieldMap(Map<String, String> fieldMap) {
        StringBuilder sb = new StringBuilder("====request====");
        Set<String> keys = fieldMap.keySet();
        for (String key : keys) {
            sb.append(key + ":" + fieldMap.get(key) + "...");
        }
        MyLog.i(sb.toString());
    }

}
