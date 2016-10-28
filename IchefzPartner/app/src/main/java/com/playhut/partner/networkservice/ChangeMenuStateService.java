package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 修改菜的状态
 */
public interface ChangeMenuStateService {

    @FormUrlEncoded
    @POST("menu_show.html")
    Observable<ResponseBody> change(@FieldMap Map<String, String> map);

}
