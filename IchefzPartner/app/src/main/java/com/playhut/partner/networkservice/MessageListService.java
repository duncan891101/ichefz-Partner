package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 用户消息列表
 */
public interface MessageListService {

    @FormUrlEncoded
    @POST("inbox_user_list.html")
    Observable<ResponseBody> getList(@FieldMap Map<String, String> map);

}
