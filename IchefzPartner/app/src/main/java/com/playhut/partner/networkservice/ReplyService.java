package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 消息回复
 */
public interface ReplyService {

    @FormUrlEncoded
    @POST("inbox_reply.html")
    Observable<ResponseBody> reply(@FieldMap Map<String, String> map);

}
