package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 厨师收入
 */
public interface IncomeService {

    @FormUrlEncoded
    @POST("income.html")
    Observable<ResponseBody> getIncome(@FieldMap Map<String, String> map);

}
