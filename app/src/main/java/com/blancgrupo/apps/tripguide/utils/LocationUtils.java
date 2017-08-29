package com.blancgrupo.apps.tripguide.utils;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.blancgrupo.apps.tripguide.R;

import java.util.Locale;

/**
 * Created by root on 8/28/17.
 */

@SuppressWarnings("MissingPermission")
public class LocationUtils {
    public static final int PERMISSION_MAP_REQUEST_CODE = 100;
    public static final int PERMISSION_LOCATION_REQUEST_CODE = 101;
    public static final int PERMISSION_ENABLE_GPS_REQUEST_CODE = 102;

    public static boolean checkForPermissionOrRequest(Context context) {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((AppCompatActivity) context, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            }, PERMISSION_LOCATION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    public static void showDialogEnableGps(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.location_services_are_off)
                .setMessage(R.string.location_services_why)
                .setPositiveButton(R.string.action_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((AppCompatActivity) context).startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                PERMISSION_ENABLE_GPS_REQUEST_CODE);

                    }
                })
                .setNegativeButton(R.string.not_now, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showAreYouSureGps(context);
                    }
                });

        builder.create().show();
    }

    public static void showAreYouSureLocation(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.are_you_sure)
                .setMessage(R.string.location_why)
                .setPositiveButton(R.string.update_permissions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((AppCompatActivity) context).startActivityForResult(new Intent(Settings.ACTION_APPLICATION_SETTINGS,
                                        Uri.parse("com.blancgrupo.apps.tripguide")),
                                PERMISSION_ENABLE_GPS_REQUEST_CODE);

                    }
                })
                .setNegativeButton(R.string.not_now, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        builder.create().show();
    }

    public static void showAreYouSureGps(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.are_you_sure)
                .setMessage(R.string.location_why)
                .setPositiveButton(R.string.update_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((AppCompatActivity) context).startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                PERMISSION_ENABLE_GPS_REQUEST_CODE);

                    }
                })
                .setNegativeButton(R.string.not_now, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        builder.create().show();
    }

    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean gpsEnabledOrShowDialog(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            showDialogEnableGps(context);
            return false;
        }
    }

    public static Location getCurrentLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Location currentLoc = null;


        if (isGpsEnabled || isNetworkEnabled) {
            currentLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (currentLoc == null && isNetworkEnabled) {
                currentLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
        return currentLoc;
    }

    public static void requestLocationUpdates(Context context, LocationListener locationListener) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1, 1, locationListener);
    }

    public static String measureDistance(Location currentLocation, double lat, double lng) {
        android.location.Location where = new android.location.Location("where");
        where.setLatitude(lat);
        where.setLongitude(lng);
        float dis = currentLocation.distanceTo(where);
        if (dis >= 1000) {
            return String.format(Locale.getDefault(), "%.1f km", dis / 1000);
        } else {
            return String.format(Locale.getDefault(), "%d m", (int)dis);
        }
    }
}