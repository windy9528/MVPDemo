package bwie.com.weidumall.util;

import android.content.Context;

import java.io.IOException;

import bwie.com.weidumall.common.App;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author:kson
 * E-mail:19655910@qq.com
 * Time:2018/09/05
 * Description:自定义头部拦截器（应用拦截器）
 */
public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();//原始请求对象
        Request.Builder builder = request.newBuilder();
        builder.addHeader("userId", String.valueOf(App
                .getUserInfo().getInt("userId", 0)));
        builder.addHeader("sessionId", App
                .getUserInfo().getString("sessionId", ""));
        /*
        builder.addHeader("ak", SpUtils.getString("0110010010000"));
        builder.addHeader("cache-control","max-age: 1000");
        builder.addHeader("versionCode", SpUtils.getString("sessionId"));
        builder.addHeader("deviceId", SpUtils.getString("sessionId"));
        builder.addHeader("appKey", SpUtils.getString("sessionId"));
        */
        return chain.proceed(builder.build());
    }
}
