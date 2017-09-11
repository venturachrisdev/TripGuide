package com.blancgrupo.apps.tripguide.domain.repository;

import com.blancgrupo.apps.tripguide.data.entity.api.ParentTourWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesCoverWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.TourWrapper;

import io.reactivex.Observable;

/**
 * Created by root on 9/6/17.
 */

public interface TourRepository {
    Observable<PlacesWrapper> getTours();
    Observable<ParentTourWrapper> getSinglePlaceTour(String placeId);
    Observable<TourWrapper> getSingleTour(String tourId);
}
