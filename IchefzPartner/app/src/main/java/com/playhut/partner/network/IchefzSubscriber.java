package com.playhut.partner.network;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.playhut.partner.base.PartnerApplication;
import com.playhut.partner.constants.NetworkConstants;
import com.playhut.partner.debug.MyLog;
import com.playhut.partner.entity.BaseResponseIsList;
import com.playhut.partner.entity.BaseResponseNoList;
import com.playhut.partner.entity.SubscriberResponse;
import com.playhut.partner.utils.PartnerUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * 自定义RXJAVA观察者，处理返回结果和异常信息
 */
public abstract class IchefzSubscriber<K> extends Subscriber<ResponseBody> {

    private Class<K> cls;

    /**
     * @param cls 需要解析成的对象
     */
    public IchefzSubscriber(Class<K> cls) {
        this.cls = cls;
    }

    @Override
    public void onNext(ResponseBody responseBody) {
        try {
            Reader reader = responseBody.charStream();
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null)
                sb.append(line);
            MyLog.i("====response====" + sb.toString());

            JSONObject jsonObject = new JSONObject(sb.toString());
            int code = jsonObject.getInt("code");
            String info = jsonObject.getString("info");
            if (code == NetworkConstants.RESPONSE_SUCCESS_CODE) {
                // 返回200
                String content = jsonObject.get("content").toString();
                if (!TextUtils.isEmpty(content)) {
                    if (this.cls == null) {
                        // 自己不想关注返回的数据时，可传null
                        onSuccess(null);
                    } else {
                        if (content.startsWith("[") && content.endsWith("]")) {
                            // content是一个jsonArray
                            BaseResponseIsList baseResponseIsList = IchefzGson.fromJson(sb.toString(), this.cls, BaseResponseIsList.class);
                            List<K> resultList = baseResponseIsList.getContent();
                            if (resultList != null) {
                                SubscriberResponse<K> subscriberResponse = new SubscriberResponse();
                                subscriberResponse.setResultList(resultList);
                                subscriberResponse.setState(SubscriberResponse.STATE.RESULT_LIST);
                                onSuccess(subscriberResponse);
                            } else {
                                errorException(new IchefzException(NetworkConstants.SERVER_ERROR_CODE));
                            }
                        } else {
                            BaseResponseNoList baseResponseNoList = IchefzGson.fromJson(sb.toString(), this.cls, BaseResponseNoList.class);
                            K result = (K) baseResponseNoList.getContent();
                            if (result != null) {
                                SubscriberResponse<K> subscriberResponse = new SubscriberResponse();
                                subscriberResponse.setResult(result);
                                subscriberResponse.setState(SubscriberResponse.STATE.RESULT);
                                onSuccess(subscriberResponse);
                            } else {
                                errorException(new IchefzException(NetworkConstants.SERVER_ERROR_CODE));
                            }
                        }
                    }
                } else {
                    // "content":null或者"content":""的情况
                    onSuccess(null);
                }
            } else {
                // 返回非200
                errorException(new IchefzException(code, info));
            }
        } catch (JsonSyntaxException exception) {
            errorException(new IchefzException(NetworkConstants.PARSER_ERROR_CODE));
        } catch (JSONException exception) {
            errorException(new IchefzException(NetworkConstants.SERVER_ERROR_CODE));
        } catch (IOException exception) {
            errorException(new IchefzException(NetworkConstants.SERVER_ERROR_CODE));
        }
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            // 非2XX响应码的异常，例如404
            errorException(new IchefzException(NetworkConstants.SERVER_ERROR_CODE));
        } else if (e instanceof IOException) {
            // 连接超时、主机地址找不到等异常
            errorException(new IchefzException(NetworkConstants.TIMEOUT_ERROR_CODE));
        } else if (e instanceof JsonParseException || e instanceof JSONException) {
            // JSON解析异常
            errorException(new IchefzException(NetworkConstants.PARSER_ERROR_CODE));
        }
    }

    @Override
    public void onCompleted() {

    }

    private void errorException(IchefzException exception) {
        if (!PartnerUtils.checkNetwork(PartnerApplication.mApp.getApplicationContext())) {
            // 网络错误
            onError(new IchefzException(NetworkConstants.NETWORK_ERROR_CODE));
        } else {
            onError(exception);
        }
    }

    public abstract void onError(IchefzException exception);

    public abstract void onSuccess(SubscriberResponse<K> subscriberResponse);

}
