package com.blancgrupo.apps.tripguide.data;

import com.blancgrupo.apps.tripguide.data.entity.api.ParentTourWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesCoverWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.TourWrapper;
import com.blancgrupo.apps.tripguide.data.service.ApiPlaceService;
import com.blancgrupo.apps.tripguide.domain.repository.TourRepository;

import io.reactivex.Observable;

/**
 * Created by root on 9/6/17.
 */

public class ApiTourRepository implements TourRepository {
    ApiPlaceService apiPlaceService;

    public ApiTourRepository(ApiPlaceService apiPlaceService) {
        this.apiPlaceService = apiPlaceService;
    }

    @Override
    public Observable<PlacesWrapper> getTours() {
        return apiPlaceService.getTours();
    }

    @Override
    public Observable<ParentTourWrapper> getSinglePlaceTour(String placeId) {
        return apiPlaceService.getSinglePlaceTour(placeId);
    }

    @Override
    public Observable<TourWrapper> getSingleTour(String tourId) {
        return apiPlaceService.getSingleTour(tourId);
    }
}
