package com.blancgrupo.apps.tripguide.utils;


import com.blancgrupo.apps.tripguide.data.entity.api.PlaceCover;

public class TextStringUtils {

    public static String formatTitle(String title) {
        String str = title.replace("_", " ");
        return Character.toUpperCase(title.charAt(0)) + str.substring(1);
    }

    public interface PlaceItemActivityListener {
        void onPlaceClick(PlaceCover place);
    }
}
