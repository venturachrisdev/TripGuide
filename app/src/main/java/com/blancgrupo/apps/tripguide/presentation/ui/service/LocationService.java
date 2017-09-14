package com.blancgrupo.apps.tripguide.presentation.ui.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceTypesCover;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.SingleTourActivity;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.blancgrupo.apps.tripguide.utils.LocationUtils;

public class LocationService extends IntentService
        implements LocationListener {
    boolean listening = false;
    NotificationCompat.Builder builder;
    double startDistance;
    double realDistance;
    PlaceTypesCover cover;
    int position;
    String tourId;
    int progress = 0;

    public LocationService() {
        super("LocationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Toast.makeText(this, "From goddam service", Toast.LENGTH_SHORT).show();
        startListening();
        Bundle data = intent.getExtras();
        cover = data.getParcelable(Constants.EXTRA_PLACE_ID);
        startDistance = data.getDouble(Constants.EXTRA_START_POSITION);
        position = data.getInt(Constants.EXTRA_CURRENT_IMAGE_POSITION);
        tourId = data.getString(Constants.EXTRA_SINGLE_TOUR_ID);
        progress = data.getInt(Constants.EXTRA_PROGRESS, 0);
        realDistance = calcuteDistance(LocationUtils.getCurrentLocation(this), cover.getLocation());

        // Se construye la notificación
        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_place_white_24dp)
                .setContentTitle("Running to " + cover.getName())
                .setContentText("Distance: " + LocationUtils.prettifyDistance(realDistance) );

        // Crear Intent para iniciar una actividad al presionar la notificación
        Intent notificationIntent = new Intent(this, SingleTourActivity.class);
        notificationIntent.putExtra(Constants.EXTRA_SINGLE_TOUR_ID, tourId);
        notificationIntent.putExtra(Constants.EXTRA_IS_TOUR_RUNNING, true);
        notificationIntent.putExtra(Constants.EXTRA_CURRENT_POSITION, realDistance);
        notificationIntent.putExtra(Constants.EXTRA_CURRENT_IMAGE_POSITION, position);
        notificationIntent.putExtra(Constants.EXTRA_PROGRESS, progress);
        notificationIntent.putExtra(Constants.EXTRA_CURRENT_DISTANCE, realDistance);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        calculateProgress();

        // Poner en primer plano
        startForeground(1, builder.build());
    }

    void calculateProgress() {
        builder.setContentText("Distance: " + LocationUtils.prettifyDistance(realDistance) );
        int percent = (int) (100 / (startDistance / realDistance));
        progress = 100 - percent;
        if (progress >= 0) {
            builder.setProgress(100, progress, false);
            if (progress >= 95 || (realDistance != 0 && realDistance < 100)) {
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setContentTitle(getString(R.string.we_arrived_to) + " " + cover.getName());
                builder.setContentText(getString(R.string.we_dit_it));
                builder.setProgress(0, 0, false);
            }
        } else {
            builder.setProgress(100, 0, false);
        }
    }

    double calcuteDistance(Location currentLocation, com.blancgrupo.apps.tripguide.data.entity.api.Location destiny) {
        return LocationUtils.measureDoubleDistance(this, currentLocation, destiny.getLat(), destiny.getLng());
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
        realDistance = calcuteDistance(location, cover.getLocation());
        Intent notificationIntent = new Intent(this, SingleTourActivity.class);
        notificationIntent.setAction("android.intent.action.RUN");
        notificationIntent.putExtra(Constants.EXTRA_IS_TOUR_RUNNING, true);
        notificationIntent.putExtra(Constants.EXTRA_CURRENT_POSITION, realDistance);
        notificationIntent.putExtra(Constants.EXTRA_CURRENT_IMAGE_POSITION, position);
        notificationIntent.putExtra(Constants.EXTRA_SINGLE_TOUR_ID, tourId);
        notificationIntent.putExtra(Constants.EXTRA_PROGRESS, progress);
        notificationIntent.putExtra(Constants.EXTRA_CURRENT_DISTANCE, realDistance);
        notificationIntent.putExtra(Constants.EXTRA_START_POSITION, startDistance);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        calculateProgress();
        builder.setContentIntent(pendingIntent);
        // Poner en primer plano
        startForeground(1, builder.build());
        sendBroadcast(notificationIntent);
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
