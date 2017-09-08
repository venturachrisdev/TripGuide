package com.blancgrupo.apps.tripguide.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.graphics.BitmapCompat;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by root on 8/18/17.
 */

public class ApiUtils {


    public static String getPlacePhotoUrl(MyApplication app, String reference, int maxwidth) {
        return Constants.PHOTO_PLACE_API_URL
                + "?maxwidth=" + maxwidth
                + "&photoreference=" + reference
                + "&key=" + app.getApiKey();
    }

    public static BitmapDescriptor drawMarkerByType(Context context,  String type) {
        int mipmap;
        switch (type) {
            case "hotel":
            case "lodging":
                mipmap = R.mipmap.hotels;
                break;
            case "park":
                mipmap = R.mipmap.parks;
                break;
            case "golf":
                mipmap = R.mipmap.games;
                break;
            case "point_of_interest":
                mipmap = R.mipmap.meetups;
                break;
            case "beach":
                mipmap = R.mipmap.travel;
                break;
            case "cafe":
                mipmap = R.mipmap.cafe;
                break;
            case "restaurant":
                mipmap = R.mipmap.restaurants;
                break;
            default:
                mipmap = R.mipmap.default_marker;
        }
        Bitmap resource = BitmapFactory.decodeResource(context.getResources(), mipmap);
        //Bitmap bitmap = Bitmap.createScaledBitmap(resource, 80, 80, false);
        Bitmap bitmap = resource;
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public interface OnBoardingListener {
        void onBoardingFinished();
    }

    public interface ActionCallback {
        void call();
    }
}

