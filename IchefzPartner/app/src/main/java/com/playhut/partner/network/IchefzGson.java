package com.playhut.partner.network;

import com.google.gson.Gson;
import com.playhut.partner.entity.BaseResponse;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 通过type的形式解析JSON
 */
public class IchefzGson {

    /**
     *
     * @param json
     * @param clazz 需要解析的对象
     * @param baseResponseCls BaseResponseIsList或者BaseResponseNoList
     * @param <T>
     * @return
     */
    public static <T extends BaseResponse> T fromJson(String json, Class clazz, Class<T> baseResponseCls) {
        Gson gson = new Gson();
        Type objectType = type(baseResponseCls, clazz);
        return gson.fromJson(json, objectType);
    }

    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

}
