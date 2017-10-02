package com.blancgrupo.apps.tripguide.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceTypesCover;
import com.blancgrupo.apps.tripguide.data.entity.api.TourCover;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

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
//        Bitmap bitmap = resource;
        return BitmapDescriptorFactory.fromBitmap(resource);
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

    public interface RunningPlaceListener {
        void onRunningPlaceClick(PlaceTypesCover cover);
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
    public interface AuthFragment {
        void handleSignInResult(GoogleSignInResult result);
        void handleSignOut(Status status);
        boolean isUserSaved();
    }


    public static File resizeImage(String id, File file, int requiredSize, int quality){
        try {
            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = requiredSize;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);
            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            int orientation = 1;
            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.d("Tripguide", "Imagen Orientation: " + orientation);
            inputStream.close();
            if (orientation > 2) {
                boolean xy = false;
                int degrees = 0;
                Matrix matrix = new Matrix();
                switch (orientation) {
                    case 3:
                    case 4:
                        degrees = 180;
                        break;
                    case 5:
                    case 6:
                        degrees = 90;
                        xy = true;
                        break;
                    case 7:
                    case 8:
                        degrees = 270;
                        break;
                }
                matrix.postRotate(degrees);
                int square = selectedBitmap.getWidth();
                if (xy) {
                    square = selectedBitmap.getHeight();
                }
                selectedBitmap = Bitmap.createBitmap(selectedBitmap, 0, 0, square, square,
                        matrix, true);
            }

            File newFile = new File("/storage/emulated/0/DCIM/tripguide-profile-" + id + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(newFile);
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            return newFile;
        } catch (Exception e) {
            Log.d("Tripguide", e.getMessage());
            e.printStackTrace();
            return file;
        }
    }
}

