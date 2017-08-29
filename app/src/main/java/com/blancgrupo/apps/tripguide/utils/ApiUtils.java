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
                mipmap = R.mipmap.marker_hotel;
                break;
            case "park":
                mipmap = R.mipmap.marker_park;
                break;
            case "golf":
                mipmap = R.mipmap.marker_golf;
                break;
            case "point_of_interest":
                mipmap = R.mipmap.marker_interest;
                break;
            case "beach":
                mipmap = R.mipmap.marker_beach;
                break;
            case "restaurant":
            default:
                mipmap = R.mipmap.marker_default;
        }
        Bitmap resource = BitmapFactory.decodeResource(context.getResources(), mipmap);
        Bitmap bitmap = Bitmap.createScaledBitmap(resource, 80, 80, false);
        //return BitmapDescriptorFactory.fromBitmap(bitmap);
        return BitmapDescriptorFactory.defaultMarker();
    }
}

