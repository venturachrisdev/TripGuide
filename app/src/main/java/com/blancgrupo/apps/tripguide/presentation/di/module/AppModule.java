package com.blancgrupo.apps.tripguide.presentation.di.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.blancgrupo.apps.tripguide.data.persistence.PlacesDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by root on 8/15/17.
 */

@Module
public class AppModule {
    Application app;
    public AppModule(Application app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return this.app;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application app) {
        return PreferenceManager.getDefaultSharedPreferences(app);
    }

    @Provides
    @Singleton
    PlacesDatabase providesPlaceDatabase(Application app) {
        return PlacesDatabase.getInstance(app);
    }
}