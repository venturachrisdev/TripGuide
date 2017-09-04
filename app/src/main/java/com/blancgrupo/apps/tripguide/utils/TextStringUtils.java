package com.blancgrupo.apps.tripguide.utils;


import com.blancgrupo.apps.tripguide.data.entity.api.PlaceCover;

import java.util.List;

public class TextStringUtils {

    public static String formatTitle(String title) {
        if (title != null) {
            String str = title.replace("_", " ");
            return Character.toUpperCase(title.charAt(0)) + str.substring(1);
        }
        return "";
    }

    public static String arrayToString(List<String> array) {
        if (array != null && array.size() > 0) {
            return array.toString()
                    .replace("[", "")
                    .replace("]", "")
                    .replace(", ", "\n");
        }
        return "";
    }

    public static String shortText(String text) {
        if (text.length() >= 270) {
            return text.substring(0, 270) + "...";
        }
        return text;
    }

    public interface PlaceItemActivityListener {
        void onPlaceClick(PlaceCover place);
    }
}
