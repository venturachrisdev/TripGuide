package com.blancgrupo.apps.tripguide.data;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by root on 8/15/17.
 */

public class ApiKeyInterceptor implements Interceptor {
    String apiKey;

    public ApiKeyInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        HttpUrl oldUrl = oldRequest.url();

        HttpUrl newUrl = oldUrl.newBuilder()
                .addQueryParameter("key", apiKey)
                .build();

        Request newRequest = oldRequest.newBuilder()
                .url(newUrl)
                .build();

        return chain.proceed(newRequest);
    }
}
