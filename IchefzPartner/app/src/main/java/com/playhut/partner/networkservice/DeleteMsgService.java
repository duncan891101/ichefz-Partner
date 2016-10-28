package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 删除消息
 */
public interface DeleteMsgService {

    @FormUrlEncoded
    @POST("inbox_delete.html")
    Observable<ResponseBody> delete(@FieldMap Map<String, String> map);

}
