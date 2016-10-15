package com.playhut.partner.network;

import java.io.UnsupportedEncodingException;

/**
 * Base64加密
 */
public class Base64 {

    public static String base64Digest(String str) {
        try {
            byte[] encodeBase64 = org.apache.commons.codec.binary.Base64.encodeBase64(str.getBytes("UTF-8"));
            return new String(encodeBase64);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
