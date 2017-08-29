package com.blancgrupo.apps.tripguide.data.service;

import com.blancgrupo.apps.tripguide.data.entity.googleplace.PlaceDetailWrapper;
import com.blancgrupo.apps.tripguide.data.entity.googleplace.PlaceSearchWrapper;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by root on 8/15/17.
 */

public interface GooglePlaceService {
    @GET("place/details/json")
    Observable<PlaceDetailWrapper> getPlaceDetail(@Query("placeid") String placeId,
                                                  @Query("language") String lang);

    @GET("place/textsearch/json")
    Observable<PlaceSearchWrapper> searchPlaceByType(@Query("query") String query,
                                                     @Query("type") String type,
                                                     @Query("language") String lang);

    @GET("place/nearbysearch/json")
    Observable<PlaceSearchWrapper> searchNearBy(@Query("location") String location,
                                         @Query("language") String language,
                                         @Query("type") String type,
                                         @Query("radius") int radius);
}
