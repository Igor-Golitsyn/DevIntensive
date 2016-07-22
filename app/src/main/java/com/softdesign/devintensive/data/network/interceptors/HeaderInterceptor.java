package com.softdesign.devintensive.data.network.interceptors;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.managers.PreferenceManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Игорь on 12.07.2016.
 */
public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        PreferenceManager preferenceManager= DataManager.getInstance().getPreferenceManager();
        Request original=chain.request();
        Request.Builder requestBuilder=original.newBuilder()
                .header("X-Access-Token",preferenceManager.getAuthToken())
                .header("Request-User-Id",preferenceManager.getUserId())
                .header("User-Agent","DevIntensiveApp")
                .header("Cache-Control","max-age="+(60*60*24));
        Request request=requestBuilder.build();
        return chain.proceed(request);
    }
}
