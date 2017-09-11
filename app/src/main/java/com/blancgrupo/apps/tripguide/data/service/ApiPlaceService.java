package com.blancgrupo.apps.tripguide.data.service;

import com.blancgrupo.apps.tripguide.data.entity.api.CitiesWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.CityWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.ParentTourWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceDescriptionWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesCoverWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.TourWrapper;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by root on 8/18/17.
 */

public interface ApiPlaceService {

    @GET("where")
    Observable<String> getCurrentCity(@Query("lat") String lat, @Query("lng") String lng);

    @GET("places?json")
    Observable<PlacesWrapper> getPlaces();

    @GET("places/{place_id}?json")
    Observable<PlaceWrapper> getSinglePlace(@Path("place_id") String placeId);

    @GET("cities?json")
    Observable<CitiesWrapper> getCities();

    @GET("cities/{city_id}?json")
    Observable<CityWrapper> getSingleCity(@Path("city_id") String cityId);

    @GET("tours?json")
    Observable<PlacesCoverWrapper> getTours();

    @GET("tours/single/{place_id}?json")
    Observable<ParentTourWrapper> getSinglePlaceTour(@Path("place_id") String placeId);

    @GET("tours/all/{tour_id}?json")
    Observable<TourWrapper> getSingleTour(@Path("tour_id") String tourId);

    @GET("info/{place_id}/lang/{lang}?json")
    Observable<PlaceDescriptionWrapper>
    getPlaceDescription(@Path("place_id") String place_id,
                        @Path("lang") String lang);
}
