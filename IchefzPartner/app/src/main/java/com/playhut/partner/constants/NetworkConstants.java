package com.playhut.partner.constants;

/**
 * 网络常量
 */
public class NetworkConstants {

    public static final String MAIN_SERVER_URL = "https://www.ichefz.com/Apis/"; // 主机地址

    public static final int NET_CONNECT_TIMEOUT = 20; // 网络连接超时时间，单位秒

    public static final int NET_READ_TIMEOUT = 80; // 网络读取超时时间，单位秒

    public static final int RESPONSE_SUCCESS_CODE = 200; // 返回的JSON对象中，成功的状态码

    public static final int NETWORK_ERROR_CODE = -50005; // 无网络状态

    public static final String NETWORK_ERROR_MSG = "Network error, please try again later";

    public static final int SERVER_ERROR_CODE = -50006; // 服务器无法连接

    public static final String SERVER_ERROR_MSG = "Server error, please try again later";

    public static final int TIMEOUT_ERROR_CODE = -50007; // 超时

    public static final String TIMEOUT_ERROR_MSG = "Server connection timed out, please try again later";

    public static final int PARSER_ERROR_CODE = -50008; // 解析错误

    public static final String PARSER_ERROR_MSG = "Server exceptions, data parsing errors";

    public static final int UNKNOWN_ERROR_CODE = -50009; // 未知错误

    public static final String UNKNOWN_ERROR_MSG = "Service exceptions, there is an unknown error";

    public static final String API_VERSION_NAME = "api_verson";

    public static final String API_VERSION_VALUE = "2.0"; // 目前接口版本号

    public static final String TIME_STAMP_NAME = "api_timstamp";

    public static final String CLIENT_TYPE_NAME = "api_detect";

    public static final String CLIENT_TYPE_VALUE = "android"; // 客户端类型

    public static final String API_TOKEN_NAME = "api_token";

    public static final String API_SIGN_NAME = "api_sign";

    public static final String API_KEY = "a98d668b26b6a1f0022774fab313b186";

}
