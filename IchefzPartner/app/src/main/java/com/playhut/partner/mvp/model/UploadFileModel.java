package com.playhut.partner.mvp.model;

import com.playhut.partner.network.ApiManager;
import com.playhut.partner.network.FieldMapManager;
import com.playhut.partner.networkservice.UploadAvatarService;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * 上传文件Model
 */
public class UploadFileModel {

    /**
     * 上传头像
     */
    public Observable<ResponseBody> uploadAvatar(File file) {
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(null);

        Map<String, RequestBody> paramsMap = new TreeMap<>();
        Set<String> set = fieldMap.keySet();
        for (String key : set) {
            String value = fieldMap.get(key);
            paramsMap.put(key, RequestBody.create(MediaType.parse("text/plain"), value));
        }
        paramsMap.put("avatar\"; filename=\"" + file.getName() + "\"",
                RequestBody.create(MediaType.parse("multipart/form-data"), file));

        UploadAvatarService uploadAvatarService = ApiManager.create(UploadAvatarService.class);
        return uploadAvatarService.upload(paramsMap);
    }

}
