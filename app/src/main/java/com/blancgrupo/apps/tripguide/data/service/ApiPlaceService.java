package com.blancgrupo.apps.tripguide.data.service;

import com.blancgrupo.apps.tripguide.data.entity.api.CitiesWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.CityWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesWrapper;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by root on 8/18/17.
 */

public interface ApiPlaceService {
    @GET("places?json")
    Observable<PlacesWrapper> getPlaces();

    @GET("places/{place_id}?json")
    Observable<PlaceWrapper> getSinglePlace(@Path("place_id") String placeId);

    @GET("cities?json")
    Observable<CitiesWrapper> getCities();

    @GET("cities/{city_id}?json")
    Observable<CityWrapper> getSingleCity(@Path("city_id") String cityId);

    @GET("tours?json")
    Observable<PlacesWrapper> getTours();

    @GET("tours/{tour_id}?json")
    Observable<PlaceWrapper> getSingleTour(@Path("tour_id") String tourId);
}
