package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 删除菜单
 */
public interface DeleteMenuService {

    @FormUrlEncoded
    @POST("menu_delete.html")
    Observable<ResponseBody> delete(@FieldMap Map<String, String> map);

}
