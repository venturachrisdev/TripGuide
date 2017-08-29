package com.blancgrupo.apps.tripguide.presentation.di.module;

import android.app.Application;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.ApiKeyInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by root on 8/15/17.
 */

@Module
public class NetModule {
    String baseUrl1;
    String baseUrl2;

    public NetModule(String baseUrl1, String baseUrl2) {
        this.baseUrl1 = baseUrl1;
        this.baseUrl2 = baseUrl2;
    }

    @Provides
    @Singleton
    Cache providesCache(Application app) {
        int cache_size = 10 * 1024 * 1024;
        return new Cache(app.getCacheDir(), cache_size);
    }

    @Provides
    @Singleton
    @Named("ApiKeyInterceptor")
    Interceptor providesApiKeyInterceptor(Application app) {
        return new ApiKeyInterceptor(((MyApplication) app).getApiKey());
    }

    @Provides
    @Singleton
    @Named("LoggingInterceptor")
    Interceptor providesLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    @Singleton
    Gson providesGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(
                        FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Provides
    @Singleton
    OkHttpClient providesClient(Cache cache,
                                @Named("ApiKeyInterceptor") Interceptor apiKeyInterceptor,
                                @Named("LoggingInterceptor") Interceptor loggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(apiKeyInterceptor)
                .addInterceptor(loggingInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .cache(cache);
        return builder.build();

    }

    @Provides
    @Singleton
    @Named("GoogleRetrofit")
    Retrofit providesGoogleRetrofit(Gson gson, OkHttpClient client) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .baseUrl(baseUrl1);

        return builder.build();
    }

    @Provides
    @Singleton
    @Named("ApiRetrofit")
    Retrofit providesApiRetrofit(Gson gson, OkHttpClient client) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .baseUrl(baseUrl2);

        return builder.build();
    }
}

