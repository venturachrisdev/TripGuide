package com.blancgrupo.apps.tripguide.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.TourCover;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

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
//            case "hotel":
//            case "lodging":
//                mipmap = R.mipmap.hotels;
//                break;
//            case "golf":
//            mipmap = R.mipmap.games;
//            break;
            case "park":
            case "point_of_interest":
                mipmap = R.mipmap.park_marker;
                break;
            case "beach":
                mipmap = R.mipmap.beach_marker;
                break;
            case "cafe":
                mipmap = R.mipmap.cafe_marker;
                break;
            case "restaurant":
                mipmap = R.mipmap.restaurant_marker;
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

    public interface SingleTourListener {
        void onSingleTourClick(TourCover placeCover, String imageUrl);
    }


    public static String getTourTime(Context context, int minutes) {
        if (minutes <= 0) {
            return context.getString(R.string.n_a);
        }

        if (minutes >= 60) { /* more than an hour */
            return minutes/60 + "h " + minutes%60 + "min";
        }
        return minutes + " min";

    }

    public static String getTourDistance(Context context, int meters) {
        if (meters <= 0) {
            return context.getString(R.string.n_a);
        }
        if (meters >= 1000) { /* more than a kilometer */
            if (meters%1000 == 0) {
                return meters/1000 + "km";
            }
            return meters/1000 + "." + meters%1000 + "km";
        }
        return meters + "m";
    }
}

