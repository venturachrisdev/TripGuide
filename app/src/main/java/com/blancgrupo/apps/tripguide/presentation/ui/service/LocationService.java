package com.blancgrupo.apps.tripguide.presentation.ui.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.arch.lifecycle.LifecycleService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.ApiCityRepository;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerNetComponent;
import com.blancgrupo.apps.tripguide.presentation.di.component.NetComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.di.module.AppModule;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.CityDetailActivity;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.blancgrupo.apps.tripguide.utils.LocationUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LocationService extends LifecycleService
        implements LocationListener {
    @Inject
    ApiCityRepository apiCityRepository;
    @Inject
    SharedPreferences sharedPreferences;
    Disposable disposable;
    boolean listening = false;
    public LocationService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startListening();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startListening() {
        if (!listening) {
            LocationUtils.requestLocationUpdates(this, this);
            listening = true;
        }
    }

    private void stopListening() {
        if (listening) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
            listening = false;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        disposable = apiCityRepository.getCurrentCity(String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude()))
                .debounce(15, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        String currentCity = sharedPreferences.getString(Constants.CURRENT_LOCATION_SP, null);
                        if (currentCity != null) {
                            if (!currentCity.equals(s)) {
                                NotificationCompat.Builder builder =
                                        new NotificationCompat.Builder(LocationService.this)
                                        .setSmallIcon(R.drawable.ic_place_black_24dp)
                                        .setContentTitle("You dumbass are in another country!")
                                        .setContentText("Explore the tours there");

                                NotificationManager notificationManager = (NotificationManager)
                                        getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(0, builder.build());
                            }
                        }
                    }
                });
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        startListening();
    }

    @Override
    public void onProviderDisabled(String s) {
        stopListening();
    }
}
