package com.blancgrupo.apps.tripguide.utils;

import com.blancgrupo.apps.tripguide.MyApplication;

/**
 * Created by root on 8/25/17.
 */

public class Constants {
    public static final String FONT_PATH_REGULAR = "fonts/Montserrat/Montserrat-Regular.ttf";
    public static final String FONT_PATH_MEDIUM = "fonts/Montserrat/Montserrat-Medium.ttf";
    public static final String FONT_PATH_BOLD = "fonts/Montserrat/Montserrat-Bold.ttf";
    public static final String API_BASE_URL_1 =
            "https://maps.googleapis.com/maps/api/";
    public static final String API_URL = "http://138.197.43.190/";
//public static final String API_URL = "http://10.0.0.6/";
    public static final String API_BASE_URL_2 = API_URL + "api/v1/";
    public static final String API_UPLOAD_URL = API_URL + "uploads/";
    public static final String PHOTO_PLACE_API_URL = "https://maps.googleapis.com/maps/api/place/photo";
    public static final String HIGHLIGHT_CATEGORY = "tour";
    public static final String EXTRA_CITY_ID = createExtra("CITY_ID");
    public static final String EXTRA_PLACE_ID = createExtra("PLACE_ID");
    public static final String EXTRA_SEARCH_PLACE_QUERY = createExtra("SEARCH_PLACE_QUERY");
    public static final String EXTRA_PLACE_FOR_MAP = createExtra("PLACE_FOR_MAP");
    public static final String STATUS_OK = "OK";
    public static final String EXTRA_PLACE_GOOGLE_ID = createExtra("PLACE_GOOGLE_ID");
    public static final String EXTRA_IMAGE_URL = createExtra("IMAGE_URL");
    public static final String CURRENT_LOCATION_SP = createSharedPreference("CURRENT_LOCATION");
    public static final int CHOOSE_LOCATION_RC = 100;
    public static final String GETTING_STARTED_SP =  createSharedPreference("GETTING STARTED");
    public static final String EXTRA_PLACE_TOUR_ID = createExtra("PLACE_TOUR_ID");
    public static final String EXTRA_SINGLE_TOUR_ID = createExtra("SINGLE_TOUR_ID");
    public static final String EXTRA_CURRENT_IMAGE_POSITION = createExtra("CURRENT_IMAGE_POSITION");
    public static final String EXTRA_TOTAL = createExtra("TOTAL");
    public static final java.lang.String EXTRA_IS_TOUR_RUNNING = createExtra("IS_TOUR_RUNNING");
    public static final String EXTRA_SINGLE_TOUR_PLACES = createExtra("EXTRA_SINGLE_TOUR_PLACES");
    public static final String EXTRA_CURRENT_DISTANCE = createExtra("CURRENT_DISTANCE");
    public static final String EXTRA_START_POSITION = createExtra("START_POSITION");
    public static final String USER_LOGGED_API_TOKEN_SP = createSharedPreference("USER_LOGGED_API_TOKEN_SP");
    public static final String USER_LOGGED_SP = createSharedPreference("USER_LOGGED");
    public static final String USER_LOGGED_ID_SP = createSharedPreference("USER_LOGGED_ID");
    public static final String USER_LOGGED_TYPE_SP = createSharedPreference("USER_LOGGED_TYPE");
    public static final java.lang.String EXTRA_PLACE_NAME = createExtra("PLACE_NAME");
    public static String EXTRA_CURRENT_POSITION = createExtra("CURRENT_POSITION");
    public static String EXTRA_PROGRESS = createExtra("PROGRESS");


    public static final int XP_FIRST_LEVEL_TOP = 20;
    public static final int XP_SECOND_LEVEL_TOP = 50;
    public static final int XP_THIRD_LEVEL_TOP = 100;


    public static String createExtra(String name) {
        return String.format("%s.EXTRA_%s", MyApplication.packageName, name);
    }

    public static String createSharedPreference(String name) {
        return String.format("%s.SHARED_PREFERENCE_%s", MyApplication.packageName, name);
    }

}
