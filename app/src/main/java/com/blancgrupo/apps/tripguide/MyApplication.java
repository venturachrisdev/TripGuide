package com.blancgrupo.apps.tripguide;

import android.app.Application;
import android.util.Log;

import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerNetComponent;
import com.blancgrupo.apps.tripguide.presentation.di.component.NetComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.AppModule;
import com.blancgrupo.apps.tripguide.presentation.di.module.NetModule;
import com.blancgrupo.apps.tripguide.utils.Constants;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by root on 8/15/17.
 */

public class MyApplication extends Application {
    public static String TAG = MyApplication.class.getSimpleName();
    public static String packageName = MyApplication.class.getPackage().getName();
    NetComponent netComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Application created");

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(Constants.FONT_PATH_REGULAR)
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        netComponent = DaggerNetComponent.builder()
                .netModule(new NetModule(Constants.API_BASE_URL_1, Constants.API_BASE_URL_2))
                .appModule(new AppModule(this))
                .build();
    }

    public String getApiKey() {
        return getString(R.string.google_maps_key);
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }
}
