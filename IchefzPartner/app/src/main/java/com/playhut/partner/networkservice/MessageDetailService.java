package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 消息明细
 */
public interface MessageDetailService {

    @FormUrlEncoded
    @POST("inbox_user_message_list.html")
    Observable<ResponseBody> getList(@FieldMap Map<String, String> map);

}
