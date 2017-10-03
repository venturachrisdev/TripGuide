package com.blancgrupo.apps.tripguide.presentation.di.component;

import android.app.Application;
import android.content.SharedPreferences;

import com.blancgrupo.apps.tripguide.data.persistence.PlacesDatabase;
import com.blancgrupo.apps.tripguide.presentation.di.module.AppModule;
import com.blancgrupo.apps.tripguide.presentation.di.module.NetModule;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by root on 8/15/17.
 */

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {
    @Named("GoogleRetrofit") Retrofit retrofit1();
    @Named("ApiRetrofit") Retrofit retrofit2();
    Application application();
    SharedPreferences sharedPreferences();
    PlacesDatabase placeDatabase();
};
