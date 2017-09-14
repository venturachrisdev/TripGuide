package com.blancgrupo.apps.tripguide.presentation.ui.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.LifecycleService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceTypesCover;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.RunningTourActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.SingleTourActivity;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.blancgrupo.apps.tripguide.utils.LocationUtils;

public class LocationService extends LifecycleService
        implements LocationListener {
    boolean listening = false;
    NotificationCompat.Builder builder;
    double startDistance;
    double realDistance;
    PlaceTypesCover cover;
    int position;
    public LocationService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startListening();
        Bundle data = intent.getExtras();
        cover = data.getParcelable(Constants.EXTRA_PLACE_ID);
        startDistance = data.getDouble(Constants.EXTRA_CURRENT_POSITION);
        position = data.getInt(Constants.EXTRA_CURRENT_IMAGE_POSITION);
        realDistance = startDistance;
        // Se construye la notificación
        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_place_white_24dp)
                .setContentTitle("Running to " + cover.getName())
                .setContentText("Distance: " + LocationUtils.prettifyDistance(realDistance) );

        // Crear Intent para iniciar una actividad al presionar la notificación
        Intent notificationIntent = new Intent(this, SingleTourActivity.class);
        notificationIntent.putExtra(Constants.EXTRA_IS_TOUR_RUNNING, true);
        notificationIntent.putExtra(Constants.EXTRA_CURRENT_POSITION, realDistance);
        notificationIntent.putExtra(Constants.EXTRA_CURRENT_IMAGE_POSITION, position);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);
        calculateProgress();

        // Poner en primer plano
        startForeground(1, builder.build());
        return super.onStartCommand(intent, flags, startId);
    }

    void calculateProgress() {
        builder.setContentText("Distance: " + LocationUtils.prettifyDistance(realDistance) );
        int percent = (int) (100 / (startDistance / realDistance));
        int progress = 100 - percent;
        if (progress >= 0) {
            builder.setProgress(100, progress, false);
            if (progress >= 95 || (realDistance != 0 && realDistance < 150)) {
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setContentTitle(getString(R.string.we_arrived_to) + " " + cover.getName());
                builder.setContentText(getString(R.string.we_dit_it));
                builder.setProgress(0, 0, false);
            }
        } else {
            builder.setProgress(100, 0, false);
        }
    }

    private void startListening() {
        if (!listening) {
            LocationUtils.requestLocationUpdates(this, this);
            listening = true;
        }
    }

    private void stopListening() {
        listening = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        // Crear Intent para iniciar una actividad al presionar la notificación
        Intent notificationIntent = new Intent(this, SingleTourActivity.class);
        notificationIntent.putExtra(Constants.EXTRA_IS_TOUR_RUNNING, true);
        notificationIntent.putExtra(Constants.EXTRA_CURRENT_POSITION, realDistance);
        notificationIntent.putExtra(Constants.EXTRA_CURRENT_IMAGE_POSITION, position);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, notificationIntent, 0);
        realDistance = LocationUtils.measureDoubleDistance(this, location,
                cover.getLocation().getLat(), cover.getLocation().getLng());
        calculateProgress();
        builder.setContentIntent(pendingIntent);
        // Poner en primer plano
        startForeground(1, builder.build());
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
