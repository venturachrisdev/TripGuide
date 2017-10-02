package com.blancgrupo.apps.tripguide.data;

import com.blancgrupo.apps.tripguide.data.entity.api.Place;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceDescriptionWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesWrapper;
import com.blancgrupo.apps.tripguide.data.service.ApiPlaceService;
import com.blancgrupo.apps.tripguide.domain.repository.PlaceRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by root on 8/18/17.
 */

public class ApiPlaceRepository implements PlaceRepository {
    ApiPlaceService apiPlaceService;

    @Inject
    public ApiPlaceRepository(ApiPlaceService apiPlaceService) {
        this.apiPlaceService = apiPlaceService;
    }

    @Override
    public Observable<PlacesWrapper> getPlaces() {
        return apiPlaceService.getPlaces();
    }

    @Override
    public Observable<PlaceWrapper> getSinglePlace(String placeId, String apiToken) {
        return apiPlaceService.getSinglePlace(placeId, apiToken);
    }
    @Override
    public Observable<PlaceDescriptionWrapper> getPlaceDescription(String placeId, String lang) {
        return apiPlaceService.getPlaceDescription(placeId, lang);
    }

    @Override
    public Observable<PlacesWrapper> getMyFavorites(String apiToken) {
        return apiPlaceService.getMyFavorites(apiToken);
    }

    @Override
    public Observable<String> addToMyFavorites(String apiToken, Place place) {
        return apiPlaceService.addToMyFavorites(apiToken, place);
    }

}
