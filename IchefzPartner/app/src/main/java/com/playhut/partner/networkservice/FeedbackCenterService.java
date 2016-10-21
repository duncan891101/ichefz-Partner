package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 意见反馈中心
 */
public interface FeedbackCenterService {

    @FormUrlEncoded
    @POST("feedback.html")
    Observable<ResponseBody> send(@FieldMap Map<String, String> map);

}
