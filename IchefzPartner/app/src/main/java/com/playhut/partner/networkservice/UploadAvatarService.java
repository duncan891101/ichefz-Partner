package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * 上传头像
 */
public interface UploadAvatarService {

    @Multipart
    @POST("profile_photo_upload.html")
    Observable<ResponseBody> upload(@PartMap Map<String, RequestBody> map);

}
